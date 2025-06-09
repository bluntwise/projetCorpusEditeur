package src.model;
import java.util.LinkedHashMap;


/**
 * Model representing a corpus composed of multiple chapters, each identified by an integer ID.
 * Provides methods to add, update, and retrieve chapters, while internally tracking the current chapter index.
 * Chapters are stored in insertion order using a LinkedHashMap.
 */

public class CorpusModel {
    private int id_chapter;
    private LinkedHashMap<Integer, String> allContent;


    public void updateChapter(int id_chapter, String content) {
        this.getAllContent().put(id_chapter, content);
    }


    public LinkedHashMap<Integer, String> getAllContent() {
        return allContent;
    }

    public CorpusModel() {
        this.id_chapter = 0;
        this.allContent = new LinkedHashMap<>();
    }

    public void addChapter(String content) {
        this.allContent.put(this.id_chapter, content);
        this.id_chapter++;
    }

    public int getIdChapter() {
        return id_chapter;
    }
}