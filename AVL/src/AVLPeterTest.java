import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This is a not-so-basic set of unit tests for AVL.
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
 * @version 1.1
 * <p>
 * Thanks to Michael Einhorn for the idea of random tests for the k-Smallest
 * and Predecessor!
 */
public class AVLPeterTest {

    private static final int TIMEOUT = 200;
    //For random tests
    private static final int NUM_TESTS = 10000;
    private static final int MAX_NUM_ITEMS = 20;
    private Random randomSeeder;

    private AVL<Integer> avl;
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("Your tree when test below failed: ");
            printTree(avl);
        }
    };
    private List<Integer> expectedPreorder;

    @Before
    public void setup() {
        avl = new AVL<>();
        /*
        We need a way to repeat a SPECIFIC test when a random test fails.
        Therefore, EVERY time we run a random test, we seed the test'srandom
        number generator with a new random seed created by this random number
         generator. When a test fails, we can print out that random seed,
         thereby enabling us to rerun that specific test.
         */
        randomSeeder = new Random(42);
    }

    @Test(timeout = TIMEOUT)
    public void testConstructorAndClear() {
        /*
                     1
                    / \
                   0   2
        */

        avl = new AVL<>(Arrays.asList(1, 0, 2));

        expectedPreorder = Arrays.asList(1, 0, 2);
        checkSizeHeightBFandData(avl, expectedPreorder);

        avl.clear();
        assertEquals(0, avl.size());
        assertNull(avl.getRoot());

        //Empty collection
        avl = new AVL<Integer>(new ArrayList<Integer>());
        assertEquals(0, avl.size());
        assertNull(avl.getRoot());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testConstructorOfNull() {
        avl = new AVL<>(null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testConstructorWithNull() {
        List<Integer> toAdd = new ArrayList<Integer>(Arrays.asList(7, 10, 11));
        toAdd.add(null);
        avl = new AVL<>(toAdd);
    }

    @Test(timeout = TIMEOUT)
    public void testBasicAdd() {
        // Right rotate
        /*
                    2
                   /
                  1
                 /
                0

                ->

                  1
                 / \
                0   2
         */

        avl.add(2);
        avl.add(1);
        avl.add(0);

        expectedPreorder = Arrays.asList(1, 0, 2);
        checkSizeHeightBFandData(avl, expectedPreorder);

        // Left rotate
        /*
                0
                 \
                  1
                  \
                   2

                ->

                  1
                 / \
                0   2
         */
        avl = new AVL<>();
        avl.add(0);
        avl.add(1);
        avl.add(2);

        expectedPreorder = Arrays.asList(1, 0, 2);
        checkSizeHeightBFandData(avl, expectedPreorder);

        // Right left rotate
        /*
                0
                 \
                  2
                 /
                1

                ->

                  1
                 / \
                0   2
         */

        avl = new AVL<>();
        avl.add(0);
        avl.add(2);
        avl.add(1);

        expectedPreorder = Arrays.asList(1, 0, 2);
        checkSizeHeightBFandData(avl, expectedPreorder);

        // Left right rotate
        /*
                2
               /
              0
               \
                1

                ->

                  1
                 / \
                0   2
         */

        avl = new AVL<>();
        avl.add(2);
        avl.add(0);
        avl.add(1);

        expectedPreorder = Arrays.asList(1, 0, 2);
        checkSizeHeightBFandData(avl, expectedPreorder);
    }

    @Test(timeout = TIMEOUT)
    public void testAddDoubleRotations() {
        /*
                     5
                   /   \
                 1      10
                / \
               0   3
                  /
                 2

               ->

                     3
                   /   \
                 1      5
                /  \     \
               0    2    10
         */
        //Before adding the last 2:
        avl = new AVL<>(Arrays.asList(5, 1, 10, 0, 3));

        expectedPreorder = Arrays.asList(5, 1, 0, 3, 10);
        checkSizeHeightBFandData(avl, expectedPreorder);

        avl.add(2);

        expectedPreorder = Arrays.asList(3, 1, 0, 2, 5, 10);
        checkSizeHeightBFandData(avl, expectedPreorder);


        /*
                     5
                   /   \
                 1      10
                       /  \
                      7   11
                     /
                    6

               ->

                     7
                   /   \
                 5      10
                / \       \
               1   6      11
         */
        //Before adding the last 6:
        avl = new AVL<>(Arrays.asList(5, 1, 10, 7, 11));

        expectedPreorder = Arrays.asList(5, 1, 10, 7, 11);
        checkSizeHeightBFandData(avl, expectedPreorder);

        avl.add(6);

        expectedPreorder = Arrays.asList(7, 5, 1, 6, 10, 11);
        checkSizeHeightBFandData(avl, expectedPreorder);
    }

    @Test(timeout = TIMEOUT)
    public void testAddDuplicate() {
        //Adding a duplicate can't change the list
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.add(2);
        expectedPreorder = Arrays.asList(1, 0, 2);
        checkSizeHeightBFandData(avl, expectedPreorder);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAddNull() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.add(null);
    }

    @Test(timeout = TIMEOUT)
    public void testBasicRemove() {
        Integer temp = 1;

        /*
                     3
                   /   \
                 1      4
                / \
               0   2

               ->

                     3
                   /   \
                 2      4
                /
               0
         */


        avl.add(3);
        avl.add(temp);
        avl.add(4);
        avl.add(0);
        avl.add(2);

        assertSame(temp, avl.remove(1));
        expectedPreorder = Arrays.asList(3, 2, 0, 4);
        checkSizeHeightBFandData(avl, expectedPreorder);

        //Now remove 2:
        /*
                     3
                   /   \
                 0      4
         */
        assertEquals((Integer) 2, avl.remove(2));
        expectedPreorder = Arrays.asList(3, 0, 4);
        checkSizeHeightBFandData(avl, expectedPreorder);

        //Now remove 0:
        assertEquals((Integer) 0, avl.remove(0));
        expectedPreorder = Arrays.asList(3, 4);
        checkSizeHeightBFandData(avl, expectedPreorder);

        //Now remove 3:
        assertEquals((Integer) 3, avl.remove(3));
        expectedPreorder = Arrays.asList(4);
        checkSizeHeightBFandData(avl, expectedPreorder);

        //Now remove 4, the last element:
        assertEquals((Integer) 4, avl.remove(4));
        assertEquals(0, avl.size());
        assertNull(avl.getRoot());
    }

    @Test(timeout = TIMEOUT)
    public void testBasicRotateRemove() {
        /* Remove 10 (should be a right rotate):
                     5
                   /  \
                  2    10
                 / \
                0   3

                ->

                  2
                 / \
                0   5
                   /
                  3

         */
        avl = new AVL<>(Arrays.asList(5, 2, 10, 0, 3));
        assertEquals((Integer) 10, avl.remove(10));

        expectedPreorder = Arrays.asList(2, 0, 5, 3);
        checkSizeHeightBFandData(avl, expectedPreorder);

        /* Remove 2 (should be a left rotate):
                     5
                   /  \
                  2    10
                      /  \
                     7    12

                ->

                  10
                 /  \
                5   12
                \
                 7

         */
        avl = new AVL<>(Arrays.asList(5, 2, 10, 7, 12));
        assertEquals((Integer) 2, avl.remove(2));

        expectedPreorder = Arrays.asList(10, 5, 7, 12);
        checkSizeHeightBFandData(avl, expectedPreorder);
    }

    @Test(timeout = TIMEOUT)
    public void testDoubleRotateRemove() {
        /* Remove 12:
                    12
                   /  \
                 6     18
                / \    /
               4  9   15
                 / \
                7  10

                ->
                    15
                   /  \
                 6     18
                / \
               4  9
                 / \
                7  10

                ->
                    9
                   /  \
                 6     15
                / \   /  \
               4  7  10  18

         */

        avl = new AVL<>(Arrays.asList(12, 6, 18, 4, 9, 15, 7, 10));
        expectedPreorder = Arrays.asList(12, 6, 4, 9, 7, 10, 18, 15);
        checkSizeHeightBFandData(avl, expectedPreorder);

        assertEquals((Integer) 12, avl.remove(12));

        expectedPreorder = Arrays.asList(9, 6, 4, 7, 15, 10, 18);
        checkSizeHeightBFandData(avl, expectedPreorder);
    }

    @Test(timeout = TIMEOUT)
    public void testRotateOnRemove() {
        /* Remove 12, then 16:
                    12
                   /  \
                 6     18
                /     /  \
               4   15    21
                    \    /
                     16 20
                  ->
                    15
                   /  \
                 6     18
                /     /  \
               4    16    21
                         /
                        20
                  ->
                    16
                   /  \
                 6     20
                /     /  \
               4     18  21
         */
        avl = new AVL<>(Arrays.asList(12, 6, 18, 4, 15, 21, 16, 20));
        expectedPreorder = Arrays.asList(12, 6, 4, 18, 15, 16, 21, 20);
        checkSizeHeightBFandData(avl, expectedPreorder);

        assertEquals((Integer) 12, avl.remove(12));

        expectedPreorder = Arrays.asList(15, 6, 4, 18, 16, 21, 20);
        checkSizeHeightBFandData(avl, expectedPreorder);

        assertEquals((Integer) 15, avl.remove(15));

        expectedPreorder = Arrays.asList(16, 6, 4, 20, 18, 21);
        checkSizeHeightBFandData(avl, expectedPreorder);
    }

    @Test(timeout = TIMEOUT)
    public void testSucessorRemove() {
        /* Remove 18:
                    12
                   /  \
                 6     18
                /     /  \
               4     15   21
                           \
                            23
                  ->
                    12
                   /  \
                 6     21
                /     /  \
               4     15  23
         */
        avl = new AVL<>(Arrays.asList(12, 6, 18, 4, 15, 21, 23));
        expectedPreorder = Arrays.asList(12, 6, 4, 18, 15, 21, 23);
        checkSizeHeightBFandData(avl, expectedPreorder);

        assertEquals((Integer) 18, avl.remove(18));

        expectedPreorder = Arrays.asList(12, 6, 4, 21, 15, 23);
        checkSizeHeightBFandData(avl, expectedPreorder);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveNonexistant() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.remove(-1);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testRemoveNull() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.remove(null);
    }

    @Test(timeout = TIMEOUT)
    public void testGetAndContains() {
        Integer temp1 = 1;
        Integer temp0 = 0;
        Integer temp2 = 2;
        Integer temp3 = 3;

        /*
               1
             /   \
            0     2
                    \
                     3
         */

        avl.add(temp1);
        avl.add(temp0);
        avl.add(temp2);
        avl.add(temp3);

        // We want to make sure the data we retrieve is the one from the tree,
        // not the data that was passed in.

        assertSame(temp1, avl.get(1));
        assertTrue(avl.contains(1));
        assertSame(temp0, avl.get(0));
        assertTrue(avl.contains(0));
        assertSame(temp2, avl.get(2));
        assertTrue(avl.contains(2));
        assertSame(temp3, avl.get(3));
        assertTrue(avl.contains(3));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testGetNonexistant() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.get(-1);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testGetNull() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.get(null);
    }

    @Test(timeout = TIMEOUT)
    public void testContainsNonexistant() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        assertFalse(avl.contains(-1));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testContainsNull() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.contains(null);
    }

    @Test(timeout = TIMEOUT)
    public void testHeight() {
        /*
                     3
                   /   \
                 1      4
                / \
               0   2
         */

        avl.add(3);
        avl.add(1);
        avl.add(4);
        avl.add(0);
        avl.add(2);

        assertEquals(2, avl.height());
    }

    @Test(timeout = TIMEOUT)
    public void testBasicPredecessor() {
        /*
                76
              /    \
            34      90
             \      /
              40  81
         */

        avl.add(76);
        avl.add(34);
        avl.add(90);
        avl.add(40);
        avl.add(81);

        assertEquals((Integer) 40, avl.predecessor(76));
        assertEquals((Integer) 34, avl.predecessor(40));
        assertEquals((Integer) 76, avl.predecessor(81));
    }

    @Test(timeout = 2000)
    public void testRandomPredecessor() {
        Random rand;
        long randomSeed;

        int indexToChoose;
        Integer[] addedInts = new Integer[MAX_NUM_ITEMS];

        for (int i = 0; i < NUM_TESTS; i++) {
            /*
            NOTE: when putting in a specific seed, don't forget to add L (to
            indicated the literal is a long, not an int) at the end of it! Ex.
            rand = new Random(-5025562857975149833L);
            NOT
            rand = new Random(-5025562857975149833);
            */

            randomSeed = randomSeeder.nextLong();
            rand = new Random(randomSeed);

            avl.clear();

            for (int j = 0; j < MAX_NUM_ITEMS; j++) {
                Integer toAdd = rand.nextInt();
                avl.add(toAdd);
                addedInts[j] = toAdd;  //Completely overwrite old data
            }

            Arrays.sort(addedInts);

            //Randomly choose which number we will get the predecessor of
            indexToChoose = rand.nextInt(MAX_NUM_ITEMS);

            if (indexToChoose == 0) {
                assertNull(avl.predecessor(addedInts[0]));
            } else {
                //Predecessor will be item just before the one we chose since
                // the array is sorted
                assertEquals("Random Seed: " + randomSeed + "L",
                        addedInts[indexToChoose - 1],
                        avl.predecessor(addedInts[indexToChoose]));
            }
        }
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testPredecessorNonexistant() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.predecessor(-1);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testPredecessorNull() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.predecessor(null);
    }

    @Test(timeout = TIMEOUT)
    public void testBasicKSmallest() {
        /*
                    76
                 /      \
               34        90
              /  \      /
            20    40  81
         */

        avl.add(76);
        avl.add(34);
        avl.add(90);
        avl.add(20);
        avl.add(40);
        avl.add(81);

        List<Integer> smallest = new ArrayList<>();
        smallest.add(20);
        smallest.add(34);
        smallest.add(40);

        // Should be [20, 34, 40]
        assertEquals(smallest, avl.kSmallest(3));

        smallest.add(76);

        // Should be [20, 34, 40, 76]
        assertEquals(smallest, avl.kSmallest(4));

        smallest.add(81);
        smallest.add(90);

        // Should be [20, 34, 40, 81, 90]
        assertEquals(smallest, avl.kSmallest(6));
    }

    @Test(timeout = 2000)
    public void testRandomKSmallest() {
        final int numTests = 1000;
        final int maxNumItems = 20;
        Random rand;
        long randomSeed;

        int numItems;
        Integer[] addedInts = new Integer[maxNumItems];
        Integer[] sortedkSmallest;

        for (int i = 0; i < numTests; i++) {
            /*
            NOTE: when putting in a specific seed, don't forget to add L (to
            indicated the literal is a long, not an int) at the end of it! Ex.
            rand = new Random(-5025562857975149833L);
            NOT
            rand = new Random(-5025562857975149833);
            */

            randomSeed = randomSeeder.nextLong() + 1;
            rand = new Random(randomSeed);

            avl.clear();
            numItems = rand.nextInt(maxNumItems + 1);
            sortedkSmallest = new Integer[numItems];

            for (int j = 0; j < maxNumItems; j++) {
                Integer toAdd = rand.nextInt();
                avl.add(toAdd);
                addedInts[j] = toAdd;  //Completely overwrite old data
            }

            Arrays.sort(addedInts);
            sortedkSmallest = Arrays.copyOfRange(addedInts, 0, numItems);

            Object[] kSmallest = avl.kSmallest(numItems).toArray();
            assertArrayEquals("Random Seed: " + randomSeed + "L Expected: "
                    + Arrays.deepToString(sortedkSmallest) + " Was: " + Arrays.deepToString(kSmallest), sortedkSmallest, kSmallest);
        }
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNegKSmallest() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.kSmallest(-1);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testTooBigKSmallest() {
        avl = new AVL<>(Arrays.asList(1, 0, 2));
        avl.kSmallest(4);
    }


    /**
     * One test method to rule them all - this checks size, node heights and
     * balance factors, and the nodes' data
     * <p>
     * Starts by checking size
     * Then checks all heights and balance factors
     * Finally checks the tree against the given preorder traversal, since
     * preorder traversal uniquely defines the AVL.
     * @param tree             Tree to check
     * @param expectedPreorder What the preorder SHOULD be
     */
    public void checkSizeHeightBFandData(AVL<Integer> tree,
                                         List<Integer> expectedPreorder) {
        assertEquals(expectedPreorder.size(), tree.size());

        checkHeightAndBF(tree.getRoot());

        List<Integer> preorderList = preorder(tree);
        assertArrayEquals(expectedPreorder.toArray(), preorderList.toArray());
    }

    /**
     * Calculates the heights and balance factors of all nodes and compares
     * them to what is stored in the node
     * @param curr root of subtree to check
     * @return height of that subtree
     */
    private int checkHeightAndBF(AVLNode<Integer> curr) {
        if (curr == null) {
            return -1;
        }
        int heightLeft = checkHeightAndBF(curr.getLeft());
        int heightRight = checkHeightAndBF(curr.getRight());
        int height = Math.max(heightLeft, heightRight) + 1;
        int balanceFactor = heightLeft - heightRight;

        assertEquals("Wrong height in node " + nodeToString(curr), height,
                curr.getHeight());
        assertEquals("Wrong balance factor in node " + nodeToString(curr),
                balanceFactor, curr.getBalanceFactor());

        return height;
    }

    /**
     * Returns a preorder traversal of the tree
     * @param tree tree to traverse
     * @return preorder list
     */
    private List<Integer> preorder(AVL<Integer> tree) {
        ArrayList<Integer> preorderList = new ArrayList<Integer>(tree.size());
        preorderH(tree.getRoot(), preorderList);
        return preorderList;
    }

    /**
     * Recursive helper for a preorder traversal of the tree
     * @param curr        subtree to traverse
     * @param currentData LinkedList of current data in the tree
     */
    private void preorderH(AVLNode<Integer> curr,
                           ArrayList<Integer> currentData) {
        if (curr != null) {
            currentData.add(curr.getData());
            preorderH(curr.getLeft(), currentData);
            preorderH(curr.getRight(), currentData);
        }
    }


    /*
     * Tree format:
     *                                   76, H: 2, BF: 0
     *
     *                                   /              \
     *
     * 34, H: 1, BF: -1                                          90, H: 1, BF: 1
     *                 \                                           /
     *
     *                 40, H: 0, BF: 0                  81, H: 0, BF: 0
     */


    /**
     * Prints the AVL tree in a nice format:
     * H is height, B is balance factor
     * @param toPrint Tree to print
     */
    public void printTree(AVL<Integer> toPrint) {
        AVLNode<Integer> root = toPrint.getRoot();
        if (root == null) {
            System.out.println("Root is null");
        }

        //How wide tree will be after printing
        int widthOfTree = treeWidth(root);

        StringBuilder[] treeByLevels =
                new StringBuilder[toPrint.height() * 2 + 1];
        for (int i = 0; i < treeByLevels.length; i++) {
            treeByLevels[i] = new StringBuilder(spaces(widthOfTree));
        }

        addSubtreeToPrint(root, treeByLevels, 0, 0);

        for (StringBuilder treeLevel : treeByLevels) {
            System.out.println(treeLevel);
        }
    }

    /**
     * Adds current subtree to the StringBuilder printing list
     * @param curr       root of subtree
     * @param treeLevels StringBuilder array that will be printed
     * @param currLevel  Depth of current node
     * @param startPoint Offset of current subtree from end of StringBuilder
     */
    private void addSubtreeToPrint(AVLNode<Integer> curr,
                                   StringBuilder[] treeLevels, int currLevel,
                                   int startPoint) {
        if (curr == null) {
            return;
        }
        //Add current node to the StringBuilder Array
        int leftTreeWidth = treeWidth(curr.getLeft());

        String currNodeString = nodeToString(curr);
        treeLevels[currLevel].replace(startPoint + leftTreeWidth,
                startPoint + leftTreeWidth + currNodeString.length(),
                currNodeString);

        if (curr.getLeft() != null) {
            treeLevels[currLevel + 1].setCharAt(startPoint + leftTreeWidth,
                    '/');
            addSubtreeToPrint(curr.getLeft(), treeLevels, currLevel + 2,
                    startPoint);
        }
        if (curr.getRight() != null) {
            treeLevels[currLevel + 1].setCharAt(startPoint + leftTreeWidth + currNodeString.length(), '\\');
            addSubtreeToPrint(curr.getRight(), treeLevels, currLevel + 2,
                    startPoint + leftTreeWidth + currNodeString.length());
        }

    }

    /**
     * Gets the width of print representation of the current subtree
     * @param curr subtree to print
     * @return width in characters of this subtree when printed
     */
    private int treeWidth(AVLNode<Integer> curr) {
        if (curr == null) {
            return 0;
        } else {
            return treeWidth(curr.getLeft()) + nodeToString(curr).length() + treeWidth(curr.getRight());
        }
    }

    /**
     * A better string representation of a node
     * @param theNode Node to convert
     * @return String representation
     */
    private String nodeToString(AVLNode<Integer> theNode) {
        if (theNode == null) {
            return "";
        }
        return theNode.getData() + ", H: " + theNode.getHeight() + ", BF: " + theNode.getBalanceFactor();
    }

    /**
     * Generate specified number of spaces
     * @param num number of spaces
     * @return String of specified number of spaces
     */
    private String spaces(int num) {
        return new String(new char[num]).replace("\0", " ");
    }
}
