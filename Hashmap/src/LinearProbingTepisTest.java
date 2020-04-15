import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

// It took me only about 20 minutes to write the actual homework, but writing
// this test took me 4 hours. (╯‵□′)╯︵┻━┻
//
// I do believe these 50 test cases are fairly comprehensive, and error
// messages should be very easy to read and understand, so good luck!
//                                                                     -- Tepis

public class LinearProbingTepisTest {

    private static final int TIMEOUT = 200;

    /** 100 null 214 null 304 null */
    private LinearProbingHashMap<TepisInt, String> createSparseMap() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(6);

        assertNull(map.put(new TepisInt(100), "a100"));
        assertNull(map.put(new TepisInt(214), "a214"));
        assertNull(map.put(new TepisInt(304), "a304"));

        return map;
    }

    /** 100 220 300 null null */
    private LinearProbingHashMap<TepisInt, String> createCondenseMap() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(5);

        assertNull(map.put(new TepisInt(100), "a100"));
        assertNull(map.put(new TepisInt(220), "a220"));
        assertNull(map.put(new TepisInt(300), "a300"));

        return map;
    }

    /** 334 404 null null 104 204 */
    private LinearProbingHashMap<TepisInt, String> createCondenseCircularMap() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(6);

        assertNull(map.put(new TepisInt(104), "a104"));
        assertNull(map.put(new TepisInt(204), "a204"));
        assertNull(map.put(new TepisInt(334), "a334"));
        assertNull(map.put(new TepisInt(404), "a404"));

        return map;
    }

    /** D666 D101 D202 D303 D404 D505 */
    private LinearProbingHashMap<TepisInt, String> createGhostMap() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(6);

        assertNull(map.put(new TepisInt(666), "a666"));
        assertSame("a666", map.remove(new TepisInt(666)));
        assertNull(map.put(new TepisInt(101), "a101"));
        assertSame("a101", map.remove(new TepisInt(101)));
        assertNull(map.put(new TepisInt(202), "a202"));
        assertSame("a202", map.remove(new TepisInt(202)));
        assertNull(map.put(new TepisInt(303), "a303"));
        assertSame("a303", map.remove(new TepisInt(303)));
        assertNull(map.put(new TepisInt(404), "a404"));
        assertSame("a404", map.remove(new TepisInt(404)));
        assertNull(map.put(new TepisInt(505), "a505"));
        assertSame("a505", map.remove(new TepisInt(505)));

        return map;
    }


    @Test(timeout = TIMEOUT)
    public void testDefaultConstructor() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>();
        new HashMapShapeAsserter(0,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testInitialSizeConstructorSize1() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(1);
        new HashMapShapeAsserter(0, (AE) null).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testInitialSizeConstructorSize3() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(3);
        new HashMapShapeAsserter(0, null, null, null).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutBuildSparse() {
        new HashMapShapeAsserter(3,
                new AE(100, "a100"),
                null,
                new AE(214, "a214"),
                null,
                new AE(304, "a304"),
                null
        ).assertEquals(this.createSparseMap());
        // 100 null 214 null 304 null
    }

    @Test(timeout = TIMEOUT)
    public void testPutBuildCondense() {
        new HashMapShapeAsserter(3,
                new AE(100, "a100"),
                new AE(220, "a220"),
                new AE(300, "a300"),
                null,
                null
        ).assertEquals(this.createCondenseMap());
        // 100 220 300 null null
    }

    @Test(timeout = TIMEOUT)
    public void testPutBuildCondenseCircular() {
        new HashMapShapeAsserter(4,
                new AE(334, "a334"),
                new AE(404, "a404"),
                null,
                null,
                new AE(104, "a104"),
                new AE(204, "a204")
        ).assertEquals(this.createCondenseCircularMap());
        // 334 404 null null 104 204
    }

    @Test(timeout = TIMEOUT)
    public void testPutExpandRightBefore() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(100);
        for (int i = 0; i < 67; i++) {
            map.put(new TepisInt(i), Integer.toString(i));
        }
        // Shouldn't expand
        assertEquals(100, map.getTable().length);
    }

    @Test(timeout = TIMEOUT)
    public void testPutExpand() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(100);
        for (int i = 0; i < 68; i++) {
            map.put(new TepisInt(i), Integer.toString(i));
        }
        // Should expand
        assertEquals(201, map.getTable().length);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveSparse() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        assertSame("a100", map.remove(new TepisInt(100)));
        assertSame("a214", map.remove(new TepisInt(214)));
        assertSame("a304", map.remove(new TepisInt(304)));

        new HashMapShapeAsserter(0,
                new AE(100, "a100", true),
                null,
                new AE(214, "a214", true),
                null,
                new AE(304, "a304", true),
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveCondense() {
        LinearProbingHashMap<TepisInt, String> map = this.createCondenseMap();
        // 100 220 300 null null

        assertSame("a220", map.remove(new TepisInt(220)));
        assertSame("a300", map.remove(new TepisInt(300)));
        assertSame("a100", map.remove(new TepisInt(100)));

        new HashMapShapeAsserter(0,
                new AE(100, "a100", true),
                new AE(220, "a220", true),
                new AE(300, "a300", true),
                null,
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveCondenseCircular() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        assertSame("a404", map.remove(new TepisInt(404)));
        assertSame("a104", map.remove(new TepisInt(104)));
        assertSame("a204", map.remove(new TepisInt(204)));
        assertSame("a334", map.remove(new TepisInt(334)));

        new HashMapShapeAsserter(0,
                new AE(334, "a334", true),
                new AE(404, "a404", true),
                null,
                null,
                new AE(104, "a104", true),
                new AE(204, "a204", true)
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutUpdateSparse() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        assertSame("a100", map.put(new TepisInt(100), "b100"));
        assertSame("a214", map.put(new TepisInt(214), "b214"));
        assertSame("a304", map.put(new TepisInt(304), "b304"));


        new HashMapShapeAsserter(3,
                new AE(100, "b100"),
                null,
                new AE(214, "b214"),
                null,
                new AE(304, "b304"),
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutUpdateCondense() {
        LinearProbingHashMap<TepisInt, String> map = this.createCondenseMap();
        // 100 220 300 null null

        // Remove an element so no resizing is triggered
        assertSame("a300", map.remove(new TepisInt(300)));

        assertSame("a100", map.put(new TepisInt(100), "b100"));
        assertSame("a220", map.put(new TepisInt(220), "b220"));


        new HashMapShapeAsserter(2,
                new AE(100, "b100"),
                new AE(220, "b220"),
                new AE(300, "a300", true),
                null,
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutUpdateCondenseCircular() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        // Remove an element so no resizing is triggered
        assertSame("a104", map.remove(new TepisInt(104)));

        assertSame("a204", map.put(new TepisInt(204), "b204"));
        assertSame("a334", map.put(new TepisInt(334), "b334"));
        assertSame("a404", map.put(new TepisInt(404), "b404"));

        new HashMapShapeAsserter(3,
                new AE(334, "b334"),
                new AE(404, "b404"),
                null,
                null,
                new AE(104, "a104", true),
                new AE(204, "b204")
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutWriteToFirstRemoved() {
        LinearProbingHashMap<TepisInt, String> map = this.createCondenseMap();
        // 100 220 300 null null

        assertSame("a300", map.remove(new TepisInt(300)));
        assertSame("a100", map.remove(new TepisInt(100)));
        assertNull(map.put(new TepisInt(450), "a450"));

        new HashMapShapeAsserter(2,
                new AE(450, "a450"),
                new AE(220, "a220"),
                new AE(300, "a300", true),
                null,
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutDuplicateAfterFirstRemoved() {
        LinearProbingHashMap<TepisInt, String> map = this.createCondenseMap();
        // 100 220 300 null null

        assertSame("a300", map.remove(new TepisInt(300)));
        assertSame("a100", map.remove(new TepisInt(100)));
        assertSame("a220", map.put(new TepisInt(220), "b220"));

        new HashMapShapeAsserter(1,
                new AE(100, "a100", true),
                new AE(220, "b220"),
                new AE(300, "a300", true),
                null,
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutGhostHash0() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createGhostMap();
        // D666 D101 D202 D303 D404 D505

        assertNull(map.put(new TepisInt(106), "a106"));

        new HashMapShapeAsserter(1,
                new AE(106, "a106"),
                new AE(101, "a101", true),
                new AE(202, "a202", true),
                new AE(303, "a303", true),
                new AE(404, "a404", true),
                new AE(505, "a505", true)
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutGhostHash1() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createGhostMap();
        // D666 D101 D202 D303 D404 D505

        assertNull(map.put(new TepisInt(107), "a107"));

        new HashMapShapeAsserter(1,
                new AE(666, "a666", true),
                new AE(107, "a107"),
                new AE(202, "a202", true),
                new AE(303, "a303", true),
                new AE(404, "a404", true),
                new AE(505, "a505", true)
        ).assertEquals(map);
    }

    /**
     * ...so you would trigger a resize before you even attempt to add the data
     * or figure out if it's a duplicate.
     *   -- JSDoc
     */
    @Test(timeout = TIMEOUT)
    public void testPutDuplicateStillTriggerResize() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseMap();
        // 100 220 300 null null

        assertSame("a100", map.put(new TepisInt(100), "b100"));

        new HashMapShapeAsserter(3,
                new AE(100, "b100"),
                new AE(300, "a300"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new AE(220, "a220"),
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testPutNoDuplicateTriggerResize() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseMap();
        // 100 220 300 null null

        assertNull(map.put(new TepisInt(250), "a250"));

        new HashMapShapeAsserter(4,
                new AE(100, "a100"),
                new AE(300, "a300"),
                null,
                null,
                null,
                null,
                new AE(250, "a250"),
                null,
                null,
                new AE(220, "a220"),
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testResizeBarelyFit() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        map.resizeBackingTable(4);

        new HashMapShapeAsserter(4,
                new AE(404, "a404"),
                new AE(104, "a104"),
                new AE(334, "a334"),
                new AE(204, "a204")
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testResizeDeletedBeGone() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createGhostMap();
        // D666 D101 D202 D303 D404 D505

        map.resizeBackingTable(5);

        new HashMapShapeAsserter(0,
                null,
                null,
                null,
                null,
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testClear() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        map.clear();
        new HashMapShapeAsserter(0,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testGetSparse() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        assertSame("a100", map.get(new TepisInt(100)));
        assertSame("a214", map.get(new TepisInt(214)));
        assertSame("a304", map.get(new TepisInt(304)));

        new HashMapShapeAsserter(3,
                new AE(100, "a100"),
                null,
                new AE(214, "a214"),
                null,
                new AE(304, "a304"),
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCondense() {
        LinearProbingHashMap<TepisInt, String> map = this.createCondenseMap();
        // 100 220 300 null null

        assertSame("a220", map.get(new TepisInt(220)));
        assertSame("a300", map.get(new TepisInt(300)));
        assertSame("a100", map.get(new TepisInt(100)));

        new HashMapShapeAsserter(3,
                new AE(100, "a100"),
                new AE(220, "a220"),
                new AE(300, "a300"),
                null,
                null
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCondenseCircular() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        assertSame("a404", map.get(new TepisInt(404)));
        assertSame("a104", map.get(new TepisInt(104)));
        assertSame("a204", map.get(new TepisInt(204)));
        assertSame("a334", map.get(new TepisInt(334)));

        new HashMapShapeAsserter(4,
                new AE(334, "a334"),
                new AE(404, "a404"),
                null,
                null,
                new AE(104, "a104"),
                new AE(204, "a204")
        ).assertEquals(map);
    }

    @Test(timeout = TIMEOUT)
    public void testContainsKeySparse() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        assertTrue(map.containsKey(new TepisInt(100)));
        assertTrue(map.containsKey(new TepisInt(214)));
        assertTrue(map.containsKey(new TepisInt(304)));
        assertFalse(map.containsKey(new TepisInt(200)));
        assertFalse(map.containsKey(new TepisInt(220)));
        assertFalse(map.containsKey(new TepisInt(101)));
    }

    @Test(timeout = TIMEOUT)
    public void testContainsKeyCondense() {
        LinearProbingHashMap<TepisInt, String> map = this.createCondenseMap();
        // 100 220 300 null null

        assertTrue(map.containsKey(new TepisInt(100)));
        assertTrue(map.containsKey(new TepisInt(220)));
        assertTrue(map.containsKey(new TepisInt(300)));
        assertFalse(map.containsKey(new TepisInt(200)));
        assertFalse(map.containsKey(new TepisInt(120)));
        assertFalse(map.containsKey(new TepisInt(101)));
    }

    @Test(timeout = TIMEOUT)
    public void testContainsKeyCondenseCircular() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        assertTrue(map.containsKey(new TepisInt(334)));
        assertTrue(map.containsKey(new TepisInt(404)));
        assertTrue(map.containsKey(new TepisInt(104)));
        assertTrue(map.containsKey(new TepisInt(204)));
        assertFalse(map.containsKey(new TepisInt(504)));
        assertFalse(map.containsKey(new TepisInt(100)));
        assertFalse(map.containsKey(new TepisInt(101)));
    }

    @Test(timeout = TIMEOUT)
    public void testContainsKeyGhost() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createGhostMap();
        // D666 D101 D202 D303 D404 D505

        assertFalse(map.containsKey(new TepisInt(334)));
        assertFalse(map.containsKey(new TepisInt(404)));
        assertFalse(map.containsKey(new TepisInt(104)));
        assertFalse(map.containsKey(new TepisInt(204)));
        assertFalse(map.containsKey(new TepisInt(504)));
        assertFalse(map.containsKey(new TepisInt(100)));
        assertFalse(map.containsKey(new TepisInt(101)));
    }

    @Test(timeout = TIMEOUT)
    public void testContainsKeyCondenseCircularWithRemoved() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        map.remove(new TepisInt(334));
        map.remove(new TepisInt(104));

        assertFalse(map.containsKey(new TepisInt(334)));
        assertTrue(map.containsKey(new TepisInt(404)));
        assertFalse(map.containsKey(new TepisInt(104)));
        assertTrue(map.containsKey(new TepisInt(204)));
        assertFalse(map.containsKey(new TepisInt(504)));
        assertFalse(map.containsKey(new TepisInt(100)));
        assertFalse(map.containsKey(new TepisInt(101)));
    }

    @Test(timeout = TIMEOUT)
    public void testKeySet() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        Set<TepisInt> expected = new HashSet<>();
        expected.add(new TepisInt(334));
        expected.add(new TepisInt(404));
        expected.add(new TepisInt(104));
        expected.add(new TepisInt(204));
        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void testKeySetWithRemoved() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        map.remove(new TepisInt(404));
        map.remove(new TepisInt(104));

        Set<TepisInt> expected = new HashSet<>();
        expected.add(new TepisInt(334));
        expected.add(new TepisInt(204));
        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void testValues() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        List<String> expected = new LinkedList<>();
        expected.add("a334");
        expected.add("a404");
        expected.add("a104");
        expected.add("a204");
        assertEquals(expected, map.values());
    }

    @Test(timeout = TIMEOUT)
    public void testValuesWithRemoved() {
        LinearProbingHashMap<TepisInt, String> map =
                this.createCondenseCircularMap();
        // 334 404 null null 104 204

        map.remove(new TepisInt(404));
        map.remove(new TepisInt(104));

        List<String> expected = new LinkedList<>();
        expected.add("a334");
        expected.add("a204");
        assertEquals(expected, map.values());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testPutExceptionKeyNull() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(6);
        map.put(null, "nope");
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testPutExceptionValueNull() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(6);
        map.put(new TepisInt(123), null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testPutExceptionKeyValueNull() {
        LinearProbingHashMap<TepisInt, String> map =
                new LinearProbingHashMap<>(6);
        map.put(null, null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testResizeExceptionTooSmall() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.resizeBackingTable(2);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testRemoveExceptionNull() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.remove(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveExceptionNotFound() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.remove(new TepisInt(101));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveExceptionNotFoundHashSame() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.remove(new TepisInt(300));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveExceptionNotFoundGhost() {
        LinearProbingHashMap<TepisInt, String> map = this.createGhostMap();
        // D666 D101 D202 D303 D404 D505

        map.remove(new TepisInt(300));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveExceptionNotFoundRemoved() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.remove(new TepisInt(100));
        map.remove(new TepisInt(100));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testGetExceptionNull() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.get(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testGetExceptionNotFound() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.get(new TepisInt(101));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testGetExceptionNotFoundHashSame() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.get(new TepisInt(300));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testGetExceptionNotFoundGhost() {
        LinearProbingHashMap<TepisInt, String> map = this.createGhostMap();
        // D666 D101 D202 D303 D404 D505

        map.get(new TepisInt(300));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testgetExceptionNotFoundRemoved() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.remove(new TepisInt(100));
        map.get(new TepisInt(100));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testContainsKeyExceptionNull() {
        LinearProbingHashMap<TepisInt, String> map = this.createSparseMap();
        // 100 null 214 null 304 null

        map.containsKey(null);
    }


    @BeforeClass
    public static void ad() {
        System.out.println(
                "Test cases brought to you by Tepis with <3\n\n"
              + "   ████████╗███████╗██████╗ ██╗███████╗   \n"
              + "   ╚══██╔══╝██╔════╝██╔══██╗██║██╔════╝   \n"
              + "      ██║   █████╗  ██████╔╝██║███████╗   \n"
              + "      ██║   ██╔══╝  ██╔═══╝ ██║╚════██║   \n"
              + "      ██║   ███████╗██║     ██║███████║   \n"
              + "      ╚═╝   ╚══════╝╚═╝     ╚═╝╚══════╝   \n\n"
              + "                Good Luck!                "
        );
    }

    /**
     * A container class that stores integers.
     * The hash of it is always its value % 100.
     * For example, new TepisInt(207).hashCode() = 7.
     */
    public static class TepisInt {
        private int value;
        public TepisInt(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            TepisInt tepisInt = (TepisInt) o;
            return this.value == tepisInt.value;
        }

        @Override
        public int hashCode() {
            return this.value % 100;
        }

        @Override
        public String toString() {
            return value + " (h=" + this.hashCode() + ")";
        }
    }

    /** Asserter entry */
    public static class AE {
        private TepisInt key;
        private String value;
        private boolean deleted;
        public AE(int k, String v, boolean d) {
            this.key = new TepisInt(k);
            this.value = v;
            this.deleted = d;
        }
        public AE(int k, String v) {
            this(k, v, false);
        }
        @Override
        public String toString() {
            return (this.deleted ? "[D] " : "") + this.key + ": "
                    + this.value;
        }
    }

    public static class HashMapShapeAsserter {
        private int size;
        private AE[] expected;
        public HashMapShapeAsserter(int size, AE... expected) {
            this.size = size;
            this.expected = expected;
        }
        public void assertEquals(
                LinearProbingHashMap<TepisInt, String> against
        ) {
            if (against.size() != this.size) {
                throw new AssertionError("HashMap returned wrong value"
                        + "for .size(). Expected: " + this.size + ". Actual: "
                        + against.size() + ".");
            }
            LinearProbingMapEntry<TepisInt, String>[] table =
                    against.getTable();
            if (table == null) {
                throw new AssertionError("Table is null.");
            }
            if (table.length != this.expected.length) {
                throw new HashMapShapeAssertionError("Size of the backing "
                        + "array is incorrect.", table);
            }
            for (int i = 0; i < this.expected.length; i++) {
                AE expected = this.expected[i];
                LinearProbingMapEntry<TepisInt, String> actual = table[i];
                if (expected == null && actual != null) {
                    throw new HashMapShapeAssertionError("The entry with "
                            + "index = " + i + " should be null.",
                            table);
                }
                if (expected != null && actual == null) {
                    throw new HashMapShapeAssertionError("The entry with "
                            + "index = " + i + " should not be null.",
                            table);
                }
                if (expected == null && actual == null) {
                    continue;
                }
                if (expected.deleted != actual.isRemoved()) {
                    throw new HashMapShapeAssertionError("The entry with "
                            + "index = " + i + " has wrong removed state.",
                            table);
                }
                if (!expected.key.equals(actual.getKey())) {
                    throw new HashMapShapeAssertionError("The entry with "
                            + "index = " + i + " has wrong key.",
                            table);
                }
                // Strict equality intended
                if (expected.value != actual.getValue()) {
                    throw new HashMapShapeAssertionError("The entry with "
                            + "index = " + i + " has wrong value.",
                            table);
                }
            }
        }

        public class HashMapShapeAssertionError extends AssertionError {
            public HashMapShapeAssertionError(
                    String message,
                    LinearProbingMapEntry<TepisInt, String>[] actual
            ) {
                super(message
                        + "\nExpected: "
                        + Arrays.stream(HashMapShapeAsserter.this.expected)
                            .map(ae -> ae == null ? "null" : ae.toString())
                            .collect(Collectors.joining(", ", "[", "]"))
                        + " Size = "
                        + HashMapShapeAsserter.this.expected.length
                        + "\n  Actual: "
                        + Arrays.stream(actual)
                            .map(entry -> (entry == null)
                                    ? "null"
                                    : ((entry.isRemoved())
                                        ? "[D] "
                                        : "") + entry.getKey() + ": "
                                    + entry.getValue())
                            .collect(Collectors.joining(", ", "[", "]"))
                        + " Size = "
                        + actual.length
                );
            }
        }
    }
}
