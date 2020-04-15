import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Your implementation of various sorting algorithms.
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
public class Sorting {

    /**
     * Implement selection sort.
     *
     * It should be:
     * in-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n^2)
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void selectionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort null array");
        } else if (comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot sort with null comparator");
        }
        for (int i = 0; i < arr.length; i++) {
            int min = i;
            int j = i + 1;
            while (j < arr.length) {
                int c = comparator.compare(arr[min], arr[j]);
                if (c > 0) {
                    min = j;
                }
                j++;
            }
            T temp = arr[i];
            arr[i] = arr[min];
            arr[min] = temp;
        }
    }

    /**
     * Implement insertion sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort null array");
        } else if (comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot sort with null comparator");
        }
        int numSorted = 1;
        while (numSorted < arr.length) {
            int i = numSorted;
            T temp = arr[numSorted];
            while (i > 0) {
                int c = comparator.compare(temp, arr[i - 1]);
                if (c < 0) {
                    arr[i] = arr[i - 1];
                } else {
                    break;
                }
                i--;
            }
            arr[i] = temp;
            numSorted++;
        }
    }

    /**
     * Implement cocktail sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * NOTE: See pdf for last swapped optimization for cocktail sort. You
     * MUST implement cocktail sort with this optimization
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort null array");
        } else if (comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot sort with null comparator");
        }
        int lastSwapped = 0;
        int left = 0;
        int right = arr.length - 1;
        boolean swap = true;
        while (swap) {
            swap = false;
            for (int i = lastSwapped; i < right; i++) {
                int c = comparator.compare(arr[i], arr[i + 1]);
                if (c > 0) {
                    T temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    lastSwapped = i;
                    swap = true;
                }
            }
            right = lastSwapped;
            if (!swap) {
                break;
            } else {
                swap = false;
                for (int i = lastSwapped; i > left; i--) {
                    int c = comparator.compare(arr[i], arr[i - 1]);
                    if (c < 0) {
                        T temp = arr[i];
                        arr[i] = arr[i - 1];
                        arr[i - 1] = temp;
                        lastSwapped = i;
                        swap = true;
                    }
                }
                left = lastSwapped;
            }
        }
    }

    /**
     * Implement merge sort.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * You can create more arrays to run merge sort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * Hint: If two data are equal when merging, think about which subarray
     * you should pull from first
     *
     * @param <T>        data type to sort
     * @param arr        the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort null array");
        } else if (comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot sort with null comparator");
        }
        int mid = arr.length / 2;
        mergeHelp(arr, comparator);
    }

    /**
     * Helper method for merge sort.
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    private static <T> void mergeHelp(T[] arr, Comparator<T> comparator) {
        if (arr.length < 2) {
            return;
        }
        int mid = arr.length / 2;
        T[] left = (T[]) new Object[mid];
        T[] right = (T[]) new Object[arr.length - mid];
        for (int i = 0; i < mid; i++) {
            left[i] = arr[i];
        }
        for (int i = mid; i < arr.length; i++) {
            right[i - mid] = arr[i];
        }
        mergeSort(left, comparator);
        mergeSort(right, comparator);
        merge(arr, left, right, mid, arr.length - mid, comparator);
    }

    /**
     * Helper merge method for merge sort.
     *
     * @param <T>        data type to sort
     * @param a          the array that must be sorted after the method runs
     * @param left       left array
     * @param right      right array
     * @param l          starting index of left array
     * @param r          starting index of right array
     * @param comparator the Comparator used to compare the data in arr
     */
    private static <T> void merge(T[] a, T[] left, T[] right, int l,
                                  int r, Comparator<T> comparator) {
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < l && j < r) {
            int c = comparator.compare(left[i], right[j]);
            if (c <= 0) {
                a[k++] = left[i++];
            } else {
                a[k++] = right[j++];
            }
        }
        while (i < l) {
            a[k++] = left[i++];
        }
        while (j < r) {
            a[k++] = right[j++];
        }
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     * in-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not receive
     * credit if you do not use the one we have taught you!
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     * @throws java.lang.IllegalArgumentException if the array or comparator or
     *                                            rand is null
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator,
                                     Random rand) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort null array");
        } else if (comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot sort with null comparator");
        } else if (rand == null) {
            throw new IllegalArgumentException(
                    "Cannot sort with null random");
        }
        quickH(arr, 0, arr.length - 1, comparator, rand);
    }

    /**
     * Helper method for quick sort
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param left       the left index
     * @param right      right index
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     */
    private static <T> void quickH(T[] arr, int left, int right,
                                   Comparator<T> comparator, Random rand) {
        if (left >= right) {
            return;
        }
        int l = left + 1;
        int r = right;
        int pivot = rand.nextInt(right - left + 1) + left;
        T p = arr[pivot];
        arr[pivot] = arr[left];
        arr[left] = p;
        while (l <= r) {
            while (l <= r) {
                if (comparator.compare(arr[l], p) <= 0) {
                    l++;
                } else {
                    break;
                }
            }
            while (l < r) {
                if (comparator.compare(arr[r], p) >= 0) {
                    r--;
                } else {
                    break;
                }
            }
            if (l == r) {
                r--;
            }
            if (l < r) {
                T temp = arr[l];
                arr[l] = arr[r];
                arr[r] = temp;
                l++;
                r--;
            }
        }
        T temp = arr[r];
        arr[r] = arr[left];
        arr[left] = temp;
        quickH(arr, left, r - 1, comparator, rand);
        quickH(arr, r + 1, right, comparator, rand);
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(kn)
     *
     * And a best case running time of:
     * O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need.
     *
     * At no point should you find yourself needing a way to exponentiate a
     * number; any such method would be non-O(1). Think about how how you can
     * get each power of BASE naturally and efficiently as the algorithm
     * progresses through each digit.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use ArrayList or LinkedList if you wish, but it may only be
     * used inside radix sort and any radix sort helpers. Do NOT use these
     * classes with other sorts. However, be sure the List implementation you
     * choose allows for stability while being as efficient as possible.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @param arr the array to be sorted
     * @throws java.lang.IllegalArgumentException if the array is null
     */
    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort null array");
        }
        LinkedList<Integer>[] buckets =
                (LinkedList<Integer>[]) new LinkedList[19];
        int mod = 10;
        int div = 1;
        boolean cont = true;
        while (cont) {
            cont = false;
            for (int num : arr) {
                int bucket = num / div;
                if (bucket / 10 != 0) {
                    cont = true;
                }
                if (buckets[bucket % mod + 9] == null) {
                    buckets[bucket % mod + 9] = new LinkedList<>();
                }
                buckets[bucket % mod + 9].add(num);
            }
            int arrIdx = 0;
            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i] != null) {
                    for (int num : buckets[i]) {
                        arr[arrIdx++] = num;
                    }
                    buckets[i].clear();
                }
            }
            div *= 10;
        }
    }
}
