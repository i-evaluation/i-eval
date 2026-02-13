package com.litevar.ieval.llm.runner;
/**
 * @Author  action
 * @Date  2025/12/19 14:25
 * @company litevar
 **/
public class TestedSuite {
    private int rowNum;
    private String description;
    private String executeResult;
    public TestedSuite(int rowNum, String description, String executeResult) {
        this.rowNum = rowNum;
        this.description = description;
        this.executeResult = executeResult;
    }
    public int getRowNum() {
        return rowNum;
    }
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getExecuteResult() {
        return executeResult;
    }
    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }
}
