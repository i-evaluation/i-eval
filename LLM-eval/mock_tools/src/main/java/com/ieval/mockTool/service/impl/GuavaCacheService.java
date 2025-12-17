package com.ieval.mockTool.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ieval.mockTool.service.BaseService;

@Service
public class GuavaCacheService extends BaseService {
	
    private final LoadingCache<String, Object> loadingCache;

    public GuavaCacheService() {
        loadingCache = CacheBuilder.newBuilder()
        		.initialCapacity(10)
        		.concurrencyLevel(5)
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(8, TimeUnit.HOURS) // 设置缓存过期时间
                .build(new CacheLoader<String, Object>() { // 初始化CacheLoader用于自动加载数据
                    @Override
                    public Object load(String key) throws Exception {
                        return loadDataFromSource(key); // 当缓存未命中时调用此方法加载数据
                    }
                });
    }

    public Object getFromCache(String key) {
        return loadingCache.getIfPresent(key);
    }

    public void putIntoCache(String key, Object value) {
        loadingCache.put(key, value); // 手动写入缓存
    }
    
    public void clearCache(String key) {
    	loadingCache.invalidate(key);
    }

    private Object loadDataFromSource(String key) {
        // 实际从数据源加载数据的逻辑
    	return "data for " + key;
    }
}
