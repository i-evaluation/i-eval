package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process")
public class TemplateStandardController extends BasedController {

    // 22. 上传工艺模板
    @PostMapping("/templates")
    public String uploadTemplate(@RequestBody String reqBody) {
        logger.info("Execute upload template...");
        com.alibaba.fastjson.JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("templateId", System.currentTimeMillis());
        result.put("templateName", body.getString("templateName"));
        result.put("createdAt", Instant.now());
        return result.toString();
    }

    // 23. 查询模板信息
    @GetMapping("/templates/{templateId}")
    public String getTemplate(@PathVariable Long templateId) {
        logger.info("Execute get template...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("templateId", templateId);
        result.put("templateName", "示例模板");
        return result.toString();
    }

    // 24. 查询所有模板
    @GetMapping("/templates")
    public String getAllTemplates() {
        logger.info("Execute get all templates...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("templates", new String[]{"模板A", "模板B"});
        return result.toString();
    }

    // 25. 上传工艺标准
    @PostMapping("/standards")
    public String uploadStandard(@RequestBody String reqBody) {
        logger.info("Execute upload standard...");
        com.alibaba.fastjson.JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("standardId", System.currentTimeMillis());
        result.put("standardName", body.getString("standardName"));
        return result.toString();
    }

    // 26. 查询标准信息
    @GetMapping("/standards/{standardId}")
    public String getStandard(@PathVariable Long standardId) {
        logger.info("Execute get standard...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("standardId", standardId);
        result.put("standardName", "示例标准");
        return result.toString();
    }

    // 27. 查询所有标准
    @GetMapping("/standards")
    public String getAllStandards() {
        logger.info("Execute get all standards...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("standards", new String[]{"标准A", "标准B"});
        return result.toString();
    }

    // 28. 上传模板应用记录
    @PostMapping("/applications/templates/{templateId}")
    public String uploadTemplateApplication(@PathVariable Long templateId, @RequestBody String reqBody) {
        logger.info("Execute upload template application...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        result.put("code", 200);
        result.put("templateId", templateId);
        result.put("applicationId", System.currentTimeMillis());
        return result.toString();
    }

    // 29. 查询模板应用记录
    @GetMapping("/applications/templates/{templateId}")
    public String getTemplateApplications(@PathVariable Long templateId) {
        logger.info("Execute get template applications...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("templateId", templateId);
        result.put("applications", new String[]{"应用记录1", "应用记录2"});
        return result.toString();
    }

    // 30. 上传标准应用记录
    @PostMapping("/applications/standards/{standardId}")
    public String uploadStandardApplication(@PathVariable Long standardId, @RequestBody String reqBody) {
        logger.info("Execute upload standard application...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        result.put("code", 200);
        result.put("standardId", standardId);
        result.put("applicationId", System.currentTimeMillis());
        return result.toString();
    }

    // 31. 查询标准应用记录
    @GetMapping("/applications/standards/{standardId}")
    public String getStandardApplications(@PathVariable Long standardId) {
        logger.info("Execute get standard applications...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();

        result.put("code", 200);
        result.put("standardId", standardId);
        result.put("applications", new String[]{"应用记录1", "应用记录2"});
        return result.toString();
    }
}
