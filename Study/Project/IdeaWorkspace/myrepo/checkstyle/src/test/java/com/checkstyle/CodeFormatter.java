package com.checkstyle;

import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationResolver;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class CodeFormatter {

    public static void main(String[] args) throws Exception {
        String basePath = "src/main/java";
        List<File> javaFiles = getJavaFiles(basePath);
        Configuration checkstyleConfig = getCheckstyleConfiguration();

        Checker checker = new Checker();
        checker.setModuleClassLoader(Thread.currentThread().getContextClassLoader());
        checker.configure(checkstyleConfig);


        for (File javaFile : javaFiles) {
            String javaCode = FileUtils.readFileToString(javaFile, StandardCharsets.UTF_8);
            FileContents contents = new FileContents(javaCode, javaFile.getAbsolutePath());
            checker.process(Collections.singletonList(contents));
            String formattedCode = contents.getContents();

            CompilationUnit cu = StaticJavaParser.parse(javaFile);
            cu.accept(new CodeStyleVisitor(checkstyleConfig), null);
            String formattedCode = cu.toString();

            // 写回到原文件
            try (FileOutputStream fos = new FileOutputStream(javaFile)) {
                IOUtils.write(formattedCode, fos, StandardCharsets.UTF_8);
            }
        }
    }

    private static Configuration getCheckstyleConfiguration() throws CheckstyleException {
        ConfigurationResolver configurationResolver = new ConfigurationResolver();
        ConfigurationLoader configurationLoader = ConfigurationLoader.getInstance();
        Configuration checkstyleConfig = configurationLoader.loadConfiguration(
                configurationResolver.resolve(), new PropertiesExpander(System.getProperties()), true);
        return checkstyleConfig;
    }

    private static List<File> getJavaFiles(String basePath) {
        IOFileFilter javaFileFilter = new SuffixFileFilter(".java");
        IOFileFilter directoryFilter = FileFilterUtils.directoryFileFilter();
        IOFileFilter filter = FileFilterUtils.and(javaFileFilter, FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("package-info.java")));

        return (List<File>) FileUtils.listFiles(new File(basePath), filter, directoryFilter);
    }

    private static class CodeStyleVisitor extends ModifierVisitor<Void> {
        private final Checker checker;

        public CodeStyleVisitor(Configuration config) throws CheckstyleException {
            checker = new Checker();
            checker.setModuleClassLoader((ClassLoader) null);
            checker.configure(config);
        }

        @Override
        public Visitable visit(CompilationUnit cu, Void arg) {
            FileContents contents = new FileContents(cu.toString(), "temp.java");
            try {
                checker.process(Collections.singletonList(contents));
            } catch (CheckstyleException e) {
                String localizedMessage = e.getLocalizedMessage();
                System.err.printf(localizedMessage);
            }

            return super.visit(cu, arg);
        }
    }
}
