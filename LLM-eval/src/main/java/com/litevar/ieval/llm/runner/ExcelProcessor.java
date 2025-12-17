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
    private final String[] cnHeaders1 = {"序号", "类别", "并发数", "提示词", "工具", "大模型", "用例集", "总结", "执行时间", "结果"};
    private final String[] cnHeaders2 = {"序号", "指令", "图片", "期望", "得分", "时长(总)", "首token(平均)", "token每秒(平均)", "生成token数(总)", "上下文长度", "上下文条数", "详情"};
    private final String[] enHeaders1 = {"No.", "Category", "Concurrency", "Prompt", "Tools", "LLM", "Test Suite", "Summary", "Execution Time", "Result"};
    private final String[] enHeaders2 = {"No.", "Instruction", "Image", "Expectation", "Score", "Duration(Total)", "First Token(Average)", "Tokens Per Second(Average)", "Generated Tokens(Total)", "Context Length", "Context Entries", "Details"};

    /**
     * 处理Excel文件，读取序号、类别、提示词、工具、大模型、用例集六列，
     * @param filePath Excel文件路径
     */
    public List<TestSuiteInput> readTestPlan(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            logger.info("套件总行数: {}", sheet.getLastRowNum());
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
                    if(StringUtils.isNoneBlank(prompt) && StringUtils.isNoneBlank(model) && StringUtils.isNoneBlank(suite)) {
                        testSuites.add(new TestSuiteInput(row.getRowNum(), serial, category, concurrency, prompt, tools, model, suite));
                    }
                    else {
                        logger.warn("文件: {}, 行: {}, 的提示词,大模型或用例集字段不能为空，跳过", filePath, i);
                    }
                }
                return testSuites;
            }
            else {
                logger.warn("文件: {}, 的标题行不符合要求(第一行必须为'序号、类别、并发数、提示词、工具、大模型、用例集、总结、执行时间、结果'10列)，请检查", filePath);
            }
        } catch (Exception e) {
            logger.error("unknownException: ", e);
        }
        return null;
    }
    //测试计划：将结果写入总结和执行时间两列
    public void writeSummaryToTestPlan(String filePath, TestSuiteOutput testSuite) {
        if(StringUtils.isNoneBlank(testSuite.getSummary()) && StringUtils.isNoneBlank(testSuite.getExecuteTime())) {
            try {
                FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
                int headerType = checkHeader(sheet.getRow(0));
                if(headerType == 1) {
                    Row row = sheet.getRow(testSuite.getRowNum());
                    // 写入总结和执行时间列
                    Cell summaryCell = row.createCell(7);
                    summaryCell.setCellValue(testSuite.getSummary());

                    Cell executionTimeCell = row.createCell(8);
                    executionTimeCell.setCellValue(testSuite.getExecuteTime());
                    if(StringUtils.isNoneBlank(testSuite.getExecuteResult())) {
                        //写入 文件
                        Cell resultCell = row.createCell(9);
                        resultCell.setCellValue(testSuite.getExecuteResult());
                    }
                    FileOutputStream fos = new FileOutputStream(new File(filePath));
                    workbook.write(fos);
                    fos.close();
                }
                else {
                    logger.warn("文件: {}, 的标题行不符合要求(第一行必须为'序号、类别、并发数、提示词、工具、大模型、用例集、总结、执行时间、结果'十列)，请检查", filePath);
                }
                workbook.close();
                fis.close();
            } catch (Exception e) {
                logger.error("unknownException: ", e);
            }
        }
    }
    /**
     * 处理Excel文件，读取序号、指令、期望三列，
     * @param filePath Excel文件路径
     */
    public Map<Integer, TestCaseInput> readTestSuite(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            logger.info("用例总行数: {}", sheet.getLastRowNum());
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
                        logger.warn("文件: {}, 行: {}, 的指令不能为空，跳过", filePath, i);
                    }
                }
                return testCases;
            }
            else {
                logger.warn("文件: {}, 的标题行不符合要求(第一行必须为'序号、指令、期望、得分、详情'5列)，请检查", filePath);
            }
        } catch (Exception e) {
            logger.error("unknownException: ", e);
        }
        return null;
    }
    //测试套件：将结果写入得分和详情两列(非并发测试)
    public boolean writeResultToTestSuite(String filePath, List<TestCaseOutput> testCases) {
        if(testCases!=null && !testCases.isEmpty()) {
            try {
                FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
                int headerType = checkHeader(sheet.getRow(0));
                if(headerType == 2) {
                    for (TestCaseOutput testCase : testCases) {
                        if(StringUtils.isNoneBlank(testCase.getScore()) && StringUtils.isNoneBlank(testCase.getDetails())) {
                            Row row = sheet.getRow(testCase.getRowNum());
                            // 写入得分和详情列
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
                            logger.warn("文件: {}, 行: {}, 的得分或详情不能为空，跳过", filePath, testCase.getRowNum());
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(new File(filePath));
                    workbook.write(fos);
                    fos.close();
                }
                else {
                    logger.warn("文件: {}, 的标题行不符合要求(第一行必须为'序号、指令、期望、得分、详情'五列)，请检查", filePath);
                }
                workbook.close();
                fis.close();
                return true;
            } catch (Exception e) {
                logger.error("unknownException: ", e);
            }
        }
        return false;
    }
    //并发套件：写入序号、指令、期望、得分、详情5列
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
                logger.info("并发测试，原文件：{},结果文件：{}", filePath, resultPath);
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
        logger.info("头列数：{}, headers1: {}, headers2: {}", row.getLastCellNum(), headers1.length, headers2.length);
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
     * 获取单元格的字符串值
     *
     * @param row 行对象
     * @param columnIndex 列索引
     * @return 字符串值
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
}
