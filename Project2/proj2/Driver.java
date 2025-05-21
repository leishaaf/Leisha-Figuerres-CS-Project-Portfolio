package proj2;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Driver {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("songs.txt");
		ArrayList<Song> songList = new ArrayList<>();
		Scanner scan = new Scanner(file);
		scan.useDelimiter(", ");
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] split = line.split(", ");
			String artist = split[0];
			String title = split[1];
			String album = split[2];
			Song song = new Song(artist, title, album);
			songList.add(song);
		}
		scan.close();
		System.out.println("Welcome to the Playlist Processor Program!");
		SongRefiner nSongList = new SongRefiner(songList);
		ArrayList<Song> newSongList = nSongList.refineSongs(songList);
		PlaylistProcessor sList = new PlaylistProcessor(newSongList);
		
// ORIGINAL LINES OF TEXT FILE AS SONG OBJECTS
//		int c = 0;
//		for(Song s: songList) {
//			c += 1;
//			System.out.println(c + " " + s);
//		}
//	
	sList.displayPlaylist();
}
}
