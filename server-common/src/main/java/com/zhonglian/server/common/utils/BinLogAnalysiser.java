package com.zhonglian.server.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class BinLogAnalysiser {
    private static Map<String, Map<Long, Long>> analysie = new TreeMap<>();
    private static int timestamp = 0;

    public static void main(String[] args) throws Exception {
        File root = new File("./");
        long count = 0L;
        long filecount = 0L;

        File[] output;
        for (File file : output = root.listFiles()) {
            if (file.getName().endsWith(".txt")) {
                System.out.println("analysie file:" + file.getName() + "   count:" + ++filecount);
                try (FileReader fileReader = new FileReader(file); BufferedReader buffered = new BufferedReader(fileReader)) {
                    String line = null;

                    while ((line = buffered.readLine()) != null) {
                        record(line);
                        if (++count % 1000000L == 0L) {
                            System.out.println("analysise count:" + count);
                        }
                    }
                }
            }
        }

        System.out.println("analysise over, total:" + count);
        System.out.println("start output to analysie.out...");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (FileWriter outputx = new FileWriter(new File("analysie.out"))) {
            for (Entry<String, Map<Long, Long>> pair : analysie.entrySet()) {
                int amount = pair.getValue().values().stream().mapToInt(x -> x.intValue()).sum();
                StringBuilder head = new StringBuilder();
                StringBuilder line = new StringBuilder();
                head.append(pair.getKey()).append('\t');
                line.append(amount).append('\t');

                for (Entry<Long, Long> entry : pair.getValue().entrySet()) {
                    head.append(sdf.format(new Date(entry.getKey() * 1000L))).append("\t");
                    line.append(entry.getValue()).append("\t");
                    amount = (int)(amount + entry.getValue());
                }

                head.append('\n');
                line.append('\n');
                outputx.write(head.toString());
                outputx.write(line.toString());
            }
        }

        System.out.println("output to analysie.out over!");
    }

    private static void record(String line) {
        String start = null;
        if (line.startsWith("SET TIMESTAMP=")) {
            timestamp = Integer.parseInt(line.replace("SET TIMESTAMP=", "").replace("/*!*/;", ""));
        } else {
            if (line.startsWith("update")) {
                String[] split = line.split(" ", 3);
                start = split[1].replace("`", "").replace(",", "") + ' ' + split[0];
            } else if (line.startsWith("insert")) {
                String[] split = line.split(" ", 4);
                start = split[2].replace("(`id`", "").replace("(`ID`", "").replace("`", "").replace(",", "") + ' ' + split[0];
            } else if (line.startsWith("delete")) {
                String[] split = line.split(" ", 4);
                start = split[2].replace("`", "") + ' ' + split[0];
            }

            if (start != null) {
                Map<Long, Long> detail = analysie.get(start);
                if (detail == null) {
                    analysie.put(start, detail = new TreeMap<>());
                }

                long time = timestamp / 180 * 180;
                Long before = detail.get(time);
                detail.put(time, before == null ? 1L : before + 1L);
            }
        }
    }
}
