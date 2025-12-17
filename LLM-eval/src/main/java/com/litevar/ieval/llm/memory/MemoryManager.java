package com.litevar.ieval.llm.memory;

import com.alibaba.fastjson.JSONArray;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author  action
 * @Date  2025/10/15 10:03
 * @company litevar
 **/
public class MemoryManager {
    private static MemoryManager instance = null;
    private MemoryManager() {}
    private static final Map<String, JSONArray> contextMap = new ConcurrentHashMap<>();
    synchronized public static MemoryManager getInstance() {
        if(instance == null) {
            instance = new MemoryManager();
        }
        return instance;
    }
    public void addContext(String key, JSONArray value) {
        contextMap.put(key, value);
    }
    public JSONArray getContext(String key) {
        return contextMap.getOrDefault(key, null);
    }
    public void removeContext(String key) {
        contextMap.remove(key);
    }
}
