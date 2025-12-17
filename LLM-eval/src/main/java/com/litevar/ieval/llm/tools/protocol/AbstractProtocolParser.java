package com.litevar.ieval.llm.tools.protocol;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author  action
 * @Date  2025/10/15 11:26 
 * @company litevar
 **/
public abstract class AbstractProtocolParser {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract JSONObject parse(String raw, String protocol);
}
