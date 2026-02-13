package com.litevar.ieval.llm.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.chat.message.ChatResponse;
import com.litevar.ieval.llm.chat.message.FunctionCall;
import com.litevar.ieval.llm.chat.message.ToolCalls;
import com.litevar.ieval.llm.utils.RestUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author  action 
 * @Date  2025/10/14 16:29 
 * @company litevar
 **/
public class OpenAiClient extends LlmHttpClient {

    public ChatResponse request(ChatConfig config, ChatOptions options, JSONArray messages, JSONArray tools) {
        ChatResponse response = new ChatResponse();
        long start = System.currentTimeMillis();
        JSONObject object = doRequest(config, options, messages, tools);
        long duration = System.currentTimeMillis() - start;
        response.setDuration(duration);
        if(object.containsKey("usage")) {
            response.setUsage(dealCost(object, config.getModel()));
        }
        if(object.containsKey("choices")) {
            JSONObject msgObject = object.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
            logger.debug("AI response: {}", msgObject);
            if(msgObject.containsKey("tool_calls") && msgObject.getJSONArray("tool_calls")!=null && !msgObject.getJSONArray("tool_calls").isEmpty()) {
                logger.info("tool_calls: {}", msgObject.getJSONArray("tool_calls"));
                ToolCalls toolCalls = dealFunctionCall(msgObject);
                if(toolCalls!=null) {
                    response.setOriginalFunctions(toolCalls.getOriginalFunctions());
                    if(toolCalls.getFunctionCalls()!=null) {
                        response.setFunctionCalls(toolCalls.getFunctionCalls());
                    }
                    if(toolCalls.getWrongArgFunctionCalls()!=null) {
                        response.setWrongArgFunctionCalls(toolCalls.getWrongArgFunctionCalls());
                    }
                }
            }
            else if(msgObject.containsKey("content") && StringUtils.isNoneBlank(msgObject.getString("content"))) {
                response.setContent(msgObject.getString("content"));
            }
            if(response.getUsage()!=null && response.getUsage().containsKey("completionTokens")) {
                int tokenLen = response.getUsage().getInteger("completionTokens");
                response.setTokenPerSecond((double) 1000*tokenLen /duration);
            }
            logger.info("duration: {}, token/s: {}", duration, response.getTokenPerSecond());
        }
        else {
            response.setAnomalous(dealAnomalousResponse(object));
        }
        return response;
    }

    // deal cost
    protected JSONObject dealCost(JSONObject object, String model) {
        JSONObject usage = object.getJSONObject("usage");
        JSONObject costObject = new JSONObject();
        costObject.put("id", object.getString("id"));
        costObject.put("model", object.getString("model"));
        costObject.put("promptTokens", usage.getInteger("prompt_tokens"));
        costObject.put("completionTokens", usage.getInteger("completion_tokens"));
        costObject.put("totalTokens", usage.getInteger("total_tokens"));
        return costObject;
    }
    //deal function call
    protected ToolCalls dealFunctionCall(JSONObject msgObject) {
        ToolCalls toolCalls = new ToolCalls();
        List<FunctionCall> functionCalls = new ArrayList<>();
        JSONArray wrongArgFunctionCalls = new JSONArray();
        JSONArray originalFunctions = new JSONArray();
        JSONArray funcArray = msgObject.getJSONArray("tool_calls");
        try{
            for(int i=0; i<funcArray.size(); i++) {
                JSONObject tmpObject = funcArray.getJSONObject(i);
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

    @Override
    protected JSONObject dealAnomalousResponse(JSONObject object) {
        JSONObject result = null;
        if(object.containsKey("error")) {
            JSONObject error = object.getJSONObject("error");
            result = new JSONObject();
            result.put("type", error.getString("type"));
            result.put("msg", error.getString("message"));
        }
        else if(object.containsKey("code") && object.containsKey("info")) {
            result = new JSONObject();
            result.put("code", object.getInteger("code"));
            result.put("info", object.getString("info"));
        }
        return result;
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
    public JSONObject doRequest(ChatConfig config, ChatOptions options, JSONArray messages, JSONArray tools) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + config.getApiKey());
        JSONObject sendPayloadObj = new JSONObject();
        sendPayloadObj.put("model", config.getModel());
        sendPayloadObj.put("messages", messages);
        sendPayloadObj.put("stream", false);
        sendPayloadObj.put("temperature", options.getTemperature());
        sendPayloadObj.put("max_tokens", options.getMaxTokens());
        sendPayloadObj.put("seed", options.getSeed());
        //sendPayloadObj.put("top_p", 0.8);
        //sendPayloadObj.put("top_k", 20);
        //sendPayloadObj.put("repetition_penalty", 1.05);
        if(tools!=null) {
            sendPayloadObj.put("tool_choice", "auto");
            sendPayloadObj.put("tools", tools);
        }
        if("json_object".equals(options.getResponseFormat())) {
            sendPayloadObj.put("response_format", JSONObject.parseObject("{\"type\": \"json_object\"}"));
        }
        String url = "/v1/chat/completions";
        if(config.getModel().startsWith("glm-")) url = "/api/paas/v4/chat/completions";
        JSONObject result = RestUtil.postWithHeader(config.getEndpoint()+url, headers, sendPayloadObj.toJSONString());
        logger.info("receive>>> {}", result);
        return result;
    }
}
