package sample.java;

import javafx.fxml.FXMLLoader;
import javafx.scene.SubScene;
import javafx.scene.layout.VBox;

public class Example {

    public SubScene loadExample1() {
        try {
            return new SubScene(FXMLLoader.load(getClass().getResource("example#1.fxml")), 0, 0);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return new SubScene(new VBox(), 0,0);
        }
    }


}
