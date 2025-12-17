package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/supply")
public class SupplyChainManagementController extends BasedController {

    // 110. 上传物料信息
    @PostMapping("/materials")
    public String uploadMaterialInfo(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload material info...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("materialId", System.currentTimeMillis());
        result.put("materialCode", requestBody.getString("materialCode"));
        result.put("materialName", requestBody.getString("materialName"));
        result.put("category", requestBody.getString("category"));
        result.put("specification", requestBody.getString("specification"));
        result.put("unit", requestBody.getString("unit"));
        result.put("supplier", requestBody.getString("supplier"));
        result.put("unitPrice", requestBody.getDouble("unitPrice"));
        result.put("minimumStock", requestBody.getInteger("minimumStock"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload material info...");
        return result.toString();
    }

    // 111. 查询物料信息
    @GetMapping("/materials/{materialId}")
    public String getMaterialInfo(@PathVariable Long materialId) throws InterruptedException {
        logger.info("Execute get material info...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (materialId == null || materialId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的物料ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("materialId", materialId);
            result.put("materialCode", "MAT-001");
            result.put("materialName", "铝合金板材");
            result.put("category", "原材料");
            result.put("specification", "6061-T6");
            result.put("unit", "块");
            result.put("supplier", "供应商A");
            result.put("unitPrice", 150.0);
            result.put("minimumStock", 50);
            result.put("createdAt", "2025-09-01T10:00:00Z");
            logger.info("Success to get material info...");
        }
        return result.toString();
    }

    // 112. 查询所有物料
    @GetMapping("/materials")
    public String getAllMaterials() throws InterruptedException {
        logger.info("Execute get all materials...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        com.alibaba.fastjson.JSONArray materials = new com.alibaba.fastjson.JSONArray();
        materials.add(createMaterialObject(1L, "MAT-001", "铝合金板材", "原材料", "6061-T6", "块", 150.0, 50));
        materials.add(createMaterialObject(2L, "MAT-002", "不锈钢棒材", "原材料", "304", "根", 200.0, 30));
        materials.add(createMaterialObject(3L, "MAT-003", "硬质合金刀具", "工具", "Φ10mm", "把", 80.0, 20));
        result.put("materials", materials);
        logger.info("Success to get all materials...");
        return result.toString();
    }

    // 113. 查询物料库存
    @GetMapping("/materials/{materialId}/stock")
    public String getMaterialStock(@PathVariable Long materialId) throws InterruptedException {
        logger.info("Execute get material stock...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (materialId == null || materialId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的物料ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("materialId", materialId);
            result.put("currentStock", 120);
            result.put("minimumStock", 50);
            result.put("maximumStock", 200);
            result.put("stockStatus", "正常");
            result.put("lastUpdated", "2025-09-23T10:30:00Z");
            logger.info("Success to get material stock...");
        }
        return result.toString();
    }

    // 114. 上传采购订单
    @PostMapping("/orders")
    public String uploadPurchaseOrder(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload purchase order...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("orderId", System.currentTimeMillis());
        result.put("orderNumber", requestBody.getString("orderNumber"));
        result.put("supplier", requestBody.getString("supplier"));
        result.put("orderDate", requestBody.getString("orderDate"));
        result.put("expectedDeliveryDate", requestBody.getString("expectedDeliveryDate"));
        result.put("items", requestBody.getJSONArray("items"));
        result.put("totalAmount", requestBody.getDouble("totalAmount"));
        result.put("status", "待审核");
        result.put("createdBy", requestBody.getString("createdBy"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload purchase order...");
        return result.toString();
    }

    // 115. 查询采购订单
    @GetMapping("/orders/{orderId}")
    public String getPurchaseOrder(@PathVariable Long orderId) throws InterruptedException {
        logger.info("Execute get purchase order...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (orderId == null || orderId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的订单ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("orderId", orderId);
            result.put("orderNumber", "PO-20250923-001");
            result.put("supplier", "供应商A");
            result.put("orderDate", "2025-09-23T10:00:00Z");
            result.put("expectedDeliveryDate", "2025-09-30T10:00:00Z");
            com.alibaba.fastjson.JSONArray items = new com.alibaba.fastjson.JSONArray();
            items.add(createOrderItemObject("MAT-001", "铝合金板材", "块", 100, 150.0));
            items.add(createOrderItemObject("MAT-003", "硬质合金刀具", "把", 50, 80.0));
            result.put("items", items);
            result.put("totalAmount", 19000.0);
            result.put("status", "已审核");
            result.put("createdBy", "采购员");
            result.put("createdAt", "2025-09-23T10:00:00Z");
            logger.info("Success to get purchase order...");
        }
        return result.toString();
    }

    // 116. 查询所有采购订单
    @GetMapping("/orders")
    public String getAllPurchaseOrders() throws InterruptedException {
        logger.info("Execute get all purchase orders...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        com.alibaba.fastjson.JSONArray orders = new com.alibaba.fastjson.JSONArray();
        orders.add(createOrderObject(1L, "PO-20250923-001", "供应商A", "2025-09-23", "已审核"));
        orders.add(createOrderObject(2L, "PO-20250922-001", "供应商B", "2025-09-22", "已发货"));
        orders.add(createOrderObject(3L, "PO-20250921-001", "供应商C", "2025-09-21", "已完成"));
        result.put("orders", orders);
        logger.info("Success to get all purchase orders...");
        return result.toString();
    }

    // 117. 上传供应商信息
    @PostMapping("/suppliers")
    public String uploadSupplierInfo(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload supplier info...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("supplierId", System.currentTimeMillis());
        result.put("supplierCode", requestBody.getString("supplierCode"));
        result.put("supplierName", requestBody.getString("supplierName"));
        result.put("contactPerson", requestBody.getString("contactPerson"));
        result.put("contactInfo", requestBody.getJSONObject("contactInfo"));
        result.put("address", requestBody.getString("address"));
        result.put("materials", requestBody.getJSONArray("materials"));
        result.put("rating", requestBody.getDouble("rating"));
        result.put("status", "活跃");
        result.put("createdAt", Instant.now());
        logger.info("Success to upload supplier info...");
        return result.toString();
    }

    // 118. 查询供应商信息
    @GetMapping("/suppliers/{supplierId}")
    public String getSupplierInfo(@PathVariable Long supplierId) throws InterruptedException {
        logger.info("Execute get supplier info...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (supplierId == null || supplierId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的供应商ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("supplierId", supplierId);
            result.put("supplierCode", "SUP-001");
            result.put("supplierName", "供应商A");
            result.put("contactPerson", "张经理");
            com.alibaba.fastjson.JSONObject contactInfo = new com.alibaba.fastjson.JSONObject();
            contactInfo.put("phone", "13900139000");
            contactInfo.put("email", "zhang@supplierA.com");
            result.put("contactInfo", contactInfo);
            result.put("address", "北京市朝阳区XX路XX号");
            com.alibaba.fastjson.JSONArray materials = new com.alibaba.fastjson.JSONArray();
            materials.add("铝合金板材");
            materials.add("不锈钢棒材");
            result.put("materials", materials);
            result.put("rating", 4.5);
            result.put("status", "活跃");
            result.put("createdAt", "2025-09-01T10:00:00Z");
            logger.info("Success to get supplier info...");
        }
        return result.toString();
    }

    // 119. 查询所有供应商
    @GetMapping("/suppliers")
    public String getAllSuppliers() throws InterruptedException {
        logger.info("Execute get all suppliers...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        com.alibaba.fastjson.JSONArray suppliers = new com.alibaba.fastjson.JSONArray();
        suppliers.add(createSupplierObject(1L, "SUP-001", "供应商A", "张经理", 4.5, "活跃"));
        suppliers.add(createSupplierObject(2L, "SUP-002", "供应商B", "李经理", 4.2, "活跃"));
        suppliers.add(createSupplierObject(3L, "SUP-003", "供应商C", "王经理", 3.8, "暂停"));
        result.put("suppliers", suppliers);
        logger.info("Success to get all suppliers...");
        return result.toString();
    }

    // 120. 上传交付记录
    @PostMapping("/deliveries")
    public String uploadDeliveryRecord(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload delivery record...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("deliveryId", System.currentTimeMillis());
        result.put("orderId", requestBody.getString("orderId"));
        result.put("supplier", requestBody.getString("supplier"));
        result.put("deliveryDate", requestBody.getString("deliveryDate"));
        result.put("items", requestBody.getJSONArray("items"));
        result.put("receivedBy", requestBody.getString("receivedBy"));
        result.put("status", "已接收");
        result.put("createdAt", Instant.now());
        logger.info("Success to upload delivery record...");
        return result.toString();
    }

    // 121. 查询交付记录
    @GetMapping("/deliveries/{deliveryId}")
    public String getDeliveryRecord(@PathVariable Long deliveryId) throws InterruptedException {
        logger.info("Execute get delivery record...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (deliveryId == null || deliveryId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的交付记录ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("deliveryId", deliveryId);
            result.put("orderId", "PO-20250923-001");
            result.put("supplier", "供应商A");
            result.put("deliveryDate", "2025-09-28T10:00:00Z");
            com.alibaba.fastjson.JSONArray items = new com.alibaba.fastjson.JSONArray();
            items.add(createDeliveryItemObject("MAT-001", "铝合金板材", "块", 100));
            items.add(createDeliveryItemObject("MAT-003", "硬质合金刀具", "把", 50));
            result.put("items", items);
            result.put("receivedBy", "仓库管理员");
            result.put("status", "已接收");
            result.put("createdAt", "2025-09-28T10:30:00Z");
            logger.info("Success to get delivery record...");
        }
        return result.toString();
    }

    // 122. 查询所有交付记录
    @GetMapping("/deliveries")
    public String getAllDeliveryRecords() throws InterruptedException {
        logger.info("Execute get all delivery records...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        com.alibaba.fastjson.JSONArray deliveries = new com.alibaba.fastjson.JSONArray();
        deliveries.add(createDeliveryObject(1L, "PO-20250923-001", "供应商A", "2025-09-28", "已接收"));
        deliveries.add(createDeliveryObject(2L, "PO-20250922-001", "供应商B", "2025-09-27", "已接收"));
        deliveries.add(createDeliveryObject(3L, "PO-20250921-001", "供应商C", "2025-09-26", "部分接收"));
        result.put("deliveries", deliveries);
        logger.info("Success to get all delivery records...");
        return result.toString();
    }

    // 123. 上传退货记录
    @PostMapping("/returns")
    public String uploadReturnRecord(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload return record...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("returnId", System.currentTimeMillis());
        result.put("orderId", requestBody.getString("orderId"));
        result.put("supplier", requestBody.getString("supplier"));
        result.put("returnDate", requestBody.getString("returnDate"));
        result.put("items", requestBody.getJSONArray("items"));
        result.put("reason", requestBody.getString("reason"));
        result.put("status", "待处理");
        result.put("createdBy", requestBody.getString("createdBy"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload return record...");
        return result.toString();
    }

    // 124. 查询退货记录
    @GetMapping("/returns/{returnId}")
    public String getReturnRecord(@PathVariable Long returnId) throws InterruptedException {
        logger.info("Execute get return record...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (returnId == null || returnId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的退货记录ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("returnId", returnId);
            result.put("orderId", "PO-20250922-001");
            result.put("supplier", "供应商B");
            result.put("returnDate", "2025-09-25T10:00:00Z");
            com.alibaba.fastjson.JSONArray items = new com.alibaba.fastjson.JSONArray();
            items.add(createReturnItemObject("MAT-002", "不锈钢棒材", "根", 5, "规格不符"));
            result.put("items", items);
            result.put("reason", "规格不符");
            result.put("status", "已处理");
            result.put("createdBy", "质检员");
            result.put("createdAt", "2025-09-25T10:30:00Z");
            logger.info("Success to get return record...");
        }
        return result.toString();
    }

    // 125. 查询所有退货记录
    @GetMapping("/returns")
    public String getAllReturnRecords() throws InterruptedException {
        logger.info("Execute get all return records...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        com.alibaba.fastjson.JSONArray returns = new com.alibaba.fastjson.JSONArray();
        returns.add(createReturnObject(1L, "PO-20250922-001", "供应商B", "2025-09-25", "已处理"));
        returns.add(createReturnObject(2L, "PO-20250920-001", "供应商C", "2025-09-23", "处理中"));
        result.put("returns", returns);
        logger.info("Success to get all return records...");
        return result.toString();
    }

    // 126. 上传库存盘点记录
    @PostMapping("/inventory/audit")
    public String uploadInventoryAuditRecord(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload inventory audit record...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("auditId", System.currentTimeMillis());
        result.put("auditDate", requestBody.getString("auditDate"));
        result.put("auditor", requestBody.getString("auditor"));
        result.put("items", requestBody.getJSONArray("items"));
        result.put("summary", requestBody.getJSONObject("summary"));
        result.put("status", "已完成");
        result.put("createdAt", Instant.now());
        logger.info("Success to upload inventory audit record...");
        return result.toString();
    }

    // 127. 查询库存盘点记录
    @GetMapping("/inventory/audit/{auditId}")
    public String getInventoryAuditRecord(@PathVariable Long auditId) throws InterruptedException {
        logger.info("Execute get inventory audit record...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (auditId == null || auditId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的盘点记录ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("auditId", auditId);
            result.put("auditDate", "2025-09-23T10:00:00Z");
            result.put("auditor", "仓库管理员");
            com.alibaba.fastjson.JSONArray items = new com.alibaba.fastjson.JSONArray();
            items.add(createAuditItemObject("MAT-001", "铝合金板材", "块", 120, 120, 0));
            items.add(createAuditItemObject("MAT-002", "不锈钢棒材", "根", 85, 80, 5));
            result.put("items", items);
            com.alibaba.fastjson.JSONObject summary = new com.alibaba.fastjson.JSONObject();
            summary.put("totalItems", 2);
            summary.put("matchedItems", 1);
            summary.put("mismatchedItems", 1);
            summary.put("totalDiscrepancy", 5);
            result.put("summary", summary);
            result.put("status", "已完成");
            result.put("createdAt", "2025-09-23T12:00:00Z");
            logger.info("Success to get inventory audit record...");
        }
        return result.toString();
    }

    // 128. 查询所有盘点记录
    @GetMapping("/inventory/audit")
    public String getAllInventoryAuditRecords() throws InterruptedException {
        logger.info("Execute get all inventory audit records...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        com.alibaba.fastjson.JSONArray audits = new com.alibaba.fastjson.JSONArray();
        audits.add(createAuditObject(1L, "2025-09-23", "仓库管理员", "已完成"));
        audits.add(createAuditObject(2L, "2025-08-23", "仓库管理员", "已完成"));
        audits.add(createAuditObject(3L, "2025-07-23", "仓库管理员", "已完成"));
        result.put("audits", audits);
        logger.info("Success to get all inventory audit records...");
        return result.toString();
    }

    private com.alibaba.fastjson.JSONObject createMaterialObject(Long id, String code, String name, String category, String spec, String unit, Double price, Integer minStock) {
        com.alibaba.fastjson.JSONObject material = new com.alibaba.fastjson.JSONObject();
        material.put("materialId", id);
        material.put("materialCode", code);
        material.put("materialName", name);
        material.put("category", category);
        material.put("specification", spec);
        material.put("unit", unit);
        material.put("unitPrice", price);
        material.put("minimumStock", minStock);
        return material;
    }

    private com.alibaba.fastjson.JSONObject createOrderItemObject(String materialCode, String materialName, String unit, Integer quantity, Double unitPrice) {
        com.alibaba.fastjson.JSONObject item = new com.alibaba.fastjson.JSONObject();
        item.put("materialCode", materialCode);
        item.put("materialName", materialName);
        item.put("unit", unit);
        item.put("quantity", quantity);
        item.put("unitPrice", unitPrice);
        item.put("totalPrice", quantity * unitPrice);
        return item;
    }

    private com.alibaba.fastjson.JSONObject createOrderObject(Long id, String number, String supplier, String date, String status) {
        com.alibaba.fastjson.JSONObject order = new com.alibaba.fastjson.JSONObject();
        order.put("orderId", id);
        order.put("orderNumber", number);
        order.put("supplier", supplier);
        order.put("orderDate", date);
        order.put("status", status);
        return order;
    }

    private com.alibaba.fastjson.JSONObject createSupplierObject(Long id, String code, String name, String contact, Double rating, String status) {
        com.alibaba.fastjson.JSONObject supplier = new com.alibaba.fastjson.JSONObject();
        supplier.put("supplierId", id);
        supplier.put("supplierCode", code);
        supplier.put("supplierName", name);
        supplier.put("contactPerson", contact);
        supplier.put("rating", rating);
        supplier.put("status", status);
        return supplier;
    }

    private com.alibaba.fastjson.JSONObject createDeliveryItemObject(String materialCode, String materialName, String unit, Integer quantity) {
        com.alibaba.fastjson.JSONObject item = new com.alibaba.fastjson.JSONObject();
        item.put("materialCode", materialCode);
        item.put("materialName", materialName);
        item.put("unit", unit);
        item.put("quantity", quantity);
        return item;
    }

    private com.alibaba.fastjson.JSONObject createDeliveryObject(Long id, String orderId, String supplier, String date, String status) {
        com.alibaba.fastjson.JSONObject delivery = new com.alibaba.fastjson.JSONObject();
        delivery.put("deliveryId", id);
        delivery.put("orderId", orderId);
        delivery.put("supplier", supplier);
        delivery.put("deliveryDate", date);
        delivery.put("status", status);
        return delivery;
    }

    private com.alibaba.fastjson.JSONObject createReturnItemObject(String materialCode, String materialName, String unit, Integer quantity, String reason) {
        com.alibaba.fastjson.JSONObject item = new com.alibaba.fastjson.JSONObject();
        item.put("materialCode", materialCode);
        item.put("materialName", materialName);
        item.put("unit", unit);
        item.put("quantity", quantity);
        item.put("reason", reason);
        return item;
    }

    private com.alibaba.fastjson.JSONObject createReturnObject(Long id, String orderId, String supplier, String date, String status) {
        com.alibaba.fastjson.JSONObject returnRecord = new com.alibaba.fastjson.JSONObject();
        returnRecord.put("returnId", id);
        returnRecord.put("orderId", orderId);
        returnRecord.put("supplier", supplier);
        returnRecord.put("returnDate", date);
        returnRecord.put("status", status);
        return returnRecord;
    }

    private com.alibaba.fastjson.JSONObject createAuditItemObject(String materialCode, String materialName, String unit, Integer systemQuantity, Integer actualQuantity, Integer discrepancy) {
        com.alibaba.fastjson.JSONObject item = new com.alibaba.fastjson.JSONObject();
        item.put("materialCode", materialCode);
        item.put("materialName", materialName);
        item.put("unit", unit);
        item.put("systemQuantity", systemQuantity);
        item.put("actualQuantity", actualQuantity);
        item.put("discrepancy", discrepancy);
        return item;
    }

    private com.alibaba.fastjson.JSONObject createAuditObject(Long id, String date, String auditor, String status) {
        com.alibaba.fastjson.JSONObject audit = new com.alibaba.fastjson.JSONObject();
        audit.put("auditId", id);
        audit.put("auditDate", date);
        audit.put("auditor", auditor);
        audit.put("status", status);
        return audit;
    }
}
