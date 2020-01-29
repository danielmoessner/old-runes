package sample.java2d.game2.game2_sample.game2_controller;

import javafx.scene.control.Button;
import sample.java2d.game2.game2_sample.game2_Main;

public class menuController {

    public Button playButton = new Button();
    public Button newGameButton = new Button();

    public void play() {
        game2_Main.vBox.getChildren().removeAll(game2_Main.vBox.getChildren());
        game2_Main.vBox.getChildren().add(game2_Main.canvasChess);
    }

    public void newGame(){
        game2_Main.chess.initialize(game2_Main.gc);
    }

}
