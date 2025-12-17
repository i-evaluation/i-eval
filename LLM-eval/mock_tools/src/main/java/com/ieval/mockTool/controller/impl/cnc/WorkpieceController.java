package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/workpieces")
public class WorkpieceController extends BasedController {

    // 5. 上传工件信息
    @PostMapping
    public String uploadWorkpiece(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload workpiece...");
        JSONObject result = new JSONObject();
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);

        TimeUnit.MILLISECONDS.sleep(200);
        result.put("code", 200);
        result.put("workpieceId", System.currentTimeMillis());
        result.put("workpieceName", body.getString("workpieceName"));
        result.put("createdAt", Instant.now());
        return result.toString();
    }

    // 6. 查询工件信息
    @GetMapping("/{workpieceId}")
    public String getWorkpiece(@PathVariable Long workpieceId) {
        logger.info("Execute get workpiece...");
        JSONObject result = new JSONObject();

        if (workpieceId == null || workpieceId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的工件ID");
        } else {
            result.put("code", 200);
            result.put("workpieceId", workpieceId);
            result.put("workpieceName", "示例工件");
        }
        return result.toString();
    }

    // 7. 查询所有工件
    @GetMapping
    public String getAllWorkpieces() {
        logger.info("Execute get all workpieces...");
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("workpieces", new String[]{"工件A", "工件B"});
        return result.toString();
    }

    // 8. 上传工件批次信息
    @PostMapping("/{workpieceId}/batch")
    public String uploadBatch(@PathVariable Long workpieceId, @RequestBody String reqBody) {
        logger.info("Execute upload batch...");
        JSONObject result = new JSONObject();
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);

        result.put("code", 200);
        result.put("workpieceId", workpieceId);
        result.put("batchId", System.currentTimeMillis());
        result.put("batchName", body.getString("batchName"));
        return result.toString();
    }

    // 9. 查询批次信息
    @GetMapping("/batch/{batchId}")
    public String getBatch(@PathVariable Long batchId) {
        logger.info("Execute get batch...");
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("batchId", batchId);
        result.put("batchName", "示例批次");
        return result.toString();
    }

    // 10. 上传运输记录
    @PostMapping("/{workpieceId}/transport")
    public String uploadTransport(@PathVariable Long workpieceId, @RequestBody String reqBody) {
        logger.info("Execute upload transport...");
        JSONObject result = new JSONObject();
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);

        result.put("code", 200);
        result.put("workpieceId", workpieceId);
        result.put("transportId", System.currentTimeMillis());
        result.put("route", body.getString("route"));
        return result.toString();
    }

    // 11. 查询运输记录
    @GetMapping("/{workpieceId}/transport")
    public String getTransport(@PathVariable Long workpieceId) {
        logger.info("Execute get transport...");
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("workpieceId", workpieceId);
        result.put("transportRecords", new String[]{"运输1", "运输2"});
        return result.toString();
    }

    // 12. 上传存储位置
    @PostMapping("/{workpieceId}/storage")
    public String uploadStorage(@PathVariable Long workpieceId, @RequestBody String reqBody) {
        logger.info("Execute upload storage...");
        JSONObject result = new JSONObject();
        JSONObject body = com.alibaba.fastjson.JSON.parseObject(reqBody);

        result.put("code", 200);
        result.put("workpieceId", workpieceId);
        result.put("location", body.getString("location"));
        return result.toString();
    }

    // 13. 查询存储位置
    @GetMapping("/{workpieceId}/storage")
    public String getStorage(@PathVariable Long workpieceId) {
        logger.info("Execute get storage...");
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("workpieceId", workpieceId);
        result.put("location", "仓库A-1");
        return result.toString();
    }
}
