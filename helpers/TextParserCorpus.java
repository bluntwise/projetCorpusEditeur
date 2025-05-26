package helpers;

import model.MainModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
public class TextParserCorpus {
    private String content;
    public TextParserCorpus(String content) {
        this.content = content;
    }
    public String getContent() {
        return this.content;
    }

    public MainModel parse() {
        MainModel m = new MainModel();

        Pattern pattern = Pattern.compile("(^[IVXLCDM]+\\..*)", Pattern.MULTILINE);
        String[] parts = pattern.split(content);

        Matcher matcher = pattern.matcher(content);
        List<String> titles = new ArrayList<>();

        while (matcher.find()) {
            titles.add(matcher.group().trim());
        }

        for (int i = 0; i < titles.size(); i++) {
            String body = (i + 1 < parts.length) ? parts[i + 1].trim() : "";

//            List<String> body = Arrays.stream(bodyRaw.split("\n"))
//                    .map(String::trim)
//                    .filter(line -> !line.isEmpty())
//                    .toList();
            m.addChapter(body);
        }
        return m;
    }



}
