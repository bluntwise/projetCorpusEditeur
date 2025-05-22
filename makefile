JFX_LIB=/home/alandely/opt/openjfx-21.0.7_linux-x64_bin-sdk/javafx-sdk-21.0.7/lib
JFX_MODS=javafx.controls,javafx.fxml

default: run

run:
	@mkdir -p out/view
	@javac --module-path $(JFX_LIB) --add-modules $(JFX_MODS) -d out $(shell find . -name "*.java")
	@cp view/*.fxml out/view/
	@java --module-path $(JFX_LIB) --add-modules $(JFX_MODS) -cp out App

clean:
	rm -rf out
