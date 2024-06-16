package org.yangtongshen;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class white_test {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        List<Node> nodes1 = Arrays.asList(new Node("a",1), new Node("b",2), new Node("c",3),new Node("d",4 ));
        Run.setNodes(nodes1);
        int[][] graph = new int[][] {
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
                {0, 0, 0, 0}
        };
        Run.setGraph(graph);//a -> b, b -> c, c -> d
    }

    @Test
    public void testWord1NotInNodes() {
        assertNull(Run.queryBridgeWords("e", "a"));
    }

    @Test
    public void testWord2NotInNodes() {
        assertNull(Run.queryBridgeWords("a", "e"));
    }

    @Test
    public void testNoBridgeWords() {
        assertTrue(Run.queryBridgeWords("a", "d").isEmpty());
    }

    @Test
    public void testOneBridgeWord() {
        List<String> result = Run.queryBridgeWords("a", "c");
        assertEquals(1, result.size());
        assertEquals("b", result.get(0));
    }

}
