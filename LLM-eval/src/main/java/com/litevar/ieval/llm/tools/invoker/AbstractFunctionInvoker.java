package com.litevar.ieval.llm.tools.invoker;

import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.tools.FunctionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author  action
 * @Date  2025/10/14 17:20
 * @company litevar
 **/
public abstract class AbstractFunctionInvoker {
    protected FunctionInfo info;
    protected String methodName;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbstractFunctionInvoker(FunctionInfo info, String method) {
        this.info = info;
        this.methodName = method;
    }

    public abstract Object invoke(JSONObject realArgs);
}
