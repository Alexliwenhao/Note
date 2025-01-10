package com.alex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * TODO
 *
 * @author liwenhao
 * @date 2023/3/30 11:13
 * @copyright 成都精灵云科技有限公司
 */
public class GenerateSQL {

    public void generateSqlFile(List<String> rules, Function<String, String> mapper, String file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            rules.stream()
                    .map(mapper)
                    .distinct()
                    .forEach(str -> {
                        try {
                            bw.write(str);
                            bw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }


}
 