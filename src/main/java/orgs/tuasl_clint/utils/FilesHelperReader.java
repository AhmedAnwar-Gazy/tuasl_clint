package orgs.tuasl_clint.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FilesHelperReader {
    /*
     * this method reads the file and returns list of strings from the file -(@param filePath String)- that is end with character -(@param delimiter char)-
     * @param filePath refers the file path, and it's name
     * @param delimiter refers for the char of end all strings
     */
    public static List<String> readUntilChar(String filePath, char delimiter) throws IOException {
        List<String> result = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {
            int currentChar;
            while ((currentChar = reader.read()) != -1) {
                if (currentChar == delimiter) {

                    if (currentString.length() > 0) {
                        result.add(currentString.toString());
                        currentString = new StringBuilder();
                    }

                } else {
                    currentString.append((char) currentChar);
                }
            }
            // Add the last string if it exists (file doesn't end with delimiter)
            if (currentString.length() > 0) {
                result.add(currentString.toString());
            }
        }
        return result;
    }
}
