import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sorting.heapsort.HeapSort;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeapSortTest {

    private HeapSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new HeapSort();
    }

    @Test
    void testSortSimpleAscending() {
        Integer[] arr = {5, 1, 4, 2, 3};
        sorter.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testSortWithDuplicatesAndNegatives() {
        Integer[] arr = { 0, -5, 7, -1, 7, 3, 0, -5, 2 };
        sorter.sort(arr);
        assertArrayEquals(
                new Integer[]{-5, -5, -1, 0, 0, 2, 3, 7, 7},
                arr
        );
    }

    @Test
    void testSortRandom() {
        int n = 50;
        Integer[] arr = new Integer[n];
        Random rnd = new Random();  // no fixed seed
        for (int i = 0; i < n; i++) {
            arr[i] = rnd.nextInt(200) - 50;  // values in [-50..149]
        }

        // Copy and sort with Java’s Arrays.sort for the expected result
        Integer[] expected = Arrays.copyOf(arr, n);
        Arrays.sort(expected);

        sorter.sort(arr);
        assertArrayEquals(expected, arr, "HeapSort did not match Java’s Arrays.sort");
    }

    @Test
    void testSortEmptyArray() {
        Integer[] arr = {};
        sorter.sort(arr);
        assertEquals(0, arr.length);
    }

    @Test
    void testSortSingleElement() {
        Integer[] arr = {42};
        sorter.sort(arr);
        assertArrayEquals(new Integer[]{42}, arr);
    }
}