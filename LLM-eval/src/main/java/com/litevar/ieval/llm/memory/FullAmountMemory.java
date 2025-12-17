package com.litevar.ieval.llm.memory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.chat.AgentEvent;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author  action
 * @Date  2025/10/14 16:45 
 * @company litevar
 **/
public class FullAmountMemory extends ChatMemory {
    private static final String OUTPUT_DIR = "records/";
    private final JSONArray messages = new JSONArray();
    public FullAmountMemory() {
        Path path = Paths.get(OUTPUT_DIR);
        if(Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                logger.error("create directory error: ", e);
            }
        }
    }
    @Override
    public JSONArray getMessages(String userCmd) {
        return this.messages;
    }

    @Override
    public void addMessage(JSONObject message) {
        this.messages.add(message);
    }

    @Override
    public void addMessages(JSONArray messages) {
        this.messages.addAll(messages);
        logger.info("context size: {}", this.messages.size());
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
        return this.messages.size();
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
    private void saveMemoryToFile(String sessionId){
        try {
            Path path = Paths.get(OUTPUT_DIR + sessionId + ".json");
            if(Files.exists(path)) {
                Files.deleteIfExists(path);
            }
            Files.write(path, messages.toJSONString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            logger.error("write file error: ", e);
        }
    }
}
