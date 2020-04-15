import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Your implementation of an AVL.
 *
 * @author Cullen Bertram
 * @version 1.0
 * @userid cbertram8
 * @GTID 903399638
 *
 * Collaborators: I worked independently on this assignment.
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
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
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
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
        size = 0;
        for (T element: data) {
            if (data == null) {
                throw new IllegalArgumentException("Cannot use null data");
            }
            add(element);
            size++;
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
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        root = addHelper(data, root);
    }

    /**
     *  addHelper method
     * @param data The data we are adding
     * @param node The root usually, the node we start
     * @return the node we added at
     */
    private AVLNode<T> addHelper(T data, AVLNode<T> node) {
        AVLNode<T> curr = node;
        if (curr == null) {
            size++;
            return new AVLNode<T>(data);
        } else {
            if (data.compareTo(curr.getData()) > 0) {
                curr.setRight(addHelper(data, curr.getRight()));
            } else if (data.compareTo(curr.getData()) < 0) {
                curr.setLeft(addHelper(data, curr.getLeft()));
            } else {
                return curr;
            }
        }
        updateHeightBF(curr);
        return balance(curr);
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
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
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
        if (data == null) {
            throw new IllegalArgumentException("Cannot insert null data into data structure.");
        }
        AVLNode<T> dummy = new AVLNode<>(data);
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
    private AVLNode<T> removeHelper(AVLNode<T> node, T data, AVLNode<T> dummy) {
        if (node == null) {
            throw new NoSuchElementException("The element was not found");
        }
        if (data.compareTo(node.getData()) > 0) {
            node.setRight(removeHelper(node.getRight(), data, dummy));
        } else if (data.compareTo(node.getData()) < 0) {
            node.setLeft(removeHelper(node.getLeft(), data, dummy));
        } else {
            dummy.setData(node.getData());
            if (node.getLeft() != null && node.getRight() != null) {
                AVLNode<T> temp = new AVLNode<>(null);
                node.setLeft(getPredecessor(node.getLeft(), temp));
                node.setData(temp.getData());
            } else {
                if (node.getRight() == null) {
                    return node.getLeft();
                } else {
                    return node.getRight();
                }
            }
        }
        updateHeightBF(node);
        return balance(node);
    }
    /**
     * Helper method to get successor
     * @param node The node we are at
     * @param temp the node to store the data we need
     * @return The successor
     */
    private AVLNode<T> getPredecessor(AVLNode<T> node, AVLNode<T> temp) {
        if (node.getRight() == null) {
            temp.setData(node.getData());
            node.setRight(null);
            return node.getLeft();
        } else {
            node.setRight(getPredecessor(node.getRight(), temp));
            updateHeightBF(node);
            return balance(node);
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
    public T get(T data) {
        AVLNode<T> curr = root;
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
    private T getHelper(T data, AVLNode<T> node) {
        AVLNode<T> curr = node;
        if (curr == null) {
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
     * @param data the data to search for in the tree
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        AVLNode<T> curr = root;
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
    private boolean containsHelper(T data, AVLNode<T> node) {
        AVLNode<T> curr = node;
        if (curr == null) {
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
     * Returns the height of the root of the tree.
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        AVLNode<T> curr = root;
        return heightHelper(curr);
    }
    /**
     *
     * @param curr the node we are finding height of
     * @return the height
     */
    private int heightHelper(AVLNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        return curr.getHeight();
    }


    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        size = 0;
        root = null;
    }

//    /**
//     * Find all elements within a certain distance from the given data.
//     * "Distance" means the number of edges between two nodes in the tree.
//     *
//     * To do this, first find the data in the tree. Keep track of the distance
//     * of the current node on the path to the data (you can use the return
//     * value of a helper method to denote the current distance to the target
//     * data - but note that you must find the data first before you can
//     * calculate this information). After you have found the data, you should
//     * know the distance of each node on the path to the data. With that
//     * information, you can determine how much farther away you can traverse
//     * from the main path while remaining within distance of the target data.
//     *
//     * Use a HashSet as the Set you return. Keep in mind that since it is a
//     * Set, you do not have to worry about any specific order in the Set.
//     *
//     * Note: We recommend 2 helper methods:
//     * 1. One helper method should be for adding the nodes on the path (from
//     * the root to the node containing the data) to the Set (if they are within
//     * the distance). This helper method will also need to find the distance
//     * between each node on the path and the target data node.
//     * 2. One helper method should be for adding the children of the nodes
//     * along the path to the Set (if they are within the distance). The
//     * private method stub called elementsWithinDistanceBelow is intended to
//     * be the second helper method. You do NOT have to implement
//     * elementsWithinDistanceBelow. However, we recommend you use this method
//     * to help implement elementsWithinDistance.
//     *
//     * Ex:
//     * Given the following AVL composed of Integers
//     *              50
//     *            /    \
//     *         25      75
//     *        /  \     / \
//     *      13   37  70  80
//     *    /  \    \      \
//     *   12  15    40    85
//     *  /
//     * 10
//     * elementsWithinDistance(37, 3) should return the set {12, 13, 15, 25,
//     * 37, 40, 50, 75}
//     * elementsWithinDistance(85, 2) should return the set {75, 80, 85}
//     * elementsWithinDistance(13, 1) should return the set {12, 13, 15, 25}
//     *
//     * @param data     the data to begin calculating distance from
//     * @param distance the maximum distance allowed
//     * @return the set of all data within a certain distance from the given data
//     * @throws java.lang.IllegalArgumentException if data is null
//     * @throws java.util.NoSuchElementException   is the data is not in the tree
//     * @throws java.lang.IllegalArgumentException if distance is negative
//     */
//    public Set<T> elementsWithinDistance(T data, int distance) {
//        ewDP(data, distance, );
//    }

//    /**
//     * @param data
//     */
//    private int ewDP(T data, maxDist, AVLNode<T> curr, set) {
//        //Curr is my root that I will use to GO to the data
//        if (data.compareTo(curr.getData()) > 0) {
//            childDist = ewDP(data, maxDist, curr.getRight());
//        }
//        childDist = ewDP(data, maxDist, curr.left or curr.right); //Choose whichever that takes to data
//        currDist = childDist + 1;
//        if (currDist < or equals maxDist){
//            set.add(curr.data);
//        }
//        if (currDist < maxDist) {
//            ewDB(curr.left or curr.right, maxDist, currDist++, set);
//        }
//        return currDist;
//    }


//    /**
//     * You do NOT have to implement this method if you choose not to.
//     * However, this will help with the elementsWithinDistance method.
//     *
//     * Adds data to the Set if the current node is within the maximum distance
//     * from the target node. Recursively call on the current node's children to
//     * add their data too if the children's data are also within the maximum
//     * distance from the target node.
//     *
//     * @param curNode         the current node
//     * @param maximumDistance the maximum distance allowed
//     * @param currentDistance the distance between the current node and the
//     *                        target node
//     * @param currentResult   the current set of data within the maximum
//     *                        distance
//     */
//    private void elementsWithinDistanceBelow(AVLNode<T> curNode,
//                                             int maximumDistance,
//                                             int currentDistance,
//                                             Set<T> currentResult) {
//        if (currentDistance <= maximumDistance) {
//            currentResult.add(curNode.getData());
//        }
//        if (currentDistance < maximumDistance) {
//            if (curNode.getLeft() != null) {
//                elementsWithinDistanceBelow(curNode.getLeft(), maximumDistance, currentDistance++, currentResult);
//            } else {
//                elementsWithinDistanceBelow(curNode.getRight(), maximumDistance, currentDistance++, currentResult);
//            }
//        }
//    }

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

    /**
     * Updates height and balance
     * @param node the root we are starting at to update the trees height and balance foctor
     *
     */
    private void updateHeightBF(AVLNode<T> node) {
        AVLNode<T> curr = node;
        int leftHeight = heightHelper(curr.getLeft());
        int rightHeight = heightHelper(curr.getRight());
        curr.setHeight(Math.max(leftHeight, rightHeight) + 1);
        curr.setBalanceFactor(heightHelper(curr.getLeft()) - heightHelper(curr.getRight()));
    }

    /**
     * Checks balance and does rotations
     * @param node the node we are checking
     * @return the balanced tree
     */
    private AVLNode<T> balance(AVLNode<T> node) {
        AVLNode<T> curr = node;
        if (curr.getBalanceFactor() > 1) {
            if (curr.getLeft().getBalanceFactor() < 0) {
                curr.setLeft(leftRotation(curr.getLeft()));
            }
            curr = rightRotation(curr);
        } else if (curr.getBalanceFactor() < -1) {
            if (curr.getRight().getBalanceFactor() > 0) {
                curr.setRight(rightRotation(node.getRight()));
            }
            curr = leftRotation(curr);
        }
        return curr;
    }
    /**
     * Left rotation
     * @param node the node we are rotating from
     * @return the rotated node i.e. the parent of the rotation
     */
    private AVLNode<T> leftRotation(AVLNode<T> node) {
        AVLNode<T> curr = node;
        AVLNode<T> temp = curr.getRight();
        curr.setRight(temp.getLeft());
        temp.setLeft(curr);
        updateHeightBF(curr);
        updateHeightBF(temp);
        return temp;
    }
    /**
     * Left rotation
     * @param node the node we are rotating from
     * @return the rotated node i.e. the parent of the rotation
     */
    private AVLNode<T> rightRotation(AVLNode<T> node) {
        AVLNode<T> curr = node;
        AVLNode<T> temp = curr.getLeft();
        curr.setLeft(temp.getRight());
        temp.setRight(curr);
        updateHeightBF(curr);
        updateHeightBF(temp);
        return temp;
    }
}
