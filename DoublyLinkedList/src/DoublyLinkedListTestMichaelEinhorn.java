import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

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
public class DoublyLinkedListTestMichaelEinhorn {

    private static final int TIMEOUT = 2000;
    private static final int TEST_CASES = 10000;
    private DoublyLinkedList<String> list;
    private LinkedList<String> jlist;


    @Before
    public void setUp() {
        jlist = new LinkedList<String>();
        list = new DoublyLinkedList<String>();
    }
    @Test(timeout = TIMEOUT)
    public void testLastOc() {
        list.addAtIndex(0, "potato");
        assertEquals(list.removeLastOccurrence("potato"), "potato");
        assertNull(list.getHead());
        assertNull(list.getTail());
        assertEquals(list.size(), 0);
        for (int i = 0; i< 4; i++) {
            list.addToBack("stuff" + i);
        }
        assertEquals(list.removeLastOccurrence("stuff0"), "stuff0");
        assertEquals(list.removeLastOccurrence("stuff3"), "stuff3");
        assertArrayEquals(list.toArray(), new String[]{"stuff1", "stuff2"});
        assertEquals(2, list.size());
    }
    @Test(timeout = TIMEOUT)
    public void testRandom() {
        for (int i = 0; i < TEST_CASES; i++) {
            boolean except = false;
            int index = (int) (Math.random() * list.size());
            String str1 = "";
            String str2 = "";
            switch ((int) (11 * Math.random())) {
                case 0:
                    System.out.println("clear");
                    jlist.clear();
                    list.clear();
                    break;
                case 1:
                    System.out.println("isEmpty");
                    assertTrue(jlist.isEmpty() == list.isEmpty());
                    break;
                case 2:
                    System.out.println("removeFront");
                    str1 = "";
                    str2 = "";
                    try {str1 = list.removeFromFront();} catch (Exception e){  break;}
                    try {str2 = jlist.remove(0);} catch (Exception e){  break;}
                    assertEquals(str1,str2);
                    break;
                case 3:
                    System.out.println("removeBack");
                    str1 = "";
                    str2 = "";
                    try {str1 = list.removeFromBack();} catch (Exception e){  break;}
                    try {str2 = jlist.remove(jlist.size() - 1);} catch (Exception e){  break;}
                    assertFalse(except);
                    assertEquals(str1, str2);
                    break;
                case 4:
                    System.out.println("remove " + index);
                    str1 = "";
                    str2 = "";
                    try {str1 = list.removeAtIndex(index);} catch (Exception e){ break;}
                    try {str2 = jlist.remove(index);} catch (Exception e){ break;}
                    assertFalse(except);
                    assertEquals(str1, str2);
                    break;
                case 5:
                    System.out.println("addBack");
                    list.addToBack("" + i);
                    jlist.add("" + i);
                    break;
                case 6:
                    System.out.println("addFront");
                    list.addToFront("" + i);
                    jlist.add(0, "" + i);
                    break;
                case 7:
                    System.out.println("addIndex " + index);
                    list.addAtIndex(index, "" + i);
                    jlist.add(index, "" + i);
                    break;
                case 8:
                    String num = "" +  ((char) ((Math.random() * 4) + 'a')) ;
                    System.out.println("removeLastOc " + num);
                    try {list.removeLastOccurrence("" + num);} catch (Exception e){  break;}
                    try {jlist.removeLastOccurrence("" + num);} catch (Exception e){  break;}
                    break;
                case 9:
                    System.out.println("get");
                    str1 = "";
                    str2 = "";
                    try {str1 = list.get(index);} catch (Exception e){  break;}
                    try {str2 = jlist.get(index);} catch (Exception e){  break;}
                    assertEquals(str1, str2);
                    break;
                case 10:
                    System.out.println("addTen");
                    for (int j = 0; j < 10; j++) {
                        index = (int) (Math.random() * (1 + list.size()));
                        char ch = (char) ((Math.random() * 4) + 'a');
                        list.addAtIndex(index,"" + ch);
                        jlist.add(index,"" + ch);
                    }
                    break;
                    default:
                        break;


            }
            System.out.println(jlist);
            System.out.println(Arrays.asList(list.toArray()));
            assertArrayEquals(jlist.toArray(), list.toArray());
            assertEquals(list.size(), jlist.size());
        }
    }

}