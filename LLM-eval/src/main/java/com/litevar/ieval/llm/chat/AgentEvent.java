package com.litevar.ieval.llm.chat;
/**
 * @Author  action
 * @Date  2025/10/14 15:39 
 * @company litevar
 **/
public class AgentEvent {
    private Integer agentId;
    private String nodeId;
    private String event;
    private String message;

    public AgentEvent(Integer agentId, String nodeId, String event, String message) {
        this.agentId = agentId;
        this.nodeId = nodeId;
        this.event = event;
        this.message = message;
    }
    public Integer getAgentId() {
        return agentId;
    }
    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
    public String getNodeId() {
        return nodeId;
    }
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
