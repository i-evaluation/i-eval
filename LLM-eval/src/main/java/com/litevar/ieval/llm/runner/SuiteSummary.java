package com.litevar.ieval.llm.runner;
/**
 * @Author  action
 * @Date  2025/12/19 15:25
 * @company litevar
 **/
public class SuiteSummary {
    private String name;
    private String description;
    private String level;
    private float score;
    private String summary;

    public SuiteSummary(String name, String level, float score, String summary) {
        this.name = name;
        this.level = level;
        this.score = score;
        this.summary = summary;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
}
