import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.lang.Math.abs;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This is a not-so-basic set of unit tests for LinearProbingHashMap.
 * <p>
 * Passing these tests doesn't guarantee any grade on these assignments. These
 * student JUnits that we provide should be thought of as a sanity check to
 * help you get started on the homework and writing JUnits in general.
 * <p>
 * We highly encourage you to write your own set of JUnits for each homework
 * to cover edge cases you can think of for each data structure. Your code must
 * work correctly and efficiently in all cases, which is why it's important
 * to write comprehensive tests to cover as many cases as possible.
 * @author CS 1332 TAs + Peter Wilson
 * @version 1.0
 */
public class LinearProbingHashMapPeterTest {

    private static final int TIMEOUT = 200;
    private static final int INITIAL_CAPACITY =
            LinearProbingHashMap.INITIAL_CAPACITY;
    private static final int A = 65;

    private LinearProbingHashMap<Integer, String> map;
    private LinearProbingMapEntry[] expected;
    //Thanks to William Chen for showing me this
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("Backing array when test below failed: ");
            printArr(map.getTable());

            if (causedByArrayEquals(e.getStackTrace())) {
                System.out.println("Expected array:");
                printArr(expected);
            }
        }

        @Override
        protected void succeeded(Description description) {
        }
    };

    @Before
    public void setUp() {
        map = new LinearProbingHashMap<>();
        expected =
                new LinearProbingMapEntry[INITIAL_CAPACITY];

    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, map.size());
        assertArrayEquals(new LinearProbingMapEntry[INITIAL_CAPACITY],
                map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testBasicPut() {
        //Basic functionality
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        for (int i = 0; i < 5; i++) {
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(i, letter));
            expected[i] = new LinearProbingMapEntry<>(i, letter);
        }
        assertEquals(5, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testWrappingPut() {
        //Wrap around end of array
        for (int i = 0; i < 5; i++) {
            int key = 2 * INITIAL_CAPACITY - 3 + i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = key % expected.length;
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }
        assertEquals(5, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testNegativeAndDuplicatesPut() {
        //Negative keyed elements
        // [(0, A), (-1, B), (-2, C), (-3, D), (-4, E), _, _, _, _, _, _, _, _]
        for (int i = 0; i < 5; i++) {
            int key = -i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }
        assertEquals(5, map.size());
        assertArrayEquals(expected, map.getTable());

        //Now add duplicates
        // [(0, A), (-1, B), (-2, New), (-3, D), (-4, E), _, _, _, _, _, _,
        // _, _]
        assertEquals("C", map.put(-2, "New"));
        expected[2].setValue("New");

        assertEquals(5, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testExpandingAndDeletingPut() {
        //Create map of size 0
        //After adding 8 elements, should be size 15
        map = new LinearProbingHashMap<>(0);
        expected = new LinearProbingMapEntry[15];

        for (int i = 0; i < 8; i++) {
            int key = i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }
        assertEquals(8, map.size());
        assertArrayEquals(expected, map.getTable());

        //Now delete everything:
        // [DEL, DEL, DEL, DEL, DEL, DEL, DEL, DEL, _,  _,  _,  _,  _,  _,  _]
        //YOUR REMOVE NEEDS TO WORK FOR THIS
        for (int i = 0; i < 8; i++) {
            String letter = Character.toString((char) (i + A));
            assertEquals(letter, map.remove(i));
            expected[i].setRemoved(true);
        }

        //Now add more (this should NOT expand the array):
        //[DEL, DEL, DEL, DEL, DEL, DEL, DEL, DEL, (8, I), (9, J), (10, K),
        // (11, L), (12, M), (13, N), (14, O)]
        for (int i = 8; i < 15; i++) {
            int key = i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }

        assertEquals(7, map.size());
        assertArrayEquals(expected, map.getTable());

        //Now add at index 8. The probe should check the WHOLE array (since
        // there are no nulls), but should remember the first DEL was at index 0
        //[(23, P), DEL, DEL, DEL, DEL, DEL, DEL, DEL, (8, I), (9, J), (10,
        // K), (11, L), (12, M), (13, N), (14, O)]
        assertNull(map.put(8 + expected.length, "P"));
        expected[0] = new LinearProbingMapEntry<>(8 + expected.length, "P");

        assertEquals(8, map.size());
        assertArrayEquals(expected, map.getTable());


        // Now expand the array. ALL the elements should be rehashed and the
        // DELs removed.

        //[(0, A), (1, B), (2, C),_,_,_,_,_, (8, I), (9, J), (10, K), (11, L),
        // (12, M), (13, N), (14, O),_,_,_,_,_,_,_,_,(23, P),_,_,_,_,_,_,_]
        expected = new LinearProbingMapEntry[31];

        for (int i = 0; i < 3; i++) {
            int key = i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            //Add items 0...2 to expected
            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }

        //Add 8...14 to expected since they were deleted
        for (int i = 8; i < 15; i++) {
            String letter = Character.toString((char) (i + A));
            expected[i] = new LinearProbingMapEntry<>(i, letter);
        }

        //Add back 23 to expected:
        expected[23] = new LinearProbingMapEntry<>(23, "P");

        assertEquals(11, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testDuplicatePut() {
        //Negative keyed elements
        // [(0, A), (-1, B), (-2, C), (-3, D), (-4, E), _, _, _, _, _, _, _, _]
        for (int i = 0; i < 5; i++) {
            int key = -i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }
        assertEquals(5, map.size());
        assertArrayEquals(expected, map.getTable());

        //Now put a duplicate
        // [(0, A), (-1, B), (-2, New), (-3, D), (-4, E), (-15, New), _, _, _,
        // _, _, _, _]
        assertNull(map.put(-2 - INITIAL_CAPACITY, "New"));
        expected[5] = new LinearProbingMapEntry<>(-2 - INITIAL_CAPACITY, "New");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testPutDuplicateWithDels() {
        //When we encouter a duplicate key, must add the new value at that
        // index EVEN IF we previously encountered a DEL

        // [_, DEL: (1, A), (14, C), _, _, _, _, _, _, _, _, _, _]
        assertNull(map.put(1, "A"));
        assertNull(map.put(1 + INITIAL_CAPACITY, "B"));
        assertEquals("A", map.remove(1));
        assertEquals("B", map.put(1 + INITIAL_CAPACITY, "C"));

        expected[1] = new LinearProbingMapEntry<>(1, "A");
        expected[1].setRemoved(true);
        expected[2] = new LinearProbingMapEntry<>(14, "C");

        assertEquals(1, map.size());
        assertArrayEquals(expected, map.getTable());

        //Now make sure we ignore duplicate keys in DELs
        // [_, (14, D), DEL: (14, C), _, _, _, _, _, _, _, _, _, _]
        assertEquals("C", map.remove(1 + INITIAL_CAPACITY));
        //Don't return prior value if deleted
        assertNull(map.put(1 + INITIAL_CAPACITY, "D"));

        expected[2].setRemoved(true);
        expected[1] = new LinearProbingMapEntry<>(1 + INITIAL_CAPACITY, "D");

        assertEquals(1, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testRemove() {
        String temp = "D";

        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, temp));
        assertNull(map.put(4, "E"));

        // [(0, A), (1, B), (2, C), DEL: (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertSame(temp, map.remove(3));
        assertEquals(4, map.size());
        LinearProbingMapEntry[] expected =
                new LinearProbingMapEntry[INITIAL_CAPACITY];
        expected[0] = new LinearProbingMapEntry<>(0, "A");
        expected[1] = new LinearProbingMapEntry<>(1, "B");
        expected[2] = new LinearProbingMapEntry<>(2, "C");
        expected[3] = new LinearProbingMapEntry<>(3, "D");
        expected[3].setRemoved(true);
        expected[4] = new LinearProbingMapEntry<>(4, "E");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testNegativeRemove() {
        //Negative keyed elements
        // [(0, A), (-1, B), (-2, C), (-3, D), (-4, E), _, _, _, _, _, _, _, _]
        for (int i = 0; i < 5; i++) {
            int key = -i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }
        assertEquals(5, map.size());
        assertArrayEquals(expected, map.getTable());

        assertEquals("B", map.remove(-1));
        assertEquals("C", map.remove(-2));

        expected[1].setRemoved(true);
        expected[2].setRemoved(true);

        assertEquals(3, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testWrappingRemove() {
        map.put(INITIAL_CAPACITY - 1, "A");  //At end of array

        //At beginning of array, since probe wraps around
        map.put(2 * INITIAL_CAPACITY - 1, "A");

        map.remove(INITIAL_CAPACITY - 1);
        map.remove(2 * INITIAL_CAPACITY - 1);

        expected[INITIAL_CAPACITY - 1] =
                new LinearProbingMapEntry<>(INITIAL_CAPACITY - 1, "A");
        expected[INITIAL_CAPACITY - 1].setRemoved(true);

        expected[0] =
                new LinearProbingMapEntry<>(2 * INITIAL_CAPACITY - 1, "A");
        expected[0].setRemoved(true);

        assertEquals(0, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testGet() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));

        assertEquals("A", map.get(0));
        assertEquals("B", map.get(1));
        assertEquals("C", map.get(2));
        assertEquals("D", map.get(3));
        assertEquals("E", map.get(4));
    }

    @Test(timeout = TIMEOUT)
    public void testWrappingGet() {
        assertNull(map.put(INITIAL_CAPACITY - 1, "A"));  //At end of array

        //At beginning of array, since probe wraps around
        assertNull(map.put(2 * INITIAL_CAPACITY - 1, "B"));

        assertEquals("A", map.get(INITIAL_CAPACITY - 1));
        assertEquals("B", map.get(2 * INITIAL_CAPACITY - 1));
    }

    @Test(timeout = TIMEOUT)
    public void testContainsKey() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));

        assertTrue(map.containsKey(3));
        assertFalse(map.containsKey(5));

        //Something at this index, but NOT the desired key
        assertFalse(map.containsKey(1 + INITIAL_CAPACITY));

        map.remove(3);
        assertFalse(map.containsKey(3));
    }

    @Test(timeout = TIMEOUT)
    public void testKeySet() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));

        Set<Integer> expected = new HashSet<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);
        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void testKeySetRemovedItems() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));

        map.remove(2);

        Set<Integer> expected = new HashSet<>();
        expected.add(0);
        expected.add(1);
        expected.add(3);
        expected.add(4);
        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void testKeySetNoItems() {
        Set<Integer> expected = new HashSet<>();
        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void testValues() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));

        List<String> expected = new LinkedList<>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        expected.add("D");
        expected.add("E");
        assertEquals(expected, map.values());

        //Make sure it works with remove too
        assertEquals("A", map.remove(0));
        expected.remove("A");

        assertEquals(expected, map.values());
    }

    @Test(timeout = TIMEOUT)
    public void testValuesNoItems() {
        List<String> expected = new LinkedList<>();
        assertEquals(expected, map.values());
    }

    @Test(timeout = TIMEOUT)
    public void testResize() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));

        // [(0, A), (1, B), (2, C), (3, D), (4, E)]
        map.resizeBackingTable(5);
        assertEquals(5, map.size());
        LinearProbingMapEntry[] expected = new LinearProbingMapEntry[5];
        expected[0] = new LinearProbingMapEntry<>(0, "A");
        expected[1] = new LinearProbingMapEntry<>(1, "B");
        expected[2] = new LinearProbingMapEntry<>(2, "C");
        expected[3] = new LinearProbingMapEntry<>(3, "D");
        expected[4] = new LinearProbingMapEntry<>(4, "E");
        assertArrayEquals(expected, map.getTable());
    }

    //Checks if you accidentally resize when load factor >= length, not only
    // when load factor > length
    @Test(timeout = TIMEOUT)
    public void testResizeCapacity() {
        map = new LinearProbingHashMap<>(100);

        for (int i = 0; i < 67; i++) {
            int key = i + 13;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));
        }

        assertEquals(100, map.getTable().length);

        map.resizeBackingTable(67);
        assertEquals(67, map.getTable().length);
    }

    @Test(timeout = TIMEOUT)
    public void testResizeOnDuplicate() {
        expected = new LinearProbingMapEntry[2 * INITIAL_CAPACITY + 1];
        for (int i = 0; i < 8; i++) {
            int key = i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }

        //Hasn't resized yet
        assertEquals(INITIAL_CAPACITY, map.getTable().length);

        //Even though key 0 is already in the map, this should resize the
        // backing array
        assertEquals("A", map.put(0, "New"));

        expected[0].setValue("New");

        assertEquals(8, map.size());
        assertArrayEquals(expected, map.getTable());
    }

    //You should NOT remove duplicates when resizing
    @Test(timeout = TIMEOUT)
    public void testResizeDuplicateKeys() {
        expected = new LinearProbingMapEntry[2 * INITIAL_CAPACITY + 1];
        for (int i = 0; i < 8; i++) {
            int key = i;
            String letter = Character.toString((char) (i + A));
            assertNull(map.put(key, letter));

            int actualIndex = abs(key % expected.length);
            expected[actualIndex] = new LinearProbingMapEntry<>(key, letter);
        }

        //Create a duplicate key in the table at position 1
        map.getTable()[1].setKey(0);
        //Assume that after resize the duplicate with be placed directly
        // after the original 0 key
        expected[1].setKey(0);

        map.resizeBackingTable(2 * INITIAL_CAPACITY + 1);

        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testResizeDeleted() {
        map.put(5, "D");
        map.remove(5);
        map.resizeBackingTable(0);
    }

    @Test(timeout = TIMEOUT)
    public void testClear() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));

        map.clear();
        assertEquals(0, map.size());
        assertArrayEquals(new LinearProbingMapEntry[LinearProbingHashMap
                .INITIAL_CAPACITY], map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testStringKeys() {
        LinearProbingHashMap<String, String> strMap =
                new LinearProbingHashMap<>();

        assertNull(strMap.put("A", "c"));
        assertNull(strMap.put("B", "b"));
        assertEquals("c", strMap.put("A", "a"));

        assertEquals("b", strMap.remove("B"));
        assertEquals("a", strMap.remove("A"));
    }

    //Test the exceptions

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullKeyPut() {
        map.put(null, "D");
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullValuePut() {
        map.put(1, null);
    }


    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testDeletedRemove() {
        map.put(5, "A");
        map.remove(5);
        map.remove(5);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testNotPresetRemove() {
        map.remove(5);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullRemove() {
        map.remove(null);
    }


    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testDeletedGet() {
        map.put(5, "A");
        map.remove(5);
        map.get(5);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testNotPresentGet() {
        map.get(5);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullGet() {
        map.get(null);
    }


    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullContains() {
        map.containsKey(null);
    }


    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testTinyResize() {
        map.put(5, "D");
        map.resizeBackingTable(0);
    }


    /**
     * Checks if the exception was thrown by assertArrayEquals based on the
     * stack trace
     * @param traceArr the trace
     * @return true if it was
     */
    private boolean causedByArrayEquals(StackTraceElement[] traceArr) {
        String arrayEqualsName = "assertArrayEquals";

        for (StackTraceElement element : traceArr) {
            if (arrayEqualsName.equals(element.getMethodName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints array. Indicates if an element is deleted.
     * @param toPrint array to print
     */
    private void printArr(LinearProbingMapEntry[] toPrint) {
        String printString = "[";
        for (LinearProbingMapEntry elem : toPrint) {
            if (elem == null) {
                printString += "null";
            } else {
                if (elem.isRemoved()) {
                    printString += "DEL: ";
                }
                printString += elem.toString();
            }
            printString += ", ";
        }
        //remove trailing ', ' :
        printString = printString.substring(0, printString.length() - 2);
        printString += "]";

        System.out.println(printString);

    }
}
