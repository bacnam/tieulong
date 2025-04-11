package com.zhonglian.server.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class BinLogAnalysiser {
    private static Map<String, Map<Long, Long>> analysie = new TreeMap<>();
    private static int timestamp = 0;

    public static void main(String[] args) throws Exception {
        File root = new File("./");
        long count = 0L;
        long filecount = 0L;
        byte b;
        int i;
        File[] arrayOfFile;
        for (i = (arrayOfFile = root.listFiles()).length, b = 0; b < i; ) {
            File file = arrayOfFile[b];
            if (file.getName().endsWith(".txt")) {
                Exception exception4;

                filecount++;
                System.out.println("analysie file:" + file.getName() + "   count:" + filecount);
                Exception exception3 = null;
            }

            b++;
        }

        System.out.println("analysise over, total:" + count);
        System.out.println("start output to analysie.out...");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Exception exception1 = null, exception2 = null;
        try {
            FileWriter output = new FileWriter(new File("analysie.out"));

            try {
                for (Map.Entry<String, Map<Long, Long>> pair : analysie.entrySet()) {
                    int amount = ((Map) pair.getValue()).values().stream().mapToInt(x -> x.intValue()).sum();
                    StringBuilder head = new StringBuilder();
                    StringBuilder line = new StringBuilder();
                    head.append(pair.getKey()).append('\t');
                    line.append(amount).append('\t');
                    for (Map.Entry<Long, Long> entry : (Iterable<Map.Entry<Long, Long>>) ((Map) pair.getValue()).entrySet()) {
                        head.append(sdf.format(new Date(((Long) entry.getKey()).longValue() * 1000L))).append("\t");
                        line.append(entry.getValue()).append("\t");
                        amount = (int) (amount + ((Long) entry.getValue()).longValue());
                    }
                    head.append('\n');
                    line.append('\n');
                    output.write(head.toString());
                    output.write(line.toString());
                }
            } finally {
                if (output != null) output.close();
            }
        } finally {
            exception2 = null;
            if (exception1 == null) {
                exception1 = exception2;
            } else if (exception1 != exception2) {
                exception1.addSuppressed(exception2);
            }
        }

    }

    private static void record(String line) {
        String start = null;
        if (line.startsWith("SET TIMESTAMP=")) {
            timestamp = Integer.parseInt(line.replace("SET TIMESTAMP=", "").replace(";", ""));
            return;
        }
        if (line.startsWith("update")) {
            String[] split = line.split(" ", 3);
            start = String.valueOf(split[1].replace("`", "").replace(",", "")) + ' ' + split[0];
        } else if (line.startsWith("insert")) {
            String[] split = line.split(" ", 4);
            start = String.valueOf(split[2].replace("(`id`", "").replace("(`ID`", "").replace("`", "").replace(",", "")) + ' ' + split[0];
        } else if (line.startsWith("delete")) {
            String[] split = line.split(" ", 4);
            start = String.valueOf(split[2].replace("`", "")) + ' ' + split[0];
        }
        if (start == null) {
            return;
        }
        Map<Long, Long> detail = analysie.get(start);
        if (detail == null) {
            analysie.put(start, detail = new TreeMap<>());
        }
        long time = (timestamp / 180 * 180);
        Long before = detail.get(Long.valueOf(time));
        detail.put(Long.valueOf(time), Long.valueOf((before == null) ? 1L : (before.longValue() + 1L)));
    }
}

