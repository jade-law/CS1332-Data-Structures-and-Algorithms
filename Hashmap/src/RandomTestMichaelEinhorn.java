import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeSet;
import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Randomized testing against other hash maps
 *
 * (2*26) ^ 2 = 2704
 * This is the number of strings with 2 characters of either upper or lower case
 * The website below shows that between these strings there are 1250 collisions
 * https://javachannel.org/posts/finding-hash-collisions-in-java-strings/
 *
 *
 * @author Michael Einhorn
 * @version 1.0
 *
 */
public class RandomTestMichaelEinhorn {
    private static final int TIMEOUT = 20000;
    int TEST_CASES;
    LinearProbingHashMap<String, Integer> map;
    Random rand;
    String outputFilePath = "output.txt";
    String testFilePath = "MichaelEinhornOutput.txt";
    File outputFile = null;
    File testFile = null;
    final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    StringBuilder str;
    int seed;

    //Generates output file of the same seed
    @Before
    public void setUp() {
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
            TEST_CASES = 1000;
        }
        map = new LinearProbingHashMap<>();
        rand = new Random(seed);
    }
    @Test(timeout = TIMEOUT)
    public void compare() {
        Scanner test;
        try {
            test = new Scanner(testFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IndexOutOfBoundsException("Change testFilePath to be the location and name of your test file");
        }
        str = new StringBuilder("" + seed);
        assertEquals("test and output files don't match", test.nextLine(), str.toString());
        str = new StringBuilder("" + TEST_CASES);
        assertEquals("test and output files don't match", test.nextLine(), str.toString());



        for (int i = 0; i < TEST_CASES; i++) {
            str = new StringBuilder("");
            String num;
            int random1 = (int) (5 * rand.nextDouble());
            switch (random1) {
                case 0:
                    int random2 = (int) (6 * rand.nextDouble());
                    switch (random2) {
                        case 0:
                            //adds all possibilities without resize
                            map = new LinearProbingHashMap<>((int)Math.pow(chars.length(), 2)*2+1);
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append(map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt())+ " ");
                                }
                            }
                            break;
                        case 1:
                            //adds all possibilities with resize
                            map.clear();
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append((map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt())+ " "));
                                }
                            }
                            break;
                        case 2:
                            //ghosts all possibilities
                            map = new LinearProbingHashMap<>((int)Math.pow(chars.length() * 2, 2)+1);
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append((map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt()) + " "));
                                    str.append((map.remove(("" + chars.charAt(j)) + chars.charAt(q)) + " "));
                                }
                            }
                        case 3:
                            //ghosts all possibilities with resize
                            map.clear();
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append((map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt()) + " "));
                                    str.append((map.remove(("" + chars.charAt(j)) + chars.charAt(q)) + " "));
                                }
                            }
                            break;
                        default:
                            map.clear();
                            break;
                    }
                    assertEquals("failed case " + random1 + "-" + random2 + " at i = " + i, test.nextLine(), str.toString());
                    break;
                case 1:
                    //System.out.println("add");
                    num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                    str.append((map.put("" + num, i)));
                    assertEquals("failed case " + random1, test.nextLine(), str.toString());
                    break;
                case 2:
                    for (int q = i+10; i < q && i < TEST_CASES; i++) {
                        //System.out.println("add");
                        num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                        str.append((map.put("" + num, i) + " "));
                    }
                    assertEquals("failed case " + random1 + " at i = " + i, test.nextLine(), str.toString());
                    break;
                case 3:
                    num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                    if (map.containsKey(num)) {
                        str.append(("true "));
                        str.append((map.get(num)));
                    } else {
                        str.append(("false"));
                    }
                    assertEquals("failed case " + random1 + " at i = " + i, test.nextLine(), str.toString());
                    break;
                case 4:
                    num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                    if (map.containsKey(num)) {
                        str.append(("true "));
                        str.append((map.remove(num)));
                    } else {
                        str.append(("false"));
                    }
                    assertEquals("failed case " + random1 + " at i = " + i, test.nextLine(), str.toString());
                    break;
                default:
                    break;
            }
            str = new StringBuilder("[");
            for (LinearProbingMapEntry<String,Integer> e : Arrays.asList(map.getTable())) {
                if (e == null) {
                    str.append("null, ");
                } else {
                    str.append(e.toString() + e.isRemoved() + ", ");
                }
            }
            str.append("]");
            assertEquals("failed case " + random1 + " at i = " + i, test.nextLine(), str.toString());
        }
    }
    @Test(timeout = TIMEOUT)
    public void write() {
        try {
            System.setOut(new PrintStream(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IndexOutOfBoundsException("Change OutputFilePath to be where you want the file to be written to");
        }
        str = new StringBuilder("" + seed);
        System.out.println(str);
        str = new StringBuilder("" + TEST_CASES);
        System.out.println(str);
        


        for (int i = 0; i < TEST_CASES; i++) {
            str = new StringBuilder("");
            String num;
            switch ((int) (5 * rand.nextDouble())) {
                case 0:
                    switch ((int) (6 * rand.nextDouble())) {
                        case 0:
                            //adds all possibilities without resize
                            map = new LinearProbingHashMap<>((int)Math.pow(chars.length(), 2)*2+1);
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append(map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt())+ " ");
                                }
                            }
                            break;
                        case 1:
                            //adds all possibilities with resize
                            map.clear();
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append((map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt())+ " "));
                                }
                            }
                            break;
                        case 2:
                            //ghosts all possibilities
                            map = new LinearProbingHashMap<>((int)Math.pow(chars.length() * 2, 2)+1);
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append((map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt()) + " "));
                                    str.append((map.remove(("" + chars.charAt(j)) + chars.charAt(q)) + " "));
                                }
                            }
                        case 3:
                            //ghosts all possibilities with resize
                            map.clear();
                            for (int j = 0; j < chars.length(); j++) {
                                for (int q = 0; q < chars.length(); q++) {
                                    str.append((map.put(("" + chars.charAt(j)) + chars.charAt(q), rand.nextInt()) + " "));
                                    str.append((map.remove(("" + chars.charAt(j)) + chars.charAt(q)) + " "));
                                }
                            }
                            break;
                            default:
                                map.clear();
                                break;
                    }
                    System.out.println(str);
                    break;
                case 1:
                    //System.out.println("add");
                    num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                    str.append((map.put("" + num, i)));
                    System.out.println(str);
                    break;
                case 2:
                    for (int q = i+10; i < q && i < TEST_CASES; i++) {
                        //System.out.println("add");
                        num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                        str.append((map.put("" + num, i) + " "));
                    }
                    System.out.println(str);
                    break;
                case 3:
                    num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                    if (map.containsKey(num)) {
                        str.append(("true "));
                        str.append((map.get(num)));
                    } else {
                        str.append(("false"));
                    }
                    System.out.println(str);
                    break;
                case 4:
                    num = ("" + chars.charAt((int)(rand.nextDouble() * chars.length()))) + chars.charAt((int)(rand.nextDouble() * chars.length()));
                    if (map.containsKey(num)) {
                        str.append(("true "));
                        str.append((map.remove(num)));
                    } else {
                        str.append(("false"));
                    }
                    System.out.println(str);
                    break;
                    default:
                        break;
            }
            str = new StringBuilder("[");
            for (LinearProbingMapEntry<String,Integer> e : Arrays.asList(map.getTable())) {
                if (e == null) {
                    str.append("null, ");
                } else {
                    str.append(e.toString() + e.isRemoved() + ", ");
                }
            }
            str.append("]");
            System.out.println(str);
        }
    }
}