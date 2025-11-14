package songlist;

/** A class representing a song with the given title, artist, and score (rating). */
public class Song {
    private String title;
    private String artist;
    private int songScore; // score(rating) of a song

    /**
     * Constructor
     * @param title title of the song
     * @param artist artist's name
     * @param songScore score from 1 to 5
     */
    public Song(String title, String artist, int songScore) {
        this.title = title;
        this.artist = artist;
        this.songScore = songScore;
    }

    /**
     * Return the title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return the name of the artist
     * @return artist's name
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Return the score (rating)
     * @return score
     */
    public int getScore() {
        return songScore;
    }

    /**
     * Return a song as a string
     * @return string that contains title, artist and score
     */
    @Override
    public String toString() {
        return title + " by " + artist + "; score " + songScore;
    }

}
