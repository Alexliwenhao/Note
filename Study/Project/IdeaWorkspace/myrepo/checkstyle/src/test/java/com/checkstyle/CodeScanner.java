package com.checkstyle;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CodeScanner {
    public static void main(String[] args) throws Exception {
        // 读取所有 Java 文件
        List<File> files = Arrays.stream(new File("src").listFiles())
                .filter(file -> file.getName().endsWith(".java"))
                .collect(Collectors.toList());

        // 创建 CheckStyle 检查器
        Checker checker = new Checker();
        checker.setModuleClassLoader(CodeScanner.class.getClassLoader());
        checker.configure(ConfigurationLoader.loadConfiguration("checkstyle.xml", null));

        // 将 AST 转换为流，进行检查和修复

        checker.process(files);

    }
}
