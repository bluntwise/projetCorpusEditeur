package src.helpers;

import src.model.CorpusModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParserCorpus {
    private String content;
    public TextParserCorpus(String content) {
        this.content = content;
    }
    public String getContent() {
        return this.content;
    }

    public CorpusModel parse() {
        CorpusModel m = new CorpusModel();

        Pattern pattern = Pattern.compile("(^[IVXLCDM]+\\..*)", Pattern.MULTILINE);
        String[] parts = pattern.split(content);

        Matcher matcher = pattern.matcher(content);
        List<String> titles = new ArrayList<>();

        while (matcher.find()) {
            titles.add(matcher.group().trim());
        }

        for (int i = 0; i < titles.size(); i++) {
            String body = (i + 1 < parts.length) ? parts[i + 1].trim() : "";

           m.addChapter(body);
        }
        return m;
    }



}
