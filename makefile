# Makefile pour projet JavaFX avec structure modulaire

# Répertoire JavaFX
PATH_TO_FX=/opt/javafx/javafx-sdk-21.0.7/lib
JAVAFX_MODULES=javafx.controls,javafx.fxml

# Répertoires
SRC_DIR=.
BIN_DIR=out
VIEW_DIR=view

# Classe principale
MAIN_CLASS=App

# Cible par défaut
default: run

# Compilation de tous les .java récursivement
compile:
	@echo "Compilation des sources Java..."
	@mkdir -p $(BIN_DIR)
	@javac --module-path $(PATH_TO_FX) --add-modules $(JAVAFX_MODULES) -d $(BIN_DIR) $(shell find $(SRC_DIR) -name "*.java")

# Copie des fichiers FXML dans le dossier de sortie
resources:
	@echo "Copie des fichiers FXML..."
	@mkdir -p $(BIN_DIR)/view
	@cp -R $(VIEW_DIR)/*.fxml $(BIN_DIR)/view/

# Exécution
run: compile resources
	@echo "Lancement de l'application..."
	@java --module-path $(PATH_TO_FX) --add-modules $(JAVAFX_MODULES) -cp $(BIN_DIR) $(MAIN_CLASS)

# Nettoyage
clean:
	@echo "Nettoyage des fichiers compilés..."
	@rm -rf $(BIN_DIR)
