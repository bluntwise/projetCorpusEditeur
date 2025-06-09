package src.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Extract and return raw Content of a File
 */
public class FileContentRaw {
    private String filename;
    public FileContentRaw(String filename) {
        this.filename = filename;


    }

    public String getContent() {
        BufferedReader reader;
        StringBuilder result = new StringBuilder();
        try {

            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();

            while (line != null) {
                result.append(line).append("\n");
                line = reader.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
