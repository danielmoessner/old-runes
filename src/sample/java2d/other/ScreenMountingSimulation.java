package sample.java2d.other;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class ScreenMountingSimulation {
    // canvas
    private Canvas canvas;
    private GraphicsContext gc;
    private final double canvasXLength = 750, canvasYLength = 450;
    // increase factor
    private double k = 5;
    // room
    private double deskXLength = 140;
    private double deskYLength = 80;
    private double speakerXLength = 20;
    private double speakerYLength = 32;
    private double speakerDistance = 90;
    // screen mounting
    private ScreenMounting w1;
    private ScreenMounting w2;

    public ScreenMountingSimulation() {
        deskXLength *= k;
        deskYLength *= k;
        speakerXLength *= k;
        speakerYLength *= k;
        speakerDistance *= k;

        canvas = new Canvas(canvasXLength, canvasYLength);
        gc = canvas.getGraphicsContext2D();
        w1 = new ScreenMounting();
        w2 = new ScreenMounting(51, 4, 7.5);
    }

    private void drawSimulation() {
        // clear
        gc.clearRect(0, 0, canvasXLength, canvasYLength);

        // desk
        gc.setFill(Color.STEELBLUE);
        gc.fillRect((canvasXLength - deskXLength) / 2, 0, deskXLength, deskYLength);
        // speaker
        gc.setFill(Color.RED);
        gc.fillRect(canvasXLength / 2 - (speakerDistance / 2 + speakerXLength / 2) - speakerXLength / 2, 0, speakerXLength, speakerYLength);
        gc.fillRect(canvasXLength / 2 + (speakerDistance / 2 + speakerXLength / 2) - speakerXLength / 2, 0, speakerXLength, speakerYLength);
        // middle line
        gc.setFill(Color.YELLOW);
        gc.fillRect(canvasXLength / 2 - 2, 0, 4, canvasYLength);

        // screen mountings
        w1.draw();
        w2.draw();

        // frame
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private GridPane controls() {
        // controls
        final GridPane w1Controls = w1.controls();
        final GridPane w2Controls = w2.controls();
        // mouse
        final Label mouse_label = new Label("Mouse X | Mouse Y");
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String text = "Canvas X: " + mouseEvent.getX() / k + " | Canvas Y: " + mouseEvent.getY() / k;
                mouse_label.setText(text);
            }
        });
        // pane
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(25);
        grid.setHgap(20);
        GridPane.setConstraints(w1Controls, 0, 0);
        GridPane.setConstraints(w2Controls, 0, 1);
        GridPane.setConstraints(mouse_label, 0, 2);
        grid.getChildren().addAll(w1Controls, w2Controls, mouse_label);
        return grid;
    }

    public GridPane load() {
        drawSimulation();

        GridPane controls = controls();

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        GridPane.setConstraints(pane, 0, 0);
        GridPane.setConstraints(controls, 1, 0);
        pane.getChildren().addAll(canvas, controls);
        return pane;
    }


    private class ScreenMounting {
        //
        private double axisThickness = 4;
        // mount
        private double mountX = canvasXLength / 2;
        private double mountY = 0;
        private double wallMountLength = 4;
        private DoubleProperty wallMountX = new SimpleDoubleProperty();
        private DoubleProperty wallMountY = new SimpleDoubleProperty();
        // axis
        private double axis1Angle = 0;
        private double axis1Length = 10.5;
        private DoubleProperty axis1X = new SimpleDoubleProperty();
        private DoubleProperty axis1Y = new SimpleDoubleProperty();
        private double axis2Angle = 0;
        private double axis2Length = 24;
        private DoubleProperty axis2X = new SimpleDoubleProperty();
        private DoubleProperty axis2Y = new SimpleDoubleProperty();
        private double axis3Angle = 0;
        private double axis3Length = 10;
        private DoubleProperty axis3X = new SimpleDoubleProperty();
        private DoubleProperty axis3Y = new SimpleDoubleProperty();
        // screen
        private double screenXLength = 47;
        private double screenYLength = 6.5;

        // constructor
        ScreenMounting() {
            wallMountLength *= k;
            axis1Length *= k;
            axis2Length *= k;
            axis3Length *= k;
            axisThickness *= k;
            screenYLength *= k;
            screenXLength *= k;
        }

        ScreenMounting(double screenXLength, double screenYLength, double axis3Length) {
            this.screenXLength = screenXLength;
            this.screenYLength = screenYLength;
            this.axis3Length = axis3Length;

            this.wallMountLength *= k;
            this.axis1Length *= k;
            this.axis2Length *= k;
            this.axis3Length *= k;
            this.axisThickness *= k;
            this.screenYLength *= k;
            this.screenXLength *= k;
        }

        // draw
        private void drawPoint(double x, double y) {
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 3, y - 3, 6, 6);
        }

        private void drawMount(double mountX, double mountY, double thickness, double length,
                               DoubleProperty axisX, DoubleProperty axisY, Color c) {
            // mount
            gc.setFill(c);
            gc.fillRoundRect(mountX - thickness / 2, mountY - thickness / 2,
                    thickness, length + thickness,
                    thickness, thickness);
            // end of the mount
            axisX.setValue(mountX);
            axisY.setValue(mountY + length);
        }

        private void drawAxis(double x, double y, double thickness, double length, double angle,
                              DoubleProperty axisX, DoubleProperty axisY, Color c, boolean lastAxis) {
            gc.setFill(c);
            // axis
            Rotate r = new Rotate(angle, x, y);
            gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            if (!lastAxis) gc.fillRoundRect(x - thickness / 2, y - thickness / 2,
                    thickness, length + thickness, thickness, thickness);
            else gc.fillRect(x - thickness / 2, y - thickness / 2, thickness, length + thickness / 2);
            r.setAngle(0);
            gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            // end of the axis
            axisX.setValue(x - length * Math.sin(Math.toRadians(angle)));
            axisY.setValue(y + length * Math.cos(Math.toRadians(angle)));
        }

        private void drawScreen(double x, double y, double xLength, double yLength, double angle, Color c) {
            gc.setFill(c);
            gc.setGlobalAlpha(0.8);
            // screen
            Rotate r = new Rotate(angle, x, y);
            gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            gc.fillRect(x - xLength / 2, y - yLength, xLength, yLength);
            r.setAngle(0);
            gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            gc.setGlobalAlpha(1);
        }

        private void draw() {
            // mount and axis and screen
            drawMount(mountX, mountY, axisThickness, wallMountLength, wallMountX, wallMountY, Color.BLUE);
            drawAxis(wallMountX.getValue(), wallMountY.getValue(), axisThickness, axis1Length, axis1Angle, axis1X, axis1Y, Color.CHOCOLATE, false);
            drawAxis(axis1X.getValue(), axis1Y.getValue(), axisThickness, axis2Length, axis2Angle, axis2X, axis2Y, Color.BISQUE, false);
            drawAxis(axis2X.getValue(), axis2Y.getValue(), axisThickness, axis3Length, axis3Angle, axis3X, axis3Y, Color.GREEN, true);
            drawScreen(axis3X.getValue(), axis3Y.getValue(), screenXLength, screenYLength, axis3Angle, Color.DARKGREEN);
            // important points
            drawPoint(mountX, mountY);
            drawPoint(wallMountX.getValue(), wallMountY.getValue());
            drawPoint(axis1X.getValue(), axis1Y.getValue());
            drawPoint(axis2X.getValue(), axis2Y.getValue());
        }

        // controls
        private GridPane controls() {
            // slider
            final Label l1 = new Label("Axis 1 Angle:");
            final Label l2 = new Label("Axis 2 Angle:");
            final Label l3 = new Label("Screen Angle:");
            final Label l4 = new Label("Mount X Value:");
            final Slider s1 = new Slider(80, 280, 180);
            final Slider s2 = new Slider(0, 360, 180);
            final Slider s3 = new Slider(90, 270, 180);
            final Slider s4 = new Slider(-45, 45, 0);
            final Label mount_label = new Label("Mount X");
            s1.setMinWidth(200);
            s2.setMinWidth(200);
            s3.setMinWidth(200);
            s4.setMinWidth(200);
            s1.valueProperty().addListener(change -> {
                axis1Angle = -s1.getValue() + 180;
                drawSimulation();
            });
            s2.valueProperty().addListener(change -> {
                axis2Angle = -s2.getValue() + 180;
                drawSimulation();
            });
            s3.valueProperty().addListener(change -> {
                axis3Angle = -s3.getValue() + 180;
                drawSimulation();
            });
            s4.valueProperty().addListener(change -> {
                mountX = canvasXLength / 2 + s4.getValue() * k;
                mount_label.setText("Mount X: " + String.format("%.1f", s4.getValue()));
                drawSimulation();
            });
            // pane
            final GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setVgap(10);
            grid.setHgap(20);
            GridPane.setConstraints(l1, 0, 0);
            GridPane.setConstraints(s1, 1, 0);
            GridPane.setConstraints(l2, 0, 1);
            GridPane.setConstraints(s2, 1, 1);
            GridPane.setConstraints(l3, 0, 2);
            GridPane.setConstraints(s3, 1, 2);
            GridPane.setConstraints(l4, 0, 3);
            GridPane.setConstraints(s4, 1, 3);
            GridPane.setConstraints(mount_label, 0, 4, 3, 1);
            grid.getChildren().addAll(l1, s1, l2, s2, l3, s3, l4, s4, mount_label);
            return grid;
        }
    }
}
