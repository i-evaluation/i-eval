package com.litevar.ieval.llm.memory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author  action
 * @Date  2025/10/14 16:00 
 * @company litevar
 **/
public abstract class ChatMemory {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final String OUTPUT_DIR = "records/";
    protected final JSONArray messages = new JSONArray();
    public ChatMemory() {
        Path path = Paths.get(OUTPUT_DIR);
        if(Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                logger.error("create directory error: ", e);
            }
        }
    }
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
    protected void saveMemoryToFile(String sessionId){
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
