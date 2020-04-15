import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This set of JUnits assumes you have already passed all tests in the StudentTest
 * provided tests.
 *
 * If any tests do not work as expected, please let me know! Email me at tschott6@gatech.edu or
 * reply to my Piazza post.
 *
 *
 * TESTS INCLUDED:
 * Exception handling for every method that can throw an exception
 * Cases when size = 0
 * Cases when size = 1
 * Cases when size = 2 (Root and Left Node)
 * Traversals
 * Removing
 * Other edge cases
 *
 *
 * VISUALIZATION TOOL (Beta Version):
 * I created a visualization tool to help you understand your binary trees visually. To enable this feature,
 * change the static variable "visualize" to true. I recommend running 1 test at a time if this feature
 * is enabled, as it will print an array representation of your tree after each test that runs.
 * Some tests will print an expected and actual tree, so you can visually compare them to see what
 * possibly went wrong.
 *
 * To actually visualize your tree, copy the array that is outputted and go to:
 * https://tylrshot.github.io/bst/
 *
 * Paste your array in the text field and click show.
 * This will turn the array into the actual tree.
 *
 * To clear the current tree(s), just refresh the page. You can show multiple trees at a time, it
 * will simply create a new tree below any currently showing trees.
 *
 * This tool is in beta, but it should work for now! Let me know if you have any issues :)
 *
 *
 * NOTE:
 * There are multiple inner classes that can be run separately or together. There is one for each set
 * of tests I could think of. They should work with the given imports, but may require more.
 * Tested for IntelliJ.
 *
 *
 * @author Tyler Schott
 * @version 1.2
 */

@RunWith(SchottJUnit04.class)
@Suite.SuiteClasses({ SchottJUnit04.testExceptions.class, SchottJUnit04.testEmpty.class, SchottJUnit04.testSize1.class,
        SchottJUnit04.testSize2.class, SchottJUnit04.testTraversals1.class, SchottJUnit04.testTraversals2.class,
        SchottJUnit04.testRemove.class })
public class SchottJUnit04 extends Suite {

    /**
     * Make this true to have visualization arrays printed after each test
     * I recommend that you turn if to FALSE unless you are running 1 test at a time
     */
    public static final boolean visualize = false;

    public SchottJUnit04(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    // Ignore this: allows the visualization tool to work
    public static class testingTools {
        public static String binaryTreeVisualizer(BST tree) {
            int height = tree.height();

            Object[][] toReturn = new Object[height + 1][];

            for (int i = 0; i <= height; i++) {
                Object[] thisLevel = new Object[(int) Math.pow(2, i)];
                toReturn[i] = thisLevel;
            }

            binaryTreeVisualizerHelper(tree.getRoot(), 0, 0, toReturn);

            return Arrays.deepToString(toReturn);
        }

        private static void binaryTreeVisualizerHelper(BSTNode curr, int depth, int shift, Object[][] toReturn) {
            if (curr != null) {
                toReturn[depth][shift] = curr.getData();
                binaryTreeVisualizerHelper(curr.getLeft(), depth + 1, shift * 2, toReturn);
                binaryTreeVisualizerHelper(curr.getRight(), depth + 1, (shift * 2) + 1, toReturn);
            }
        }
    }

    public static class testExceptions {

        private static final int TIMEOUT = 200;
        private BST<Integer> tree;

        @Before
        public void setup() {
            tree = new BST<>();
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.print("Actual: ");
                testingTools.binaryTreeVisualizer(tree);
            }
        }

        @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
        public void testAddNull() {
            tree.add(null);
        }

        @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
        public void testRemoveNull() {
            tree.add(1);
            tree.remove(null);
        }

        @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
        public void testRemoveNoSuchElement() {
            tree.add(1);
            tree.remove(2);
        }

        @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
        public void testRemoveEmpty() {
            tree.remove(1);
        }

        @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
        public void testGetNull() {
            tree.add(1);
            tree.get(null);
        }

        @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
        public void testGetNoSuchElement() {
            tree.add(1);
            tree.get(2);
        }

        @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
        public void testGetEmpty() {
            tree.get(1);
        }

        @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
        public void testContainsNull() {
            tree.add(1);
            tree.contains(null);
        }

    }

    public static class testEmpty {

        private static final int TIMEOUT = 200;
        private BST<Integer> tree;

        @Before
        public void setup() {
            tree = new BST<>();
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(testingTools.binaryTreeVisualizer(tree));
            }
        }

        @Test(timeout = TIMEOUT)
        public void testAddEmpty() {
            tree.add(1);
            assertEquals((Integer) 1, tree.getRoot().getData());
            assertEquals(1, tree.size());
        }

        @Test(timeout = TIMEOUT)
        public void testContainsEmpty() {
            assertEquals(false, tree.contains(1));
        }

        @Test(timeout = TIMEOUT)
        public void testPreorderEmpty() {
            List<Integer> expected = new ArrayList<Integer>();
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.preorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testInorderEmpty() {
            List<Integer> expected = new ArrayList<Integer>();
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.inorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testPostorderEmpty() {
            List<Integer> expected = new ArrayList<Integer>();
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.postorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLevelorderEmpty() {
            List<Integer> expected = new ArrayList<Integer>();
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.levelorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testHeightEmpty() {
            assertEquals(-1, tree.height());
        }

        @Test(timeout = TIMEOUT)
        public void testClearEmpty() {
            tree.clear();
            assertEquals(0, tree.size());
            assertEquals(null, tree.getRoot());
        }

        @Test(timeout = TIMEOUT)
        public void testGetMaxDataPerLevelEmpty() {
            List<Integer> expected = new ArrayList<Integer>();
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.getMaxDataPerLevel()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

    }

    public static class testSize1 {

        private static final int TIMEOUT = 200;
        private BST<Integer> tree;

        @Before
        public void setup() {
            tree = new BST<>();
            tree.add(1);
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(testingTools.binaryTreeVisualizer(tree));
            }
        }

        @Test(timeout = TIMEOUT)
        public void testAddSize1() {
            tree.add(2);
            assertEquals((Integer) 1, tree.getRoot().getData());
            assertEquals((Integer) 2, tree.getRoot().getRight().getData());
            assertEquals(2, tree.size());
        }

        @Test(timeout = TIMEOUT)
        public void testAddDuplicateSize1() {
            tree.add(1);
            assertEquals((Integer) 1, tree.getRoot().getData());
            assertEquals(1, tree.size());
            assertEquals(0, tree.height());
        }

        @Test(timeout = TIMEOUT)
        public void testRemoveSize1() {
            tree.remove(1);
            assertEquals(0, tree.size());
            assertEquals(null, tree.getRoot());
        }

        @Test(timeout = TIMEOUT)
        public void testGetSize1() {
            assertEquals((Integer) 1, tree.get(1));
        }

        @Test(timeout = TIMEOUT)
        public void testContainsSize1() {
            assertEquals(true, tree.contains(1));
            assertEquals(false, tree.contains(2));
        }

        @Test(timeout = TIMEOUT)
        public void testPreorderSize1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.preorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testInorderSize1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.inorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testPostorderSize1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.postorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLevelorderSize1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.levelorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testHeightSize1() {
            assertEquals(0, tree.height());
        }

        @Test(timeout = TIMEOUT)
        public void testClearSize1() {
            tree.clear();
            assertEquals(0, tree.size());
            assertEquals(null, tree.getRoot());
        }

        @Test(timeout = TIMEOUT)
        public void testGetMaxDataPerLevelSize1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.getMaxDataPerLevel()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

    }

    public static class testSize2 {

        private static final int TIMEOUT = 200;
        private BST<Integer> tree;

        /*
         Tree Shape:

                3
              /
            1

         */

        @Before
        public void setup() {
            tree = new BST<>();
            tree.add(3);
            tree.add(1);
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(testingTools.binaryTreeVisualizer(tree));
            }
        }

        @Test(timeout = TIMEOUT)
        public void testAddSize2Left() {
            tree.add(0);
            assertEquals((Integer) 0, tree.getRoot().getLeft().getLeft().getData());
            assertEquals(2, tree.height());
            assertEquals(3, tree.size());
        }

        @Test(timeout = TIMEOUT)
        public void testAddSize2Right() {
            tree.add(5);
            assertEquals((Integer) 5, tree.getRoot().getRight().getData());
            assertEquals(1, tree.height());
            assertEquals(3, tree.size());
        }

        @Test(timeout = TIMEOUT)
        public void testAddSize2Middle() {
            tree.add(2);
            assertEquals((Integer) 2, tree.getRoot().getLeft().getRight().getData());
            assertEquals(2, tree.height());
            assertEquals(3, tree.size());
        }

        @Test(timeout = TIMEOUT)
        public void testRemoveSize2Child() {
            tree.remove(1);
            assertNull(tree.getRoot().getLeft());
            assertEquals(0, tree.height());
            assertEquals((Integer) 3, tree.getRoot().getData());
        }

        @Test(timeout = TIMEOUT)
        public void testRemoveSize2Root() {
            tree.remove(3);
            assertNull(tree.getRoot().getLeft());
            assertNull(tree.getRoot().getRight());
            assertEquals(0, tree.height());
            assertEquals((Integer) 1, tree.getRoot().getData());
        }

        @Test(timeout = TIMEOUT)
        public void testDoubleRemoveSize2() {
            tree.remove(3);
            tree.remove(1);
            assertNull(tree.getRoot());
            assertEquals(0, tree.size());
        }

        @Test(timeout = TIMEOUT)
        public void testGetSize2() {
            assertEquals((Integer) 3, tree.get(3));
            assertEquals((Integer) 1, tree.get(1));
        }

        @Test(timeout = TIMEOUT)
        public void testContainsSize2() {
            assertTrue(tree.contains(1));
            assertTrue(tree.contains(3));
        }

        @Test(timeout = TIMEOUT)
        public void testPreorderSize2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(3);
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.preorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testInorderSize2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            expected.add(3);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.inorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testPostorderSize2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            expected.add(3);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.postorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLevelorderSize2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(3);
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.levelorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testHeightSize2() {
            assertEquals(1, tree.height());
        }

        @Test(timeout = TIMEOUT)
        public void testSizeSize2() {
            assertEquals(2, tree.size());
        }

        @Test(timeout = TIMEOUT)
        public void testGetMaxDataPerLevelSize2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(3);
            expected.add(1);
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.getMaxDataPerLevel()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }
    }

    public static class testTraversals1 {

        private static final int TIMEOUT = 200;
        private BST<Integer> tree;

        @Before
        public void setup() {
            Integer[] toAdd = new Integer[]{100, 50, 150, 25, 75, 125, 175};
            tree = new BST<Integer>(Arrays.asList(toAdd));
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(testingTools.binaryTreeVisualizer(tree));
            }
        }

        @Test(timeout = TIMEOUT)
        public void testPreorderTraversal1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{100, 50, 25, 75, 150, 125, 175}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.preorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testInorderTraversal1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{25, 50, 75, 100, 125, 150, 175}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.inorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testPostorderTraversal1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{25, 75, 50, 125, 175, 150, 100}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.postorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLevelorderTraversal1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{100, 50, 150, 25, 75, 125, 175}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.levelorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testGetMaxDataPerLevel1() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{100, 150, 175}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.getMaxDataPerLevel()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }
    }

    public static class testTraversals2 {

        private static final int TIMEOUT = 200;
        private BST<Integer> tree;

        @Before
        public void setup() {
            Integer[] toAdd = new Integer[]{100, 90, 80, 70, 60, 50, 120, 110, 75};
            tree = new BST<Integer>(Arrays.asList(toAdd));
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(testingTools.binaryTreeVisualizer(tree));
            }
        }

        @Test(timeout = TIMEOUT)
        public void testPreorderTraversal2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{100, 90, 80, 70, 60, 50, 75, 120, 110}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.preorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testInorderTraversal2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{50, 60, 70, 75, 80, 90, 100, 110, 120}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.inorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testPostorderTraversal2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{50, 60, 75, 70, 80, 90, 110, 120, 100}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.postorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLevelorderTraversal2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{100, 90, 120, 80, 110, 70, 60, 75, 50}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.levelorder()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testGetMaxDataPerLevel2() {
            List<Integer> expected = new ArrayList<Integer>();
            expected.addAll(Arrays.asList(new Integer[]{100, 120, 110, 70, 75, 50}));
            List<Integer> actual = new ArrayList<Integer>();
            for (Integer i : tree.getMaxDataPerLevel()) {
                actual.add(i);
            }

            assertEquals(expected, actual);
        }
    }

    public static class testRemove {

        private BST<Integer> tree;
        private static final int TIMEOUT = 200;
        private String expected;
        private String actual;

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(actual);
                System.out.println("Expected:");
                System.out.println(expected);
            }
        }

        @Test
        public void testRemove1() {
            Integer[] toAdd = new Integer[]{100, 90, 80, 70, 60, 50, 120, 110, 75};
            tree = new BST<Integer>(Arrays.asList(toAdd));

            tree.remove(100);
            expected = "[[110], [90, 120], [80, null, null, null], [70, null, null, null, null, null, null, null], [60, 75, null, null, null, null, null, null, null, null, null, null, null, null, null, null], [50, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual First:");
                System.out.println(actual);
                System.out.println("Expected First:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(120);
            expected = "[[110], [90, null], [80, null, null, null], [70, null, null, null, null, null, null, null], [60, 75, null, null, null, null, null, null, null, null, null, null, null, null, null, null], [50, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test
        public void testRemove2() {
            Integer[] toAdd = new Integer[]{10, 5, 15};
            tree = new BST<Integer>(Arrays.asList(toAdd));

            tree.remove(5);
            expected = "[[10], [null, 15]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual First:");
                System.out.println(actual);
                System.out.println("Expected First:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(10);
            expected = "[[15]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual Second:");
                System.out.println(actual);
                System.out.println("Expected Second:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(15);
            assertNull(tree.getRoot());
            assertEquals(0, tree.size());
        }

        @Test
        public void testRemove3() {
            Integer[] toAdd = new Integer[]{200, 100, 300, 50, 150, 250, 350, 25, 75, 125, 175, 225, 275, 325, 375};
            tree = new BST<Integer>(Arrays.asList(toAdd));

            tree.remove(200);
            expected = "[[225], [100, 300], [50, 150, 250, 350], [25, 75, 125, 175, null, 275, 325, 375]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual First:");
                System.out.println(actual);
                System.out.println("Expected First:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(100);
            expected = "[[225], [125, 300], [50, 150, 250, 350], [25, 75, null, 175, null, 275, 325, 375]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual Second:");
                System.out.println(actual);
                System.out.println("Expected Second:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(225);
            expected = "[[250], [125, 300], [50, 150, 275, 350], [25, 75, null, 175, null, null, 325, 375]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual Third:");
                System.out.println(actual);
                System.out.println("Expected Third:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(375);
            expected = "[[250], [125, 300], [50, 150, 275, 350], [25, 75, null, 175, null, null, 325, null]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual Fourth:");
                System.out.println(actual);
                System.out.println("Expected Fourth:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(150);
            expected = "[[250], [125, 300], [50, 175, 275, 350], [25, 75, null, null, null, null, 325, null]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            if (visualize) {
                System.out.println("Actual Fifth:");
                System.out.println(actual);
                System.out.println("Expected Fifth:");
                System.out.println(expected);
                System.out.println();
            }

            tree.remove(250);
            expected = "[[275], [125, 300], [50, 175, null, 350], [25, 75, null, null, null, null, 325, null]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);

            assertEquals(9, tree.size());
            assertEquals(3, tree.height());
        }
    }
}