package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.Media;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MediaFactory {
    public Media create() {
        return new Media();
    }

    public Media createFromResultSet(ResultSet rs) throws SQLException {
        return new Media(
                BigInteger.valueOf(rs.getLong("media_id")),
                rs.getLong("uploader_user_id") != 0 ? BigInteger.valueOf(rs.getLong("uploader_user_id")) : null,
                rs.getString("file_name"),
                rs.getString("file_path_or_url"),
                rs.getString("mime_type"),
                BigInteger.valueOf(rs.getLong("file_size_bytes")),
                rs.getString("thumbnail_url"),
                rs.getObject("duration_seconds") != null ? rs.getInt("duration_seconds") : null,
                rs.getObject("width") != null ? rs.getInt("width") : null,
                rs.getObject("height") != null ? rs.getInt("height") : null,
                rs.getTimestamp("uploaded_at")
        );
    }

    public Media findById(BigInteger mediaId) throws SQLException {
        String sql = "SELECT * FROM media WHERE media_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, mediaId.longValue());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Media> findByUploader(BigInteger uploaderUserId) throws SQLException {
        String sql = "SELECT * FROM media WHERE uploader_user_id = ?";
        List<Media> mediaList = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, uploaderUserId.longValue());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(createFromResultSet(rs));
                }
            }
        }
        return mediaList;
    }

    public List<Media> findByMimeType(String mimeType) throws SQLException {
        String sql = "SELECT * FROM media WHERE mime_type LIKE ?";
        List<Media> mediaList = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, "%" + mimeType + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(createFromResultSet(rs));
                }
            }
        }
        return mediaList;
    }

    public List<Media> findByFileName(String fileName) throws SQLException {
        String sql = "SELECT * FROM media WHERE file_name LIKE ?";
        List<Media> mediaList = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, "%" + fileName + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(createFromResultSet(rs));
                }
            }
        }
        return mediaList;
    }

    public List<Media> findRecent(int limit) throws SQLException {
        String sql = "SELECT * FROM media ORDER BY uploaded_at DESC LIMIT ?";
        List<Media> mediaList = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(createFromResultSet(rs));
                }
            }
        }
        return mediaList;
    }
}