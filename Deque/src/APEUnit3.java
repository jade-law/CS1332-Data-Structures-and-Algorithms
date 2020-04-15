import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class APEUnit3 {
    private ArrayDeque<String> ard;
    @Before
    public void setUp() throws Exception {
        ard = new ArrayDeque<String>();
        deque = new LinkedDeque<>();
        adeque = new ArrayDeque<String>();
    }

    @Test
    public void addFirst() {
        System.out.println("TESTING ADD FIRST");
        ard.addFirst("k");
        String[] expect = new String[ArrayDeque.INITIAL_CAPACITY];
        //check first addFirst. should go to last index in arr
        expect[10] = "k";
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("k", ard.getFirst());
        assertEquals("k", ard.getLast());

        //add data
        ard.addFirst("j");
        ard.addFirst("i");
        ard.addFirst("h");
        ard.addFirst("g");
        ard.addFirst("f");
        assertEquals(6, ard.size());

        // checking about halfway through
        expect = new String[] {null, null, null, null, null, "f", "g", "h", "i", "j", "k"};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("f", ard.getFirst());
        assertEquals("k", ard.getLast());

        //add data
        ard.addFirst("e");
        ard.addFirst("d");
        ard.addFirst("c");
        ard.addFirst("b");
        assertEquals(10, ard.size());

        //before adding last element
        expect = new String[] {null, "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("b", ard.getFirst());
        assertEquals("k", ard.getLast());

        //add and check before resizing
        ard.addFirst("a");
        assertEquals(11, ard.size());
        expect = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("k", ard.getLast());

        //resize
        ard.addFirst("welcome to new array");
        assertEquals(12, ard.size());
        //filling expect with nulls.
        expect = new String[] {"welcome to new array", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
        for (int i = 12; i < ard.INITIAL_CAPACITY*2; i++) {
            String[] temp = Arrays.copyOf(expect, i + 1);
            temp[i] = null;
            expect = temp;
        }
        System.out.println("should look like this after resize: " + Arrays.toString(expect)); //print expect
        System.out.println("yours looks like this:              " + Arrays.toString(ard.getBackingArray()));
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("welcome to new array", ard.getFirst());
        assertEquals("k", ard.getLast());
        System.out.println();
    }

    @Test
    public void addLast() {
        System.out.println("TESTING ADD LAST");
        ard.addLast("a");
        String[] expect = new String[ArrayDeque.INITIAL_CAPACITY];
        //check first addFirst. should go to last index in arr
        expect[0] = "a";
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("a", ard.getLast());

        //add data
        ard.addLast("b");
        ard.addLast("c");
        ard.addLast("d");
        ard.addLast("e");
        ard.addLast("f");
        assertEquals(6, ard.size());

        // checking about halfway through
        expect = new String[] {"a", "b", "c", "d", "e", "f", null, null, null, null, null};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("f", ard.getLast());

        //add data
        ard.addLast("g");
        ard.addLast("h");
        ard.addLast("i");
        ard.addLast("j");
        assertEquals(10, ard.size());

        //before adding last element
        expect = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", null};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("j", ard.getLast());

        //add and check before resizing
        ard.addLast("k");
        assertEquals(11, ard.size());
        expect = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("k", ard.getLast());

        //resize
        ard.addLast("welcome to new array");
        assertEquals(12, ard.size());
        //filling expect with nulls.
        expect = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "welcome to new array"};
        for (int i = 12; i < ard.INITIAL_CAPACITY*2; i++) {
            String[] temp = Arrays.copyOf(expect, i + 1);
            temp[i] = null;
            expect = temp;
        }
        System.out.println("should look like this after resize: " + Arrays.toString(expect)); //print expect
        System.out.println("yours looks like this:              " + Arrays.toString(ard.getBackingArray()));
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("welcome to new array", ard.getLast());
    }

    @Test
    public void mixedResizeNoWrap() {
        System.out.println();
        System.out.println("TESTING MIXED ADDS AND RESIZE");
        System.out.println("add first");
        ard.addFirst("f");
        ard.addFirst("e");
        ard.addFirst("d");
        ard.addFirst("c");
        ard.addFirst("b");
        ard.addFirst("a");
        // checking about halfway through
        String[] expect = new String[] {null, null, null, null, null, "a", "b", "c", "d", "e", "f"};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("f", ard.getLast());
        assertEquals(6, ard.size());

        System.out.println("add last");
        ard.addLast("g");
        ard.addLast("h");
        ard.addLast("i");
        ard.addLast("j");
        ard.addLast("k");
        //checking the add lasts
        assertEquals(11, ard.size());
        expect = new String[] {"g", "h", "i", "j", "k", "a", "b", "c", "d", "e", "f"};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("k", ard.getLast());

        //testing add last in mixed case
        System.out.println("testing add last in mixed case");
        ard.addLast("welcome to new array");
        assertEquals(12, ard.size());
        expect = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "welcome to new array"};
        for (int i = 12; i < ard.INITIAL_CAPACITY*2; i++) {
            String[] temp = Arrays.copyOf(expect, i + 1);
            temp[i] = null;
            expect = temp;
        }
        System.out.println("should look like this after resize: " + Arrays.toString(expect)); //print expect
        System.out.println("yours looks like this:              " + Arrays.toString(ard.getBackingArray()));
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("a", ard.getFirst());
        assertEquals("welcome to new array", ard.getLast());

        //reset data
        ard = new ArrayDeque<String>();
        ard.addFirst("f");
        ard.addFirst("e");
        ard.addFirst("d");
        ard.addFirst("c");
        ard.addFirst("b");
        ard.addFirst("a");
        assertEquals(6, ard.size());
        ard.addLast("g");
        ard.addLast("h");
        ard.addLast("i");
        ard.addLast("j");
        ard.addLast("k");
        assertEquals(11, ard.size());

        //testing add first in mixed case
        System.out.println("testing add first in mixed case");
        ard.addFirst("welcome to new array");
        assertEquals(12, ard.size());
        //filling expect with nulls.
        expect = new String[] {"welcome to new array", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
        for (int i = 12; i < ard.INITIAL_CAPACITY*2; i++) {
            String[] temp = Arrays.copyOf(expect, i + 1);
            temp[i] = null;
            expect = temp;
        }
        System.out.println("should look like this after resize: " + Arrays.toString(expect)); //print expect
        System.out.println("yours looks like this:              " + Arrays.toString(ard.getBackingArray()));
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        assertEquals("welcome to new array", ard.getFirst());
        assertEquals("k", ard.getLast());
        System.out.println();

        //DO NOT RESIZE ON REMOVE AND OTHER LOGIC TEST
        ard.removeLast();
        Object[] temp = ard.getBackingArray(); /* for some stupid reason this is necessary:
                       assertEquals(22, ard.getBackingArray.length) throws a casting error between String and Object*/
        assertEquals(22, temp.length);

        expect[11] = null;
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        expect[11] = "k";
        ard.addLast("k");
        temp = ard.getBackingArray();
        assertEquals(22, temp.length);

        ard.removeFirst();
        expect[0] = null;
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        for(int i = 0; i < 11; i++) {
            ard.removeLast();
            temp = ard.getBackingArray();
            assertEquals(22, temp.length);
        }
        assertEquals(0, ard.size());
    }

    @Test
    public void removeFirst() {
        ard.addFirst("c");
        ard.addFirst("b");
        ard.addFirst("a");

        assertEquals(3, ard.size());
        assertEquals("a", ard.removeFirst());
        assertEquals(2, ard.size());
        assertEquals("b", ard.removeFirst());
        assertEquals(1, ard.size());
        assertEquals("c", ard.removeFirst());
        assertEquals(0, ard.size());
        //all interesting wraparound behavior is tested later
    }

    @Test
    public void removeLast() {
        ard.addFirst("c");
        ard.addFirst("b");
        ard.addFirst("a");

        assertEquals(3, ard.size());
        assertEquals("c", ard.removeLast());
        assertEquals(2, ard.size());
        assertEquals("b", ard.removeLast());
        assertEquals(1, ard.size());
        assertEquals("a", ard.removeLast());
        assertEquals(0, ard.size());
        //all interesting wraparound behavior is tested later
    }

    //Remove from front/back, then add should fill those empty spaces
    //Do not shift spaces with remove.
    @Test
    public void removeThenAdd() {
        String[] expect = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
        ard.addFirst("k");
        ard.addFirst("j");
        ard.addFirst("i");
        ard.addFirst("h");
        ard.addFirst("g");
        ard.addFirst("f");
        ard.addFirst("e");
        ard.addFirst("d");
        ard.addFirst("c");
        ard.addFirst("b");
        ard.addFirst("a");

        ard.removeLast();
        ard.addFirst("first last element");
        expect[10] = "first last element";
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        System.out.println("WRAPAROUND BEHAVIOR TEST");
        System.out.println("removelast then addfirst");
        System.out.println("should look like this after resize: " + Arrays.toString(expect)); //print expect
        System.out.println("yours looks like this:              " + Arrays.toString(ard.getBackingArray()));
        assertEquals("j", ard.removeLast());

        ard.addLast("j");
        ard.removeFirst();
        ard.addLast("k");
        expect[10] = "k";
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        System.out.println("resetting");
        System.out.println("should look like this after resize: " + Arrays.toString(expect)); //print expect
        System.out.println("yours looks like this:              " + Arrays.toString(ard.getBackingArray()));

        ard.removeFirst();
        ard.addLast("last first element");
        expect[0] = "last first element";
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        System.out.println("removefirst then addlast");
        System.out.println("should look like this after resize: " + Arrays.toString(expect)); //print expect
        System.out.println("yours looks like this:              " + Arrays.toString(ard.getBackingArray()));
        assertEquals("b", ard.removeFirst());
    }

    //Don't explicitly reset front to 0.
    //size 1 to size 0 should not be a special case
    @Test
    public void removeLastItem() {
        ard.addLast("a");
        //front should be 0
        ard.addLast("b");
        //front should be 0
        ard.addLast("c");
        //front = 0
        ard.removeFirst();
        //front = 1
        assertEquals(2, ard.size());
        ard.removeFirst();
        //front = 2
        assertEquals(1, ard.size());
        ard.removeFirst();
        //front = 3
        assertEquals(0, ard.size());
        ard.addFirst("front was 2, now is 1");
        String[] expect = {null, null, "front was 2, now is 1", null, null, null, null, null, null, null, null};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));

        ard = new ArrayDeque<String>();
        ard.addFirst("c");
        //front should be -1
        ard.addFirst("b");
        //front should be -2
        ard.addFirst("a");
        //front = -3
        ard.removeLast();
        //front = -3
        assertEquals(2, ard.size());
        ard.removeLast();
        //front = -3
        assertEquals(1, ard.size());
        ard.removeLast();
        //front = -3
        assertEquals(0, ard.size());
        ard.addFirst("front was -3, now is -4");
        expect = new String[]{null, null, null, null, null, null, null, "front was -3, now is -4", null, null, null};
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
    }

    //should just get first item. no remove
    //relies on front to be reliable
    @Test
    public void getFirst() {
        //getFirst and getLast are also relied on for the majority of this Junit.
        //if these are broken you can probably figure that out elsewhere
        String[] expect = new String[ArrayDeque.INITIAL_CAPACITY];
        //check first addFirst. should go to last index in arr
        expect[0] = "a";
        expect[1] = "b";
        expect[2] = "c";
        expect[3] = "d";
        expect[4] = "e";
        ard.addLast("a");
        ard.addLast("b");
        ard.addLast("c");
        ard.addLast("d");
        ard.addLast("e");

        //getFirst and getLast are also relied on for the majority of this Junit.
        //if these are broken you can probably figure that out elsewhere
        assertEquals("a", ard.getFirst());
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        ard.removeFirst();
        expect[0] = null;
        assertEquals("b", ard.getFirst());
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        ard.addLast("after");
        expect[5] = "after";
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        for (int i = 0; i < 5; i++) {
            ard.addLast("more after");
            expect[6 + i] = "more after";
        }
        assertEquals("b", ard.getFirst());
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
    }

    //should just get last item. no remove
    //relies on proper usage of mod
    @Test
    public void getLast() {
        //getFirst and getLast are also relied on for the majority of this Junit.
        //if these are broken you can probably figure that out elsewhere
        String[] expect = new String[ArrayDeque.INITIAL_CAPACITY];
        //check first addFirst. should go to last index in arr
        expect[0] = "a";
        expect[1] = "b";
        expect[2] = "c";
        expect[3] = "d";
        expect[4] = "e";
        ard.addLast("a");
        ard.addLast("b");
        ard.addLast("c");
        ard.addLast("d");
        ard.addLast("e");

        //getFirst and getLast are also relied on for the majority of this Junit.
        //if these are broken you can probably figure that out elsewhere
        assertEquals("e", ard.getLast());
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        ard.removeLast();
        expect[4] = null;
        assertEquals("d", ard.getLast());
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        ard.addFirst("before");
        expect[10] = "before";
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
        for (int i = 0; i < 5; i++) {
            ard.addFirst("more before");
            expect[9 - i] = "more before";
        }
        assertEquals("d", ard.getLast());
        assertEquals(Arrays.toString(expect), Arrays.toString(ard.getBackingArray()));
    }

    //----------------- EXPECTED ERROR BEHAVIOR ------------------
    @Test(expected = IllegalArgumentException.class)
    public void errorAddFirst() {
        ard.addFirst(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void errorAddLast() {
        ard.addLast(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void errorRemoveFirst() {
        ard.addFirst("a");
        ard.removeFirst();
        ard.removeFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void errorRemoveCross() {
        ard.addFirst("a");
        ard.removeLast();
        ard.removeLast();
    }

    @Test(expected = NoSuchElementException.class)
    public void errorRemoveCross2() {
        ard.addLast("a");
        ard.removeFirst();
        ard.removeFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void errorRemoveLast() {
        ard.addLast("a");
        ard.removeLast();
        ard.removeLast();
    }

    @Test(expected = NoSuchElementException.class)
    public void errorGetLast() {
        ard.addLast("a");
        ard.removeLast();
        ard.getLast();
    }

    @Test(expected = NoSuchElementException.class)
    public void errorGetFirst() {
        ard.addFirst("a");
        ard.removeFirst();
        ard.getFirst();
    }

    @Test
    public void lastValueLogic() {
        ard.addFirst("a");
        assertEquals(ard.getFirst(), ard.getLast());

        ard.addLast("b");
        assertEquals("a", ard.getFirst());
        assertEquals("b", ard.getLast());

        ard.removeFirst();
        assertEquals(ard.getFirst(), ard.getLast());

        ard.addLast("c");
        ard.removeLast();

    }

    //-------------------------- LINKEDEQUE ------------------------------
    private LinkedDeque<String> deque;
    private String message = "Bitcoin Ethereum Libra Money monai Monaiii";
    private String[] values = message.split(" ");

    @Test
    public void testAddToFrontEmpty() {
        deque.addFirst(values[0]);
        assertEquals(values[0], deque.getHead().getData());
        assertEquals(values[0], deque.getTail().getData());
        assertEquals(values[0], deque.getFirst());
        assertEquals(values[0], deque.getLast());
        assertEquals(1, deque.size());
    }

    @Test
    public void testAddToFrontMultiple() {
        deque.addFirst(values[0]);
        deque.addFirst(values[1]);
        deque.addFirst(values[2]);
        deque.addFirst(values[3]);
        assertEquals(values[3], deque.getHead().getData());
        assertEquals(values[3], deque.getFirst());
        assertEquals(values[0], deque.getTail().getData());
        assertEquals(values[0], deque.getLast());
        assertEquals(4, deque.size());
    }

    @Test
    public void testAddToBackEmpty() {
        deque.addLast(values[0]);
        assertEquals(values[0], deque.getHead().getData());
        assertEquals(values[0], deque.getTail().getData());
        assertEquals(values[0], deque.getFirst());
        assertEquals(values[0], deque.getLast());
    }

    @Test
    public void testAddToBackMultiple() {
        deque.addLast(values[0]);
        deque.addLast(values[1]);
        deque.addLast(values[2]);
        deque.addLast(values[3]);
        assertEquals(values[0], deque.getHead().getData());
        assertEquals(values[0], deque.getFirst());
        assertEquals(values[3], deque.getTail().getData());
        assertEquals(values[3], deque.getLast());
        assertEquals(4, deque.size());
    }

    @Test
    public void testAddAnywhere() {
        deque.addLast(values[0]);
        deque.addFirst(values[1]);
        deque.addFirst(values[2]);
        deque.addLast(values[3]);
        deque.addLast(values[4]);
        assertEquals(values[4], deque.getTail().getData());
        assertEquals(values[4], deque.getLast());
        assertEquals(values[2], deque.getHead().getData());
        assertEquals(values[2], deque.getFirst());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveFromFirstEmpty() {
        deque.removeFirst();
    }

    @Test
    public void testRemoveFromFirstMultiple() {
        helperAdd();
        assertEquals(values[3], deque.removeFirst());
        assertEquals(values[2], deque.removeFirst());
        assertEquals(values[1], deque.removeFirst());
        assertEquals(values[0], deque.removeFirst());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveFromBackEmpty() {
        deque.removeLast();
    }

    @Test
    public void testRemoveFromBackMultiple() {
        helperAdd();
        assertEquals(values[0], deque.removeLast());
        assertEquals(values[1], deque.removeLast());
        assertEquals(values[2], deque.removeLast());
        assertEquals(values[3], deque.removeLast());
    }

    @Test
    public void testRemoveAnywhere() {
        helperAdd();
        assertEquals(values[0], deque.removeLast());
        assertEquals(values[3], deque.removeFirst());
        assertEquals(values[1], deque.removeLast());
        assertEquals(values[2], deque.removeFirst());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstNullData() {
        deque.addFirst(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLastNullData() {
        deque.addLast(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetFirstEmpty() {
        deque.getFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetLastEmpty() {
        deque.getLast();
    }

    private void helperAdd() {
        deque.addFirst(values[0]);
        deque.addFirst(values[1]);
        deque.addFirst(values[2]);
        deque.addFirst(values[3]);
    }

    //---------------------ARRAYDEQUE TEST 2------------------------
    ArrayDeque<String> adeque;
    private String sentence = "hello human! Array deques are really really really cool! Also, modular arithmetic is pretty amazing. " +
            "If you are reading this sentence, then you are reading this sentence. That's all me got to say. Tee hee.";
    private String[] avalues = sentence.split(" ");

    @Test
    public void testAdd() {
        helperAdd(adeque);
        Object[] expectedValues = new Object[adeque.INITIAL_CAPACITY];
        expectedValues[0] = "human!";
        expectedValues[1] = "Array";
        expectedValues[expectedValues.length - 1] = "hello";
        expectedValues[expectedValues.length - 2] = "deques";

        assertArrayEquals(expectedValues, adeque.getBackingArray());

    }

    @Test
    public void testRemove() {
        helperAdd(adeque);
        System.out.println();
        System.out.println("start" + Arrays.toString(adeque.getBackingArray()));
        assertEquals(avalues[2], adeque.removeLast());
        assertEquals(avalues[3], adeque.removeFirst());
        assertEquals(avalues[0], adeque.removeFirst());
        assertEquals(avalues[1], adeque.removeFirst());
        System.out.println("aval" + Arrays.toString(avalues));
        System.out.println("1" + Arrays.toString(adeque.getBackingArray()));

        adeque.addFirst(avalues[4]);
        System.out.println("2" + Arrays.toString(adeque.getBackingArray()));
        adeque.addLast(avalues[5]);
        System.out.println("3" + Arrays.toString(adeque.getBackingArray()));
        adeque.addFirst(avalues[6]);
        System.out.println("4" + Arrays.toString(adeque.getBackingArray()));
        adeque.addLast(avalues[7]);
        System.out.println("5" + Arrays.toString(adeque.getBackingArray()));
        adeque.addFirst(avalues[8]);
        System.out.println("6" + Arrays.toString(adeque.getBackingArray()));

        Object[] expectedValues = new Object[adeque.INITIAL_CAPACITY];
        System.out.println("expectedvalues");
        System.out.println(Arrays.toString(expectedValues));
        expectedValues[0] = avalues[4];
        System.out.println(Arrays.toString(expectedValues));
        expectedValues[1] = avalues[5];
        System.out.println(Arrays.toString(expectedValues));
        expectedValues[expectedValues.length - 1] = avalues[6];
        System.out.println(Arrays.toString(expectedValues));
        expectedValues[2] = avalues[7];
        System.out.println(Arrays.toString(expectedValues));
        expectedValues[expectedValues.length - 2] = avalues[8];
        System.out.println(Arrays.toString(expectedValues));
        System.out.println();
        System.out.println(Arrays.toString(adeque.getBackingArray()));
        assertArrayEquals(expectedValues, adeque.getBackingArray());
    }

    @Test
    public void testWrapAround() {
        helperAdd(adeque);
        helperRemove(adeque);
        adeque.addFirst(avalues[0]);
        adeque.addLast(avalues[1]);
        adeque.removeFirst();
        adeque.addLast(avalues[2]);
        adeque.removeFirst();
        adeque.addLast(avalues[3]);
        adeque.addLast(avalues[4]);
        adeque.addLast(avalues[5]);
        adeque.addFirst(avalues[6]);
        adeque.addFirst(avalues[7]);
        Object expectedValues[] = new Object[adeque.INITIAL_CAPACITY];
        expectedValues[0] = avalues[7];
        expectedValues[1] = avalues[6];
        expectedValues[2] = avalues[2];
        expectedValues[3] = avalues[3];
        expectedValues[4] = avalues[4];
        expectedValues[5] = avalues[5];

        assertArrayEquals(expectedValues, adeque.getBackingArray());
    }

    @Test
    public void testRegrowWithAddFirst() {
        for (int i = 0; i < 11; i++) {
            adeque.addLast(avalues[i]);
        }
        adeque.addFirst(avalues[11]);
        Object[] expectedValues = new Object[adeque.INITIAL_CAPACITY * 2];
        for (int i = 0; i < 11; i++) {
            expectedValues[i+1] = avalues[i];
        }
        expectedValues[0] = avalues[11];
        assertArrayEquals(expectedValues, adeque.getBackingArray());
    }

    @Test
    public void testRegrowWithAddLast() {
        for (int i = 0; i < 11; i++) {
            adeque.addLast(avalues[i]);
        }
        adeque.addLast(avalues[11]);
        Object[] expectedValues = new Object[adeque.INITIAL_CAPACITY * 2];
        for (int i = 0; i < 12; i++) {
            expectedValues[i] = avalues[i];
        }
        assertArrayEquals(expectedValues, adeque.getBackingArray());
    }

    @Test
    public void testRegrowWithWrapAround() {
        helperAdd(adeque);

        helperRemove(adeque);
        adeque.addFirst(avalues[0]);
        adeque.addLast(avalues[1]);
        adeque.removeFirst();
        adeque.addLast(avalues[2]);
        adeque.removeFirst();
        adeque.addLast(avalues[3]);
        adeque.addLast(avalues[4]);
        adeque.addLast(avalues[5]);
        System.out.println(Arrays.toString(adeque.getBackingArray()));
        Object[] expectedValues = new Object[adeque.INITIAL_CAPACITY * 2];
        for (int i = 0; i < 7; i++) {
            adeque.addLast(avalues[6 + i]);
            expectedValues[5 + i] = avalues[6 + i];
        }

        adeque.addFirst(avalues[13]);

        expectedValues[0] = avalues[13];
        expectedValues[1] = avalues[2];
        expectedValues[2] = avalues[3];
        expectedValues[3] = avalues[4];
        expectedValues[4] = avalues[5];
        System.out.println(Arrays.toString(adeque.getBackingArray()));
        assertArrayEquals(expectedValues, adeque.getBackingArray());
    }

    private void helperAdd(ArrayDeque<String> deque) {
        deque.addFirst(avalues[0]);
        deque.addLast(avalues[1]);
        deque.addLast(avalues[2]);
        deque.addFirst(avalues[3]);
    }

    private void helperRemove(ArrayDeque<String> deque) {
        deque.removeLast();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
    }
    //-------------------- APEUNIT BOILERPLATE --------------------
    private static int failures = 0;
    private static int succeded = 0;
    public static final int TIMEOUT = 200;
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            failures++;
        }
        @Override
        protected void succeeded(Description description) {
            succeded++;
        }
    };

    //@Test(timeout =TIMEOUT)
    //tests go here
    //
    //@Test(expected = NullPointerException.class)

    @AfterClass
    public static void testCompleted() throws Exception {

        if (failures <= 0 && succeded >= 38) {
            try {
                Desktop.getDesktop().browse(
                        new URL("https://ape-unit.github.io/"
                                + "ape-invasion/").toURI());
            } catch (Exception e) { }
        } else {
            System.out.println("Ape game not unlocked."
                    + "Please continue to work on your tests.");
        }
    }
}