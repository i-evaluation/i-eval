package com.litevar.ieval.llm.chat;
/**
 * @Author  action
 * @Date  2025/10/14 16:11 
 * @company litevar
 **/
public class ChatOptions {
    private float temperature = 0.5f;
    private int maxTokens = 0;
    private int seed = 7;
    private boolean stream = false;
    private String responseFormat = "text";
    private boolean noThink = false;

    public ChatOptions() {}
    public ChatOptions(float temperature, int maxTokens, int seed) {
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.seed = seed;
    }
    public float getTemperature() {
        return temperature;
    }
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    public int getMaxTokens() {
        return maxTokens;
    }
    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
    public int getSeed() {
        return seed;
    }
    public void setSeed(int seed) {
        this.seed = seed;
    }
    public boolean isStream() {
        return stream;
    }
    public void setStream(boolean stream) {
        this.stream = stream;
    }
    public String getResponseFormat() {
        return responseFormat;
    }
    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }
    public boolean isNoThink() {
        return noThink;
    }
    public void setNoThink(boolean noThink) {
        this.noThink = noThink;
    }
}
