package orgs.model;

import java.time.LocalDateTime;

public class Media {
    private int id;
    private String filePathOrUrl;
    private String thumbnailUrl;
    private Long fileSize; // Use Long for BIGINT
    private String mediaType;
    private int uploadedByUserId;
    private LocalDateTime uploadedAt;
    private String fileName;
    private String transferId;

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    // Constructors
    public Media() {
    }

    public Media(int id, String filePathOrUrl, String thumbnailUrl, Long fileSize, String mediaType, int uploadedByUserId, LocalDateTime uploadedAt ,String fileName) {
        this.id = id;
        this.filePathOrUrl = filePathOrUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.uploadedByUserId = uploadedByUserId;
        this.uploadedAt = uploadedAt;
        this.fileName = fileName ;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePathOrUrl() {
        return filePathOrUrl;
    }

    public void setFilePathOrUrl(String filePathOrUrl) {
        this.filePathOrUrl = filePathOrUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(int uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    //--------------------------

    public Media(Integer id, String filePathOrUrl, String thumbnailUrl, Long fileSize, String mediaType, int uploadedByUserId, LocalDateTime uploadedAt) {
        this.id = id;
        this.filePathOrUrl = filePathOrUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.uploadedByUserId = uploadedByUserId;
        this.uploadedAt = uploadedAt;
    }


    // --- Setters ---
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", filePathOrUrl='" + filePathOrUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", uploadedByUserId=" + uploadedByUserId +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
