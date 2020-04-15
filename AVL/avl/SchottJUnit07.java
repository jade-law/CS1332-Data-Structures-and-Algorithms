import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.util.*;

import static org.junit.Assert.*;

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
 * Cases when size = 0, size = 1
 * Basic and Complex Rotations
 * Removing (Including root and removing that requires rotations)
 * Elements Within Distance Tests
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
 * This tool is not meant to replace the TAs visualization tool, rather is to help you see what your code
 * is actually doing. I recommend using this tool in conjunction with the TAs viz tool to see what your
 * code is supposed to be doing (their tool) vs what it is actually doing (this tool).
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

@RunWith(SchottJUnit07.class)
@Suite.SuiteClasses({ SchottJUnit07.TestExceptions.class, SchottJUnit07.TestEmpty.class, SchottJUnit07.TestSize1.class,
    SchottJUnit07.TestSmallRotations.class, SchottJUnit07.TestLargerRotations.class, SchottJUnit07.TestRemove.class,
    SchottJUnit07.TestElementsWithinDistanceFromExample.class, SchottJUnit07.TestElementsWithinDistanceMore.class })
public class SchottJUnit07 extends Suite {

    /**
     * Make this true to have visualization arrays printed after each test
     * I recommend that you turn if to FALSE unless you are running 1 test at a time
     */
    public static final boolean visualize = false;

    public SchottJUnit07(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    // Ignore this: allows the visualization tool to work
    // Relies on your height method working correctly
    public static class testingTools {
        public static String binaryTreeVisualizer(AVL tree) {
            int height = tree.height();

            Object[][] toReturn = new Object[height + 1][];

            for (int i = 0; i <= height; i++) {
                Object[] thisLevel = new Object[(int) Math.pow(2, i)];
                toReturn[i] = thisLevel;
            }

            binaryTreeVisualizerHelper(tree.getRoot(), 0, 0, toReturn);

            return Arrays.deepToString(toReturn);
        }

        private static void binaryTreeVisualizerHelper(AVLNode curr, int depth, int shift, Object[][] toReturn) {
            if (curr != null) {
                toReturn[depth][shift] = curr.getData();
                binaryTreeVisualizerHelper(curr.getLeft(), depth + 1, shift * 2, toReturn);
                binaryTreeVisualizerHelper(curr.getRight(), depth + 1, (shift * 2) + 1, toReturn);
            }
        }
    }

    public static class TestExceptions {

        private static final int TIMEOUT = 200;
        private AVL<Integer> tree;

        @Before
        public void setup() {
            tree = new AVL<>();
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

//        @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
//        public void testElementsWithinDistanceNullData() {
//            tree.add(1);
//            tree.elementsWithinDistance(null, 1);
//        }
//
//        @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
//        public void testElementsWithinDistanceNegativeDistance() {
//            tree.add(1);
//            tree.elementsWithinDistance(1, -1);
//        }
//
//        @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
//        public void testElementsWithinDistanceNotInTree() {
//            tree.add(1);
//            tree.elementsWithinDistance(2, 1);
//        }
//
//        @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
//        public void testElementsWithinDistanceNotInTreeEmpty() {
//            tree.elementsWithinDistance(2, 1);
//        }
    }

    public static class TestEmpty {
        private static final int TIMEOUT = 200;
        private AVL<Integer> tree;

        @Before
        public void setup() {
            tree = new AVL<>();
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
        public void testHeightEmpty() {
            assertEquals(-1, tree.height());
        }

        @Test(timeout = TIMEOUT)
        public void testClearEmpty() {
            tree.clear();
            assertEquals(0, tree.size());
            assertEquals(null, tree.getRoot());
        }
    }

    public static class TestSize1 {

        private static final int TIMEOUT = 200;
        private AVL<Integer> tree;

        @Before
        public void setup() {
            tree = new AVL<>();
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
        public void testHeightSize1() {
            assertEquals(0, tree.height());
        }

        @Test(timeout = TIMEOUT)
        public void testClearSize1() {
            tree.clear();
            assertEquals(0, tree.size());
            assertEquals(null, tree.getRoot());
        }

//        @Test(timeout=TIMEOUT)
//        public void testElementsWithinDistance0() {
//            Set<Integer> expected = new HashSet<Integer>();
//            expected.add(1);
//
//            assertEquals(expected, tree.elementsWithinDistance(1, 0));
//        }
//
//        @Test(timeout=TIMEOUT)
//        public void testElementsWithinDistance1() {
//            Set<Integer> expected = new HashSet<Integer>();
//            expected.add(1);
//
//            assertEquals(expected, tree.elementsWithinDistance(1, 1));
//        }
//
//        @Test(timeout=TIMEOUT)
//        public void testElementsWithinDistance10() {
//            Set<Integer> expected = new HashSet<Integer>();
//            expected.add(1);
//
//            assertEquals(expected, tree.elementsWithinDistance(1, 10));
//        }
    }

    public static class TestSmallRotations {
        private AVL<Integer> tree;
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

        @Test(timeout = TIMEOUT)
        public void testRightRotationAdd() {
            Integer[] toAdd = new Integer[]{2, 1, 0};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLeftRotationAdd() {
            Integer[] toAdd = new Integer[]{0, 1, 2};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testRightLeftRotationAdd() {
            Integer[] toAdd = new Integer[]{0, 2, 1};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLeftRightRotationAdd() {
            Integer[] toAdd = new Integer[]{2, 1, 0};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }


        @Test(timeout = TIMEOUT)
        public void testRightRotationRemove() {
            Integer[] toAdd = new Integer[]{2, 1, 3, 0};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            tree.remove(3);

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLeftRotationRemove() {
            Integer[] toAdd = new Integer[]{0, -1, 1, 2};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            tree.remove(-1);

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testRightLeftRotationRemove() {
            Integer[] toAdd = new Integer[]{0, -1, 2, 1};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            tree.remove(-1);

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testLeftRightRotationRemove() {
            Integer[] toAdd = new Integer[]{2, 3, 1, 0};
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            tree.remove(3);

            expected = "[[1], [0, 2]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }
    }

    /**
     * Make sure these tests pass before debugging TestElementsWithinDistance
     */
    public static class TestLargerRotations {
        private AVL<Integer> tree;
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

        /**
         * Adds an ordered list of data that will require many rotations
         * Set eachStepVizualize to print an array representation of your tree after each item added
         */
        boolean eachStepVizualize = false;
        @Test(timeout = TIMEOUT)
        public void testAddOrderedList1() {
            Integer[] toAdd = new Integer[]{ 25, 50, 75, 100, 125, 150, 175 };
            tree = new AVL<Integer>();
            for (Integer i : toAdd) {
                tree.add(i);
                if (eachStepVizualize) {
                    System.out.println(testingTools.binaryTreeVisualizer(tree));
                }
            }

            actual = testingTools.binaryTreeVisualizer(tree);
            expected = "[[100], [50, 150], [25, 75, 125, 175]]";

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testAddOrderedList() {
            Integer[] toAdd = new Integer[]{ 6, 12, 17, 25, 30, 37, 40, 50, 60, 65, 70, 75, 80, 85, 90, 100, 110, 115, 120, 125, 130, 135, 140, 150, 155, 160, 170, 175, 180, 185, 190 };
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            actual = testingTools.binaryTreeVisualizer(tree);
            expected = "[[100], [50, 150], [25, 75, 125, 175], [12, 37, 65, 85, 115, 135, 160, 185], [6, 17, 30, 40, 60, 70, 80, 90, 110, 120, 130, 140, 155, 170, 180, 190]]";

            assertEquals(expected, actual);
        }
    }

    /**
     * Make sure TestLargerRotations tests pass before debugging this class
     */
    public static class TestRemove {
        private static final int TIMEOUT = 200;
        private AVL<Integer> tree;
        private String expected;
        private String actual;

        @Before
        public void setup() {
            Integer[] toAdd = new Integer[]{ 6, 12, 17, 25, 30, 37, 40, 50, 60, 65, 70, 75, 80, 85, 90, 100, 110, 115, 120, 125, 130, 135, 140, 150, 155, 160, 170, 175, 180, 185, 190 };
            tree = new AVL<Integer>(Arrays.asList(toAdd));
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(actual);
                System.out.println("Expected:");
                System.out.println(expected);
            }
        }

        @Test(timeout = TIMEOUT)
        public void testRemoveLeaf() {
            tree.remove(70);

            expected = "[[100], [50, 150], [25, 75, 125, 175], [12, 37, 65, 85, 115, 135, 160, 185], [6, 17, 30, 40, 60, null, 80, 90, 110, 120, 130, 140, 155, 170, 180, 190]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testRemoveWithTwoChildrenLow() {
            tree.remove(115);

            expected = "[[100], [50, 150], [25, 75, 125, 175], [12, 37, 65, 85, 110, 135, 160, 185], [6, 17, 30, 40, 60, 70, 80, 90, null, 120, 130, 140, 155, 170, 180, 190]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testRemoveWithTwoChildrenHigh() {
            tree.remove(150);

            expected = "[[100], [50, 140], [25, 75, 125, 175], [12, 37, 65, 85, 115, 135, 160, 185], [6, 17, 30, 40, 60, 70, 80, 90, 110, 120, 130, null, 155, 170, 180, 190]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        // Example from 10am class 2/24/20
        @Test(timeout = TIMEOUT)
        public void testRemoveLeafRequiresRotation() {
            Integer[] toAdd = new Integer[]{ 20, 11, 35, 29, 40 };
            tree = new AVL<Integer>(Arrays.asList(toAdd));

            tree.remove(11);

            expected = "[[35], [20, 40], [null, 29, null, null]]";
            actual = testingTools.binaryTreeVisualizer(tree);

            assertEquals(expected, actual);
        }

        @Test(timeout = TIMEOUT)
        public void testRemoveRoot() {
            tree.remove(100);

            expected = "[[90], [50, 150], [25, 75, 125, 175], [12, 37, 65, 85, 115, 135, 160, 185], [6, 17, 30, 40, 60, 70, 80, null, 110, 120, 130, 140, 155, 170, 180, 190]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        // Removes the root enough times to require a rotation
        @Test(timeout = TIMEOUT)
        public void testRemoveRootMultiple() {
            tree.remove(100);
            tree.remove(90);
            tree.remove(85);

            expected = "[[80], [50, 150], [25, 65, 125, 175], [12, 37, 60, 75, 115, 135, 160, 185], [6, 17, 30, 40, null, null, 70, null, 110, 120, 130, 140, 155, 170, 180, 190]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

        //Keeps removing the root until a rotation in your predecessor method is required
        @Test(timeout = TIMEOUT)
        public void testRemoveRootUntilPredecessorRotation() {
            tree.remove(100);
            tree.remove(90);
            tree.remove(85);
            tree.remove(80);
            tree.remove(75);
            tree.remove(70);

            expected = "[[65], [25, 150], [12, 50, 125, 175], [6, 17, 37, 60, 115, 135, 160, 185], [null, null, null, null, 30, 40, null, null, 110, 120, 130, 140, 155, 170, 180, 190]]";
            actual = testingTools.binaryTreeVisualizer(tree);

            assertEquals(expected, actual);
        }

        //Keeps removing the root lots of times :)
        @Test(timeout = TIMEOUT)
        public void testRemoveRootExtreme() {
            tree.remove(100);
            tree.remove(90);
            tree.remove(85);
            tree.remove(80);
            tree.remove(75);
            tree.remove(70);
            tree.remove(65);
            tree.remove(60);
            tree.remove(50);
            tree.remove(40);
            tree.remove(37);
            tree.remove(30);

            expected = "[[150], [25, 175], [12, 125, 160, 185], [6, 17, 115, 135, 155, 170, 180, 190], [null, null, null, null, 110, 120, 130, 140, null, null, null, null, null, null, null, null]]";
            actual = testingTools.binaryTreeVisualizer(tree);
            assertEquals(expected, actual);
        }

    }

    /**
     * Uses the same tree and tests from the AVL.java javadocs for elementsWithinDistance
     */
    public static class TestElementsWithinDistanceFromExample {
        private static final int TIMEOUT = 200;
        private AVL<Integer> tree;

        @Before
        public void setup() {
            Integer[] toAdd = new Integer[]{ 50, 25, 75, 13, 37, 70, 80, 12, 15, 40, 85, 10 };
            tree = new AVL<Integer>(Arrays.asList(toAdd));
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(testingTools.binaryTreeVisualizer(tree));
            }
        }

//        @Test(timeout = TIMEOUT)
//        public void testElements1() {
//            Integer[] expectedInSet = new Integer[]{ 12, 13, 15, 25, 37, 40, 50, 75 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(37, 3));
//        }
//
//        @Test(timeout = TIMEOUT)
//        public void testElements2() {
//            Integer[] expectedInSet = new Integer[]{ 75, 80, 85 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(85, 2));
//        }
//
//        @Test(timeout = TIMEOUT)
//        public void testElements3() {
//            Integer[] expectedInSet = new Integer[]{ 12, 13, 15, 25 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(13, 1));
//        }
    }

    /**
     * Make sure TestLargerRotations tests pass before debugging this class
     */
    public static class TestElementsWithinDistanceMore {
        private static final int TIMEOUT = 200;
        private AVL<Integer> tree;

        @Before
        public void setup() {
            Integer[] toAdd = new Integer[]{ 6, 12, 17, 25, 30, 37, 40, 50, 60, 65, 70, 75, 80, 85, 90, 100, 110, 115, 120, 125, 130, 135, 140, 150, 155, 160, 170, 175, 180, 185, 190 };
            tree = new AVL<Integer>(Arrays.asList(toAdd));
        }

        @After
        public void visualize() {
            if (visualize) {
                System.out.println("Actual:");
                System.out.println(testingTools.binaryTreeVisualizer(tree));
            }
        }

//        @Test (timeout = TIMEOUT)
//        public void testElementsWithinDistance1() {
//            Integer[] expectedInSet = new Integer[]{ 50, 60, 65, 70, 75, 85 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(65, 2));
//        }
//
//        @Test (timeout = TIMEOUT)
//        public void testElementsWithinDistance2() {
//            Integer[] expectedInSet = new Integer[]{ 100, 50, 150, 160, 175, 185, 125, 110, 115, 120, 135, 130, 140 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(135, 4));
//        }
//
//        @Test (timeout = TIMEOUT)
//        public void testElementsWithinDistanceDistance0() {
//            Integer[] expectedInSet = new Integer[]{ 100 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(100, 0));
//        }
//
//        @Test (timeout = TIMEOUT)
//        public void testElementsWithinDistanceDistanceLarge() {
//            Integer[] expectedInSet = new Integer[]{ 6, 12, 17, 25, 30, 37, 40, 50, 60, 65, 70, 75, 80, 85, 90, 100, 110, 115, 120, 125, 130, 135, 140, 150, 155, 160, 170, 175, 180, 185, 190 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(185, 100));
//        }
//
//        @Test (timeout = TIMEOUT)
//        public void testElementsWithinDistanceDistance7() {
//            Integer[] expectedInSet = new Integer[]{ 12, 25, 37, 50, 65, 75, 85, 100, 110, 115, 120, 125, 130, 135, 140, 150, 155, 160, 170, 175, 180, 185, 190 };
//            Set<Integer> expected = new HashSet<Integer>(Arrays.asList(expectedInSet));
//
//            assertEquals(expected, tree.elementsWithinDistance(130, 7));
//        }
    }
}