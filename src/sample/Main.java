package sample;

import javafx.application.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import sample.java.*;
import sample.java2d.examples.CanvasAPI;
import sample.java2d.examples.SliderExample;
import sample.java2d.game1.*;
import sample.java2d.game2.game2_sample.game2_Main;
import sample.java2d.other.ScreenMountingSimulation;
import sample.java3d.examples.*;
import sample.java3d.simple3d.*;
import sample.neuralnet.*;

import java.util.*;

@SuppressWarnings("unused")
public class Main extends Application {

    private SuperBar b;
    private SuperBar.SuperMenu m;
    private SuperBar.SuperMenu.SuperItem i1;
    private SuperBar.SuperMenu.SuperItem i2;
    private SuperBar.SuperMenu.SuperItem i3;
    private SuperBar.SuperMenu.SuperItem i4;
    private SuperBar.SuperMenu.SuperItem i5;

    private void initMainMenu(Stage primaryStage) {
        // Menus
        SuperBar.SuperMenu sm0 = b.new SuperMenu("sm0");
        sm0.getMENU().setDisable(true);
        sm0.getMENU().setVisible(false);
        // Items
        i1 = m.new SuperItem("Neural Network", new ImageView(new Image("/pictures/neurons.jpg")));
        i2 = m.new SuperItem("Work Bench", new ImageView(new Image("/pictures/concept.jpg")));
        i3 = m.new SuperItem("Java", new ImageView(new Image("/pictures/javalogo.jpg")));
        i4 = m.new SuperItem("Java 2D", new ImageView(new Image("/pictures/anime.jpeg")));
        i5 = m.new SuperItem("Java 3D", new ImageView(new Image("/pictures/supernova.jpg")));
        i1.getMENU_ITEM().addEventHandler(ActionEvent.ANY, e0 -> initNeuralNetMenu());
        i2.getMENU_ITEM().addEventHandler(ActionEvent.ANY, e0 -> initWorkBenchMenu());
        i3.getMENU_ITEM().addEventHandler(ActionEvent.ANY, e0 -> initJavaMenu());
        i4.getMENU_ITEM().addEventHandler(ActionEvent.ANY, e0 -> initJava2dMenu());
        i5.getMENU_ITEM().addEventHandler(ActionEvent.ANY, e0 -> initJava3dMenu());
        // Secret
        SuperBar.SuperMenu.SuperItem si0 = sm0.new SuperItem("si0", new ImageView(new Image("pictures/Kate.jpg")));
        ArrayList<SuperBar.SuperMenu.SuperItem> key = new ArrayList<>();
        key.add(i3);
        key.add(i2);
        key.add(i1);
        b.addKey(key, si0);
        // adjust size
        primaryStage.addEventHandler(KeyEvent.KEY_TYPED, keyEvent -> {
            if (keyEvent.getCharacter().equals(" ")) {
                Image image;
                try {
                    image = m.getSelectedSuperItem().getImageView().getImage();
                } catch (NoSuchElementException e) {
                    image = sm0.getSelectedSuperItem().getImageView().getImage();
                }
                primaryStage.setWidth(999);
                primaryStage.setHeight(image.getHeight() / image.getWidth() * 999 + b.getMENUBAR().getHeight());
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Essential
        b = new SuperBar();
        m = b.new SuperMenu("Main");
        SuperBar.SuperMenu.SuperItem i = m.new SuperItem("Inception", new ImageView("/pictures/ass.jpg"));
        i.getMENU_ITEM().fire();
        // Menu
        initMainMenu(primaryStage);
        // Stage
        primaryStage.setTitle("Runes");
        primaryStage.getIcons().add(new Image("/pictures/BEST-ICON.png"));
        primaryStage.setScene(new Scene(b.getROOT(), i.getImageView().getImage().getWidth() / 2, i.getImageView().getImage().getHeight() / 2, true));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    private void initNeuralNetMenu() {
        // Text Area
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Line Graph
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Pass");
        yAxis.setLabel("Error");
        final XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Learning Curve");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setMinSize(800, 400);
        lineChart.getData().add(series);

        // Menu
        SuperBar.SuperMenu m1 = b.new SuperMenu("Box", i1);
        SuperBar.SuperMenu m2 = b.new SuperMenu("Edit", i1);
        SuperBar.SuperMenu m3 = b.new SuperMenu("View", i1);
        SuperBar.SuperMenu.SuperItem m1i1 = m1.new SuperItem("Line Graph", lineChart);
        SuperBar.SuperMenu.SuperItem m1i2 = m1.new SuperItem("Text Area", textArea);
        SuperBar.SuperMenu.SuperItem m2i1 = m2.new SuperItem("Restart");
        SuperBar.SuperMenu.SuperItem m3i1 = m3.new SuperItem("Open Current Box Node On A New AlertBox...");

        // Action Listener
        m2i1.getMENU_ITEM().addEventHandler(ActionEvent.ANY, e0 -> {
            series.getData().clear();
            textArea.clear();
            neuralNet(textArea, series);
        });
        m3i1.getMENU_ITEM().addEventHandler(ActionEvent.ANY, e0 -> {
            AlertBox ab;
            if (m.getSelectedSuperItem().getImageView() == b.getSTACK_PANE().getChildren().get(0))
                ab = new AlertBox((ImageView) b.getSTACK_PANE().getChildren().get(0));
            else if (m1.getSelectedSuperItem().getRegion() == b.getSTACK_PANE().getChildren().get(0))
                try {
                    ab = new AlertBox((TextArea) b.getSTACK_PANE().getChildren().get(0));
                } catch (ClassCastException e) {
                    ab = new AlertBox(b.getSTACK_PANE().getChildren().get(0));
                }
        });

        // start
        neuralNet(textArea, series);
    }

    private void initWorkBenchMenu() {
        // #work1: Line Chart Example
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 114));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 124));
        series.setName("My Data");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        final LineChart<Number, Number> work1 = new LineChart<>(xAxis, yAxis);
        work1.setTitle("Line Chart");
        work1.getData().add(series);

        // #work2: Event Handler on SubScene
        class work2 {
            private SubScene scene;

            private SubScene loadSubScene() {
                VBox root = new VBox();
                scene = new SubScene(root, 0, 0);
                scene.setFill(Color.FIREBRICK);
                scene.setCamera(new PerspectiveCamera(true));
                scene.addEventHandler(KeyEvent.KEY_TYPED, e -> System.err.println("Key " + e.getCharacter()));
                return scene;
            }
        }

        // #work3:
        TextArea work3 = new TextArea();
        for (int i = 0; i < 100; i++)
            work3.appendText("" + i + "\n");

        // Menu
        SuperBar.SuperMenu m1 = b.new SuperMenu("Box", i2);
        SuperBar.SuperMenu.SuperItem m1i0 = m1.new SuperItem("#work 1", work1);
        SuperBar.SuperMenu.SuperItem m1i1 = m1.new SuperItem("#work 2", new work2().loadSubScene());
        SuperBar.SuperMenu.SuperItem m1i2 = m1.new SuperItem("#work 3", work3);
        SuperBar.SuperMenu.SuperItem m1i3 = m1.new SuperItem("#work 4.0", new SubScene(new VBox(), 0, 0));
        SuperBar.SuperMenu.SuperItem m1i4 = m1.new SuperItem("#work 4.1", new Region());
    }

    private void initJavaMenu() {
        // Menu
        SuperBar.SuperMenu m1 = b.new SuperMenu("Box", i3);
        SuperBar.SuperMenu.SuperItem m1i1 = m1.new SuperItem("#1", new Example().loadExample1());
    }

    private void initJava2dMenu() {
        // Menu
        SuperBar.SuperMenu m1 = b.new SuperMenu("Box", i4);
        AntCalculator.ReturnObject ro = new AntCalculator().loadAntCalculator();
        SuperBar.SuperMenu.SuperItem m1i1 = m1.new SuperItem("#Ant Calculator", ro.getSubScene(), ro.getAnimationTimer());
        SuperBar.SuperMenu.SuperItem m1i2 = m1.new SuperItem("#Chess", new VBox());
        SuperBar.SuperMenu m2 = b.new SuperMenu("Examples", i4);
        SuperBar.SuperMenu.SuperItem m2i1 = m2.new SuperItem("#1 Canvas API", new CanvasAPI().loadCanvasAPI(1));
        SuperBar.SuperMenu.SuperItem m2i2 = m2.new SuperItem("#2 Canvas API", new CanvasAPI().loadCanvasAPI(2));
        SuperBar.SuperMenu.SuperItem m2i3 = m2.new SuperItem("#3 Canvas API", new CanvasAPI().loadCanvasAPI(3));
        m2i3.addHelp("Move over the blue canvas while\n" +
                " any mouse button is clicked.\n" +
                "To reset the blue canvas double-\n" +
                "click any mouse button.");
        SuperBar.SuperMenu.SuperItem m2i4 = m2.new SuperItem("#4 Canvas API", new CanvasAPI().loadCanvasAPI(4));
        m2i4.addHelp("Front Canvas\n" +
                "Click 'G' or 'B' to change the foreground canvas");
        SuperBar.SuperMenu.SuperItem m2i5 = m2.new SuperItem("#5 Slider Sample", new SliderExample().load());
        SuperBar.SuperMenu m3 = b.new SuperMenu("Simulations", i4);
        SuperBar.SuperMenu.SuperItem m3i1 = m3.new SuperItem("Screen Wall Mount", new ScreenMountingSimulation().load());

        // overwrite SuperBar setOnAction Method, the program is faster this way
        m1i2.getMENU_ITEM().setOnAction(action -> {
            b.getSTACK_PANE().getChildren().removeAll(b.getSTACK_PANE().getChildren());
            b.getSTACK_PANE().getChildren().add(new game2_Main().loadChess());
        });
    }

    private void initJava3dMenu() {
        // Simple 3D
        Simple3D s3d = new Simple3D();

        // Menu
        SuperBar.SuperMenu m1 = b.new SuperMenu("Box", i5);
        SuperBar.SuperMenu.SuperItem m1i1 = m1.new SuperItem("#Simple 3d Box", s3d.start());
        SuperBar.SuperMenu m2 = b.new SuperMenu("Examples", i5);
        SuperBar.SuperMenu.SuperItem m2i1 = m2.new SuperItem("#Rotation Transform", new Example_3D().load3dExample(1));
        SuperBar.SuperMenu.SuperItem m2i2 = m2.new SuperItem("#MSAA", new Example_3D().load3dExample(2));
        SuperBar.SuperMenu.SuperItem m2i3 = m2.new SuperItem("#Pick Mesh", new PickMesh_3D().loadPickMesh());
        m2i3.addHelp("Pick Mesh\n" +
                "Press 'X' to hide/show the mesh.\n");
        SuperBar.SuperMenu.SuperItem m2i4 = m2.new SuperItem("#Molecule", new Molecule_3D().loadMolecule());
        m2i4.addHelp("Molecule\n" +
                "Press 'Z' to reset the position of the molecule.\n" +
                "Press 'X' to hide/show the axis.\n" +
                "Press 'V' to hide/show the molecule.");

        // Action Listener
        m1i1.getMENU_ITEM().addEventHandler(ActionEvent.ANY, event -> s3d.reset());
    }

    private void neuralNet(TextArea ta, XYChart.Series<Number, Number> series) {
        //clear
        ta.clear();
        series.getData().clear();

        // Display Information
        int trainingPass = 0;
        /* ********************Neural Net************** */
        // Topology
        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(2);
        topology.add(4);
        topology.add(1);

        // Create the Neural Net
        Net myNet = new Net(topology, ta);

        // Lists
        ArrayList<Double> inputVals = new ArrayList<>();
        ArrayList<Double> targetVals = new ArrayList<>();
        ArrayList<Double> resultVals = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            // Create Training Data
            int random1 = Math.round(Math.round(Math.random()));
            int random2 = Math.round(Math.round(Math.random()));
            TrainingData trainData = new TrainingData(random1, random2);

            // Transfer Training Data
            inputVals.add((double) random1);
            inputVals.add((double) random2);
            targetVals.add((double) trainData.output);

            // Get new input data and feed it forward:
            myNet.feedForward(inputVals);

            // Collect the net's actual results:
            myNet.getResults(resultVals);

            // Train the net what the outputs should have been:
            myNet.backProp(targetVals);

            // Report how well the training is working, average over recent samples:
            series.getData().add(new XYChart.Data<>(i, myNet.getRecentAverageError()));

            // Display Information
            trainingPass++;
            if (i % 10 == 0) {
                ta.appendText("\n");
                ta.appendText("Pass: " + trainingPass + "\n");
                ta.appendText("Inputs: " + inputVals.get(inputVals.size() - 1) + " " + inputVals.get(inputVals.size() - 2) + "\n");
                ta.appendText("Outputs: " + resultVals + "\n");
                ta.appendText("Target: " + targetVals.get(targetVals.size() - 1) + "\n");
                ta.appendText("Net recent average error: " + myNet.getRecentAverageError() + "\n");
            }
        }
        // Display Information
        ta.appendText("\n");
        ta.appendText("Done!");
        /* ******************************************** */
    }

    public static void main(String[] args) {
        launch(args);
    }

}
