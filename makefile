# Configuration
PATH_TO_FX=lib/
JAVAFX_MODULES=javafx.controls,javafx.fxml

SRC_DIR=src
BIN_DIR=out
VIEW_SRC=$(SRC_DIR)/view
VIEW_OUT=$(BIN_DIR)/view
MAIN_CLASS=src.App

default: run

compile:
	@echo "🔧 Compilation Java..."
	@mkdir -p $(BIN_DIR)
	@find $(SRC_DIR) -name "*.java" > sources.txt
	@javac --module-path $(PATH_TO_FX) --add-modules $(JAVAFX_MODULES) -d $(BIN_DIR) @sources.txt
	@rm sources.txt

resources:
	@echo "📁 Copie des fichiers FXML..."
	@mkdir -p $(VIEW_OUT)
	@cp $(VIEW_SRC)/*.fxml $(VIEW_OUT)/

run: compile resources
	@echo "🚀 Lancement..."
	@java --module-path $(PATH_TO_FX) --add-modules $(JAVAFX_MODULES) -cp $(BIN_DIR) $(MAIN_CLASS)

clean:
	@echo "🧹 Nettoyage..."
	@rm -rf $(BIN_DIR)
