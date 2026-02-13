package com.litevar.ieval.llm.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.litevar.ieval.llm.runner.SuiteSummary;
import com.litevar.ieval.llm.runner.SuiteSummaryHandler;
import com.litevar.ieval.llm.runner.TestCaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author  action
 * @Date  2025/10/14 15:45 
 * @company litevar
 **/
public class OutputCollector {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private EventBus outputBus;
    private final String MARKDOWN_DIR = "markdown/";
    private String outputMarkdown;
    private int cmdCount = 0;
    private int totalPoint = 0;
    private final List<TestCaseOutput> testCaseOutputList = new ArrayList<>();
    private int lessThanSixPointsCount = 0;
    private int lessThanThreePointsCount = 0;
    private int deducePointCount = 0;

    public OutputCollector(String suite) {
        this.outputBus = new EventBus();
        this.outputBus.register(this);
        if((suite.endsWith(".xlsx")||suite.endsWith(".xls"))) {
            if(suite.contains("/")) {
                this.outputMarkdown = MARKDOWN_DIR + suite.substring(suite.lastIndexOf("/")+1, suite.lastIndexOf("."))+ System.currentTimeMillis()+".md";
            }
            else if(suite.contains("\\")) {
                this.outputMarkdown = MARKDOWN_DIR + suite.substring(suite.lastIndexOf("\\")+1, suite.lastIndexOf("."))+ System.currentTimeMillis()+".md";
            }
        }
        else {
            this.outputMarkdown = MARKDOWN_DIR + System.currentTimeMillis() + ".md";
        }
        SuiteSummaryHandler.appendStringToFile(outputMarkdown,"| No. | Instruction | Image | Expectation | Score | Duration(Total,ms) | First Token(Average) | Tokens Per Second(Average) | Generated Tokens(Total) | Context Length | Context Entries | Details |\n| --- | --- | --- | --- | :---: | :---: | :---: | :---: | --- | --- | --- | --- |\n");
    }

    @Subscribe
    public void onAgentEvent(AgentEvent event) {
        //System.out.println("Receive event from node: " + event.getNodeId() + ", type: " + event.getEvent()+", message: " + event.getMessage());
        if(StringUtils.isNoneBlank(event.getEvent()) && StringUtils.isNoneBlank(event.getMessage())) {
            String eventType = event.getEvent();
            switch (eventType) {
                case "llm-output":
                    transOutputToMarkdown(event.getMessage());
                    break;
                default:
                    break;
            }
        }
    }

    public EventBus getOutputBus() {
        return outputBus;
    }

    private void transOutputToMarkdown(String output) {
        if(output.contains("llmOutput") && output.contains("humanCmd") && output.contains("testCaseId")) {
            cmdCount++;
            try {
                //markdown format output
                StringBuilder markdownBuffer = new StringBuilder();
                JSONObject outputObject = JSONObject.parseObject(output);
                TestCaseOutput testCaseOutput = new TestCaseOutput(outputObject.getIntValue("testCaseId"));
                String picture = outputObject.containsKey("picture")?outputObject.getString("picture"):"";
                String expectation = outputObject.containsKey("expectation")?outputObject.getString("expectation"):"";
                markdownBuffer.append("| ").append(outputObject.getIntValue("testCaseId")).append(" | ").append(outputObject.getString("humanCmd").replaceAll("\\R", "<br>")).append(" | ").append(picture).append(" | ").append(expectation).append(" | ");
                StringBuilder detailsBuffer = new StringBuilder();
                //detailsBuffer.append("# "+cmdCount).append(", Command："+outputObject.getString("humanCmd")).append("\n");
                int expectCompletionTokens = 0;
                int expectFCCount = 0;
                String fcSequence = null;
                String[] fcSequences = null;
                String textFormat = null;
                JSONObject fcInfo=new JSONObject();
                if(StringUtils.isNoneBlank(expectation)) {
                    //detailsBuffer.append("## Expectation: ").append(outputObject.getString("expectation")).append("\n");
                    JSONObject expectationObj = getExpectationObject(expectation);
                    if(expectationObj!=null) {
                        if(expectationObj.containsKey("completionTokens")) {
                            expectCompletionTokens = expectationObj.getInteger("completionTokens");
                        }
                        if(expectationObj.containsKey("fcCount")) {
                            expectFCCount = expectationObj.getIntValue("fcCount");
                        }
                        if(expectationObj.containsKey("fcSequence")) {
                            fcSequence = expectationObj.getString("fcSequence");
                            if(fcSequence.contains(",")) {
                                fcSequences = fcSequence.split(",");
                            }
                            else {
                                fcSequences = new String[]{fcSequence};
                            }
                        }
                        if(expectationObj.containsKey("textFormat")) {
                            textFormat = expectationObj.getString("textFormat");
                        }
                        if(expectationObj.containsKey("fcInfo")) {
                            fcInfo = expectationObj.getJSONObject("fcInfo");
                        }
                    }
                }
                String reportTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(new Date().toInstant().atZone(ZoneId.systemDefault()));
                detailsBuffer.append("## Time：").append(reportTime).append("\n");
                detailsBuffer.append("## Output：").append("\n");
                JSONArray llmOutput = outputObject.getJSONArray("llmOutput");
                int testPoint = 10;
                StringBuffer deductionBuffer = new StringBuffer();
                if(!llmOutput.isEmpty()) {
                    long duration = 0;
                    long firstToken = 0;
                    double tokenPerSecond = 0.0;
                    int completionTokens = 0;
                    int promptTokens = 0;
                    int contextSize = 0;
                    JSONArray totalOriginalFunctions = new JSONArray();
                    int outputCount = llmOutput.size();
                    for (int i = 0; i < outputCount; i++) {
                        JSONObject item = llmOutput.getJSONObject(i);
                        detailsBuffer.append("### ").append("Time Tokens: ").append("\n");
                        detailsBuffer.append("  - ").append("Model: ").append(item.get("model")).append("\n");
                        detailsBuffer.append("  - ").append("First Token Time: ").append(item.get("firstToken")).append("\n");
                        detailsBuffer.append("  - ").append("Generated Tokens: ").append(item.get("completionTokens")).append("\n");
                        detailsBuffer.append("  - ").append("Duration: ").append(item.get("duration")).append("\n");
                        detailsBuffer.append("  - ").append("Token/s: ").append(item.get("tokenPerSecond")).append("\n");
                        detailsBuffer.append("  - ").append("Context Length: ").append(item.get("promptTokens")).append("\n");
                        detailsBuffer.append("  - ").append("Context Entries: ").append(item.get("contextSize")).append("\n");
                        duration += item.getLongValue("duration");
                        firstToken += item.getLongValue("firstToken");
                        tokenPerSecond += item.getDoubleValue("tokenPerSecond");
                        completionTokens += item.getIntValue("completionTokens");
                        promptTokens = item.getIntValue("promptTokens");
                        contextSize = item.getIntValue("contextSize");
                        if(item.containsKey("originalFunctions")) {
                            totalOriginalFunctions.addAll(item.getJSONArray("originalFunctions"));
                            detailsBuffer.append("### ").append("Function Calls: ").append("\n");
                            detailsBuffer.append("  - ").append("Original Functions: ").append(item.getJSONArray("originalFunctions")).append("\n");
                            detailsBuffer.append("  - ").append("Corrected Functions: ").append(item.getJSONArray("correctFunctions")).append("\n");
                            detailsBuffer.append("  - ").append("Executed Functions: ").append(item.getJSONArray("executedFunctions")).append("\n");
                            if(item.containsKey("wrongArgFunctions")) {
                                detailsBuffer.append("  - ").append("Functions with Wrong Arguments: ").append(item.getJSONArray("wrongArgFunctions")).append("\n");
                                testPoint = testPoint -2;
                                deductionBuffer.append("  - Functions with wrong arguments exist: ").append(item.getJSONArray("wrongArgFunctions")).append(", deduct 2 points\n");
                            }
                            if(StringUtils.isNoneBlank(item.getString("unknownFunctions"))) {
                                detailsBuffer.append("  - ").append("Unknown Functions: ").append(item.getString("unknownFunctions")).append("\n");
                                testPoint--;
                                deductionBuffer.append("  - Unknown functions exist: ").append(item.getString("unknownFunctions")).append(", deduct 1 point\n");
                            }
                            JSONArray correctFunctions=item.getJSONArray("correctFunctions");
                            if (!correctFunctions.isEmpty() && !fcInfo.isEmpty()) {
                            	for(int cf=0;cf<=correctFunctions.size();cf++) {
                            			JSONObject correctFCInfo=correctFunctions.getJSONObject(cf);
                                        if(correctFCInfo.containsKey("arguments")&& fcInfo.containsKey(correctFCInfo.getString("name"))) {
                                            JSONObject arguments = correctFCInfo.getJSONObject("arguments");
                                            if(arguments.size() != fcInfo.getInteger(correctFCInfo.getString("name"))) {
                                                testPoint = testPoint -5;
                                                logger.info("-------------------------------------------------");
                                                deductionBuffer.append("  - Function call argument error, expected argument count: ").append(fcInfo.getInteger(correctFCInfo.getString("name"))).append(", actual argument count: ").append(arguments.size()).append(", deduct 5 points\n");
                                            }
                                            break;
                                        }
									}
                                }
                        }
                        else {
                            detailsBuffer.append("### ").append("Text: ").append("\n");
                            detailsBuffer.append("   ```"+item.getString("text")).append("```\n");
                            if(textFormat!=null && textFormat.equals("json")) {
                                String jsonCheck = checkJsonTextFormat(item.getString("text"));
                                if(StringUtils.isNoneBlank(jsonCheck)) {
                                    testPoint = testPoint -5;
                                    deductionBuffer.append("  - JSON format error: ").append(jsonCheck).append(", deduct 5 points\n");
                                }
                            }
                            if(item.getInteger("completionTokens") < expectCompletionTokens) {
                                testPoint = testPoint -5;
                                deductionBuffer.append("  - Model generated token count (").append(item.getInteger("completionTokens")).append(") is less than expected value ("+expectCompletionTokens+"), deduct 5 points\n");
                            }
                            if(item.containsKey("isAnomalous") && item.getBoolean("isAnomalous")) {
                                String anomalousText = item.getString("text");
                                if(StringUtils.isNoneBlank(anomalousText) && (anomalousText.startsWith("error:400,") || anomalousText.startsWith("error:500,"))) {
                                    testPoint = testPoint -5;
                                    deductionBuffer.append("  - Model response anomalously, ").append(anomalousText).append(", deduct 5 points\n");
                                }
                            }
                        }
                        if(item.getInteger("firstToken") > 1000) {
                            testPoint--;
                            deductionBuffer.append("  - First token time (").append(item.getInteger("firstToken")).append(") is greater than 1s, deduct 1 point\n");
                        }
                        if(item.getDouble("tokenPerSecond") < 10) {
                            testPoint--;
                            deductionBuffer.append("  - token/s(").append(item.getDouble("tokenPerSecond")).append(") is less than 10, deduct 1 point\n");
                        }
                        testPoint = testPoint + dealCompletionTokensDuration(item.getInteger("completionTokens"), item.getLong("duration"), deductionBuffer);
                    }
                    if(expectFCCount>0 && totalOriginalFunctions.size() < expectFCCount) {
                        testPoint = testPoint -5;
                        deductionBuffer.append("  - Generated function count: ").append(totalOriginalFunctions.size()).append(", less than expected value: ").append(expectFCCount).append(", deduct 5 points\n");
                    }
                    if(fcSequences!=null) {
                        if(totalOriginalFunctions.size() != fcSequences.length) {
                            testPoint = testPoint -5;
                            deductionBuffer.append("  - Generated function count: ").append(totalOriginalFunctions.size()).append(", not equal to expected value: ").append(fcSequences.length).append(", deduct 5 points\n");
                        }
                        else {
                            boolean isSequence = true;
                            for(int j=0; j<totalOriginalFunctions.size(); j++) {
                                if(!totalOriginalFunctions.getString(j).equals(fcSequences[j])) {
                                    isSequence = false;
                                    break;
                                }
                            }
                            if(!isSequence) {
                                testPoint = testPoint -5;
                                deductionBuffer.append("  - Function call sequence error, actual sequence: ").append(totalOriginalFunctions).append(", expected sequence: ").append(fcSequence).append(", deduct 5 points\n");
                            }
                        }
                    }
                    detailsBuffer.append("## Score (base score 10): "+testPoint).append("\n");
                    if(!deductionBuffer.isEmpty()) {
                        detailsBuffer.append("## Deduction reasons: \n").append(deductionBuffer.substring(0, deductionBuffer.length()-1)).append("\n");
                        deducePointCount++;
                    }
                    if(testPoint<6) {
                        lessThanSixPointsCount++;
                        if(testPoint<3) {
                            lessThanThreePointsCount++;
                            if(testPoint<0) {
                                testPoint = 0;
                            }
                        }
                    }
                    totalPoint += testPoint;
                    long avgFirstToken = outputCount>1?firstToken/outputCount:firstToken;
                    double avgTokenPerSecond = outputCount>1?tokenPerSecond/outputCount:tokenPerSecond;
                    testCaseOutput.setScore(String.valueOf(testPoint));
                    testCaseOutput.setDetails(detailsBuffer.toString());
                    testCaseOutput.setDuration(duration);
                    testCaseOutput.setFirstToken(avgFirstToken>0?avgFirstToken:1);
                    testCaseOutput.setTokenPerSecond(avgTokenPerSecond>0?avgTokenPerSecond:1);
                    testCaseOutput.setCompletionTokens(completionTokens);
                    testCaseOutput.setPromptTokens(promptTokens);
                    testCaseOutput.setContextSize(contextSize);
                    testCaseOutputList.add(testCaseOutput);
                    String markdownDetails = detailsBuffer.toString();
                    if(markdownDetails.endsWith("\n")) {
                        markdownDetails = markdownDetails.substring(0, markdownDetails.length()-1);
                    }
                    markdownDetails = markdownDetails.replaceAll("\n", "<br>");
                    markdownBuffer.append(testPoint).append(" | ").append(duration).append(" | ").append(avgFirstToken>0?avgFirstToken:1).append(" | ").append(String.format("%.2f", avgTokenPerSecond>0?avgTokenPerSecond:1)).append(" | ").append(completionTokens).append(" | ").append(promptTokens).append(" | ").append(contextSize).append(" | ").append(markdownDetails).append(" |\n");
                    SuiteSummaryHandler.appendStringToFile(outputMarkdown, markdownBuffer.toString());
                }
            } catch (Exception e) {
                logger.error("add to markdown error: ", e);
            }
        }
    }
    private JSONObject getExpectationObject(String expectation){
        try {
            return JSONObject.parseObject(expectation);
        } catch (JSONException e) {
            logger.error("getCmdExpectation error: ", e);
        }
        return null;
    }
    private int dealCompletionTokensDuration(int completionTokens, long duration, StringBuffer deductionBuffer) {
        int deduce = 0;
        if(completionTokens < 11 && duration > 2000) {
            deduce--;
            deductionBuffer.append("  - Generated token count (").append(completionTokens).append(") is less than 11, and duration is greater than 2s, deduct 1 point\n");
        }
        else if(completionTokens < 101 && duration > 3500) {
            deduce--;
            deductionBuffer.append("  - Generated token count (").append(completionTokens).append(") is less than 101, and duration is greater than 3.5s, deduct 1 point\n");
        }
        else if(completionTokens < 1001 && duration > 8000) {
            deduce--;
            deductionBuffer.append("  - Generated token count (").append(completionTokens).append(") is less than 1001, and duration is greater than 8s, deduct 1 point\n");
        }
        else if(completionTokens < 5001 && duration > 20000) {
            deduce--;
            deductionBuffer.append("  - Generated token count (").append(completionTokens).append(") is less than 5001, and duration is greater than 20s, deduct 1 point\n");
        }
        else if(completionTokens < 10001 && duration > 45000) {
            deduce--;
            deductionBuffer.append("  - Generated token count (").append(completionTokens).append(") is less than 10001, and duration is greater than 45s, deduct 1 point\n");
        }
        else if(completionTokens < 50001 && duration > 60000) {
            deduce--;
            deductionBuffer.append("  - Generated token count (").append(completionTokens).append(") is less than 50001, and duration is greater than 60s, deduct 1 point\n");
        }
        else if(completionTokens < 100001 && duration > 90000) {
            deduce--;
            deductionBuffer.append("  - Generated token count (").append(completionTokens).append(") is less than 100001, and duration is greater than 90s, deduct 1 point\n");
        }
        else if(duration > 120000) {
            deduce = deduce -2;
            deductionBuffer.append("  - Duration (").append(duration).append(") is greater than 120s, deduct 2 points\n");
        }
        return deduce;
    }
    //check json format, return abnormal information
    private String checkJsonTextFormat(String text) {
        if(StringUtils.isNoneBlank(text)) {
            String tmpContent = text.trim();
            if(StringUtils.startsWith(tmpContent, "```json") && StringUtils.endsWith(tmpContent, "```")) {
                tmpContent = StringUtils.substring(tmpContent, StringUtils.indexOf(tmpContent, "```json")+7, StringUtils.lastIndexOf(tmpContent, "```"));
            }
            else if(StringUtils.startsWith(tmpContent, "```") && StringUtils.endsWith(tmpContent, "```")) {
                tmpContent = StringUtils.substring(tmpContent, StringUtils.indexOf(tmpContent, "```")+3, StringUtils.lastIndexOf(tmpContent, "```"));
            }
            else if(StringUtils.contains(tmpContent, "```json") && (StringUtils.indexOf(tmpContent, "```json")<StringUtils.lastIndexOf(tmpContent, "```"))) {
                tmpContent = StringUtils.substring(tmpContent, StringUtils.indexOf(tmpContent, "```json")+7, StringUtils.lastIndexOf(tmpContent, "```"));
            }
            else if(StringUtils.contains(tmpContent, "```") && (StringUtils.indexOf(tmpContent, "```")<StringUtils.lastIndexOf(tmpContent, "```"))) {
                tmpContent = StringUtils.substring(tmpContent, StringUtils.indexOf(tmpContent, "```")+3, StringUtils.lastIndexOf(tmpContent, "```"));
            }
            if(tmpContent.startsWith("\n")) {
                tmpContent= tmpContent.replaceFirst("^\\n", "");
            }
            if(tmpContent.endsWith("\n")) {
                tmpContent = tmpContent.replaceAll("\\n$", "");
            }
            tmpContent = tmpContent.trim();
            try {
                if (StringUtils.startsWith(tmpContent, "[") && StringUtils.endsWith(tmpContent, "]")) {
                    JSONArray.parseArray(tmpContent);
                } else if (StringUtils.startsWith(tmpContent, "{") && StringUtils.endsWith(tmpContent, "}")) {
                    JSONObject.parseObject(tmpContent);
                }
            } catch (JSONException e) {
                logger.error("checkJsonText error: ", e);
                return "jsonError: "+e.getMessage();
            }
        }
        return null;
    }
    public SuiteSummary getSuiteOutputSummary() {
        return SuiteSummaryHandler.dealSuiteOutputSummary(totalPoint, cmdCount, deducePointCount, lessThanSixPointsCount, lessThanThreePointsCount, outputMarkdown);
    }

    public List<TestCaseOutput> getTestCaseOutputList() {
        return testCaseOutputList;
    }

}
