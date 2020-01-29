package sample.java3d.examples;

import javafx.animation.*;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.util.*;

/**
 * Example of how Rotation Transforms work in JavaFX
 */
public class Example_3D {

    // number 1

    /**
     * Generate a new slider control initialized to the given value.
     */
    private Slider createSlider(final double value, final String helpText) {
        final Slider slider = new Slider(-50, 151, value);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setStyle("-fx-text-fill: white");
        slider.setTooltip(new Tooltip(helpText));
        return slider;
    }

    // number 2
    private static Parent setTitle(String str) {
        final VBox vbox = new VBox();
        final Text text = new Text(str);
        text.setFont(Font.font("Times New Roman", 24));
        text.setFill(Color.WHEAT);
        vbox.getChildren().add(text);
        return vbox;
    }

    private static SubScene createSubScene(String title, Node node,
                                           Paint fillPaint, Camera camera, boolean msaa) {
        Group root = new Group();

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(50);
        light.setTranslateY(-300);
        light.setTranslateZ(-400);
        PointLight light2 = new PointLight(Color.color(0.6, 0.3, 0.4));
        light2.setTranslateX(400);
        light2.setTranslateY(0);
        light2.setTranslateZ(-400);

        AmbientLight ambientLight = new AmbientLight(Color.color(0.2, 0.2, 0.2));
        node.setRotationAxis(new Point3D(2, 1, 0).normalize());
        node.setTranslateX(180);
        node.setTranslateY(180);
        root.getChildren().addAll(setTitle(title), ambientLight,
                light, light2, node);

        SubScene subScene = new SubScene(root, 500, 400, true,
                msaa ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
        subScene.setFill(fillPaint);
        subScene.setCamera(camera);

        return subScene;
    }

    // load
    public SubScene load3dExample(int which) {
        switch (which) {
            case 1:
                // Rotation Transform Example
                // create some controls to manipulate the x and y pivot points of the rotation.
                final Slider xPivotSlider = createSlider(50, "Best values are 0, 50 or 100");
                final Slider yPivotSlider = createSlider(50, "Best values are 0, 50 or 100");
                final Slider zPivotSlider = createSlider(0, "Won't do anything until you use an X or Y axis of rotation");
                final ToggleGroup axisToggleGroup = new ToggleGroup();
                final ToggleButton xAxisToggleButton = new ToggleButton("X Axis");
                final ToggleButton yAxisToggleButton = new ToggleButton("Y Axis");
                final ToggleButton zAxisToggleButton = new ToggleButton("Z Axis");
                xAxisToggleButton.setToggleGroup(axisToggleGroup);
                yAxisToggleButton.setToggleGroup(axisToggleGroup);
                zAxisToggleButton.setToggleGroup(axisToggleGroup);

                // create a node to animate.
                Node square = new ImageView(new Image("/pictures/Smurf.jpg"));
                square.setTranslateZ(150);
                square.setOpacity(0.7);
                square.setMouseTransparent(true);

                // create a rotation transform starting at 0 degrees, rotating about pivot point 50, 50.
                final Rotate rotationTransform = new Rotate(0, 50, 50);
                square.getTransforms().add(rotationTransform);

                // rotate a square using timeline attached to the rotation transform's angle property.
                final Timeline rotationAnimation = new Timeline();
                rotationAnimation.getKeyFrames()
                        .add(new KeyFrame(Duration.seconds(5), new KeyValue(
                                rotationTransform.angleProperty(),
                                360)));
                rotationAnimation.setCycleCount(Animation.INDEFINITE);
                rotationAnimation.play();

                // bind the transforms pivot points to our slider controls.
                rotationTransform.pivotXProperty().bind(xPivotSlider.valueProperty());
                rotationTransform.pivotYProperty().bind(yPivotSlider.valueProperty());
                rotationTransform.pivotZProperty().bind(zPivotSlider.valueProperty());

                // allow our toggle controls to choose the axis of rotation..
                xAxisToggleButton.setOnAction(e -> rotationTransform.setAxis(Rotate.X_AXIS));
                yAxisToggleButton.setOnAction(e -> rotationTransform.setAxis(Rotate.Y_AXIS));
                zAxisToggleButton.setOnAction(e -> rotationTransform.setAxis(Rotate.Z_AXIS));

                // display a crosshair to mark the current pivot point.
                final Line verticalLine = new Line(0, -10, 0, 10);
                verticalLine.setStroke(Color.FIREBRICK);
                verticalLine.setStrokeWidth(3);
                verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);
                final Line horizontalLine = new Line(-10, 0, 10, 0);
                horizontalLine.setStroke(Color.FIREBRICK);
                horizontalLine.setStrokeWidth(3);
                verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);
                Group pivotMarker = new Group(verticalLine, horizontalLine);
                pivotMarker.translateXProperty().bind(xPivotSlider.valueProperty().subtract(50));
                pivotMarker.translateYProperty().bind(yPivotSlider.valueProperty().subtract(50));

                // display a dashed square border outline to mark the original location of the square.
                final Rectangle squareOutline = new Rectangle(100, 100);
                squareOutline.setFill(Color.TRANSPARENT);
                squareOutline.setOpacity(0.7);
                squareOutline.setMouseTransparent(true);
                squareOutline.setStrokeType(StrokeType.INSIDE);
                squareOutline.setStrokeWidth(1);
                squareOutline.setStrokeLineCap(StrokeLineCap.BUTT);
                squareOutline.setStroke(Color.DARKGRAY);
                squareOutline.setStrokeDashOffset(5);
                squareOutline.getStrokeDashArray().add(10.0);

                // layout the scene.
                final HBox xPivotControl = new HBox(5);
                xPivotControl.getChildren().addAll(new Label("X Pivot Point"), xPivotSlider);
                HBox.setHgrow(xPivotSlider, Priority.ALWAYS);

                final HBox yPivotControl = new HBox(5);
                yPivotControl.getChildren().addAll(new Label("Y Pivot Point"), yPivotSlider);
                HBox.setHgrow(yPivotSlider, Priority.ALWAYS);

                final HBox zPivotControl = new HBox(5);
                zPivotControl.getChildren().addAll(new Label("Z Pivot Point"), zPivotSlider);
                HBox.setHgrow(zPivotSlider, Priority.ALWAYS);

                final HBox axisControl = new HBox(20);
                axisControl.getChildren().addAll(new Label("Axis of Rotation"), xAxisToggleButton, yAxisToggleButton, zAxisToggleButton);
                axisControl.setAlignment(Pos.BASELINE_LEFT);

                final StackPane displayPane = new StackPane();
                displayPane.getChildren().addAll(square, pivotMarker, squareOutline);
                displayPane.setTranslateY(80);
                displayPane.setMouseTransparent(true);

                final VBox layout = new VBox();
                layout.getChildren().addAll(xPivotControl, yPivotControl, zPivotControl, axisControl, displayPane);
                layout.setStyle("-fx-background-color: linear-gradient(to bottom, cornsilk, midnightblue); -fx-padding:10; -fx-font-size: 16");

                //
                return new SubScene(layout, 0, 0);
            case 2:
                // MSAA Example
                if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
                    throw new RuntimeException("*** ERROR: common conditional SCENE3D is not supported");
                }

                HBox hbox = new HBox();
                hbox.setLayoutX(25);
                hbox.setLayoutY(25);

                PhongMaterial phongMaterial = new PhongMaterial(Color.color(1.0, 0.7, 0.8));
                Cylinder cylinder1 = new Cylinder(100, 200);
                cylinder1.setMaterial(phongMaterial);
                SubScene noMsaa = createSubScene("MSAA = false", cylinder1,
                        Color.TRANSPARENT,
                        new PerspectiveCamera(), false);
                hbox.getChildren().add(noMsaa);

                Cylinder cylinder2 = new Cylinder(100, 200);
                cylinder2.setMaterial(phongMaterial);
                SubScene msaa = createSubScene("MSAA = true", cylinder2,
                        Color.TRANSPARENT,
                        new PerspectiveCamera(), true);
                hbox.getChildren().add(msaa);

                Slider slider = new Slider(0, 360, 0);
                slider.setBlockIncrement(1);
                slider.setTranslateX(375);
                slider.setTranslateY(300);
                cylinder1.rotateProperty().bind(slider.valueProperty());
                cylinder2.rotateProperty().bind(slider.valueProperty());

                Group root = new Group();
                root.getChildren().addAll(hbox, slider);

                SubScene scene = new SubScene(root, 0, 0);
                scene.setFill(Color.color(0.2, 0.2, 0.2, 1.0));

                return scene;
            default:
                return new SubScene(new VBox(), 0, 0);
        }
    }
}
