package proj2;
import java.util.ArrayList;

enum Genre {
	HIPHOP,
	RNB,
	INDIEROCK;
}

public class PlaylistProcessor {
	private ArrayList<Song> songList;
	
	public PlaylistProcessor (ArrayList<Song> songList){
		this.songList = songList;
	}
	
	public void displayPlaylist() {
		int rnbCount = 0;
		int indieCount = 0;
		int hiphopCount = 0;
		int fSongs = 0;
		int dSongs = 0;
		int sSongs = 0;
		int hSongs = 0;
		int tSongs = 0;
		
		for(Song s : songList) {
			Genre genre = null;
			if(s.getArtist().equals("Frank Ocean")) {
				genre = Genre.RNB;
				fSongs += 1;
			}else if(s.getArtist().equals("Daniel Caesar")) {
				genre = Genre.RNB;
				dSongs += 1;
			}else if(s.getArtist().equals("SZA")) {
				genre = Genre.RNB;
				sSongs += 1;
			}else if(s.getArtist().equals("Hers")) {
				genre = Genre.INDIEROCK;
				hSongs += 1;
			}else if(s.getArtist().equals("Tyler The Creator")) {
				genre = Genre.HIPHOP;
				tSongs += 1;
			}else {
				System.out.println("Artist doesn't fit any categories avaiable to evaluate.");
			}
			switch(genre) {
			case RNB:
				System.out.println("The song '" +s.getTitle() + "' by " + s.getArtist() +  " in the album, " + s.getAlbum() + ", was added to the playlist! It is an R&B/Soul Song!");
				rnbCount += 1;
				break;
			case HIPHOP:
				System.out.println("The song '" + s.getTitle() + "' by " + s.getArtist() + " in the album, " + s.getAlbum() + ", was added to the playlist! It is either a HipHop + Rap, or HipHop + NeoSoul Song!");
				hiphopCount += 1;
				break;
			case INDIEROCK:
				System.out.println("The song '" +s.getTitle() + "' by " + s.getArtist() + " in the album, " + s.getAlbum() + ", was added to the playlist! It is an Indie Rock / Alternative Song!");
				indieCount += 1;
				break;
			default:
				System.out.println("This artist is unknown.");
			}
	
		}
		//Output/Results
		System.out.println("There are: ");
		System.out.println(rnbCount + " R&B songs, " + hiphopCount + " Hiphop songs, and " + indieCount + " Indie Rock songs in the Playlist.");
		System.out.println("R&B is the most listened to genre!");
		System.out.println("There are:");
		System.out.println(fSongs + " songs by Frank Ocean");
		System.out.println(dSongs + " songs by Daniel Caesar");
		System.out.println(tSongs + " songs by Tyler the Creator");
		System.out.println(sSongs + " songs by SZA");
		System.out.println(hSongs + " songs by Her's");	
		System.out.println("Daniel Caesar and Frank Ocean are tied for the most listened to artist in the playlist.");
		
	}
}
