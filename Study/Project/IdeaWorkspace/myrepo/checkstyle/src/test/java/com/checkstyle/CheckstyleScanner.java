package com.checkstyle;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.LocalizedMessage;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessageBuilder;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import com.puppycrawl.tools.checkstyle.fix.CheckstyleExecutor;
import com.puppycrawl.tools.checkstyle.utils.ModuleFactory;
import com.puppycrawl.tools.checkstyle.utils.ModuleReflectionUtils;
import com.puppycrawl.tools.checkstyle.utils.PropertiesExpander;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CheckstyleScanner {

    public static void main(String[] args) throws IOException, CheckstyleException {
        // 配置Checkstyle
        Configuration checkstyleConfig = ConfigurationLoader.loadConfiguration("checkstyle.xml",null);

        // 扫描目录中的所有Java文件
        Path directory = Paths.get("src");
        List<CheckResult> checkResults = new ArrayList<>();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    try {
                        scanFile(file.toFile(), checkstyleConfig, checkResults);
                    } catch (Exception e) {
                        System.err.println("Error scanning file " + file.toAbsolutePath() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // 输出检查结果
        System.out.println(checkResults.size() + " errors and warnings detected:");
        for (CheckResult checkResult : checkResults) {
            System.out.println(checkResult);
        }
    }

    private static void scanFile(File file, Configuration checkstyleConfig, List<CheckResult> checkResults)
            throws IOException {
        // 使用JavaParser解析Java文件
        JavaParser javaParser = new JavaParser();
        CompilationUnit compilationUnit = javaParser.parse(file);

        // 使用Checkstyle检查Java文件
        DefaultConfiguration builder = (DefaultConfiguration) checkstyleConfig;
        builder.setFile(file.getAbsolutePath());
        builder.setClassLoader(Thread.currentThread().getContextClassLoader());
        builder.setCharset(StandardCharsets.UTF_8);
        builder.setPackageName("");
        builder.setSeverity(SeverityLevel.INFO);
        builder.setSourceName(file.getAbsolutePath());
        builder.setTimestamp(new Date());
        builder.setExternalInfo(Collections.emptyList());

        FileContents contents = new FileContents(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8));
        ModuleFactory moduleFactory = new ModuleFactory();
        moduleFactory.setClassLoader(Thread.currentThread().getContextClassLoader());
        moduleFactory.setPropertiesExpander(new PropertiesExpander(System.getProperties()));
        moduleFactory.setProperty("charset", StandardCharsets.UTF_8.name());
        moduleFactory.setProperty("tabWidth", "4");
        CheckstyleExecutor executor = new CheckstyleExecutor(builder.build());
        executor.setModuleFactory(moduleFactory);
        executor.setContent(contents);
        executor.addListener(new CheckstyleListener(checkResults));
        executor.process();

        // 格式化Java文件
        String originalCode = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        String formattedCode = formatCode(originalCode, checkstyleConfig);

        // 将格式化后的Java文件写回原文件
        if (!originalCode.equals(formattedCode)) {
            FileUtils.write(file, formattedCode, StandardCharsets.UTF_8);
        }
    }

    private static String formatCode(String code, Configuration checkstyleConfig) throws CheckstyleException {
        // 配置CheckstyleFixer
        CheckstyleExecutor checkstyleFixer = new CheckstyleExecutor(checkstyleConfig);
        checkstyleFixer.setModuleFactory(ModuleReflectionUtils.createModuleFactory(checkstyleConfig));
        checkstyleFixer.setClassLoader(Thread.currentThread().getContextClassLoader());

        // 格式化代码
        FileContents contents = new FileContents(code);
        StringWriter writer = new StringWriter();
        checkstyleFixer.process(Collections.singletonList(contents), new PrintWriter(writer));

        return writer.toString();
    }

    private static class CheckstyleListener implements CheckstyleExecutor.Listener {
        private final List<CheckResult> checkResults;

        public CheckstyleListener(List<CheckResult> checkResults) {
            this.checkResults = checkResults;
        }

        @Override
        public void onError(Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onFileProcessed(File file, List<LocalizedMessage> messages) {
            for (LocalizedMessage message : messages) {
                checkResults.add(new CheckResult(file, message));
            }
        }
    }

    private static class CheckResult {
        private final File file;
        private final LocalizedMessage message;

        public CheckResult(File file, LocalizedMessage message) {
            this.file = file;
            this.message = message;
        }

        @Override
        public String toString() {
            return file.getAbsolutePath() + ":" + message. + ":" + message.getColumn() + " " + message.getMessage();
        }
    }
}