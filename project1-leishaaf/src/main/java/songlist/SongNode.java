package songlist;

/** A class representing a node in the song list. Each SongNode
 *  stores the song and two "next" pointers (nextByTitle and nextByScore).
  */
public class SongNode {
    private Song song;
    private SongNode nextByTitle; // Next node in ascending title order
    private SongNode nextByScore; // Next node in descending score order

    /**
     * Constuctor
     * @param song song
     */
    public SongNode(Song song) {
        this.song = song;
    }

    /**
     * Getter for the song
     * @return song
     */
    public Song getSong() {
        return song;
    }

    /**
     * Return the "next" node in the SongList in the ascending alphabetical order of the title.
     * @return SongNode
     */
    public SongNode getNextByTitle() {
        return nextByTitle;
    }

    /**
     * Return the "next" node in the SongList in the descending order of the score
     * @return SongNode
     */
    public SongNode getNextByScore() {
        return nextByScore;
    }


    /**
     * Set the "nextByTitle" pointer to the given node
     * @param nextByTitle
     */
    public void setNextByTitle(SongNode nextByTitle) {
        this.nextByTitle = nextByTitle;
    }

    /**
     * Sets the "nextByScore" pointer to the given node.
     * @param nextByScore
     */
    public void setNextByScore(SongNode nextByScore) {
        this.nextByScore = nextByScore;
    }
}
