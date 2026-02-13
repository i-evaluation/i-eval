package com.litevar.ieval.llm.memory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author  action
 * @Date  2026/1/15 17:03
 * @company litevar
 **/
public class SlidingWindowMemory extends ChatMemory {
    private int windowSize = 60;

    public SlidingWindowMemory(int windowSize) {
        super();
        if(windowSize>0) {
            this.windowSize = windowSize;
        }
    }
    @Override
    public JSONArray getMessages(String userCmd) {
        return  messages;
    }

    @Override
    public void addMessage(JSONObject message) {
        messages.add(message);
        if(!"user".equals(message.getString("role"))) {
            reduceMemory();
        }
    }

    @Override
    public void addMessages(JSONArray messages) {
        this.messages.addAll(messages);
        logger.info("context size: {}", this.messages.size());
        reduceMemory();
    }

    private void reduceMemory() {
        if(messages.size()>windowSize) {
            logger.info("Over threshold: {}", this.messages.size()-windowSize);
            int index = 0;
            int userCount = 0;
            for(int i = 0; i < this.messages.size(); i++) {
                JSONObject message = this.messages.getJSONObject(i);
                if("user".equals(message.getString("role"))) {
                    userCount++;
                }
                if(userCount==2) {
                    index = i;
                    break;
                }
            }
            if(index>0) {
                if("system".equals(this.messages.getJSONObject(0).getString("role"))) {
                    this.messages.subList(1, index).clear();
                    logger.debug("keep system prompt");
                    index--;
                }
                else{
                    this.messages.subList(0, index).clear();
                }
                logger.info("Sliding {} messages from memory, remain: {}", index, this.messages.size());
            }
        }
    }
    @Override
    public void resetMessages(JSONArray messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }

    @Override
    public void clearContext() {
        this.messages.clear();
    }

    @Override
    public int getContextSize() {
        return messages.size();
    }

    @Override
    public void persistContext(String sessionId) {
        if(!this.messages.isEmpty()) {
            saveMemoryToFile(sessionId);
        }
    }

    @Override
    public String getLastContent() {
        if(!this.messages.isEmpty()) {
            int index = this.messages.size()-1;
            for (int i = index; i >= 0; i--) {
                JSONObject message = this.messages.getJSONObject(i);
                if("assistant".equals(message.getString("role")) && StringUtils.isNoneBlank(message.getString("content"))) {
                    return message.getString("content");
                }
            }
        }
        return "";
    }

    @Override
    public boolean correctContext(String content) {
        if(!this.messages.isEmpty()) {
            JSONObject lastMessage = this.messages.getJSONObject(this.messages.size()-1);
            if("user".equals(lastMessage.getString("role"))) {
                logger.info("Last message role is user, correcting context...");
                if("remove".equals(content)) {
                    this.messages.removeLast();
                }
                else {
                    JSONObject assistMessage = new JSONObject();
                    assistMessage.put("role", "assistant");
                    assistMessage.put("content", content);
                    this.messages.add(assistMessage);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public JSONObject getLastContext() {
        if(!this.messages.isEmpty()) {
            return this.messages.getJSONObject(this.messages.size()-1);
        }
        return null;
    }
}
