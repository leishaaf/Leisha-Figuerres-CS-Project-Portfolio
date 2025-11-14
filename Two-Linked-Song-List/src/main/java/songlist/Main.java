package songlist;

import java.sql.SQLOutput;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        SongList playList = new SongList();
        playList.loadSongs("src/main/resources/songs.csv");
        SongList playList2 = new SongList();
        // FILL IN CODE: call all methods to test them
        // Also use iterators to traverse the list by title and by score
        playList2.insert("See You Again", "Tyler the Creator", 4);
        playList2.insert("Dark Red", "Steve Lacy", 4);
        playList2.insert("Lovers Rock", "TV Girl", 5);
        playList2.insert("7 Summers", "Morgan Wallen", 3);
        playList2.insert("Japanese Denim", "Daniel Caesar", 2);
        playList2.insert("Death and Taxes", "Daniel Caesar", 3);
        playList2.insert("Ivy", "Frank Ocean", 4);
        playList2.insert("Baby", "Summer Walker", 1);
        playList2.insert("Prom", "SZA", 4);
        playList2.insert("Self Control", "Frank Ocean", 1);
        playList2.insert("Rusty", "Tyler the Creator", 5);

         // Testing insert by printing score and title iterations
        System.out.println("Printing Original Playlist by Title:");
        Iterator<Song> itByTitle = playList.iteratorByTitle();
        while(itByTitle.hasNext()){ System.out.println(itByTitle.next());}
        System.out.println();
        System.out.println("Printing Original Playlist by Score:");
        Iterator<Song> playListIt = playList.iteratorByScore();
        while(playListIt.hasNext()){ System.out.println(playListIt.next()); }
        // playList 2 created by me
        System.out.println();
        System.out.println("Printing Playlist2 by Title:");
        Iterator<Song> playListItTitle2 = playList2.iteratorByTitle();
        while(playListItTitle2.hasNext()){ System.out.println(playListItTitle2.next());}
        System.out.println();
        System.out.println("Printing Playlist2 by Score:");
        Iterator<Song> playListIt2 = playList2.iteratorByScore();
        while(playListIt2.hasNext()){ System.out.println(playListIt2.next()); }
        System.out.println();

        // Testing containsSong()
        System.out.println("Does playList2 contain Self Control by Frank Ocean?: " + (playList2.containsSong("Self Control", "Frank Ocean"))); // should return true
        System.out.println("Does playList2 contain Heavy by The Marias?: " + (playList2.containsSong("Heavy", "The Marias"))); // shoul return false
        System.out.println();

        // Testing findSongsWithinScoreRange();
        SongList result = playList2.findSongsWithinScoreRange(1,3);
        Iterator<Song> pl2 = result.iteratorByTitle();
        System.out.println("Scores within range in playList2 from 1-3:");
        while(pl2.hasNext()){ System.out.println(pl2.next()); }
        System.out.println("After entering invalid number for max:");
        SongList result2 = playList2.findSongsWithinScoreRange(0,-1);
        System.out.println();

        // Testing for findBestKSongs
        System.out.println("Printing the 2 best songs:");
        SongList bestSongs = playList2.findBestKSongs(2);
        Iterator<Song> bestSongsIt = bestSongs.iteratorByScore();
        while(bestSongsIt.hasNext()){ System.out.println(bestSongsIt.next()); }
        System.out.println();
        System.out.println("Case where whole list is returned because k = 13 - greater than amount of songs in songlist");
        // where k is greater or equal to the amount of songs in songlist called with
        SongList bestSongs2 = playList2.findBestKSongs(13);
        Iterator<Song> bestSongsIt2 = bestSongs2.iteratorByScore();
        while(bestSongsIt2.hasNext()){ System.out.println(bestSongsIt2.next());}
        System.out.println("After entering invalid k:");
        SongList bestSongs3 = playList2.findBestKSongs(-2); // case where k is invalid
        System.out.println();

        //Testing mergeWith()
        SongList merged = playList.mergeWith(playList2);
        SongList emptyList = new SongList();

        System.out.println("Original playlist merged with playList2 by SCORE - both full");
        Iterator<Song> mergedBothIT = merged.iteratorByScore();
        while(mergedBothIT.hasNext()){ System.out.println(mergedBothIT.next()); }
        System.out.println();
        System.out.println("Original playlist merged with playList2 by TITLE - both full");
        Iterator<Song> mergedBothITT = merged.iteratorByTitle();
        while(mergedBothITT.hasNext()){ System.out.println(mergedBothITT.next()); }
        // cases where list called with is empty
        System.out.println();
        System.out.println("Cases where one of the lists called by are empty 1st iteration by score, 2nd by title");
        SongList mergedEmpty1 = emptyList.mergeWith(playList2);
        Iterator<Song> mergedListEmpty = mergedEmpty1.iteratorByScore();
        while(mergedListEmpty.hasNext()){ System.out.println(mergedListEmpty.next()); }
        // other list is empty
        System.out.println();
        SongList mergedEmpty2 = playList2.mergeWith(emptyList);
        Iterator<Song> mergedList2EmptyT = mergedEmpty2.iteratorByTitle();
        while(mergedList2EmptyT.hasNext()){ System.out.println(mergedList2EmptyT.next()); }

        // Testing findWorstKSongs
        System.out.println();
        System.out.println("3 Worst Songs of playList2");
        // testing worstCase
       SongList resultWorst = playList2.findWorstKSongs(3);
       Iterator<Song> worstIT = resultWorst.iteratorByScore();
       while(worstIT.hasNext()){ System.out.println(worstIT.next()); }
       System.out.println("After entering invalid k:");
       SongList resultWorst2 = playList2.findWorstKSongs(-1); // case where k is invalid
    }
}
