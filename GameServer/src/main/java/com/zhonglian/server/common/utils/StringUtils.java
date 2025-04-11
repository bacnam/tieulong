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
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    static final Pattern COMMA_SEP_TRIM_REGEX;
    static final Pattern COMMA_SEP_NO_TRIM_REGEX;

    static {
        try {
            COMMA_SEP_TRIM_REGEX = Pattern.compile("\\s*\\,\\s*");
            COMMA_SEP_NO_TRIM_REGEX = Pattern.compile("\\,");
        } catch (PatternSyntaxException e) {
            CommLog.error(null, e);
            throw new InternalError(e.toString());
        }
    }

    public static List<Integer> string2Integer(String str) {
        try {
            List<Integer> intList = new ArrayList<>();
            String[] strList = str.split(";");
            byte b;
            int i;
            String[] arrayOfString1;
            for (i = (arrayOfString1 = strList).length, b = 0; b < i; ) {
                String item = arrayOfString1[b];
                intList.add(Integer.valueOf(item));
                b++;
            }

            return intList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static String replaceArgs(String src, String... args) {
        if (args == null || src == null) {
            return src;
        }
        for (int i = 0; i < args.length; i++) {
            src = src.replaceAll("\\{" + i + "\\}", args[i]);
        }
        return src;
    }

    public static String joinList(String[] list, String sep) {
        StringBuffer sb = new StringBuffer();
        if (list == null || list.length == 0) {
            return "";
        }
        for (int i = 0; i < list.length; i++) {
            sb.append(list[i]);
            if (i != list.length - 1) {
                sb.append(sep);
            }
        }

        return sb.toString();
    }

    public static <T> String list2String(List<T> intList) {
        try {
            StringBuilder sBuilder = new StringBuilder();
            for (T num : intList) {
                sBuilder.append((new StringBuilder()).append(num).append(";").toString());
            }
            return sBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static List<String> mailArgs(String str) {
        return stringToList(str, "\t");
    }

    public static List<String> stringToList(String str, String splitor) {
        List<String> ret = new ArrayList<>();
        if (str == null || splitor == null) {
            return ret;
        }
        return Arrays.asList(str.split(splitor));
    }

    public static int compareToBytes(String str, byte[] rawString) {
        byte[] raw1 = str.getBytes(Charset.forName("US-ASCII"));

        int min = Math.min(raw1.length, rawString.length);
        int diff = 0;
        int i;
        for (i = 0; i < min; i++) {
            diff = raw1[i] - rawString[i];
            if (diff != 0) {
                return (diff > 0) ? 1 : -1;
            }
        }
        if (rawString.length == raw1.length) {
            return 0;
        }

        return (i == rawString.length) ? 1 : -1;
    }

    public static int httpLikeHeader(byte[] input, int size) throws IOException {
        int cur = 0;
        int line_start = 0;
        int state = 0;
        int c = 0;

        int ContentLength = 0;
        boolean firstLine = true;

        while (cur < size) {
            c = input[cur];

            if (state == 0) {
                if (c == 13) {
                    state = 1;
                }
            } else if (state == 1) {
                if (c == 10) {
                    state = 2;

                    if (!firstLine) {
                        int lineLen = cur - line_start - 1;
                        if (lineLen > 0) {
                            String line = new String(input, line_start, lineLen);
                            String[] split = line.split(":");
                            if (split.length == 2) {
                                String key = split[0].trim();
                                if (key.equals("Content-Length")) {
                                    ContentLength = Integer.parseInt(split[1].trim());
                                }
                            }
                        } else {
                            if (ContentLength > 0) {
                                if (cur + ContentLength + 1 <= size) {
                                    return cur + ContentLength + 1;
                                }
                                return -3;
                            }

                            return cur + 1;
                        }
                    } else {

                        firstLine = false;
                    }
                    state = 0;
                    line_start = cur + 1;
                } else {
                    return -2;
                }
            }
            cur++;
        }
        return -1;
    }
}

