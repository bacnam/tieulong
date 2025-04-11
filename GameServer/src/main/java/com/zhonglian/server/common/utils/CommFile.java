package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommFile {
    public static void close(Closeable... closeables) {
        byte b;
        int i;
        Closeable[] arrayOfCloseable;
        for (i = (arrayOfCloseable = closeables).length, b = 0; b < i; ) {
            Closeable c = arrayOfCloseable[b];
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException e) {
                CommLog.error(NetUtil.class.getName(), e);
            }
            b++;
        }

    }

    public static String bufferedReader(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException("file not exist:" + path);
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    public static String BufferedReaderEncode(String path, String code) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }

        FileInputStream fr = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fr, code));

        StringBuilder sb = new StringBuilder();
        String temp = br.readLine();

        while (temp != null) {
            sb.append(temp).append(" ");
            temp = br.readLine();
        }
        return sb.toString();
    }

    public static Map<String, Map<String, String>> GetTable(String path, int fieldLine, int contentLine) {
        Map<String, Map<String, String>> table = new HashMap<>();

        File file = new File(path);
        if (!file.exists() || file.isDirectory() || fieldLine >= contentLine || fieldLine <= 0 || contentLine <= 0) {
            CommLog.error("file not exist:{} ", path);
            return table;
        }

        Charset cs = Charset.forName("utf-8");
        BufferedReader br = null;
        try {
            FileInputStream fr = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fr, cs));
        } catch (FileNotFoundException e) {
            CommLog.error(null, e);
        }
        if (br == null) {
            CommLog.error("Refdata readfailed:{} ", path);
            return table;
        }

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
                            close(new Closeable[]{br});
                            CommLog.error("ParseTable fieldCnt <= 0, path:{}", path);
                            return table;
                        }

                        fields = new String[values.length];
                        for (int index = 0; index < values.length; index++) {
                            fields[index] = values[index].trim();
                        }
                    } else if (curLine >= contentLine) {
                        if (fields == null) {
                            close(new Closeable[]{br});
                            CommLog.error("ParseTable fieldCnt null(fieldLine: {}), path:{}", Integer.valueOf(fieldLine), path);
                            return table;
                        }

                        String[] values = temp.split("\t");
                        if (fields.length < values.length) {
                            CommLog.error("解析表失败 [{}]头部字段数({})<值字段数({}) 无法解析, content:{}, path:{}", new Object[]{file.getName(), Integer.valueOf(fields.length), Integer.valueOf(values.length), temp});
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
                                CommLog.error("键值重复: {}, at line:{}, path:{}", new Object[]{key, Integer.valueOf(curLine), path});
                            }
                            table.put(key, lineValue);
                        }
                    }
                }
            } catch (IOException e) {
                CommLog.error(null, e);
            }
        } while (temp != null);

        close(new Closeable[]{br});

        return table;
    }

    public static List<Map<String, String>> GetRowSet(String path, int fieldLine, int contentLine) {
        List<Map<String, String>> lines = new ArrayList<>();
        Map<String, Map<String, String>> table = new ConcurrentHashMap<>();

        File file = new File(path);
        if (!file.exists() || file.isDirectory() || fieldLine >= contentLine || fieldLine <= 0 || contentLine <= 0) {
            return lines;
        }

        Charset cs = Charset.forName("utf-8");
        BufferedReader br = null;
        try {
            FileInputStream fr = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fr, cs));
        } catch (FileNotFoundException e) {
            CommLog.error(null, e);
        }
        if (br == null) {
            return lines;
        }

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
                            close(new Closeable[]{br});
                            CommLog.error("ParseTable fieldCnt <= 0, path:{}", path);
                            return lines;
                        }

                        fields = new String[values.length];
                        for (int index = 0; index < values.length; index++) {
                            fields[index] = values[index].trim();
                        }
                    } else if (curLine >= contentLine) {
                        if (fields == null) {
                            close(new Closeable[]{br});
                            CommLog.error("ParseTable fieldCnt null(fieldLine: {}), path:{}", Integer.valueOf(fieldLine), path);
                            return lines;
                        }

                        String[] values = temp.split("\t");
                        if (fields.length < values.length) {
                            CommLog.error("解析表失败 代码字段({}) < 配表字段({}), content:{}, path:{}", new Object[]{Integer.valueOf(fields.length), Integer.valueOf(values.length), temp, path});
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
                                CommLog.error("键值重复: {}, at line:{}, path:{}", new Object[]{key, Integer.valueOf(curLine), path});
                            }
                            table.put(key, lineValue);
                            lines.add(lineValue);
                        }
                    }
                }
            } catch (IOException e) {
                CommLog.error(null, e);
            }
        } while (temp != null);

        close(new Closeable[]{br});

        return lines;
    }

    public static void getFileListByPath(String dirPath, List<File> fileList, String postfix) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            return;
        }

        List<File> subDirFileList = new ArrayList<>();
        List<File> curDirFileList = new ArrayList<>();
        byte b;
        int i;
        File[] arrayOfFile1;
        for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) {
            File file = arrayOfFile1[b];
            if (file.isDirectory()) {
                getFileListByPath(file.getAbsolutePath(), subDirFileList, postfix);
            } else if (postfix.isEmpty() || file.getName().toLowerCase().endsWith("." + postfix.trim())) {

                curDirFileList.add(file);
            }

            b++;
        }

        fileList.addAll(subDirFileList);
        fileList.addAll(curDirFileList);
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
        } catch (FileNotFoundException e) {
            CommLog.error(null, e);
        } catch (IOException e) {
            CommLog.error(null, e);
        }
    }

    public static CharBuffer getFileData(String filePath) {
        CharBuffer data = null;

        File file = new File(filePath);
        if (!file.exists())
            return null;
        FileInputStream fin = null;
        InputStreamReader isReader = null;
        BufferedReader bufReader = null;

        try {
            fin = new FileInputStream(file);
            isReader = new InputStreamReader(fin, "UTF-8");
            bufReader = new BufferedReader(isReader);

            data = CharBuffer.allocate((int) file.length());
            int count = bufReader.read(data);
            data.limit(count);
        } catch (Exception e) {
            return null;
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException iOException) {
                }
            }

            if (isReader != null) {
                try {
                    isReader.close();
                } catch (IOException iOException) {
                }
            }

            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException iOException) {
                }
            }
        }

        return data;
    }

    public static String getTextFromFile(String strFilePath) {
        CharBuffer buf = getFileData(strFilePath);
        if (buf != null) {
            String res = new String(buf.array());
            return res.trim();
        }
        return null;
    }

    public static Set<String> getLineSetsFromFile(String strFilePath) {
        Set<String> rowsets = new HashSet<>();
        List<String> lines = getLinesFromFile(strFilePath);
        for (String line : lines) {
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
        } catch (FileNotFoundException e) {
            CommLog.error("getLinesFromFile", e);
        } catch (IOException e) {
            CommLog.error("getLinesFromFile", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    CommLog.error("getLinesFromFile", e);
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

