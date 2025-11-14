import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import songlist.Song;
import songlist.SongList;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class SongListTest {
    private SongList listA;
    private SongList listB;
    private SongList list;

    @BeforeEach
    public void setUp() {
        listA = new SongList();
        listB = new SongList();
        list = new SongList();
    }

    @Test
    public void testInsertAndIterationByTitle() {
        listA.insert("Love Story", "Taylor Swift", 5);
        listA.insert("Bad Habits", "Ed Sheeran", 3);
        listA.insert("Hello", "Adele", 4);
        Iterator<Song> titleIt = listA.iteratorByTitle();
        // We expect to traverse the list in alphabetical order of the title:
        // "Bad Habits", "Hello", "Love Story"
        assertTrue(titleIt.hasNext(), "Iterator should have elements");
        Song t1 = titleIt.next();
        assertEquals("Bad Habits", t1.getTitle());

        Song t2 = titleIt.next();
        assertEquals("Hello", t2.getTitle());

        Song t3 = titleIt.next();
        assertEquals("Love Story", t3.getTitle());

        assertFalse(titleIt.hasNext(), "Iterator should have no elements.");
    }


    @Test
    public void testInsertAndIterationByScore() {
        listA.insert("Love Story", "Taylor Swift", 5);
        listA.insert("Bad Habits", "Ed Sheeran", 3);
        listA.insert("Hello", "Adele", 4);

        Iterator<Song> scoreIt = listA.iteratorByScore();
        // Expect to traverse the list in descending order of the scores: 5, 4, 3
        assertTrue(scoreIt.hasNext(), "Iterator should have elements");
        Song first = scoreIt.next();
        assertEquals("Love Story", first.getTitle());
        assertEquals("Taylor Swift", first.getArtist());
        assertEquals(5, first.getScore());

        Song second = scoreIt.next();
        assertEquals("Hello", second.getTitle());
        assertEquals("Adele", second.getArtist());
        assertEquals(4, second.getScore());

        Song third = scoreIt.next();
        assertEquals("Bad Habits", third.getTitle());
        assertEquals("Ed Sheeran", third.getArtist());
        assertEquals(3, third.getScore());

        assertFalse(scoreIt.hasNext(), "Iterator should have no elements");

    }

    @Test
    public void testContainsSong() {
        listA.insert("Song A", "Artist A", 2);
        listA.insert("Song B", "Artist B", 4);

        assertTrue(listA.containsSong("Song A", "Artist A"));
        assertTrue(listA.containsSong("Song B", "Artist B"));
        assertFalse(listA.containsSong("Song C", "Artist C"));
    }

    @Test
    public void testFindSongsWithinScoreRange() {
        listA.insert("Low Score", "Artist", 1);
        listA.insert("Mid Score", "Artist", 3);
        listA.insert("High Score", "Artist", 5);

        SongList filtered = listA.findSongsWithinScoreRange(2, 5);
        // Expect to see only "Mid Score" (3) and "High Score" (5)

        Iterator<Song> it = filtered.iteratorByScore();
        assertTrue(it.hasNext(), "Should have some songs in the given range");
        Song first = it.next(); // nodes should be sorted in decreasing order of the scores
        assertEquals("High Score", first.getTitle());
        assertEquals(5, first.getScore());

        Song second = it.next();
        assertEquals("Mid Score", second.getTitle());
        assertEquals(3, second.getScore());

        assertFalse(it.hasNext(), "No more songs in filtered list");
    }

    @Test
    public void testMergeWith() {
        listA.insert("Love Story", "Taylor Swift", 5);
        listA.insert("Bad Habits", "Ed Sheeran", 3);

        listB.insert("Hello", "Adele", 4);
        listB.insert("Roar", "Katy Perry", 5);

        SongList merged = listA.mergeWith(listB);

        // Expect scores in the merged list to be: 5, 5, 4, 3
        // Love Story should come before Roar - both have the same score of 5, so arranged alphabetically.
        Iterator<Song> scoreIt = merged.iteratorByScore();
        Song s1 = scoreIt.next();
        assertEquals("Love Story", s1.getTitle());
        assertEquals(5, s1.getScore());

        Song s2 = scoreIt.next();
        assertEquals("Roar", s2.getTitle());
        assertEquals(5, s2.getScore());

        Song s3 = scoreIt.next();
        assertEquals("Hello", s3.getTitle());
        assertEquals(4, s3.getScore());

        Song s4 = scoreIt.next();
        assertEquals("Bad Habits", s4.getTitle());
        assertEquals(3, s4.getScore());

        assertFalse(scoreIt.hasNext());

        // If we traverse the list by title, it should be:
        // "Bad Habits", "Hello", "Love Story", "Roar"
        Iterator<Song> titleIt = merged.iteratorByTitle();
        Song t1 = titleIt.next();
        assertEquals("Bad Habits", t1.getTitle());

        Song t2 = titleIt.next();
        assertEquals("Hello", t2.getTitle());

        Song t3 = titleIt.next();
        assertEquals("Love Story", t3.getTitle());

        Song t4 = titleIt.next();
        assertEquals("Roar", t4.getTitle());

        assertFalse(titleIt.hasNext());
    }

    @Test
    public void testLoadSongsNonExistentFile() {
        // Code should not crash if no file exists
        listA.loadSongs("nonExistentFile.csv"); // Should handle IOException, not crash
        assertTrue(true, "No crash on non-existent file");
    }

    @Test
    public void testLoadSongsFromCsv() {
        String csvPath = "src/main/resources/songs.csv"; // valid path
        list.loadSongs(csvPath);
        // Check several songs if they were added to the list:
        assertTrue(list.containsSong("Birds of a Feather", "Billie Eilish"),
                "Should contain 'Birds of a Feather' by Billie Eilish");
        assertTrue(list.containsSong("Just the Way You Are", "Bruno Mars"),
                "Should contain 'Just the Way You Are' by Bruno Mars");
        assertTrue(list.containsSong("Hotel California", "Eagles"),
                "Should contain 'Hotel California' by Eagles");
        // Invalid song that is not in the file:
        assertFalse(list.containsSong("Random Title", "Unknown Artist"),
                "Should not contain arbitrary missing songs");
    }

    @Test
    public void testFindBestK() {
        // Insert songs with descending scores 5,4,4,3
        list.insert("SongA", "ArtistA", 5);
        list.insert("SongB", "ArtistB", 4);
        list.insert("SongC", "ArtistC", 4);
        list.insert("SongD", "ArtistD", 3);

        // findBestKSongs(2) - expect to see two best songs: SongA(5), SongB(4)
        SongList best2 = list.findBestKSongs(2);
        Iterator<Song> it = best2.iteratorByScore();
        assertTrue(it.hasNext());
        Song first = it.next();
        assertEquals("SongA", first.getTitle());
        assertEquals(5, first.getScore());

        assertTrue(it.hasNext());
        Song second = it.next();
        assertEquals("SongB", second.getTitle());
        assertEquals(4, second.getScore());

        assertFalse(it.hasNext(), "Should only have 2 songs in best2");

        // findBestKSongs(10) => we only have 4, return all of them
        SongList best10 = list.findBestKSongs(10);
        Iterator<Song> it10 = best10.iteratorByScore();
        assertEquals("SongA", it10.next().getTitle());
        assertEquals("SongB", it10.next().getTitle());
        assertEquals("SongC", it10.next().getTitle());
        assertEquals("SongD", it10.next().getTitle());
        assertFalse(it10.hasNext());
    }

    @Test
    public void testFindWorstK() {
        // Chain by score in descending order:
        // Head-> SongX(5)-> SongY(4)-> SongZ(4)-> SongW(3)
        list.insert("SongW", "ArtistW", 3);
        list.insert("SongZ", "ArtistZ", 4);
        list.insert("SongY", "ArtistY", 4);
        list.insert("SongX", "ArtistX", 5);

        // findWorstKSongs(1) => expect SongW with the lowest score of 3.
        SongList worst1 = list.findWorstKSongs(1);
        Iterator<Song> it = worst1.iteratorByScore();
        assertTrue(it.hasNext());
        Song only = it.next();
        assertEquals("SongW", only.getTitle());
        assertEquals(3, only.getScore());
        assertFalse(it.hasNext());

        // findWorstKSongs(2): SongZ(4), SongW(3)
        SongList worst2 = list.findWorstKSongs(2);
        Iterator<Song> it2 = worst2.iteratorByScore();
        assertTrue(it2.hasNext());
        Song first = it2.next(); // 4 (SongZ)
        Song second = it2.next(); // 3 (SongW)
        assertFalse(it2.hasNext());

        assertTrue(first.getScore() == 4, "Expect 4");
        assertTrue(second.getScore() == 3, "Expect 3");

        // findWorstKSongs(10): return all songs we have (4)
        SongList worst10 = list.findWorstKSongs(10);
        Iterator<Song> it10 = worst10.iteratorByScore();
        // Should contain all 4 in descending order: 5,4,4,3
        assertEquals(5, it10.next().getScore());
        assertEquals(4, it10.next().getScore());
        assertEquals(4, it10.next().getScore());
        assertEquals(3, it10.next().getScore());
        assertFalse(it10.hasNext());
    }

    @Test
    public void testFindBestAndWorstEdgeCases() {
        SongList emptyBest = list.findBestKSongs(3);
        assertFalse(emptyBest.iteratorByScore().hasNext(), "Empty list => no best songs");

        SongList emptyWorst = list.findWorstKSongs(2);
        assertFalse(emptyWorst.iteratorByScore().hasNext(), "Empty list => no worst songs");

        list.insert("Solo", "OneArtist", 5);
        SongList best1 = list.findBestKSongs(1);
        assertTrue(best1.containsSong("Solo", "OneArtist"));

        SongList worst1 = list.findWorstKSongs(1);
        assertTrue(worst1.containsSong("Solo", "OneArtist"));

        // Negative or zero k
        SongList none = list.findBestKSongs(0);
        assertFalse(none.iteratorByScore().hasNext(), "Zero k => empty result");
        SongList neg = list.findWorstKSongs(-5);
        assertFalse(neg.iteratorByScore().hasNext(), "Negative k => empty result");
    }
}