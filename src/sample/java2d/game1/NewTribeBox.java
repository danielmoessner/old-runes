package sample.java2d.game1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewTribeBox {

	private Stage window;
	private TableView<Tribe> tribes;
	private TextField xInput, yInput, nameInput, rulesetInput, strengthInput, speedInput, birthrateInput, sicknessInput;
	private Label errors;
	private AntCalculator antCalculator;

	@SuppressWarnings("unchecked")
	public void display(AntCalculator antCalculator) {
		this.antCalculator = antCalculator;

		Label capital = new Label("tribe properties");
		capital.setPrefWidth(350);
		capital.setFont(new Font(20));
		capital.setAlignment(Pos.CENTER);

		Label name = new Label("name");
		name.setPrefWidth(175);
		name.setFont(new Font(18));

		Label ruleset = new Label("ruleset");
		ruleset.setPrefWidth(175);
		ruleset.setFont(new Font(18));

		Label strength = new Label("strength");
		strength.setPrefWidth(175);
		strength.setFont(new Font(18));

		Label speed = new Label("speed");
		speed.setPrefWidth(175);
		speed.setFont(new Font(18));

		Label birthrate = new Label("birthrate");
		birthrate.setPrefWidth(175);
		birthrate.setFont(new Font(18));

		Label sickness = new Label("sickness immunity");
		sickness.setPrefWidth(175);
		sickness.setFont(new Font(18));

		Label x = new Label("xStart");
		x.setPrefWidth(175);
		x.setFont(new Font(18));

		Label y = new Label("yStart");
		y.setPrefWidth(175);
		y.setFont(new Font(18));

		nameInput = new TextField();
		nameInput.setPrefWidth(175);
		nameInput.setFont(new Font(18));

		rulesetInput = new TextField();
		rulesetInput.setPrefWidth(175);
		rulesetInput.setFont(new Font(18));

		strengthInput = new TextField();
		strengthInput.setPrefWidth(175);
		strengthInput.setFont(new Font(18));

		speedInput = new TextField();
		speedInput.setPrefWidth(175);
		speedInput.setFont(new Font(18));

		birthrateInput = new TextField();
		birthrateInput.setPrefWidth(175);
		birthrateInput.setFont(new Font(18));

		sicknessInput = new TextField();
		sicknessInput.setPrefWidth(175);
		sicknessInput.setFont(new Font(18));

		xInput = new TextField();
		xInput.setPrefWidth(175);
		xInput.setFont(new Font(18));

		yInput = new TextField();
		yInput.setPrefWidth(175);
		yInput.setFont(new Font(18));

		errors = new Label("no errors");
		errors.setPrefWidth(360);

		Button birth = new Button("birth tribe");
		birth.setPrefWidth(175);
		birth.setFont(new Font(14));
		birth.setOnMouseClicked(e -> birth());

		Button replace = new Button("replace tribe");
		replace.setPrefWidth(175);
		replace.setFont(new Font(14));
		replace.setOnMouseClicked(e -> replace());

		Button close = new Button("close and continue");
		close.setPrefWidth(360);
		close.setFont(new Font(14));
		close.setOnMouseClicked(e -> window.close());

		Button restart = new Button("close and restart");
		restart.setPrefWidth(360);
		restart.setFont(new Font(14));
		restart.setOnMouseClicked(e -> close());

		TableColumn<Tribe, String> nameColumn = new TableColumn<>("tribe");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		tribes = new TableView<>();
		tribes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tribes.getColumns().addAll(nameColumn);
		tribes.setItems(antCalculator.getTableContent());
		tribes.setPrefWidth(360);
		tribes.setPrefHeight(200);

		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(400, 560);
		flowPane.setPadding(new Insets(20, 20, 20, 20));
		flowPane.setHgap(10);
		flowPane.setVgap(10);

		flowPane.getChildren().addAll(capital, name, nameInput, ruleset, rulesetInput, x, xInput, y, yInput, strength,
				strengthInput, speed, speedInput, birthrate, birthrateInput, sickness, sicknessInput, errors, tribes,
				birth, replace, close, restart);

		Scene scene = new Scene(flowPane);
		window = new Stage();
		window.setScene(scene);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("New Tribe");
		window.showAndWait();

	}

	private void close(){
		antCalculator.restart();
		window.close();
	}

	private void birth() {
		antCalculator.setTribesNumber(antCalculator.getTribesNumber()+1);

		Tribe[] tribes = antCalculator.getTribes();
		Canvas[] canvasMove = antCalculator.getCanvasMove();
		GraphicsContext[] gcm = antCalculator.getGCM();

		antCalculator.setTribes(new Tribe[antCalculator.getTribesNumber()]);
		antCalculator.setCanvasMove(new Canvas[antCalculator.getTribesNumber()]);
		antCalculator.setGCM(new GraphicsContext[antCalculator.getTribesNumber()]);


		if(!birthTribe(antCalculator.getTribesNumber()-1)){
			antCalculator.setTribesNumber(antCalculator.getTribesNumber()-1);

			antCalculator.setTribes(new Tribe[antCalculator.getTribesNumber()]);
			antCalculator.setCanvasMove(new Canvas[antCalculator.getTribesNumber()]);
			antCalculator.setGCM(new GraphicsContext[antCalculator.getTribesNumber()]);
			for(int i = 0; i < antCalculator.getTribes().length; i++){
				antCalculator.getTribes()[i] = tribes[i];
				antCalculator.getCanvasMove()[i] = canvasMove[i];
				antCalculator.getGCM()[i] = gcm[i];
			}
			return;
		}

		antCalculator.getCanvasMove()[antCalculator.getTribesNumber()-1] = new Canvas(AntCalculator.WIDTH * AntCalculator.CELLSIZE, AntCalculator.HEIGHT * AntCalculator.CELLSIZE);
		antCalculator.getGCM()[antCalculator.getTribesNumber()-1] = antCalculator.getCanvasMove()[antCalculator.getTribesNumber()-1].getGraphicsContext2D();
		for(int i = 0; i < antCalculator.getTribes().length-1; i++){
			antCalculator.getTribes()[i] = tribes[i];
			antCalculator.getCanvasMove()[i] = canvasMove[i];
			antCalculator.getGCM()[i] = gcm[i];
		}

		antCalculator.getTableContent().removeAll(antCalculator.getTableContent());
		antCalculator.getTableContent().addAll(antCalculator.getTribes());
	}

	private void replace() {
		int which;
		which = tribes.getSelectionModel().getSelectedIndex();
		if (which < 0) {
			errors.setText("choose a tribe");
			return;
		}
		birthTribe(which);

		antCalculator.getTableContent().removeAll(antCalculator.getTableContent());
		antCalculator.getTableContent().addAll(antCalculator.getTribes());
	}

	private boolean birthTribe(int which) {

		int strength=-1, speed=-1, birthrate=-1, sickness=-1, x=-1, y=-1;
		String name = nameInput.getText();
		String ruleset = rulesetInput.getText();

		for (int i = 0; i < ruleset.length(); i++) {
			if (ruleset.charAt(i) != 'R' && ruleset.charAt(i) != 'L' && ruleset.charAt(i) != 'B'
					&& ruleset.charAt(i) != 'F') {
				errors.setText("the ruleset must only contain 'R', 'L', 'F' or 'B'");
				return false;
			}
		}
		if(ruleset.length()==0){
			errors.setText("the ruleset must contain 'R', 'L', 'F' or 'B'");
			return false;
		}
		try {
			strength = Integer.parseInt(strengthInput.getText());
			if(strength < 0)
				throw(new Exception());

		} catch (Exception e) {
			errors.setText("the strength has to be a positive number");
			return false;
		}
		try {
			speed = Integer.parseInt(speedInput.getText());
			if(speed < 0)throw(new Exception());
		} catch (Exception e) {
			errors.setText("the speed has to be a positive number");
			return false;
		}
		try {
			birthrate = Integer.parseInt(birthrateInput.getText());
			if(birthrate < 0)throw(new Exception());
		} catch (Exception e) {
			errors.setText("the birthrate has to be a positive number");
			return false;
		}
		try {
			sickness = Integer.parseInt(sicknessInput.getText());
			if(sickness < 0)throw(new Exception());
		} catch (Exception e) {
			errors.setText("the sickness has to be a positive number");
			return false;
		}
		try {
			x = Integer.parseInt(xInput.getText());
			if (x < 0 || x > AntCalculator.WIDTH)throw(new Exception());
		} catch (Exception e) {
			errors.setText("the xStart has to be a number between 0 and " + AntCalculator.WIDTH);
			return false;
		}
		try {
			y = Integer.parseInt(yInput.getText());
			if (y < 0 || y > AntCalculator.HEIGHT)throw(new Exception());
		} catch (Exception e) {
			errors.setText("the yStart has to be a number between 0 and " + AntCalculator.HEIGHT);
			return false;
		}
		antCalculator.getTribes()[which] = new Tribe(name, ruleset, x, y, strength, speed, birthrate, sickness);
		errors.setText("succeed");
		return true;
	}
}





















