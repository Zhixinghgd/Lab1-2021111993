
public class Node {
    String name;
    int id;
    int use_num;

    public Node(String name, int id) {
        this.name = name;
        this.id = id;
        this.use_num = 1; // 初始化use_num为1，因为每次创建Node时都是第一次出现
    }
}