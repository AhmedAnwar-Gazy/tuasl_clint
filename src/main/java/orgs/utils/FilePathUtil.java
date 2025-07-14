package orgs.utils;

public class FilePathUtil {

    /**
     * Cleans a file path string by:
     * 1. Removing common problematic invisible Unicode control characters (like LRM and RLM).
     * 2. Trimming leading/trailing whitespace.
     * 3. Replacing forward slashes ('/') with backslashes ('\') to normalize for Windows paths.
     * (Note: This specifically targets Windows normalization. For cross-platform,
     * consider using File.separator or java.nio.file.Paths for more robust handling.)
     *
     * @param rawFilePath The original file path string, potentially containing issues.
     * @return A cleaned and Windows-normalized file path string.
     */
    public static String cleanAndNormalizeWindowsPath(String rawFilePath) {
        if (rawFilePath == null) {
            return null;
        }

        // Trim whitespace and remove specific problematic Unicode control characters
        // \u202A: LEFT-TO-RIGHT EMBEDDING
        // \u202B: RIGHT-TO-LEFT EMBEDDING
        // \u200E: LEFT-TO-RIGHT MARK
        // \u200F: RIGHT-TO-LEFT MARK
        String cleanedPath = rawFilePath
                .trim()
                .replace("\u202A", "")
                .replace("\u202B", "")
                .replace("\u200E", "")
                .replace("\u200F", "");

        // Replace forward slashes with backslashes for Windows compatibility.
        cleanedPath = cleanedPath.replace('/', '\\');

        return cleanedPath;
    }

    /**
     * Cleans a file path string by removing common problematic invisible Unicode control characters
     * and trimming leading/trailing whitespace. This method does NOT normalize slashes.
     * Use this if you want to preserve original slash style or are on a non-Windows OS.
     *
     * @param rawFilePath The original file path string.
     * @return A cleaned file path string.
     */
    public static String cleanFilePath(String rawFilePath) {
        if (rawFilePath == null) {
            return null;
        }
        return rawFilePath
                .trim()
                .replace("\u202A", "")
                .replace("\u202B", "")
                .replace("\u200E", "")
                .replace("\u200F", "");
    }
}