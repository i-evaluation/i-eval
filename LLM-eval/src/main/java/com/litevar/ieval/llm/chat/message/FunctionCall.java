package com.litevar.ieval.llm.chat.message;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author  action 
 * @Date  2025/10/14 16:17 
 * @company litevar
 **/
public class FunctionCall {
    private JSONObject original;
    private String toolCallId;
    private String method;
    private JSONObject arguments;

    public FunctionCall(JSONObject original, String toolCallId, String method, JSONObject arguments) {
        this.original = original;
        this.toolCallId = toolCallId;
        this.method = method;
        this.arguments = arguments;
    }
    public JSONObject getOriginal() {
        return original;
    }
    public void setOriginal(JSONObject original) {
        this.original = original;
    }
    public String getToolCallId() {
        return toolCallId;
    }
    public void setToolCallId(String toolCallId) {
        this.toolCallId = toolCallId;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public JSONObject getArguments() {
        return arguments;
    }
    public void setArguments(JSONObject arguments) {
        this.arguments = arguments;
    }
}
