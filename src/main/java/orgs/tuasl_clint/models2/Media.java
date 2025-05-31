package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;
import java.math.BigInteger;

public class Media {
    private BigInteger mediaId;
    private BigInteger uploaderUserId;
    private String fileName;
    private String filePathOrUrl;
    private String mimeType;
    private BigInteger fileSizeBytes;
    private String thumbnailUrl;
    private Integer durationSeconds;
    private Integer width;
    private Integer height;
    private Timestamp uploadedAt;

    public Media() {}

    public Media(String fileName, String filePathOrUrl, String mimeType, BigInteger fileSizeBytes, Timestamp uploadedAt) {
        this.fileName = fileName;
        this.filePathOrUrl = filePathOrUrl;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
        this.uploadedAt = uploadedAt;
    }

    public Media(BigInteger mediaId, BigInteger uploaderUserId, String fileName, String filePathOrUrl, String mimeType, BigInteger fileSizeBytes, String thumbnailUrl, Integer durationSeconds, Integer width, Integer height, Timestamp uploadedAt) {
        this.mediaId = mediaId;
        this.uploaderUserId = uploaderUserId;
        this.fileName = fileName;
        this.filePathOrUrl = filePathOrUrl;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
        this.thumbnailUrl = thumbnailUrl;
        this.durationSeconds = durationSeconds;
        this.width = width;
        this.height = height;
        this.uploadedAt = uploadedAt;
    }

    public BigInteger getMediaId() { return mediaId; }
    public void setMediaId(BigInteger mediaId) { this.mediaId = mediaId; }
    public BigInteger getUploaderUserId() { return uploaderUserId; }
    public void setUploaderUserId(BigInteger uploaderUserId) { this.uploaderUserId = uploaderUserId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePathOrUrl() { return filePathOrUrl; }
    public void setFilePathOrUrl(String filePathOrUrl) { this.filePathOrUrl = filePathOrUrl; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public BigInteger getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(BigInteger fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }

    public boolean save() throws SQLException {
        String sql = "INSERT INTO media (uploader_user_id, file_name, file_path_or_url, mime_type, file_size_bytes, thumbnail_url, duration_seconds, width, height, uploaded_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, uploaderUserId != null ? uploaderUserId.longValue() : 0);
            statement.setString(2, fileName);
            statement.setString(3, filePathOrUrl);
            statement.setString(4, mimeType);
            statement.setLong(5, fileSizeBytes.longValue());
            statement.setString(6, thumbnailUrl);
            statement.setObject(7, durationSeconds);
            statement.setObject(8, width);
            statement.setObject(9, height);
            statement.setTimestamp(10, uploadedAt);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.mediaId = BigInteger.valueOf(generatedKeys.getLong(1));
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE media SET uploader_user_id = ?, file_name = ?, file_path_or_url = ?, mime_type = ?, file_size_bytes = ?, thumbnail_url = ?, duration_seconds = ?, width = ?, height = ? WHERE media_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, uploaderUserId != null ? uploaderUserId.longValue() : 0);
            statement.setString(2, fileName);
            statement.setString(3, filePathOrUrl);
            statement.setString(4, mimeType);
            statement.setLong(5, fileSizeBytes.longValue());
            statement.setString(6, thumbnailUrl);
            statement.setObject(7, durationSeconds);
            statement.setObject(8, width);
            statement.setObject(9, height);
            statement.setLong(10, mediaId.longValue());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM media WHERE media_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, mediaId.longValue());
            return statement.executeUpdate() > 0;
        }
    }
}