package com.litevar.ieval.llm.chat;
/**
 * @Author  action
 * @Date  2025/10/14 15:56
 * @company litevar
 **/
public interface AgentListener {
    void onAgentEvent(OutputCollector collector, AgentEvent event);
}
