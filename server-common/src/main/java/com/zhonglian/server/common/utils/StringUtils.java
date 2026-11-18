package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class StringUtils {
    static final Pattern COMMA_SEP_TRIM_REGEX;
    static final Pattern COMMA_SEP_NO_TRIM_REGEX;
    public static final String[] EMPTY_STRING_ARRAY;

    static {
        try {
            COMMA_SEP_TRIM_REGEX = Pattern.compile("\\s*\\,\\s*");
            COMMA_SEP_NO_TRIM_REGEX = Pattern.compile("\\,");
        } catch (PatternSyntaxException var1) {
            CommLog.error(null, var1);
            throw new InternalError(var1.toString());
        }

        EMPTY_STRING_ARRAY = new String[0];
    }

    public static List<Integer> string2Integer(String str) {
        try {
            List<Integer> intList = new ArrayList<>();
            String[] strList = str.split(";");

            for (String item : strList) {
                intList.add(Integer.valueOf(item));
            }

            return intList;
        } catch (Exception var7) {
            return new ArrayList<>();
        }
    }

    public static String replaceArgs(String src, String... args) {
        if (args != null && src != null) {
            for (int i = 0; i < args.length; i++) {
                src = src.replaceAll("\\{" + i + "\\}", args[i]);
            }

            return src;
        } else {
            return src;
        }
    }

    public static String joinList(String[] list, String sep) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.length != 0) {
            for (int i = 0; i < list.length; i++) {
                sb.append(list[i]);
                if (i != list.length - 1) {
                    sb.append(sep);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static <T> String list2String(List<T> intList) {
        try {
            StringBuilder sBuilder = new StringBuilder();

            for (T num : intList) {
                sBuilder.append(num + ";");
            }

            return sBuilder.toString();
        } catch (Exception var4) {
            return "";
        }
    }

    public static List<String> mailArgs(String str) {
        return stringToList(str, "\t");
    }

    public static List<String> stringToList(String str, String splitor) {
        List<String> ret = new ArrayList<>();
        return str != null && splitor != null ? Arrays.asList(str.split(splitor)) : ret;
    }

    public static int compareToBytes(String str, byte[] rawString) {
        byte[] raw1 = str.getBytes(Charset.forName("US-ASCII"));
        int min = Math.min(raw1.length, rawString.length);
        int diff = 0;

        int i;
        for (i = 0; i < min; i++) {
            diff = raw1[i] - rawString[i];
            if (diff != 0) {
                return diff > 0 ? 1 : -1;
            }
        }

        if (rawString.length == raw1.length) {
            return 0;
        } else {
            return i == rawString.length ? 1 : -1;
        }
    }

    public static int httpLikeHeader(byte[] input, int size) throws IOException {
        int cur = 0;
        int line_start = 0;
        int state = 0;
        int c = 0;
        int ContentLength = 0;

        for (boolean firstLine = true; cur < size; cur++) {
            int var13 = input[cur];
            if (state == 0) {
                if (var13 == 13) {
                    state = 1;
                }
            } else if (state == 1) {
                if (var13 != 10) {
                    return -2;
                }

                int var12 = 2;
                if (!firstLine) {
                    int lineLen = cur - line_start - 1;
                    if (lineLen <= 0) {
                        if (ContentLength > 0) {
                            if (cur + ContentLength + 1 <= size) {
                                return cur + ContentLength + 1;
                            }

                            return -3;
                        }

                        return cur + 1;
                    }

                    String line = new String(input, line_start, lineLen);
                    String[] split = line.split(":");
                    if (split.length == 2) {
                        String key = split[0].trim();
                        if (key.equals("Content-Length")) {
                            ContentLength = Integer.parseInt(split[1].trim());
                        }
                    }
                } else {
                    firstLine = false;
                }

                state = 0;
                line_start = cur + 1;
            }
        }

        return -1;
    }
}
