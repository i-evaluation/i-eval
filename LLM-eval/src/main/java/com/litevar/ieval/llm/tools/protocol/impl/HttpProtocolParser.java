package com.litevar.ieval.llm.tools.protocol.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.tools.protocol.AbstractProtocolParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author  action
 * @Date  2025/10/15 11:29
 * @company litevar
 **/
public class HttpProtocolParser extends AbstractProtocolParser {
    private String apiKey;
    public HttpProtocolParser(String apiKey) {
        this.apiKey = apiKey;
    }
    @Override
    public JSONObject parse(String raw, String protocol) {
        JSONObject result = new JSONObject();
        result.put("code", 10500);
        if(StringUtils.contains(raw, "openapi")) {
            Map<String, String> headers = null;
            if(StringUtils.isNoneBlank(apiKey)) {
                logger.info("apiKey: {}", apiKey);
                headers = new HashMap<>();
                headers.put("Authorization", apiKey);
            }
            try {
                ParseOptions parseOptions = new ParseOptions();
                parseOptions.setResolve(true); // 默认启用
                parseOptions.setResolveFully(true);
                parseOptions.setResolveCombinators(true);
                SwaggerParseResult spr  = new OpenAPIV3Parser().readContents(raw, null, parseOptions);
                //SwaggerParseResult spr  = new OpenAPIV3Parser().readContents(raw);
                OpenAPI openAPI = spr.getOpenAPI();
                StringBuffer stringBuffer = new StringBuffer();
                if (spr.getMessages()!=null && !spr.getMessages().isEmpty()) {
                    List<String> msgList = spr.getMessages();
                    for(String str: msgList) {
                        stringBuffer.append(str+",");
                    }
                    logger.error("error when parsing: {}", stringBuffer.toString());
                    result.put("info", stringBuffer.toString());
                }
                else if(openAPI==null) {
                    logger.error("openAPI object is null");
                    stringBuffer.append("openAPI object is null");
                    result.put("info", stringBuffer.toString());
                }
                else {
                    String apiServer = "http://127.0.0.1";
                    List<Server> servers = openAPI.getServers();
                    if(servers!=null && !servers.isEmpty()) {
                        apiServer = servers.getFirst().getUrl();
                        logger.info("server-url: {}", apiServer);
                    }
                    JSONArray funcDetailArray = new JSONArray();
                    JSONArray funcInfoArray = new JSONArray();
                    Paths paths = openAPI.getPaths();
                    for(Map.Entry<String, PathItem> entry : paths.entrySet()) {
                        String path = entry.getKey();
                        PathItem pathItem = entry.getValue();
                        Map<String, Operation> object = getApiOperation(pathItem);
                        if(object!=null && !object.isEmpty()) {
                            for(Map.Entry<String, Operation> ety : object.entrySet()) {
                                JSONObject funcToolObj = analysePath(path, ety.getKey(), ety.getValue(), apiServer, headers);
                                if(funcToolObj.containsKey("funcInfoObj")) {
                                    funcInfoArray.add(funcToolObj.getJSONObject("funcInfoObj"));
                                }
                                if(funcToolObj.containsKey("funcDetailObj")) {
                                    funcDetailArray.add(funcToolObj.getJSONObject("funcDetailObj"));
                                }
                            }
                        }
                    }
                    if(!funcInfoArray.isEmpty() && !funcDetailArray.isEmpty()) {
                        result.put("code", 10200);
                        result.put("functionInfo", funcInfoArray);
                        result.put("functionDetails", funcDetailArray);
                        result.put("info", "ok");
                    }
                    else {
                        result.put("info", "function not found");
                    }
                }
            }
            catch (Exception e) {
                logger.error("wrong yaml string: ", e);
                result.put("info", "Wrong yaml string: "+e.getMessage());
            }
        }
        else {
            result.put("info", "only support openapi");
        }
        return result;
    }

    private Map<String, Operation> getApiOperation(PathItem pathItem) {
        Map<String, Operation> operationMap = new HashMap<>();
        if(pathItem.getGet()!=null) {
            operationMap.put("GET", pathItem.getGet());
        }
        if(pathItem.getPost()!=null) {
            operationMap.put("POST", pathItem.getPost());
        }
        if(pathItem.getPut()!=null) {
            operationMap.put("PUT", pathItem.getPut());
        }
        if(pathItem.getDelete()!=null) {
            operationMap.put("DELETE", pathItem.getDelete());
        }
        return operationMap;
    }

    private JSONObject analysePath(String path, String method, Operation operation, String apiServer, Map<String, String> headers) {
        JSONObject funcToolObj = new JSONObject();
        logger.info("path: {}, method: {}", path, method);
        logger.info("summary: {}, description: {}", operation.getSummary(), operation.getDescription());
        List<Server> serverList = operation.getServers();
        if(serverList!=null && !serverList.isEmpty()) {
            apiServer = serverList.getFirst().getUrl();
            logger.info("method server-url: {}", apiServer);
        }
        String methodName = path.contains("/")?path.replaceAll("/", ""):path;
        JSONObject funcInfoObj = new JSONObject();
        funcInfoObj.put("kind", "http");
        funcInfoObj.put("server", apiServer+path);
        funcInfoObj.put("name", methodName+"_"+method);
        funcInfoObj.put("method", method);
        if(headers!=null) {
            funcInfoObj.put("headers", headers);
        }
        funcInfoObj.put("description", operation.getDescription());
        funcToolObj.put("funcInfoObj", funcInfoObj);
        JSONObject funcDetailObj = new JSONObject();
        funcDetailObj.put("type", "function");
        JSONObject functionObj = new JSONObject();
        functionObj.put("name", methodName+"_"+method);
        functionObj.put("description", operation.getDescription());
        funcDetailObj.put("function", functionObj);
        JSONObject paramsObj = new JSONObject();
        paramsObj.put("type", "object");
        functionObj.put("parameters", paramsObj);

        if("GET".equals(method) || "DELETE".equals(method)) {
            JSONArray requireArray = new JSONArray();
            JSONObject propertiesObj = new JSONObject();
            paramsObj.put("properties", propertiesObj);
            List<Parameter> paramList = operation.getParameters();
            if(paramList!=null && !paramList.isEmpty()) {
                for(Parameter p : paramList) {
                    logger.info("param: {}, in {} ,description: {}", p.getName(), p.getIn(), p.getDescription());
                    if("path".equals(p.getIn())) {
                        if(!funcInfoObj.containsKey("paths")) {
                            funcInfoObj.put("paths", p.getName()+",");
                        }
                        else {
                            funcInfoObj.put("paths", funcInfoObj.getString("paths")+p.getName()+",");
                        }
                    }
                    else {
                        if(!funcInfoObj.containsKey("queries")) {
                            funcInfoObj.put("queries", p.getName()+",");
                        }
                        else {
                            funcInfoObj.put("queries", funcInfoObj.getString("queries")+p.getName()+",");
                        }
                    }
                    Schema schema = p.getSchema();
                    logger.info("type: {}", schema.getType());
                    JSONObject columnObj = new JSONObject();
                    columnObj.put("type", schema.getType());
                    columnObj.put("description", p.getDescription());
                    if(schema.getEnum()!=null&& !schema.getEnum().isEmpty()) {
                        columnObj.put("enum", schema.getEnum());
                    }
                    propertiesObj.put(p.getName(), columnObj);
                    if(p.getRequired()) {
                        requireArray.add(p.getName());
                    }
                }
            }
            if(!requireArray.isEmpty()) {
                paramsObj.put("required", requireArray);
            }
        }
        else if("POST".equals(method) || "PUT".equals(method)) {
            if(operation.getRequestBody()!=null) {
                JSONArray requireArray = new JSONArray();
                ObjectSchema schema = (ObjectSchema)operation.getRequestBody().getContent().get("application/json").getSchema();
                //paramsObj.put("properties", schema.getProperties());
                JSONObject propertiesObj = new JSONObject();
                paramsObj.put("properties", propertiesObj);
                //deal queries or paths
                List<Parameter> paramList = operation.getParameters();
                if(paramList!=null && !paramList.isEmpty()) {
                    for(Parameter p : paramList) {
                        logger.info("param: {}, in {} ,description: {}", p.getName(), p.getIn(), p.getDescription());
                        if("path".equals(p.getIn())) {
                            if(!funcInfoObj.containsKey("paths")) {
                                funcInfoObj.put("paths", p.getName()+",");
                            }
                            else {
                                funcInfoObj.put("paths", funcInfoObj.getString("paths")+p.getName()+",");
                            }
                        }
                        else {
                            if(!funcInfoObj.containsKey("queries")) {
                                funcInfoObj.put("queries", p.getName()+",");
                            }
                            else {
                                funcInfoObj.put("queries", funcInfoObj.getString("queries")+p.getName()+",");
                            }
                        }
                        Schema pSchema = p.getSchema();
                        logger.info("type: {}", pSchema.getType());
                        JSONObject columnObj = new JSONObject();
                        columnObj.put("type", pSchema.getType());
                        columnObj.put("description", p.getDescription());
                        if(pSchema.getEnum()!=null&& !pSchema.getEnum().isEmpty()) {
                            columnObj.put("enum", pSchema.getEnum());
                        }
                        propertiesObj.put(p.getName(), columnObj);
                        if(p.getRequired()) {
                            requireArray.add(p.getName());
                        }
                    }
                }
                logger.info("rootSchema type: {}", schema.getType());
                Map<String, Schema> propertyMap = schema.getProperties();
                //schema.getOneOf();
                propertyMap.forEach((key, value) -> {
                    logger.info("key: {}, type: {}", key, value.getType());
                    JSONObject l1 = new JSONObject();
                    l1.put("type", value.getType());
                    l1.put("description", value.getDescription());
                    if(value.getEnum()!=null) {
                        l1.put("enum", value.getEnum());
                    }
                    if(value.getDefault()!=null) {
                        l1.put("default", value.getDefault());
                    }
                    if(value.getFormat()!=null) {
                        l1.put("format", value.getFormat());
                    }
                    if("object".equals(value.getType())) {
                        JSONObject l2 = new JSONObject();
                        l1.put("properties", l2);
                        if(value.getRequired()!=null && !value.getRequired().isEmpty()) {
                            l1.put("required", value.getRequired());
                        }
                        travelSchema(l2, value.getProperties(), 1);
                    }
                    else if("array".equals(value.getType())) {
                        logger.info("L1 deal array items");
                        Schema arraySchema = value.getItems();
                        JSONObject itemsObject = new JSONObject();
                        l1.put("items", itemsObject);
                        if("object".equals(arraySchema.getType())) {
                            itemsObject.put("type", "object");
                            JSONObject properObjL2 = new JSONObject();
                            itemsObject.put("properties", properObjL2);
                            if(arraySchema.getRequired()!=null && !arraySchema.getRequired().isEmpty()) {
                                itemsObject.put("required", arraySchema.getRequired());
                            }
                            travelSchema(properObjL2, arraySchema.getProperties(), 10);
                        }
                    }
                    //加入属性
                    propertiesObj.put(key, l1);
                });
                if(schema.getRequired()!=null&& !schema.getRequired().isEmpty()) {
                    requireArray.addAll(schema.getRequired());
                    paramsObj.put("required", requireArray);
                    //paramsObj.put("required", schema.getRequired());
                }
                else if(!requireArray.isEmpty()) {
                    paramsObj.put("required", requireArray);
                }
            }
            else {
                logger.warn("{} method without body", method);
            }
        }
        funcToolObj.put("funcDetailObj", funcDetailObj);
        return funcToolObj;
    }

    private void travelSchema(JSONObject parent, Map<String, Schema> subSchemaMap, int deepth) {
        if(subSchemaMap!=null && !subSchemaMap.isEmpty()) {
            deepth++;
            logger.info("parsing deepth: {}", deepth);
            subSchemaMap.forEach((key, value) -> {
                logger.info("key: {}, type: {}", key, value.getType());
                JSONObject lx = new JSONObject();
                lx.put("type", value.getType());
                if(!"object".equals(value.getType()) && !"array".equals(value.getType())) {
                    lx.put("description", value.getDescription());
                }
                if(value.getEnum()!=null) {
                    lx.put("enum", value.getEnum());
                }
                if(value.getDefault()!=null) {
                    lx.put("default", value.getDefault());
                }
                if(value.getFormat()!=null) {
                    lx.put("format", value.getFormat());
                }
                if("object".equals(value.getType())) {
                    JSONObject childProperties = new JSONObject();
                    lx.put("properties", childProperties);
                    if(value.getRequired()!=null && !value.getRequired().isEmpty()) {
                        lx.put("required", value.getRequired());
                    }
                    travelSchema(childProperties, value.getProperties(), 30);
                }
                else if("array".equals(value.getType())) {
                    logger.info("Lx deal array items");
                    Schema arraySchema = value.getItems();
                    JSONObject itemsObject = new JSONObject();
                    lx.put("items", itemsObject);
                    if("object".equals(arraySchema.getType())) {
                        itemsObject.put("type", "object");
                        JSONObject properObjLx = new JSONObject();
                        itemsObject.put("properties", properObjLx);
                        if(arraySchema.getRequired()!=null && !arraySchema.getRequired().isEmpty()) {
                            itemsObject.put("required", arraySchema.getRequired());
                        }
                        travelSchema(properObjLx, arraySchema.getProperties(), 60);
                    }
                }
                parent.put(key, lx);
            });
        }
    }
}
