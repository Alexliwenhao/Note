package com.checkstyle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExample {
//  public static void main(String[] args) {
//    String[] inputs = {"java正则表达式main(3 to 25 file1.c) - FAIL",
//                       "main(3 to 25 file-2.c) - FAIL",
//                       "main(3 to 25 file1.cpp) - FAIL",
//                       "main(3 to 25 file-2.cpp) - FAIL"};
//    Pattern pattern = Pattern.compile("[A-Za-z0-9-]+?\\.(c|cpp)");
//    for (String input : inputs) {
//      Matcher matcher = pattern.matcher(input);
//      if (matcher.find()) {
//        String filename = matcher.group(1);
//        System.out.println(filename);
//      }
//    }
//  }

  public static void main(String[] args) {
    String[] inputs = {"main(3 to 25 XXX.c)", " main(3 to 25 XX-X.c)", " main(3 to 25 XXX.cpp)", " main(3 to 25 XX-X.cpp)", " main(25 XXX.c)", " main(25 XX-X.c)", " main(25 XXX.cpp)", " main(25 XX-X.cpp)"};

    Pattern pattern = Pattern.compile("\\b[A-Za-z0-9-]+\\.(c|cpp)\\b");
    for (String input : inputs) {
      Matcher matcher = pattern.matcher(input);
      if (matcher.find()) {
        String filename = matcher.group();
        System.out.println(filename);
      }
    }
  }
}
