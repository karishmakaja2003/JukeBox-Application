package dao;

import Model.Songs;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    List<Songs> listofsongs;
    public List<Songs> getAllSongs() {
        listofsongs = new ArrayList<>();
        System.out.println("Welcome to Song DAO");
        try (Connection connection = DBConfig.getConnection()) {
           System.out.println("SongDAO-After DB Connection");
            String query = "SELECT * FROM Songs";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                System.out.println("SongDAO-After prepare statement");
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("SongDAO-After Execute Query");
                while (resultSet.next()) {
                    Songs song = new Songs();
                    song.setSongID(resultSet.getInt("SongID"));
                    song.setTitle(resultSet.getString("Title"));
                    song.setArtist(resultSet.getString("Artist"));
                    song.setGenre(resultSet.getString("Genre"));
                    song.setAlbum(resultSet.getString("Album"));
                    song.setDuration(resultSet.getTime("Duration"));
                    song.setFilePath(resultSet.getString("FilePath"));
                    listofsongs.add(song);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listofsongs;
    }

    // Method to search songs based on category (Title, Artist, Album, Genre) and search term
    public static List<Songs> searchSongs(List<Songs> songList, String searchCategory, String searchTerm) {
        List<Songs> searchResults = new ArrayList<>();
        searchTerm = searchTerm.toLowerCase(); // Convert search term to lowercase for case-insensitive comparison

        for (Songs song : songList) {
            switch (searchCategory.toLowerCase()) { // Convert category to lowercase for consistency
                case "title":
                    if (song.getTitle().toLowerCase().contains(searchTerm)) {
                        searchResults.add(song);
                    }
                    break;
                case "artist":
                    if (song.getArtist().toLowerCase().contains(searchTerm)) {
                        searchResults.add(song);
                    }
                    break;
                case "album":
                    if (song.getAlbum().toLowerCase().contains(searchTerm)) {
                        searchResults.add(song);
                    }
                    break;
                case "genre":
                    if (song.getGenre().toLowerCase().contains(searchTerm)) {
                        searchResults.add(song);
                    }
                    break;
                default:
                    System.out.println("Invalid search category. Please use 'Title', 'Artist', 'Album', or 'Genre'.");
                    return searchResults; // Return empty list for invalid category
            }
        }
        return searchResults;
    }



    public static void displaySongs(List<Songs> viewsongs) {

        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-30s | %-20s | %-15s | %-15s | %-10s |%n",
                "ID", "Title", "Artist", "Genre", "Album", "Duration");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Songs song : viewsongs) {
            System.out.printf("| %-5d | %-30s | %-20s | %-15s | %-15s | %-10s |%n",
                    song.getSongID(), song.getTitle(), song.getArtist(), song.getGenre(),
                    song.getAlbum(), song.getDuration());
        }
        System.out.println("---------------------------------------------------------------------------------------------");

    }

}

