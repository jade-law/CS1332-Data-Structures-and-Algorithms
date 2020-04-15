import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class DequeJasonTest {
    private static final int TIMEOUT = 200;
    private ArrayDeque<String> array;
    private LinkedDeque<String> linked;

    @Before
    public void setup() {
        array = new ArrayDeque<>();
        linked = new LinkedDeque<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, array.size());
        assertArrayEquals(new Object[ArrayDeque.INITIAL_CAPACITY],
                array.getBackingArray());
        assertEquals(0, linked.size());
        assertNull(linked.getHead());
        assertNull(linked.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeFullListAddLast() {
        array.addLast("0a"); // 0a, _, _, _, _, _, _, _, _, _, _
        array.addLast("1a"); // 0a, 1a, _, _, _, _, _, _, _, _, _
        array.addLast("2a"); // 0a, 1a, 2a,  _, _, _, _, _, _, _, _
        array.addLast("3a"); // 0a, 1a, 2a, 3a, _, _, _, _, _, _, _
        array.addLast("4a"); // 0a, 1a, 2a, 3a, 4a, _, _, _, _, _, _
        array.addLast("5a"); // 0a, 1a, 2a, 3a, 4a, 5a, _, _, _, _, _
        array.addLast("6a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, _, _, _, _
        array.addLast("7a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, _, _, _
        array.addLast("8a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, _, _
        array.addLast("9a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, _
        array.addLast("10a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, 10a

        assertEquals(11, array.size());
        String[] expected = new String[ArrayDeque.INITIAL_CAPACITY];
        expected[0] = "0a";
        expected[1] = "1a";
        expected[2] = "2a";
        expected[3] = "3a";
        expected[4] = "4a";
        expected[5] = "5a";
        expected[6] = "6a";
        expected[7] = "7a";
        expected[8] = "8a";
        expected[9] = "9a";
        expected[10] = "10a";
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("10a", array.getLast());

        // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, 10a, 11a, _, _, _, _, _, _, _, _, _
        array.addLast("11a");

        assertEquals(12, array.size());
        String[] expected1 = new String[ArrayDeque.INITIAL_CAPACITY * 2];
        expected1[0] = "0a";
        expected1[1] = "1a";
        expected1[2] = "2a";
        expected1[3] = "3a";
        expected1[4] = "4a";
        expected1[5] = "5a";
        expected1[6] = "6a";
        expected1[7] = "7a";
        expected1[8] = "8a";
        expected1[9] = "9a";
        expected1[10] = "10a";
        expected1[11] = "11a";
        expected1[12] = null;
        expected1[13] = null;
        expected1[14] = null;
        expected1[15] = null;
        expected1[16] = null;
        expected1[17] = null;
        expected1[18] = null;
        expected1[19] = null;
        expected1[20] = null;
        expected1[21] = null;
        assertArrayEquals(expected1, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("11a", array.getLast());

        // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, 10a, _, _, _, _, _, _, _, _, _
        assertEquals("11a", array.removeLast());
        expected1[11] = null;

        // _, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, 10a, _, _, _, _, _, _, _, _, _
        assertEquals("0a", array.removeFirst());
        assertEquals(10, array.size());
        expected1[0] = null;
        expected1[1] = "1a";
        expected1[2] = "2a";
        expected1[3] = "3a";
        expected1[4] = "4a";
        expected1[5] = "5a";
        expected1[6] = "6a";
        expected1[7] = "7a";
        expected1[8] = "8a";
        expected1[9] = "9a";
        expected1[10] = "10a";
        expected1[11] = null;
        assertArrayEquals(expected1, array.getBackingArray());
        assertEquals("1a", array.getFirst());
        assertEquals("10a", array.getLast());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeFullListAddFirst() {
        array.addLast("0a"); // 0a, _, _, _, _, _, _, _, _, _, _
        array.addLast("1a"); // 0a, 1a, _, _, _, _, _, _, _, _, _
        array.addLast("2a"); // 0a, 1a, 2a,  _, _, _, _, _, _, _, _
        array.addLast("3a"); // 0a, 1a, 2a, 3a, _, _, _, _, _, _, _
        array.addLast("4a"); // 0a, 1a, 2a, 3a, 4a, _, _, _, _, _, _
        array.addLast("5a"); // 0a, 1a, 2a, 3a, 4a, 5a, _, _, _, _, _
        array.addLast("6a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, _, _, _, _
        array.addLast("7a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, _, _, _
        array.addLast("8a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, _, _
        array.addLast("9a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, _
        array.addLast("10a"); // 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, 10a

        // 20a, 0a, 1a, 2a, 3a, 4a, 5a, 6a, 7a, 8a, 9a, 10a, _, _, _, _, _, _, _, _, _, _
        array.addFirst("20a");
        assertEquals(12, array.size());
        assertEquals("20a", array.getFirst());
        assertEquals("10a", array.getLast());
        String[] expected = new String[ArrayDeque.INITIAL_CAPACITY * 2];
        expected[0] = "20a";
        expected[1] = "0a";
        expected[2] = "1a";
        expected[3] = "2a";
        expected[4] = "3a";
        expected[5] = "4a";
        expected[6] = "5a";
        expected[7] = "6a";
        expected[8] = "7a";
        expected[9] = "8a";
        expected[10] = "9a";
        expected[11] = "10a";
        expected[12] = null;
        expected[13] = null;
        expected[14] = null;
        expected[15] = null;
        expected[16] = null;
        expected[17] = null;
        expected[18] = null;
        expected[19] = null;
        expected[20] = null;
        expected[21] = null;
        assertArrayEquals(expected, array.getBackingArray());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeWrapAround() {
        array.addLast("5a"); // 5a, _, _, _, _, _, _, _, _, _, _
        array.addLast("6a"); // 5a, 6a, _, _, _, _, _, _, _, _, _
        array.addLast("7a"); // 5a, 6a, 7a, _, _, _, _, _, _, _, _
        array.addLast("8a"); // 5a, 6a, 7a, 8a, _, _, _, _, _, _, _
        array.addLast("9a"); // 5a, 6a, 7a, 8a, 9a, _, _, _, _, _, _

        array.addFirst("4a"); // 5a, 6a, 7a, 8a, 9a, _, _, _, _, _, 4a
        array.addFirst("3a"); // 5a, 6a, 7a, 8a, 9a, _, _, _, _, 3a, 4a
        array.addFirst("2a"); // 5a, 6a, 7a, 8a, 9a, _, _, _, 2a, 3a, 4a
        array.addFirst("1a"); // 5a, 6a, 7a, 8a, 9a, _, _, 1a, 2a, 3a, 4a
        array.addFirst("0a"); // 5a, 6a, 7a, 8a, 9a, _, 0a, 1a, 2a, 3a, 4a

        assertEquals(10, array.size());
        String[] expected = new String[ArrayDeque.INITIAL_CAPACITY];
        expected[0] = "5a";
        expected[1] = "6a";
        expected[2] = "7a";
        expected[3] = "8a";
        expected[4] = "9a";
        expected[5] = null;
        expected[6] = "0a";
        expected[7] = "1a";
        expected[8] = "2a";
        expected[9] = "3a";
        expected[10] = "4a";
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("9a", array.getLast());

        // 5a, 6a, 7a, 8a, _, _, 0a, 1a, 2a, 3a, 4a
        assertEquals("9a", array.removeLast());
        // 5a, 6a, 7a, 8a, _, _, _, 1a, 2a, 3a, 4a
        assertEquals("0a", array.removeFirst());
        // 5a, 6a, 7a, _, _, _, _, 1a, 2a, 3a, 4a
        assertEquals("8a", array.removeLast());
        // 5a, 6a, 7a, _, _, _, _, _, 2a, 3a, 4a
        assertEquals("1a", array.removeFirst());

        assertEquals(6, array.size());
        expected[3] = null;
        expected[4] = null;
        expected[6] = null;
        expected[7] = null;
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("2a", array.getFirst());
        assertEquals("7a", array.getLast());

        // 5a, 6a, _, _, _, _, _, _, 2a, 3a, 4a
        assertEquals("7a", array.removeLast());

        assertEquals(5, array.size());
        expected[2] = null;
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("2a", array.getFirst());
        assertEquals("6a", array.getLast());

        array.addFirst("0a"); // 5a, 6a, _, _, _, _, _, 0a, 2a, 3a, 4a
        array.addLast("1a"); // 5a, 6a, 1a, _, _, _, _, 0a, 2a, 3a, 4a

        assertEquals(7, array.size());
        expected[7] = "0a";
        expected[2] = "1a";
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("1a", array.getLast());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeEmptyList() {
        array.addLast("5a"); // 5a, _, _, _, _, _, _, _, _, _, _

        // _, _, _, _, _, _, _, _, _, _, _
        assertEquals("5a", array.removeLast());
        assertEquals(0, array.size());
        String[] expected = new String[ArrayDeque.INITIAL_CAPACITY];
        expected[0] = null;
        expected[1] = null;
        expected[2] = null;
        expected[3] = null;
        expected[4] = null;
        expected[5] = null;
        expected[6] = null;
        expected[7] = null;
        expected[8] = null;
        expected[9] = null;
        expected[10] = null;
        assertNotNull(array.getBackingArray());
        assertArrayEquals(expected, array.getBackingArray());
    }


    @Test(timeout = TIMEOUT)
    public void testLinkedDequeAdd() {
        linked.addFirst("1a"); // 1a
        linked.addLast("9a"); // 1a, 9a
        linked.addFirst("4a"); // 4a, 1a, 9a
        linked.addFirst("8a"); // 8a, 4a, 1a, 9a
        linked.addFirst("5a"); // 5a, 8a, 4a, 1a, 9a

        assertEquals(5, linked.size());
        LinkedNode<String> cur = linked.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("5a", cur.getData());

        LinkedNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("8a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotEquals(null, cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("9a", cur.getData());
        assertEquals(linked.getTail(), cur);
        assertNull(cur.getNext());
    }

    @Test(timeout = TIMEOUT)
    public void testEmptyLinkedDequeRemove() {
        linked.addFirst("0a");
        assertEquals(1, linked.size());
        assertEquals("0a", linked.removeLast()); // the tail and the head is the same
        assertNull(linked.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedDequeRemove() {
        linked.addFirst("1a");
        linked.addFirst("0a"); // 0a, 1a
        linked.addLast("2a"); // 0a, 1a, 2a
        linked.addLast("3a"); // 0a, 1a, 2a, 3a

        assertEquals(4, linked.size());
        assertNull(linked.getHead().getPrevious());
        assertNotNull(linked.getHead());

        assertEquals("3a", linked.removeLast()); // 0a, 1a, 2a
        assertEquals("0a", linked.removeFirst()); // 1a, 2a

        assertEquals(2, linked.size());

        LinkedNode<String> cur = linked.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("1a", cur.getData());

        LinkedNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());
        assertEquals(linked.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedDequeGet() {
        linked.addFirst("9");
        linked.addLast("8");
        linked.removeFirst();
        linked.addFirst("5");
        linked.addLast("3");

        // 5, 8, 3
        LinkedNode<String> cur = linked.getHead();
        LinkedNode<String> prev = cur;
        cur = cur.getNext();
        assertEquals("8", cur.getData());
        assertEquals("5", prev.getData());
        assertEquals("5", linked.getFirst());
        assertEquals("3", linked.getLast());
        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals("3", cur.getData());
        assertEquals(linked.getTail(), cur);
        assertNull(cur.getNext());

    }

}
