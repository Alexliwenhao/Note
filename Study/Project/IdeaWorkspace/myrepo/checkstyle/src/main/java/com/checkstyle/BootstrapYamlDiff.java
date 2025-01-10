package com.checkstyle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import java.util.*;

public class BootstrapYamlDiff {
    public static void main(String[] args) throws IOException {

        String baseDir = "/Volumes/myDisk/myrepo/checkstyle/src/main/resources/";
        // 读取原始的两个bootstrap.yml文件
        String bootstrap1 = baseDir + "bootstrap1.yml";
        String bootstrap2 = baseDir + "bootstrap2.yml";
        String diffBootstrap = baseDir + "bootstrap_diff.yml";

        Map<String, Object> map1 = loadYamlFile(bootstrap1);
        Map<String, Object> map2 = loadYamlFile(bootstrap2);
        Map<String, Object> diff = computeDiff(map1, map2);
        writeYamlFile(diff, diffBootstrap);
    }




    private static Map<String, Object> loadYamlFile(String filePath) throws IOException {
        Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions());

        String content = FileUtils.readFileToString(new File(filePath), "UTF-8");
        Map<String, Object> map = yaml.load(content);

        return Optional.ofNullable(map).orElse(new LinkedHashMap<>());
    }


    private static Map<String, Object> computeDiff(Map<String, Object> map1, Map<String, Object> map2) {

        Map<String, Object> diff = new HashMap<>();

        for (String key : map1.keySet()) {
            if (map2.containsKey(key)) {
                Object value1 = map1.get(key);
                Object value2 = map2.get(key);
                if (!Objects.equals(value1, value2)) {
                    if (value1 instanceof Map && value2 instanceof Map) {
                        Map<String, Object> nestedDiff = computeDiff((Map<String, Object>) value1, (Map<String, Object>) value2);
                        if (!nestedDiff.isEmpty()) {
                            diff.put(key, nestedDiff);
                        }
                    } else {
                        diff.put(key, value1);
                    }
                }
            } else {
                diff.put(key, map1.get(key));
            }
        }

        for (String key : map2.keySet()) {
            if (!map1.containsKey(key)) {
                diff.put(key, map2.get(key));
            }
        }

        return diff;
    }


    private static void writeYamlFile(Map<String, Object> map, String filePath) throws IOException {
        Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions());

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory()
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
                .enable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID));

        String content = mapper.writeValueAsString(map);
        FileWriter fileWriter = new FileWriter(filePath);
        try {
            Node node = yaml.compose(new StringReader(content));
            Tag tag = node.getTag();
            Object obj = yaml.load(content);
            String yamlStr = yaml.dumpAs(obj, tag, DumperOptions.FlowStyle.BLOCK);
            fileWriter.write(yamlStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileWriter.close();
        }

    }
}

