import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This is a not-so-basic set of unit tests for MinHeap.
 * <p>
 * Passing these tests doesn't guarantee any grade on these assignments. These
 * student JUnits that we provide should be thought of as a sanity check to
 * help you get started on the homework and writing JUnits in general.
 * <p>
 * We highly encourage you to write your own set of JUnits for each homework
 * to cover edge cases you can think of for each data structure. Your code must
 * work correctly and efficiently in all cases, which is why it's important
 * to write comprehensive tests to cover as many cases as possible.
 * @author CS 1332 TAs + Peter
 * @version 1.0
 */
public class MinHeapPeterTest {

    private static final int TIMEOUT = 200;
    private MinHeap<Integer> minHeap;

    //Thanks to William Chen for showing me this
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("Backing array when test below failed: ");
            System.out.println(Arrays.deepToString(minHeap.getBackingArray()));
        }

        @Override
        protected void succeeded(Description description) {
        }
    };

    @Before
    public void setUp() {
        minHeap = new MinHeap<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, minHeap.size());
        assertArrayEquals(new Comparable[MinHeap.INITIAL_CAPACITY],
                minHeap.getBackingArray());
    }

    @Test(timeout = TIMEOUT)
    public void testBuildHeap() {
        /*
                 6
               /   \
              8     4
             / \
            2   1

            ->

                 1
               /   \
              2     4
             / \
            6   8
        */
        ArrayList<Integer> data = new ArrayList<>();
        data.add(6);
        data.add(8);
        data.add(4);
        data.add(2);
        data.add(1);
        minHeap = new MinHeap<>(data);

        assertEquals(5, minHeap.size());
        Integer[] expected = new Integer[11];
        expected[1] = 1;
        expected[2] = 2;
        expected[3] = 4;
        expected[4] = 6;
        expected[5] = 8;
        assertArrayEquals(expected, minHeap.getBackingArray());
    }

    @Test(timeout = TIMEOUT)
    public void testBuildSmallHeap() {
        //With an empty list
        ArrayList<Integer> data = new ArrayList<>();
        minHeap = new MinHeap<>(data);

        Integer[] expected = new Integer[]{null};
        assertEquals(0, minHeap.size());
        assertArrayEquals(expected, minHeap.getBackingArray());

        //With a size-1 list
        data = new ArrayList<>(Arrays.asList(1));
        minHeap = new MinHeap<>(data);

        expected = new Integer[]{null, 1, null};
        assertEquals(1, minHeap.size());
        assertArrayEquals(expected, minHeap.getBackingArray());

        //With a size-2 list
        data = new ArrayList<>(Arrays.asList(2, 1));
        minHeap = new MinHeap<>(data);

        expected = new Integer[]{null, 1, 2, null, null};
        assertEquals(2, minHeap.size());
        assertArrayEquals(expected, minHeap.getBackingArray());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testBuildNullHeap() {
        minHeap = new MinHeap<>(null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testBuildNullDataHeap() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(2, null));
        minHeap = new MinHeap<>(data);
    }

    @Test(timeout = TIMEOUT)
    public void testAdd() {
        /*
                 1
               /   \
              2     4
             / \
            6   8
        */
        minHeap.add(4);
        minHeap.add(2);
        minHeap.add(6);
        minHeap.add(1);
        minHeap.add(8);

        assertEquals(5, minHeap.size());
        Integer[] expected = new Integer[MinHeap.INITIAL_CAPACITY];
        expected[1] = 1;
        expected[2] = 2;
        expected[3] = 6;
        expected[4] = 4;
        expected[5] = 8;
        assertArrayEquals(expected, minHeap.getBackingArray());

        //Added tests
        ArrayList<Integer> data = new ArrayList<>();
        minHeap = new MinHeap<>(data);

        minHeap.add(5);  //expands array
        expected = new Integer[]{null, 5};
        assertArrayEquals(expected, minHeap.getBackingArray());

        minHeap.add(2);  //expands array
        minHeap.add(1);
        minHeap.add(0);  //expands array
        /* Current heap:
                 0
               /   \
              1     2
             /
            5
        */

        expected = new Integer[]{null, 0, 1, 2, 5, null, null, null};
        assertEquals(4, minHeap.size());
        assertArrayEquals(expected, minHeap.getBackingArray());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAddNull() {
        minHeap.add(null);
    }

    @Test(timeout = TIMEOUT)
    public void testRemove() {
        /*
                 1
               /   \
              2     4
             / \
            8   10
              ->
                 2
               /   \
              8     4
             /
            10
               ->
                 4
               /   \
              8    10

            ->

              8
             /
            10
        */
        //Build initial array:
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1, 2, 4, 8,
                10));
        int backingArrLength = data.size() * 2 + 1;
        minHeap = new MinHeap<>(data);

        assertEquals((Integer) 1, minHeap.remove());
        Integer[] expected = new Integer[]{null, 2, 8, 4, 10, null, null,
                null, null, null, null};
        assertArrayEquals(expected, minHeap.getBackingArray());

        assertEquals((Integer) 2, minHeap.remove());
        expected = new Integer[]{null, 4, 8, 10, null, null, null, null,
                null, null, null};
        assertArrayEquals(expected, minHeap.getBackingArray());

        assertEquals((Integer) 4, minHeap.remove());
        expected = new Integer[]{null, 8, 10, null, null, null, null, null,
                null, null, null};
        assertArrayEquals(expected, minHeap.getBackingArray());

        assertEquals(2, minHeap.size());

        //Removing till empty
        assertEquals((Integer) 8, minHeap.remove());
        assertEquals((Integer) 10, minHeap.remove());

        assertEquals(0, minHeap.size());
        expected = new Integer[backingArrLength];
        assertArrayEquals(expected, minHeap.getBackingArray());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveEmpty() {
        minHeap.remove();
    }

    @Test(timeout = TIMEOUT)
    public void testGetMin() {
        Integer temp = 1;
        Integer[] expected = new Integer[MinHeap.INITIAL_CAPACITY];

        /*
                 1
               /   \
              2     3
             / \
            4   5
         */
        minHeap.add(temp);
        expected[1] = temp;
        for (int i = 2; i < 6; i++) {
            minHeap.add(i);
            expected[i] = i;
        }

        assertSame(temp, minHeap.getMin());

        //Check for modifications to heap
        assertArrayEquals(expected, minHeap.getBackingArray());
        assertEquals(5, minHeap.size());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testGetEmpty() {
        minHeap.getMin();
    }

    @Test(timeout = TIMEOUT)
    public void testIsEmptyAndClear() {
        // Should be empty at initialization
        assertTrue(minHeap.isEmpty());

        // Should not be empty after adding elements
        /*
                 1
               /   \
              2     3
             / \
            4   5
         */
        for (int i = 1; i < 6; i++) {
            minHeap.add(i);
        }
        assertFalse(minHeap.isEmpty());

        // Clearing the list should empty the array and reset size
        minHeap.clear();
        assertTrue(minHeap.isEmpty());
        assertEquals(0, minHeap.size());
        assertArrayEquals(new Comparable[MinHeap.INITIAL_CAPACITY],
                minHeap.getBackingArray());
    }
}