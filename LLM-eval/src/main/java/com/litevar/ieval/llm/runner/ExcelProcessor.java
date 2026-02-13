package com.litevar.ieval.llm.runner;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @Author  action
 * @Date  2025/10/15 17:04
 * @company litevar
 **/
public class ExcelProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String[] cnHeaders1 = {"序号", "类别", "并发数", "提示词", "工具", "大模型", "用例集", "描述", "总结", "执行时间", "结果"};
    private final String[] cnHeaders2 = {"序号", "指令", "图片", "期望", "得分", "时长(总)", "首token(平均)", "token每秒(平均)", "生成token数(总)", "上下文长度", "上下文条数", "详情"};
    private final String[] enHeaders1 = {"No.", "Category", "Concurrency", "Prompt", "Tools", "LLM", "Test Suite", "Description", "Summary", "Execution Time", "Result"};
    private final String[] enHeaders2 = {"No.", "Instruction", "Image", "Expectation", "Score", "Duration(Total)", "First Token(Average)", "Tokens Per Second(Average)", "Generated Tokens(Total)", "Context Length", "Context Entries", "Details"};

    /**
     * Process Excel files, read 8 columns: serial number, category, concurrency, prompt, tools, large model, test suite, description,
     * @param filePath Excel file path
     */
    public List<TestSuiteInput> readTestPlan(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            logger.info("Total suite rows: {}", sheet.getLastRowNum());
            int headerType = checkHeader(sheet.getRow(0));
            if(headerType == 1) {
                List<TestSuiteInput> testSuites = new ArrayList<>();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    String serial = getStringCellValue(row, 0);
                    String category = getStringCellValue(row, 1);
                    String concurrency = getStringCellValue(row, 2);
                    String prompt = getStringCellValue(row, 3);
                    String tools = getStringCellValue(row, 4);
                    String model = getStringCellValue(row, 5);
                    String suite = getStringCellValue(row, 6);
                    String description = getStringCellValue(row, 7);
                    if(StringUtils.isNoneBlank(prompt) && StringUtils.isNoneBlank(model) && StringUtils.isNoneBlank(suite)) {
                        testSuites.add(new TestSuiteInput(row.getRowNum(), serial, category, concurrency, prompt, tools, model, suite, description));
                    }
                    else {
                        logger.warn("File: {}, Row: {}, Prompt, Large Model or Test Suite fields cannot be empty, skipping", filePath, i);
                    }
                }
                return testSuites;
            }
            else {
                logger.warn("File: {}, Header row does not meet requirements (first row must be 'No.,Category,Concurrency,Prompt,Tools,LLM,Test Suite,Description,Summary,Execution Time,Result' 11 columns), please check", filePath);
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            logger.error("unknownException: ", e);
        }
        return null;
    }
    //Test plan: Write results to Summary, Execution Time, and Result columns
    public void writeSummaryToTestPlan(String filePath, TestSuiteOutput testSuite) {
        if(StringUtils.isNoneBlank(testSuite.getSummary()) && StringUtils.isNoneBlank(testSuite.getExecuteTime())) {
            try {
                FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0);
                int headerType = checkHeader(sheet.getRow(0));
                if(headerType == 1) {
                    Row row = sheet.getRow(testSuite.getRowNum());
                    //write Summary, Execution Time
                    Cell summaryCell = row.createCell(8);
                    summaryCell.setCellValue(testSuite.getSummary());

                    Cell executionTimeCell = row.createCell(9);
                    executionTimeCell.setCellValue(testSuite.getExecuteTime());
                    if(StringUtils.isNoneBlank(testSuite.getExecuteResult())) {
                        //write result(testcases result file)
                        Cell resultCell = row.createCell(10);
                        resultCell.setCellValue(testSuite.getExecuteResult());
                    }
                    FileOutputStream fos = new FileOutputStream(new File(filePath));
                    workbook.write(fos);
                    fos.close();
                }
                else {
                    logger.warn("File: {}, Header row does not meet requirements (first row must be 'No.,Category,Concurrency,Prompt,Tools,LLM,Test Suite,Description,Summary,Execution Time,Result' 11 columns)", filePath);
                }
                workbook.close();
                fis.close();
            } catch (Exception e) {
                logger.error("unknownException: ", e);
            }
        }
    }
    /**
     * Process Excel files, read three columns: serial number, instruction, expectation,
     * @param filePath Excel file path
     */
    public Map<Integer, TestCaseInput> readTestSuite(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            logger.info("Total case rows: {}", sheet.getLastRowNum());
            int headerType = checkHeader(sheet.getRow(0));
            if(headerType == 2) {
                Map<Integer, TestCaseInput> testCases = new HashMap<>();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    String serial = getStringCellValue(row, 0);
                    String cmd = getStringCellValue(row, 1);
                    String picture = getStringCellValue(row, 2);
                    String expectation = getStringCellValue(row, 3);
                    if(StringUtils.isNoneBlank(cmd)) {
                        testCases.put(row.getRowNum(), new TestCaseInput(row.getRowNum(), serial, cmd, picture, expectation));
                    }
                    else {
                        logger.warn("File: {}, Row: {}, Instruction cannot be empty, skipping", filePath, i);
                    }
                }
                return testCases;
            }
            else {
                logger.warn("File: {}, Header row does not meet requirements (first row must be 'No.,Instruction,Image,Expectation,Score,Duration(Total),First Token(Average),Tokens Per Second(Average),Generated Tokens(Total),Context Length,Context Entries,Details' 12 columns), please check", filePath);
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            logger.error("unknownException: ", e);
        }
        return null;
    }
    //Test suite: Write results to Score and Details columns (non-concurrent testing)
    public boolean writeResultToTestSuite(String filePath, List<TestCaseOutput> testCases) {
        if(testCases!=null && !testCases.isEmpty()) {
            try {
                FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0);
                int headerType = checkHeader(sheet.getRow(0));
                if(headerType == 2) {
                    for (TestCaseOutput testCase : testCases) {
                        if(StringUtils.isNoneBlank(testCase.getScore()) && StringUtils.isNoneBlank(testCase.getDetails())) {
                            Row row = sheet.getRow(testCase.getRowNum());
                            Cell scoreCell = row.createCell(4);
                            scoreCell.setCellValue(testCase.getScore());
                            Cell durationCell = row.createCell(5);
                            durationCell.setCellValue(testCase.getDuration());
                            Cell firstTokenCell = row.createCell(6);
                            firstTokenCell.setCellValue(testCase.getFirstToken());
                            Cell tokenPerSecondCell = row.createCell(7);
                            tokenPerSecondCell.setCellValue(testCase.getTokenPerSecond());
                            Cell completionTokensCell = row.createCell(8);
                            completionTokensCell.setCellValue(testCase.getCompletionTokens());
                            Cell promptTokensCell = row.createCell(9);
                            promptTokensCell.setCellValue(testCase.getPromptTokens());
                            Cell contextSizeCell = row.createCell(10);
                            contextSizeCell.setCellValue(testCase.getContextSize());
                            Cell detailsCell = row.createCell(11);
                            detailsCell.setCellValue(testCase.getDetails());
                        }
                        else {
                            logger.warn("File: {}, Row: {}, Score or Details cannot be empty, skipping", filePath, testCase.getRowNum());
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(new File(filePath));
                    workbook.write(fos);
                    fos.close();
                }
                else {
                    logger.warn("File: {}, Header row does not meet requirements (first row must be 'No.,Instruction,Image,Expectation,Score,Duration(Total),First Token(Average),Tokens Per Second(Average),Generated Tokens(Total),Context Length,Context Entries,Details' 12 columns)", filePath);
                }
                workbook.close();
                fis.close();
                return true;
            } catch (Exception e) {
                logger.error("unknownException: ", e);
            }
        }
        else {
            logger.warn("File: {}, No test cases to write", filePath);
        }
        return false;
    }
    //Concurrent suite: Write 5 columns - Serial Number, Instruction, Expectation, Score, and Details
    public String writeConcurrencyResultToSuite(String filePath, Map<Integer, TestCaseInput> inputMap, List<TestCaseOutput> outputList) {
        if(outputList!=null && !outputList.isEmpty()) {
            try {
                String resultPath = null;
                if(filePath.endsWith(".xls")) {
                    resultPath = filePath.substring(0, filePath.lastIndexOf(".")) +"_result" + System.currentTimeMillis()+ ".xls";
                }
                else {
                    resultPath = filePath.substring(0, filePath.lastIndexOf(".")) +"_result" + System.currentTimeMillis()+ ".xlsx";
                }
                File resultFile = new File(resultPath);
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Results");
                String[] headers2 = isChinese() ? cnHeaders2 : enHeaders2;
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers2.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers2[i]);
                }
                logger.info("header row lastCellNum: {}", headerRow.getLastCellNum());
                int rowIdx = 1;
                for (TestCaseOutput output : outputList) {
                    TestCaseInput testCaseInput = inputMap.get(output.getRowNum());
                    if(testCaseInput!=null) {
                        Row row = sheet.createRow(rowIdx);
                        Cell serialCell = row.createCell(0);
                        serialCell.setCellValue(testCaseInput.getSerial());
                        Cell cmdCell = row.createCell(1);
                        cmdCell.setCellValue(testCaseInput.getCmd());
                        Cell pictureCell = row.createCell(2);
                        pictureCell.setCellValue(testCaseInput.getPicture());
                        Cell expectationCell = row.createCell(3);
                        expectationCell.setCellValue(testCaseInput.getExpectation());
                        Cell scoreCell = row.createCell(4);
                        scoreCell.setCellValue(output.getScore());
                        Cell durationCell = row.createCell(5);
                        durationCell.setCellValue(output.getDuration());
                        Cell firstTokenCell = row.createCell(6);
                        firstTokenCell.setCellValue(output.getFirstToken());
                        Cell tokenPerSecondCell = row.createCell(7);
                        tokenPerSecondCell.setCellValue(output.getTokenPerSecond());
                        Cell completionTokensCell = row.createCell(8);
                        completionTokensCell.setCellValue(output.getCompletionTokens());
                        Cell promptTokensCell = row.createCell(9);
                        promptTokensCell.setCellValue(output.getPromptTokens());
                        Cell contextSizeCell = row.createCell(10);
                        contextSizeCell.setCellValue(output.getContextSize());
                        Cell detailsCell = row.createCell(11);
                        detailsCell.setCellValue(output.getDetails());
                        rowIdx++;
                    }
                }
                FileOutputStream fos = new FileOutputStream(resultFile);
                workbook.write(fos);
                fos.close();
                workbook.close();
                logger.info("Concurrent testing, original file: {}, result file: {}", filePath, resultPath);
                return resultPath;
            } catch (Exception e) {
                logger.error("unknownException: ", e);
            }
        }
        return null;
    }
    private int checkHeader(Row row) {
        if (row == null || row.getLastCellNum() < 1) {
            return 0;
        }
        boolean isCN = isChinese();
        String[] headers1 = isCN ? cnHeaders1 : enHeaders1;
        String[] headers2 = isCN ? cnHeaders2 : enHeaders2;
        logger.info("header column count: {}, headers1: {}, headers2: {}", row.getLastCellNum(), headers1.length, headers2.length);
        if(row.getLastCellNum() == headers1.length) {
            for (int i = 0; i < headers1.length; i++) {
                if (!headers1[i].equals(row.getCell(i).getStringCellValue())) {
                    return 0;
                }
            }
            return 1;
        }
        else if(row.getLastCellNum() == headers2.length) {
            for (int i = 0; i < headers2.length; i++) {
                if (!headers2[i].equals(row.getCell(i).getStringCellValue())) {
                    return 0;
                }
            }
            return 2;
        }
        return 0;
    }
    /**
     * Get the string value of a cell
     *
     * @param row Row object
     * @param columnIndex Column index
     * @return String value
     */
    private String getStringCellValue(Row row, int columnIndex) {
        if (row == null) {
            return "";
        }

        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    private boolean isChinese() {
        Locale defaultLocale = Locale.getDefault();
        String language = defaultLocale.getLanguage();
        return "zh".equals(language);
    }

    /** revise logic
     *
     * @param filePath
     * @return TestedSuite
     */
    public List<TestedSuite> getTestedSuiteList(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            logger.info("Total suite rows: {}", sheet.getLastRowNum());
            int headerType = checkHeader(sheet.getRow(0));
            if(headerType == 1) {
                List<TestedSuite> suiteList = new ArrayList<>();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    String description = getStringCellValue(row, 7);
                    String result = getStringCellValue(row, 10);
                    if(StringUtils.isNoneBlank(description) && StringUtils.isNoneBlank(result)) {
                        suiteList.add(new TestedSuite(row.getRowNum(), description, result));
                    }
                    else {
                        logger.warn("File: {}, Row: {}, description, Result fields cannot be empty, skipping", filePath, i);
                    }
                }
                return suiteList;
            }
            else {
                logger.warn("File: {}, Header row does not meet requirements (first row must be 'No.,Category,Concurrency,Prompt,Tools,LLM,Test Suite,Description,Summary,Execution Time,Result' 11 columns), please check", filePath);
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            logger.error("unknownException: ", e);
        }
        return null;
    }
    public SuiteSummary readTestedSuite(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            logger.info("Total case rows: {}", sheet.getLastRowNum());
            int headerType = checkHeader(sheet.getRow(0));
            if(headerType == 2) {
                String outputMarkdown = "markdown/";
                if((filePath.endsWith(".xlsx")||filePath.endsWith(".xls"))) {
                    if(filePath.contains("/")) {
                        outputMarkdown = outputMarkdown + filePath.substring(filePath.lastIndexOf("/")+1, filePath.lastIndexOf("."))+ System.currentTimeMillis()+"_r.md";
                    }
                    else if(filePath.contains("\\")) {
                        outputMarkdown = outputMarkdown + filePath.substring(filePath.lastIndexOf("\\")+1, filePath.lastIndexOf("."))+ System.currentTimeMillis()+"_r.md";
                    }
                }
                else {
                    outputMarkdown = outputMarkdown + System.currentTimeMillis() + "r_.md";
                }
                SuiteSummaryHandler.appendStringToFile(outputMarkdown,"| No. | Instruction | Image | Expectation | Score | Duration(Total,ms) | First Token(Average) | Tokens Per Second(Average) | Generated Tokens(Total) | Context Length | Context Entries | Details |\n| --- | --- | --- | --- | :---: | :---: | :---: | :---: | --- | --- | --- | --- |\n");
                int cmdCount = 0;
                double totalPoint = 0;
                int deducePointCount = 0;
                int lessThanSixPointsCount = 0;
                int lessThanThreePointsCount = 0;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    StringBuilder markdownBuffer = new StringBuilder();
                    String serial = getStringCellValue(row, 0);
                    String cmd = getStringCellValue(row, 1);
                    String picture = getStringCellValue(row, 2);
                    String expectation = getStringCellValue(row, 3);
                    String point = getStringCellValue(row, 4);
                    if(StringUtils.isNoneBlank(cmd) && StringUtils.isNoneBlank(point)) {
                        cmdCount++;
                        double score = Double.parseDouble(point);
                        totalPoint += score;
                        if(score < 10) {
                            deducePointCount++;
                            if(score < 6) {
                                lessThanSixPointsCount++;
                                if(score < 3) {
                                    lessThanThreePointsCount++;
                                    if(score<0) {
                                        score = 0;
                                    }
                                }
                            }
                        }
                        String duration = getStringCellValue(row, 5);
                        String firstToken = getStringCellValue(row, 6);
                        String tokensPerSecond = getStringCellValue(row, 7);
                        double tokenPerSecond = Double.parseDouble(tokensPerSecond);
                        String generatedTokens = getStringCellValue(row, 8);
                        String contextLength = getStringCellValue(row, 9);
                        String contextEntries = getStringCellValue(row, 10);
                        String details = getStringCellValue(row, 11);
                        if(details.endsWith("\n")) {
                            details = details.substring(0, details.length()-1);
                        }
                        details = details.replaceAll("\n", "<br>");
                        markdownBuffer.append("| ").append(serial).append(" | ").append(cmd.replaceAll("\\R", "<br>")).append(" | ").append(picture!=null?picture:"").append(" | ").append(expectation!=null?expectation:"").append(" | ");
                        markdownBuffer.append(score).append(" | ").append(duration).append(" | ").append(firstToken).append(" | ").append(String.format("%.2f", tokenPerSecond)).append(" | ").append(generatedTokens).append(" | ").append(contextLength).append(" | ").append(contextEntries).append(" | ").append(details).append(" |\n");
                        SuiteSummaryHandler.appendStringToFile(outputMarkdown, markdownBuffer.toString());
                    }
                    else {
                        logger.warn("File: {}, Row: {}, Instruction or Score column is null, skipping", filePath, i);
                    }
                }
                return SuiteSummaryHandler.dealSuiteOutputSummary((int)totalPoint, cmdCount, deducePointCount, lessThanSixPointsCount, lessThanThreePointsCount, outputMarkdown);
            }
            else {
                logger.warn("File: {}, Header row does not meet requirements (first row must be 'No.,Instruction,Image,Expectation,Score,Duration(Total),First Token(Average),Tokens Per Second(Average),Generated Tokens(Total),Context Length,Context Entries,Details' 12 columns), please check", filePath);
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            logger.error("unknownException: ", e);
        }
        return null;
    }
    public void updateSuiteSummaryToTestPlan(String filePath, int rowNum, SuiteSummary suiteSummary) {
        if(StringUtils.isNoneBlank(suiteSummary.getSummary()) && rowNum>0) {
            try {
                FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0);
                int headerType = checkHeader(sheet.getRow(0));
                if(headerType == 1) {
                    Row row = sheet.getRow(rowNum);
                    //only update summary
                    Cell summaryCell = row.createCell(8);
                    summaryCell.setCellValue(suiteSummary.getSummary());
                    FileOutputStream fos = new FileOutputStream(new File(filePath));
                    workbook.write(fos);
                    fos.close();
                    logger.info("File: {}, Row: {}, Summary updated", filePath, rowNum);
                }
                else {
                    logger.warn("File: {}, Header row does not meet requirements (first row must be 'No.,Category,Concurrency,Prompt,Tools,LLM,Test Suite,Description,Summary,Execution Time,Result' 11 columns)", filePath);
                }
                workbook.close();
                fis.close();
            } catch (Exception e) {
                logger.error("unknownException: ", e);
            }
        }
    }
}
