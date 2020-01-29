package sample.java;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    public Label bottomLabel;
    public TextField username;
    public TextArea textArea;
    public Button motivationButton;
    private boolean motivation = false;
    private static int helpProvided = 0;
    private static long time = System.nanoTime();


    public void loginButtonClicked(){
        bottomLabel.textProperty().bind(username.textProperty());
        motivationButton.setDisable(false);
    }

    public void activateHelp() {

        if(System.nanoTime()-time<1000*1000*1000*10 || helpProvided==0){
            time = System.nanoTime();
            helpProvided++;
            return;
        }

        textArea.setText(textArea.getText()+"hello");
        time = System.nanoTime();
    }

    public void setMotivation(){
        motivation = !motivation;
    }

    public void startMotivationProgram(){
        if(motivation!=true)return;
        textArea.setText("Hello unknown Person\n");
    }

}
