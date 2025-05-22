import helpers.FileContentRaw;
import helpers.TextParserCorpus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.MainModel;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainView.fxml"));
//        Parent root = loader.load();
//
//        Scene scene = new Scene(root, 400, 200);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("EditeurCorpus");
//        primaryStage.show();

        FileContentRaw f_content = new FileContentRaw("./Data/De_Senectute_EN.txt");
        TextParserCorpus tf_corpus = new TextParserCorpus(f_content.getContent());
        System.out.println(tf_corpus.getContent());
        MainModel m = tf_corpus.parse();
//        System.out.println(m.getContent());
//        System.out.println(m.getId_chapter());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
