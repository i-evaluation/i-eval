package com.litevar.ieval.llm.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.chat.message.ChatResponse;
import com.litevar.ieval.llm.chat.message.ToolCalls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author  action
 * @Date  2025/10/14 16:15 
 * @company litevar
 **/
public abstract class LlmHttpClient {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    public abstract ChatResponse request(ChatConfig config, ChatOptions options, JSONArray messages, JSONArray tools);
    protected abstract JSONObject dealCost(JSONObject object, String model);
    protected abstract ToolCalls dealFunctionCall(JSONObject msgObject);
    protected abstract JSONObject dealAnomalousResponse(JSONObject object);
}
