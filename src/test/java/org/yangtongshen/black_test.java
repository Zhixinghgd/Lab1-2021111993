package org.yangtongshen;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class black_test {


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        List<Node> nodes1=Arrays.asList(new Node("to",1), new Node("explore",2), new Node("strange",3),new Node("new",4),new Node("worlds",5),new Node("seek",6),new Node("out",7),new Node("life",8),new Node("and",9),new Node("civilizations",10));
        Run.setNodes(nodes1);
        int[][] graph = {
                {0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0}
        };
        Run.setGraph(graph);
    }
    @Test
    public void testSingleWord() {
        String originText = "to";
        String expected = "to";
        assertEquals(expected, Run.generateNewText(originText));
    }

    @Test
    public void testEmptyString() {
        String originText = "";
        String expected = "";
        assertEquals(expected, Run.generateNewText(originText));
    }

    @Test
    public void testValidInputWithMultipleBridgeWords() {
        String originText = "to out new and";
        String expected = "to seek out new life and";
        assertEquals(expected, Run.generateNewText(originText));
    }

    @Test
    public void testMixedCaseInput() {
        String originText = "to explore new LIFE";
        String expected = "to explore strange new life";
        assertEquals(expected, Run.generateNewText(originText));
    }

    @Test
    public void testUpperCaseInput() {
        String originText = "TO SEEK NEW LIFE";
        String expected = "to seek out new life";
        assertEquals(expected, Run.generateNewText(originText));
    }

    @Test
    public void testInputWithSpecialCharacters() {
        String originText = "life n##**(e)*(&w a89/n4896d";
        String expected = "life and new life and";
        assertEquals(expected, Run.generateNewText(originText));
    }

}
