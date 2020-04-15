import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Randomized testing against javas LinkedList
 *
 * @author Michael Einhorn
 * @version 1.0
 *
 */
@SuppressWarnings("ALL")
public class RandomTestMichaelEinhorn {

    private static final int TIMEOUT = 2000;
    int TEST_CASES = 10000;
    AVL<String> list;
    List<String> jlist;
    static boolean write = false;
    Random rand;
    String outputFilePath = "output.txt";
    String testFilePath = "MichaelEinhornOutput.txt";
    File outputFile = null;
    File testFile = null;
    StringBuilder str;
    int seed;


    @Before
    public void setUp() {
        list = new AVL();
        jlist = new ArrayList<>();

        str = new StringBuilder("");
        outputFile = new File(outputFilePath);
        testFile = new File(testFilePath);
        try {
            Scanner test = new Scanner(testFile);
            seed = test.nextInt();
            TEST_CASES = test.nextInt();
            test.close();
        } catch (Exception e) {
            seed = 42;
            TEST_CASES = 10000;
        }
        rand = new Random(seed);
    }
    @SuppressWarnings({"CheckStyle"})
    @Test(timeout = TIMEOUT)
    public void testRandom() {
        Scanner test;
        if (write) {
            try {
                System.setOut(new PrintStream(outputFile));
            } catch (Exception e) {
                e.printStackTrace();
                throw new IndexOutOfBoundsException("Change OutputFilePath to be where you want the file to be written to");
            }
        }
        try {
            test = new Scanner(testFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IndexOutOfBoundsException("Change testFilePath to be the location and name of your test file");
        }
        str = new StringBuilder("" + seed);
        if (write) { System.out.println(str); } else { assertEquals("test and output files don't match", test.nextLine(), str.toString());}
        str = new StringBuilder("" + TEST_CASES);
        if (write) { System.out.println(str); } else { assertEquals("test and output files don't match", test.nextLine(), str.toString());}

        for (int i = 0; i < TEST_CASES; i++) {
            String str1 = "";
            int index = (int)(rand.nextDouble() * jlist.size());
            switch ((int) (7 * rand.nextDouble())) {
                case 0:
                    list.clear();
                    jlist.clear();
                    assertEquals(list.size(), jlist.size());
                    break;
                case 1:
                    if (jlist.size() != 0) {
                        String obj = jlist.remove(index);
                        str1 = list.remove(obj);
                        assertEquals(str1, obj);
                        assertEquals(list.size(), jlist.size());
                    }
                    break;
                case 2:
                    int num = (int)(rand.nextDouble() * 1000);
                    list.add("" + num + i);
                    jlist.add("" + num + i);
                    break;
                case 3:
                    if (jlist.size() != 0) {
                        String obj2 = jlist.get(index);
                        str1 = list.get(obj2);
                        assertEquals(str1, obj2);
                    }
                    break;
                case 4:
                    if (jlist.size() != 0) {
                        String obj3 = jlist.get(index);
                        boolean expexted = rand.nextDouble() > 0.5;
                        if (!expexted) {
                            obj3 += "potato";
                        }
                        boolean b1 = list.contains(obj3);
                        assertEquals(b1, expexted);
                    }
                    break;
                case 5:
                    int init = i;
                    for (i = i; i < init + 20 && i < TEST_CASES; i++) {
                        int num2 = (int) (rand.nextDouble() * 1000);
                        list.add("" + num2 + i);
                        jlist.add("" + num2 + i);
                    }
                    break;
                case 6:
                    List<String> temp = list.kSmallest((int)rand.nextDouble() * list.size());
                    List<String> temp2 = new ArrayList<>(temp);
                    Collections.sort(temp2);
                    assertEquals(temp.toString(),temp2.toString());
                    String temp3 = null;
                    int q = 0;
                    for (String str : temp) {
                        assertEquals(temp3, list.predecessor(str));
                        temp3 = str;
                        q++;
                    }
            }
            checkTree(list,test);
            assertEquals(list.size(), jlist.size());
        }
    }
    static int z = 0;
    public static <T extends Comparable<? super T>> void checkTree(AVL<T> tree, Scanner scan) {
        String[] ex = null;
        z = 0;
        if (!write) {
            ex = scan.nextLine().split(", ");
        }
        checkTreeHelper(tree.getRoot(), ex);
        if (write) {
            System.out.println();
        }
    }
    private static <T extends Comparable<? super T>> void checkTreeHelper(AVLNode<T> curr, String[] ex) {
        if (curr != null) {
            NodeWrap temp = new NodeWrap<T>(curr.getData(),curr.getHeight(),curr.getBalanceFactor());
            if (write) {
                System.out.print(temp.toString() + ", ");
            } else {
                assertEquals(temp.toString(), ex[z++]);
            }
            checkTreeHelper(curr.getLeft(), ex);
            checkTreeHelper(curr.getRight(), ex);
        }
    }
    private static class NodeWrap<T extends Comparable<? super T>> {
        T data;
        int height;
        int balance;
        public NodeWrap (T d, int h, int b) {
            data = d;
            height = h;
            balance = b;
        }
        public String toString() {
            return data.toString() + " h:" + height + " b:" + balance;
        }
    }
}