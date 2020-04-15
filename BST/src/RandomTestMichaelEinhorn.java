import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
public class RandomTestMichaelEinhorn {

    private static final int TIMEOUT = 2000;
    private static final int TEST_CASES = 10000;
    TreeSet<String> jlist;
    BST<String> list;

    @Before
    public void setUp() {
        jlist = new TreeSet<>();
        list = new BST();
    }
    @Test(timeout = TIMEOUT)
    public void testRandom() {
        for (int i = 0; i < TEST_CASES; i++) {
            boolean except = false;
            String str1 = "";
            int index = (int)(Math.random() * jlist.size());
            switch ((int) (11 * Math.random())) {
                case 0:
                    System.out.println("clear");
                    jlist.clear();
                    list.clear();
                    break;
                case 1:
                    if (jlist.size() != 0) {
                        String obj = jlist.toArray(new String[]{})[index];
                        System.out.println("remove " + obj);
                        try {
                            except = !jlist.remove(obj);
                        } catch (Exception e) {
                            break;
                        }
                        try {
                            str1 = list.remove(obj);
                        } catch (Exception e) {
                            assertTrue(except);
                            break;
                        }
                        assertEquals(str1, obj);
                    }
                    break;
                case 2:
                    System.out.println("add");
                    int num = (int)(Math.random() * 1000);
                    list.add("" + num + i);
                    jlist.add("" + num + i);
                    break;
                case 3:
                    if (jlist.size() != 0) {
                        String obj2 = jlist.toArray(new String[]{})[index];
                        System.out.println("get " + obj2);
                        try {
                            str1 = list.get(obj2);
                        } catch (Exception e) {
                            assertTrue(false);
                        }
                        assertEquals(str1, obj2);
                    }
                    break;
                case 4:
                    if (jlist.size() != 0) {
                        String obj3 = jlist.toArray(new String[]{})[index];
                        boolean expexted = Math.random() > 0.5;
                        if (!expexted) {
                            obj3 += "potato";
                        }
                        System.out.println("contains " + obj3);
                        boolean b1 = list.contains(obj3);
                        assertEquals(b1, expexted);
                    }
                    break;
                case 5:
                    System.out.println("addTen");
                    int init = i;
                    for (i = i; i < init + 10; i++) {
                        int num2 = (int) (Math.random() * 1000);
                        list.add("" + num2 + i);
                        jlist.add("" + num2 + i);
                    }
                    break;
            }
            System.out.println(jlist);
            System.out.println((list.inorder()));
            assertArrayEquals(jlist.toArray(), list.inorder().toArray());
            assertEquals(list.size(), jlist.size());
        }
    }

}