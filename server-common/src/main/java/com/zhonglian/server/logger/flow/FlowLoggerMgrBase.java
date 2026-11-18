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

        Method[] e;
        for (Method method : e = baselogger.getMethods()) {
            if (!this.isInSuperType(method, baseMethod)) {
                if (this.methods.containsKey(method.getName())) {
                    CommLog.error("FlowLogger 重复定义了log 方法[{}]", method);
                    System.exit(-1);
                }

                this.methods.put(method.getName(), method);
            }
        }

        for (Class<?> cs : loggersClass) {
            Logger logger = null;

            try {
                logger = (Logger)CommClass.forName(cs.getName()).newInstance();
            } catch (Exception var9) {
                CommLog.error("初始化SDK日志失败，原因{}", var9.getMessage(), var9);
            }

            if (logger != null && logger.isOpen()) {
                this.loggers.add(logger);
                CommLog.info("注册SDK日志 {} ", logger.getClass().getSimpleName());
            }
        }

        return true;
    }

    private boolean isInSuperType(Method method, Method[] baseMethod) {
        for (Method m : baseMethod) {
            if (method.getName().equals(m.getName())) {
                return true;
            }
        }

        return false;
    }

    protected void log(String method, Object... params) {
        for (Logger logger : this.loggers) {
            if (logger.isOpen()) {
                try {
                    this.methods.get(method).invoke(logger, params);
                } catch (Exception var6) {
                    CommLog.error("{} 记录 {} 时发生异常,信息:{}", logger.getClass().getSimpleName(), method, var6.getMessage(), var6);
                }
            }
        }
    }
}
