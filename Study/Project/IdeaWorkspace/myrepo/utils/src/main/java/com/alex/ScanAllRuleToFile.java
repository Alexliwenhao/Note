/*
package com.alex;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

*/
/**
 * TODO
 *
 * @author liwenhao
 * @date 2023/3/29 16:38
 * @copyright 成都精灵云科技有限公司
 *//*

public class ScanAllRuleToFile {

    private static final String REGE = "document\\.write\\(.*?'(.*?)'\\)\\+.*?'(.*?)'\\)";

    static GenerateSQL generateSQL = new GenerateSQL();

    static Set<String> standards = new HashSet<>();

    static {
        standards.add("(M) Mandatory Standards");
        standards.add("Required Standards");
        standards.add("Advisory Standards");
        standards.add("Shall Standards");
        standards.add("Will Standards");
        standards.add("Should Standards");
    }

    static String ruleSqlTemp = "INSERT INTO `rule` (`set_id`,`base_rule_id`,`rule_name`,`tool_name`," +
            "`priority`,`flag`,`delete_flag`,`enable_flag`,`rule_name_ch`,`map`,`map_own`,`grade`) " +
            "VALUES((SELECT id from rule_set WHERE `name` = 'testBed' AND lang = 'C' AND description = '初始化数据')," +
            "(SELECT id FROM base_rule WHERE tool_name = " +
            "'testBed' AND rule_name = '%s'),'%s','testBed',0,0,1,1,'%s',0,0,0);";
    static String ruleSqlFile = "D:\\ubuntu_alex\\IdeaProjects\\devops-cloud-static-analysis-service\\" +
            "static-analysis-service\\src\\test\\resources\\rule.sql";

    static String baseRuleSqlTemp = "INSERT INTO `base_rule`(`tool_id`,`tool_name`,`rule_name`,`rule_name_ch`) " +
            "VALUES((SELECT id FROM tool WHERE tool_name = 'testBed'),'testBed','%s','%s');";
    static String baseRuleSqlFile = "D:\\ubuntu_alex\\IdeaProjects\\devops-cloud-static-analysis-service\\" +
            "static-analysis-service\\src\\test\\resources\\baseRule.sql";

    public static void main(String[] args) throws IOException {
        boolean flag = false;
        if (flag) {
            scanAllRule();
        } else {
            List<String> rules = readAllRule("tableTran.json");
            Map<String, String> collect = rules.stream()
                    .collect(Collectors.toMap(m -> StringUtils.substringBefore(m, "&&")
                            .replaceAll("'", ""), b -> StringUtils.substringAfter(b, "&&")
                            .replaceAll("'", ""), (v1, v2) -> v2));

            rules = collect.entrySet().stream()
                    .map(m -> m.getKey() + "&&" + m.getValue())
                    .collect(Collectors.toList());

            generateSQL.generateSqlFile(rules,
                    m -> String.format(baseRuleSqlTemp, StringUtils.substringBefore(m, "&&")
                                    .replaceAll("'", ""),
                            StringUtils.substringAfter(m, "&&")
                                    .replaceAll("'", "")),
                    baseRuleSqlFile);

            generateSQL.generateSqlFile(rules,
                    m -> String.format(ruleSqlTemp, StringUtils.substringBefore(m, "&&")
                                    .replaceAll("'", ""),
                            StringUtils.substringBefore(m, "&&")
                                    .replaceAll("'", ""),
                            StringUtils.substringAfter(m, "&&")
                                    .replaceAll("'", "")),
                    ruleSqlFile);
        }


    }


    public void writeFile() throws IOException {
        List<String> allRule = readAllRule("allRuleTable.json");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("addRule.txt"))) {
            for (String s : allRule) {
                bw.write(s);
                bw.newLine();
            }
        }
    }

    private static void mergeFileToNewFile() throws IOException {

        String rulesTran = "D:\\\\ubuntu_alex\\\\IdeaProjects\\\\devops-cloud-static-analysis-service\\\\" +
                "static-analysis-service\\\\src\\\\test\\\\resources\\\\rulesTran.txt";
        List<String> rulesList = readAllRule("table.json");
        List<String> rulesTranList = Files.readAllLines(Paths.get(rulesTran));
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < rulesList.size(); i++) {
            strings.add(rulesList.get(i) + "&&" +rulesTranList.get(i));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("table", strings);
        String s = jsonObject.toJSONString();
        String fileName = "D:\\\\ubuntu_alex\\\\IdeaProjects\\\\devops-cloud-static-analysis-service\\\\" +
                "static-analysis-service\\\\src\\\\test\\\\resources\\\\tableTran.json";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static List<String> readAllRule(String fileName) {
        InputStream resourceAsStream = ScanAllRuleToFile.class.getClassLoader().getResourceAsStream(fileName);
        com.alibaba.fastjson2.JSONObject parse = JSON.parseObject(resourceAsStream);
        List<String> rules = parse.getList("table", String.class);
        return rules;
    }

    private static void scanAllRule() throws IOException {
        String shallStandardsFile = "C:\\agent\\devops\\project-2133\\pipeline-1373\\9\\2133_pipeline-1373_9_tbwrkfls" +
                "\\2133_pipeline-1373_9.rps.htm";
        String requiredStandardsFile = "C:\\agent\\devops\\project-2356\\pipeline-1495\\18\\task\\" +
                "2356_pipeline-1495_18_tbwrkfls\\2356_pipeline-1495_18.rps.htm";
        String standardsFile = "C:\\agent\\devops\\project-2318\\pipeline-1383\\34\\include\\" +
                "2318_pipeline-1383_34_tbwrkfls\\2318_pipeline-1383_34.rps.htm";

        List<Element> elements = new ArrayList<>();
        findMatchTable(shallStandardsFile, elements);
        findMatchTable(requiredStandardsFile, elements);
        findMatchTable(standardsFile, elements);
        Set<String> tables = resolveTable(elements);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("table", tables);
        String s = jsonObject.toJSONString();
        String fileName = "table.json";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Set<String> resolveTable(List<Element> elements) {
        return elements.stream()
                .map(m -> {
                    Elements select = m.select("tbody > tr");
                    Iterator<Element> iterator = select.iterator();
                    iterator.next();
                    List<String> strings = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Element next = iterator.next();
                        Element child = next.child(2);
                        Element script = child.select("script").first();
                        String str = script.toString();
                        String substringAfter =
                                StringUtils.substringAfter(str, "<script language=\"javascript\">");
                        String substringBefore = StringUtils.substringBefore(substringAfter, "</script>");

                        final String s = StringUtils.substringBeforeLast(substringBefore, "+");
                        final String afterLast = StringUtils.substringAfterLast(s, "+");
                        final String last = StringUtils.substringBeforeLast(afterLast, "')");
                        final String s1 = StringUtils.substringAfterLast(last, "document.write('");
                        strings.add(s1);
                    }
                    return strings;
                })
                .flatMap(Collection::stream)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());


    }

    public static void findMatchTable(String filePath, List<Element> elements) throws IOException {
        File shallStandardsFile = new File(filePath);

        Document doc = Jsoup.parse(shallStandardsFile, "UTF-8");

        Elements bgcolor = doc.getElementsByAttributeValue("bgcolor", "#ECE2E2");
        for (Element element : bgcolor) {
            Elements select = element.select("tbody > tr > th");
            String text = select.text();
            boolean b = standards.stream().anyMatch(text::contains);
            if (b) {
                elements.add(element);
            }
        }
    }
}
 */
