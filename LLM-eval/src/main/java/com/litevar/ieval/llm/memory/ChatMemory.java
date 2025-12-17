package com.litevar.ieval.llm.memory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author  action
 * @Date  2025/10/14 16:00 
 * @company litevar
 **/
public abstract class ChatMemory {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract JSONArray getMessages(String userCmd);

    public abstract void addMessage(JSONObject message);

    public abstract void addMessages(JSONArray messages);

    public abstract void resetMessages(JSONArray messages);

    public abstract void clearContext();

    public abstract int getContextSize();

    public abstract void persistContext(String sessionId);
    public abstract String getLastContent();
    public abstract boolean correctContext(String content);
    public abstract JSONObject getLastContext();
}
