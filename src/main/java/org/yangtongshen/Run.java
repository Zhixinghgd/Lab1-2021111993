package org.yangtongshen;

import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Run {
    private static int node_num=0; // 用于生成Node的id

    public static int[][] graph;
    public static List<Node> nodes;
    public static List<Integer> path;

    public int[][] getGraph() {
        return graph;
    }

    public int getNode_num() {
        return node_num;
    }

    public List<Node> getNodes() {
        return nodes;
    }
    public static void setNodes(List<Node> nodes) {
        Run.nodes = nodes;
    }

    public static void setGraph(int[][] graph) {
        Run.graph = graph;
    }

    public static void main(String[] args) {
        String filePath = "./file_long.txt";
        List<Node> nodes = Read(filePath); // 调用read函数并获取Node列表
        // 打印所有Node的信息
        for (Node node : nodes) {
            System.out.println("ID: " + node.id + ", Name: " + node.name + ", Use Num: " + node.use_num);
        }
        System.out.println("目前节点总数" + node_num);
        graph = createGraph(node_num, filePath, nodes);//int[][]
        showDirectedGraph(graph, nodes);//画图
        System.out.println("输出邻接矩阵如下：");
        for (int[] row : graph) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        boolean flag = true;
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while(flag){
            System.out.print("功能1: 查询桥接词\n功能2：根据桥接词生成新文本\n功能3：计算最短路径\n功能4：随机游走\n-1：退出\n ");
            int input = scanner.nextInt();
            if(input == -1){
                System.out.print("程序已经退出");
                flag = false;
            }
            switch (input) {
                case 1:
                    readBridgeWord();//功能1: 查询桥接词
                    break;
                case 2:
                    generateNewText1();//功能2：根据桥接词生成新文本
                    break;
                case 3:
                    jisuanshortestpath(nodes);//功能3：计算最短路径
                    break;
                case 4:
                    String result=randomWalk(graph, nodes);//功能4：随机游走
                    System.out.print(result+"\n");
                default:
                    // Default case to handle unexpected values
                   // System.out.println("Invalid number of arguments");
                    break;
            }
        }
        scanner.close();
    }

    private static void jisuanshortestpath(List<Node> nodes) {
        String start, end;
        Scanner scanner = new Scanner(System.in, "UTF-8");
        System.out.print("请输入起点词：");
        start = scanner.nextLine();
        System.out.print("请输入终点词：");
        end = scanner.nextLine();
        if (getNodeByName(nodes, start) != null && getNodeByName(nodes, end) != null) {
            String shortestpath = null ;
            shortestpath = calcShortestPath(start, end);
            drawline(graph, nodes, shortestpath);
            if (shortestpath.isEmpty()) {
                System.out.println(start + "到" + end + "不可达");
            } else {
                System.out.println("路径为：" + shortestpath);
            }
        } else {
            System.out.println("输入词中有不属于句子的节点");
        }
    }

    public static String calcShortestPath(String word1, String word2) {
        int start_id = 0, end_id = 0;
        String strpath = "";
        start_id = getNodeIdByName(nodes, word1);
        end_id = getNodeIdByName(nodes, word2);
        path = dijkstra(start_id - 1, end_id - 1);
        /*for (int element : path) {
            System.out.println(element);
        }*/
        if (path == null) {
            System.out.println("输入越界");
        } else if (!path.isEmpty()) {
            int i = 0;
            for (i = 0; i < path.size() - 1; i++) {
                strpath = strpath.concat(getNodeById(nodes, path.get(i) + 1).name);
                strpath = strpath.concat("->");
            }
            strpath = strpath.concat(getNodeById(nodes, path.get(i) + 1).name);
        } else {
        }

        return strpath;
    }

    public static List<Integer> dijkstra(int i, int j) {
        if (i < 0 || i >= node_num || j < 0 || j >= node_num) {
            return null; // 节点索引超出范围
        }
        int[] dist = new int[node_num];
        boolean[] visited = new boolean[node_num];

        for (int k = 0; k < node_num; k++) {
            dist[k] = Integer.MAX_VALUE;
            visited[k] = false;
        }
        dist[i] = 0;

        for (int count = 0; count < node_num - 1; count++) {
            int u = -1;
            int minDist = Integer.MAX_VALUE;
            for (int v = 0; v < node_num; v++) {
                if (!visited[v] && dist[v] < minDist) {
                    u = v;
                    minDist = dist[v];
                }
            }

            if (u == -1) {
                break;
            }

            visited[u] = true;
            for (int v = 0; v < node_num; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        int index = j;
        while (index != i) {
            if (dist[index] == Integer.MAX_VALUE) {
                // 如果从起点到当前节点的距离是无穷大，说明没有路径
                return new ArrayList<>(); // 返回空路径
            }
            path.add(0, index);
            for (int k = 0; k < node_num; k++) {
                if (graph[k][index] != 0 && dist[index] == dist[k] + graph[k][index]) {
                    index = k;
                    break;
                }
            }
        }
        path.add(0, i);

        return path;
    }

    private static void generateNewText1() {

        String bridgetext;//读入的

        Scanner scanner = new Scanner(System.in, "UTF-8");
        System.out.print("请输入一个字符串：");
        bridgetext = scanner.nextLine();

        String createText1 = generateNewText(bridgetext);
//        int j;
        System.out.println("添加桥接词后的字符串为：");
//        for (j = 0; j < createText.size(); j++) {
//            System.out.print(createText.get(j) + " ");
//        }
        System.out.println(createText1);
    }
    public static String generateNewText(String bridgetext){
        StringBuilder buffer = new StringBuilder();
        String previous_word = "";
        List<String> createText = new ArrayList<String>();
        List<String> createWords = new ArrayList<String>();
        int now_Nodeid = 0, previous_Nodeid = 0;
        for (int i = 0; i < bridgetext.length(); i++) {
            char character = bridgetext.charAt(i);
            if (character >= 'a' && character <= 'z') {
                buffer.append(character);
            } else if (character >= 'A' && character <= 'Z') {
                buffer.append(Character.toLowerCase(character));
            } else if (character == ' ' || character == '\n' || character == '\r') {
                if (buffer.length() > 0) {
                    String nodeName = buffer.toString();
                    now_Nodeid = getNodeIdByName(nodes, nodeName);
                    if (now_Nodeid == 0) {//输入的词不在词表内
                        // System.out.println("词表中没找到"+nodeName);
                        createText.add(nodeName);
                    } else {
                        if (previous_Nodeid != 0) {//不是第一个词或前一个词在词表内
                            createWords = queryBridgeWords(previous_word, nodeName);
                            if (!createWords.isEmpty()) {
                                createText.add(createWords.get(0));
                            }
                            createText.add(nodeName);
                        } else {//是第一个词或前一个词不在词表内
                            createText.add(nodeName);
                        }
                    }

                    previous_Nodeid = now_Nodeid;
                    previous_word = nodeName;
                    buffer.setLength(0); // 清空缓冲区
                    //System.out.print("当前生成串长度为"+createText.size());
                }
            } else {
                // 其他字符，不做处理，继续读取下一个字符
            }
        }
        // 文件读完，如果缓冲区还有内容，处理最后一个单词
        if (buffer.length() > 0) {
            String nodeName = buffer.toString();
            now_Nodeid = getNodeIdByName(nodes, nodeName);
            if (now_Nodeid == 0) {//输入的词不在词表内
                // System.out.println("词表中没找到"+nodeName);
                createText.add(nodeName);
            } else {
                if (previous_Nodeid != 0) {//不是第一个词或前一个词在词表内
                    createWords = queryBridgeWords(previous_word, nodeName);
                    if (!createWords.isEmpty()) {
                        createText.add(createWords.get(0));
                    }
                    createText.add(nodeName);
                } else {//是第一个词或前一个词不在词表内
                    createText.add(nodeName);
                }
            }

//            previous_Nodeid = now_Nodeid;
//            previous_word = nodeName;
            buffer.setLength(0); // 清空缓冲区
        }
//        int j;
//        System.out.println("添加桥接词后的字符串为：");
//        for (j = 0; j < createText.size(); j++) {
//            System.out.print(createText.get(j) + " ");
//        }
        String createText1 = String.join(" ", createText);
        return createText1;
        //System.out.println(j);


    }

    private static void readBridgeWord() {
        String words1, words2;
        List<String> bridgeword;
        int i = 0;
        Scanner scanner = new Scanner(System.in, "UTF-8");
        System.out.print("请输入第一个字符串：");
        words1 = scanner.nextLine();
        System.out.print("请输入第二个字符串：");
        words2 = scanner.nextLine();

        System.out.println("第一个字符串是：" + words1);
        System.out.println("第二个字符串是：" + words2);

        if (!words1.isEmpty() && !words2.isEmpty()) {
            if (getNodeByName(nodes, words1) != null && getNodeByName(nodes, words2) != null) {
                bridgeword = queryBridgeWords(words1, words2);
                if (!bridgeword.isEmpty()) {
                    if (bridgeword.size() == 1) {
                        System.out.println("The bridge words from " +"\"" + words1 +"\"" + " to " +"\"" + words2 +"\"" + " is: " + bridgeword.get(0));
                    } else {
                        System.out.print("The bridge words from " +"\"" + words1 +"\"" + " to " +"\"" + words2 +"\"" + " are: ");
                        for (i = 0; i < bridgeword.size() - 1; i++) {
                            System.out.print(bridgeword.get(i) + ",");
                        }
                        System.out.println("and" + bridgeword.get(i) + ".");
                    }
                } else {
                    System.out.println("No bridge words from " +"\"" + words1 +"\"" + " to " +"\"" + words2 +"\"" );
                }
            } else if (getNodeByName(nodes, words1) == null && getNodeByName(nodes, words2) != null) {
                System.out.println("No " + "\"" +words1 +"\"" + " in the graph!");
            } else if (getNodeByName(nodes, words1) != null && getNodeByName(nodes, words2) == null) {
                System.out.println("No " +"\"" + words2 +"\"" + " in the graph!");
            } else if (getNodeByName(nodes, words1) == null && getNodeByName(nodes, words2) == null) {
                System.out.println("No " +"\"" + words1 +"\"" + " and " +"\"" + words2 +"\"" + " in the graph!");
            }
        } else {
            System.out.println("输入的两个字符串中有空串");
        }
    }

    private static List<Node> Read(String filePath) {
        StringBuilder buffer = new StringBuilder(); // 用于存储字符的缓冲区
        StringBuilder oldbuffer = new StringBuilder();
        nodes = new ArrayList<>(); // 用于存储Node对象的列表

        // 使用try-with-resources语句自动关闭资源
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            int charInt;
            // 逐个字符读取文件
            while ((charInt = bufferedReader.read()) != -1) {
                char character = (char) charInt;
                if (character >= 'a' && character <= 'z') {
                    buffer.append(character);
                } else if (character >= 'A' && character <= 'Z') {
                    buffer.append(Character.toLowerCase(character));
                } else if (character == ' ' || character == '\n' || character == '\r') {
                    if (buffer.length() > 0) {
                        String nodeName = buffer.toString();
                        Node existingNode = getNodeByName(nodes, nodeName);
                        if (existingNode != null) {
                            existingNode.use_num++; // 如果Node已存在，增加use_num
                        } else {
                            Node newNode = new Node(nodeName, ++node_num); // 创建新Node并分配id
                            nodes.add(newNode); // 将新Node添加到列表中
                        }
                        buffer.setLength(0); // 清空缓冲区
                    }
                } else {
                    // 其他字符，不做处理，继续读取下一个字符
                }
            }

            // 文件读完，如果缓冲区还有内容，处理最后一个单词
            if (buffer.length() > 0) {
                String nodeName = buffer.toString();
                Node existingNode = getNodeByName(nodes, nodeName);
                if (existingNode != null) {
                    existingNode.use_num++;
                } else {
                    Node newNode = new Node(nodeName, ++node_num);
                    nodes.add(newNode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodes; // 返回Node列表
    }

    private static Node getNodeByName(List<Node> nodes, String name) {
        for (Node node : nodes) {
            if (node.name.equals(name)) {
                return node;
            }
        }
        return null; // 如果没有找到，返回null
    }

    private static int getNodeIdByName(List<Node> nodes, String name) {
        for (Node node : nodes) {
            if (node.name.equals(name)) {
                return node.id;
            }
        }
        return 0; // 如果没有找到
    }

    private static Node getNodeById(List<Node> nodes, int id) {
        for (Node node : nodes) {
            if (node.id == id) {
                return node;
            }
        }
        return null; // 如果没有找到，返回null
    }

    public static int[][] createGraph(int node_num, String filePath, List<Node> nodes) {
        int[][] graph = new int[node_num][node_num];
        int buffer_id = 0, oldbuffer_id = 0;
        for (int i = 0; i < node_num; i++) {
            for (int j = 0; j < node_num; j++) {
                graph[i][j] = 0;
            }
        }
        StringBuilder buffer = new StringBuilder(); // 用于存储字符的缓冲区
        //StringBuilder oldbuffer = new StringBuilder();
        // 使用try-with-resources语句自动关闭资源
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            int charInt;
            // 逐个字符读取文件
            while ((charInt = bufferedReader.read()) != -1) {
                char character = (char) charInt;
                if (character >= 'a' && character <= 'z') {
                    buffer.append(character);
                } else if (character >= 'A' && character <= 'Z') {
                    buffer.append(Character.toLowerCase(character));
                } else if (character == ' ' || character == '\n' || character == '\r') {
                    if (buffer.length() > 0) {
                        String nodeName = buffer.toString();
                        buffer_id = getNodeIdByName(nodes, nodeName);
                        if (buffer_id == 0) {
                            System.out.print("未找到" + nodeName + "对应的节点");
                        }
                        if (oldbuffer_id > 0) {
                            graph[oldbuffer_id - 1][buffer_id - 1]++;
                        }
                        oldbuffer_id = buffer_id;
                        buffer.setLength(0); // 清空缓冲区
                    }
                } else {
                    // 其他字符，不做处理，继续读取下一个字符
                }
            }

            // 文件读完，如果缓冲区还有内容，处理最后一个单词
            if (buffer.length() > 0) {
                String nodeName = buffer.toString();
                buffer_id = getNodeIdByName(nodes, nodeName);
                if (buffer_id == 0) {
                    System.out.print("未找到" + nodeName + "对应的节点");
                }
                if (oldbuffer_id > 0) {
                    graph[oldbuffer_id - 1][buffer_id - 1]++;
                }
                buffer.setLength(0); // 清空缓冲区
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph; // 返回Node列表
    }

    static List<String> queryBridgeWords(String word1, String word2) {
        List<String> bridgeWords = new ArrayList<>();
        node_num=nodes.toArray().length;
        int id1 = getNodeIdByName(nodes, word1);
        int id2 = getNodeIdByName(nodes, word2);
        if (id1==0 || id2==0) {
            return null;
        }

        for (int i = 0; i < node_num; i++) {
            if (graph[id1 - 1][i] != 0) {
                if (graph[i][id2 - 1] != 0) {
                    bridgeWords.add(getNodeById(nodes, i + 1).name);
                }
            }
        }

        return bridgeWords;
    }

    private static void showDirectedGraph(int[][] graph, List<Node> nodes) {
        GraphViz gViz = new GraphViz("C:\\java\\program\\Lab1", "C:\\Program Files\\Graphviz\\bin\\dot.exe");
        gViz.start_graph();
        for (int i = 0; i < node_num; i++) {
            for (int j = 0; j < node_num; j++) {
                if (graph[i][j] != 0) {
                    Node node1 = getNodeById(nodes, i + 1);
                    Node node2 = getNodeById(nodes, j + 1);
                    gViz.addln(node1.name + "->" + node2.name + "[label=1];");
                }
            }
        }
        gViz.end_graph();

        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String[] splitString(String input) {
        // 将字符串按"->"分割
        String[] words = input.split("->");
        return words;
    }
    private static void drawline(int[][] graph, List<Node> nodes,String str) {
        String[] words = splitString(str);
        GraphViz gViz = new GraphViz("C:\\java\\program\\lab3", "C:\\Program Files\\Graphviz\\bin\\dot.exe");
        gViz.start_graph();
        for (int i = 0; i < node_num; i++) {
            for (int j = 0; j < node_num; j++) {
                if (graph[i][j] != 0) {
                    Node node1 = getNodeById(nodes, i + 1);
                    Node node2 = getNodeById(nodes, j + 1);
                    gViz.addln(node1.name + "->" + node2.name + "[label=1];");
                }
            }
        }
        for (int i = 0; i < Arrays.asList(words).size()-1; i++) {
            String temp = words[i] + "->" + words[i+1]+"[color=red];";
            gViz.addln(temp);
        }
        gViz.end_graph();
        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String randomWalk(int[][] graph, List<Node> nodes) {
        SecureRandom random = new SecureRandom();
        Set<Integer> visitedNodes = new HashSet<>();
        List<String> path = new ArrayList<>();

        // 随机选择起始节点
        int currentNodeIndex = random.nextInt(node_num);
        visitedNodes.add(currentNodeIndex);
        path.add(nodes.get(currentNodeIndex).name);

        while (true) {
            // 获取当前节点的邻居节点
            List<Integer> neighbors = new ArrayList<>();
            for (int i = 0; i < node_num; i++) {
                if (graph[currentNodeIndex][i] != 0) {
                    neighbors.add(i);
                }
            }
            // 如果没有邻居节点或者所有邻居节点已经被访问过，则结束遍历
            if (neighbors.isEmpty()) {
                break;
            }
            // 随机选择下一个节点
            int nextNodeIndex = neighbors.get(random.nextInt(neighbors.size()));
            // 记录路径
            path.add(nodes.get(nextNodeIndex).name);
            // 如果下一个节点已经被访问过，则结束遍历
            if (visitedNodes.contains(nextNodeIndex)) {
                break;
            }
            // 更新当前节点
            currentNodeIndex = nextNodeIndex;
            visitedNodes.add(currentNodeIndex);
        }
        // 将路径列表转换为字符串
        StringBuilder result = new StringBuilder();
        for (String nodeName : path) {
            result.append(nodeName).append("->");
        }
        // 去除最后一个箭头符号
        result.delete(result.length() - 2, result.length());
        drawline(graph,nodes,result.toString());
        return result.toString();
    }
}
