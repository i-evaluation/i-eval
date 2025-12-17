package com.litevar.ieval.llm.chat.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author  action
 * @Date  2025/10/14 16:19
 * @company litevar
 **/
public class ChatResponse {
    private JSONObject usage;
    private List<FunctionCall> functionCalls;
    private String content;
    private JSONObject anomalous;
    private long firstTokenTime;
    private long duration;
    private double tokenPerSecond;
    //{"name":"","arguments":"","exception":""}
    private JSONArray wrongArgFunctionCalls;
    private JSONArray originalFunctions;

    public JSONObject getUsage() {
        return usage;
    }
    public void setUsage(JSONObject usage) {
        this.usage = usage;
    }
    public List<FunctionCall> getFunctionCalls() {
        return functionCalls;
    }
    public void setFunctionCalls(List<FunctionCall> functionCalls) {
        this.functionCalls = functionCalls;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public JSONObject getAnomalous() {
        return anomalous;
    }
    public void setAnomalous(JSONObject anomalous) {
        this.anomalous = anomalous;
    }
    public long getFirstTokenTime() {
        return firstTokenTime;
    }
    public void setFirstTokenTime(long firstTokenTime) {
        this.firstTokenTime = firstTokenTime;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public double getTokenPerSecond() {
        return tokenPerSecond;
    }
    public void setTokenPerSecond(double tokenPerSecond) {
        this.tokenPerSecond = tokenPerSecond;
    }
    public JSONArray getWrongArgFunctionCalls() {
        return wrongArgFunctionCalls;
    }
    public void setWrongArgFunctionCalls(JSONArray wrongArgFunctionCalls) {
        this.wrongArgFunctionCalls = wrongArgFunctionCalls;
    }
    public JSONArray getOriginalFunctions() {
        return originalFunctions;
    }
    public void setOriginalFunctions(JSONArray originalFunctions) {
        this.originalFunctions = originalFunctions;
    }
}
