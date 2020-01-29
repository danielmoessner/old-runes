package sample.java2d.game1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonEventHandler implements EventHandler<ActionEvent> {

	private Button addButton;
	private Button deleteButton;
	private Button pauseButton;
	private Button restartButton;
	private Button switchScreenButton;
	private AntCalculator antCalculator;

	public ButtonEventHandler(Button addButton, Button deleteButton, Button pauseButton, Button restartButton,
			Button switchScreenButton, AntCalculator antCalculator) {
		this.addButton = addButton;
		this.deleteButton = deleteButton;
		this.pauseButton = pauseButton;
		this.restartButton = restartButton;
		this.switchScreenButton = switchScreenButton;
		this.antCalculator = antCalculator;
	}

	public void handle(ActionEvent event) {

		if(event.getSource()==addButton){
			antCalculator.add();
		}
		if(event.getSource()==deleteButton){
			antCalculator.delete();
		}
		if(event.getSource()==pauseButton){
			antCalculator.setPause(!antCalculator.isPause());
		}
		if(event.getSource()==restartButton){
			antCalculator.restart();
		}
		if(event.getSource()==switchScreenButton){
			antCalculator.switchScreen();
		}
	}
}
