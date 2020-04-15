import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * This is a basic set of unit tests for BST.
 *
 * Passing these tests doesn't guarantee any grade on these assignments. These
 * student JUnits that we provide should be thought of as a sanity check to
 * help you get started on the homework and writing JUnits in general.
 *
 * We highly encourage you to write your own set of JUnits for each homework
 * to cover edge cases you can think of for each data structure. Your code must
 * work correctly and efficiently in all cases, which is why it's important
 * to write comprehensive tests to cover as many cases as possible.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class BSTStudentTest {

    private static final int TIMEOUT = 200;
    private BST<Integer> bst;

    @Before
    public void setup() {
        bst = new BST<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, bst.size());
        assertNull(bst.getRoot());
    }

    @Test(timeout = TIMEOUT)
    public void testConstructorAndClear() {
        /*
                     2
                    /
                   0
                    \
                     1
        */

        List<Integer> toAdd = new ArrayList<>();
        toAdd.add(2);
        toAdd.add(0);
        toAdd.add(1);
        bst = new BST<>(toAdd);

        assertEquals((Integer) 2, bst.getRoot().getData());
        assertEquals((Integer) 0, bst.getRoot().getLeft().getData());
        assertEquals((Integer) 1, bst.getRoot().getLeft().getRight().getData());

        bst.clear();
        assertEquals(0, bst.size());
        assertNull(bst.getRoot());
    }

    @Test(timeout = TIMEOUT)
    public void testAdd() {
        /*
                      1
                     / \
                    0   2
        */

        bst.add(1);
        bst.add(0);
        bst.add(2);

        assertEquals(3, bst.size());
        assertEquals((Integer) 1, bst.getRoot().getData());
        assertEquals((Integer) 0, bst.getRoot().getLeft().getData());
        assertEquals((Integer) 2, bst.getRoot().getRight().getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemove() {
        Integer temp = 3;

        /*
                      2                    2
                     / \                  / \
                    1   3                1   4
                         \     ->             \
                          4                    5
                           \
                            5
        */

        bst.add(2);
        bst.add(1);
        bst.add(temp);
        bst.add(4);
        bst.add(5);

        assertSame(temp, bst.remove(3));
        assertEquals(4, bst.size());
        assertEquals((Integer) 2, bst.getRoot().getData());
        assertEquals((Integer) 1, bst.getRoot().getLeft().getData());
        assertEquals((Integer) 4, bst.getRoot().getRight().getData());
        assertEquals((Integer) 5, bst.getRoot().getRight()
            .getRight().getData());
    }

    @Test(timeout = TIMEOUT)
    public void testGetAndContains() {
        Integer temp4 = 4;
        Integer temp1 = 1;
        Integer temp2 = 2;
        Integer temp3 = 3;
        Integer temp7 = 7;
        Integer temp5 = 5;
        Integer temp6 = 6;

        /*
                       4
                    /     \
                   1       7
                    \     /
                     2   5
                      \   \
                       3   6
        */

        bst.add(temp4);
        bst.add(temp1);
        bst.add(temp2);
        bst.add(temp3);
        bst.add(temp7);
        bst.add(temp5);
        bst.add(temp6);

        // We want to make sure the data we retrieve is the one from the tree,
        // not the data that was passed in.

        assertSame(temp4, bst.get(4));
        assertTrue(bst.contains(4));
        assertSame(temp1, bst.get(1));
        assertTrue(bst.contains(1));
        assertSame(temp2, bst.get(2));
        assertTrue(bst.contains(2));
        assertEquals(temp3, bst.get(3));
        assertTrue(bst.contains(3));
        assertEquals(temp7, bst.get(7));
        assertTrue(bst.contains(7));
        assertEquals(temp5, bst.get(5));
        assertTrue(bst.contains(5));
        assertEquals(temp6, bst.get(6));
        assertTrue(bst.contains(6));
    }

    @Test(timeout = TIMEOUT)
    public void testPreorder() {
        /*
                       4
                    /     \
                   1       9
                    \     /
                     2   5
                      \   \
                       3   7
                          / \
                         6   8
        */

        bst.add(4);
        bst.add(1);
        bst.add(2);
        bst.add(3);
        bst.add(9);
        bst.add(5);
        bst.add(7);
        bst.add(6);
        bst.add(8);

        List<Integer> preorder = new ArrayList<>();
        preorder.add(4);
        preorder.add(1);
        preorder.add(2);
        preorder.add(3);
        preorder.add(9);
        preorder.add(5);
        preorder.add(7);
        preorder.add(6);
        preorder.add(8);

        // Should be [4, 1, 2, 3, 9, 5, 7, 6, 8]
        assertEquals(preorder, bst.preorder());
    }

    @Test(timeout = TIMEOUT)
    public void testInorder() {
        /*
                       4
                    /     \
                   1       9
                    \     /
                     2   5
                      \   \
                       3   7
                          / \
                         6   8
        */

        bst.add(4);
        bst.add(1);
        bst.add(2);
        bst.add(3);
        bst.add(9);
        bst.add(5);
        bst.add(7);
        bst.add(6);
        bst.add(8);

        List<Integer> inorder = new ArrayList<>();
        inorder.add(1);
        inorder.add(2);
        inorder.add(3);
        inorder.add(4);
        inorder.add(5);
        inorder.add(6);
        inorder.add(7);
        inorder.add(8);
        inorder.add(9);

        // Should be [1, 2, 3, 4, 5, 6, 7, 8, 9]
        assertEquals(inorder, bst.inorder());
    }

    @Test(timeout = TIMEOUT)
    public void testPostorder() {
        /*
                       4
                    /     \
                   1       9
                    \     /
                     2   5
                      \   \
                       3   7
                          / \
                         6   8
        */

        bst.add(4);
        bst.add(1);
        bst.add(2);
        bst.add(3);
        bst.add(9);
        bst.add(5);
        bst.add(7);
        bst.add(6);
        bst.add(8);

        List<Integer> postorder = new ArrayList<>();
        postorder.add(3);
        postorder.add(2);
        postorder.add(1);
        postorder.add(6);
        postorder.add(8);
        postorder.add(7);
        postorder.add(5);
        postorder.add(9);
        postorder.add(4);

        // Should be [3, 2, 1, 6, 8, 7, 5, 9, 4]
        assertEquals(postorder, bst.postorder());
    }

    @Test(timeout = TIMEOUT)
    public void testLevelorder() {
        /*
                       4
                    /     \
                   1       9
                    \     /
                     2   5
                      \   \
                       3   7
                          / \
                         6   8
        */

        bst.add(4);
        bst.add(1);
        bst.add(2);
        bst.add(3);
        bst.add(9);
        bst.add(5);
        bst.add(7);
        bst.add(6);
        bst.add(8);

        List<Integer> levelorder = new ArrayList<>();
        levelorder.add(4);
        levelorder.add(1);
        levelorder.add(9);
        levelorder.add(2);
        levelorder.add(5);
        levelorder.add(3);
        levelorder.add(7);
        levelorder.add(6);
        levelorder.add(8);

        // Should be [4, 1, 9, 2, 5, 3, 7, 6, 8]
        assertEquals(levelorder, bst.levelorder());
    }

    @Test(timeout = TIMEOUT)
    public void testHeight() {
        /*
                     3
                    /
                   1
                    \
                     2
        */

        bst.add(3);
        bst.add(1);
        bst.add(2);

        assertEquals(2, bst.height());
    }

    @Test(timeout = TIMEOUT)
    public void testFindPathBetween() {
        /*
                      1
                     / \
                    0   2
        */

        bst.add(1);
        bst.add(0);
        bst.add(2);

        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        assertEquals(expected, bst.findPathBetween(0, 2));

        expected.clear();
        expected.add(2);
        expected.add(1);
        assertEquals(expected, bst.findPathBetween(2, 1));
    }
}