import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ArrayListJustinTest {

    private static final int TIMEOUT = 200;
    private ArrayList<String> list;

    @Before
    public void setUp() {
        list = new ArrayList<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, list.size());
        assertArrayEquals(new Object[ArrayList.INITIAL_CAPACITY],
                list.getBackingArray());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromFront() {
        list.addAtIndex(0, "5");
        list.addAtIndex(0, "4");
        list.addAtIndex(0, "3");
        list.addAtIndex(0, "2");
        list.addAtIndex(0, "1");
        assertEquals("1", list.removeFromFront());
        assertEquals(4, list.size());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromBack() {
        list.addAtIndex(0, "5");
        list.addAtIndex(0, "4");
        list.addAtIndex(0, "3");
        list.addAtIndex(0, "2");
        list.addAtIndex(0, "1");
        assertEquals("5", list.removeFromBack());
        assertEquals(4, list.size());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromIndex() {
        list.addAtIndex(0, "5");
        list.addAtIndex(0, "4");
        list.addAtIndex(0, "3");
        list.addAtIndex(0, "2");
        list.addAtIndex(0, "1");
        assertEquals("3", list.removeAtIndex(2));
        assertEquals(4, list.size());
    }

    @Test(timeout = TIMEOUT)
    public void testExpand() {
        list.addAtIndex(0, "10");
        list.addAtIndex(0, "9");
        list.addAtIndex(0, "8");
        list.addAtIndex(0, "7");
        list.addAtIndex(0, "6");
        list.addAtIndex(0, "5");
        list.addAtIndex(0, "4");
        list.addAtIndex(0, "3");
        list.addAtIndex(0, "2");
        list.addAtIndex(0, "1");
        assertEquals(10, list.size());
        assertEquals(18, ((Object[]) list.getBackingArray()).length);
    }
}
