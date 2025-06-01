package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;


public class Media {
    private long mediaId;
    private long uploaderUserId;
    private String fileName;
    private String filePathOrUrl;
    private String mimeType;
    private long fileSizeBytes;
    private String thumbnailUrl;
    private Integer durationSeconds;
    private Integer width;
    private Integer height;
    private Timestamp uploadedAt;

    public Media() {}

    public Media(String fileName, String filePathOrUrl, String mimeType, long fileSizeBytes, Timestamp uploadedAt) {
        this.fileName = fileName;
        this.filePathOrUrl = filePathOrUrl;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
        this.uploadedAt = uploadedAt;
    }

    public Media(long mediaId, long uploaderUserId, String fileName, String filePathOrUrl, String mimeType, long fileSizeBytes, String thumbnailUrl, Integer durationSeconds, Integer width, Integer height, Timestamp uploadedAt) {
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

    public long getMediaId() { return mediaId; }
    public void setMediaId(long mediaId) { this.mediaId = mediaId; }
    public long getUploaderUserId() { return uploaderUserId; }
    public void setUploaderUserId(long uploaderUserId) { this.uploaderUserId = uploaderUserId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePathOrUrl() { return filePathOrUrl; }
    public void setFilePathOrUrl(String filePathOrUrl) { this.filePathOrUrl = filePathOrUrl; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
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
            statement.setLong(1, uploaderUserId != 0 ? uploaderUserId : 0);
            statement.setString(2, fileName);
            statement.setString(3, filePathOrUrl);
            statement.setString(4, mimeType);
            statement.setLong(5, fileSizeBytes);
            statement.setString(6, thumbnailUrl);
            statement.setObject(7, durationSeconds);
            statement.setObject(8, width);
            statement.setObject(9, height);
            statement.setTimestamp(10, uploadedAt);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.mediaId = (generatedKeys.getLong(1));
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE media SET uploader_user_id = ?, file_name = ?, file_path_or_url = ?, mime_type = ?, file_size_bytes = ?, thumbnail_url = ?, duration_seconds = ?, width = ?, height = ? WHERE media_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, uploaderUserId != 0 ? uploaderUserId : 0);
            statement.setString(2, fileName);
            statement.setString(3, filePathOrUrl);
            statement.setString(4, mimeType);
            statement.setLong(5, fileSizeBytes);
            statement.setString(6, thumbnailUrl);
            statement.setObject(7, durationSeconds);
            statement.setObject(8, width);
            statement.setObject(9, height);
            statement.setLong(10, mediaId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM media WHERE media_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, mediaId);
            return statement.executeUpdate() > 0;
        }
    }
}