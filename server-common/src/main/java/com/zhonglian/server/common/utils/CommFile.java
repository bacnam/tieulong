package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CommFile {
    public static void close(Closeable... closeables) {
        for (Closeable c : closeables) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException var6) {
                CommLog.error(NetUtil.class.getName(), (Throwable)var6);
            }
        }
    }

    public static String bufferedReader(String path) throws IOException {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            br.close();
            return sb.toString();
        } else {
            throw new FileNotFoundException("file not exist:" + path);
        }
    }

    public static String BufferedReaderEncode(String path, String code) throws IOException {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            FileInputStream fr = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fr, code));
            StringBuilder sb = new StringBuilder();

            for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
                sb.append(temp).append(" ");
            }

            return sb.toString();
        } else {
            throw new FileNotFoundException();
        }
    }

    public static Map<String, Map<String, String>> GetTable(String path, int fieldLine, int contentLine) {
        Map<String, Map<String, String>> table = new HashMap<>();
        File file = new File(path);
        if (file.exists() && !file.isDirectory() && fieldLine < contentLine && fieldLine > 0 && contentLine > 0) {
            Charset cs = Charset.forName("utf-8");
            BufferedReader br = null;

            try {
                FileInputStream fr = new FileInputStream(file);
                br = new BufferedReader(new InputStreamReader(fr, cs));
            } catch (FileNotFoundException var13) {
                CommLog.error(null, var13);
            }

            if (br == null) {
                CommLog.error("Refdata readfailed:{} ", path);
                return table;
            } else {
                String temp = null;
                String[] fields = null;
                int curLine = 0;

                do {
                    try {
                        temp = br.readLine();
                        if (temp == null) {
                            break;
                        }

                        temp = temp.trim();
                        curLine++;
                        if (!temp.equals("")) {
                            if (fieldLine == curLine) {
                                String[] values = temp.split("\t");
                                if (values.length <= 0) {
                                    close(br);
                                    CommLog.error("ParseTable fieldCnt <= 0, path:{}", path);
                                    return table;
                                }

                                fields = new String[values.length];

                                for (int index = 0; index < values.length; index++) {
                                    fields[index] = values[index].trim();
                                }
                            } else if (curLine >= contentLine) {
                                if (fields == null) {
                                    close(br);
                                    CommLog.error("ParseTable fieldCnt null(fieldLine: {}), path:{}", fieldLine, path);
                                    return table;
                                }

                                String[] values = temp.split("\t");
                                if (fields.length < values.length) {
                                    CommLog.error("解析表失败 [{}]头部字段数({})<值字段数({}) 无法解析, content:{}, path:{}", file.getName(), fields.length, values.length, temp);
                                } else {
                                    Map<String, String> lineValue = new ConcurrentHashMap<>();

                                    for (int index = 0; index < fields.length; index++) {
                                        if (values.length > index) {
                                            lineValue.put(fields[index], values[index].trim());
                                        } else {
                                            lineValue.put(fields[index], "");
                                        }
                                    }

                                    String key = values[0];
                                    if (table.containsKey(key)) {
                                        CommLog.error("键值重复: {}, at line:{}, path:{}", key, curLine, path);
                                    }

                                    table.put(key, lineValue);
                                }
                            }
                        }
                    } catch (IOException var14) {
                        CommLog.error(null, var14);
                    }
                } while (temp != null);

                close(br);
                return table;
            }
        } else {
            CommLog.error("file not exist:{} ", path);
            return table;
        }
    }

    public static List<Map<String, String>> GetRowSet(String path, int fieldLine, int contentLine) {
        List<Map<String, String>> lines = new ArrayList<>();
        Map<String, Map<String, String>> table = new ConcurrentHashMap<>();
        File file = new File(path);
        if (file.exists() && !file.isDirectory() && fieldLine < contentLine && fieldLine > 0 && contentLine > 0) {
            Charset cs = Charset.forName("utf-8");
            BufferedReader br = null;

            try {
                FileInputStream fr = new FileInputStream(file);
                br = new BufferedReader(new InputStreamReader(fr, cs));
            } catch (FileNotFoundException var14) {
                CommLog.error(null, var14);
            }

            if (br == null) {
                return lines;
            } else {
                String temp = null;
                String[] fields = null;
                int curLine = 0;

                do {
                    try {
                        temp = br.readLine();
                        if (temp == null) {
                            break;
                        }

                        temp = temp.trim();
                        curLine++;
                        if (!temp.equals("")) {
                            if (fieldLine == curLine) {
                                String[] values = temp.split("\t");
                                if (values.length <= 0) {
                                    close(br);
                                    CommLog.error("ParseTable fieldCnt <= 0, path:{}", path);
                                    return lines;
                                }

                                fields = new String[values.length];

                                for (int index = 0; index < values.length; index++) {
                                    fields[index] = values[index].trim();
                                }
                            } else if (curLine >= contentLine) {
                                if (fields == null) {
                                    close(br);
                                    CommLog.error("ParseTable fieldCnt null(fieldLine: {}), path:{}", fieldLine, path);
                                    return lines;
                                }

                                String[] values = temp.split("\t");
                                if (fields.length < values.length) {
                                    CommLog.error("解析表失败 代码字段({}) < 配表字段({}), content:{}, path:{}", fields.length, values.length, temp, path);
                                } else {
                                    Map<String, String> lineValue = new ConcurrentHashMap<>();

                                    for (int index = 0; index < fields.length; index++) {
                                        if (values.length > index) {
                                            lineValue.put(fields[index], values[index].trim());
                                        } else {
                                            lineValue.put(fields[index], "");
                                        }
                                    }

                                    String key = values[0];
                                    if (table.containsKey(key)) {
                                        CommLog.error("键值重复: {}, at line:{}, path:{}", key, curLine, path);
                                    }

                                    table.put(key, lineValue);
                                    lines.add(lineValue);
                                }
                            }
                        }
                    } catch (IOException var15) {
                        CommLog.error(null, var15);
                    }
                } while (temp != null);

                close(br);
                return lines;
            }
        } else {
            return lines;
        }
    }

    public static void getFileListByPath(String dirPath, List<File> fileList, String postfix) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            List<File> subDirFileList = new ArrayList<>();
            List<File> curDirFileList = new ArrayList<>();

            for (File file : files) {
                if (file.isDirectory()) {
                    getFileListByPath(file.getAbsolutePath(), subDirFileList, postfix);
                } else if (postfix.isEmpty() || file.getName().toLowerCase().endsWith("." + postfix.trim())) {
                    curDirFileList.add(file);
                }
            }

            fileList.addAll(subDirFileList);
            fileList.addAll(curDirFileList);
        }
    }

    public static void getLinesFromFile(List<String> methodList, String strFilePath) {
        File file = null;
        BufferedReader br = null;
        String strLine = null;

        try {
            file = new File(strFilePath);
            br = new BufferedReader(new FileReader(file));

            while ((strLine = br.readLine()) != null) {
                if (!strLine.trim().isEmpty()) {
                    methodList.add(strLine.trim());
                }
            }
        } catch (FileNotFoundException var6) {
            CommLog.error(null, var6);
        } catch (IOException var7) {
            CommLog.error(null, var7);
        }
    }

    public static CharBuffer getFileData(String filePath) {
        CharBuffer data = null;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        } else {
            FileInputStream fin = null;
            InputStreamReader isReader = null;
            BufferedReader bufReader = null;

            try {
                fin = new FileInputStream(file);
                isReader = new InputStreamReader(fin, "UTF-8");
                bufReader = new BufferedReader(isReader);
                data = CharBuffer.allocate((int)file.length());
                int count = bufReader.read(data);
                ((Buffer)data).limit(count);
                return data;
            } catch (Exception var23) {
            } finally {
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (IOException var22) {
                    }
                }

                if (isReader != null) {
                    try {
                        isReader.close();
                    } catch (IOException var21) {
                    }
                }

                if (bufReader != null) {
                    try {
                        bufReader.close();
                    } catch (IOException var20) {
                    }
                }
            }

            return null;
        }
    }

    public static String getTextFromFile(String strFilePath) {
        CharBuffer buf = getFileData(strFilePath);
        if (buf != null) {
            String res = new String(buf.array());
            return res.trim();
        } else {
            return null;
        }
    }

    public static Set<String> getLineSetsFromFile(String strFilePath) {
        Set<String> rowsets = new HashSet<>();

        for (String line : getLinesFromFile(strFilePath)) {
            rowsets.add(line);
        }

        return rowsets;
    }

    public static List<String> getLinesFromFile(String strFilePath) {
        File file = null;
        BufferedReader br = null;
        String strLine = null;
        List<String> rowsets = new LinkedList<>();

        try {
            file = new File(strFilePath);
            br = new BufferedReader(new FileReader(file));

            while ((strLine = br.readLine()) != null) {
                if (!strLine.trim().isEmpty()) {
                    rowsets.add(strLine.trim());
                }
            }
        } catch (FileNotFoundException var16) {
            CommLog.error("getLinesFromFile", (Throwable)var16);
        } catch (IOException var17) {
            CommLog.error("getLinesFromFile", (Throwable)var17);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception var15) {
                    CommLog.error("getLinesFromFile", (Throwable)var15);
                }
            }
        }

        return rowsets;
    }

    public static void Copy(String from, String to) throws IOException {
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;

        try {
            String currentDir = System.getProperty("user.dir");
            CommLog.info("Current dir using System:" + currentDir);
            fromFile = new RandomAccessFile(from, "r");
            FileChannel fromChannel = fromFile.getChannel();
            toFile = new RandomAccessFile(to, "rw");
            FileChannel toChannel = toFile.getChannel();
            long size = fromChannel.size();
            fromChannel.transferTo(0L, size, toChannel);
        } finally {
            if (fromFile != null) {
                fromFile.close();
            }

            if (toFile != null) {
                toFile.close();
            }
        }
    }

    public static void Write(String path, byte[] content) throws IOException {
        Write(path, new String(content));
    }

    public static void Write(String path, String content) throws IOException {
        File f = new File(path);
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.write(content);
        osw.flush();
        osw.close();
    }
}
