package com.checkstyle;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffAlgorithmListener;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @auther: liwenhao
 * @Date: 2023/2/24 09:38
 * @Description:
 */
public class BootstrapYamlComparator {

    public static void main(String[] args) throws IOException {
        String baseDir = "/Volumes/myDisk/myrepo/checkstyle/src/main/resources/";
        // 读取原始的两个bootstrap.yml文件
        String bootstrap1 = baseDir + "bootstrap1.yml";
        String bootstrap2 = baseDir + "bootstrap2.yml";
        String file1Content = FileUtils.readFileToString(new File(bootstrap1), "UTF-8");
        String file2Content = FileUtils.readFileToString(new File(bootstrap2), "UTF-8");

        // 解析YAML格式
        Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions());
        Map<String, Object> file1Map = (Map<String, Object>) yaml.load(file1Content);
        Map<String, Object> file2Map = (Map<String, Object>) yaml.load(file2Content);

        // 比较两个Map对象之间的差异
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String file1Json = objectMapper.writeValueAsString(file1Map);
        String file2Json = objectMapper.writeValueAsString(file2Map);
        Patch<String> patch = DiffUtils.diffInline(file1Json, file2Json);

        // 将差异内容输出到第三个bootstrap.yml文件中
        FileWriter fileWriter = new FileWriter(baseDir + "bootstrap_diff.yml");
        for (AbstractDelta<String> delta : patch.getDeltas()) {
            String diff = delta.getType().name().toString().trim();
            Node node = yaml.compose(new StringReader(diff));
            Tag tag = node.getTag();
            Object obj = yaml.load(diff);
            String yamlStr = yaml.dumpAs(obj, tag, DumperOptions.FlowStyle.BLOCK);
            fileWriter.write(yamlStr);
        }
        fileWriter.close();
    }
}

