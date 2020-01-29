package sample.java2d.game2.game2_sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class game2_Main {

    public static final int SIZE = 800;
    public static Canvas canvasChess;
    public static VBox menuBox;
    public static StackPane vBox;
    public static GraphicsContext gc;
    public static Chess chess;

    public StackPane loadChess() {

        try {
            canvasChess = FXMLLoader.load(getClass().getResource("game2_fxml/game.fxml"));
            menuBox = FXMLLoader.load(getClass().getResource("game2_fxml/menu.fxml"));
        } catch (Exception e) {
            System.err.println(e.toString());
            return new StackPane(new Text("probably fxml not found"));
        }

        vBox = new StackPane(menuBox);

        chess = new Chess(new Image(getClass().getResourceAsStream("/sample/java2d/game2/game2_res/Chess_Board_1.png")));
        chess.initialize(canvasChess.getGraphicsContext2D());

        canvasChess.setOnMouseClicked(event ->
                chess.movePiece(event.getX(), event.getY(), canvasChess.getGraphicsContext2D()));
        canvasChess.setOnMouseDragged(moveEvent ->
                chess.movePieceUpdate(moveEvent.getX(), moveEvent.getY(), canvasChess.getGraphicsContext2D()));

        return vBox;
    }

}






















