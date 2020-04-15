import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/*
Over the semester, I have gotten questions about why I continue to write
these tests. "The TAs have tests. People can write their own. Why do you
continue to spend all that time writing these?"

But my Lord and Savior, Jesus Christ, was clear: "'Truly I tell you, whatever
you did for one of the least of these brothers and sisters of mine, you did
for me'" (Matthew 25:40). He died to save me while I was still a sinner, in
rebellion against him. He loves everyone here incredibly deeply - no matter
what. So I continue writing JUnit tests to try to show that love in whatever
way I can.

And whatever grade you do (or don't) get, please remember this: "For God so
loved the world that he gave his one and only Son, that whoever believes in
him shall not perish but have eternal life" (John 3:16). Remember that you
are valuable not because of anything you have done, but because the God of
the universe made you, cares for you, and desperately wants you to know him.
 */

/**
 * This is a not-so-basic set of unit tests for graph algorithms. Please read
 * the Javadocs before asking on Piazza - I have attempted to document the
 * code and your questions may be answered. Note that the main test methods
 * have their own Javadocs too, explaining how they run.
 * <p>
 * These tests run 10000 random tests on graphs ranging from size 1 to 20. These
 * graphs are directed with parallel edges, self-loops, and disconnected nodes.
 * <p>
 * It checks:
 * * For a correct traversal/mapping for every algorithm
 * * That your code does not modify the graph
 * * Exceptions
 * * Kruskal's algorithm in the size-0 case
 * <p>
 * Things it does NOT check:
 * * Efficiency
 * * That your DFS is recursive
 * * In DFS and BFS, whether or not you iterate over the nodes in the exact
 * order returned by the adjacency list.
 * <p>
 * Using the IntelliJ debugger with random tests:
 * The Random object rand used to create the arrays for each test iteration is
 * reseeded each iteration by a random long derived from the test number.
 * The test number gets printed whenever a test fails. So to repeat a
 * specific test, just set testNumber to be the seed of the test you want to
 * run.
 * @author CS 1332 TAs + Peter Wilson
 * @version 1.2
 */
public class GraphAlgorithmsPeterTest {
    //Tests are at the bottom this time - all the machinery is first

    private static final int TEST_LOOPS = 10000;  //Number of tests to run

    private static final int SHORT_TIMEOUT = 200;
    private static final int LONG_TIMEOUT = 4000;  //For the long tests
    private static final int MAX_GRAPH_SIZE = 20;   //Biggest graph to test on
    private static final int MIN_GRAPH_SIZE = 1;   //Smallest graph to test on
    private static final int MAX_NODE_CONNECTIONS = 5;   //Most outbound links
    private static final int MIN_NODE_CONNECTIONS = 1;   //Fewest outbound links
    private static final Comparator<Vertex<String>> VERTEX_COMP =
            Comparator.comparing(Vertex::getData);
    private static final Comparator<VertexDistance<String>> VERTEX_DIST_COMP
            = (VertexDistance<String> vert1, VertexDistance<String> vert2) -> {
        int nodeComp = VERTEX_COMP.compare(vert1.getVertex(),
                vert2.getVertex());
        if (nodeComp == 0) {
            return vert1.getDistance() - vert2.getDistance();
        }
        return nodeComp;
    };
    private static Random rand;   //Generates random numbers for each tests
    private int testNumber; //Which number test we are on. For repeating tests

    private Graph<String> graph;
    private Vertex<String> startNode;
    private Map<Vertex<String>, Integer> dijkstraMap;
    private List<Vertex<String>> bfsList;
    private List<Vertex<String>> dfsList;
    private Set<Edge<String>> mst;
    /**
     * This method runs whenever your test fails. It prints useful debug data
     */
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            if (testNumber != -1) {
                System.out.println("Test number when test below failed: " + testNumber + " Use to repeat a specific test iteration in the debugger.");
            }
            if (graph != null) {
                System.out.println("Graph when test failed:");
                printGraph(graph);
            }
            if (startNode != null) {
                System.out.println("Start node for iteration:\n" + startNode);
            }
            if (dijkstraMap != null) {
                System.out.println("Your dijkstra distances:\n" + dijkstraMap);
            }
            if (bfsList != null) {
                System.out.println("Your BFS traversal:\n" + bfsList);
            }
            if (dfsList != null) {
                System.out.println("Your DFS traversal:\n" + dfsList);
            }
            if (mst != null) {
                System.out.println("WARNING: MST tests are invalid if your "
                        + "DFS is incorrect - it is used to check if the "
                        + "graph is connected.");
                System.out.println("Your Minimum Spanning Tree:\n" + edgeStr(mst));
            }
        }
    };

    /**
     * Shuffles the array. A wrapper for Collections.shuffle.
     * @param arr     array to shuffle
     * @param theRand random to use
     * @param <T>     type
     * @return shuffled array
     */
    private static <T> List<T> shuffleArr(T[] arr, Random theRand) {
        List<T> toShuffle = Arrays.asList(arr);
        Collections.shuffle(toShuffle, theRand);
        //Return as correct type
        return toShuffle;
    }

    /**
     * Generates a string from a number
     * <p>
     * 0 ->"A", 1 ->"B" ... 25 -> "Z", 26 ->"AA", 27 -> "AB" ...
     * @param occurrence number of times this value has come up.
     * @return string
     */
    private static String orderStr(int occurrence) {
        occurrence++;
        String stringRep = "";
        while (occurrence > 0) {
            // generate character from A-Z and add to stringRep
            char nextChar = (char) ((occurrence - 1) % 26 + 'A');
            stringRep = nextChar + stringRep;
            occurrence = (occurrence - 1) / 26;
        }
        return stringRep;
    }

    /**
     * Generates the inverse adjacency list for a graph. Instead of showing
     * which nodes you can get to from the current node, the inverse
     * adjacency list shows what nodes point to the current one.
     * @param theGraph graph to process
     * @param <T>      its type
     * @return inverse adjacency list
     */
    private static <T> Map<Vertex<T>, List<VertexDistance<T>>> invertAdjList(Graph<T> theGraph) {
        Set<Vertex<T>> vertices = theGraph.getVertices();
        Set<Edge<T>> edges = theGraph.getEdges();
        Map<Vertex<T>, List<VertexDistance<T>>> invAdjList =
                new HashMap<>(2 * theGraph.getAdjList().size());

        for (Vertex<T> v : vertices) {
            invAdjList.put(v, new ArrayList<>());
        }

        for (Edge<T> e : edges) {
            invAdjList.get(e.getV()).add(
                    new VertexDistance<>(e.getU(), e.getWeight()));

        }
        return invAdjList;
    }

    /**
     * Prints the adjacency list for the given graph. If you have suggestions
     * for more readable ways to do this, please post to Piazza.
     * @param toPrint graph to print
     */
    private static void printGraph(Graph<String> toPrint) {
        /*
        We convert the verticies to an array so that we can sort and print in
         a consistent order
         */
        Map<Vertex<String>, List<VertexDistance<String>>> adjList =
                toPrint.getAdjList();
        Vertex<String>[] verticies =
                toPrint.getVertices().toArray((Vertex<String>[]) new Vertex[0]);
        Arrays.parallelSort(verticies, VERTEX_COMP);

        for (Vertex<String> vertex : verticies) {
            List<VertexDistance<String>> edgesFrom = adjList.get(vertex);
            edgesFrom.sort(VERTEX_DIST_COMP);

            StringBuilder strEdgesFrom = vertexDistStr(edgesFrom);

            System.out.println(vertex + " : " + strEdgesFrom);
        }
    }

    /**
     * Converts the given collection of VertexDistances to a string
     * @param links VertexDistances to convert
     * @param <T>   type of data
     * @return string representation of
     */
    private static <T> StringBuilder vertexDistStr(Collection<VertexDistance<T>> links) {
        StringBuilder strEdges = new StringBuilder("[");
        for (VertexDistance<T> edge : links) {
            strEdges.append("(");
            strEdges.append(edge.getVertex());
            strEdges.append(":");
            strEdges.append(edge.getDistance());
            strEdges.append(")");
        }
        //Last ]
        strEdges.append("]");

        return strEdges;
    }

    /**
     * Converts the given collection of VertexDistances to a string
     * @param links VertexDistances to convert
     * @param <T>   type of data
     * @return string representation of
     */
    private static <T> StringBuilder edgeStr(Collection<Edge<T>> links) {
        if (links == null || links.isEmpty()) {
            return new StringBuilder("No edges");
        }
        StringBuilder strEdges = new StringBuilder("[");
        for (Edge<T> edge : links) {
            strEdges.append("(");
            strEdges.append(edge.getU());
            strEdges.append(":");
            strEdges.append(edge.getV());
            strEdges.append(":");
            strEdges.append(edge.getWeight());
            strEdges.append("), ");
        }
        //Change last comma to ]
        strEdges.replace(strEdges.length() - 2, strEdges.length(), "] ");

        return strEdges;
    }

    /**
     * It turned out that just seeding the random with a continually
     * increasing sequence caused it to not be random. This uses the MD5 hash
     * to generate pseudorandom seeds so random is a good pseudorandom number
     * generator.
     * @param loopNum int to stretch
     * @return a random seed
     */
    private static long randomSeedStretcher(int loopNum) {
        try {
            byte[] loopNumBytes = new byte[5];
            loopNumBytes[0] = (byte) (loopNum >> 24);
            loopNumBytes[1] = (byte) (loopNum >> 16);
            loopNumBytes[2] = (byte) (loopNum >> 8);
            loopNumBytes[3] = (byte) loopNum;
            loopNumBytes[4] = (byte) 42; //Salting it

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(loopNumBytes);

            long result = 0;
            for (int i = 0; i < 8; i++) {
                result += digest[i] << (8 * i);
            }
            return result;

        } catch (NoSuchAlgorithmException e) {
        }

        throw new IllegalStateException("This really shouldn't have happened "
                + "- I hard-coded that algorithm. Please report to Piazza.");
    }

    /**
     * Adds the VertexDistance links coming out from vertex to the Edge
     * collection addTo
     * @param adjList adjacency list
     * @param addTo   collection to add ot
     * @param vertex  vertex to start from
     * @param <T>     type of data
     */
    private static <T> void addToEdgeSet(Map<Vertex<T>,
            List<VertexDistance<T>>> adjList, Collection<Edge<T>> addTo,
                                         Vertex<T> vertex) {
        for (VertexDistance<T> linkOut : adjList.get(vertex)) {
            addTo.add(new Edge<>(vertex,
                    linkOut.getVertex(), linkOut.getDistance()));
        }
    }

    /**
     * A deep equals for maps. DOES NOT WORK for null values.
     * @param map1 first map
     * @param map2 second map
     * @param <K>  key type
     * @param <V>  value type
     * @return true if all keys
     */
    private static <K, V> boolean deepMapEquals(Map<K, V> map1,
                                                Map<K, V> map2) {
        if (map1 == map2) {
            return true;
        } else if (map1.size() != map2.size()) {
            return false;
        }
        for (Map.Entry<K, V> entry : map1.entrySet()) {
            V value1 = entry.getValue();
            V value2 = map2.get(entry.getKey());

            if (value1 == value2) {
                continue;
            } else if (value2 == null || !value1.equals(value2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a directed, randomly-connected graph. Verticies are lettered
     * A, B, C ... AA, AB ...
     * <p>
     * Graph is stored in graph field, the start node is stored in startNode.
     * @param numVerticies number of verticies in graph
     * @param theRand      random to use for consistency
     */
    private void genRandGraph(int numVerticies,
                              Random theRand) {
        Vertex<String>[] verticies = new Vertex[numVerticies];
        Set<Vertex<String>> vertexSet = new HashSet<>(2 * numVerticies);
        HashSet<Edge<String>> edges =
                new HashSet<>(numVerticies * MAX_NODE_CONNECTIONS);

        //Generate verticies with values A, B, C ... AA, AB
        for (int i = 0; i < verticies.length; i++) {
            Vertex<String> toAdd = new Vertex<>(orderStr(i));
            verticies[i] = toAdd;
            vertexSet.add(toAdd);
        }

        //Randomly connect the verticies
        for (Vertex<String> curr : verticies) {
            int numEdges =
                    theRand.nextInt(MAX_NODE_CONNECTIONS - MIN_NODE_CONNECTIONS) + MIN_NODE_CONNECTIONS;
            for (int i = 0; i < numEdges; i++) {
                Vertex<String> endpt = verticies[theRand.nextInt(numVerticies)];
                int weight = theRand.nextInt(numVerticies);
                edges.add(new Edge<>(curr, endpt, weight));
            }
        }
        graph = new Graph<>(vertexSet, edges);
        startNode = verticies[theRand.nextInt(verticies.length)];
    }

    //Begin tests
    //Test Dijkstra

    /**
     * Generates a undirected, randomly-connected graph. Verticies are lettered
     * A, B, C ... AA, AB ...
     * There are no duplicate edge weights so that MSTs are unique
     * <p>
     * Graph is stored in graph field, the start node is stored in startNode.
     * <p>
     * WE generate the graph by generating the verticies, shuffling them, and
     * then connecting them by edges with linearly increasing weights.
     * @param numVerticies number of verticies in graph
     * @param theRand      random to use for consistency
     */
    private void genUndirectedRandGraph(int numVerticies,
                                        Random theRand) {
        Vertex<String>[] verticies =
                (Vertex<String>[]) new Vertex[numVerticies];
        Set<Vertex<String>> vertexSet = new HashSet<>(2 * numVerticies);
        HashSet<Edge<String>> edges =
                new HashSet<>(numVerticies * MAX_NODE_CONNECTIONS);

        //Generate verticies with values A, B, C ... AA, AB
        for (int i = 0; i < verticies.length; i++) {
            Vertex<String> toAdd = new Vertex<>(orderStr(i));
            verticies[i] = toAdd;
            vertexSet.add(toAdd);
        }

        //Shuffle array so that we don't always have the smallest weights on
        // nodes at the front of the
        verticies = shuffleArr(verticies, theRand).toArray(verticies);

        //Randomly connect the verticies
        int weight = theRand.nextInt(1); //Randomly start at 0 or 1
        for (Vertex<String> curr : verticies) {
            //Divide by 2 since adding 2 edges - one forward and one back -
            // per loop
            int numEdges =
                    theRand.nextInt((MAX_NODE_CONNECTIONS - MIN_NODE_CONNECTIONS) / 2) + MIN_NODE_CONNECTIONS;
            for (int i = 0; i < numEdges; i++) {
                Vertex<String> endpt = verticies[theRand.nextInt(numVerticies)];
                edges.add(new Edge<>(curr, endpt, weight));
                edges.add(new Edge<>(endpt, curr, weight));
                weight++;
            }
        }

        graph = new Graph<>(vertexSet, edges);
        startNode = verticies[theRand.nextInt(verticies.length)];
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullGraphDijkstra() {
        genRandGraph(1, new Random(42));
        dijkstraMap = GraphAlgorithms.dijkstras(startNode, null);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullStartDijkstra() {
        genRandGraph(1, new Random(42));
        dijkstraMap = GraphAlgorithms.dijkstras(null, graph);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNonexistentStartDijkstra() {
        genRandGraph(4, new Random(42));
        dijkstraMap =
                GraphAlgorithms.dijkstras(new Vertex<>("Not in graph"), graph);
    }

    //Test BFS

    /**
     * For this test, we generate a random graph and start vertex and test
     * your code on it.
     * <p>
     * Note that if you are failing this test with the message "Wrong
     * djikstra distance", the actual problem may not be on the link that caused
     * the test to fail. This test does not generate its own Djikstra's map;
     * it checks yours for consistency with the requirements.
     * <p>
     * Checking your dijkstra's method involves iterating over every node.
     * * If a node is the start node, its weight must be 0
     * * If a node is has no path from the start, we check that every node that
     * has a path to it is also disconnected
     * * Else (normal case), we check that node's weight is the minimum of
     * the weights + distances to of its neighbors
     */
    @Test(timeout = LONG_TIMEOUT)
    public void testDijkstra() {
        Map<Vertex<String>, List<VertexDistance<String>>> invAdjList;
        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            dijkstraMap = null;
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            testNumber = 2531;

            rand = new Random(randomSeedStretcher(testNumber));

            //Start with small graphs
            int numVerticies =
                    testNumber * (MAX_GRAPH_SIZE - MIN_GRAPH_SIZE) / TEST_LOOPS + MIN_GRAPH_SIZE;

            genRandGraph(numVerticies, rand);
            invAdjList = invertAdjList(graph);

            //Probably bad but easily comprehensible way to save the state of
            // the adjacency list. If we just try to save the by creating a
            // new one, the underlying lists are shared between the maps.
            String adjListStr = graph.getAdjList().toString();

            dijkstraMap = GraphAlgorithms.dijkstras(startNode, graph);

            //Check you didn't modify the map
            assertEquals("Don't modify map", adjListStr,
                    graph.getAdjList().toString());


            //printGraph(graph);
            //System.out.println("");

            for (Map.Entry<Vertex<String>, Integer> entry
                    : dijkstraMap.entrySet()) {
                Vertex<String> node = entry.getKey();
                int distance = entry.getValue();

                if (node == startNode) {
                    //Check for start node
                    assertEquals("Start should have distance of 0", 0,
                            distance);
                } //else if (distance == Integer.MAX_VALUE) {
//                    //Check for disconnected nodes
//                    for (VertexDistance<String> edgeTo : invAdjList.get(node)) {
//                        Vertex<String> pathFrom = edgeTo.getVertex();
//                        assertEquals("Connected node: " + pathFrom + " Has "
//                                        + "path to node: " + node,
//                                Integer.MAX_VALUE,
//                                (int) dijkstraMap.get(pathFrom));
//                    }
//                }
                else {
                    //Normal case
                    long minDistTo = Integer.MAX_VALUE;
                    for (VertexDistance<String> edgeTo : invAdjList.get(node)) {
                        Vertex<String> pathFrom = edgeTo.getVertex();
                        long distTo =
                                (long) dijkstraMap.get(pathFrom) + edgeTo.getDistance();
                        if (distTo < minDistTo) {
                            minDistTo = distTo;
                        }
                    }
                    assertEquals("Wrong djikstra distance", minDistTo,
                            distance);
                }
            }
        }
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullGraphBFS() {
        genRandGraph(1, new Random(42));
        bfsList = GraphAlgorithms.bfs(startNode, null);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullStartBFS() {
        genRandGraph(1, new Random(42));
        bfsList = GraphAlgorithms.bfs(null, graph);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNonexistentStartBFS() {
        genRandGraph(4, new Random(42));
        bfsList = GraphAlgorithms.bfs(new Vertex<>("Not in graph"), graph);
    }

    /**
     * For this test, we generate a random graph and start vertex and test
     * your code on it.
     * <p>
     * This tests your code by "peeling the onion inside-out". We take the
     * prior "layer" of the search, see what nodes connect to it that we
     * haven't visited, and make that our current layer. Then we check that
     * the current layer is the next nodes in the bfsList. Then we make the
     * current layer the prior layer, and repeat.
     */
    @Test(timeout = LONG_TIMEOUT)
    public void testBFS() {
        Map<Vertex<String>, List<VertexDistance<String>>> adjList;
        HashSet<Vertex<String>> alreadyProcessed =
                new HashSet<>(2 * MAX_GRAPH_SIZE);
        HashSet<Vertex<String>> priorLayer = new HashSet<>(2 * MAX_GRAPH_SIZE);
        HashSet<Vertex<String>> currLayer = new HashSet<>(2 * MAX_GRAPH_SIZE);

        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            bfsList = null;
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            //Start with small graphs
            int numVerticies =
                    testNumber * (MAX_GRAPH_SIZE - MIN_GRAPH_SIZE) / TEST_LOOPS + MIN_GRAPH_SIZE;

            genRandGraph(numVerticies, rand);
            adjList = graph.getAdjList();

            //Probably bad but easily comprehensible way to save the state of
            // the adjacency list. If we just try to save the by creating a
            // new one, the underlying lists are shared between the maps.
            String adjListStr = adjList.toString();

            bfsList = GraphAlgorithms.bfs(startNode, graph);

            //Check you didn't modify the map
            assertEquals("Don't modify map", adjListStr,
                    graph.getAdjList().toString());
            /*
                System.out.println(testNumber);
                System.out.println(startNode);
                printGraph(graph);
                System.out.println(bfsList);
                System.out.println("");
            */

            //Check length
            int traversalLength = bfsList.size();
            assertTrue("Traversal is too long: " + traversalLength,
                    traversalLength <= numVerticies);

            Iterator<Vertex<String>> bfsIterator = bfsList.iterator();

            //Check start
            assertSame(startNode, bfsIterator.next());

            //Set up sets
            alreadyProcessed.clear();
            priorLayer.clear();
            currLayer.clear();

            priorLayer.add(startNode);
            alreadyProcessed.add(startNode);

            //We didn't find a path to any nodes we didn't already process
            boolean done = false;

            while (!done) {
                //Determine next layer in BFS iteration
                done = true;
                currLayer.clear();

                for (Vertex<String> curr : priorLayer) {
                    for (VertexDistance<String> link : adjList.get(curr)) {
                        Vertex<String> adjNode = link.getVertex();
                        //If not already traversed, add it to traversal
                        if (alreadyProcessed.add(adjNode)) {
                            currLayer.add(adjNode);
                            done = false;
                        }
                    }
                }

                priorLayer.clear();
                //Check next n items in BFS list are what we expect
                int itemsInLayer = currLayer.size();
                for (int i = 0; i < itemsInLayer; i++) {
                    assertTrue("Too few items in BFS", bfsIterator.hasNext());

                    Vertex<String> nextBFSnode = bfsIterator.next();
                    assertTrue("Node " + nextBFSnode + " too early in list. "
                                    + "Before it should be: " + currLayer,
                            currLayer.remove(nextBFSnode));

                    //Build prior layer for next time
                    priorLayer.add(nextBFSnode);
                }

            }
            assertEquals("Traversal has wrong number of items",
                    alreadyProcessed.size(), traversalLength);

        }
    }

    //Test DFS

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullGraphDFS() {
        genRandGraph(1, new Random(42));
        dfsList = GraphAlgorithms.dfs(startNode, null);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullStartDFS() {
        genRandGraph(1, new Random(42));
        dfsList = GraphAlgorithms.dfs(null, graph);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNonexistentStartDFS() {
        genRandGraph(4, new Random(42));
        dfsList = GraphAlgorithms.dfs(new Vertex<>("Not in graph"), graph);
    }

    /**
     * For this test, we generate a random graph and start vertex and test
     * your code on it.
     * <p>
     * For the DFS iteration, we traverse over the graph by fully exploring a
     * tree before we go on to the next one. This means that for the next
     * node in your dfsList, there are two options:
     * * There exists a link to it from the node right before it (we are
     * exploring down a branch)
     * * There exists a link to it from a node further back in the dfsList
     * (in which case we just finished the recursive call on the node before it)
     */
    @Test(timeout = LONG_TIMEOUT)
    public void testDFS() {
        Map<Vertex<String>, List<VertexDistance<String>>> adjList;
        HashSet<Vertex<String>> alreadyProcessed =
                new HashSet<>(2 * MAX_GRAPH_SIZE);
        HashMap<Vertex<String>, HashSet<Vertex<String>>> adjSet =
                new HashMap<>(2 * MAX_GRAPH_SIZE);
        HashSet<Vertex<String>> finishedCall =
                new HashSet<>(2 * MAX_GRAPH_SIZE);

        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            dfsList = null;
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            //Start with small graphs
            int numVerticies =
                    testNumber * (MAX_GRAPH_SIZE - MIN_GRAPH_SIZE) / TEST_LOOPS + MIN_GRAPH_SIZE;

            genRandGraph(numVerticies, rand);
            adjList = graph.getAdjList();

            //Probably bad but easily comprehensible way to save the state of
            // the adjacency list. If we just try to save the by creating a
            // new one, the underlying lists are shared between the maps.
            String adjListStr = adjList.toString();

            dfsList = GraphAlgorithms.dfs(startNode, graph);

            //Check you didn't modify the map
            assertEquals("Don't modify map", adjListStr,
                    graph.getAdjList().toString());

            //For O(1) access
            Vertex<String>[] dfsArr = dfsList.toArray(new Vertex[0]);

            /*
                System.out.println(testNumber);
                System.out.println(startNode);
                printGraph(graph);
                System.out.println(dfsList);
                System.out.println("");
            */

            //Check length
            int traversalLength = dfsList.size();
            assertTrue("Traversal is too long: " + traversalLength,
                    traversalLength <= numVerticies);

            //Check start
            assertSame(startNode, dfsArr[0]);

            //Set up sets + arrays
            alreadyProcessed.clear();
            alreadyProcessed.add(startNode);
            finishedCall.clear();

            //Create adjacency Set so we can see if 2 nodes are adjacent O(1)
            for (Vertex<String> node : graph.getVertices()) {
                HashSet<Vertex<String>> connectedSet =
                        new HashSet<>(2 * MAX_NODE_CONNECTIONS);
                for (VertexDistance<String> linkFrom : adjList.get(node)) {
                    connectedSet.add(linkFrom.getVertex());
                }
                adjSet.put(node, connectedSet);
            }

            //Iterate over list, making sure we know where every node came from
            for (int i = 1; i < dfsArr.length; i++) {
                Vertex<String> curr = dfsArr[i];
                assertTrue("Duplicate item in list" + curr,
                        alreadyProcessed.add(curr));
                int priorIndex = i - 1;
                Vertex<String> prior;
                boolean foundLink = false;

                while (!foundLink) {
                    assertTrue("Didn't find link to " + curr, priorIndex >= 0);
                    prior = dfsArr[priorIndex];
                    if (adjSet.get(prior).contains(curr)) {
                        //Node curr links to here
                        foundLink = true;

                        /*
                        This is an internal test consistency check - if a
                        prior node has a link to the current node, it
                        should not have been marked as finished
                         */
                        assertFalse(finishedCall.contains(prior));

                    } else if (!finishedCall.contains((prior))) {
                        //Curr didn't come from prior, so we need to
                        //check that the prior node had no more possible calls
                        for (VertexDistance<String> adjacentToPrior
                                : adjList.get(prior)) {
                            assertTrue("Found node not explored: " + prior,
                                    alreadyProcessed.contains(adjacentToPrior.getVertex()));
                        }
                        //Mark prior as finished
                        finishedCall.add(prior);
                    }
                    //Finished that node, check item before it (if needed).
                    priorIndex--;
                }
            }
            assertEquals("Traversal has wrong number of items",
                    alreadyProcessed.size(), traversalLength);
        }
    }

    //Test Kruskal's MST algorithm

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullGraphKruskals() {
        mst = GraphAlgorithms.kruskals(null);
    }

    //Per Piazza, we will not be tested on a graph of size 0,
    //so that test was removed.

    /**
     * For this test, we generate a undirected random graph and test your
     * code on it. We generate a graph with all different edge weights so
     * that the mst is unique
     * <p>
     * The test algorithm is a modified Prim's algorithm. We don't actually
     * build the minimum spanning tree, though.
     */
    @Test(timeout = LONG_TIMEOUT)
    public void testKruskals() {
        Map<Vertex<String>, List<VertexDistance<String>>> adjList;
        HashSet<Vertex<String>> alreadyProcessed =
                new HashSet<>(2 * MAX_GRAPH_SIZE);
        PriorityQueue<Edge<String>> smallestEdge =
                new PriorityQueue<>(MAX_NODE_CONNECTIONS * MAX_GRAPH_SIZE);

        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            mst = null;
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            //Start with small graphs
            int numVerticies =
                    testNumber * (MAX_GRAPH_SIZE - MIN_GRAPH_SIZE) / TEST_LOOPS + MIN_GRAPH_SIZE;

            genUndirectedRandGraph(numVerticies, rand);
            adjList = graph.getAdjList();

            //Clear the startNode so that we don't print it on failing
            Vertex<String> primStart = startNode;
            startNode = null;

            //Probably bad but easily comprehensible way to save the state of
            // the adjacency list. If we just try to save the by creating a
            // new one, the underlying lists are shared between the maps.
            String adjListStr = adjList.toString();

            mst = GraphAlgorithms.kruskals(graph);

            //Check you didn't modify the map
            assertEquals("Don't modify map", adjListStr,
                    graph.getAdjList().toString());

            /*
                System.out.println(testNumber);
                System.out.println(startNode);
                printGraph(graph);
                System.out.println(dfsList);
                System.out.println("");
            */

            //Check if the graph was not connected
            List<Vertex<String>> testTraversal =
                    GraphAlgorithms.dfs(primStart, graph);

            if (testTraversal.size() != numVerticies) {
                assertNull("Graph is not connected", mst);

            } else {
                //Graph is connected.
                assertNotNull("Graph is connected; a MST exists", mst);

                //Check length
                int mstLength = mst.size();
                assertEquals("Wrong length MST", 2 * (numVerticies - 1),
                        mstLength);


                //Set up
                alreadyProcessed.clear();
                alreadyProcessed.add(primStart);
                smallestEdge.clear();
                addToEdgeSet(adjList, smallestEdge, primStart);

                //Don't change original
                Set<Edge<String>> mstCopy = new HashSet<>(mst);

                Edge<String> minEdgeOut;
                Vertex<String> toClump;

                while (alreadyProcessed.size() < numVerticies) {
                    minEdgeOut = smallestEdge.remove();
                    toClump = minEdgeOut.getV();

                    if (alreadyProcessed.add(toClump)) {
                        //Found smallest link out of Prim's clump

                        //Remove, not just contains, so we can check if
                        // this test is trying the same edge twice
                        assertTrue("Should contain " + minEdgeOut,
                                mstCopy.remove(minEdgeOut));

                        //Reverse edge should also be in set
                        Edge<String> reverseEdge = new Edge<>(toClump,
                                minEdgeOut.getU(), minEdgeOut.getWeight());
                        assertTrue("Should contain " + reverseEdge,
                                mstCopy.remove(reverseEdge));

                        //Add its links to the list of links out of the edge set
                        addToEdgeSet(adjList, smallestEdge, toClump);

                    }
                }

                /*
                Your mst had the correct number of edges and the graph was
                connected, so the test algorithm should have removed all the
                edges from the mst or already thrown an error (if you had a
                wrong edge in the set). This test should always pass.

                Please post any errors here to Piazza - it probably indicates a
                flaw in the JUnit.
                 */
                assertTrue("Test consistency error", mstCopy.isEmpty());
            }
        }
    }

    @Before
    public void setUp() {
        /*
        We need a way to repeat a SPECIFIC test when a random test fails.
        Therefore, EVERY time we run a random test, we seed the test's random
        number generator with a function of which test number we are on
         */
        testNumber = -1;
    }

}