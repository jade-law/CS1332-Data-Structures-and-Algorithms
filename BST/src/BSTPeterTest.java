import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This is a not-so-basic set of unit tests for BST. Also be sure to check
 * out the tests over on CS 1332's online section: there are some excellent
 * tests over there too.
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
/*
 CRITICAL NOTE ON TESTS:
 Many of my added tests rely on comparing the preorder traversal of the BST to
 a predefined array, to check if your BST structure is correct (pre and
 postorder traversals uniquely define a BST). If your preorder method is
 incorrect, THOSE ADDED TESTS ARE MEANINGLESS (except for the ones checking
 various exceptions and null root cases).

 It also relies on your constructor correctly adding the data from an array
 to your BST.

 TL;DR: use the TA tests make sure your preorder method and constructors are
 correct before using these tests to debug the rest of your methods.
 */
public class BSTPeterTest {

    private static final int TIMEOUT = 200;
    /*
    Preorder of this BST (also a setup list for it):
         21
      /     \
    5        33
            /
          25
     */
    private static final List<Integer> SMALL_SETUP_LIST = Arrays.asList(21, 5,
            33, 25);

    /* To generate this BST (from BST.java):
                    50
                /        \
              25         75
            /    \
           12    37
          /  \    \
        10   15   40
            /
          13
     */
    private static final List<Integer> BIG_SETUP_LIST = Arrays.asList(50, 25,
            12, 10, 15, 13, 37, 40, 75);

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

        /*
                      1
                     / \
                    0   2
        */

        toAdd = new ArrayList<>();
        toAdd.add(1);
        toAdd.add(0);
        toAdd.add(2);
        bst = new BST<>(toAdd);

        assertEquals(3, bst.size());
        assertEquals((Integer) 1, bst.getRoot().getData());
        assertEquals((Integer) 0, bst.getRoot().getLeft().getData());
        assertEquals((Integer) 2, bst.getRoot().getRight().getData());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testConstructorOfNull() {
        bst = new BST<>(null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testConstructorWithNull() {
        ArrayList<Integer> toAdd = new ArrayList<Integer>(SMALL_SETUP_LIST);
        toAdd.add(null);
        bst = new BST<>(toAdd);
    }

    @Test(timeout = TIMEOUT)
    public void testConstructorAndPreorder() {
        //If this test fails, most of my other tests are no good to you. All
        // of them rely on being able to setup a BST through the constructor
        // and check it with a preorder traversal.
        //Possible exception to that are the ones checking various exceptions
        // and null root cases
        bst = new BST<>(SMALL_SETUP_LIST);
        assertArrayEquals(SMALL_SETUP_LIST.toArray(), bst.preorder().toArray());

        bst = new BST<>(BIG_SETUP_LIST);
        assertArrayEquals(BIG_SETUP_LIST.toArray(), bst.preorder().toArray());
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
    public void testAddDuplicate() {
        //Adding a duplicate can't change the list
        bst = new BST<>(SMALL_SETUP_LIST);
        bst.add(33);
        assertArrayEquals(SMALL_SETUP_LIST.toArray(), bst.preorder().toArray());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAddNull() {
        bst = new BST<>(SMALL_SETUP_LIST);
        bst.add(null);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveRightChild() {
        //The basic default test. Tests removing with only right child.
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
    public void testRemoveLeaf() {
        //Tests removing leaf node
        /*
             21                   21
          /     \                    \
        5        33    ->             33
                /                    /
              25                   25
         */
        bst = new BST<>(SMALL_SETUP_LIST);

        Integer temp = new Integer(5);
        Integer removed = bst.remove(temp);
        //Check removed value is equal but not the same
        assertEquals(temp, removed);
        assertNotSame(temp, removed);

        List<Integer> expected = Arrays.asList(21, 33, 25);
        assertArrayEquals(expected.toArray(), bst.preorder().toArray());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLeftChild() {
        //Tests removing left child
        /*
             21                   21
          /     \              /     \
        5        33    ->    5        25
                /
              25
         */
        bst = new BST<>(SMALL_SETUP_LIST);

        Integer temp = new Integer(33);
        bst.remove(temp);
        List<Integer> expected = Arrays.asList(21, 5, 25);

        assertArrayEquals(expected.toArray(), bst.preorder().toArray());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveTwoChildNode() {
        //Tests removing node with 2 children
        bst = new BST<>(BIG_SETUP_LIST);
        /*
                        50                          40
                    /        \                  /        \
                  25         75    ->         25         75
                /    \                      /    \
               12    37                    12    37
              /  \    \                   /  \
            10   15   40                10   15
                /                           /
              13                          13
         */
        bst.remove(50);
        List<Integer> expected = Arrays.asList(40, 25, 12, 10, 15, 13, 37, 75);
        assertArrayEquals(expected.toArray(), bst.preorder().toArray());

        bst = new BST<>(BIG_SETUP_LIST);
        /*
                        50                          50
                    /        \                  /        \
                  25         75    ->         15         75
                /    \                      /    \
               12    37                    12    37
              /  \    \                   /  \    \
            10   15   40                10   13   40
                /
              13
         */
        bst.remove(25);
        expected = Arrays.asList(50, 15, 12, 10, 13, 37, 40, 75);
        assertArrayEquals(expected.toArray(), bst.preorder().toArray());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveNonexistant() {
        bst = new BST<>(SMALL_SETUP_LIST);
        bst.remove(-1);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testRemoveNull() {
        bst = new BST<>(SMALL_SETUP_LIST);
        bst.remove(null);
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

        //Check bst hasn't been changed
        List<Integer> expected = Arrays.asList(4, 1, 2, 3, 7, 5, 6);
        assertArrayEquals(expected.toArray(), bst.preorder().toArray());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testGetNonexistant() {
        bst = new BST<>(SMALL_SETUP_LIST);
        bst.get(-1);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testGetNull() {
        bst = new BST<>(SMALL_SETUP_LIST);
        bst.get(null);
    }

    @Test(timeout = TIMEOUT)
    public void testContainsNonexistant() {
        bst = new BST<>(SMALL_SETUP_LIST);
        assertFalse(bst.contains(-1));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testContainsNull() {
        bst = new BST<>(SMALL_SETUP_LIST);
        bst.contains(null);
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
    public void testPreorderNullList() {
        List<Integer> preorder = new ArrayList<>();
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
    public void testInorderNullList() {
        List<Integer> inorder = new ArrayList<>();
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
    public void testPostorderNullList() {
        List<Integer> postorder = new ArrayList<>();
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
    public void testLevelorderNullList() {
        List<Integer> levelorder = new ArrayList<>();
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

        bst = new BST<>(BIG_SETUP_LIST);
        assertEquals(4, bst.height());
    }

    @Test(timeout = TIMEOUT)
    public void testHeightTinyLists() {
        //null has a height of -1, so empty list should be -1 high
        assertEquals(-1, bst.height());
        bst.add(-64);
        assertEquals(0, bst.height());
    }

    //TODO: Test if data is the same (but different reference equalities)
    //TODO: Test if one data is a descendent of the other
    //TODO: Test if root is null, or run into a null while searching for DCA
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

        /*
                        50
                    /        \
                  25         75
                /    \
               12    37
              /  \    \
            10   15   40
                /
              13
         */
        bst = new BST<>(BIG_SETUP_LIST);

        expected = Arrays.asList(10, 12, 25, 37, 40);
        assertArrayEquals(expected.toArray(),
                bst.findPathBetween(10, 40).toArray());

        expected = Arrays.asList(25, 12, 15, 13);
        assertArrayEquals(expected.toArray(),
                bst.findPathBetween(25, 13).toArray());

        expected = Arrays.asList(13, 15, 12, 25);
        assertArrayEquals(expected.toArray(),
                bst.findPathBetween(13, 25).toArray());

        expected = Arrays.asList(12);
        assertArrayEquals(expected.toArray(),
                bst.findPathBetween(12, 12).toArray());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testFindPathBetweenNotFound() {
        //We run off the tree while looking for the Deepest Common Ancestor
        bst = new BST<>(BIG_SETUP_LIST);
        bst.findPathBetween(-2, -1);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testFindPathBetweenNotFound2() {
        //We run off the tree while searching for each item separately
        bst = new BST<>(BIG_SETUP_LIST);
        bst.findPathBetween(13, -1);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testFindPathBetweenNotFound3() {
        //The same item, just not in the tree
        bst = new BST<>(BIG_SETUP_LIST);
        bst.findPathBetween(-1, -1);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testFindPathBetweenNoList() {
        bst.findPathBetween(13, -1);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testFindPathBetweenNull() {
        bst.findPathBetween(null, -1);
    }
}