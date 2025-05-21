package dao;

import Model.Playlist;
import Model.Songs;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayListDao 
{
    // Insert playlist into database
    public void insertPlaylist(Playlist playlist) {
        try (Connection connection = DBConfig.getConnection()) {
            String query = "INSERT INTO Playlists (PlaylistName) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, playlist.getPlaylistName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insertSongIntoPlaylist(int playlistID, int songID) {
        try (Connection connection = DBConfig.getConnection()) {
            String query = "INSERT INTO PlaylistSongs (PlaylistID, SongID) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, playlistID);
                preparedStatement.setInt(2, songID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Retrieve all playlists from the database
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        try (Connection connection = DBConfig.getConnection()) {
            String query = "SELECT * FROM Playlists";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Playlist playlist = new Playlist();
                    playlist.setPlaylistID(resultSet.getInt("PlaylistID"));
                    playlist.setPlaylistName(resultSet.getString("PlaylistName"));
                    playlists.add(playlist);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    // Retrieve all songs associated with a playlist
    public List<Integer> getSongsInPlaylist(int playlistID) {
        List<Integer> songIDs = new ArrayList<>();
        try (Connection connection = DBConfig.getConnection()) {
            String query = "SELECT SongID FROM PlaylistSongs WHERE PlaylistID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, playlistID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    songIDs.add(resultSet.getInt("SongID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songIDs;
    }
}
