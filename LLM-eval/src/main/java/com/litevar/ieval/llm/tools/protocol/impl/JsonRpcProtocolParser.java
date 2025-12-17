package com.litevar.ieval.llm.tools.protocol.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.tools.protocol.AbstractProtocolParser;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author  action
 * @Date  2025/10/15 11:31
 * @company litevar
 **/
public class JsonRpcProtocolParser extends AbstractProtocolParser {
    private String apiKey;
    public JsonRpcProtocolParser(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public JSONObject parse(String raw, String protocol) {
        JSONObject result = new JSONObject();
        result.put("code", 10500);
        if(StringUtils.startsWith(raw, "{")&&StringUtils.endsWith(raw, "}")&&StringUtils.contains(raw, "openrpc")) {
            Map<String, String> headers = null;
            if(StringUtils.isNoneBlank(apiKey)) {
                logger.info("apiKey: {}", apiKey);
                headers = new HashMap<>();
                headers.put("Authorization", apiKey);
            }
            try {
                JSONObject source = JSONObject.parseObject(raw);
                if(!source.containsKey("servers")) {
                    result.put("info", "Missing field 'servers'");
                }
                else if(!source.containsKey("methods")){
                    result.put("info", "Missing field 'methods'");
                }
                else {
                    JSONArray serverArray = source.getJSONArray("servers");
                    String server = serverArray.getJSONObject(0).getString("url");
                    JSONArray methods = source.getJSONArray("methods");
                    JSONArray funcDetailArray = new JSONArray();
                    JSONArray funcInfoArray = new JSONArray();
                    JSONObject funcDetailObj = null;
                    JSONObject funcInfoObj = null;
                    int cnt = methods.size();
                    logger.info("method count: {}", cnt);
                    for(int i=0; i<cnt; i++) {
                        JSONObject methodObj = methods.getJSONObject(i);
                        funcInfoObj = new JSONObject();
                        funcInfoObj.put("kind", protocol);
                        funcInfoObj.put("server", server);
                        funcInfoObj.put("name", methodObj.getString("name"));
                        if(headers!=null) {
                            funcInfoObj.put("headers", headers);
                        }
                        String funcDescription = methodObj.getString("description");
                        if(StringUtils.isBlank(funcDescription)) {
                            funcDescription = methodObj.getString("summary");
                        }
                        funcInfoObj.put("description", funcDescription!=null?funcDescription:"");
                        funcInfoArray.add(funcInfoObj);
                        funcDetailObj = new JSONObject();
                        funcDetailObj.put("type", "function");
                        JSONObject function = new JSONObject();
                        function.put("name", methodObj.getString("name"));
                        function.put("description", funcDescription!=null?funcDescription:"");
                        funcDetailObj.put("function", function);
                        JSONObject params = new JSONObject();
                        params.put("type", "object");
                        JSONObject properties = new JSONObject();
                        params.put("properties", properties);
                        function.put("parameters", params);
                        JSONArray rawParams = methodObj.getJSONArray("params");
                        JSONArray requireArray = new JSONArray();
                        for(int j=0; j< rawParams.size(); j++) {
                            JSONObject rawParamObj = rawParams.getJSONObject(j);
                            JSONObject column = new JSONObject();
                            if(rawParamObj.containsKey("schema")) {
                                column.put("type", rawParamObj.getJSONObject("schema").getString("type"));
                                if(rawParamObj.getJSONObject("schema").containsKey("default")) {
                                    column.put("default", rawParamObj.getJSONObject("schema").getString("default"));
                                }
                                if(rawParamObj.getJSONObject("schema").containsKey("enum")) {
                                    column.put("enum", rawParamObj.getJSONObject("schema").getJSONArray("enum"));
                                }
                            }
                            else if(rawParamObj.containsKey("type")) {
                                column.put("type", rawParamObj.getString("type"));
                            }
                            String paramDescription = rawParamObj.getString("description");
                            if(StringUtils.isBlank(paramDescription)) {
                                paramDescription = rawParamObj.getString("summary");
                            }
                            column.put("description", paramDescription);
                            if(rawParamObj.containsKey("enum")) {
                                column.put("enum", rawParamObj.getString("enum"));
                            }
                            properties.put(rawParamObj.getString("name"), column);
                            if("object".equals(column.getString("type")) && rawParamObj.getJSONObject("schema").containsKey("properties")) {
                                JSONObject l2 = new JSONObject();
                                column.put("properties", l2);
                                travelProperties(l2, rawParamObj.getJSONObject("schema").getJSONObject("properties"));
                            }
                            else if("array".equals(column.getString("type")) && rawParamObj.getJSONObject("schema").containsKey("items")) {
                                JSONObject itemsObject = new JSONObject();
                                JSONObject properObjL2 = new JSONObject();
                                itemsObject.put("properties", properObjL2);
                                column.put("items", itemsObject);
                                travelProperties(properObjL2, rawParamObj.getJSONObject("schema").getJSONObject("items"));
                            }
                            if(rawParamObj.containsKey("required") && rawParamObj.getBooleanValue("required")) {
                                requireArray.add(rawParamObj.getString("name"));
                            }
                        }
                        if(!requireArray.isEmpty()) {params.put("required", requireArray);}
                        funcDetailArray.add(funcDetailObj);
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
                logger.error("wrong json string: ", e);
                result.put("info", "Wrong json string: "+e.getMessage());
            }
        }
        else {
            result.put("info", "json string illegal: must startWith { and endWith }");
        }
        return result;
    }

    public void travelProperties(JSONObject parent, JSONObject properObject) {
        if(properObject!=null && !properObject.isEmpty()) {
            JSONArray requireArray = new JSONArray();
            properObject.forEach((key, value) -> {
                logger.info("column: {}", key);
                //
                if(value instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) value;
                    JSONObject lx = new JSONObject();
                    if(jsonObj.containsKey("schema")) {
                        lx.put("type", jsonObj.getJSONObject("schema").getString("type"));
                        if(jsonObj.getJSONObject("schema").containsKey("default")) {
                            lx.put("default", jsonObj.getJSONObject("schema").getString("default"));
                        }
                        if(jsonObj.getJSONObject("schema").containsKey("enum")) {
                            lx.put("enum", jsonObj.getJSONObject("schema").getJSONArray("enum"));
                        }
                    }
                    else if(jsonObj.containsKey("type")) {
                        lx.put("type", jsonObj.getString("type"));
                    }
                    String paramDescription = jsonObj.getString("description");
                    if(StringUtils.isBlank(paramDescription)) {
                        paramDescription = jsonObj.getString("summary");
                    }
                    lx.put("description", paramDescription);
                    if(jsonObj.containsKey("enum")) {
                        lx.put("enum", jsonObj.getString("enum"));
                    }
                    if("object".equals(lx.getString("type")) && jsonObj.getJSONObject("schema").containsKey("properties")) {

                        travelProperties(lx, jsonObj.getJSONObject("schema").getJSONObject("properties"));
                    }
                    else if("array".equals(lx.getString("type")) && jsonObj.getJSONObject("schema").containsKey("items")) {
                        logger.info("Lx deal array items");
                        JSONObject items = jsonObj.getJSONObject("schema").getJSONObject("items");
                        JSONArray itemArray = new JSONArray();
                        lx.put("items", itemArray);
                        if("object".equals(items.getString("type"))) {
                            JSONObject properObjLx = new JSONObject();
                            travelProperties(properObjLx, items.getJSONObject("properties"));
                            itemArray.add(properObjLx);
                        }
                    }
                    if(jsonObj.containsKey("required") && jsonObj.getBooleanValue("required")) {
                        requireArray.add(jsonObj.getString("name"));
                    }
                    //添加属性
                    parent.put(key, lx);
                }
            });
            if(!requireArray.isEmpty()) {properObject.put("required", requireArray);}
        }
    }
}
