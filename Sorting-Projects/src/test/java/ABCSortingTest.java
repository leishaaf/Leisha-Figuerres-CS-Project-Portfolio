import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sorting.partition.ABCSorting;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ABCSortingTest {

    private ABCSorting sorter;

    @BeforeEach
    void setUp() {
        sorter = new ABCSorting();
    }

    @Test
    void testWinnerA() {
        String[] votes = {"A","B","A","C","A","A","A","B","C","A","B"};
        String winner = sorter.sortAndFindWinner(votes);
        assertEquals("A", winner);
        // after sorting, all A’s come first, then B’s, then C’s
        assertArrayEquals(
                new String[]{"A","A","A","A","A","A","B","B","B","C","C"},
                votes
        );
    }

    @Test
    void testWinnerB() {
        String[] votes = {"B","C","A","B","B","C","B","A","C","B","A"};
        String winner = sorter.sortAndFindWinner(votes);
        assertEquals("B", winner);
        assertArrayEquals(
                new String[]{"A","A","A","B","B","B","B","B","C","C","C"},
                votes
        );
    }

    @Test
    void testWinnerC() {
        String[] votes = {"C","C","B","A","C","B","C","A","C","B","C"};
        String winner = sorter.sortAndFindWinner(votes);
        assertEquals("C", winner);
        assertArrayEquals(
                new String[]{"A","A","B","B","B","C","C","C","C","C","C"},
                votes
        );
    }
}