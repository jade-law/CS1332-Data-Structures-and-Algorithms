import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This is a basic set of unit tests for AVL.
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
public class AVLStudentTest {

    private static final int TIMEOUT = 200;
    private AVL<Integer> avl;

    @Before
    public void setup() {
        avl = new AVL<>();
    }

    @Test(timeout = TIMEOUT)
    public void testConstructorAndClear() {
        /*
                     1
                    / \
                   0   2
        */

        List<Integer> toAdd = new ArrayList<>();
        toAdd.add(1);
        toAdd.add(0);
        toAdd.add(2);
        avl = new AVL<>(toAdd);

        assertEquals(3, avl.size());
        AVLNode<Integer> root = avl.getRoot();
        assertEquals((Integer) 1, root.getData());
        assertEquals(1, root.getHeight());
        assertEquals(0, root.getBalanceFactor());
        AVLNode<Integer> left = root.getLeft();
        assertEquals((Integer) 0, left.getData());
        assertEquals(0, left.getHeight());
        assertEquals(0, left.getBalanceFactor());
        AVLNode<Integer> right = root.getRight();
        assertEquals((Integer) 2, right.getData());
        assertEquals(0, right.getHeight());
        assertEquals(0, right.getBalanceFactor());

        avl.clear();
        assertEquals(0, avl.size());
        assertNull(avl.getRoot());
    }

    @Test(timeout = TIMEOUT)
    public void testAdd() {
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

//        avl.add(2);
//        avl.add(1);
//        avl.add(0);

//        assertEquals(3, avl.size());
//        AVLNode<Integer> root = avl.getRoot();
//        assertEquals((Integer) 1, root.getData());
//        assertEquals(1, root.getHeight());
//        assertEquals(0, root.getBalanceFactor());
//        AVLNode<Integer> left = root.getLeft();
//        assertEquals((Integer) 0, left.getData());
//        assertEquals(0, left.getHeight());
//        assertEquals(0, left.getBalanceFactor());
//        AVLNode<Integer> right = root.getRight();
//        assertEquals((Integer) 2, right.getData());
//        assertEquals(0, right.getHeight());
//        assertEquals(0, right.getBalanceFactor());


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

        assertEquals(3, avl.size());

        assertEquals(3, avl.size());
        AVLNode<Integer> root = avl.getRoot();
        assertEquals((Integer) 1, root.getData());
        assertEquals(1, root.getHeight());
        assertEquals(0, root.getBalanceFactor());
        AVLNode<Integer> left = root.getLeft();
        assertEquals((Integer) 0, left.getData());
        assertEquals(0, left.getHeight());
        assertEquals(0, left.getBalanceFactor());
        AVLNode<Integer> right = root.getRight();
        assertEquals((Integer) 2, right.getData());
        assertEquals(0, right.getHeight());
        assertEquals(0, right.getBalanceFactor());
    }


    @Test(timeout = TIMEOUT)
    public void testRemove() {
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
        assertEquals(4, avl.size());
        AVLNode<Integer> root = avl.getRoot();
        assertEquals((Integer) 3, root.getData());
        assertEquals(2, root.getHeight());
        assertEquals(1, root.getBalanceFactor());
        AVLNode<Integer> left = root.getLeft();
        assertEquals((Integer) 2, left.getData());
        assertEquals(1, left.getHeight());
        assertEquals(1, left.getBalanceFactor());
        AVLNode<Integer> right = root.getRight();
        assertEquals((Integer) 4, right.getData());
        assertEquals(0, right.getHeight());
        assertEquals(0, right.getBalanceFactor());
        AVLNode<Integer> leftLeft = left.getLeft();
        assertEquals((Integer) 0, leftLeft.getData());
        assertEquals(0, leftLeft.getHeight());
        assertEquals(0, leftLeft.getBalanceFactor());
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
    public void testPredecessor() {
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

    @Test(timeout = TIMEOUT)
    public void testKSmallest() {
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
}
