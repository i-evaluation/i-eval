package com.litevar.ieval.llm.runner;

public class SuiteSummary {
    private String name;
    private String level;
    private float score;
    public SuiteSummary(String name, String level, float score) {
        this.name = name;
        this.level = level;
        this.score = score;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
}
