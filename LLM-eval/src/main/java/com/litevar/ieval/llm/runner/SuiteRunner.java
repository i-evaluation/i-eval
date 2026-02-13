package com.litevar.ieval.llm.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.chat.*;
import com.litevar.ieval.llm.memory.ChatMemory;
import com.litevar.ieval.llm.memory.FullAmountMemory;
import com.litevar.ieval.llm.memory.SlidingWindowMemory;
import com.litevar.ieval.llm.tools.FunctionInfo;
import com.litevar.ieval.llm.tools.protocol.impl.HttpProtocolParser;
import com.litevar.ieval.llm.tools.protocol.impl.JsonRpcProtocolParser;
import com.litevar.ieval.llm.utils.TimeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author  action
 * @Date  2025/10/16 15:48 
 * @company litevar
 **/
public class SuiteRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public void run(String testPlanPath, String model, boolean slidingWindow) {
        ExcelProcessor excelProcessor = new ExcelProcessor();
        List<TestSuiteInput> testSuites = excelProcessor.readTestPlan(testPlanPath);
        if(testSuites != null && !testSuites.isEmpty()) {
            List<SuiteSummary> suiteSummaryList = new ArrayList<>();
            for (TestSuiteInput testSuite : testSuites) {
                logger.info("deal testSuite: {}, {}", testSuite.getSerial(), testSuite.getSuite());
                Map<Integer, TestCaseInput> testCaseInputMap = excelProcessor.readTestSuite(testSuite.getSuite());
                if(testCaseInputMap != null && !testCaseInputMap.isEmpty()) {
                    TestSuiteOutput testSuiteOutput = new TestSuiteOutput(testSuite.getRowNum());
                    OutputCollector collector = new OutputCollector(testSuite.getSuite());
                    boolean isContinuousChat;
                    if("单次对话".equals(testSuite.getCategory()) || "single conversation".equals(testSuite.getCategory())) {
                        isContinuousChat = false;
                    } else {
                        isContinuousChat = true;
                    }
                    int concurrency = 0;
                    if(StringUtils.isNoneBlank(testSuite.getConcurrency())) {
                        concurrency = (int)Double.parseDouble(testSuite.getConcurrency());
                    }
                    if(concurrency>1) {
                        logger.info("Concurrency testing mode: {}", concurrency);
                        if(concurrency>100) {
                            logger.warn("Concurrency > 100, set to 100");
                            concurrency = 100;
                        }
                        List<TestCaseInput> testCaseInputList = new ArrayList<>();
                        for (Map.Entry<Integer, TestCaseInput> integerTestCaseInputEntry : testCaseInputMap.entrySet()) {
                            testCaseInputList.add(integerTestCaseInputEntry.getValue());
                        }
                        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
                        for(int i=0; i<concurrency; i++) {
                            executor.submit(() -> {
                                batchChat(create(slidingWindow, collector, isContinuousChat, testSuite.getRowNum(), testSuite.getSuite(), testSuite.getPrompt(), testSuite.getLlmConfig(), testSuite.getSerial(), testSuite.getTools()), testCaseInputList);
                            });
                        }
                        executor.shutdown();
                        try {
                            // wait for executor to finish: 90 minutes
                            if (!executor.awaitTermination(90, TimeUnit.MINUTES)) {
                                // shutdown after timeout
                                executor.shutdownNow();
                                logger.warn("Concurrency test shutdown after 90 minutes");
                            }
                        } catch (InterruptedException e) {
                            executor.shutdownNow();
                            Thread.currentThread().interrupt();
                        }
                        logger.info("Concurrency test mode done");
                        String resultPath = excelProcessor.writeConcurrencyResultToSuite(testSuite.getSuite(), testCaseInputMap, collector.getTestCaseOutputList());
                        testSuiteOutput.setExecuteResult(resultPath);
                    }
                    else {
                        Agent agent = create(slidingWindow, collector, isContinuousChat, testSuite.getRowNum(), testSuite.getSuite(), testSuite.getPrompt(), testSuite.getLlmConfig(), testSuite.getSerial(), testSuite.getTools());
                        for (Map.Entry<Integer, TestCaseInput> entry : testCaseInputMap.entrySet()) {
                            agent.chat(entry.getValue());
                            TimeUtil.waitFor(15000);
                        }
                        boolean success = excelProcessor.writeResultToTestSuite(testSuite.getSuite(), collector.getTestCaseOutputList());
                        testSuiteOutput.setExecuteResult(success ? testSuite.getSuite() : "");
                    }
                    SuiteSummary suiteSummary = collector.getSuiteOutputSummary();
                    testSuiteOutput.setSummary(suiteSummary!=null?suiteSummary.getSummary():"");
                    excelProcessor.writeSummaryToTestPlan(testPlanPath, testSuiteOutput);
                    if(suiteSummary!=null){
                    	suiteSummary.setDescription(testSuite.getDescription());
	                    suiteSummaryList.add(suiteSummary);
	                }
                }
                else {
                    logger.warn("No test cases found in test suite: {}", testSuite.getSerial());
                }
                TimeUtil.waitFor(20000);
            }
            SuiteSummaryHandler.suiteSummaryToMarkdown(suiteSummaryList, model,  false);
        }
        else {
            logger.warn("No test suites found in test plan: {}", testPlanPath);
        }
    }

    public JSONObject dealTools(String serial, String tools) {
        if (StringUtils.isNoneBlank(tools) && StringUtils.startsWith(tools, "[") && StringUtils.endsWith(tools, "]")) {
            JSONArray toolArray = JSONArray.parseArray(tools);
            if (!toolArray.isEmpty()) {
                JSONArray functionInfoArray = new JSONArray();
                JSONArray functionDetailArray = new JSONArray();
                for (int i = 0; i < toolArray.size(); i++) {
                    JSONObject toolObj = toolArray.getJSONObject(i);
                    String apiKey = toolObj.containsKey("apiKey")&&toolObj.getString("apiKey")!=null?"Bearer "+toolObj.getString("apiKey"):null;
                    //like {"schema": "file/tools/schema.json"}
                    if(toolObj.containsKey("schema")) {
                        String schema = toolObj.getString("schema");
                        JSONObject parseResult = parseSchema(schema, apiKey);
                        if(parseResult != null) {
                            functionInfoArray.addAll(parseResult.getJSONArray("functionInfo"));
                            functionDetailArray.addAll(parseResult.getJSONArray("functionDetails"));
                        }
                    }
                    //like: {"schemaDir": "file/tools/subDirectory"}
                    else if(toolObj.containsKey("schemaDir")) {
                        List<String> schemaFiles = getSchemaFiles(toolObj.getString("schemaDir"));
                        if(schemaFiles != null && !schemaFiles.isEmpty()) {
                            for(String schemaFile: schemaFiles) {
                                JSONObject parseResult = parseSchema(schemaFile, apiKey);
                                if(parseResult != null) {
                                    functionInfoArray.addAll(parseResult.getJSONArray("functionInfo"));
                                    functionDetailArray.addAll(parseResult.getJSONArray("functionDetails"));
                                }
                            }
                        }
                        else {
                            logger.warn("No schema files found in directory: {}", toolObj.getString("schemaDir"));
                        }
                    }
                }
                if(!functionInfoArray.isEmpty() && !functionDetailArray.isEmpty()) {
                    JSONObject result = new JSONObject();
                    result.put("functionInfo", functionInfoArray);
                    result.put("functionDetails", functionDetailArray);
                    return result;
                }
            }
        } else {
            logger.warn("tools is not valid: {}, {}", serial, tools);
        }
        return null;
    }
    private List<String> getSchemaFiles(String schemaDir) {
        List<String> fileList = null;
        if(StringUtils.isNoneBlank(schemaDir)) {
            File schemaDirFile = new File(schemaDir);
            if(schemaDirFile.exists() && schemaDirFile.isDirectory()) {
                fileList = new ArrayList<>();
                File[] files = schemaDirFile.listFiles();
                if(files != null) {
                    for (File file : files) {
                        if(file.isFile() && (file.getName().endsWith(".json") || file.getName().endsWith(".yml") || file.getName().endsWith(".yaml"))) {
                            fileList.add(file.getAbsolutePath());
                        }
                    }
                }
            }
        }
        return fileList;
    }
    private JSONObject parseSchema(String schema, String apiKey) {
        if(StringUtils.isNoneBlank(schema)) {
            String raw = readFileToString(schema);
            JSONObject parseResult = null;
            if(StringUtils.isNoneBlank(raw) && (schema.endsWith(".yml")||schema.endsWith(".yaml"))) {
                parseResult = new HttpProtocolParser(apiKey).parse(raw, "openapi");
            }
            else if(StringUtils.isNoneBlank(raw) && schema.endsWith(".json")) {
                parseResult = new JsonRpcProtocolParser(apiKey).parse(raw, "jsonrpcHttp");
            }
            else {
                logger.error("schema: {} is blank or not yml or json file,", schema);
            }
            if(parseResult != null && 10200==parseResult.getIntValue("code")) {
                return parseResult;
            }
        }
        return null;
    }
    private String readFileToString(String filePath) {
        String result = null;
        if(filePath!=null){
            try {
                result = FileUtils.readFileToString(new File(filePath), "UTF-8");
            } catch (IOException e) {
                logger.error("readFileToString: ", e);
            }
        }
        return result;
    }
    private Agent create(boolean slidingWindow, OutputCollector collector, boolean isContinuousChat, int id, String name, String prompt, String llmConfig, String serial, String tools) {
        String sessionId = UUID.randomUUID().toString();
        logger.info("sessionId: {}", sessionId);
        JSONObject modelKeyObject = JSONObject.parseObject(llmConfig);
        ChatConfig chatConfig = JSON.toJavaObject(modelKeyObject.getJSONObject("chatConfig"), ChatConfig.class);
        ChatOptions chatOptions = JSON.toJavaObject(modelKeyObject.getJSONObject("chatOptions"), ChatOptions.class);
        ChatMemory memory = slidingWindow? new SlidingWindowMemory(30) : new FullAmountMemory();
        Agent agent = new Agent(id, name, prompt, chatConfig, chatOptions, memory, true, sessionId);
        agent.setOutputBus(collector.getOutputBus());
        agent.setIsContinuousChat(isContinuousChat);
        if(StringUtils.isNoneBlank(tools)) {
            JSONObject result = dealTools(serial, tools);
            if(result != null) {
                JSONArray functionInfoArray = result.getJSONArray("functionInfo");
                JSONArray functionDetailArray = result.getJSONArray("functionDetails");
                Map<String, FunctionInfo> functionInfoMap = new HashMap<>();
                for (int i = 0; i < functionInfoArray.size(); i++) {
                    FunctionInfo functionInfo = JSONObject.parseObject(functionInfoArray.getString(i), FunctionInfo.class);
                    functionInfoMap.put(functionInfo.getName(), functionInfo);
                }
                agent.setTools(functionDetailArray, functionInfoMap);
            }
        }
        return agent;
    }
    private void batchChat(Agent agent, List<TestCaseInput> testCaseInputList) {
        int caseSize = testCaseInputList.size();
        int batchSize = 50;
        if(caseSize<20) batchSize = 10;
        if(caseSize<50) batchSize = 20;
        logger.info("commands: {}, batchSize: {}", caseSize, batchSize);
        for(int i=0; i<batchSize; i++) {
            TestCaseInput testCaseInput = testCaseInputList.get(ThreadLocalRandom.current().nextInt(0, caseSize));
            if(StringUtils.isNoneBlank(testCaseInput.getCmd())) {
                agent.chat(testCaseInput);
            }
        }
    }
}
