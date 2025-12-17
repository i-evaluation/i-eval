package com.litevar.ieval.llm.runner;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author  action
 * @Date  2025/10/20 11:21 
 * @company litevar
 **/
public class TestSuiteOutput {
    private int rowNum;
    private String summary;
    private String executeTime;
    private String executeResult;
    public TestSuiteOutput(int rowNum) {
        this.rowNum = rowNum;
        this.executeTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(new Date().toInstant().atZone(ZoneId.systemDefault()));
    }
    public int getRowNum() {
        return rowNum;
    }
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getExecuteTime() {
        return executeTime;
    }
    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }
    public String getExecuteResult() {
        return executeResult;
    }
    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }
}
