import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
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
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        root = null;
        size = 0;
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot input null data.");
        }
        for (T d : data) {
            if (d == null) {
                throw new IllegalArgumentException("Cannot input null data.");
            }
            add(d);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot input null data.");
        } else {
            root = addHelper(root, data);
        }
    }

    private AVLNode<T> addHelper(AVLNode<T> curr, T data) {
        if (curr == null) {
            AVLNode<T> node = new AVLNode<>(data);
            size++;
            return node;
        } else {
            if (data.compareTo(curr.getData()) > 0) {
                curr.setRight(addHelper(curr.getRight(), data));
            } else if (data.compareTo(curr.getData()) < 0) {
                curr.setLeft(addHelper(curr.getLeft(), data));
            }
            update(curr);
            return balance(curr);
        }
    }

    private void update(AVLNode<T> curr) {
        int leftHeight = 0;
        int rightHeight = 0;
        if (curr.getLeft() == null) {
            leftHeight = -1;
        } else {
            leftHeight = curr.getLeft().getHeight();
        }
        if (curr.getRight() == null) {
            rightHeight = -1;
        } else {
            rightHeight = curr.getRight().getHeight();
        }
        curr.setHeight(Math.max(leftHeight, rightHeight) + 1);
        curr.setBalanceFactor(leftHeight - rightHeight);
    }

    private AVLNode<T> balance(AVLNode<T> curr) {
        if (curr.getBalanceFactor() > 1) {
            if (curr.getLeft().getBalanceFactor() < 0) {
                curr.setLeft(rotateLeft(curr.getLeft()));
            }
            curr = rotateRight(curr);
        } else if (curr.getBalanceFactor() < -1) {
            if (curr.getRight().getBalanceFactor() > 0) {
                curr.setRight(rotateRight(curr.getRight()));
            }
            curr = rotateLeft(curr);
        }
        return curr;
    }

    private AVLNode<T> rotateLeft(AVLNode<T> node) {
        AVLNode<T> right = node.getRight();
        node.setRight(right.getLeft());
        right.setLeft(node);
        update(node);
        update(right);
        return right;
    }

    private AVLNode<T> rotateRight(AVLNode<T> node) {
        AVLNode<T> left = node.getLeft();
        node.setLeft(left.getRight());
        left.setRight(node);
        update(node);
        update(left);
        return left;
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data, NOT predecessor. As a reminder, rotations can occur
     * after removing the successor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        AVLNode<T> node = new AVLNode<>(null);//use update + rotates from above
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data.");
        } else {
            root = removeHelp(data, root, node);
            size--;
            return node.getData();
        }
    }

    private AVLNode<T> removeHelp(T data, AVLNode<T> curr, AVLNode<T> node) {               //check
        if (curr == null) {                                                                 //add update + rotates
            throw new NoSuchElementException("Data not found in tree.");
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(removeHelp(data, curr.getRight(), node));
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(removeHelp(data, curr.getLeft(), node));
        } else {
            node.setData(curr.getData());
            if (curr.getLeft() != null && curr.getRight() != null) {
                AVLNode<T> n = new AVLNode<>(null);
                curr.setRight(successor(curr.getRight(), n));
                curr.setData(n.getData());
            } else {
                if (curr.getLeft() == null && curr.getRight() != null) {
                    return curr.getRight();
                } else if (curr.getRight() == null) {
                    return curr.getLeft();
                }
            }
        }
        update(curr);
        return balance(curr);
    }

    private AVLNode<T> successor(AVLNode<T> curr, AVLNode<T> node) {
        if (curr.getLeft() == null) {
            node.setData(curr.getData());
            curr.setLeft(null);
            return curr.getRight();
        } else {
            curr.setLeft(successor(curr.getLeft(), node));
            update(curr);
            return balance(curr);
        }
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {                                                      //check
        if (data == null) {
            throw new IllegalArgumentException("Cannot get null data.");
        }
        if (!contains(data)) {
            throw new NoSuchElementException("Data not found in tree.");
        } else {
            AVLNode<T> node = getRecursion(root, data);
            return node.getData();
        }
    }

    private AVLNode<T> getRecursion(AVLNode<T> node, T data) {
        if (data.compareTo(node.getData()) > 0) {
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
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {                                       //check*******************
        if (data == null) {
            throw new IllegalArgumentException(
                    "Cannot get null data from tree");
        } else {
            return containsRecursion(data, root);
        }
    }

    private boolean containsRecursion(T data, AVLNode<T> node) {
        if (node == null) {
            return false;
        }
        if (data == node.getData()) {
            return true;
        } else if (data.compareTo(node.getData()) > 0) {
            return containsRecursion(data, node.getRight());
        } else if (data.compareTo(node.getData()) < 0) {
            return containsRecursion(data, node.getLeft());
        }
        return false;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        } else {
            return root.getHeight();
        }
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * In your BST homework, you worked with the concept of the predecessor, the
     * largest data that is smaller than the current data. However, you only
     * saw it in the context of the 2-child remove case.
     *
     * This method should retrieve (but not remove) the predecessor of the data
     * passed in. There are 2 cases to consider:
     * 1: The left subtree is non-empty. In this case, the predecessor is the
     * rightmost node of the left subtree.
     * 2: The left subtree is empty. In this case, the predecessor is the lowest
     * ancestor of the node containing data whose right child is also
     * an ancestor of data.
     *
     * This should NOT be used in the remove method.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *                    76
     *                  /    \
     *                34      90
     *                  \    /
     *                  40  81
     * predecessor(76) should return 40
     * predecessor(81) should return 76
     *
     * @param data the data to find the predecessor of
     * @return the predecessor of data. If there is no smaller data than the
     * one given, return null.
     * @throws java.lang.IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T predecessor(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data.");
        } else if (!contains(data)) {
            throw new NoSuchElementException("Data not found in tree");
        }
        AVLNode<T> node = getRecursion(root, data);
        if (node.getLeft() == null) {
            AVLNode<T> predecessor = preHelp2(root, node);;
            return predecessor.getData();
        } else {
            AVLNode<T> predecessor = preHelp1(node.getLeft());
            return predecessor.getData();
        }
    }

    private AVLNode<T> preHelp1(AVLNode<T> curr) {
        if (curr.getRight() == null) {
            return curr;
        } else {
            return preHelp1(curr.getRight());
        }
    }

    private AVLNode<T> preHelp2(AVLNode<T> curr, AVLNode<T> pre) {
        if (curr.getRight() == pre) {
            return curr;
        } else if (curr.getRight() != null) {
            if (curr.getRight().getLeft() == pre) {
                return curr;
            }
        }
        if (curr.getData().compareTo(pre.getData()) < 0){
            return preHelp2(curr.getRight(), pre);
        } else if (curr.getData().compareTo(pre.getData()) > 0) {
            return preHelp2(curr.getLeft(), pre);
        }
        return null;
    }

    /**
     * Finds and retrieves the k-smallest elements from the AVL in sorted order,
     * least to greatest.
     *
     * In most cases, this method will not need to traverse the entire tree to
     * function properly, so you should only traverse the branches of the tree
     * necessary to get the data and only do so once. Failure to do so will
     * result in an efficiency penalty.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *                50
     *              /    \
     *            25      75
     *           /  \     / \
     *          12   37  70  80
     *         /  \    \      \
     *        10  15    40    85
     *           /
     *          13
     * kSmallest(0) should return the list []
     * kSmallest(5) should return the list [10, 12, 13, 15, 25].
     * kSmallest(3) should return the list [10, 12, 13].
     *
     * @param k the number of smallest elements to return
     * @return sorted list consisting of the k smallest elements
     * @throws java.lang.IllegalArgumentException if k < 0 or k > n, the number
     *                                            of data in the AVL
     */
    public List<T> kSmallest(int k) {                                               //integrate k
        if (k < 0 || k > size) {
            throw new IllegalArgumentException("k is out of bounds.");
        }
        List<T> list = new ArrayList<>();
        return inOrderHelper(root, list, k);
    }

    private List<T> inOrderHelper(AVLNode<T> node, List<T> list, int k) {
        if (node == null) {
            return list;
        }
        if (list.size() >= k) {
            return list;
        } else {
            inOrderHelper(node.getLeft(), list, k);
            if (list.size() < k) {
                list.add(node.getData());
            }
            inOrderHelper(node.getRight(), list, k);
        }
        return list;
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
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
