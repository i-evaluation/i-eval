package com.litevar.ieval;

import com.litevar.ieval.llm.runner.SuiteReviser;

public class MainReviser {
    public static void main(String[] args) {
        String testPlanPath = "file/testPlan-local.xlsx";
        new SuiteReviser().revise(testPlanPath, "qwen3-omni-30b");
    }
}
