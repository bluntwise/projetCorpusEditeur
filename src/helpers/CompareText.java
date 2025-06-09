package src.helpers;

import javafx.scene.Parent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Method that compare two texts and return common words
 */
public class CompareText {

    public static Set<String> compareTextsUtils(String t1, String t2) {
        String[] words1 = t1.split("\\s+");
        String[] words2 = t2.split("\\s+");

        Set<String> set1 = new HashSet<>(List.of(words1));
        Set<String> set2 = new HashSet<>(List.of(words2));


        // Common Words
        Set<String> commonWords = new HashSet<>(set1);
        commonWords.retainAll(set2);


        return commonWords;
    }

}
