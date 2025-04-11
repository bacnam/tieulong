package com.zhonglian.server.logger.flow;

import BaseCommon.CommClass;
import BaseCommon.CommLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowLoggerMgrBase<Logger extends FlowLoggerBase> {
    private List<Logger> loggers = new ArrayList<>();
    private Map<String, Method> methods = new HashMap<>();

    public boolean init(Class<Logger> baselogger, String loggerPath) {
        List<Class<?>> loggersClass = CommClass.getAllClassByInterface(baselogger, loggerPath);

        Method[] baseMethod = Object.class.getDeclaredMethods();
        byte b;
        int i;
        Method[] arrayOfMethod1;
        for (i = (arrayOfMethod1 = baselogger.getMethods()).length, b = 0; b < i; ) {
            Method method = arrayOfMethod1[b];
            if (!isInSuperType(method, baseMethod)) {

                if (this.methods.containsKey(method.getName())) {
                    CommLog.error("FlowLogger 重复定义了log 方法[{}]", method);
                    System.exit(-1);
                }
                this.methods.put(method.getName(), method);
            }
            b++;
        }

        for (Class<?> cs : loggersClass) {
            FlowLoggerBase flowLoggerBase;
            Logger logger = null;
            try {
                flowLoggerBase = CommClass.forName(cs.getName()).newInstance();
            } catch (Exception e) {
                CommLog.error("初始化SDK日志失败，原因{}", e.getMessage(), e);
            }
            if (flowLoggerBase == null || !flowLoggerBase.isOpen()) {
                continue;
            }
            this.loggers.add((Logger) flowLoggerBase);
            CommLog.info("注册SDK日志 {} ", flowLoggerBase.getClass().getSimpleName());
        }
        return true;
    }

    private boolean isInSuperType(Method method, Method[] baseMethod) {
        byte b;
        int i;
        Method[] arrayOfMethod;
        for (i = (arrayOfMethod = baseMethod).length, b = 0; b < i; ) {
            Method m = arrayOfMethod[b];
            if (method.getName().equals(m.getName()))
                return true;
            b++;
        }

        return false;
    }

    protected void log(String method, Object... params) {
        for (FlowLoggerBase flowLoggerBase : this.loggers) {
            if (!flowLoggerBase.isOpen()) {
                continue;
            }

            try {
                ((Method) this.methods.get(method)).invoke(flowLoggerBase, params);
            } catch (Exception e) {
                CommLog.error("{} 记录 {} 时发生异常,信息:{}", new Object[]{flowLoggerBase.getClass().getSimpleName(), method, e.getMessage(), e});
            }
        }
    }
}

