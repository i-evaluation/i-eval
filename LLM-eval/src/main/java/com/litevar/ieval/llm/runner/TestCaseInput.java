package com.litevar.ieval.llm.runner;
/**
 * @Author  action
 * @Date  2025/10/20 11:21
 * @company litevar
 **/
public class TestCaseInput {
    private int rowNum;
    private String serial;
    private String cmd;
    private String picture;
    private String expectation;
    public TestCaseInput(String cmd, String picture) {
        this.cmd = cmd;
        this.picture = picture;
    }
    public TestCaseInput(int rowNum, String serial, String cmd, String picture, String expectation) {
        this.rowNum = rowNum;
        this.serial = serial;
        this.cmd = cmd;
        this.picture = picture;
        this.expectation = expectation;
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
    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getExpectation() {
        return expectation;
    }
    public void setExpectation(String expectation) {
        this.expectation = expectation;
    }

}
