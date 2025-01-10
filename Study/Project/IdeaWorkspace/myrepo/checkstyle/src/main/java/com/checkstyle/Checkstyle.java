/*
package com.checkstyle;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

*/
/**
 * @auther: liwenhao
 * @Date: 2023/2/20 20:22
 * @Description:
 *//*

public class Checkstyle {

    private static Configuration configuration;

    public static void main(String[] args) throws Exception {
        // 加载Checkstyle配置文件
        configuration = ConfigurationLoader.loadConfiguration("checkstyle.xml",
                null);
        String rootDir = "";
        Files.walk(Paths.get(rootDir))
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().endsWith(".java"))
                .forEach(Checkstyle::processJavaFile);

    }

    private static void processJavaFile(Path path) {
        String currentFilePath = path.toAbsolutePath().toString();
        JavaParser javaParser = new JavaParser();
        try {

            Optional<CompilationUnit> result = javaParser.parse(new File(currentFilePath)).getResult();
            result.ifPresent(cu -> {
                // 使用Checkstyle检查和格式化代码
                new FileText()
                FileContents contents =
                        new FileContents(cu.get);
                        CheckstyleResults results =
                        new CheckstyleResults();
                LocalizedMessageRecorder messageRecorder =
                        new LocalizedMessageRecorder(results);


                try {
                    Checker checker = new com.puppycrawl.tools.checkstyle.Checker();
                    checker.setModuleClassLoader(Thread.currentThread().getContextClassLoader());
                    checker.configure(configuration);
                    checker.process(Collections.singletonList(contents));
                    checker.destroy();
                } catch (CheckstyleException e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
*/
