package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/tools")
public class ToolController extends BasedController {

    // 14. 上传刀具信息
    @PostMapping
    public String uploadTool(@RequestBody String reqBody) {
        logger.info("Execute upload tool...");
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("toolId", System.currentTimeMillis());
        result.put("toolName", body.getString("toolName"));
        result.put("createdAt", Instant.now());
        return result.toString();
    }

    // 15. 查询刀具信息
    @GetMapping("/{toolId}")
    public String getTool(@PathVariable Long toolId) {
        logger.info("Execute get tool...");
        JSONObject result = new JSONObject();

        if (toolId == null || toolId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的刀具ID");
        } else {
            result.put("code", 200);
            result.put("toolId", toolId);
            result.put("toolName", "示例刀具");
            result.put("status", "正常");
        }
        return result.toString();
    }

    // 16. 上传刀具使用记录
    @PostMapping("/usage")
    public String uploadUsage(@RequestBody String reqBody) {
        logger.info("Execute upload tool usage...");
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("usageId", System.currentTimeMillis());
        result.put("toolId", body.getLong("toolId"));
        result.put("duration", body.getString("duration"));
        return result.toString();
    }

    // 17. 查询刀具使用记录
    @GetMapping("/usage/{toolId}")
    public String getUsage(@PathVariable Long toolId) {
        logger.info("Execute get tool usage...");
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("toolId", toolId);
        result.put("usageRecords", new String[]{"使用1h", "使用3h"});
        return result.toString();
    }

    // 18. 上传刀具消耗统计
    @PostMapping("/consumption")
    public String uploadConsumption(@RequestBody String reqBody) {
        logger.info("Execute upload tool consumption...");
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("consumptionId", System.currentTimeMillis());
        result.put("toolId", body.getLong("toolId"));
        result.put("consumptionValue", body.getString("consumptionValue"));
        return result.toString();
    }

    // 19. 查询刀具消耗统计
    @GetMapping("/consumption/{toolId}")
    public String getConsumption(@PathVariable Long toolId) {
        logger.info("Execute get tool consumption...");
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("toolId", toolId);
        result.put("consumptionValue", "50%");
        return result.toString();
    }

    // 20. 查询刀具剩余寿命
    @GetMapping("/life/{toolId}")
    public String getLife(@PathVariable Long toolId) {
        logger.info("Execute get tool life...");
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("toolId", toolId);
        result.put("remainingLife", "100小时");
        return result.toString();
    }

    // 21. 调整刀具寿命
    @PostMapping("/life/{toolId}/adjust")
    public String adjustLife(@PathVariable Long toolId, @RequestBody String reqBody) {
        logger.info("Execute adjust tool life...");
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("toolId", toolId);
        result.put("newLife", body.getString("newLife"));
        return result.toString();
    }
}
