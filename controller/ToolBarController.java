package controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import helpers.FileContentRaw;
import helpers.TextParserCorpus;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MainModel;
import javafx.fxml.FXML;
import helpers.RomanConverter;

import static helpers.RomanConverter.toRomanWithDot;

public class ToolBarController {
    private editeurController parent;
    private String zone;
    private int chapter;
    private File FileSource;
    @FXML
    private ComboBox<String> comboBoxChapters;


    public void setParent(editeurController parent) {
        this.parent = parent;
        this.chapter = 0;
        this.FileSource = null;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public ComboBox<String> getComboBoxChapters() {
        return comboBoxChapters;
    }

    private MainModel model;
    private Stage stage;
    @FXML
    public void initialize() {
        model = new MainModel();
        setupComboBoxChapters();

    }


    public void setupComboBoxChapters() {
        comboBoxChapters.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadChapter(comboBoxChapters.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void loadChapter(int chapterIndex) {
        String content = model.getAllContent().get(chapterIndex);
        String text = String.join("\n", content); // concatène les lignes avec des sauts de ligne

        if ("left".equals(zone)) {
            parent.getLeftTextArea().setText(text);
        } else if ("right".equals(zone)) {
            parent.getRightTextArea().setText(text);
        }

        this.chapter = chapterIndex;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void saveFile(){
        if (this.FileSource == null) {
            System.out.println("Aucun fichier téléchargé");
            return;
        }
        LinkedHashMap<Integer, String> allContent = model.getAllContent();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FileSource))) {
            for(Map.Entry<Integer, String> entry : allContent.entrySet()) {
                Integer id_chapter = entry.getKey();
                String content_chapter = entry.getValue();


                writer.write("\n" + toRomanWithDot(id_chapter + 1) + ".\n"); // chapitre 1 = I.
                writer.newLine();

                // Puis écrire toutes les lignes
                writer.write(content_chapter);
                writer.newLine();


            }
            System.out.println("Modifications enregistrées dans : " + FileSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void editFile() {
        TextArea targetTextArea = "left".equals(zone)
                ? parent.getLeftTextArea()
                : parent.getRightTextArea();

        boolean isNowEditable = !targetTextArea.isEditable();
        targetTextArea.setEditable(isNowEditable);

        if (isNowEditable) {
            targetTextArea.setStyle("-fx-control-inner-background: #ffffe0;");
            System.out.println("Édition activée");
        } else {
            targetTextArea.setStyle("");
            System.out.println("Édition désactivée. Mise à jour du modèle.");

            int selectedIndex = comboBoxChapters.getSelectionModel().getSelectedIndex();
            model.updateChapter(selectedIndex, targetTextArea.getText());
        }
    }




    @FXML
    private void chargeFile(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger un fichier texte");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        this.FileSource = selectedFile;
        if (selectedFile != null) {
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
            // Tu peux maintenant le lire et l'afficher dans un TextArea
        }

        assert selectedFile != null;
        FileContentRaw extractContent = new FileContentRaw(selectedFile.getAbsolutePath());

        TextParserCorpus parser = new TextParserCorpus(extractContent.getContent());
        model = parser.parse();
        int nbChapters = model.getIdChapter();

        List<String> chapterLabels = new ArrayList<>();
        for (int i = 0; i < nbChapters; i++) {
            chapterLabels.add("Chapitre " + (i + 1));
        }

        comboBoxChapters.getItems().setAll(chapterLabels);
        comboBoxChapters.getSelectionModel().selectFirst();
//        List<String> chapters = model.getAllContent().get(this.chapter);
//        if ("left".equals(zone)) {
//            parent.getLeftListView().getItems().setAll(chapters);
//        } else if ("right".equals(zone)) {
//            parent.getRightListView().getItems().setAll(chapters);
//        }
    }
}