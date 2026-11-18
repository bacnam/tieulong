package business.gmcmd.cmd;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Command {
    private String name;
    private String comment;
    private Method cmd;
    private Object excuter;

    public Command(business.gmcmd.annotation.Command annotation, Method m, Object excuter) {
        this.name = annotation.command().trim();
        if (this.name.isEmpty()) {
            this.name = m.getName().toLowerCase();
        }

        this.comment = annotation.comment();
        this.cmd = m;
        this.excuter = excuter;
    }

    public String run(long player, String[] args) throws Exception {
        Object[] params = this.parseParams(player, args);
        if (this.cmd.getReturnType() == null) {
            this.cmd.invoke(this.excuter, params);
            return "command[" + this.name + "] run success";
        } else {
            Object rtn = this.cmd.invoke(this.excuter, params);
            return rtn == null ? "command[" + this.name + "] run success" : rtn.toString();
        }
    }

    private Object[] parseParams(long player, Object[] args) throws Exception {
        Object[] rtnParam = new Object[this.cmd.getParameterCount()];
        Class[] type = this.cmd.getParameterTypes();
        int parameCount = this.cmd.getParameterCount();
        int shift = 0;
        if (type.length > 0 && (type[0] == Long.class || type[0] == long.class)) {
            parameCount--;
            shift = 1;
            rtnParam[0] = player;
        }

        if (args.length != parameCount) {
            throw new Exception("[" + this.name + "] needs " + type.length + " params but provide " + args.length + " params");
        } else {
            Object[] newArgs = new Object[type.length];
            if (parameCount < type.length) {
                int index = 0;

                for (int i = shift; i < type.length; i++) {
                    newArgs[i] = args[index];
                    index++;
                }
            }

            for (int i = shift; i < type.length; i++) {
                rtnParam[i] = this.parseParam(type[i], newArgs[i]);
            }

            return rtnParam;
        }
    }

    private Object parseParam(Class<?> clazz, Object object) throws Exception {
        String param = (String)object;
        if (List.class.isAssignableFrom(clazz)) {
            List<Object> list;
            if (clazz.isAssignableFrom(List.class)) {
                list = new ArrayList<>();
            } else {
                list = (List<Object>)clazz.newInstance();
            }

            Type gt = clazz.getGenericSuperclass();
            ParameterizedType pt = (ParameterizedType)gt;
            Class<?> ptClass = (Class<?>)pt.getActualTypeArguments()[0];
            String[] subparams = param.split(";");

            for (String p : subparams) {
                list.add(this.parseParam(ptClass, p));
            }

            return list;
        } else if (!clazz.isArray()) {
            if (clazz == int.class || clazz == Integer.class) {
                return Integer.parseInt(param);
            } else if (clazz == long.class || clazz == Long.class) {
                return Long.parseLong(param);
            } else if (clazz == float.class || clazz == Float.class) {
                return Float.valueOf(param);
            } else if (clazz == double.class || clazz == Double.class) {
                return Double.valueOf(param);
            } else if (clazz == boolean.class || clazz == Boolean.class) {
                return Boolean.valueOf(param);
            } else if (clazz == String.class) {
                return param;
            } else if (clazz.isEnum()) {
                return Enum.valueOf((Class) clazz, param);
            } else {
                throw new Exception("can not instance " + clazz.getSimpleName() + " define for command:" + this.name);
            }
        } else {
            String[] subparams = param.split(";");
            Object[] array = new Object[subparams.length];
            Class<Object[]> ptClass = (Class<Object[]>)clazz.getComponentType();

            for (int i = 0; i < subparams.length; i++) {
                array[i] = this.parseParam(ptClass, subparams[i]);
            }

            return array;
        }
    }

    @Override
    public String toString() {
        return this.name + " " + this.getParamString() + " " + this.comment;
    }

    private String getParamString() {
        StringBuilder stringBuilder = new StringBuilder();

        Parameter[] var5;
        for (Parameter parameter : var5 = this.cmd.getParameters()) {
            stringBuilder.append(parameter.getType().getSimpleName().toLowerCase()).append(" ");
        }

        return stringBuilder.toString();
    }

    public String getName() {
        return this.name;
    }
}
