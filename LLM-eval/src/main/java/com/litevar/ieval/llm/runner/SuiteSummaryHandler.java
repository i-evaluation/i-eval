package com.litevar.ieval.llm.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @Author  action
 * @Date  2025/12/19 15:03
 * @company litevar
 **/
public class SuiteSummaryHandler {
    private static final Logger logger = LoggerFactory.getLogger(SuiteSummaryHandler.class);
    public static SuiteSummary dealSuiteOutputSummary(int totalPoint, int cmdCount, int deducePointCount, int lessThanSixPointsCount, int lessThanThreePointsCount, String outputMarkdown) {
        StringBuilder suitesBuffer = new StringBuilder();
        if(totalPoint > 0 && cmdCount > 0) {
            float averagePoint = (float)totalPoint/cmdCount;
            suitesBuffer.append("# Test Results Summary\n").append("## Total Test Cases: "+cmdCount).append("\nAverage Score: "+averagePoint).append("\n");
            float suiteBasicPoint = 10 * averagePoint;
            if(deducePointCount > 0) {
                float lessThanTenDeduce = (float) 10*(deducePointCount-lessThanSixPointsCount)/cmdCount;
                suitesBuffer.append("## Test cases scoring below 10 points: "+deducePointCount).append(", Points deducted: "+lessThanTenDeduce).append("\n");
                suiteBasicPoint = suiteBasicPoint - lessThanTenDeduce;
                if(lessThanSixPointsCount>0) {
                    float lessThanSixDeduce = (float) 20*(lessThanSixPointsCount-lessThanThreePointsCount)/cmdCount;
                    suitesBuffer.append("## Test cases scoring below 6 points: "+lessThanSixPointsCount).append(", Points deducted: "+lessThanSixDeduce).append("\n");
                    suiteBasicPoint = suiteBasicPoint - lessThanSixDeduce;
                }
                if(lessThanThreePointsCount>0) {
                    float lessThanThreeDeduce = (float) 30*lessThanThreePointsCount/cmdCount;
                    suitesBuffer.append("## Test cases scoring below 3 points: "+lessThanThreePointsCount).append(", Points deducted: "+lessThanThreeDeduce).append("\n");
                    suiteBasicPoint = suiteBasicPoint - lessThanThreeDeduce;
                }
            }
            String ratingLevel = "D";
            if(suiteBasicPoint>95) {
                ratingLevel = "SS";
            } else if (suiteBasicPoint>90) {
                ratingLevel = "S";
            } else if (suiteBasicPoint>80) {
                ratingLevel = "A";
            } else if (suiteBasicPoint>70) {
                ratingLevel = "B";
            } else if (suiteBasicPoint>60) {
                ratingLevel = "C";
            }
            suitesBuffer.append("## Test Suite\\n### Score: "+suiteBasicPoint).append("\n### Rating Level: "+ratingLevel);
            return new SuiteSummary(outputMarkdown, ratingLevel, suiteBasicPoint, suitesBuffer.toString());
        }
        return null;
    }

    public static void suiteSummaryToMarkdown(List<SuiteSummary> suiteSummaryList, String model, boolean isRevised) {
        String evaluationTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(new Date().toInstant().atZone(ZoneId.systemDefault()));
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("# Test Report\n");
        stringBuffer.append("| Model | Score | Level | Environment | Remarks | Suites |\n");
        stringBuffer.append("| :---: | :---: | :---: | --- | --- | --- |\n");
        stringBuffer.append("| ").append(model).append(" | ");
        StringBuilder detailBuffer = new StringBuilder();
        float totalScore = 0;
        for (SuiteSummary suiteSummary : suiteSummaryList) {
            totalScore += suiteSummary.getScore();
            detailBuffer.append("<tr><td>[").append(suiteSummary.getDescription()).append("](../").append(suiteSummary.getName()).append(")</td><td>").append(suiteSummary.getScore()).append("</td><td>").append(suiteSummary.getLevel()).append("</td></tr>");
            logger.info("Suite: {}, score: {}, level: {}", suiteSummary.getName(), suiteSummary.getScore(), suiteSummary.getLevel());
        }
        int suiteCount = suiteSummaryList.size();
        float averageScore = suiteCount>1?(totalScore/suiteCount):totalScore;
        String ratingLevel = "D";
        if(averageScore>95) {
            ratingLevel = "SS";
        } else if (averageScore>90) {
            ratingLevel = "S";
        } else if (averageScore>80) {
            ratingLevel = "A";
        } else if (averageScore>70) {
            ratingLevel = "B";
        } else if (averageScore>60) {
            ratingLevel = "C";
        }
        stringBuffer.append(averageScore).append(" | ").append(ratingLevel).append(" | ").append(evaluationTime).append("<br>Saas | / | ").append("<table><tr><th>Description</th><th>Score</th><th>Level</th></tr>").append(detailBuffer).append("</table> |");
        logger.info("Test plan done with {} suites, score: {}, level: {}", suiteCount, averageScore, ratingLevel);
        String markdownPath = isRevised?"records/evaluationReport_"+System.currentTimeMillis()+"_r.md":"records/evaluationReport_"+System.currentTimeMillis()+".md";
        appendStringToFile(markdownPath, stringBuffer.toString());
        logger.info("LLM evaluation result: {}", markdownPath);
    }
    public static void appendStringToFile(String markdownPath, String content) {
        try {
            Path path = Paths.get(markdownPath);
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            logger.error("write file error: ", e);
        }
    }
}
