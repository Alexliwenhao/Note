package com.alex;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liwenhao
 * @since 2023/2/20 下午4:42
 * 有部分文件每个文件3万多行
 * 扫描出来总共2万3千多个函数
 * 总耗时：2719毫秒
 * 总函数数量：45326
 */
public class ScanCFunctions {

    static LongAdder longAdder = new LongAdder();
    public static void main(String[] args) throws IOException {
        /*StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String rootDir = "/home/liwenhao/testMyAst/test/";
        Files.walk(Paths.get(rootDir))
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().endsWith(".c") ||
                p.getFileName().toString().endsWith(".cpp"))
                .forEach(ScanCFunctions::processCFile);
        stopWatch.stop();
        System.out.println("总函数数量 = " + longAdder.sum());
        System.out.println("stopWatch = " + stopWatch.getTotalTimeMillis());*/
    }

    private static void processCFile(Path path) {
        String filePath = path.toAbsolutePath().toString();
        String fileName = path.getFileName().toString();
        System.out.println("文件名称：" + fileName);

        try {
            String code = readFile(filePath);
            String regex = "(\\w+)\\s+(\\w+)\\s*\\(([^)]*)\\)\\s*\\{";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(code);
            while (matcher.find()) {
                longAdder.increment();
                String returnType = matcher.group(1);
                String functionName = matcher.group(2);
                String paramList = matcher.group(3);
                System.out.println(returnType + " " + functionName + " (" + paramList + " )");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFile(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder builder = new StringBuilder();
        while ( (line = bufferedReader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }
}
