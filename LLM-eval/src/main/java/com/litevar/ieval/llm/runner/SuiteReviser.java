package com.litevar.ieval.llm.runner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author  action
 * @Date  2025/12/19 14:12
 * @company litevar
 **/
public class SuiteReviser {
    public void revise(String testPlanPath, String model) {
        System.out.println("Revising suite: " + testPlanPath);
        ExcelProcessor excelProcessor = new ExcelProcessor();
        List<TestedSuite> testedSuites = excelProcessor.getTestedSuiteList(testPlanPath);
        if (testedSuites != null && !testedSuites.isEmpty()) {
            List<SuiteSummary> suiteSummaryList = new ArrayList<>();
            for (TestedSuite testedSuite : testedSuites) {
                SuiteSummary suiteSummary = excelProcessor.readTestedSuite(testedSuite.getExecuteResult());
                if (suiteSummary != null) {
                    excelProcessor.updateSuiteSummaryToTestPlan(testPlanPath, testedSuite.getRowNum(), suiteSummary);
                    suiteSummary.setDescription(testedSuite.getDescription());
                    suiteSummaryList.add(suiteSummary);
                }
            }
            SuiteSummaryHandler.suiteSummaryToMarkdown(suiteSummaryList, model, true);
        }
    }
}
