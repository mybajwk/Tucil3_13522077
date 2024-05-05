package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileReader {
    public static Set<String> readStringsFromFile(String filePath) {
        Set<String> stringSet = new HashSet<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return stringSet;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                Arrays.stream(words)
                        .map(String::toLowerCase)
                        .forEach(stringSet::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringSet;
    }
}
