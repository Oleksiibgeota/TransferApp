package com.test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferApp {

    public static void main(String[] args) throws IOException {
//        String inputFilePath = args[0];
//        String outputFilePath = args[1];

        String patternNumber = "(\\w{3}\\s)(\\d{5})";
        String patternDistance = "(\\w{2}\\s+)(\\d{1,4}\\.\\d{3})(.*)";
        String patternHorizontalAngel = "(\\w{2}\\s+)(\\d{1,3}\\.\\d{4})(.*)";
        String patternVerticalAngel = "(\\w{1}\\d{1}\\s+)(\\d{1,3}\\.\\d{4})(.*)";

        String inputFilePath = "D:\\Леша\\WP\\2018\\borodai\\180208.dat";
        String outputFilePath = "out2.txt";

        String timestamp = getTimestamp();

        List<String> lines = getFileLine(inputFilePath);
        for (String line : lines) {
            StringJoiner destinationLine = new StringJoiner(",", "", "\n");
            destinationLine
                    .add("SS")
                    .add(parseRawData(getRawData(line, 1), patternNumber))
                    .add("0")
                    .add(parseRawData(getRawData(line, 3), patternDistance))
                    .add(parseRawData(getRawData(line, 4), patternHorizontalAngel))
                    .add(parseRawData(getRawData(line, 5), patternVerticalAngel))
                    .add(timestamp)
                    .add("");
            writeConvertFile(destinationLine.toString(), outputFilePath);
        }
    }

    private static String getRawData(String line, int position) {
        String[] lineArr = line.split("\\|");
        if (lineArr.length > position) {
            return lineArr[position];
        }
        return null;
    }

    private static String parseRawData(String rawData, String pattern) {
        Pattern pointNumberPattern = Pattern.compile(pattern);
        Matcher matcher = pointNumberPattern.matcher(rawData);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return null;
    }

    private static String getTimestamp() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        Date now = Calendar.getInstance().getTime();
        return sdfTime.format(now);
    }


    private static List<String> getFileLine(String filePath) {
        String line;
        List<String> lines = new ArrayList<>();
        try (InputStream is = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static void writeConvertFile(String line, String outputFile) throws IOException {
        File file = new File(outputFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileWriter fw = new FileWriter(file.getAbsoluteFile(), true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(line);
        }
    }
}
