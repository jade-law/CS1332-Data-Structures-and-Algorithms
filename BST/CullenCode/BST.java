import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Your implementation of a BST.
 *
 * @author Cullen Bertram
 * @version 1.0
 * @userid cbertram8
 * @GTID 903399638
 *
 * Collaborators: I worked independently on this assignment.
 *
 * Resources: I only consulted the course materials.
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
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
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
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
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        for (T element: data) {
            if (element == null) {
                throw new IllegalArgumentException("The element is null and cannot be used");
            }
            addHelper(element, root);
            size++;
        }
    }

    /**
     * Adds the data to the tree.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        root = addHelper(data, root);
        size++;

    }
    /**
     *
     * @param data the data to search for
     * @param node the current node
     * @return the data in the tree equal to the parameter
     */
    private BSTNode<T> addHelper(T data, BSTNode<T> node) {
        BSTNode<T> curr = node;
        if (size == 0) {
            root = new BSTNode<T>(data);
            return root;
        }
        if (curr == null) {
            return new BSTNode<T>(data);
        } else {
            if (data.compareTo(curr.getData()) > 0) {
                curr.setRight(addHelper(data, curr.getRight()));
                return curr;
            } else if (data.compareTo(curr.getData()) < 0) {
                curr.setLeft(addHelper(data, curr.getLeft()));
                return curr;
            }
        }
        return null;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        BSTNode<T> dummy = new BSTNode<T>(data);
        root = removeHelper(root, data, dummy);
        size--;
        return dummy.getData();
    }

    /**
     * Helper method for remove: remokes the BST
     * @param node The current node we are at
     * @param data The data we want to remove
     * @param dummy Dummy node for storing data to remove
     * @return the reinforced tree for the root
     */
    private BSTNode<T> removeHelper(BSTNode<T> node, T data, BSTNode<T> dummy) {
        if (node == null) {
            throw new NoSuchElementException("The element was not found");
        }
        if (data.equals(node.getData())) {
            dummy.setData(data);
            if (node.getRight() == null && node.getLeft() == null) {
                return null;
            } else if (node.getLeft() != null && node.getRight() != null) {
                BSTNode<T> temp = new BSTNode<T>(null);
                node.setRight(getSuccessor(node.getRight(), temp));
                node.setData(temp.getData());
                return node;
            } else {
                if (node.getLeft() != null) {
                    return node.getLeft();
                } else {
                    return node.getRight();
                }
            }
        }
        if (data.compareTo(node.getData()) > 0) {
            node.setRight(removeHelper(node.getRight(), data, dummy));
            return node;
        } else if (data.compareTo(node.getData()) < 0) {
            node.setLeft(removeHelper(node.getLeft(), data, dummy));
            return node;
        }
        return node;
    }

    /**
     * Helper method to get successor
     * @param node The node we are at
     * @param temp the node to store the data we need
     * @return The successor
     */
    private BSTNode<T> getSuccessor(BSTNode<T> node, BSTNode<T> temp) {
        if (node.getLeft() == null) {
            temp.setData(node.getData());
            node.setLeft(null);
            return node.getRight();
        } else if (node.getLeft().getLeft() == null) {
            temp.setData(node.getLeft().getData());
            node.getLeft().setLeft(null);
            return node.getRight();
        } else {
            getSuccessor(node.getLeft(), temp);
        }
        return node.getRight();
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        BSTNode<T> curr = root;
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        if (curr == null) {
            throw new NoSuchElementException("The data was not found in the tree");
        }
        return getHelper(data, curr);

    }

    /**
     * Helper method for get method
     * @param data the data to search for
     * @param node the current node
     * @return the data in the tree equal to the parameter
     */
    private T getHelper(T data, BSTNode<T> node) {
        BSTNode<T> curr = node;
        if (curr.getData() == null) {
            throw new NoSuchElementException("The data was not found in the tree");
        }
        if (data.equals(curr.getData())) {
            return curr.getData();
        } else {
            if (data.compareTo(curr.getData()) > 0) {
                return getHelper(data, curr.getRight());
            } else if (data.compareTo(curr.getData()) < 0) {
                return getHelper(data, curr.getLeft());
            }
        }
        return curr.getData();
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        BSTNode<T> curr = root;
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        return containsHelper(data, curr);

    }
    /**
     *
     * @param data the data to search for
     * @param node the current node
     * @return the data in the tree equal to the parameter
     */
    private boolean containsHelper(T data, BSTNode<T> node) {
        BSTNode<T> curr = node;
        if (curr.getData() == null) {
            return false;
        }
        if (data.equals(curr.getData())) {
            return true;
        } else {
            if (data.compareTo(curr.getData()) > 0) {
                return containsHelper(data, curr.getRight());
            } else if (data.compareTo(curr.getData()) < 0) {
                return containsHelper(data, curr.getLeft());
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
        List<T> list = new ArrayList<T>();
        return preorderHelper(root, list);

    }

    /**
     *
     * @param node The node we are using to traverse
     * @param list The updated list of all of the data
     * @return The list we need
     */
    private List<T> preorderHelper(BSTNode<T> node, List<T> list) {
        if (node == null) {
            return null;
        }
        list.add(node.getData());
        preorderHelper(node.getLeft(), list);
        preorderHelper(node.getRight(), list);
        return list;
    }

    /**
     * Generate an in-order traversal of the tree.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> list = new ArrayList<T>();
        return inorderHelper(root, list);
    }

    /**
     * Helper method for in order
     * @param node The node we are using to traverse
     * @param list The updated list of all of the data
     * @return The list we need
     */
    private List<T> inorderHelper(BSTNode<T> node, List<T> list) {
        if (node == null) {
            return null;
        }
        inorderHelper(node.getLeft(), list);
        list.add(node.getData());
        inorderHelper(node.getRight(), list);
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
        List<T> list = new ArrayList<T>();
        return postorderHelper(root, list);

    }


    /**
     * Helper method for post order
     * @param node The node we are using to traverse
     * @param list The updated list of all of the data
     * @return The list we need
     */
    private List<T> postorderHelper(BSTNode<T> node, List<T> list) {
        if (node == null) {
            return null;
        }
        postorderHelper(node.getLeft(), list);
        postorderHelper(node.getRight(), list);
        list.add(node.getData());
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
        Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
        List<T> list = new ArrayList<T>(size);
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode<T> curr = queue.remove();
            if (curr != null) {
                list.add(curr.getData());
                queue.add(curr.getLeft());
                queue.add(curr.getRight());
            }
        }
        return list;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        BSTNode<T> curr = root;
        return heightHelper(curr);
    }

    /**
     *
     * @param curr the node we are finding heights of
     * @return the height
     */
    private int heightHelper(BSTNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        return Math.max(heightHelper(curr.getLeft()), heightHelper(curr.getRight())) + 1;
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
     * Generates a list of the max data per level from the top to the bottom
     * of the tree. (Another way to think about this is to get the right most
     * data per level from top to bottom.)
     *
     * This list should contain the last node of each level.
     *
     * If the tree is empty, an empty list should be returned.
     *
     * Ex:
     * Given the following BST composed of Integers
     *      2
     *    /   \
     *   1     4
     *  /     / \
     * 0     3   5
     * getMaxDataPerLevel() should return the list [2, 4, 5] - 2 is the max
     * data of level 0, 4 is the max data of level 1, and 5 is the max data of
     * level 2
     *
     * Ex:
     * Given the following BST composed of Integers
     *               50
     *           /        \
     *         25         75
     *       /    \
     *      12    37
     *     /  \    \
     *   10   15   40
     *  /
     * 13
     * getMaxDataPerLevel() should return the list [50, 75, 37, 40, 13] - 50 is
     * the max data of level 0, 75 is the max data of level 1, 37 is the
     * max data of level 2, etc.
     *
     * Must be O(n).
     *
     * @return the list containing the max data of each level
     */
    public List<T> getMaxDataPerLevel() {
        int count = 0;
        List<T> list = new ArrayList<T>(size);
        list.add(root.getData());

        maxDataHelper(root, count, list);
        return list;

    }

    /**
     *
     * @param node The node we are using to parse through
     * @param count The int to use reference by value to move through and check the list index
     * @param list The list storing the values
     */
    private void maxDataHelper(BSTNode<T> node, int count, List<T> list) {
        if (node == null) {
            return;
        }
        if (node.getRight() == null && node.getLeft() == null) {
            if (list.size() > count) {
                if (node.getData().compareTo(list.get(count)) > 0) {
                    list.set(count, node.getData());
                    return;
                }
            } else if (list.size() < count) {
                list.add(node.getData());
                return;
            }
        }
        if (list.size() > count) {
            if (node.getData().compareTo(list.get(count)) > 0) {
                list.set(count, node.getData());
            }
        } else if (list.size() <= count) {
            list.add(node.getData());
        }
        count = count + 1;
        maxDataHelper(node.getRight(), count, list);
        maxDataHelper(node.getLeft(), count, list);
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
