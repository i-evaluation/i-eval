package com.litevar.ieval.llm.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.chat.message.ChatResponse;
import com.litevar.ieval.llm.chat.message.FunctionCall;
import com.litevar.ieval.llm.chat.message.ToolCalls;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author  action
 * @Date  2025/10/14 16:50 
 * @company litevar
 **/
public class OpenAiSseClient {
    private Logger logger = LoggerFactory.getLogger(OpenAiSseClient.class);
    private boolean deltaFinished;
    //[{"index":0,"id":"681641722","type":"function","function":{"name":"date-time_GET","arguments":""}}]
    private JSONArray toolCallsArray;
    private int currentToolCall = 0;

    //请求总的text回复
    private StringBuffer contents = new StringBuffer();
    private long start;
    private JSONObject usage;
    private String anomalous;
    private int checkTimes;
    private int promptTokens;
    private long firstTokenTime;
    private long duration;
    private int completionTokens;
    private String model;

    public OpenAiSseClient() {
    }

    public void request(ChatConfig config, ChatOptions options, JSONArray messages, JSONArray tools) {
        completionTokens = 0;
        logger.info("endpoint: {}", config.getEndpoint());
        promptTokens = messages.toJSONString().length();
        if(tools!=null && !tools.isEmpty()) {
            toolCallsArray = new JSONArray();
            //logger.info("tools: {}", tools);
        }
        model = config.getModel();
        JSONObject sendPayloadObj = new JSONObject();
        sendPayloadObj.put("model", config.getModel());
        sendPayloadObj.put("messages", messages);
        sendPayloadObj.put("stream", true);
        sendPayloadObj.put("temperature", options.getTemperature());
        sendPayloadObj.put("max_tokens", options.getMaxTokens());
        sendPayloadObj.put("seed", options.getSeed());
        if(tools!=null) {
            //sendPayloadObj.put("tool_choice", "required");
            sendPayloadObj.put("tool_choice", "auto");
            sendPayloadObj.put("tools", tools);
        }
        if("json_object".equals(options.getResponseFormat())) {
            sendPayloadObj.put("response_format", JSONObject.parseObject("{\"type\": \"json_object\"}"));
        }
        if(!messages.toJSONString().contains("data:image/jpeg;base64")) {
            logger.info("sendBody: {}", sendPayloadObj);
        }
        String url = "/v1/chat/completions";
        if(config.getModel().startsWith("glm-")) url = "/api/paas/v4/chat/completions";
        Request request = new Request.Builder()
                .url(config.getEndpoint()+url)
                //.url(config.getEndpoint()+"/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + config.getApiKey())
                .addHeader("Accept", "text/event-stream") // 指定接受SSE格式的数据
                .post(RequestBody.create(sendPayloadObj.toJSONString(), MediaType.parse("application/json")))
                .build();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(180, TimeUnit.SECONDS).build();
        start = System.currentTimeMillis();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("onFailure: ", e);
                anomalous = "error:"+e.getMessage();
                deltaFinished = true;
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    anomalous = "error:"+response.code() + ", "+response.message();
                    logger.warn("Unexpected code: {}, msg: {}", response.code(), response.message());
                    deltaFinished = true;
                    response.close();
                }
                else {
                    // 处理响应体
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            BufferedReader reader = new BufferedReader(responseBody.charStream());
                            String line;
                            int lineNo = 0;
                            while ((line = reader.readLine()) != null) {
                                //logger.info(line);
                                if(lineNo==0) {
                                    firstTokenTime = System.currentTimeMillis()-start;
                                    logger.info("1st token: {}", firstTokenTime);
                                    lineNo = 1;
                                }
                                if (StringUtils.isNoneBlank(line) && StringUtils.startsWith(line, "data: {") && StringUtils.endsWith(line, "}")) {
                                    dealLineDelta(JSONObject.parseObject(StringUtils.substring(line, 6)), config.getModel());
                                }
                                else if(StringUtils.isNoneBlank(line) && StringUtils.startsWith(line, "{") && StringUtils.endsWith(line, "}")) {
                                    dealLineDelta(JSONObject.parseObject(line), config.getModel());
                                }
                                else if("data: [DONE]".equals(line)){
                                    if(!deltaFinished) {
                                        deltaFinished = true;
                                        countUsage("", config.getModel());
                                    }
                                }
                            }
                            //responseBody.close();
                        }
                    } catch (Exception e) {
                        deltaFinished = true;
                        response.close();
                        logger.error("unknownException: ", e);
                    }
                    response.close();
                }
            }
        });
    }
    private void dealLineDelta(JSONObject deltaObject, String model) {
        logger.debug(deltaObject.toJSONString());
        if(deltaObject.containsKey("usage") && deltaObject.get("usage")!=null) {
            duration = System.currentTimeMillis()-start;
            JSONObject cost = deltaObject.getJSONObject("usage");
            usage = new JSONObject();
            usage.put("id", deltaObject.getString("id"));
            usage.put("model", deltaObject.getString("model"));
            usage.put("promptTokens", cost.getInteger("prompt_tokens"));
            usage.put("completionTokens", cost.getInteger("completion_tokens"));
            usage.put("totalTokens", cost.getInteger("total_tokens"));
            logger.info("usage: {}", usage);
            deltaFinished = true;
        }
        else if(deltaObject.containsKey("choices") && deltaObject.get("choices")!=null) {
            JSONObject choice0 = deltaObject.getJSONArray("choices").getJSONObject(0);
            if(choice0.containsKey("finish_reason") && StringUtils.isNoneBlank(choice0.getString("finish_reason")) && "stop".equals(choice0.getString("finish_reason"))) {
                logger.debug("event: {} finish, reason: {}", deltaObject.getString("id"), choice0.getString("finish_reason"));
                deltaFinished = true;
                countUsage(deltaObject.getString("id"), model);
            }
            else if(choice0.containsKey("delta") && choice0.get("delta")!=null) {
                JSONObject delta = choice0.getJSONObject("delta");
                if(delta.containsKey("tool_calls") && delta.get("tool_calls")!=null) {
                    JSONArray toolCalls = delta.getJSONArray("tool_calls");
                    logger.debug(toolCalls.toJSONString());
                    for(int i=0; i<toolCalls.size(); i++) {
                        JSONObject toolCall = toolCalls.getJSONObject(i);
                        if(toolCall.containsKey("id") && toolCall.containsKey("type") && toolCall.containsKey("function") &&StringUtils.isNoneBlank(toolCall.getString("id"))) {
                            //toolCallsArray.add(toolCall);
                            if(StringUtils.isNoneBlank(toolCall.getJSONObject("function").getString("name")) && toolCall.getJSONObject("function").containsKey("arguments")) {
                                toolCallsArray.add(toolCall);
                                completionTokens += toolCall.getJSONObject("function").getString("name").length();
                                String params = toolCall.getJSONObject("function").getString("arguments");
                                if(StringUtils.isNoneBlank(params) && ((params.length()>2 && params.startsWith("{") && params.endsWith("}")) || "{}".equals(params))) {
                                    completionTokens += params.length();
                                    //currentToolCall++;
                                    if(checkFunctionArguments(params)) {
                                        currentToolCall++;
                                    }
                                }
                            }
                            else if(StringUtils.isNoneBlank(toolCall.getJSONObject("function").getString("name")) && (!toolCall.getJSONObject("function").containsKey("arguments") || toolCall.getJSONObject("function").get("arguments")==null)) {
                                completionTokens += toolCall.getJSONObject("function").getString("name").length();
                                toolCall.getJSONObject("function").put("arguments", "");
                                toolCallsArray.add(toolCall);
                            }
                        }
                        else if(toolCall.containsKey("function") && toolCall.get("function")!=null && toolCall.getJSONObject("function").containsKey("arguments")) {
                            if(!toolCallsArray.isEmpty() && StringUtils.isNoneBlank(toolCall.getJSONObject("function").getString("arguments"))) {
                                completionTokens += toolCall.getJSONObject("function").getString("arguments").length();
                                JSONObject tempFC = toolCallsArray.getJSONObject(currentToolCall);
                                String add = tempFC.getJSONObject("function").getString("arguments") + toolCall.getJSONObject("function").getString("arguments");
                                tempFC.getJSONObject("function").put("arguments", add);
                                if((add.length()>2 && add.startsWith("{") && add.endsWith("}")) || "{}".equals(add)) {
                                    if(checkFunctionArguments(add)) {
                                        logger.info("ai-params: {}, completionId: {}", tempFC.toJSONString(), deltaObject.getString("id"));
                                        currentToolCall++;
                                    }
                                }
                            }
                        }
                    }
                }
                else if(delta.containsKey("content") && StringUtils.isNoneBlank(delta.getString("content"))) {
                    logger.debug("responseId: {}, content: {}", deltaObject.getString("id"), delta.getString("content"));
                    String deltaStr = delta.getString("content");
                    completionTokens += deltaStr.length();
                    //contents.append(delta.getString("content"));

                    if(!"<think>".equals(deltaStr) && !"</think>".equals(deltaStr)) {
                        String pushDelta = deltaStr;
                        if("<think>".equals(contents.toString())) {
                            pushDelta = "<think>" + pushDelta;
                        }
                        logger.debug("ai-think-delta: {}, completionId: {}", pushDelta, deltaObject.getString("id"));
                    }
                    else if("</think>".equals(deltaStr)) {
                        if(!"<think>".equals(contents.toString().trim())) {
                            logger.debug("ai-think-delta: {}, completionId: {}", deltaStr, deltaObject.getString("id"));
                        }
                    }
                    contents.append(deltaStr);
                }
                else if(delta.containsKey("content") && delta.getString("content")!=null) {
                    completionTokens += delta.getString("content").length();
                }
                else if(delta.containsKey("reasoning") && delta.getString("reasoning")!=null) {
                    logger.info("reasoning: {}, completionId: {}", delta.getString("reasoning"), deltaObject.getString("id"));
                }
            }
        }
    }
    private void countUsage(String completionId, String model) {
        if(usage==null) {
            duration = System.currentTimeMillis() - start;
            usage = new JSONObject();
            usage.put("id", "".equals(completionId)? UUID.randomUUID().toString():completionId);
            usage.put("model", model);
            usage.put("promptTokens", promptTokens);
            usage.put("completionTokens", completionTokens);
            usage.put("totalTokens", promptTokens+completionTokens);
            logger.info("add usage: {}", usage);
        }
    }
    private boolean checkFunctionArguments(String arguments) {
        try {
            JSONObject.parseObject(arguments);
            return true;
        }
        catch (Exception e) {
            logger.debug("argumentNotComplete: ", e);
            return false;
        }
    }
    private JSONObject parseStringArgs(String params, JSONObject tmpFunObj) {
        String args = params;
        if("".equals(args)) {
            tmpFunObj.put("arguments", "{}");
            return new JSONObject();
        }
        if (args.startsWith("\"{") && args.endsWith("}\"")) {
            args = args.substring(1, args.length()-1);
            if(args.contains("\\")) {
                args = args.replaceAll("\\\\", "");
            }
            tmpFunObj.put("arguments", args);
        }
        if (!args.startsWith("{")) {
            args = "{" + args;
            tmpFunObj.put("arguments", args);
        }
        if (!args.endsWith("}")) {
            args = args + "}";
            tmpFunObj.put("arguments", args);
        }
        String jsonException = "参数格式错误：";
        try{
            return JSONObject.parseObject(args);
        }
        catch(JSONException e) {
            jsonException += e.getMessage();
            logger.error("current args: {}, parseStringArgs error: {}", args, e.getMessage());
            args = args.replaceAll("([a-z0-9]+):", "\"$1\":");
            try {
                return JSONObject.parseObject(args);
            }
            catch (JSONException e1) {
                jsonException += e1.getMessage();
                logger.error("argumentNotComplete: ", e1);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonExceptionLV", jsonException);
        return jsonObject;
    }
    public boolean isDeltaFinished() {
        checkTimes++;
        return deltaFinished;
    }
    public int getCheckTimes() {
        return checkTimes;
    }
    private ToolCalls getToolCalls() {
        if(toolCallsArray!=null && !toolCallsArray.isEmpty()) {
            ToolCalls toolCalls = new ToolCalls();
            logger.info("toolCallsArray: {}", toolCallsArray);
            JSONArray originalFunctions = new JSONArray();
            List<FunctionCall> functionCalls = new ArrayList<>();
            JSONArray wrongArgFunctionCalls = new JSONArray();
            try {
                for(int i=0; i<toolCallsArray.size(); i++) {
                    JSONObject tmpObject = toolCallsArray.getJSONObject(i);
                    JSONObject tmpFunObj = tmpObject.getJSONObject("function");
                    if(StringUtils.isNoneBlank(tmpFunObj.getString("name"))) {
                        originalFunctions.add(tmpFunObj.getString("name"));
                        JSONObject arguments = null;
                        boolean argsIsValid = true;
                        if(tmpFunObj.containsKey("arguments") && tmpFunObj.get("arguments")!=null) {
                            if(tmpFunObj.get("arguments") instanceof String) {
                                String args = tmpFunObj.getString("arguments");
                                JSONObject tmpArgumentObj = parseStringArgs(args, tmpFunObj);
                                if(tmpArgumentObj!=null && !tmpArgumentObj.containsKey("jsonExceptionLV")) {
                                    arguments = tmpArgumentObj;
                                }
                                else {
                                    argsIsValid = false;
                                    if(tmpArgumentObj!=null && tmpArgumentObj.containsKey("jsonExceptionLV")) {
                                        JSONObject wrongArgFunctionCall = new JSONObject();
                                        wrongArgFunctionCall.put("name", tmpFunObj.getString("name"));
                                        wrongArgFunctionCall.put("arguments", args);
                                        wrongArgFunctionCall.put("exception", tmpArgumentObj.getString("jsonExceptionLV"));
                                        wrongArgFunctionCalls.add(wrongArgFunctionCall);
                                    }
                                }
                            }
                            else if(tmpFunObj.get("arguments") instanceof JSONObject) {
                                arguments = tmpFunObj.getJSONObject("arguments");
                            }
                            else {
                                argsIsValid = false;
                                logger.error("invalid arguments object: {}", tmpFunObj.get("arguments"));
                                JSONObject wrongArgFunctionCall = new JSONObject();
                                wrongArgFunctionCall.put("name", tmpFunObj.getString("name"));
                                wrongArgFunctionCall.put("arguments", tmpFunObj.get("arguments"));
                                wrongArgFunctionCall.put("exception", "未知的数据类型");
                                wrongArgFunctionCalls.add(wrongArgFunctionCall);
                            }
                        }
                        if(argsIsValid) {
                            functionCalls.add(new FunctionCall(tmpObject, tmpObject.getString("id"), tmpFunObj.getString("name"), arguments));
                        }
                        else {
                            logger.error("method: {} with invalid arguments: {}", tmpFunObj.getString("name"), tmpFunObj.get("arguments"));
                        }
                    }
                }
            }
            catch (Exception e) {
                logger.error("getToolCalls error: ", e);
            }
            toolCalls.setOriginalFunctions(originalFunctions);
            if(!functionCalls.isEmpty()) {
                toolCalls.setFunctionCalls(functionCalls);
            }
            if(!wrongArgFunctionCalls.isEmpty()) {
                toolCalls.setWrongArgFunctionCalls(wrongArgFunctionCalls);
            }
            return toolCalls;
        }
        return null;
    }
    public ChatResponse getChatResponse() {
        ChatResponse chatResponse = new ChatResponse();
        if(usage==null) countUsage("",  model);
        chatResponse.setUsage(usage);
        ToolCalls toolCalls = getToolCalls();
        if(toolCalls!=null) {
            chatResponse.setOriginalFunctions(toolCalls.getOriginalFunctions());
            if(toolCalls.getFunctionCalls()!=null) {
                chatResponse.setFunctionCalls(toolCalls.getFunctionCalls());
            }
            if(toolCalls.getWrongArgFunctionCalls()!=null) {
                chatResponse.setWrongArgFunctionCalls(toolCalls.getWrongArgFunctionCalls());
            }
        }
        chatResponse.setContent(contents.toString());
        if(StringUtils.isNoneBlank(anomalous)) {
            chatResponse.setAnomalous(JSONObject.parseObject("{\"info\":\"" + anomalous +"\"}"));
        }
        chatResponse.setFirstTokenTime(firstTokenTime);
        chatResponse.setDuration(duration);
        if(usage!=null && duration>0) {
            chatResponse.setTokenPerSecond(usage.getIntValue("completionTokens")*1000.0/duration);
            logger.info("duration: {}, token/s: {}", duration, chatResponse.getTokenPerSecond());
        }
        return chatResponse;
    }
}
