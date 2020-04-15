import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Randomized testing against javas Priority Queue
 *
 * @author Michael Einhorn
 * @version 1.0
 *
 */
public class RandomTestMichaelEinhorn {

    private static final int TIMEOUT = 2000;
    private static final int TEST_CASES = 100000;
    PriorityQueue<String> jlist;
    MinHeap<String> list;
    Random rand;

    @Before
    public void setUp() {
        jlist = new PriorityQueue<>();
        list = new MinHeap<>();
        rand = new Random(42);
    }
    @Test(timeout = TIMEOUT)
    public void testRandom() {
        for (int i = 0; i < TEST_CASES; i++) {
            boolean except = false;
            String str1 = "";
            int num;
            int num3 = 0;
            switch ((int) (5 * rand.nextDouble())) {
                case 0:
                    //System.out.println("clear");
                    jlist.clear();
                    list.clear();
                    break;
                case 1:
                    //System.out.println("add");
                    num = (int)(rand.nextDouble() * 1000);
                    jlist.add("" + num + "." + i);
                    list.add("" + num + "." + i);
                    break;
                case 2:
                    if (jlist.size() > 1) {
                        //System.out.println("remove");
                        str1 = jlist.poll();
                        assertEquals(str1,list.remove());
                    }
                    break;
                case 3:
                    for (int q = i+10; i < q && i < TEST_CASES; i++) {
                        //System.out.println("add");
                        num = (int)(rand.nextDouble() * 1000);
                        jlist.add("" + num + "." + i);
                        list.add("" + num + "." + i);
                    }
                    break;
                case 4:
                    num = (int)(rand.nextDouble() * 100);
                    //System.out.println("build " + num);
                    ArrayList<String> ls = new ArrayList<>(num);
                    for(int z = num3; num3 < z + num; num3++) {
                        int num2 = (int)(rand.nextDouble() * 1000);
                        ls.add("" + num2 + ".B" + num3);
                    }
                    list = new MinHeap<>(ls);
                    jlist = new PriorityQueue<>(ls);
                    break;
                    default:
                        break;
            }
            int q = 0;
            //System.out.println(jlist);
            //System.out.println(Arrays.asList(list.getBackingArray()));
            for (String str : jlist) {
                assertEquals((String)(((Comparable[])list.getBackingArray())[++q]), str);
            }
            assertEquals(list.size(), jlist.size());
        }
    }
}