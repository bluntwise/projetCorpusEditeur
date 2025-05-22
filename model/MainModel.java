package model;
import java.util.ArrayList;
import java.util.List;

public class MainModel {
    private int id_chapter;
    private List<String> content;
    public MainModel(int id_chapter, List<String> content) {
        this.id_chapter = id_chapter;
        this.content = content;
    }

    public MainModel() {
        this.id_chapter = 0;
        this.content = new ArrayList<>();
    }

    public void addChapter(String content) {
        this.id_chapter++;
        this.content.add(content);
    }

    public List<String> getContent() {
        return content;
    }
    public int getId_chapter() {
        return id_chapter;
    }
}