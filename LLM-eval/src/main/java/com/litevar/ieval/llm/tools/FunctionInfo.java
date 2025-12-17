package com.litevar.ieval.llm.tools;

import java.util.Map;

/**
 * @Author  action
 * @Date  2025/10/14 17:08 
 * @company litevar
 **/
public class FunctionInfo {
    //protocal(http or jsonrpcHttp)
    private String kind;
    //http or other protocol server, or local clazzName
    private String server;
    //additional msg
    private String remarks;
    private String queries;
    private String paths;
    //use for http
    private String method;
    private Map<String, String> headers;
    private String description;
    //use for tool call agent, or local functionCall(agentId)
    private Integer callAgent;
    private String name;
    private Integer uId;

    public FunctionInfo() {}
    public FunctionInfo(String kind, String server, String method, Map<String,String> headers) {
        this.kind = kind;
        this.server = server;
        this.method = method;
        this.headers = headers;
    }
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getServer() {
        return server;
    }
    public void setServer(String server) {
        this.server = server;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getCallAgent() {
        return callAgent;
    }
    public void setCallAgent(Integer callAgent) {
        this.callAgent = callAgent;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getuId() {
        return uId;
    }
    public void setuId(Integer uId) {
        this.uId = uId;
    }
    public String getQueries() {
        return queries;
    }
    public void setQueries(String queries) {
        this.queries = queries;
    }
    public String getPaths() {
        return paths;
    }
    public void setPaths(String paths) {
        this.paths = paths;
    }
}
