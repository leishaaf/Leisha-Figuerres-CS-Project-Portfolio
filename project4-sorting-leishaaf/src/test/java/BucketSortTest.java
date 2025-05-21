import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sorting.bucketsort.BucketSort;
import sorting.bucketsort.Elem;

import static org.junit.jupiter.api.Assertions.*;

public class BucketSortTest {
    private BucketSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new BucketSort();
    }

    // Helper to pull out keys for easy comparison
    private int[] keysOf(Elem[] arr, int low, int high) {
        int len = high - low + 1;
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = arr[low + i].key();
        }
        return result;
    }

    @Test
    void testSortAscendingSimple() {
        Elem[] arr = {
                new Elem(5, "a"),
                new Elem(1, "b"),
                new Elem(3, "c"),
                new Elem(2, "d"),
                new Elem(4, "e")
        };
        sorter.sort(arr, 0, arr.length - 1, false);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, keysOf(arr, 0, arr.length - 1));
    }

    @Test
    void testSortDescendingSimple() {
        Elem[] arr = {
                new Elem(5, "a"),
                new Elem(1, "b"),
                new Elem(3, "c"),
                new Elem(2, "d"),
                new Elem(4, "e")
        };
        sorter.sort(arr, 0, arr.length - 1, true);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, keysOf(arr, 0, arr.length - 1));
    }

    @Test
    void testSingleElement() {
        Elem[] arr = { new Elem(42, "solo") };
        sorter.sort(arr, 0, 0, false);
        assertEquals(42, arr[0].key());
        assertEquals("solo", arr[0].data());
    }

    @Test
    void testSubarraySortLeavesOutsideIntact() {
        // positions 0 and 4 should remain untouched
        Elem[] arr = {
                new Elem(99, "keep0"),
                new Elem(4,  "b"),
                new Elem(1,  "c"),
                new Elem(3,  "d"),
                new Elem(100,"keep4")
        };
        sorter.sort(arr, 1, 3, false);
        // outside region unchanged
        assertEquals(99,  arr[0].key());
        assertEquals("keep0", arr[0].data());
        assertEquals(100, arr[4].key());
        assertEquals("keep4", arr[4].data());
        // inside region sorted
        assertArrayEquals(new int[]{1,3,4}, keysOf(arr,1,3));
    }

    @Test
    void testSortKeepsAscendingThenReverses() {
        Elem[] arr = {
                new Elem(1, "a"),
                new Elem(2, "b"),
                new Elem(3, "c"),
                new Elem(4, "d"),
                new Elem(5, "e")
        };
        // already ascending
        sorter.sort(arr, 0, arr.length - 1, false);
        assertArrayEquals(new int[]{1,2,3,4,5}, keysOf(arr,0,4));

        // now reverse in place
        sorter.sort(arr, 0, arr.length - 1, true);
        assertArrayEquals(new int[]{5,4,3,2,1}, keysOf(arr,0,4));
    }

    @Test
    void testSortLargeRandomArray() {
        int n = 500;
        Elem[] arr = new Elem[n];
        java.util.Random rand = new java.util.Random(123);
        // populate with random keys in [0..999]
        for (int i = 0; i < n; i++) {
            int key = rand.nextInt(1000);
            arr[i] = new Elem(key, "data" + key);
        }
        // sort ascending
        sorter.sort(arr, 0, n - 1, false);

        // verify ascending order
        for (int i = 0; i < n - 1; i++) {
            assertTrue(arr[i].key() <= arr[i + 1].key(),
                    "Array not sorted at index " + i + ": "
                            + arr[i].key() + " > " + arr[i + 1].key());
        }
        // now sort descending in place
        sorter.sort(arr, 0, n - 1, true);
        // verify descending order
        for (int i = 0; i < n - 1; i++) {
            assertTrue(arr[i].key() >= arr[i + 1].key(),
                    "Array not sorted descending at index " + i + ": "
                            + arr[i].key() + " < " + arr[i + 1].key());
        }
    }
}