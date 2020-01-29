package sample.java2d.game1;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.SubScene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AntCalculator {

	// Calculation relevant
	static final double CELLSIZE = 10;
	static final int WIDTH = 100;
	static final int HEIGHT = 100;
	static int[] gridLook = new int[WIDTH * HEIGHT];
	private boolean pause = false, slow = false;
	private int canvasNumber = 6;
	private int tribesNumber = 4;
	private Tribe[] tribes;
	private Tribe[] defaultTribes;
	// Screen
	private VBox screenBox;
	private Canvas canvasAnt, canvasLook;
	private Canvas[] canvasMove;
	private GraphicsContext gca, gcl;
	private GraphicsContext[] gcm;
	// Menu
	private VBox menuBox, vButtonBox;
	private HBox hAddDeleButtonBox, hPauseSlowButtonBox;
	private TableView<Tribe> tribeTable;
	private TableColumn<Tribe, String> nameColumn;
	private ObservableList<Tribe> tableContent;
	private ListView<String> tribeInformation, tribeSize;
	private Button addButton, deleteButton, pauseButton, switchScreenButton, restartButton, slowButton;

	void screenInitialize() {

		canvasNumber = tribesNumber+2;
		canvasMove = new Canvas[tribesNumber];
		gcm = new GraphicsContext[tribesNumber];

		canvasAnt = new Canvas(WIDTH * CELLSIZE, HEIGHT * CELLSIZE);
		canvasLook = new Canvas(WIDTH * CELLSIZE, HEIGHT * CELLSIZE);
		gca = canvasAnt.getGraphicsContext2D();
		gcl = canvasLook.getGraphicsContext2D();

		getScreenBox().getChildren().removeAll(getScreenBox().getChildren());
		getScreenBox().getChildren().add(canvasLook);

		// Ameisenst�mme
		if (defaultTribes == null) {
			setTribes(new Tribe[tribesNumber]);
			getTribes()[0] = new Tribe("Tribe 1", 25, 25, 1, 1, 1, 1);
			if (tribesNumber > 1)
				getTribes()[1] = new Tribe("Tribe 2", 75, 25, 1, 1, 1, 1);
			if (tribesNumber > 2)
				getTribes()[2] = new Tribe("Tribe 3", 25, 75, 1, 1, 1, 1);
			if (tribesNumber > 3)
				getTribes()[3] = new Tribe("Tribe 4", 75, 75, 1, 1, 1, 1);
		}
		else{
			for(int i = 0; i<defaultTribes.length;i++)
				getTribes()[i]=defaultTribes[i];
		}


		// Move Canvas f�r die Ameisenst�mme
		for (int i = 0; i < canvasMove.length; i++) {
			canvasMove[i] = new Canvas(WIDTH * CELLSIZE, HEIGHT * CELLSIZE);
			gcm[i] = canvasMove[i].getGraphicsContext2D();
		}

		// Canvas Grids
		gcl.setStroke(Color.GRAY);
		for (int l = 0; l < gcm.length; l++) {
			gcm[l].setStroke(Color.GRAY);
		}
		for (int i = 0; i < WIDTH; i++) {
			for (int k = 0; k < HEIGHT; k++) {
				gcl.strokeRect(i * CELLSIZE, k * CELLSIZE, CELLSIZE, CELLSIZE);
				for (int l = 0; l < gcm.length; l++) {
					gcm[l].strokeRect(i * CELLSIZE, k * CELLSIZE, CELLSIZE, CELLSIZE);
				}
			}
		}

		// Default Tribes
		defaultTribes = new Tribe[getTribes().length];
		for (int i = 0; i < defaultTribes.length; i++)
			defaultTribes[i] = new Tribe(tribes[i].getName(), tribes[i].getRULESET(), tribes[i].getAnts().get(0).getX(),
					tribes[i].getAnts().get(0).getY(), tribes[i].getStrength(), tribes[i].getSpeed(),
					tribes[i].getBirthrate(), tribes[i].getSickness());

	}

	@SuppressWarnings("unchecked")
	private void menuInitialize() {

		setTableContent(FXCollections.observableArrayList());
		getTableContent().addAll(getTribes());

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		tribeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tribeTable.getColumns().addAll(nameColumn);
		tribeTable.setItems(getTableContent());
		tribeTable.setOnMouseClicked(e -> tribeInformationUpdate());
		tribeTable.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: black");
		tribeTable.autosize();

		tribeInformation.setFocusTraversable(false);
		tribeInformation.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: black");
		tribeInformation.autosize();

		tribeSize.setFocusTraversable(false);
		tribeSize.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: black");
		tribeSize.autosize();

		addButton.setPrefWidth(200);
		deleteButton.setPrefWidth(200);
		pauseButton.setPrefWidth(200);
		restartButton.setPrefWidth(400);
		switchScreenButton.setPrefWidth(400);
		slowButton.setPrefWidth(200);
		addButton.setOnAction(
				new ButtonEventHandler(addButton, deleteButton, pauseButton, restartButton, switchScreenButton, this));
		deleteButton.setOnAction(
				new ButtonEventHandler(addButton, deleteButton, pauseButton, restartButton, switchScreenButton, this));
		pauseButton.setOnAction(
				new ButtonEventHandler(addButton, deleteButton, pauseButton, restartButton, switchScreenButton, this));
		restartButton.setOnAction(
				new ButtonEventHandler(addButton, deleteButton, pauseButton, restartButton, switchScreenButton, this));
		switchScreenButton.setOnAction(
				new ButtonEventHandler(addButton, deleteButton, pauseButton, restartButton, switchScreenButton, this));
		slowButton.setOnMouseClicked(e -> slow = !slow);

		hAddDeleButtonBox.setSpacing(20);
		hAddDeleButtonBox.getChildren().addAll(addButton, deleteButton);
		hPauseSlowButtonBox.setSpacing(20);
		hPauseSlowButtonBox.getChildren().addAll(pauseButton, slowButton);
		vButtonBox.setSpacing(20);
		vButtonBox.setPadding(new Insets(20, 20, 20, 20));
		vButtonBox.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: black");
		vButtonBox.getChildren().addAll(hAddDeleButtonBox, hPauseSlowButtonBox, switchScreenButton, restartButton);

		menuBox.setStyle("-fx-border-style: solid;" + "-fx-border-width: 3;" + "-fx-border-color: black");
		menuBox.getChildren().addAll(tribeTable, tribeInformation, tribeSize, vButtonBox);

	}

    public class ReturnObject {
        private SubScene subScene;
        private AnimationTimer animationTimer;

        ReturnObject(SubScene s, AnimationTimer a){
            subScene = s;
            animationTimer = a;
        }
        public SubScene getSubScene() {
            return subScene;
        }

        public AnimationTimer getAnimationTimer() {
            return animationTimer;
        }
    }

	public ReturnObject loadAntCalculator() {

		// Screen Stuff
		setScreenBox(new VBox());
		screenInitialize();
		// Menu Stuff
		menuBox = new VBox();
		hAddDeleButtonBox = new HBox();
		hPauseSlowButtonBox = new HBox();
		vButtonBox = new VBox();
		tribeTable = new TableView<>();
		nameColumn = new TableColumn<>("tribe");
		tribeInformation = new ListView<>();
		tribeSize = new ListView<>();
		addButton = new Button("birth tribe");
		deleteButton = new Button("kill tribe");
		pauseButton = new Button("pause | continue");
		switchScreenButton = new Button("switch screen");
		restartButton = new Button("restart calculations");
		slowButton = new Button("slow | normal");
		menuInitialize();
		// Stage
		HBox box = new HBox();
		SubScene scene = new SubScene(box, WIDTH * CELLSIZE * 4 / 3, HEIGHT * CELLSIZE);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new Keyboard(this));
		box.getChildren().addAll(getScreenBox(), menuBox);

		AnimationTimer at = new AnimationTimer() {

			long slowTime = System.nanoTime();
			long updateTime = System.nanoTime();

			public void handle(long now) {

				if (slow)
					if (System.nanoTime() - slowTime < 1 * 1000 * 1000 * 1000 * 10)
						return;
					else
						slowTime = System.nanoTime();

				if (isPause())
					return;

				globalMovement(getTribes().length);
				globalTribes(gcm);
				globalWar();

				renderCanvasLook(gcl);
				renderCanvasAnt(gca);
				renderCanvasLookAntBorder(gcl, gca);

				if (System.nanoTime() - updateTime > 1000 * 1000 * 1000 * 1.4) {
					tribeSizeUpdate(tribeSize);
					updateTime = System.nanoTime();
				}

			}
		};
//		at.start();

        return new ReturnObject(scene, at);
//		return scene;
	}

	private void globalMovement(int times) {

		int maxTribeSize = getTribes()[0].getAnts().size();
		for (int i = 0; i < getTribes().length; i++) {
			if (getTribes()[i].getAnts().size() > maxTribeSize) {
				maxTribeSize = getTribes()[i].getAnts().size();
			}
		}
		int maxTribeSpeed = getTribes()[0].getSpeed();
		for (int i = 0; i < getTribes().length; i++) {
			if (getTribes()[i].getSpeed() > maxTribeSpeed) {
				maxTribeSpeed = getTribes()[i].getSpeed();
			}
		}

		for (int l = 0; l < maxTribeSpeed; l++) {

			for (int i = 0; i < maxTribeSize; i++) {

				for (int k = 0; k < times; k++) {

					if (i < getTribes()[k].getAnts().size() && l < getTribes()[k].getSpeed()) {

						getTribes()[k].getAnts().get(i).update(k);

					}
				}
			}
		}
	}

	private void globalTribes(GraphicsContext[] gcm) {
		for (int i = 0; i < getTribes().length; i++) {
			getTribes()[i].update(gcm[i]);
		}
	}

	private void globalWar() {

		for (int l = 0; l < getTribes().length; l++) {

			for (int m = l + 1; m < getTribes().length; m++) {

				int lTribeSize = getTribes()[l].getAnts().size();
				int mTribeSize = getTribes()[m].getAnts().size();

				for (int i = 0; i < lTribeSize; i++) {

					for (int k = 0; k < mTribeSize; k++) {

						if (i == lTribeSize)
							break;

						if (getTribes()[l].getAnts().get(i).getX() == getTribes()[m].getAnts().get(k).getX()
								&& getTribes()[l].getAnts().get(i).getY() == getTribes()[m].getAnts().get(k).getY()) {

							if (getTribes()[l].getStrength() > getTribes()[m].getStrength()) {
								getTribes()[m].getAnts().remove(getTribes()[m].getAnts().get(k));
								mTribeSize--;
							} else if (getTribes()[l].getStrength() < getTribes()[m].getStrength()) {
								getTribes()[l].getAnts().remove(getTribes()[l].getAnts().get(i));
								lTribeSize--;
								break;
							} else {
								getTribes()[l].getAnts().remove(getTribes()[l].getAnts().get(i));
								getTribes()[m].getAnts().remove(getTribes()[m].getAnts().get(k));
								lTribeSize--;
								mTribeSize--;
								break;
							}
						}
					}
				}
			}
		}
	}

	private void renderCanvasLook(GraphicsContext gcl) {
		for (int i = 0; i < AntCalculator.WIDTH; i++) {
			for (int k = 0; k < AntCalculator.HEIGHT; k++) {

				if (AntCalculator.gridLook[i * WIDTH + k] != 0) {

					float colorValue;

					int var = (AntCalculator.gridLook[i * WIDTH + k] - 1) / 10;

					if (AntCalculator.gridLook[i * WIDTH + k] > var * 10 && AntCalculator.gridLook[i * WIDTH + k] <= 10 * (var + 1)) {
						colorValue = 1 - ((float) AntCalculator.gridLook[i * WIDTH + k] / ((var + 1) * 10));
					} else
						continue;

					setGraphicsContextFill(gcl, var, colorValue);

					gcl.fillRect(i * AntCalculator.CELLSIZE, k * AntCalculator.CELLSIZE, AntCalculator.CELLSIZE, AntCalculator.CELLSIZE);
				}
			}
		}
	}

	private void renderCanvasAnt(GraphicsContext gca) {
		gca.setFill(Color.WHITE);
		gca.fillRect(0, 0, WIDTH * CELLSIZE, HEIGHT * CELLSIZE);
		gca.setStroke(Color.GRAY);
		gca.setLineWidth(1);
		for (int i = 0; i < WIDTH; i++) {
			for (int k = 0; k < HEIGHT; k++) {
				gca.strokeRect(i * CELLSIZE, k * CELLSIZE, CELLSIZE, CELLSIZE);
			}
		}

		for (int i = 0; i < getTribes().length; i++) {
			setGraphicsContextFill(gca, i, 0);
			for (int k = 0; k < getTribes()[i].getAnts().size(); k++) {
				gca.fillRect(getTribes()[i].getAnts().get(k).getX() * CELLSIZE, getTribes()[i].getAnts().get(k).getY() * CELLSIZE, AntCalculator.CELLSIZE,
						AntCalculator.CELLSIZE);
			}
		}
	}

	private void renderCanvasLookAntBorder(GraphicsContext gcl, GraphicsContext gca) {
		gcl.setStroke(Color.DARKCYAN);
		gca.setStroke(Color.DARKGREEN);
		gcl.setLineWidth(5);
		gca.setLineWidth(6);
		gcl.strokeRect(0, 0, WIDTH * CELLSIZE, HEIGHT * CELLSIZE);
		gca.strokeRect(0, 0, WIDTH * CELLSIZE, HEIGHT * CELLSIZE);
	}

	private void setGraphicsContextFill(GraphicsContext gcx, int var, float colorValue) {
		if (var == 1)
			gcx.setFill(new Color(1, colorValue, colorValue, 1));
		else if (var == 0)
			gcx.setFill(new Color(colorValue, colorValue, 1, 1));
		else if (var == 2)
			gcx.setFill(new Color(0.5, colorValue , colorValue / 2, 1));
		else if (var == 3)
			gcx.setFill(new Color(colorValue, colorValue, colorValue, 1));
		else if (var == 4)
			gcx.setFill(new Color(colorValue / 2, colorValue, colorValue, 1));
		else
			gcx.setFill(new Color(colorValue, colorValue, colorValue, 1));
	}

	private void tribeInformationUpdate() {
		Tribe tribe = tribeTable.getSelectionModel().getSelectedItem();
		String color;
		if(tribeTable.getSelectionModel().getSelectedIndex()==0)
			color="blue";
		else if(tribeTable.getSelectionModel().getSelectedIndex()==1)
			color="red";
		else if(tribeTable.getSelectionModel().getSelectedIndex()==2)
			color="brown";
		else
			color="black";
		tribeInformation.getItems().removeAll(tribeInformation.getItems());
		tribeInformation.getItems().addAll(tribe.getName(), "alive:   " + tribe.isAlive(), "ruleset:   " + tribe.getRULESET(),
				"strength:   " + tribe.getStrength(), "speed:   " + tribe.getSpeed(), "birthrate:   " + tribe.getBirthrate(),
				"sickness immunity:   " + tribe.getSickness(), "color: "+color);
	}

	private void tribeSizeUpdate(ListView<String> overallInformationList) {
		overallInformationList.getItems().removeAll(overallInformationList.getItems());
		for (int i = 0; i < getTribes().length; i++) {
			overallInformationList.getItems().add(getTribes()[i].getName() + " ants: " + String.valueOf(getTribes()[i].getAnts().size()));
		}
	}

	void switchScreen() {
		getScreenBox().getChildren().removeAll(getScreenBox().getChildren());
		if (canvasNumber == 1) {
			tribeTable.getSelectionModel().select(0);
			getScreenBox().getChildren().add(canvasLook);
			canvasNumber = canvasMove.length + 2;
		} else if (canvasNumber == 2) {
			tribeTable.getSelectionModel().select(0);
			getScreenBox().getChildren().add(canvasAnt);
			canvasNumber--;
		} else {
			getScreenBox().getChildren().add(canvasMove[-canvasNumber + canvasMove.length + 1 + 1]);
			tribeTable.getSelectionModel().select(-canvasNumber + canvasMove.length + 1 + 1);
			canvasNumber--;
		}
	}

	void restart() {
		setPause(true);
		gridLook = new int[WIDTH * HEIGHT];
		screenInitialize();
		setPause(false);
	}

	void delete() {
		setPause(true);
		Tribe tribe = tribeTable.getSelectionModel().getSelectedItem();
		try{
			tribe.suicide();
			tribe.setAlive(false);
		}
		catch (Exception e){
			setPause(false);
			return;
		}
		tribeInformationUpdate();
		setPause(false);
	}

	void add() {
		setPause(true);
		NewTribeBox box = new NewTribeBox();
		box.display(this);
		setPause(false);
	}

	//Setter and Getter
	public ObservableList<Tribe> getTableContent() {
		return tableContent;
	}
	public void setTableContent(ObservableList<Tribe> tableContent) {
		this.tableContent = tableContent;
	}
	public boolean isPause() {
		return pause;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	public VBox getScreenBox() {
		return screenBox;
	}
	public void setScreenBox(VBox screenBox) {
		this.screenBox = screenBox;
	}
	public Tribe[] getTribes() {
		return tribes;
	}
	public void setTribes(Tribe[] tribes) {
		this.tribes = tribes;
	}
	public void setTribesNumber(int tribesNumber){
		this.tribesNumber=tribesNumber;
	}
	public int getTribesNumber() {
		return tribesNumber;
	}
	public Tribe[] getDefaultTribes(){
		return defaultTribes;
	}

	public Canvas[] getCanvasMove() {
		return canvasMove;
	}

	public GraphicsContext[] getGCM() {
		return gcm;
	}

	public void setCanvasMove(Canvas[] canvas) {
		canvasMove = canvas;
	}

	public void setGCM(GraphicsContext[] graphicsContexts) {
		gcm = graphicsContexts;
	}
}