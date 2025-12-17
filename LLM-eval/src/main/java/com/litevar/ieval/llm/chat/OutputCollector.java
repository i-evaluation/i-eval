package com.litevar.ieval.llm.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.litevar.ieval.llm.runner.TestCaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    private final String OUTPUT_DIR = "file/suite/";
    private String suite;
    private int cmdCount = 0;
    private int totalPoint = 0;
    private final List<TestCaseOutput> testCaseOutputList = new ArrayList<>();
    private int lessThanSixPointsCount = 0;
    private int lessThanThreePointsCount = 0;
    private int deducePointCount = 0;
    private float suitePoint = 0;
    private String suiteRatingLevel = "";

    public OutputCollector(String suite) {
        this.outputBus = new EventBus();
        this.outputBus.register(this);
        this.suite = suite.endsWith(".xlsx")||suite.endsWith(".xls") ? suite.substring(0, suite.lastIndexOf("."))+ System.currentTimeMillis()+".json" : OUTPUT_DIR + System.currentTimeMillis() + ".json";
        appendStringToFile("{\"details\":[\n");
    }

    @Subscribe
    public void onAgentEvent(AgentEvent event) {
        System.out.println("【输出收集器】收到事件来自节点 " + event.getNodeId() + "，类型: " + event.getEvent()+"，消息：" + event.getMessage());
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
                //json格式数据输出
                JSONObject jsonWriteObject = new JSONObject();
                JSONObject outputObject = JSONObject.parseObject(output);
                TestCaseOutput testCaseOutput = new TestCaseOutput(outputObject.getIntValue("testCaseId"));
                StringBuffer stringBuffer = new StringBuffer();
                //stringBuffer.append("# "+cmdCount).append(". 指令："+outputObject.getString("humanCmd")).append("\n");
                jsonWriteObject.put("cmd", outputObject.getString("humanCmd"));
                int expectCompletionTokens = 0;
                int expectFCCount = 0;
                String fcSequence = null;
                String[] fcSequences = null;
                String textFormat = null;
                JSONObject fcInfo=new JSONObject();
                if(outputObject.containsKey("expectation") && StringUtils.isNoneBlank(outputObject.getString("expectation"))) {
                    //stringBuffer.append("## 预期：").append(outputObject.getString("expectation")).append("\n");
                    JSONObject expectation = getExpectationObject(outputObject.getString("expectation"));
                    if(expectation!=null) {
                        jsonWriteObject.put("expectation", expectation);
                        if(expectation.containsKey("completionTokens")) {
                            expectCompletionTokens = expectation.getInteger("completionTokens");
                        }
                        if(expectation.containsKey("fcCount")) {
                            expectFCCount = expectation.getIntValue("fcCount");
                        }
                        if(expectation.containsKey("fcSequence")) {
                            fcSequence = expectation.getString("fcSequence");
                            if(fcSequence.contains(",")) {
                                fcSequences = fcSequence.split(",");
                            }
                            else {
                                fcSequences = new String[]{fcSequence};
                            }
                        }
                        if(expectation.containsKey("textFormat")) {
                            textFormat = expectation.getString("textFormat");
                        }
                        if(expectation.containsKey("fcInfo")) {
                            fcInfo = expectation.getJSONObject("fcInfo");
                        }
                    }
                }
                String reportTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(new Date().toInstant().atZone(ZoneId.systemDefault()));
                stringBuffer.append("## 时间：").append(reportTime).append("\n");
                jsonWriteObject.put("reportTime", reportTime);
                stringBuffer.append("## 输出：").append("\n");
                JSONArray llmOutput = outputObject.getJSONArray("llmOutput");
                //json格式数据输出
                jsonWriteObject.put("llmOutput", llmOutput);
                int testPoint = 10;
                StringBuffer deductionBuffer = new StringBuffer();
                if(!llmOutput.isEmpty()) {
                    long duration = 0;
                    long firstToken = 0;
                    double tokenPerSecond = 0;
                    int completionTokens = 0;
                    int promptTokens = 0;
                    int contextSize = 0;
                    JSONArray totalOriginalFunctions = new JSONArray();
                    int outputCount = llmOutput.size();
                    for (int i = 0; i < outputCount; i++) {
                        JSONObject item = llmOutput.getJSONObject(i);
                        stringBuffer.append("### ").append("时间令牌：").append("\n");
                        stringBuffer.append("  - ").append("模型：").append(item.get("model")).append("\n");
                        stringBuffer.append("  - ").append("首token时间：").append(item.get("firstToken")).append("\n");
                        stringBuffer.append("  - ").append("生成token数：").append(item.get("completionTokens")).append("\n");
                        stringBuffer.append("  - ").append("历时：").append(item.get("duration")).append("\n");
                        stringBuffer.append("  - ").append("token/s：").append(item.get("tokenPerSecond")).append("\n");
                        stringBuffer.append("  - ").append("上下文长度：").append(item.get("promptTokens")).append("\n");
                        stringBuffer.append("  - ").append("上下文条数：").append(item.get("contextSize")).append("\n");
                        duration += item.getLongValue("duration");
                        firstToken += item.getLongValue("firstToken");
                        tokenPerSecond += item.getDoubleValue("tokenPerSecond");
                        completionTokens += item.getIntValue("completionTokens");
                        promptTokens = item.getIntValue("promptTokens");
                        contextSize = item.getIntValue("contextSize");
                        if(item.containsKey("originalFunctions")) {
                            totalOriginalFunctions.addAll(item.getJSONArray("originalFunctions"));
                            stringBuffer.append("### ").append("函数调用：").append("\n");
                            stringBuffer.append("  - ").append("原始函数：").append(item.getJSONArray("originalFunctions")).append("\n");
                            stringBuffer.append("  - ").append("校正函数：").append(item.getJSONArray("correctFunctions")).append("\n");
                            stringBuffer.append("  - ").append("执行函数：").append(item.getJSONArray("executedFunctions")).append("\n");
                            if(item.containsKey("wrongArgFunctions")) {
                                stringBuffer.append("  - ").append("参数错误函数：").append(item.getJSONArray("wrongArgFunctions")).append("\n");
                                testPoint = testPoint -2;
                                deductionBuffer.append("  - 存在参数错误函数：").append(item.getJSONArray("wrongArgFunctions")).append("，减2分\n");
                            }
                            if(StringUtils.isNoneBlank(item.getString("unknownFunctions"))) {
                                stringBuffer.append("  - ").append("未知函数：").append(item.getString("unknownFunctions")).append("\n");
                                testPoint--;
                                deductionBuffer.append("  - 存在未知函数：").append(item.getString("unknownFunctions")).append("，减1分\n");
                            }
                            JSONArray correctFunctions=item.getJSONArray("correctFunctions");
                            if (!correctFunctions.isEmpty() && !fcInfo.isEmpty()) {
                            	for(int cf=0;cf<=correctFunctions.size();cf++) {
                            			JSONObject correctfctInfo=correctFunctions.getJSONObject(cf);
                                        if(correctfctInfo.containsKey("arguments")&& fcInfo.containsKey(correctfctInfo.getString("name"))) {
                                            JSONObject arguments = correctfctInfo.getJSONObject("arguments");
                                            if(arguments.size() != fcInfo.getInteger(correctfctInfo.getString("name"))) {
                                                testPoint = testPoint -5;
                                                logger.info("-------------------------------------------------");
                                                deductionBuffer.append("  - 调用函数参数错误，期望参数数：").append(fcInfo.getInteger(correctfctInfo.getString("name"))).append("，实际参数数：").append(arguments.keySet().size()).append("，减5分\n");
                                            }
                                            break;
                                        }
									}
                                }
                        }
                        else {
                            stringBuffer.append("### ").append("文本：").append("\n");
                            stringBuffer.append("   ```"+item.getString("text")).append("```\n");
                            if(textFormat!=null && textFormat.equals("json")) {
                                String jsonCheck = checkJsonTextFormat(item.getString("text"));
                                if(StringUtils.isNoneBlank(jsonCheck)) {
                                    testPoint = testPoint -5;
                                    deductionBuffer.append("  - JSON格式错误：").append(jsonCheck).append("，减5分\n");
                                }
                            }
                            if(item.getInteger("completionTokens") < expectCompletionTokens) {
                                testPoint = testPoint -5;
                                deductionBuffer.append("  - 模型生成token数(").append(item.getInteger("completionTokens")).append(")小于期望值("+expectCompletionTokens+")，减5分\n");
                            }
                        }
                        if(item.getInteger("firstToken") > 1000) {
                            testPoint--;
                            deductionBuffer.append("  - 首token时间(").append(item.getInteger("firstToken")).append(")大于1s，减1分\n");
                        }
                        if(item.getDouble("tokenPerSecond") < 10) {
                            testPoint--;
                            deductionBuffer.append("  - token/s(").append(item.getDouble("tokenPerSecond")).append(")小于10，减1分\n");
                        }
                        testPoint = testPoint + dealCompletionTokensDuration(item.getInteger("completionTokens"), item.getLong("duration"), deductionBuffer);
                    }
                    if(expectFCCount>0 && totalOriginalFunctions.size() < expectFCCount) {
                        testPoint = testPoint -5;
                        deductionBuffer.append("  - 生成函数数量：").append(totalOriginalFunctions.size()).append("，小于期望值：").append(expectFCCount).append("，减5分\n");
                    }
                    if(fcSequences!=null) {
                        if(totalOriginalFunctions.size() != fcSequences.length) {
                            testPoint = testPoint -5;
                            deductionBuffer.append("  - 生成函数数量：").append(totalOriginalFunctions.size()).append("，与期望值：").append(fcSequences.length).append("，不相等，减5分\n");
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
                                deductionBuffer.append("  - 函数调用顺序错误，实际顺序：").append(totalOriginalFunctions).append("，期望顺序：").append(fcSequence).append("，减5分\n");
                            }
                        }
                    }
                    stringBuffer.append("## 得分(基准分10)："+testPoint).append("\n");
                    jsonWriteObject.put("score", testPoint);
                    if(!deductionBuffer.isEmpty()) {
                        stringBuffer.append("## 减分原因：\n").append(deductionBuffer.substring(0, deductionBuffer.length()-1)).append("\n");
                        jsonWriteObject.put("deductionReason", deductionBuffer.substring(0, deductionBuffer.length()-1));
                        deducePointCount++;
                    }
                    if(testPoint<6) {
                        lessThanSixPointsCount++;
                        if(testPoint<3) {
                            lessThanThreePointsCount++;
                        }
                    }
                    totalPoint += testPoint;
                    testCaseOutput.setScore(String.valueOf(testPoint));
                    testCaseOutput.setDetails(stringBuffer.toString());
                    testCaseOutput.setDuration(duration);
                    testCaseOutput.setFirstToken(outputCount>1?firstToken/outputCount:firstToken);
                    testCaseOutput.setTokenPerSecond(outputCount>1?tokenPerSecond/outputCount:tokenPerSecond);
                    testCaseOutput.setCompletionTokens(completionTokens);
                    testCaseOutput.setPromptTokens(promptTokens);
                    testCaseOutput.setContextSize(contextSize);
                    testCaseOutputList.add(testCaseOutput);
                    appendStringToFile(jsonWriteObject.toJSONString()+",");
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
            deductionBuffer.append("  - 生成token数(").append(completionTokens).append(")小于11，且历时大于2s，减1分\n");
        }
        else if(completionTokens < 101 && duration > 3500) {
            deduce--;
            deductionBuffer.append("  - 生成token数(").append(completionTokens).append(")小于101，且历时大于3.5s，减1分\n");
        }
        else if(completionTokens < 1001 && duration > 8000) {
            deduce--;
            deductionBuffer.append("  - 生成token数(").append(completionTokens).append(")小于1001，且历时大于8s，减1分\n");
        }
        else if(completionTokens < 5001 && duration > 20000) {
            deduce--;
            deductionBuffer.append("  - 生成token数(").append(completionTokens).append(")小于5001，且历时大于20s，减1分\n");
        }
        else if(completionTokens < 10001 && duration > 45000) {
            deduce--;
            deductionBuffer.append("  - 生成token数(").append(completionTokens).append(")小于10001，且历时大于45s，减1分\n");
        }
        else if(completionTokens < 50001 && duration > 60000) {
            deduce--;
            deductionBuffer.append("  - 生成token数(").append(completionTokens).append(")小于50001，且历时大于60s，减1分\n");
        }
        else if(completionTokens < 100001 && duration > 90000) {
            deduce--;
            deductionBuffer.append("  - 生成token数(").append(completionTokens).append(")小于100001，且历时大于90s，减1分\n");
        }
        else if(duration > 120000) {
            deduce = deduce -2;
            deductionBuffer.append("  - 历时(").append(duration).append(")大于120s，减2分\n");
        }
        return deduce;
    }
    //检查json格式，返回异常信息
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
                return "json异常："+e.getMessage();
            }
        }
        return null;
    }
    public String getSuiteOutputSummary() {
        StringBuffer stringBuffer = new StringBuffer();
        JSONObject testSummary = new JSONObject();
        if(totalPoint > 0 && cmdCount > 0) {
            float averagePoint = (float)totalPoint/cmdCount;
            stringBuffer.append("# 测试结果汇总\n").append("## 总用例数："+cmdCount).append("\n平均分："+averagePoint).append("\n");
            float suiteBasicPoint = 10 * averagePoint;
            if(deducePointCount > 0) {
                float lessThanTenDeduce = (float) 10*(deducePointCount-lessThanSixPointsCount)/cmdCount;
                stringBuffer.append("## 低于10分用例数："+deducePointCount).append("，扣分："+lessThanTenDeduce).append("\n");
                suiteBasicPoint = suiteBasicPoint - lessThanTenDeduce;
                if(lessThanSixPointsCount>0) {
                    float lessThanSixDeduce = (float) 20*(lessThanSixPointsCount-lessThanThreePointsCount)/cmdCount;
                    stringBuffer.append("## 低于6分用例数："+lessThanSixPointsCount).append("，扣分："+lessThanSixDeduce).append("\n");
                    suiteBasicPoint = suiteBasicPoint - lessThanSixDeduce;
                }
                if(lessThanThreePointsCount>0) {
                    float lessThanThreeDeduce = (float) 30*lessThanThreePointsCount/cmdCount;
                    stringBuffer.append("## 低于3分用例数："+lessThanThreePointsCount).append("，扣分："+lessThanThreeDeduce).append("\n");
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
            testSummary.put("total", cmdCount);
            testSummary.put("average", averagePoint);
            testSummary.put("suitePoint", suiteBasicPoint);
            testSummary.put("ratingLevel", ratingLevel);
            stringBuffer.append("## 测试集\n### 得分："+suiteBasicPoint).append("\n### 评价等级："+ratingLevel);
            suitePoint = suiteBasicPoint;
            suiteRatingLevel = ratingLevel;
        }
        else{
            testSummary.put("total", 0);
            testSummary.put("average", 0);
        }
        appendStringToFile("\n],\"summary\":\n"+testSummary.toJSONString()+"}");
        return stringBuffer.toString();
    }
    private void appendStringToFile(String content){
        try {
            Path path = Paths.get(suite);
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            logger.error("write file error: ", e);
        }
    }

    public List<TestCaseOutput> getTestCaseOutputList() {
        return testCaseOutputList;
    }
    public float getSuitePoint() {
        return suitePoint;
    }
    public String getSuiteRatingLevel() {
        return suiteRatingLevel;
    }
}
