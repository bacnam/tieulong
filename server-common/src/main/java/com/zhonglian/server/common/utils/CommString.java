package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommString {
    public static <T> String join(String sep, List<T> iters) {
        StringBuilder ret = new StringBuilder();
        if (iters.size() != 0) {
            boolean hasFirst = false;

            for (int index = 0; index < iters.size(); index++) {
                String toadd = iters.get(index).toString();
                if (!toadd.equals("")) {
                    if (hasFirst) {
                        ret.append(sep + toadd);
                    } else {
                        hasFirst = true;
                        ret.append(toadd);
                    }
                }
            }
        }

        return ret.toString();
    }

    public static List<Integer> getIntegerList(String src, String sep) {
        String[] srcEx = src.split(sep);
        List<Integer> ret = new ArrayList<>();

        for (int index = 0; index < srcEx.length; index++) {
            if (!srcEx[index].equals("")) {
                try {
                    ret.add(Integer.valueOf(srcEx[index]));
                } catch (Exception var6) {
                    CommLog.error("getIntegerList src:" + src);
                }
            }
        }

        return ret;
    }

    public static List<Double> getDoubleList(String src, String sep) {
        String[] srcEx = src.split(sep);
        List<Double> ret = new ArrayList<>();

        for (int index = 0; index < srcEx.length; index++) {
            if (!srcEx[index].equals("")) {
                try {
                    ret.add(Double.valueOf(srcEx[index]));
                } catch (Exception var6) {
                    CommLog.error("getIntegerList src:" + src);
                }
            }
        }

        return ret;
    }

    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[一-龥]";

        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength++;
            }
        }

        return valueLength;
    }

    public static String output_classField(Object t) {
        return output_class(t, false, true);
    }

    public static String output_class_line(Object t) {
        return output_class(t, true, false);
    }

    public static String output_class(Object t) {
        return output_class(t, false, false);
    }

    public static String output_class_deep(Object t, int deep) {
        return objectTosString(t, deep);
    }

    private static String output_class(Object t, boolean isInline, boolean isField) {
        StringBuilder sb = new StringBuilder();
        if (!isInline && !isField) {
            sb.append(String.format("===[%s] start...\n", t.getClass().getSimpleName()));
        }

        Field[] fds = t.getClass().getDeclaredFields();

        for (Field fd : fds) {
            Class typeClass = fd.getType();
            Object fieldValue = null;
            List.class.isAssignableFrom(typeClass);
            if (!Map.class.isAssignableFrom(typeClass) && !typeClass.isArray()) {
                try {
                    fd.setAccessible(true);
                    fieldValue = fd.get(t);
                } catch (IllegalAccessException var12) {
                    CommLog.error(CommString.class.getName(), (Throwable)var12);
                }
            }

            if (isField) {
                sb.append(String.format("%s\t", fd.getName()));
            } else if (fieldValue != null) {
                if (isInline) {
                    sb.append(String.format("%s\t", fieldValue.toString()));
                } else {
                    sb.append(String.format("[%s]%s = %s", fd.getType().getSimpleName(), fd.getName(), fieldValue.toString()));
                    sb.append(System.lineSeparator());
                }
            } else {
                sb.append(isInline ? "null" : "null\n");
            }
        }

        if (!isInline && !isField) {
            sb.append(String.format("===[%s] end!!!\n\n", t.getClass().getSimpleName()));
        }

        return sb.toString();
    }

    private static String objectTosString(Object o, int deep) {
        ArrayList<Object> already = new ArrayList<>();
        StringBuffer s = new StringBuffer();
        s.append("{\n");
        printObject(o, s, "    ", deep, already);
        s.append("}\n");
        return s.toString();
    }

    private static void printObject(Object o, StringBuffer s, String blank, int deep, ArrayList<Object> already) {
        if (o == null) {
            s.append(blank).append("null\n");
        } else {
            deep--;
            Class clazz = o.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);

                try {
                    String value = field.get(o).toString();
                    if (value.contains("@")) {
                        String typeName = "[" + field.getType().getSimpleName() + "]";
                        if (already.contains(field.get(o))) {
                            s.append(blank).append(field.getName()).append("{\n");
                            s.append(blank).append("↑↑↑ ").append(field.getType().getSimpleName()).append("\n");
                            s.append(blank).append("}\n");
                        } else {
                            already.add(field.get(o));
                            if (value.startsWith("[L")) {
                                s.append(blank).append(typeName).append(field.getName()).append("(size:").append(Array.getLength(field.get(o))).append(") [\n");

                                for (int j = 0; j < Array.getLength(field.get(o)); j++) {
                                    s.append(blank).append("    ").append(field.getName()).append("[").append(j).append("]{\n");
                                    if (deep > 0) {
                                        printObject(Array.get(field.get(o), j), s, blank + "        ", deep - 1, already);
                                    } else {
                                        s.append(blank).append("    ......\n");
                                    }

                                    s.append(blank).append("    }\n");
                                }

                                s.append(blank).append("]\n");
                            } else if (List.class.isAssignableFrom(field.getType())) {
                                List<Object> temp = (List<Object>)field.get(o);
                                s.append(blank).append(typeName).append(field.getName()).append("(size:").append(temp.size()).append(") [\n");
                                int index = 0;

                                for (Object t : temp) {
                                    s.append(blank).append("    ").append(field.getName()).append("[").append(index).append("]{\n");
                                    printObject(t, s, blank + "        ", deep - 1, already);
                                    s.append(blank).append("    }\n");
                                    index++;
                                }

                                s.append(blank).append("]\n");
                            } else if (!Map.class.isAssignableFrom(field.getType())) {
                                s.append(blank).append(typeName).append(field.getName()).append("{\n");
                                if (deep > 0) {
                                    printObject(field.get(o), s, blank + "    ", deep - 1, already);
                                } else {
                                    s.append(blank).append("......\n");
                                }

                                s.append(blank).append("}\n");
                            } else {
                                Map<Object, Object> temp = (Map<Object, Object>)field.get(o);
                                s.append(blank).append(typeName).append(field.getName()).append("(size:").append(temp.size()).append(") {\n");

                                for (Entry<Object, Object> entry : temp.entrySet()) {
                                    s.append(blank).append("    key=").append(entry.getKey().toString()).append(", value={\n");
                                    if (deep > 0) {
                                        printObject(entry.getValue(), s, blank + "        ", deep - 1, already);
                                    } else {
                                        s.append(blank).append("    ......\n");
                                    }

                                    s.append(blank).append("    }\n");
                                }

                                s.append(blank).append("}\n");
                            }

                            already.remove(field.get(o));
                        }
                    } else {
                        s.append(blank).append(field.getName()).append(":");
                        s.append(value).append("\n");
                    }
                } catch (IllegalAccessException | ArrayIndexOutOfBoundsException | IllegalArgumentException var18) {
                    if (!field.getName().startsWith("this")) {
                        s.append(blank).append(field.getName()).append(":");
                        s.append("null\n");
                    }
                }

                field.setAccessible(isAccessible);
            }
        }
    }

    public static String format(String format, Object... arguments) {
        if (format == null) {
            return format;
        } else {
            for (int i = 0; i < arguments.length; i++) {
                String info = arguments[i] == null ? "[null]" : arguments[i].toString();
                format = format.replaceAll("\\{" + i + "\\}", info);
            }

            return format;
        }
    }
}
