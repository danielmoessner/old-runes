package sample.java2d.game2.game2_sample.game2_controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sample.java2d.game2.game2_sample.game2_Main;


public class Keyboard implements EventHandler<KeyEvent>{

    public void handle(KeyEvent key) {

        if(key.getCode() == KeyCode.M){
            game2_Main.vBox.getChildren().removeAll(game2_Main.vBox.getChildren());
            game2_Main.vBox.getChildren().add(game2_Main.menuBox);
        }
    }
}
