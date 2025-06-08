package src.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainModel {
    private int id_chapter;
    private LinkedHashMap<Integer, String> allContent;
    public MainModel(int id_chapter, String content) {
        this.id_chapter = id_chapter;
        this.allContent = new LinkedHashMap<>();

    }

    public void updateChapter(int id_chapter, String content) {
        this.getAllContent().put(id_chapter, content);
    }


    public LinkedHashMap<Integer, String> getAllContent() {
        return allContent;
    }

    public MainModel() {
        this.id_chapter = 0;
        this.allContent = new LinkedHashMap<>();
    }

    public void addChapter(String content) {
        this.allContent.put(this.id_chapter, content);
        this.id_chapter++;
    }

    public LinkedHashMap<Integer, String> getAllChapter() {
        return allContent;
    }
    public int getIdChapter() {
        return id_chapter;
    }
}