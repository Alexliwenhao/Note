package com.alex;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapperXmlScan {/*
    static Map<String, Set<String>> service2Table = new HashMap<>();
    static Map<String, String> table2Service = new HashMap<>();
    static Map<String, List<Path>> seviceMappers = new HashMap<>();
    static Set<Node> nodes = new HashSet<>();
    static Set<MapperTable> mapperTables = new HashSet<>();

    public static void main(String[] args) throws IOException, DocumentException, SAXException {
        // 建立服务和表的关系
        readTableMetadata(service2Table, table2Service);
        // 扫描所有mapper.xml
        readMappers();
        // 解析所有mapper.xml
        parseMappers();
        // 打印跨服务sql操作
        printCrossServiceSql();
        // 打印指定服务表所在的mapper
//        printServiceTalbeMappers("devops-cloud-user-service");
    }

    private static void printServiceTalbeMappers(String service) {
        ArrayList<MapperTable> items = new ArrayList<>(mapperTables);
        items.sort(Comparator.comparing((MapperTable l) -> l.table)
            .thenComparing(l -> l.file)
            .thenComparing(l -> l.method));
        for (MapperTable mapperTable : items) {
            if (!Objects.equals(service, mapperTable.service)) {
                continue;
            }
            String belongToService = table2Service.get(mapperTable.table);
            if (!Objects.equals(belongToService, service)) {
                continue;
            }
            System.out.printf("%s,%s,%s%n", mapperTable.table, mapperTable.file, mapperTable.method);
        }
    }

    private static void printCrossServiceSql() {
        for (MapperTable mapperTable : mapperTables) {
            String service = table2Service.get(mapperTable.table);
            if (Objects.equals(service, mapperTable.service)) {
                continue;
            }
            Node node = new Node();
            node.service = mapperTable.service;
            node.file = mapperTable.file;
            node.method = mapperTable.method;
            node.targetTable = mapperTable.table;
            node.targetService = service;
            nodes.add(node);
        }

        List<Node> allNodes = new ArrayList<>(nodes);
        List<String> lines = new ArrayList<>();
        for (Node allNode : allNodes) {
            allNode.translate();
            lines.add(allNode.toString());
        }
        Collections.sort(lines);
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static void parseMappers() throws DocumentException {

        SAXReader saxReader = new SAXReader();
        saxReader.setEntityResolver((publicId, systemId) -> new InputSource(new StringBufferInputStream("")));

        for (Map.Entry<String, List<Path>> stringListEntry : seviceMappers.entrySet()) {
            String service = stringListEntry.getKey();
            List<Path> mapperPaths = stringListEntry.getValue();
            for (Path mapperPath : mapperPaths) {
                Document read = saxReader.read(mapperPath.toFile());
                Element rootElement = read.getRootElement();
                Attribute namespaceAttribute = rootElement.attribute("namespace");
                String className = namespaceAttribute.getValue();
                String[] split = className.split("\\.");
                className = split[split.length - 1];
                List elements = rootElement.elements();
                for (Object item : elements) {
                    Element element = (Element) item;
                    if (
                        "insert".equals(element.getName())
                            || "select".equals(element.getName())
                            || "update".equals(element.getName())
                            || "delete".equals(element.getName())
                    ) {
                        String statementId = element.attribute("id").getValue();
                        visitStatement(service, className, statementId, element);
                    }
                }
            }
        }
    }

    private static void visitStatement(String service, String className, String statementId, Element statement) {
        List<String> lines = new ArrayList<>();
        toContents(statement, lines);
        for (String line : lines) {
            String[] split = line.trim().split("[., `<>(]");
            for (String keyword : split) {
                keyword = keyword.trim().toLowerCase();
                String belongToService = table2Service.get(keyword);
                if (belongToService == null) {
                    continue;
                }
                MapperTable mapperTable = new MapperTable();
                mapperTable.service = service;
                mapperTable.file = className;
                mapperTable.method = statementId;
                mapperTable.table = keyword;
                mapperTables.add(mapperTable);
            }
        }
    }

    private static void toContents(Element statement, List<String> lines) {
        if (statement instanceof DefaultText) {
            String text = ((DefaultText) statement).getText();
            lines.add(text);
        } else {
            List elements = statement.content();
            if (elements == null) {
                return;
            }
            for (Object element : elements) {
                if (element instanceof DefaultText) {
                    String text = ((DefaultText) element).getText();
                    lines.add(text);
                    continue;
                }
                if (element instanceof DefaultComment) {
                    continue;
                }
                if (element instanceof DefaultCDATA) {
                    continue;
                }
                toContents((Element) element, lines);
            }
        }
    }


    private static void readMappers() throws IOException {
        String rootDir = "/home/zyp/project/devops-cloud";
        for (String service : service2Table.keySet()) {
            List<Path> mappers = new ArrayList<>();
            seviceMappers.put(service, mappers);
            File rootDirFile = new File(rootDir, service);
            if (!rootDirFile.exists()) {
                continue;
            }
            Stream<Path> walk = Files.walk(rootDirFile.toPath(), Integer.MAX_VALUE);
            List<Path> mapperPaths = walk.filter(i -> Files.isRegularFile(i))
                .filter(i -> i.getFileName().toString().endsWith(".xml"))
                .filter(i -> i.getParent().getFileName().toString().equals("mapper"))
                .collect(Collectors.toList());
            for (Path mapperPath : mapperPaths) {
                mappers.add(mapperPath);
            }
        }
    }

    @EqualsAndHashCode
    public static class MapperTable {
        public String service;
        public String file;
        public String method;
        public String table;
    }

    @EqualsAndHashCode
    public static class Node implements Comparable<Node> {
        public String service;
        public String file;
        private String method;
        private String targetService;
        private String targetTable;

        public void translate() {
            service = translate(service);
            targetService = translate(targetService);
        }

        private String translate(String service) {
            if (service == null) {
                return "null";
            }
            switch (service) {
                case "devops-cloud-common-service":
                    return "通用服务";
                case "devops-cloud-issue-service":
                    return "事项服务";
                case "devops-cloud-pipeline-serivce":
                    return "流水线服务";
                case "devops-cloud-user-service":
                    return "用户服务";
                default:
                    return "未知";
            }
        }

        @Override
        public int compareTo(Node o) {
            int compare = service.compareTo(o.service);
            if (compare != 0) {
                compare = file.compareTo(o.file);
            }
            if (compare != 0) {
                compare = method.compareTo(o.method);
            }
            if (compare != 0) {
                compare = targetService.compareTo(o.targetService);
            }
            if (compare != 0) {
                compare = targetTable.compareTo(o.targetTable);
            }
            return compare;
        }

        @Override
        public String toString() {
            return String.format("%s,郑勇攀,%s,%s,%s,%s", service, file, method, targetTable, targetService);
        }
    }


    private static void readTableMetadata(Map<String, Set<String>> service2Table, Map<String, String> table2Service) throws IOException {
        InputStream resourceAsStream = MapperXmlScan.class.getClassLoader().getResourceAsStream("tables.json");
        JSONObject parse = JSON.parseObject(resourceAsStream);
        resourceAsStream.close();
        for (String service : parse.keySet()) {
            JSONArray jsonArray = parse.getJSONArray(service);
            int len = jsonArray.size();
            Set<String> tables = new HashSet<>();
            for (int i = 0; i < len; i++) {
                tables.add(jsonArray.getString(i).toLowerCase());
            }
            service2Table.put(service, tables);
            for (String table : tables) {
                table2Service.put(table, service);
            }
        }
    }*/
}
