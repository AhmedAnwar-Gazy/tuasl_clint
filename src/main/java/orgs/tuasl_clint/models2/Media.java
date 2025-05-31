package orgs.tuasl_clint.models2;

import java.math.BigInteger;
import java.sql.Timestamp;

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

    // Constructors
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


    public BigInteger getMediaId() {
        return mediaId;
    }

    public BigInteger getUploaderUserId() {
        return uploaderUserId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePathOrUrl() {
        return filePathOrUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public BigInteger getFileSizeBytes() {
        return fileSizeBytes;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }
    @Override
    public String toString() {
        return "Media{" +
                "mediaId=" + mediaId +
                ", uploaderUserId=" + uploaderUserId +
                ", fileName='" + fileName + '\'' +
                ", filePathOrUrl='" + filePathOrUrl + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", fileSizeBytes=" + fileSizeBytes +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", durationSeconds=" + durationSeconds +
                ", width=" + width +
                ", height=" + height +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
