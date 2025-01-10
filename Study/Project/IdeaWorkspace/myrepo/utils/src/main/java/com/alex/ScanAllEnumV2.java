/*
package com.alex;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.javadoc.Javadoc;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

*/
/**
* @author liwenhao
* @since 2023/2/16 下午2:48
*//*

public class ScanAllEnumV2 {
   public static void main(String[] args) throws IOException {
       String rootDir = "/home/liwenhao/IdeaProjects/devops-cloud-pipeline-serivce";
       Files.walk(Paths.get(rootDir))
               .filter(Files::isRegularFile)
               .filter(p -> p.getFileName().toString().endsWith(".java"))
               .forEach(ScanAllEnumV2::processFile);
   }

   private static void processFile(Path filePath) {
       try {
           String currentFilePath = filePath.toAbsolutePath().toString();
           System.out.println("当前文件是" + currentFilePath);

           JavaParser javaParser = new JavaParser();
           Optional<CompilationUnit> result = javaParser.parse(new File(currentFilePath)).getResult();
           result.ifPresent(cu -> {

               //处理枚举的
               List<EnumDeclaration> enums = cu.findAll(EnumDeclaration.class);
               for (EnumDeclaration ed : enums) {
                   processEnumDeclaration(ed);
               }

               //处理接口或者类的
               // List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class);
               // for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
               //     processClassOrInterfaceDeclaration(classOrInterfaceDeclaration);
               // }

               if (null != enums) {
                   try (FileOutputStream outputStream = new FileOutputStream(currentFilePath)) {
                       outputStream.write(cu.toString().getBytes());
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
           });

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
   }

   private static void processClassOrInterfaceDeclaration(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {

   }

   private static void processEnumDeclaration(EnumDeclaration ed) {
       String enumName = ed.getNameAsString();
       System.out.println("枚举类名称" + enumName);
       NodeList<EnumConstantDeclaration> declarations = ed.getEntries();
       for (EnumConstantDeclaration declaration : declarations) {
           processoEnumConstantDeclaration(declaration);

       }
   }

   private static void processoEnumConstantDeclaration(EnumConstantDeclaration entry) {
       String nameAsString = entry.getNameAsString();
       System.out.println("枚举常量名称" + nameAsString);
       Optional<Javadoc> javadoc = entry.getJavadoc();
       if (javadoc.isPresent()) {
           Javadoc v = javadoc.get();
           System.out.println(nameAsString + "的注释是" + v.getDescription());
       } else {
//            需要加注释的时候放开
            */
/*NodeList<Expression> arguments = entry.getArguments();
            String comment = null;
            for (Expression argument : arguments) {
                if (argument instanceof StringLiteralExpr) {
                    System.out.println("String 类型数据是" + argument);
                    comment = ((StringLiteralExpr) argument).asString();
                    break;
                }
            }
            JavadocDescription javadocDescription = new JavadocDescription();
            if (StringUtils.isBlank(comment)) {
                comment = nameAsString;
            }
            JavadocDescriptionElement javadocDescriptionElement = new JavadocSnippet(comment);
            javadocDescription.addElement(javadocDescriptionElement);
            Javadoc javadoc1 = new Javadoc(javadocDescription);
            entry.setJavadocComment(javadoc1);*//*

       }


   }
}
*/
