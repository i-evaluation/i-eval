package com.litevar.ieval.llm.chat.message;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @Author  action
 * @Date  2025/10/14 16:18 
 * @company litevar
 **/
public class ToolCalls {
    private List<FunctionCall> functionCalls;
    private JSONArray wrongArgFunctionCalls;
    private JSONArray originalFunctions;

    public List<FunctionCall> getFunctionCalls() {
        return functionCalls;
    }
    public void setFunctionCalls(List<FunctionCall> functionCalls) {
        this.functionCalls = functionCalls;
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
