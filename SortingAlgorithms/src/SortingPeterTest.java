import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a not-so-basic set of unit tests for Sorting.
 * <p>
 * For each sort, the tests have 3 parts.
 * 1. Test how the sort performs on size 0 and 1 arrays
 * 2. Test exceptions
 * 3. Run TEST_LOOPS random tests on arrays from size-1 to size MAX_ARR_SIZE.
 * This is the main test method.
 * <p>
 * For every sort, we check if your array was correctly sorted.
 * <p>
 * For Insertion, Cocktail, and Merge sorts, we check stability.
 * For Selection and Quick sorts we check instability by seeing if you ever
 * fail a stability check (failing a stability check will result in passing
 * the test).
 * <p>
 * Currently (v1.0), this test doesn't check adaptiveness or efficiency.
 * <p>
 * All tests are run with duplicates. If there are duplicate items in the
 * array, they will show up as 20, ..., 20A, ..., 20B, etc. (the first has no
 * letter). 20 is before 20A, which is before 20B, etc. in the original
 * array, so when stably sorted the final array should be ... 20, 20A, 20B, ...
 * <p>
 * Radix sort is a bit different. It requires arrays of ints, not IntPlus
 * objects. Because we are using integers, we can't check stability (however
 * LSD radix sort relies on stability to work, so I don't think you can code
 * an unstable one).
 * <p>
 * Debugging tools:
 * Array toSort (or intsToSort for Radix sort) is the original items. Don't
 * modify it after initializing it!
 * Array sortedInts (or intsSorted for Radix sort) is what you sort.
 * <p>
 * Using the IntelliJ debugger with random tests:
 * The Random object rand used to create the arrays for each test iteration is
 * reseeded each iteration by a random int created by randSeeder and stored
 * in randSeed. This seed gets printed whenever a test fails. So to repeat a
 * specific test, just set randSeed to be the seed of the test you want to run.
 * @author CS 1332 TAs + Peter
 * @version 1.1
 */
public class SortingPeterTest {

    private static final int TEST_LOOPS = 10000;  //Number of tests to run
    private static final int MAX_ARR_SIZE = 20;   //Biggest array to test on
    //Array contains items in [0, MAX_ARR_VALUE]
    private static final int MAX_ARR_VALUE = 30;
    private static final int SHORT_TIMEOUT = 200;  //For null + length-1 tests
    private static final int LONG_TIMEOUT = 4000;  //For the random tests
    //RAND_UNSEEDED is what randSeed will be initialized to each test.
    //If randSeed == RAND_UNSEEDED, that means we never seeded - and never
    // used - the random number generator for that test.
    private static final long RAND_UNSEEDED = -5025562857975149832L;
    //Randomly seeds rand each loop of a test. That way we can repeat
    // specific loops
    private static Random randSeeder;
    //What we seeded the random number generator with last test
    private static long randSeed;
    private static Random rand;   //Generates random numbers for each tests
    private IntPlus[] toSort;     //Original array to sort
    private IntPlus[] sortedInts; //Array after your class sorted it
    private int[] intsToSort;      //Original array to sort for lsdRadixSort
    private int[] intsSorted;     //Array after sorting for lsdRadixSort
    /**
     * This method runs whenever your test fails.
     */
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            if (randSeed != RAND_UNSEEDED) {
                System.out.println("Random seed when test below failed: " + randSeed + " Use to repeat a specific test iteration in the debugger.");
            }
            if (toSort != null) {
                //For every test BUT LSD radix sort
                System.out.println("Array to sort when test failed:\n"
                        + Arrays.deepToString(toSort));
            }
            if (sortedInts != null) {
                //For every test BUT LSD radix sort
                System.out.println("Array you were sorting (might be "
                        + "partially sorted) when test failed:\n"
                        + Arrays.deepToString(sortedInts));
            }

            if (intsToSort != null) {
                //Only for LSD radix sort.
                System.out.println("Array to sort when LSD Radix sort failed:\n"
                        + Arrays.toString(intsToSort));
            }
            if (intsSorted != null) {
                //Only for LSD radix sort.
                System.out.println("Array you were sorting (might be "
                        + "partially sorted) when LSD Radix sort failed:\n"
                        + Arrays.toString(intsSorted));
            }
        }
    };
    private ComparatorPlus<IntPlus> comp;

    @Before
    public void setUp() {
        /*
        We need a way to repeat a SPECIFIC test when a random test fails.
        Therefore, EVERY time we run a random test, we seed the test's random
        number generator with a new random seed created by this random number
         generator. When a test fails, we can print out that random seed,
         thereby enabling us to rerun that specific test.
         */
        randSeeder = new Random(42);

        //Show we haven't seeded the test's random number generator
        randSeed = RAND_UNSEEDED;
        comp = IntPlus.getIntComparator();
    }

    //Tests for Selection Sort

    @Test(timeout = SHORT_TIMEOUT)
    public void testTinySelectionSort() {
        //size-0 arr
        toSort = new IntPlus[0];
        sortedInts = cloneArr(toSort);
        Sorting.selectionSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything

        //size-1 arr
        toSort = new IntPlus[1];
        toSort[0] = new IntPlus(42);
        sortedInts = cloneArr(toSort);
        Sorting.selectionSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything
    }

    //Don't need tests for null data - forbidden by PDF
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullArrSelectionSort() {
        Sorting.selectionSort(null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompSelectionSort() {
        sortedInts = new IntPlus[1];
        sortedInts[0] = new IntPlus(42);
        Sorting.selectionSort(sortedInts, null);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testSelectionSort() {
        boolean aSortWasUnstable = false;

        for (int i = 0; i < TEST_LOOPS; i++) {
            /*
            Change this line to repeat a specific test! For example, if the
            test you fail has a random seed of -929682766, do:

            randSeed = -929682766;
             */
            randSeed = randSeeder.nextInt();
            rand = new Random(randSeed);

            comp.resetCount();

            //Start with small arrays and scale larger
            int arrLength = i * MAX_ARR_SIZE / TEST_LOOPS + 1;
            toSort = randArrDuplicates(arrLength, rand);

            sortedInts = cloneArr(toSort);
            Sorting.selectionSort(sortedInts, comp);

            assertSorted(sortedInts, comp);

            //Check for instability. Know the array was already sorted, so if
            // this fails, it must be because of an unstable sort
            if (!aSortWasUnstable) {
                try {
                    assertStablySorted(toSort, sortedInts, comp);
                } catch (AssertionError e) {
                    aSortWasUnstable = true;
                }
            }
        }
        assertTrue("This sort should not be stable.", aSortWasUnstable);
    }

    //Tests for Insertion Sort

    @Test(timeout = SHORT_TIMEOUT)
    public void testTinyInsertionSort() {
        //size-0 arr
        toSort = new IntPlus[0];
        sortedInts = cloneArr(toSort);
        Sorting.insertionSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything

        //size-1 arr
        toSort = new IntPlus[1];
        toSort[0] = new IntPlus(42);
        sortedInts = cloneArr(toSort);
        Sorting.insertionSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything
    }

    //Don't need tests for null data - forbidden by PDF
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullArrInsertionSort() {
        Sorting.insertionSort(null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompInsertionSort() {
        sortedInts = new IntPlus[1];
        sortedInts[0] = new IntPlus(42);
        Sorting.insertionSort(sortedInts, null);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testInsertionSort() {
        for (int i = 0; i < TEST_LOOPS; i++) {
            /*
            Change this line to repeat a specific test! For example, if the
            test you fail has a random seed of -929682766, do:

            randSeed = -929682766;
             */
            randSeed = randSeeder.nextInt();
            rand = new Random(randSeed);

            comp.resetCount();

            //Start with small arrays and scale larger
            int arrLength = i * MAX_ARR_SIZE / TEST_LOOPS + 1;
            toSort = randArrDuplicates(arrLength, rand);

            sortedInts = cloneArr(toSort);
            Sorting.insertionSort(sortedInts, comp);

            assertSorted(sortedInts, comp);
            assertStablySorted(toSort, sortedInts, comp);

        }
    }

    /**
     * 2 tests here:
     * The first is a straight adaptiveness check that checks O(n) on a
     * completely sorted list.
     * The second is sorting a list with one item out of order. It
     * should be easy to calculate how many comparisons you need to make to
     * sort the list and check your sort finishes early.
     */
    @Test(timeout = SHORT_TIMEOUT)
    public void testAdaptiveInsertionSort() {
        //Test adaptiveness of completely ordered sort

        //Generate sorted array
        toSort = randArrDuplicates(MAX_ARR_SIZE, new Random(2110));
        Arrays.parallelSort(toSort, comp);
        sortedInts = cloneArr(toSort);

        comp.resetCount();
        Sorting.insertionSort(sortedInts, comp);
        int numComparisons = comp.getCount();

        assertSorted(sortedInts, comp);

        //Should require only 1 pass through array, for n-1 comparisons
        assertTrue("Too many comparisons: " + numComparisons,
                numComparisons <= MAX_ARR_SIZE - 1);

        //Check adaptiveness with 1 item out-of-order

        //Set up list
        toSort = new IntPlus[6];
        for (int i = 0; i < toSort.length; i++) {
            toSort[i] = new IntPlus(2 * i);
        }
        toSort[3] = new IntPlus(-1);

        sortedInts = cloneArr(toSort);

        /*
        Initial Array: [0, 2, 4, -1, 8, 10]
        Should require 7 comparisons to sort
         */
        comp.resetCount();
        Sorting.insertionSort(sortedInts, comp);
        numComparisons = comp.getCount();

        assertSorted(sortedInts, comp);

        assertTrue("Too many comparisons: " + numComparisons,
                numComparisons <= 7);
    }

    //Tests for Cocktail Shaker

    @Test(timeout = SHORT_TIMEOUT)
    public void testTinyCocktailShakerSort() {
        //size-0 arr
        toSort = new IntPlus[0];
        sortedInts = cloneArr(toSort);
        Sorting.cocktailSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything

        //size-1 arr
        toSort = new IntPlus[1];
        toSort[0] = new IntPlus(42);
        sortedInts = cloneArr(toSort);
        Sorting.cocktailSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything
    }

    //Don't need tests for null data - forbidden by PDF
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullArrCocktailShakerSort() {
        Sorting.cocktailSort(null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompCocktailShakerSort() {
        sortedInts = new IntPlus[1];
        sortedInts[0] = new IntPlus(42);
        Sorting.cocktailSort(sortedInts, null);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testCocktailShakerSort() {
        for (int i = 0; i < TEST_LOOPS; i++) {
            randSeed = randSeeder.nextInt();
            /*
            Change this line to repeat a specific test! For example, if the
            test you fail has a random seed of -929682766, do:

            rand = new Random(-929682766);
             */
            rand = new Random(randSeed);

            comp.resetCount();

            //Start with small arrays and scale larger
            int arrLength = i * MAX_ARR_SIZE / TEST_LOOPS + 1;
            toSort = randArrDuplicates(arrLength, rand);

            sortedInts = cloneArr(toSort);
            Sorting.cocktailSort(sortedInts, comp);

            assertSorted(sortedInts, comp);
            assertStablySorted(toSort, sortedInts, comp);

        }
    }

    /**
     * 2 tests here:
     * The first is a straight adaptiveness check that checks O(n) on a
     * completely sorted list.
     * The second is sorting a list with one item out of order. It
     * should be easy to calculate how many comparisons you need to make to
     * sort the list and check your sort finishes early.
     */
    @Test(timeout = SHORT_TIMEOUT)
    public void testAdaptiveCocktailShakerSort() {
        //Test adaptiveness of completely ordered sort

        //Generate sorted array
        toSort = randArrDuplicates(MAX_ARR_SIZE, new Random(2110));
        Arrays.parallelSort(toSort, comp);
        sortedInts = cloneArr(toSort);

        comp.resetCount();
        Sorting.cocktailSort(sortedInts, comp);
        int numComparisons = comp.getCount();

        assertSorted(sortedInts, comp);

        //Should require only 1 pass through array, for n-1 comparisons
        assertTrue("Too many comparisons: " + numComparisons,
                numComparisons <= MAX_ARR_SIZE - 1);

        //Check adaptiveness with 1 item out-of-order

        //Set up list
        toSort = new IntPlus[6];
        for (int i = 0; i < toSort.length; i++) {
            toSort[i] = new IntPlus(2 * i);
        }
        toSort[0] = new IntPlus(7);

        sortedInts = cloneArr(toSort);

        /*
        Initial Array: [7, 2, 4, 6, 8, 10]
        Forward pass: [ 2, 4, 6, *7, *8, *10]. 5 comparisons. Last swap at
        index 2, so starred items in order
        Backward pass: [ 2, 4, 6, *7, *8, *10]. 2 comparisons. Search ends
         */
        comp.resetCount();
        Sorting.cocktailSort(sortedInts, comp);
        numComparisons = comp.getCount();

        assertSorted(sortedInts, comp);

        assertTrue("Too many comparisons: " + numComparisons,
                numComparisons <= 7);
    }

    //Tests for Merge Sort

    @Test(timeout = SHORT_TIMEOUT)
    public void testTinyMergeSort() {
        //size-0 arr
        toSort = new IntPlus[0];
        sortedInts = cloneArr(toSort);
        Sorting.mergeSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything

        //size-1 arr
        toSort = new IntPlus[1];
        toSort[0] = new IntPlus(42);
        sortedInts = cloneArr(toSort);
        Sorting.mergeSort(sortedInts, comp);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything
    }

    //Don't need tests for null data - forbidden by PDF
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullArrMergeSort() {
        Sorting.mergeSort(null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompMergeSort() {
        sortedInts = new IntPlus[1];
        sortedInts[0] = new IntPlus(42);
        Sorting.mergeSort(sortedInts, null);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testMergeSort() {
        for (int i = 0; i < TEST_LOOPS; i++) {
            randSeed = randSeeder.nextInt();
            /*
            Change this line to repeat a specific test! For example, if the
            test you fail has a random seed of -929682766, do:

            rand = new Random(-929682766);
             */
            rand = new Random(randSeed);

            comp.resetCount();

            //Start with small arrays and scale larger
            int arrLength = i * MAX_ARR_SIZE / TEST_LOOPS + 1;
            toSort = randArrDuplicates(arrLength, rand);

            sortedInts = cloneArr(toSort);
            Sorting.mergeSort(sortedInts, comp);

            assertSorted(sortedInts, comp);
            assertStablySorted(toSort, sortedInts, comp);

        }
    }

    //Tests for Quicksort

    @Test(timeout = SHORT_TIMEOUT)
    public void testTinyQuickSort() {
        //size-0 arr
        toSort = new IntPlus[0];
        sortedInts = cloneArr(toSort);
        Sorting.quickSort(sortedInts, comp, new Random(1301));

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything

        //size-1 arr
        toSort = new IntPlus[1];
        toSort[0] = new IntPlus(42);
        sortedInts = cloneArr(toSort);
        Sorting.quickSort(sortedInts, comp, new Random(1301));

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything
    }

    //Don't need tests for null data - forbidden by PDF
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullArrQuickSort() {
        Sorting.quickSort(null, comp, new Random(1301));
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompQuickSort() {
        sortedInts = new IntPlus[1];
        sortedInts[0] = new IntPlus(42);
        Sorting.quickSort(sortedInts, null, new Random(1301));
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullRandQuickSort() {
        sortedInts = new IntPlus[1];
        sortedInts[0] = new IntPlus(42);
        Sorting.quickSort(sortedInts, comp, null);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testQuickSort() {
        boolean aSortWasUnstable = false;
        for (int i = 0; i < TEST_LOOPS; i++) {
            randSeed = randSeeder.nextInt();
            /*
            Change this line to repeat a specific test! For example, if the
            test you fail has a random seed of -929682766, do:

            rand = new Random(-929682766);
             */
            rand = new Random(randSeed);

            comp.resetCount();

            //Start with small arrays and scale larger
            int arrLength = i * MAX_ARR_SIZE / TEST_LOOPS + 1;
            toSort = randArrDuplicates(arrLength, rand);

            sortedInts = cloneArr(toSort);
            Sorting.quickSort(sortedInts, comp, rand);

            assertSorted(sortedInts, comp);
            //Check for instability. Know the array was already sorted, so if
            // this fails, it must be because of an unstable sort
            if (!aSortWasUnstable) {
                try {
                    assertStablySorted(toSort, sortedInts, comp);
                } catch (AssertionError e) {
                    aSortWasUnstable = true;
                }
            }
        }
        assertTrue("This sort should not be stable.", aSortWasUnstable);
    }

    //Tests for LSD Radix Sort
    //LSD radix sort needs ints, not IntPlus, so we can't check stability

    @Test(timeout = SHORT_TIMEOUT)
    public void testTinyRadixSort() {
        //size-0 arr
        int[] intToSort = new int[0];
        int[] intsSorted = cloneArr(intToSort);
        Sorting.lsdRadixSort(intsSorted);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything

        //size-1 arr
        toSort = new IntPlus[1];
        toSort[0] = new IntPlus(42);
        sortedInts = cloneArr(toSort);
        Sorting.lsdRadixSort(intsSorted);

        assertArrayEquals(toSort, sortedInts); //Sort can't rearrange anything
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullArrRadixSort() {
        Sorting.lsdRadixSort(null);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testRadixSort() {

        for (int i = 0; i < TEST_LOOPS; i++) {
            /*
            Change this line to repeat a specific test! For example, if the
            test you fail has a random seed of -929682766, do:

            randSeed = -929682766;
             */
            randSeed = randSeeder.nextInt();
            rand = new Random(randSeed);

            //Start with small arrays and scale larger
            int arrLength = i * MAX_ARR_SIZE / TEST_LOOPS + 1;
            boolean haveNegatives = i % 2 == 1;  //Should we test negatives
            intsToSort = new int[arrLength];

            //Fill array with random numbers
            for (int j = 0; j < intsToSort.length; j++) {
                if (haveNegatives) {
                    intsToSort[j] =
                            rand.nextInt(2 * MAX_ARR_VALUE + 1) - MAX_ARR_VALUE;
                } else {
                    intsToSort[j] = rand.nextInt(MAX_ARR_VALUE + 1);
                }
            }


            intsSorted = cloneArr(intsToSort);
            Sorting.lsdRadixSort(intsSorted);

            assertSorted(intsSorted);

        }
    }

    //Methods for checking correctness and stability of sort

    /**
     * Checks the given array  of integers was sorted
     * @param testArr Array to test
     */
    private void assertSorted(final int[] testArr) {
        for (int i = 0; i < testArr.length - 1; i++) {
            assertTrue("Array was not sorted: element " + i + " was out "
                            + "of order: \n" + Arrays.toString(testArr),
                    testArr[i] <= testArr[i + 1]);

        }
    }

    /**
     * Checks the given array was sorted
     * @param testArr Array to test
     * @param cmp     The comparator involved
     */
    private void assertSorted(final IntPlus[] testArr,
                              Comparator<IntPlus> cmp) {
        for (int i = 0; i < testArr.length - 1; i++) {
            assertTrue("Array was not sorted: element " + i + " was out "
                            + "of order: \n" + Arrays.deepToString(testArr),
                    cmp.compare(testArr[i], testArr[i + 1]) <= 0);

        }
    }

    /**
     * Checks the array was stably sorted.
     * @param origArr Original array to sort. Not modified
     * @param testArr Result of the sort
     * @param cmp     comparator to check
     */
    private void assertStablySorted(final IntPlus[] origArr,
                                    final IntPlus[] testArr,
                                    Comparator<IntPlus> cmp) {
        //Create copy of original array and sort it
        IntPlus[] stablySortedArr = cloneArr(origArr);
        Arrays.parallelSort(stablySortedArr, cmp);  //guaranteed to be stable

        //Works since stable sorts are unique - there is 1 right answer.
        for (int i = 0; i < origArr.length; i++) {
            assertEquals("Array was not stably sorted: element " + i + " was "
                            + "out of order. Expected:\n"
                            + Arrays.deepToString(stablySortedArr) + "\nYours:"
                            + "\n" + Arrays.deepToString(testArr),
                    stablySortedArr[i], testArr[i]);
        }
    }

    /**
     * Clone the given array
     * @param origArr array to clone. Does NOT create new IntPlus objects -
     *                new array refers to same objects
     * @return cloned array.
     */
    private IntPlus[] cloneArr(final IntPlus[] origArr) {
        return Arrays.copyOf(origArr, origArr.length);
    }

    /**
     * Clone the given array
     * @param origArr array to clone. Does NOT create new IntPlus objects -
     *                new array refers to same objects
     * @return cloned array.
     */
    private int[] cloneArr(final int[] origArr) {
        return Arrays.copyOf(origArr, origArr.length);
    }

    //For generating random arrays

    /**
     * Generates an array of IntPlus values from [0, MAX_ARR_VALUE] without
     * duplicates
     * @param length   number of items
     * @param randNums Random object to use for repeatability
     * @return random IntPlus array
     */
    public IntPlus[] randArrNoDuplicates(int length, Random randNums) {
        return randArrNoDuplicates(0, MAX_ARR_VALUE, length, randNums);
    }

    /**
     * Generates an array of IntPlus values from [min, max] without duplicates
     * @param min      minimum value
     * @param max      maximum value
     * @param length   number of items
     * @param randNums Random object to use for repeatability
     * @return random IntPlus array
     */
    public IntPlus[] randArrNoDuplicates(int min, int max,
                                         int length, Random randNums) {
        if (max - min <= length) {
            throw new IllegalArgumentException("Range too small: Can't "
                    + "generate array without duplicates");
        }

        //stores already-added values
        HashSet<Integer> valuesInList = new HashSet<>();

        IntPlus[] randArr = new IntPlus[length];

        int toAdd;
        for (int i = 0; i < length; i++) {
            //Generate random number not already in list
            do {
                toAdd = randNums.nextInt(max - min + 1) + min;
            } while (!valuesInList.add(toAdd));

            randArr[i] = new IntPlus(toAdd);
        }
        return randArr;
    }

    /**
     * Generates an array of IntPlus values from [0, MAX_ARR_VALUE] with
     * duplicates.
     * <p>
     * Duplicates identified by an extra string value:
     * 0 -> , 1 ->"A", 2 ->"B" ... 26 -> "Z", 27 ->"AA", 28 -> "AB" ...
     * @param length   number of items
     * @param randNums Random object to use for repeatability
     * @return random IntPlus array
     */
    public IntPlus[] randArrDuplicates(int length, Random randNums) {
        return randArrDuplicates(0, MAX_ARR_VALUE, length, randNums);
    }

    /**
     * Generates an array of IntPlus values from [min, max] with duplicates.
     * <p>
     * Duplicates identified by an extra string value:
     * 0 -> , 1 ->"A", 2 ->"B" ... 26 -> "Z", 27 ->"AA", 28 -> "AB" ...
     * @param min      minimum value
     * @param max      maximum value
     * @param length   number of items
     * @param randNums Random object to use for repeatability
     * @return random IntPlus array
     */
    public IntPlus[] randArrDuplicates(int min, int max,
                                       int length, Random randNums) {

        //Stores number of times we added a specific value
        HashMap<Integer, Integer> valuesInList = new HashMap<>();

        IntPlus[] randArr = new IntPlus[length];

        int toAdd;
        for (int i = 0; i < length; i++) {
            toAdd = randNums.nextInt(max - min + 1) + min;

            //Initialize entry to 0 if not present
            valuesInList.putIfAbsent(toAdd, 0);

            randArr[i] = new IntPlus(toAdd, idString(valuesInList.get(toAdd)));

            //Increment that value
            valuesInList.put(toAdd, valuesInList.get(toAdd) + 1);
        }
        return randArr;
    }

    /**
     * Generates a string to identify duplicate IntPlus values.
     * <p>
     * 0 -> null, 1 ->"A", 2 ->"B" ... 26 -> "Z", 27 ->"AA", 28 -> "AB" ...
     * @param occurrence number of times this value has come up.
     * @return string
     */
    private String idString(int occurrence) {
        if (occurrence == 0) {
            return null;
        }
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
     * Class for testing proper sorting.
     * Note that the comparator only checks the int values, but the class
     * also stores a string subType which affects its equality.
     */
    private static class IntPlus {
        private int value;
        private String subType;

        public IntPlus(int value, String subType) {
            this.value = value;
            this.subType = subType;
        }

        public IntPlus(int value) {
            this(value, null);
        }

        /**
         * Create a comparator that compares the values.
         * @return comparator that compares the values
         */
        public static ComparatorPlus<IntPlus> getIntComparator() {
            return new ComparatorPlus<SortingPeterTest.IntPlus>() {
                @Override
                public int compare(SortingPeterTest.IntPlus int1,
                                   SortingPeterTest.IntPlus int2) {
                    incrementCount();
                    return int1.value - int2.value;
                }
            };
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        @Override
        public String toString() {
            if (subType == null) {
                return value + "";
            } else {
                return value + subType;
            }
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof IntPlus)) {
                return false;
            }
            IntPlus o = (IntPlus) other;
            if (o.value == this.value) {
                if (o.subType == this.subType) {
                    return true;
                } else if (this.subType == null || o.subType == null) {
                    //Can't both be null since they are not =
                    return false;
                } else {
                    return this.subType.equals(o.subType);
                }
            }
            return false;
        }
    }

    /**
     * Inner class that allows counting how many comparisons were made.
     */
    private abstract static class ComparatorPlus<T> implements Comparator<T> {
        private int count;

        /**
         * Get the number of comparisons made.
         * @return number of comparisons made
         */
        public int getCount() {
            return count;
        }

        /**
         * Reset the number of comparisons made.
         */
        public void resetCount() {
            count = 0;
        }

        /**
         * Increment the number of comparisons made by one. Call this method in
         * your compare() implementation.
         */
        public void incrementCount() {
            count++;
        }
    }
}