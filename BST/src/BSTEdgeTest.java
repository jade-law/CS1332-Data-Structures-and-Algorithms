import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Note: you should have passed the BSTStudentTest first to avoid any unexpected behaviors from this test.
 */
public class BSTEdgeTest {
    private List<Integer> data =
            Arrays.asList(10, 2, 6, 7, 5, 1, 17, 13, 20);
    private BST<Integer> bst;

    /**
     * @param list a list of integers
     * @return a copy of the original list sorted in ascending order
     */
    private List<Integer> sorted(List<Integer> list) {
        return list.stream()
                .sorted(Comparator.comparing(a -> a))
                .collect(Collectors.toList());
    }

    @Before
    public void setup() {
        // Construct a new BST with data.
        bst = new BST<>(data);
    }

    @Test
    public void testRemove() {
        // Make sure that remove works for every node in the tree.
        for (int i : data) {
            System.out.println(i);
            bst = new BST<>(data);
            assertEquals(i, (int) bst.remove(i));
            List<Integer> dataSorted = sorted(data);
            dataSorted.removeIf(a -> a == i);
            assertEquals(dataSorted, bst.inorder());
            System.out.println(dataSorted);
        }

        // Make sure that remove uses predecessor when needed, instead of
        // successor.
        bst = new BST<>(data);
        bst.remove(10);
        assertEquals(7, (int) bst.getRoot().getData());
    }

    @Test
    public void testFindPathBetween() {
        // Various traversal cases
        assertEquals(
                Arrays.asList(5, 6, 2, 10, 17),
                bst.findPathBetween(5, 17)
        );
        assertEquals(
                Arrays.asList(1, 2, 10, 17, 20),
                bst.findPathBetween(1, 20)
        );
        assertEquals(Arrays.asList(6, 2), bst.findPathBetween(6, 2));
        assertEquals(Arrays.asList(2, 6), bst.findPathBetween(2, 6));
        assertEquals(Arrays.asList(13, 17, 10, 2), bst.findPathBetween(13, 2));
        assertEquals(Arrays.asList(5, 6, 2), bst.findPathBetween(5, 2));
        assertEquals(Arrays.asList(5, 6, 2, 10), bst.findPathBetween(5, 10));
        assertEquals(Arrays.asList(10, 2, 6, 5), bst.findPathBetween(10, 5));
        assertEquals(Arrays.asList(13, 17, 10), bst.findPathBetween(13, 10));

        // When data1 equals data2
        assertEquals(Collections.singletonList(5), bst.findPathBetween(5, 5));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException() {
        // Test for BST::findPathBetween
        bst.findPathBetween(3, 3);
        bst.findPathBetween(3, 7);
        bst.findPathBetween(7, 3);
        bst.findPathBetween(100, 1000);

        // When the tree is empty
        bst.clear();
        bst.findPathBetween(12, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        // Test for BST::findPathBetween
        bst.findPathBetween(null, 3);
        bst.findPathBetween(3, null);
        bst.findPathBetween(null, null);

        // Test for BST::contains
        bst.contains(null);

        // Test for BST::get
        bst.get(null);
    }

    @Test()
    public void testOrder() {
        // Test BST::inorder
        assertEquals(sorted(data), bst.inorder());
        // Test BST::levelorder
        assertEquals(
                Arrays.asList(10, 2, 17, 1, 6, 13, 20, 5, 7),
                bst.levelorder()
        );
        // Test BST::preorder
        assertEquals(
                Arrays.asList(10, 2, 1, 6, 5, 7, 17, 13, 20),
                bst.preorder()
        );
        // Test BST::postorder
        assertEquals(
                Arrays.asList(1, 5, 7, 6, 2, 13, 20, 17, 10),
                bst.postorder()
        );
    }

    @Test()
    public void testContains() {
        assertTrue(bst.contains(5));
        assertTrue(bst.contains(10));
        assertTrue(bst.contains(13));
        assertFalse(bst.contains(12));
    }

}