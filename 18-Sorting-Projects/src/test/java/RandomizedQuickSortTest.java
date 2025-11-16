import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sorting.quicksort.RandomizedQuicksort;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomizedQuickSortTest {
    private RandomizedQuicksort sorter;

    @BeforeEach
    void setUp() {
        sorter = new RandomizedQuicksort();
    }

    /** Helper to extract int keys from a Comparable[] slice, if it contains integers */
    private int[] keysOf(Comparable[] arr, int low, int high) {
        int len = high - low + 1;
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = (Integer) arr[low + i];
        }
        return result;
    }

    @Test
    void testSortSimpleAscending() {
        Integer[] arr = {5, 1, 4, 2, 3};
        sorter.sort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{1,2,3,4,5}, keysOf(arr, 0, 4));
    }

    @Test
    void testSortWithDuplicatesAndNegatives() {
        Integer[] arr = { 0, -2, 5, -2, 3, 5, 1, 0 };
        sorter.sort(arr, 0, arr.length - 1);
        assertArrayEquals(
                new int[]{-2, -2, 0, 0, 1, 3, 5, 5},
                keysOf(arr, 0, arr.length - 1)
        );
    }

    @Test
    void testSingleElement() {
        Integer[] arr = {42};
        sorter.sort(arr, 0, 0);
        assertEquals(42, arr[0]);
    }

    @Test
    void testEmptyArray() {
        Integer[] arr = {};
        // no exception, stays empty
        sorter.sort(arr, 0, arr.length - 1);
        assertEquals(0, arr.length);
    }

    @Test
    void testSubarraySort() {
        Integer[] arr = {9, 3, 7, 1, 5, 8, 2};
        // sort only 1..4
        sorter.sort(arr, 1, 4);
        // outside unchanged
        assertEquals(9, arr[0]);
        assertEquals(8, arr[5]);
        assertEquals(2, arr[6]);
        // inside sorted
        assertArrayEquals(new int[]{1,3,5,7}, keysOf(arr, 1, 4));
    }

    @Test
    void testRandomAscending() {
        int n = 100;
        Integer[] arr = new Integer[n];
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            arr[i] = rnd.nextInt(500) - 250;
        }
        Integer[] expected = Arrays.copyOf(arr, n);
        Arrays.sort(expected);
        sorter.sort(arr, 0, n - 1);
        assertArrayEquals(
                Arrays.stream(expected).mapToInt(i -> i).toArray(),
                keysOf(arr, 0, n - 1)
        );
    }

    @Test
    void testSortStrings() {
        String[] original = { "banana", "apple", "cherry", "apple", "date" };

        String[] asc = original.clone();
        sorter.sort(asc, 0, asc.length - 1);
        assertArrayEquals(
                new String[]{ "apple", "apple", "banana", "cherry", "date" },
                asc,
                "Should sort strings lexicographically ascending"
        );
    }
}