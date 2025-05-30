package controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

public class MenuBarController {

    @FXML
    private MenuBar menuBar;

    private MainController parent;

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    @FXML
    public void handleShowCommonWords() {
        System.out.println("Afficher les mots communs !");
        if (parent != null) {
            parent.compareTexts(); // ou une autre méthode si tu veux déclencher l'analyse
        }
    }
}
