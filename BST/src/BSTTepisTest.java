import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;


public class BSTTepisTest {
    private Integer a1 = new Integer(1);
    private Integer a2 = new Integer(2);
    private Integer a3 = new Integer(3);
    private Integer a4 = new Integer(4);
    private Integer a5 = new Integer(5);
    private Integer a6 = new Integer(6);
    private Integer a7 = new Integer(7);

    private List<Integer> tree1ConstructionList = Arrays.asList(a5, a3, a2,
            a7, a1, a4, a6);
    /**
     *  If anything fails, it is recommended that you draw this on paper
     *
     *          5
     *       /     \
     *      3       7
     *     / \     /
     *    2   4   6
     *   /
     *  1
     *
     *  Constructed by 5 3 2 7 1 4 6
     */
    private Node assertTree1 = new Node(a5,
            new Node(a3,
                    new Node(a2,
                            new Node(a1),
                            null),
                    new Node(a4)),
            new Node(a7,
                    new Node(a6),
                    null));

    @Test(timeout = 200)
    public void testConstructorEmpty() {
        BST<Integer> bst = new BST<>();
        assertNull(bst.getRoot());
        assertEquals(0, bst.size());
    }

    @Test(timeout = 200)
    public void testConstructorOneNode() {
        BST<Integer> bst = new BST<>(Collections.singletonList(a1));
        new Node(a1).assertEqual(bst);
        assertEquals(1, bst.size());
    }

    @Test(timeout = 200)
    public void testConstructorTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertTree1.assertEqual(bst);
        assertEquals(7, bst.size());
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testConstructorExceptionDataNull() {
        BST<Integer> bst = new BST<>(null);
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testConstructorExceptionDataContainsNull() {
        BST<Integer> bst = new BST<>(Arrays.asList(a1, null, a2));
    }

    @Test(timeout = 200)
    public void testAddOneNode() {
        BST<Integer> bst = new BST<>();
        bst.add(a1);
        new Node(a1).assertEqual(bst);
        assertEquals(1, bst.size());
    }

    @Test(timeout = 200)
    public void testAddTree1() {
        BST<Integer> bst = new BST<>();
        for (Integer item : tree1ConstructionList) {
            bst.add(item);
        }
        this.assertTree1.assertEqual(bst);
        assertEquals(7, bst.size());
    }

    @Test(timeout = 200)
    public void testAddDuplicates() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        for (Integer item : tree1ConstructionList) {
            bst.add(item);
        }
        this.assertTree1.assertEqual(bst);
        assertEquals(7, bst.size());
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testAddExceptionDataNull() {
        BST<Integer> bst = new BST<>();
        bst.add(null);
    }

    @Test(timeout = 200)
    public void testRemoveA1FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a1, bst.remove(new Integer(1)));
        assertEquals(6, bst.size());
        new Node(a5,
                new Node(a3,
                        new Node(a2),
                        new Node(a4)),
                new Node(a7,
                        new Node(a6),
                        null)).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveA2FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a2, bst.remove(new Integer(2)));
        assertEquals(6, bst.size());
        new Node(a5,
                new Node(a3,
                        new Node(a1),
                        new Node(a4)),
                new Node(a7,
                        new Node(a6),
                        null)).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveA3FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a3, bst.remove(new Integer(3)));
        assertEquals(6, bst.size());
        new Node(a5,
                new Node(a2,
                        new Node(a1),
                        new Node(a4)),
                new Node(a7,
                        new Node(a6),
                        null)).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveA4FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a4, bst.remove(new Integer(4)));
        assertEquals(6, bst.size());
        new Node(a5,
                new Node(a3,
                        new Node(a2,
                                new Node(a1),
                                null),
                        null),
                new Node(a7,
                        new Node(a6),
                        null)).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveA5FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a5, bst.remove(new Integer(5)));
        assertEquals(6, bst.size());
        new Node(a4,
                new Node(a3,
                        new Node(a2,
                                new Node(a1),
                                null),
                        null),
                new Node(a7,
                        new Node(a6),
                        null)).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveA6FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a6, bst.remove(new Integer(6)));
        assertEquals(6, bst.size());
        new Node(a5,
                new Node(a3,
                        new Node(a2,
                                new Node(a1),
                                null),
                        new Node(a4)),
                new Node(a7)).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveA7FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a7, bst.remove(new Integer(7)));
        assertEquals(6, bst.size());
        new Node(a5,
                new Node(a3,
                        new Node(a2,
                                new Node(a1),
                                null),
                        new Node(a4)),
                new Node(a6)).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveA6ThenA7FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a6, bst.remove(new Integer(6)));
        assertSame(a7, bst.remove(new Integer(7)));
        assertEquals(5, bst.size());
        new Node(a5,
                new Node(a3,
                        new Node(a2,
                                new Node(a1),
                                null),
                        new Node(a4)),
                null).assertEqual(bst);
    }

    @Test(timeout = 200)
    public void testRemoveFromOneNodeTree() {
        BST<Integer> bst = new BST<>(Collections.singletonList(a1));
        assertSame(a1, bst.remove(new Integer(1)));
        assertNull(bst.getRoot());
        assertEquals(0, bst.size());
    }

    @Test(timeout = 200)
    public void testRemoveEverythingFromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a7, bst.remove(new Integer(7)));
        assertSame(a3, bst.remove(new Integer(3)));
        assertSame(a2, bst.remove(new Integer(2)));
        assertSame(a5, bst.remove(new Integer(5)));
        assertSame(a4, bst.remove(new Integer(4)));
        assertSame(a1, bst.remove(new Integer(1)));
        assertSame(a6, bst.remove(new Integer(6)));
        assertNull(bst.getRoot());
        assertEquals(0, bst.size());
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testRemoveExceptionDataNull() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.remove(null);
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testRemoveExceptionFromEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.remove(new Integer(1));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testRemoveExceptionDataNotExistInTree() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.remove(new Integer(8));
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testRemoveExceptionDataNullFromEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.remove(null);
    }

    @Test(timeout = 200)
    public void testGetA1FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a1, bst.get(new Integer(1)));
    }

    @Test(timeout = 200)
    public void testGetA2FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a2, bst.get(new Integer(2)));
    }

    @Test(timeout = 200)
    public void testGetA3FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a3, bst.get(new Integer(3)));
    }

    @Test(timeout = 200)
    public void testGetA4FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a4, bst.get(new Integer(4)));
    }

    @Test(timeout = 200)
    public void testGetA5FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a5, bst.get(new Integer(5)));
    }

    @Test(timeout = 200)
    public void testGetA6FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a6, bst.get(new Integer(6)));
    }

    @Test(timeout = 200)
    public void testGetA7FromTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertSame(a7, bst.get(new Integer(7)));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testGetExceptionFromEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.get(new Integer(1));
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testGetExceptionDataNull() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.get(null);
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testGetExceptionDataNotExistInTree() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.get(new Integer(8));
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testGetExceptionDataNullInEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.get(null);
    }

    @Test(timeout = 200)
    public void testContainsA1InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertTrue(bst.contains(new Integer(1)));
    }

    @Test(timeout = 200)
    public void testContainsA2InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertTrue(bst.contains(new Integer(2)));
    }

    @Test(timeout = 200)
    public void testContainsA3InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertTrue(bst.contains(new Integer(3)));
    }

    @Test(timeout = 200)
    public void testContainsA4InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertTrue(bst.contains(new Integer(4)));
    }

    @Test(timeout = 200)
    public void testContainsA5InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertTrue(bst.contains(new Integer(5)));
    }

    @Test(timeout = 200)
    public void testContainsA6InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertTrue(bst.contains(new Integer(6)));
    }

    @Test(timeout = 200)
    public void testContainsA7InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertTrue(bst.contains(new Integer(7)));
    }

    @Test(timeout = 200)
    public void testContainsNonExistA8InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertFalse(bst.contains(new Integer(8)));
    }

    @Test(timeout = 200)
    public void testContainsNonExistA0InTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertFalse(bst.contains(new Integer(0)));
    }

    @Test(timeout = 200)
    public void testContainsNonExistInEmptyTree() {
        BST<Integer> bst = new BST<>();
        assertFalse(bst.contains(new Integer(5)));
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testContainsExceptionDataNull() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.contains(null);
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testContainsExceptionDataNullInEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.contains(null);
    }

    private void assertElementSame(List expected, List actual) {
        assertEquals(expected, actual);
        // Now check for reference equality
        for (int i = 0; i < expected.size(); i++) {
            if (expected.get(i) != actual.get(i)) {
                throw new AssertionError("The " + i + "th element of two list"
                        + " has different reference.");
            }
        }
    }

    @Test(timeout = 200)
    public void testPreorderTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a5, a3, a2, a1, a4, a7, a6),
                bst.preorder()
        );
    }

    @Test(timeout = 200)
    public void testPreorderEmptyTree() {
        BST<Integer> bst = new BST<>();
        this.assertElementSame(
                Collections.emptyList(),
                bst.preorder()
        );
    }

    @Test(timeout = 200)
    public void testInorderTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a1, a2, a3, a4, a5, a6, a7),
                bst.inorder()
        );
    }

    @Test(timeout = 200)
    public void testInorderEmptyTree() {
        BST<Integer> bst = new BST<>();
        this.assertElementSame(
                Collections.emptyList(),
                bst.inorder()
        );
    }

    @Test(timeout = 200)
    public void testPostorderTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a1, a2, a4, a3, a6, a7, a5),
                bst.postorder()
        );
    }

    @Test(timeout = 200)
    public void testPostorderEmptyTree() {
        BST<Integer> bst = new BST<>();
        this.assertElementSame(
                Collections.emptyList(),
                bst.postorder()
        );
    }

    @Test(timeout = 200)
    public void testLevelorderTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a5, a3, a7, a2, a4, a6, a1),
                bst.levelorder()
        );
    }

    @Test(timeout = 200)
    public void testLevelorderEmptyTree() {
        BST<Integer> bst = new BST<>();
        this.assertElementSame(
                Collections.emptyList(),
                bst.levelorder()
        );
    }

    @Test(timeout = 200)
    public void testHeightTree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        assertEquals(3, bst.height());
    }

    @Test(timeout = 200)
    public void testHeightEmptyTree() {
        BST<Integer> bst = new BST<>();
        assertEquals(-1, bst.height());
    }

    @Test(timeout = 200)
    public void testHeightOneNode() {
        BST<Integer> bst = new BST<>(Collections.singletonList(a1));
        assertEquals(0, bst.height());
    }

    @Test(timeout = 200)
    public void testClear() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.clear();
        assertNull(bst.getRoot());
        assertEquals(0, bst.size());
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA1A6Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a1, a2, a3, a5, a7, a6),
                bst.findPathBetween(new Integer(1), new Integer(6))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA6A1Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a6, a7, a5, a3, a2, a1),
                bst.findPathBetween(new Integer(6), new Integer(1))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA1A1Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Collections.singletonList(a1),
                bst.findPathBetween(new Integer(1), new Integer(1))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA2A2Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Collections.singletonList(a2),
                bst.findPathBetween(new Integer(2), new Integer(2))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA5A5Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Collections.singletonList(a5),
                bst.findPathBetween(new Integer(5), new Integer(5))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA1A5Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a1, a2, a3, a5),
                bst.findPathBetween(new Integer(1), new Integer(5))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA5A1Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a5, a3, a2, a1),
                bst.findPathBetween(new Integer(5), new Integer(1))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA3A4Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a3, a4),
                bst.findPathBetween(new Integer(3), new Integer(4))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA4A3Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a4, a3),
                bst.findPathBetween(new Integer(4), new Integer(3))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA4A6Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a4, a3, a5, a7, a6),
                bst.findPathBetween(new Integer(4), new Integer(6))
        );
    }

    @Test(timeout = 200)
    public void testFindPathBetweenA6A4Tree1() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        this.assertElementSame(
                Arrays.asList(a6, a7, a5, a3, a4),
                bst.findPathBetween(new Integer(6), new Integer(4))
        );
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testFindPathBetweenExceptionData1Null() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(null, new Integer(3));
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testFindPathBetweenExceptionData2Null() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(3), null);
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testFindPathBetweenExceptionData1Data2Null() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(null, null);
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testFindPathBetweenExceptionData1NullEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.findPathBetween(null, new Integer(3));
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testFindPathBetweenExceptionData2NullEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.findPathBetween(new Integer(3), null);
    }

    @Test(timeout = 200, expected = IllegalArgumentException.class)
    public void testFindPathBetweenExceptionData1Data2NullEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.findPathBetween(null, null);
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionEmptyTree() {
        BST<Integer> bst = new BST<>();
        bst.findPathBetween(new Integer(3), new Integer(3));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData1NotExistLarge() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(8), new Integer(3));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData1NotExistSmall() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(0), new Integer(3));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData2NotExistLarge() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(3), new Integer(8));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData2NotExistSmall() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(3), new Integer(0));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData1Data2NotExistSmall() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(0), new Integer(0));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData1Data2NotExistLarge() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(8), new Integer(8));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData1Data2NotExistSmallLarge() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(0), new Integer(8));
    }

    @Test(timeout = 200, expected = NoSuchElementException.class)
    public void testFindPathBetweenExceptionData1Data2NotExistLargeSmall() {
        BST<Integer> bst = new BST<>(tree1ConstructionList);
        bst.findPathBetween(new Integer(8), new Integer(0));
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

    class Node {
        private Integer data;
        private Node left;
        private Node right;
        Node(Integer data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
        Node(Integer data) {
            this(data, null, null);
        }
        void assertEqual(BST<Integer> tree) {
            if (tree.getRoot() == null) {
                throw new AssertionError("tree.root is null, which is "
                        + "not right.");
            }
            String path = "root";
            this.assertEqual(tree.getRoot(), path);
        }
        void assertEqual(BSTNode<Integer> node, String path) {

            // Strict equality.
            if (this.data != node.getData()) {
                throw new AssertionError("Node has wrong data, expected "
                        + this.data + ", actual " + node.getData() + ". Path: "
                        + path + ".");
            }
            if (this.left == null) {
                if (node.getLeft() != null) {
                    throw new AssertionError("Node has extra left "
                        + "child. Parent of the extra left child is "
                        + node.getData() + ". Its extra left child is "
                        + node.getLeft().getData() + ". Parent path: "
                        + path + ".");
                }
            } else {
                if (node.getLeft() == null) {
                    throw new AssertionError("Node missing left child"
                        + ". Parent missing left child is " + node.getData()
                        + ". It should have left child with value "
                        + this.left.data + ". Parent path: "
                        + path + ".");
                }
                this.left.assertEqual(node.getLeft(), path + ".left");
            }
            if (this.right == null) {
                if (node.getRight() != null) {
                    throw new AssertionError("Node has extra right "
                            + "child. Parent of the extra right child is "
                            + node.getData() + ". Its extra right child is "
                            + node.getRight().getData() + ". Parent path: "
                            + path + ".");
                }
            } else {
                if (node.getRight() == null) {
                    throw new AssertionError("Node missing right child"
                            + ". Parent missing right child is "
                            + node.getData()
                            + ". It should have right child with value "
                            + this.right.data + ". Parent path: "
                            + path + ".");
                }
                this.right.assertEqual(node.getRight(), path + ".right");
            }
        }
    }
}
