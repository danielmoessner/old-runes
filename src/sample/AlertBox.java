package sample;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("unused")
public class AlertBox {

    private final Stage STAGE = new Stage();
    private final Scene SCENE = new Scene(new VBox(), 0,0);

    private AlertBox(){
        STAGE.setScene(SCENE);
        STAGE.setTitle("Atlantica");
        STAGE.initStyle(StageStyle.UTILITY);
        STAGE.show();
    }

    public AlertBox(TextArea textArea){
        this();
        TextArea ta = new TextArea();
        ta.setText(textArea.getText());
        SCENE.widthProperty().addListener(change -> ta.setPrefWidth(SCENE.getWidth()));
        SCENE.heightProperty().addListener(change -> ta.setPrefHeight(SCENE.getHeight()));
        StackPane sp = new StackPane(ta);

        SCENE.setRoot(sp);
    }

    public AlertBox(ImageView imageView) {
        this();
        ImageView iv = new ImageView(imageView.getImage());
        iv.fitWidthProperty().bind(SCENE.widthProperty());
        iv.fitHeightProperty().bind(SCENE.heightProperty());
        StackPane sp = new StackPane(iv);

        SCENE.setRoot(sp);
    }

    public AlertBox(Node n) {
        this();
        Text t = new Text("The input node is not supported!");
        StackPane sp = new StackPane(t);

        SCENE.setRoot(sp);
        STAGE.setWidth(400);
        STAGE.setHeight(200);
    }

    public AlertBox(String message) {
        this();
        int count = message.length() / 50;
        for (int i = 1; i < count; i++) {
            message = message.substring(0, 50 * i) + "\n" + message.substring(50 * i, message.length());
        }
        Text t = new Text(message);
        t.setTextAlignment(TextAlignment.CENTER);
        StackPane sp = new StackPane(t);

        SCENE.setRoot(sp);
        STAGE.setWidth(500);
        STAGE.setHeight(300);
    }

    public AlertBox(Parent root) {
        this();
        SCENE.setRoot(root);
    }

}
