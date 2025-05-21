package proj2;

import java.util.ArrayList;

public class SongRefiner {
private ArrayList<Song> songList;
	
	public SongRefiner (ArrayList<Song> songList){
		this.songList = songList;
	}
	
	public ArrayList<Song> refineSongs(ArrayList<Song> songList){
		ArrayList<Song> newSongList = new ArrayList<>();
		String previous = songList.get(0).getTitle();
		newSongList.add(songList.get(0));
		for(Song s: songList) {
			String current = s.getTitle();
			if(!previous.equalsIgnoreCase(current)) {
				newSongList.add(s);
				previous = current;
			}
		}
	return newSongList;
	}
}
