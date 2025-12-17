package com.litevar.ieval.llm.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.EventBus;
import com.litevar.ieval.llm.chat.message.ChatResponse;
import com.litevar.ieval.llm.chat.message.FunctionCall;
import com.litevar.ieval.llm.memory.ChatMemory;
import com.litevar.ieval.llm.runner.TestCaseInput;
import com.litevar.ieval.llm.tools.FunctionInfo;
import com.litevar.ieval.llm.tools.invoker.FunctionCallExecutor;
import com.litevar.ieval.llm.utils.ImageUtil;
import com.litevar.ieval.llm.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Author  action
 * @Date  2025/10/14 15:26
 * @company litevar
 **/
public class Agent {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //agent id
    protected Integer id;
    //agent名称
    protected String name;
    //提示词
    protected String prompt;
    protected JSONArray tools;
    protected Map<String, FunctionInfo> functionInfoMap;
    private String sessionId;
    protected boolean isFirst;
    protected boolean isSingle;
    //0：普通，1：分发，2：反思
    protected int kind;
    private int iterations;
    //聊天参数
    private ChatConfig chatConfig;
    protected ChatOptions chatOptions;
    private LlmHttpClient openAiClient;
    protected ChatMemory memory;
    //产出
    //tokens(流量统计)
    private JSONArray costList;

    //输出通道
    private EventBus outputBus;
    //子agent: 普通
    private List<Agent> childrenList;
    //反思agent
    private Agent reflectAgent;
    private int reflectionCnt;
    private boolean isContinuousChat = true;
    private JSONArray llmOutputArray;

    public Agent(Integer id, String name, String prompt, ChatConfig chatConfig, ChatOptions chatOptions, ChatMemory memory, boolean isFirst, String sessionId) {
        this.id = id;
        this.name = name;
        this.prompt = chatOptions.isNoThink()?(prompt+"\n/no_think"):prompt;
        this.chatConfig = chatConfig;
        this.chatOptions = chatOptions;
        this.memory = memory;
        this.isFirst = isFirst;
        this.sessionId = sessionId;
        this.costList = new JSONArray();
        if(!this.chatOptions.isStream()) {
            this.openAiClient = new OpenAiClient();
        }
    }
    public String chat(TestCaseInput testCase) {
        if(llmOutputArray==null) {
            llmOutputArray = new JSONArray();
        }
        else {
            llmOutputArray.clear();
        }
        if(!isContinuousChat && memory!=null) {
            memory.clearContext();
        }
        if(iterations>0) {
            iterations = 0;
        }
        //反思agent无须保持上下文
        if(memory!=null && kind==2) {
            memory.clearContext();
        }
        if(memory!=null && memory.getContextSize()==0) {
            JSONObject system = new JSONObject();
            system.put("role", "system");
            system.put("content", prompt);
            memory.addMessage(system);
        }
        logger.info("running {} : {}", id, name);
        String humanCmd = testCase.getCmd();
        String expectation = testCase.getExpectation();

        //input
        /*
        JSONObject user = new JSONObject();
        user.put("role", "user");
        user.put("content", humanCmd);
        */
        JSONObject user = new JSONObject();
        if(testCase.getPicture()==null || StringUtils.isBlank(testCase.getPicture())) {
            user.put("role", "user");
            user.put("content", humanCmd);
        }
        else {
            String[] pictures = null;
            if(StringUtils.contains(testCase.getPicture(), ",")) {
                pictures = StringUtils.split(testCase.getPicture(), ",");
            }
            else {
                pictures = new String[]{testCase.getPicture()};
            }
            user.put("role", "user");
            JSONArray contentArray = new JSONArray();
            for(String picture : pictures) {
                JSONObject imageObject = new JSONObject();
                imageObject.put("type", "image_url");
                if(picture.startsWith("http")) {
                    imageObject.put("image_url", JSONObject.parseObject("{\"url\":\""+picture+"\"}"));
                }
                else {
                    imageObject.put("image_url", JSONObject.parseObject("{\"url\":\""+ ImageUtil.toBase64(picture)+"\"}"));
                }
                contentArray.add(imageObject);
            }
            JSONObject humanObject = new JSONObject();
            humanObject.put("type", "text");
            humanObject.put("text", humanCmd);
            contentArray.add(humanObject);
            user.put("content", contentArray);
        }

        memory.addMessage(user);
        logger.info("cmd: {}", humanCmd);
        dealChat(humanCmd);
        boolean correct1 = memory.correctContext("remove");
        String content = memory.getLastContent();
        if(kind!=2 && !correct1 && StringUtils.isNoneBlank(content)) {
            //deal reflection
            if(reflectAgent!=null) {
                dealReflection(testCase, content);
                boolean correct2 = memory.correctContext("remove");
                if(!correct2) {
                    content = memory.getLastContent();
                }
            }
            //deal children(normal)
            if(childrenList!=null && !childrenList.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(content + "; ");
                for(Agent child : childrenList) {
                    String childContent = child.chat(new TestCaseInput(content, null));
                    if(StringUtils.isNoneBlank(childContent)) {
                        stringBuffer.append(childContent + "; ");
                    }
                }
                if(!stringBuffer.isEmpty()) {
                    content = stringBuffer.substring(0, stringBuffer.length()-1);
                }
            }
        }
        //deal cost
        if(!costList.isEmpty()) {
            //agent-context
            if(kind<2 && isContinuousChat) {
                memory.persistContext(sessionId);
            }
        }
        //test suite
        if(outputBus!=null && llmOutputArray!=null && !llmOutputArray.isEmpty()) {
            JSONObject outputObject = new JSONObject();
            outputObject.put("humanCmd", humanCmd);
            if(StringUtils.isNoneBlank(testCase.getPicture())) {
                outputObject.put("picture", testCase.getPicture());
            }
            if(StringUtils.isNoneBlank(expectation)) {
                outputObject.put("expectation", expectation);
            }
            outputObject.put("testCaseId", testCase.getRowNum());
            outputObject.put("llmOutput", llmOutputArray);
            outputBus.post(new AgentEvent(this.id, "a-"+this.id, "llm-output", outputObject.toJSONString()));
        }
        return content;
    }
    private void dealChat(String rawCmd) {
        if(reflectionCnt>0) {
            iterations = 0;
        }
        iterations++;
        logger.info("iterations: {}", iterations);
        ChatResponse chatResponse = null;
        if(chatOptions.isStream()) {
            OpenAiSseClient sseClient = new OpenAiSseClient();
            sseClient.request(chatConfig, chatOptions, memory.getMessages(rawCmd), tools);
            while(!sseClient.isDeltaFinished() && sseClient.getCheckTimes()<1200) {
                TimeUtil.waitFor(500);
            }
            chatResponse = sseClient.getChatResponse();
        }
        else {
            chatResponse = openAiClient.request(chatConfig, chatOptions, memory.getMessages(rawCmd), tools);
        }
        String content = chatResponse.getContent();
        List<FunctionCall> toolCalls = chatResponse.getFunctionCalls();
        if(chatResponse.getAnomalous()!=null && StringUtils.isNoneBlank(chatResponse.getAnomalous().toString())) {
            logger.warn("anomalous: {}", chatResponse.getAnomalous());
        }
        if(chatResponse.getUsage()!=null && chatResponse.getUsage().containsKey("totalTokens")) {
            costList.add(chatResponse.getUsage());
        }
        if(toolCalls!=null && !toolCalls.isEmpty() && functionInfoMap!=null) {
            if(dealFunctions(toolCalls, functionInfoMap, rawCmd, chatResponse)) {
                logger.info("summary chat>>>");
                ChatResponse chatResponse2 = null;
                if(chatOptions.isStream()) {
                    OpenAiSseClient sseClient2 = new OpenAiSseClient();
                    sseClient2.request(chatConfig, chatOptions, memory.getMessages(rawCmd), tools);
                    while(!sseClient2.isDeltaFinished() && sseClient2.getCheckTimes()<1200) {
                        TimeUtil.waitFor(500);
                    }
                    chatResponse2 = sseClient2.getChatResponse();
                }
                else {
                    chatResponse2 = openAiClient.request(chatConfig, chatOptions, memory.getMessages(rawCmd), tools);
                }
                String content2 = chatResponse2.getContent();
                List<FunctionCall> toolCalls2 = chatResponse2.getFunctionCalls();
                if(chatResponse2.getAnomalous()!=null && StringUtils.isNoneBlank(chatResponse2.getAnomalous().toString())) {
                    logger.warn("anomalous: {}", chatResponse2.getAnomalous());
                }
                if(chatResponse2.getUsage()!=null && chatResponse2.getUsage().containsKey("totalTokens")) {
                    costList.add(chatResponse2.getUsage());
                }
                if(toolCalls2!=null && !toolCalls2.isEmpty()) {
                    dealFunctions(toolCalls2, functionInfoMap, rawCmd, chatResponse2);
                    if(iterations<20) {
                        dealChat(rawCmd);
                    }
                    else {
                        logger.warn("iterations large than 20, stop.");
                    }
                }
                else if(StringUtils.isNoneBlank(content2)) {
                    String target = content2;
                    JSONObject assistant = new JSONObject();
                    assistant.put("role", "assistant");
                    if(StringUtils.contains(content2, "<think>") && StringUtils.contains(content2, "</think>")) {
                        target = StringUtils.substring(content2, content2.lastIndexOf("think>")+6);
                    }
                    else if(StringUtils.contains(content2, "</think>")) {
                        target = StringUtils.substring(content2, content2.lastIndexOf("think>")+6);
                    }
                    assistant.put("content", target);
                    memory.addMessage(assistant);
                    logger.info("agent: {}, name: {}, gpt response text2: {}", this.id, this.name, content2);
                    if(content2!=null && !"<think></think>".equals(content2.trim()) && !chatOptions.isStream()) {
                        logger.info("ai-end: {}", content2.trim());
                    }
                    llmOutputArray.add(collectContent(chatResponse2, memory.getContextSize()));
                }
            }
        }
        else if(StringUtils.isNoneBlank(content)) {
            String target = content;
            JSONObject assistant = new JSONObject();
            assistant.put("role", "assistant");
            if(StringUtils.contains(content, "<think>") && StringUtils.contains(content, "</think>")) {
                target = StringUtils.substring(content, content.lastIndexOf("think>")+6);
            }
            else if(StringUtils.contains(content, "</think>")) {
                target = StringUtils.substring(content, content.lastIndexOf("think>")+6);
            }
            assistant.put("content", target);
            memory.addMessage(assistant);
            logger.info("agent: {}, name: {}, gpt response text: {}", this.id, this.name, content);
            if(content!=null && !"<think></think>".equals(content.trim()) && !chatOptions.isStream()) {
                logger.info("ai-end: {}", content.trim());
            }
            llmOutputArray.add(collectContent(chatResponse, memory.getContextSize()));
        }
    }

    private boolean dealFunctions(List<FunctionCall> functionCalls, Map<String, FunctionInfo> functionInfoMap, String rawCmd, ChatResponse chatResponse) {
        logger.info(">>>functionCalling");
        boolean isCalled = false;
        FunctionCallExecutor caller = new FunctionCallExecutor();
        JSONArray toolCallsArray = new JSONArray();
        JSONArray executeCallArray = new JSONArray();
        StringBuffer unknownFunctions = new StringBuffer();
        for(FunctionCall functionCall : functionCalls) {
            if(functionInfoMap.containsKey(functionCall.getMethod())) {
                logger.debug("Parallel method: {}", functionCall.getMethod());
                toolCallsArray.add(functionCall.getOriginal());
                //toolMode 0, 1放行
                executeCallArray.add(this.callMethod(functionCall.getToolCallId(), functionCall.getMethod(), caller, functionCall.getArguments(), functionInfoMap.get(functionCall.getMethod()), rawCmd));
                isCalled = true;
            }
            else {
                logger.error("invalid method: {} hasn't registered", functionCall.getMethod());
                unknownFunctions.append(functionCall.getMethod()).append(",");
            }
        }
        if(!toolCallsArray.isEmpty() && !executeCallArray.isEmpty()) {
            JSONObject contextObject = new JSONObject();
            contextObject.put("role", "assistant");
            contextObject.put("content", "");
            contextObject.put("tool_calls", toolCallsArray);
            JSONArray mergeArray = new JSONArray();
            mergeArray.add(contextObject);
            mergeArray.addAll(executeCallArray);
            memory.addMessages(mergeArray);
            logger.info("chat context and memory updated");
        }
        else {
            logger.warn("No toolCall happen");
        }
        if(!unknownFunctions.isEmpty()) {
            String unknownFunctionsStr = unknownFunctions.substring(0, unknownFunctions.length()-1);
            JSONObject contextObject = new JSONObject();
            contextObject.put("role", "assistant");
            contextObject.put("content", "Unknown tools: "+unknownFunctionsStr+", please return the correct tool from the list of registered methods");
            memory.addMessage(contextObject);
        }
        logger.debug("isCalled?: {}", isCalled);
        llmOutputArray.add(collectFunctionCalls(executeCallArray, unknownFunctions.toString(), chatResponse, memory.getContextSize()));
        return isCalled;
    }
    /**
     * funcCallId: 方法调用id(由大模型返回)
     * method: 调用的方法名
     * executor: 方法调用执行器
     * messages：上下文信息
     * argsObject: 参数对象
     * functionInfo: 方法执行信息(包括api地址，所用方法等)
     * */
    private JSONObject callMethod(String funcCallId, String method, FunctionCallExecutor executor, JSONObject argsObject, FunctionInfo functionInfo, String rawCmd) {
        //方法调用信息，需加入上下文
        JSONObject msgObj = new JSONObject();
        msgObj.put("tool_call_id", funcCallId);
        msgObj.put("role", "tool");
        msgObj.put("name", method);
        logger.info("call method: {}, url: {}, args: {}", method, functionInfo.getServer(), argsObject);
        Object result = executor.execute(functionInfo, method, argsObject);
        msgObj.put("content", result.toString());
        JSONObject tmpOutput = new JSONObject();
        tmpOutput.put("toolCallId", funcCallId);
        tmpOutput.put("name", method);
        tmpOutput.put("params", argsObject);
        tmpOutput.put("result", result);
        logger.info("ai: {}", tmpOutput);
        return msgObj;
    }
    public void dealReflection(TestCaseInput testCase, String outputCmd) {
        JSONObject input = new JSONObject();
        input.put("rawInput", testCase.getCmd());
        input.put("rawOutput", outputCmd);
        String content = reflectAgent.chat(new TestCaseInput(input.toJSONString(), null));
        logger.info("reflectAgent content: {}", content);
        if(StringUtils.isNoneBlank(content) && content.contains("score") && content.contains("information")) {
            content = content.trim();
            String reflectCmd = null;
            int score = 0;
            if(StringUtils.startsWith(content, "{") && StringUtils.endsWith(content, "}")) {
                JSONObject contentObject = JSONObject.parseObject(content);
                if(contentObject.containsKey("score")&&contentObject.containsKey("information")&&contentObject.getInteger("score")!=null&&contentObject.getString("information")!=null) {
                    reflectCmd = contentObject.getString("information").trim();
                    score = contentObject.getInteger("score");
                }
            }
            else if(StringUtils.startsWith(content, "```json")&&StringUtils.endsWith(content, "```")) {
                String temp = StringUtils.replace(content, "```json", "");
                JSONObject contentObject = JSONObject.parseObject(StringUtils.replace(temp, "```", ""));
                if(contentObject.containsKey("score")&&contentObject.containsKey("information")&&contentObject.getInteger("score")!=null&&contentObject.getString("information")!=null) {
                    reflectCmd = contentObject.getString("information").trim();
                    score = contentObject.getInteger("score");
                }
            }
            else {
                logger.error("must be json format, and contains score and information column");
            }
            if(score>0 && score<8 && StringUtils.isNoneBlank(reflectCmd) && reflectionCnt<9) {
                reflectionCnt++;
                logger.info("reflect cmd: {} at times: {}", reflectCmd, reflectionCnt);
                JSONObject userObj = new JSONObject();
                userObj.put("role", "user");
                userObj.put("content", reflectCmd);
                this.memory.addMessage(userObj);
                this.dealChat(reflectCmd);
            }
            else {
                logger.info("exit reflection, score: {}, times: {}", score, reflectionCnt);
            }
        }
    }
    public Integer getId() {
        return id;
    }
    public void updatePrompt(String add) {
        if(StringUtils.isNoneBlank(prompt) && StringUtils.isNoneBlank(add)) {
            prompt = prompt + "\n" + add;
        }
    }
    public void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }
    public void setOutputBus(EventBus outputBus) {
        this.outputBus = outputBus;
    }
    public void setChildrenList(List<Agent> childrenList) {
        this.childrenList = childrenList;
    }
    public void setReflectAgent(Agent reflectAgent) {
        this.reflectAgent = reflectAgent;
    }
    public void setTools(JSONArray tools, Map<String, FunctionInfo>functionInfoMap) {
        this.tools = tools;
        this.functionInfoMap = functionInfoMap;
    }
    public void setIsContinuousChat(boolean isContinuousChat) {
        this.isContinuousChat = isContinuousChat;
    }
    private JSONObject collectFunctionCalls(JSONArray executeCallArray, String unknownFunctions, ChatResponse chatResponse, int contextSize) {
        List<FunctionCall> functionCalls = chatResponse.getFunctionCalls();
        JSONObject result = new JSONObject();
        JSONArray correctFunctions = new JSONArray();
        for(FunctionCall functionCall : functionCalls) {
            JSONObject correctFunction = new JSONObject();
            correctFunction.put("name", functionCall.getMethod());
            correctFunction.put("arguments", functionCall.getArguments());
            correctFunctions.add(correctFunction);
        }
        JSONArray executedFunctions = new JSONArray();
        for(int i=0; i<executeCallArray.size(); i++) {
            JSONObject executeCall = executeCallArray.getJSONObject(i);
            JSONObject executedFunction = new JSONObject();
            executedFunction.put("name", executeCall.getString("name"));
            executedFunction.put("result", getShortContent(executeCall.getString("content"),200));
            executedFunctions.add(executedFunction);
        }
        if(chatResponse.getOriginalFunctions()!=null) {
            result.put("originalFunctions", chatResponse.getOriginalFunctions());
        }
        result.put("correctFunctions", correctFunctions);
        result.put("executedFunctions", executedFunctions);
        result.put("unknownFunctions", unknownFunctions);
        if(chatResponse.getWrongArgFunctionCalls()!=null) {
            result.put("wrongArgFunctions", chatResponse.getWrongArgFunctionCalls());
        }
        result.put("firstToken", chatResponse.getFirstTokenTime());
        if(chatResponse.getUsage()!=null) {
            result.put("completionTokens", chatResponse.getUsage().getInteger("completionTokens"));
            result.put("promptTokens", chatResponse.getUsage().getInteger("promptTokens"));
            result.put("model", chatResponse.getUsage().getString("model"));
        }
        result.put("duration", chatResponse.getDuration());
        result.put("tokenPerSecond", chatResponse.getTokenPerSecond());
        result.put("contextSize", contextSize);
        return result;
    }
    private String getShortContent(String content, int maxLength) {
        if(content!=null) {
            if(content.length()>maxLength) {
                return content.substring(0, maxLength) + "...此处省略" + (content.length()-maxLength);
            }
        }
        return content;
    }
    private JSONObject collectContent(ChatResponse chatResponse, int contextSize) {
        JSONObject result = new JSONObject();
        result.put("text", getShortContent(chatResponse.getContent(), 600));
        result.put("firstToken", 0);
        if(chatResponse.getUsage()!=null) {
            result.put("completionTokens", chatResponse.getUsage().getInteger("completionTokens"));
            result.put("promptTokens", chatResponse.getUsage().getInteger("promptTokens"));
            result.put("model", chatResponse.getUsage().getString("model"));
        }
        result.put("duration", chatResponse.getDuration());
        result.put("tokenPerSecond", chatResponse.getTokenPerSecond());
        result.put("contextSize", contextSize);
        return result;
    }
}
