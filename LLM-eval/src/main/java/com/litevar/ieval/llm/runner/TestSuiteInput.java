package com.litevar.ieval.llm.runner;
/**
 * @Author  action
 * @Date  2025/10/20 09:57
 * @company litevar
 **/
public class TestSuiteInput {
    private int rowNum;
    private String serial;
    private String category;
    private String concurrency;
    private String prompt;
    private String tools;
    private String llmConfig;
    private String suite;

    public TestSuiteInput(int rowNum, String serial, String category, String concurrency, String prompt, String tools, String llmConfig, String suite) {
        this.rowNum = rowNum;
        this.serial = serial;
        this.category = category;
        this.concurrency = concurrency;
        this.prompt = prompt;
        this.tools = tools;
        this.llmConfig = llmConfig;
        this.suite = suite;
    }
    public int getRowNum() {
        return rowNum;
    }
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    public String getSerial() {
        return serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getConcurrency() {
        return concurrency;
    }
    public void setConcurrency(String concurrency) {
        this.concurrency = concurrency;
    }
    public String getPrompt() {
        return prompt;
    }
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    public String getTools() {
        return tools;
    }
    public void setTools(String tools) {
        this.tools = tools;
    }
    public String getLlmConfig() {
        return llmConfig;
    }
    public void setLlmConfig(String llmConfig) {
        this.llmConfig = llmConfig;
    }
    public String getSuite() {
        return suite;
    }
    public void setSuite(String suite) {
        this.suite = suite;
    }
}
