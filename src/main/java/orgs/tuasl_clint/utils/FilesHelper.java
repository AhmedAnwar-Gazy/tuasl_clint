package orgs.tuasl_clint.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;

public class FilesHelper {
    public static enum fileType{
        INVALID, IMAGE, VIDEO, AUDIO, STICKER, TEXT, FILE
    }
    public static fileType getFileType(File file) {
        if (file == null || !file.exists()) {
            return fileType.INVALID;
        }

        try {
            String mimeType = Files.probeContentType(file.toPath());

            // First check by extension for more specific control
            return switch (getFileExtension(file)) {
                // Image formats
                case "jpg", "jpeg", "png", "gif", "bmp", "webp" -> fileType.IMAGE;

                // Video formats
                case "mp4", "mov", "avi", "mkv", "webm", "wmv" -> fileType.VIDEO;

                // Audio formats
                case "mp3", "wav", "ogg", "flac", "aac" -> fileType.AUDIO;

                // Stickers (could be special image formats)
                case "tgs" ->  // Telegram stickers
                        fileType.STICKER;
                default -> {
                    // Fallback to MIME type if extension not recognized
                    if (mimeType != null) {
                        if (mimeType.startsWith("image/")) {
                            yield fileType.IMAGE;
                        } else if (mimeType.startsWith("video/")) {
                            yield fileType.VIDEO;
                        } else if (mimeType.startsWith("audio/")) {
                            yield fileType.AUDIO;
                        }
                    }
                    yield fileType.FILE;
                }
            };
        } catch (IOException | StringIndexOutOfBoundsException e) {
            return fileType.INVALID;
        }
    }public static fileType getFileType(String extenion) {
            // First check by extension for more specific control
            return switch (extenion) {
                // Image formats
                case "jpg", "jpeg", "png", "gif", "bmp", "webp", "image" -> fileType.IMAGE;

                // Video formats
                case "mp4", "mov", "avi", "mkv", "webm", "wmv","video" -> fileType.VIDEO;

                // Audio formats
                case "mp3", "wav", "ogg", "flac", "aac", "audio" -> fileType.AUDIO;

                // Stickers (could be special image formats)
                case "tgs" ->  // Telegram stickers
                        fileType.STICKER;
                default -> fileType.FILE;
            };
    }
    public static fileType toMediaType(String type){
        return switch (type){
            case "text","TEXT","TXT","txt" -> fileType.TEXT;
            case "video","VIDEO","Video" -> fileType.VIDEO;
            case "image","Image","IMAGE" -> fileType.IMAGE;
            case "audio","Audio","AUDIO" -> fileType.AUDIO;
            case null -> fileType.INVALID;
            default -> fileType.FILE;
        };
    }
    public static fileType getFileType(Path path) {
        return getFileType(path.toFile());
    }
    public static String getFilePath(File file) throws IOException {
        String path = "src/main/resources/orgs/tuasl_clint/";
        switch(getFileType(file)){
            case VIDEO ->path += "videos";
            case IMAGE ->path+= "images";
            case FILE -> path += "file";
            case STICKER -> path += "sticker";
            case AUDIO -> path += "voiceNote";
            default -> throw new IOException("this file is not good!!");
        }
        return path;
    }
    public static String getFilePath(fileType type) throws IOException {
        String path = "src/main/resources/orgs/tuasl_clint/";
        switch(type){
            case VIDEO ->path += "videos";
            case IMAGE ->path+= "images";
            case FILE -> path += "file";
            case STICKER -> path += "sticker";
            case AUDIO -> path += "voiceNote";
            default -> throw new IOException("this file is not good!!");
        }
        return path;
    }
    public static String getMediaViewerPath(File file)throws Exception{
        String path = "/orgs/tuasl_clint/fxml/";
        switch(getFileType(file)){
            case VIDEO ->path += "videoItem.fxml";
            case IMAGE ->path+= "imageItem.fxml";
            case FILE, STICKER -> path += "fileItem.fxml";
            case AUDIO -> path += "audioItem.fxml";
            default -> throw new Exception("this file is not good!!");
        }
        return path;
    }
    public static long getFileSize(File file){
        if(file == null)
            return 0;
        return file.length();
    }
    public static String getFileExtension(File file){
        if(file == null)
            return "";
        int dotIndex = file.getName().lastIndexOf('.');
        return (dotIndex < file.getName().length() - 1 && dotIndex > 0)? file.getName().substring(dotIndex + 1).toLowerCase() : file.getName();
    }
    public static String formatFileSize(long size) {
        if (size <= 0) return "0 B";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

}
