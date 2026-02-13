package com.litevar.ieval;

import com.litevar.ieval.llm.runner.SuiteRunner;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args) {
        String testPlanPath = "file/testPlan-local.xlsx";
        new SuiteRunner().run(testPlanPath, "qwen3-omni-30b", true);
    }
}