package com.checkstyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanCFunctions {
    public static void main(String[] args) throws IOException {
        String rootDir = "";
        Files.walk(Paths.get(rootDir))
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().endsWith(".c") ||
                        p.getFileName().endsWith(".cpp"))
                .forEach(ScanCFunctions::processCFile);

    }

    private static void processCFile(Path path)  {
        String filename = path.toAbsolutePath().toString();
        String code = null;
        try {
            code = readFile(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /**
         *(\\w+)：匹配函数返回值类型，其中 \\w+ 表示匹配一个或多个单词字符（即字母、数字或下划线），() 表示这是一个捕获组。
         * \\s+：匹配一个或多个空白字符（包括空格、制表符等）。
         * (\\w+)：匹配函数名，同样使用 \\w+ 表示一个或多个单词字符，() 表示捕获组。
         * \\s*：匹配零个或多个空白字符。
         * \\(：匹配左括号。
         * ([^)]*)：匹配函数参数列表，其中 [^)]* 表示匹配零个或多个非右括号的字符，() 表示捕获组。
         * \\)：匹配右括号。
         * \\s*：匹配零个或多个空白字符。
         * \\{：匹配左花括号。
         */
        String regex = "(\\w+)\\s+(\\w+)\\s*\\(([^)]*)\\)\\s*\\{";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            String returnType = matcher.group(1);
            String functionName = matcher.group(2);
            String parameterList = matcher.group(3);

            System.out.println(returnType + " " + functionName + "(" + parameterList + ")");
        }
    }

    private static String readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append('\n');
        }
        reader.close();
        return builder.toString();
    }
}