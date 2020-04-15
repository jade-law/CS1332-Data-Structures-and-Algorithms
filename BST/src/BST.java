import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Your implementation of a BST.
 *
 * @author JADE LAW
 * @version 1.0
 * @userid jlaw39 (i.e. gburdell3)
 * @GTID 903437907 (i.e. 900000000)
 *
 * Collaborators: NONE
 *
 * Resources: NONE
 */
public class BST<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        root = null;
        size = 0;
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot make null tree");
        }
        for (T item: data) {
            add(item);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for a balanced tree and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Cannot input null data into tree");
        } else {
            root = addHelper(root, data);
        }
    }

    private BSTNode<T> addHelper(BSTNode<T> curr, T data) {
        if (curr == null) {
            BSTNode<T> node = new BSTNode<>(data);
            size++;
            return node;
        } else {
            if (data.compareTo(curr.getData()) > 0) {
                curr.setRight(addHelper(curr.getRight(), data));
            } else if (data.compareTo(curr.getData()) < 0) {
                curr.setLeft(addHelper(curr.getLeft(), data));
            }
            return curr;
        }
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its
     * child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data.
     * You MUST use recursion to find and remove the predecessor (you will
     * likely need an additional helper method to handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for a balanced tree and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        BSTNode<T> node = new BSTNode<>(data);
        if (data == null) {
            throw new IllegalArgumentException(
                    "Cannot remove null data from tree");
        }
        root = removeHelp(data, root, node);
        size--;
        return node.getData();
    }

    private BSTNode<T> removeHelp(T data, BSTNode<T> curr, BSTNode<T> node) {
        if (curr == null) {
            throw new NoSuchElementException("Data not found in tree.");
        }
        if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(removeHelp(data, curr.getRight(), node));
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(removeHelp(data, curr.getLeft(), node));
        } else {
            node.setData(curr.getData());
            if (curr.getLeft() != null && curr.getRight() != null) {
                BSTNode<T> n = new BSTNode<>(null);
                curr.setLeft(predecessor(curr.getLeft(), n));
                curr.setData(n.getData());
            } else {
                if (curr.getLeft() != null) {
                    return curr.getLeft();
                } else {
                    return curr.getRight();
                }
            }
        }
        return curr;
    }

    private BSTNode<T> predecessor(BSTNode<T> curr, BSTNode<T> n) {
        if (curr.getRight() == null) {
            n.setData(curr.getData());
            return curr.getLeft();
        }
        curr.setRight(predecessor(curr.getRight(), n));
        return curr;
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for a balanced tree and O(n) for worst case.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Cannot get null data from tree");
        } else {
            BSTNode<T> node = getRecursion(root, data);
            return node.getData();
        }
    }

    private BSTNode<T> getRecursion(BSTNode<T> node, T data) {
        if (node == null) {
            throw new NoSuchElementException("Data not found");
        } else if (data.compareTo(node.getData()) > 0) {
            return getRecursion(node.getRight(), data);
        } else if (data.compareTo(node.getData()) < 0) {
            return getRecursion(node.getLeft(), data);
        } else {
            return node;
        }
    }


    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for a balanced tree and O(n) for worst case.
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Cannot get null data from tree");
        } else {
            return containsRecursion(data, root);
        }
    }

    private boolean containsRecursion(T data, BSTNode<T> node) {
        if (node != null) {
            if (node.getData().equals(data)) {
                return true;
            } else if (data.compareTo(node.getData()) > 0) {
                return containsRecursion(data, node.getRight());
            } else if (data.compareTo(node.getData()) < 0) {
                return containsRecursion(data, node.getLeft());
            }
        }
        return false;
    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> list = new ArrayList<>();
        preOrderHelper(root, list);
        return list;
    }

    private void preOrderHelper(BSTNode<T> node, List<T> list) {
        if (node == null) {
            return;
        } else {
            list.add(node.getData());
            preOrderHelper(node.getLeft(), list);
            preOrderHelper(node.getRight(), list);
        }
    }

    /**
     * Generate a in-order traversal of the tree.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        return inOrderHelper(root, list);
    }

    private List<T> inOrderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            inOrderHelper(node.getLeft(), list);
            list.add(node.getData());
            inOrderHelper(node.getRight(), list);
        }
        return list;
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> list = new ArrayList<>();
        return postOrderHelper(root, list);
    }

    private List<T> postOrderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            postOrderHelper(node.getLeft(), list);
            postOrderHelper(node.getRight(), list);
            list.add(node.getData());
        }
        return list;
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        List<T> list = new ArrayList<>();
        Queue<BSTNode<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode<T> curr = queue.peek();
            if (curr == null) {
                return list;
            }
            if (curr.getLeft() != null && curr.getRight() != null) {
                queue.add(curr.getLeft());
                queue.add(curr.getRight());
                list.add(curr.getData());
            } else if (curr.getRight() != null) {
                queue.add(curr.getRight());
                list.add(curr.getData());
            } else if (curr.getLeft() != null) {
                queue.add(curr.getLeft());
                list.add(curr.getData());
            } else {
                list.add(curr.getData());
            }
            queue.remove();
        }
        return list;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child should be -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        } else {
            return heightHelp(root) - 1;
        }
    }

    private int heightHelp(BSTNode<T> curr) {
        if (curr == null) {
            return 0;
        } else {
            int left = heightHelp(curr.getLeft());
            int right = heightHelp(curr.getRight());
            if (left > right) {
                return left + 1;
            } else {
                return right + 1;
            }
        }
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Finds the path between two elements in the tree, specifically the path
     * from data1 to data2, inclusive of both.
     *
     * To do this, you must first find the deepest common ancestor of both data
     * and add it to the list. Then traverse to data1 while adding its ancestors
     * to the front of the list. Finally, traverse to data2 while adding its
     * ancestors to the back of the list. Please note that there is no
     * relationship between the data parameters in that they may not belong
     * to the same branch. You will most likely have to split off and
     * traverse the tree for each piece of data.
     **
     * You may only use 1 list instance to complete this method. Think about
     * what type of list to use since you will have to add to the front and
     * back of the list.
     *
     * This method only need to traverse to the deepest common ancestor once.
     * From that node, go to each data in one traversal each. Failure to do
     * so will result in a penalty.
     *
     * If both data1 and data2 are equal and in the tree, the list should be
     * of size 1 and contain the element from the tree equal to data1 and data2.
     *
     * Ex:
     * Given the following BST composed of Integers
     *                 50
     *             /        \
     *           25         75
     *         /    \
     *        12    37
     *       /  \    \
     *     10   15   40
     *         /
     *       13
     * findPathBetween(13, 40) should return the list [13, 15, 12, 25, 37, 40]
     * findPathBetween(50, 37) should return the list [50, 25, 37]
     * findPathBetween(75, 75) should return the list [75]
     *
     * Must be O(log n) for a balanced tree and O(n) for worst case.
     *
     * @param data1 the data to start the path from
     * @param data2 the data to end the path on
     * @return the unique path between the two elements
     * @throws java.lang.IllegalArgumentException if either data1 or data2 is
     *                                            null
     * @throws java.util.NoSuchElementException   if data1 or data2 is not in
     *                                            the tree
     */
    public List<T> findPathBetween(T data1, T data2) {
        if (data1 == null || data2 == null) {
            throw new IllegalArgumentException("Cannot find null data in tree");
        } else if ((!contains(data1)) || (!contains(data2))) {
            throw new NoSuchElementException("Data is not found in tree");
        } else {
            BSTNode<T> ancestor = new BSTNode<>(null);
            List<T> list = new ArrayList<T>();
            if (data1.compareTo(data2) > 0) {
                findCommon(data2, data1, root, ancestor);
            } else {
                findCommon(data1, data2, root, ancestor);
            }
            findPathRec(data1, ancestor, true, list, ancestor);
            findPathRec(data2, ancestor, false, list, ancestor);
            return list;
        }
    }

    private void findCommon(T lesser, T greater, BSTNode<T> curr, BSTNode<T> ancestor) {
        if (curr.getData().compareTo(lesser) >= 0 && curr.getData().compareTo(greater) <= 0) {
            ancestor.setData(curr.getData());
            ancestor.setRight(curr.getRight());
            ancestor.setLeft(curr.getLeft());
        } else if (curr.getData().compareTo(lesser) < 0) {
            findCommon(lesser, greater, curr.getRight(), ancestor);
        } else if (curr.getData().compareTo(greater) > 0) {
            findCommon(lesser, greater, curr.getLeft(), ancestor);
        } else if (curr.getData() == lesser) {
            ancestor.setData(curr.getData());
            ancestor.setRight(curr.getRight());
            ancestor.setLeft(curr.getLeft());
        } else if (curr.getData() == greater) {
            ancestor.setData(curr.getData());
            ancestor.setRight(curr.getRight());
            ancestor.setLeft(curr.getLeft());
        }
    }

    private void findPathRec(T data, BSTNode<T> curr, boolean isFirst, List<T> list, BSTNode<T> ancestor) {
        if (!isFirst) {
            if (curr.getData() != ancestor.getData()) {
                list.add(curr.getData());
            } else if (data.compareTo(curr.getData()) > 0) {
                findPathRec(data, curr.getRight(), false, list, ancestor);
            } else if (data.compareTo(curr.getData()) < 0) {
                findPathRec(data, curr.getLeft(), false, list, ancestor);
            }
        } else {
            list.add(0, curr.getData());
            if (data.compareTo(curr.getData()) > 0) {
                findPathRec(data, curr.getRight(), true, list, ancestor);
            } else if (data.compareTo(curr.getData()) < 0) {
                findPathRec(data, curr.getLeft(), true, list, ancestor);
            }
        }
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
