import Model.Playlist;
import Model.Songs;
import dao.AudioPlayer;
import dao.PlayListDao;
import dao.SongDAO;
import java.util.Scanner;
import java.util.List;

public class main {
    public static void main(String[] args)
    {
        System.out.println("Welcome to Jukebox");
        Scanner scan = new Scanner(System.in);
        SongDAO songDAO = new SongDAO();
        PlayListDao playListDao = new PlayListDao();

 
        List<Songs> songList = songDAO.getAllSongs();
        songDAO.displaySongs(songList);


        System.out.println("Choose Song Title , Artist , Album or Genre"); 
        String searchCategory = scan.nextLine();
        String searchTerm = scan.nextLine();


        List<Songs> searchResults = songDAO.searchSongs(songList, searchCategory, searchTerm);
        songDAO.displaySongs(searchResults);


        System.out.println("Do you want to create a new playlist or add a song to an existing playlist? (new/existing)");
        String choice = scan.nextLine();

        if (choice.equalsIgnoreCase("new"))
        {

               System.out.println("Enter the name for the new playlist:");
            Playlist newPlaylist = new Playlist();
            newPlaylist.setPlaylistName(scan.nextLine());
            playListDao.insertPlaylist(newPlaylist);
            System.out.println("New playlist created.");
        }
        else if (choice.equalsIgnoreCase("existing"))
        {

            List<Playlist> playlists = playListDao.getAllPlaylists();
            if (playlists.isEmpty())
            {
                System.out.println("No playlists found. Please create a new playlist first.");
                return;
            }

            System.out.println("Existing Playlists:");
            for (Playlist playlist : playlists)
            {
                System.out.printf("ID: %d, Name: %s%n", playlist.getPlaylistID(), playlist.getPlaylistName());
            }


            System.out.println("Enter the ID of the playlist you want to add a song to:");
            int playlistID = scan.nextInt();
            scan.nextLine(); // Consume newline


            System.out.println("Enter the ID of the song you want to add to the playlist:");
            int songID = scan.nextInt();


            Songs songToAdd = null;
            for (Songs song : songList)
            {
                if (song.getSongID() == songID)
                {
                    songToAdd = song;
                    break;
                }
            }

            if (songToAdd != null)
            {
                playListDao.insertSongIntoPlaylist(playlistID, songID);
                System.out.println("Song added to the playlist.");
            } else
            {
                System.out.println("Song not found.");
            }
        }
        else {
            System.out.println("Invalid option. Please choose 'new' or 'existing'.");
        }
        System.out.println("Enter the ID of the playlist you want to play:");
        int playlistIDToPlay = scan.nextInt();

        List<Integer> songIDsInPlaylist = playListDao.getSongsInPlaylist(playlistIDToPlay);

        AudioPlayer audioPlayer = new AudioPlayer();
        for (int songID : songIDsInPlaylist) {
            // Find the song by ID
            Songs songToPlay = null;
            for (Songs song : songList) {
                if (song.getSongID() == songID) {
                    songToPlay = song;
                    break;
                }
            }

            if (songToPlay != null) {

                audioPlayer.play(songToPlay.getFilePath());
                controlPlayback(audioPlayer, scan);
            } else {
                System.out.println("Song not found in the playlist.");
            }
        }
        scan.close();
        System.out.println("Thanks for Listening song....");
    }


    private static void controlPlayback(AudioPlayer audioPlayer, Scanner scanner) {
        while (true) {
            System.out.println("Select an option: ");
            System.out.println("1. Pause");
            System.out.println("2. Resume");
            System.out.println("3. Stop");
            System.out.println("4. Forward");
            System.out.println("5. Rewind");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    audioPlayer.pause();
                    break;
                case 2:
                    audioPlayer.resume();
                    break;
                case 3:
                    audioPlayer.stop();
                    break;
                case 4:
                    System.out.println("Enter milliseconds to forward:");
                    long forwardMillis = scanner.nextLong();
                    audioPlayer.forward(forwardMillis);
                    break;
                case 5:
                    System.out.println("Enter milliseconds to rewind:");
                    long rewindMillis = scanner.nextLong();
                    audioPlayer.rewind(rewindMillis);
                    break;
                case 6:
                    audioPlayer.stop();
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }


    }

    private static void displayPlaylists(List<Playlist> playlists) {
        System.out.println("Available Playlists:");
        for (Playlist playlist : playlists) {
            System.out.printf("ID: %d, Name: %s%n", playlist.getPlaylistID(), playlist.getPlaylistName());
        }
    }

    private static Playlist selectPlaylist(List<Playlist> playlists, Scanner scanner) {
        displayPlaylists(playlists);
        System.out.println("Enter the ID of the playlist you want to play:");
        int playlistID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        for (Playlist playlist : playlists) {
            if (playlist.getPlaylistID() == playlistID) {
                return playlist;
            }
        }
        return null; // Playlist not found
    }

}
