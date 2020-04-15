import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class DoublyLinkedListTestIshawnGullapalli {

    private static final int TIMEOUT = 200;
    private DoublyLinkedList<String> list;

    @Before
    public void setUp() {
        list = new DoublyLinkedList<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, list.size());
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexEmptyList() {
        list.addAtIndex(0, "0a"); // 0a
        assertEquals(1, list.size());

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(list.getHead(), list.getTail());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexZeroOneNode() {
        list.addAtIndex(0, "1a"); // 1a
        list.addAtIndex(0, "0a"); // 0a, 1a
        assertEquals(2, list.size());

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertSame(list.getTail(), curr);
        assertNull(curr.getNext());
        assertEquals("1a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexSizeOneNode() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        assertEquals(2, list.size());

        DoublyLinkedListNode<String> curr = list.getTail();
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertEquals("1a", curr.getData());

        DoublyLinkedListNode<String> next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertSame(list.getHead(), curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexZero() {
        list.addAtIndex(0, "1a"); // 1a
        list.addAtIndex(1, "2a"); // 1a, 2a
        list.addAtIndex(0, "0a"); // 0a, 1a, 2a
        assertEquals(3, list.size());

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("1a", curr.getData());

        prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("2a", curr.getData());

    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexSize() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        assertEquals(3, list.size());

        DoublyLinkedListNode<String> curr = list.getTail();
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertEquals("2a", curr.getData());

        DoublyLinkedListNode<String> next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("1a", curr.getData());

        next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertNull(curr.getPrevious());
        assertSame(curr, list.getHead());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexFrontHalf() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        list.addAtIndex(1, "test"); // 0a, test, 1a, 2a, 3a, 4a
        assertEquals(6, list.size());

        DoublyLinkedListNode<String> curr = list.getHead(); // 0a
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext(); // test
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("test", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 1a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("1a", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 2a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("2a", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 3a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("3a", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 4a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("4a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexBackHalf() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        list.addAtIndex(4, "test"); // 0a, 1a, 2a, 3a, test, 4a
        assertEquals(6, list.size());

        DoublyLinkedListNode<String> curr = list.getTail(); // 4a
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertEquals("4a", curr.getData());

        DoublyLinkedListNode<String> next = curr;
        curr = curr.getPrevious(); // test
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("test", curr.getData());

        next = curr;
        curr = curr.getPrevious(); // 3a
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("3a", curr.getData());

        next = curr;
        curr = curr.getPrevious(); // 2a
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("2a", curr.getData());

        next = curr;
        curr = curr.getPrevious(); // 1a
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("1a", curr.getData());

        next = curr;
        curr = curr.getPrevious(); // 0a
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertNull(curr.getPrevious());
        assertSame(curr, list.getHead());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexIndexOutOfBoundsException() {
        boolean worked = false;
        try {
            list.addAtIndex(10, "10a");
        } catch (IndexOutOfBoundsException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexIllegalArgumentException() {
        boolean worked = false;
        try {
            list.addAtIndex(0, null);
        } catch (IllegalArgumentException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testAddToFrontEmptyList() {
        list.addToFront("0a"); // 0a
        assertEquals(1, list.size());

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(list.getHead(), list.getTail());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddToFrontOneNode() {
        list.addAtIndex(0, "1a"); // 1a
        list.addToFront("0a"); // 0a, 1a
        assertEquals(2, list.size());

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("1a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddToFront() {
        list.addAtIndex(0, "1a"); // 1a
        list.addAtIndex(1, "2a"); // 1a, 2a
        list.addToFront("0a"); // 0a, 1a, 2a
        assertEquals(3, list.size());

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("1a", curr.getData());

        prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("2a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddToFrontIllegalArgumentException() {
        boolean worked = false;
        try {
            list.addToFront(null);
        } catch (IllegalArgumentException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testAddToBackEmptyList() {
        list.addToBack("0a"); // 0a
        assertEquals(1, list.size());

        DoublyLinkedListNode<String> curr = list.getTail();
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertNull(curr.getPrevious());
        assertSame(list.getHead(), list.getTail());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddToBackOneNode() {
        list.addAtIndex(0, "0a"); // 0a
        list.addToBack("1a"); // 0a, 1a
        assertEquals(2, list.size());

        DoublyLinkedListNode<String> curr = list.getTail();
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertEquals("1a", curr.getData());

        DoublyLinkedListNode<String> next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertNull(curr.getPrevious());
        assertSame(curr, list.getHead());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddToBack() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addToBack("2a"); // 0a, 1a, 2a
        assertEquals(3, list.size());

        DoublyLinkedListNode<String> curr = list.getTail();
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertEquals("2a", curr.getData());

        DoublyLinkedListNode<String> next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("1a", curr.getData());

        next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertNull(curr.getPrevious());
        assertSame(curr, list.getHead());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testAddToBackIllegalArgumentException() {
        boolean worked = false;
        try {
            list.addToBack(null);
        } catch (IllegalArgumentException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexSingleNode() {
        list.addAtIndex(0, "0a"); // 0a
        String removed = list.removeAtIndex(0); //

        assertEquals(0, list.size());
        assertEquals("0a", removed);
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexZero() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        String removed = list.removeAtIndex(0); // 1a

        assertEquals(1, list.size());
        assertEquals("0a", removed);

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(list.getHead(), list.getTail());
        assertEquals("1a", curr.getData());

        list.removeAtIndex(0);
        assertEquals(0, list.size());

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        removed = list.removeAtIndex(0); // 1a, 2a

        assertEquals(2, list.size());
        assertEquals("0a", removed);

        curr = list.getHead(); // 1a
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("1a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext(); // 2a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("2a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexSizeMinusOne() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        String removed = list.removeAtIndex(1); // 0a

        assertEquals(1, list.size());
        assertEquals("1a", removed);

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(list.getHead(), list.getTail());
        assertEquals("0a", curr.getData());

        list.removeAtIndex(0);
        assertEquals(0, list.size());

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        removed = list.removeAtIndex(2); // 0a, 1a

        assertEquals(2, list.size());
        assertEquals("2a", removed);

        curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("1a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexFrontHalf() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        String removed = list.removeAtIndex(1); // 0a, 2a, 3a, 4a

        assertEquals(4, list.size());
        assertEquals("1a", removed);

        DoublyLinkedListNode<String> curr = list.getHead(); // 0a
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext(); // 2a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("2a", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 3a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("3a", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 4a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("4a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexBackHalf() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        String removed = list.removeAtIndex(3); // 0a, 1a, 2a, 4a

        assertEquals(4, list.size());
        assertEquals("3a", removed);

        DoublyLinkedListNode<String> curr = list.getHead(); // 0a
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext(); // 1a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("1a", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 2a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertEquals("2a", curr.getData());

        prev = curr;
        curr = curr.getNext(); // 4a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("4a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexIndexOutOfBoundsException() {
        boolean worked = false;
        try {
            list.removeAtIndex(10);
        } catch (IndexOutOfBoundsException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromFrontOneNode() {
        list.addAtIndex(0, "0a"); // 0a
        String removed = list.removeFromFront(); //

        assertEquals(0, list.size());
        assertEquals("0a", removed);

        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromFront() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        String removed = list.removeFromFront(); // 1a

        assertEquals(1, list.size());
        assertEquals("0a", removed);

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(list.getHead(), list.getTail());
        assertEquals("1a", curr.getData());

        list.removeAtIndex(0);
        assertEquals(0, list.size());

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        removed = list.removeFromFront(); // 1a, 2a

        assertEquals(2, list.size());
        assertEquals("0a", removed);

        curr = list.getHead(); // 1a
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("1a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext(); // 2a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("2a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromFrontNoSuchElementException() {
        boolean worked = false;
        try {
            list.removeFromFront();
        } catch (NoSuchElementException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromBackOneNode() {
        list.addAtIndex(0, "0a"); // 0a
        String removed = list.removeFromBack();

        assertEquals(0, list.size());
        assertEquals("0a", removed);

        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromBack() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        String removed = list.removeFromBack(); // 0a

        assertEquals(1, list.size());
        assertEquals("1a", removed);

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(list.getHead(), list.getTail());
        assertEquals("0a", curr.getData());

        list.removeAtIndex(0);
        assertEquals(0, list.size());

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        removed = list.removeFromBack(); // 0a, 1a

        assertEquals(2, list.size());
        assertEquals("2a", removed);

        curr = list.getHead(); // 0a
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("0a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext(); // 1a
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("1a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromBackNoSuchElementException() {
        boolean worked = false;
        try {
            list.removeFromBack();
        } catch (NoSuchElementException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetIndexZero() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a

        String str = list.get(0);
        assertEquals("0a", str);
    }

    @Test(timeout = TIMEOUT)
    public void testGetIndexSizeMinusOne() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a

        String str = list.get(list.size() - 1);
        assertEquals("4a", str);
    }

    @Test(timeout = TIMEOUT)
    public void testGet() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a

        String e1 = list.get(1);
        assertEquals("1a", e1);
        String e2 = list.get(2);
        assertEquals("2a", e2);
        String e3 = list.get(3);
        assertEquals("3a", e3);

        for (int i = 0; i < 5; i++) {
            list.removeFromFront();
        }
        assertEquals(0, list.size());

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a

        e1 = list.get(1);
        assertEquals("1a", e1);
    }

    @Test(timeout = TIMEOUT)
    public void testGetIndexOutOfBoundsException() {
        boolean worked = false;
        try {
            list.get(10);
        } catch (IndexOutOfBoundsException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testIsEmpty() {
        assertEquals(0, list.size());
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testClear() {
        list.addAtIndex(0, "0a"); // 0a
        assertEquals(1, list.size());
        assertNotNull(list.getHead());
        assertNotNull(list.getTail());

        list.clear();
        assertEquals(0, list.size());
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceEmptyList() {
        boolean worked = false;
        try {
            list.removeLastOccurrence("0a");
        } catch (NoSuchElementException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceOneNode() {
        list.addAtIndex(0, "0a");
        String remove = list.removeLastOccurrence("0a");

        assertEquals(0, list.size());
        assertEquals("0a", remove);

        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceDataInTail() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "4a"); // 0a, 1a, 4a
        list.addAtIndex(3, "3a"); // 0a, 1a, 4a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 4a, 3a, 4a

        String remove = list.removeLastOccurrence("4a"); // 0a, 1a, 4a, 3a
        assertEquals(4, list.size());
        assertEquals("4a", remove);

        DoublyLinkedListNode<String> curr = list.getTail(); // 3a
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertEquals("3a", curr.getData());

        DoublyLinkedListNode<String> next = curr;
        curr = curr.getPrevious(); // 4a
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("4a", curr.getData());

        next = curr;
        curr = curr.getPrevious(); // 1a
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("1a", curr.getData());

        next = curr;
        curr = curr.getPrevious(); // 0a
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertNull(curr.getPrevious());
        assertSame(curr, list.getHead());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceDataInHead() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        String remove = list.removeLastOccurrence("0a");
        assertEquals(2, list.size());
        assertEquals("0a", remove);

        DoublyLinkedListNode<String> curr = list.getHead();
        assertNotNull(curr);
        assertNull(curr.getPrevious());
        assertEquals("1a", curr.getData());

        DoublyLinkedListNode<String> prev = curr;
        curr = curr.getNext();
        assertNotNull(curr);
        assertSame(prev, curr.getPrevious());
        assertNull(curr.getNext());
        assertSame(curr, list.getTail());
        assertEquals("2a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrence() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "4a"); // 0a, 1a, 4a
        list.addAtIndex(3, "1a"); // 0a, 1a, 4a, 1a
        list.addAtIndex(4, "2a"); // 0a, 1a, 4a, 1a, 2a
        String remove = list.removeLastOccurrence("1a"); // 0a, 1a, 4a, 2a

        assertEquals(4, list.size());
        assertEquals("1a", remove);

        DoublyLinkedListNode<String> curr = list.getTail();
        assertNotNull(curr);
        assertNull(curr.getNext());
        assertEquals("2a", curr.getData());

        DoublyLinkedListNode<String> next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("4a", curr.getData());

        next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertEquals("1a", curr.getData());

        next = curr;
        curr = curr.getPrevious();
        assertNotNull(curr);
        assertSame(next, curr.getNext());
        assertNull(curr.getPrevious());
        assertSame(curr, list.getHead());
        assertEquals("0a", curr.getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceIllegalArgumentException() {
        boolean worked = false;
        try {
            list.removeLastOccurrence(null);
        } catch (IllegalArgumentException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceNoSuchElementException() {
        boolean worked = false;
        try {
            list.addAtIndex(0, "0a");
            list.addAtIndex(1, "1a");
            list.addAtIndex(2, "2a");
            list.addAtIndex(3, "3a");
            list.addAtIndex(4, "4a");
            list.removeLastOccurrence("5a");
        } catch (NoSuchElementException e) {
            worked = true;
        }
        if (!worked) {
            fail();
        }
    }

    @Test(timeout = TIMEOUT)
    public void testToArrayEmptyList() {
        Object[] arr = new Object[0];
        assertArrayEquals(arr, list.toArray());
    }

    @Test(timeout = TIMEOUT)
    public void testToArrayOneNode() {
        list.addAtIndex(0, "0a");
        Object[] arr = new Object[1];
        arr[0] = "0a";
        assertArrayEquals(arr, list.toArray());
    }

    @Test(timeout = TIMEOUT)
    public void testToArray() {
        Object[] arr = new Object[5];
        for (int i = 0; i < 5; i++) {
            list.addAtIndex(i, i + "a");
            arr[i] = i + "a";
        }
        assertArrayEquals(arr, list.toArray());
    }

}
