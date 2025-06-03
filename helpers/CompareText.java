package helpers;

import javafx.scene.Parent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompareText {

    public static Set<String> compareTextsUtils(String t1, String t2) {
        String[] words1 = t1.split("\\s+");
        String[] words2 = t2.split("\\s+");

        Set<String> set1 = new HashSet<>(List.of(words1));
        Set<String> set2 = new HashSet<>(List.of(words2));

        // Intersection : mots communs
        Set<String> commonWords = new HashSet<>(set1);
        commonWords.retainAll(set2);

        System.out.println("Mots en commun (" + commonWords.size() + ") : " + commonWords);

        return commonWords;
    }

}
