import org.junit.Before;
import org.junit.Test;
import java.util.NoSuchElementException;
import static org.junit.Assert.assertEquals;

public class DoublyLinkedListEltonTest {

    private static final int TIMEOUT = 200;
    private DoublyLinkedList<Integer> list;
    private static final String HEADNOTUDPATED = "head was not updated when inserting into the list";
    private static final String TAILNOTUPDATED = "tail was not updated when inserting into the list";
    private static final String SIZENOTUPDATED = "size was not updated when inserting into the list";
    private static final String HEADNOTSETTONULL = "head was not set to null when removing an element from a singleton";
    private static final String TAILNOTSETTONULL = "tail was not set to null when removing an element from a singleton";

    @Before
    public void setUp() {
        list = new DoublyLinkedList<>();
    }

    @Test
    public void testAddOneToEmptyList() {
        Integer el = 12;
        list.addAtIndex(0, el);
        assertEquals(HEADNOTUDPATED, el, list.getHead().getData());
        assertEquals(TAILNOTUPDATED, el, list.getTail().getData());
        assertEquals(SIZENOTUPDATED, 1, list.size());
    }

    @Test
    public void testAddManyToEmptyList() {
        Integer headData = 12;
        Integer tailData = 13;
        list.addAtIndex(0, 2);
        list.addAtIndex(0, headData);
        assertEquals(SIZENOTUPDATED, 2, list.size());
        list.addAtIndex(1, 5);
        list.addAtIndex(2, 15);
        list.addAtIndex(list.size(), 3);
        list.addAtIndex(list.size(), tailData);

        assertEquals(HEADNOTUDPATED, headData, list.getHead().getData());
        assertEquals(TAILNOTUPDATED, tailData, list.getTail().getData());
    }

    @Test
    public void testAddToFront() {
        Integer headData1 = 12;
        Integer headData2 = 13;
        list.addToFront(headData1);
        assertEquals(HEADNOTUDPATED, headData1, list.getHead().getData());
        assertEquals(SIZENOTUPDATED, 1, list.size());
        assertEquals(TAILNOTUPDATED, headData1, list.getTail().getData());
        list.addToFront(headData2);
        assertEquals(HEADNOTUDPATED, headData2, list.getHead().getData());
        assertEquals(SIZENOTUPDATED, 2, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToFrontException() {
        list.addToFront(null);
    }

    @Test
    public void testAddToBack() {
        Integer tailData1 = 12;
        Integer tailData2 = 13;
        list.addToBack(tailData1);
        assertEquals(HEADNOTUDPATED, tailData1, list.getHead().getData());
        assertEquals(SIZENOTUPDATED, 1, list.size());
        assertEquals(TAILNOTUPDATED, tailData1, list.getTail().getData());
        list.addToBack(tailData2);
        assertEquals(TAILNOTUPDATED, tailData2, list.getTail().getData());
        assertEquals(SIZENOTUPDATED, 2, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToBackException() {
        list.addToBack(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexOutOfBoundsExceptionPositive() {
        list.addAtIndex(13, 35);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexOutOfBoundsExceptionNegative() {
        list.addAtIndex(-21, 24);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAtIndexNullData() {
        list.addAtIndex(0, null);
    }

    @Test
    public void testRemoveAtIndexSingletonAndMany() {
        Integer value = 5;
        list.addAtIndex(0, value);
        list.addAtIndex(1, 20);
        list.addAtIndex(1, 21);
        list.removeAtIndex(1);
        list.removeAtIndex(1);
        Integer removedValue = list.removeAtIndex(0);
        assertEquals(removedValue, value);
        assertEquals(HEADNOTSETTONULL, null, list.getHead());
        assertEquals(TAILNOTSETTONULL, null, list.getTail());
        assertEquals(SIZENOTUPDATED, 0, list.size());
    }

    @Test
    public void testRemoveFromFrontSingleton() {
        Integer value = 5;
        list.addAtIndex(0, value);
        Integer removedValue = list.removeFromFront();
        assertEquals(removedValue, value);
        assertEquals(HEADNOTSETTONULL, null, list.getHead());
        assertEquals(TAILNOTSETTONULL, null, list.getTail());
    }

    @Test
    public void testRemoveFromBackSingleton() {
        Integer value = 5;
        list.addAtIndex(0, value);
        Integer removedValue = list.removeFromBack();
        assertEquals(removedValue, value);
        assertEquals(HEADNOTSETTONULL, null, list.getHead());
        assertEquals(TAILNOTSETTONULL, null, list.getTail());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveFromBackEmpty() {
        list.removeFromBack();
    }

    @Test(expected= NoSuchElementException.class)
    public void testRemoveFromFrontEmpty() {
        list.removeFromFront();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexEmpty() {
        list.removeAtIndex(0);
    }

    @Test
    public void testRemoveAtIndexMany() {
        Integer headData = 12;
        Integer tailData = 13;
        list.addAtIndex(0, 3);
        list.addAtIndex(0, headData);
        list.addAtIndex(0, 24);
        list.addAtIndex(3, tailData);
        list.addAtIndex(4, 13);

        list.removeAtIndex(0);
        assertEquals(HEADNOTUDPATED, headData, list.getHead().getData());

        list.removeFromBack();
        assertEquals(TAILNOTUPDATED, tailData, list.getTail().getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveLastOccurrenceNullData() {
        list.removeLastOccurrence(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveLastOccurrenceNotFound() {
        list.addAtIndex(0, 15);
        list.addAtIndex(1, 20);
        list.removeLastOccurrence(3);
    }
}