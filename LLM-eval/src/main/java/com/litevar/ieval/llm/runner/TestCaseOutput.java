package com.litevar.ieval.llm.runner;
/**
 * @Author  action
 * @Date  2025/10/20 11:24
 * @company litevar
 **/
public class TestCaseOutput {
    private int rowNum;
    private String score;
    //Total duration
    private long duration;
    //Average first token time
    private long firstToken;
    //Average tokens per second
    private double tokenPerSecond;
    //Total generated tokens
    private int completionTokens;
    //Context length
    private int promptTokens;
    //Context entries
    private int contextSize;
    private String details;
    public TestCaseOutput(int rowNum) {
        this.rowNum = rowNum;
    }
    public TestCaseOutput(int rowNum, String score, long duration, long firstToken, double tokenPerSecond, int completionTokens, int promptTokens, int contextSize, String details) {
        this.rowNum = rowNum;
        this.score = score;
        this.duration = duration;
        this.firstToken = firstToken;
        this.tokenPerSecond = tokenPerSecond;
        this.completionTokens = completionTokens;
        this.promptTokens = promptTokens;
        this.contextSize = contextSize;
        this.details = details;
    }
    public int getRowNum() {
        return rowNum;
    }
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public long getFirstToken() {
        return firstToken;
    }
    public void setFirstToken(long firstToken) {
        this.firstToken = firstToken;
    }
    public double getTokenPerSecond() {
        return tokenPerSecond;
    }
    public void setTokenPerSecond(double tokenPerSecond) {
        this.tokenPerSecond = tokenPerSecond;
    }
    public int getCompletionTokens() {
        return completionTokens;
    }
    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }
    public int getPromptTokens() {
        return promptTokens;
    }
    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }
    public int getContextSize() {
        return contextSize;
    }
    public void setContextSize(int contextSize) {
        this.contextSize = contextSize;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
}
