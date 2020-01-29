package sample.java2d.examples;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;

public class CanvasAPI {

    // number 2
    private void drawDShape(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(70, 70);
        gc.bezierCurveTo(150, 20, 150, 150, 75, 150);
        gc.closePath();
    }

    private void drawRadialGradient(Color firstColor, Color lastColor, GraphicsContext gc) {
        gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.1, true,
                CycleMethod.REFLECT,
                new Stop(0.0, firstColor),
                new Stop(1.0, lastColor)));
        gc.fill();
    }

    private void drawLinearGradient(Color firstColor, Color secondColor, GraphicsContext gc) {
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.REFLECT,
                new Stop(0.0, firstColor),
                new Stop(1.0, secondColor));
        gc.setStroke(lg);
        gc.setLineWidth(20);
        gc.stroke();
    }

    private void drawDropShadow(Color firstColor, Color secondColor, Color thirdColor, Color fourthColor,
                                GraphicsContext gc) {
        gc.applyEffect(new DropShadow(20, 20, 0, firstColor));
        gc.applyEffect(new DropShadow(20, 0, 20, secondColor));
        gc.applyEffect(new DropShadow(20, -20, 0, thirdColor));
        gc.applyEffect(new DropShadow(20, 0, -20, fourthColor));
    }

    // load
    public Region loadCanvasAPI(int which) {
        switch (which) {
            case 1:
                Canvas canvas1 = new Canvas(200, 250);
                GraphicsContext gc1 = canvas1.getGraphicsContext2D();
                gc1.setFill(Color.GREEN);
                gc1.setStroke(Color.BLUE);
                gc1.setLineWidth(5);
                gc1.strokeLine(40, 10, 10, 40);
                gc1.fillOval(10, 60, 30, 30);
                gc1.strokeOval(60, 60, 30, 30);
                gc1.fillRoundRect(110, 60, 30, 30, 10, 10);
                gc1.strokeRoundRect(160, 60, 30, 30, 10, 10);
                gc1.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
                gc1.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
                gc1.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
                gc1.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
                gc1.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
                gc1.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
                gc1.fillPolygon(new double[]{10, 40, 10, 40},
                        new double[]{210, 210, 240, 240}, 4);
                gc1.strokePolygon(new double[]{60, 90, 60, 90},
                        new double[]{210, 210, 240, 240}, 4);
                gc1.strokePolyline(new double[]{110, 140, 110, 140},
                        new double[]{210, 210, 240, 240}, 4);

                // not part of the orig code
                gc1.setStroke(Color.BLACK);
                gc1.setLineWidth(8);
                gc1.strokeRect(0, 0, canvas1.getWidth(), canvas1.getHeight());
                //

                return new StackPane(canvas1);
            case 2:
                Canvas canvas2 = new Canvas(200, 205);
                GraphicsContext gc2 = canvas2.getGraphicsContext2D();
                drawDShape(gc2);
                drawRadialGradient(Color.RED, Color.YELLOW, gc2);
                drawLinearGradient(Color.BLUE, Color.GREEN, gc2);
                drawDropShadow(Color.GRAY, Color.BLUE, Color.GREEN, Color.RED, gc2);

                // not part of the orig code
                gc2.setStroke(Color.BLACK);
                gc2.setLineWidth(8);
                gc2.strokeRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
                //

                return new StackPane(canvas2);
            case 3:
                Canvas canvas3 = new Canvas(300, 300);
                GraphicsContext gc3 = canvas3.getGraphicsContext2D();
                // Draw background with gradient
                Rectangle rect3 = new Rectangle(canvas3.getWidth() + 100, canvas3.getHeight() + 100);
                rect3.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT,
                        new Stop(0, Color.RED), new Stop(1, Color.YELLOW)));
                // Create the Canvas, filled in with Blue
                gc3.setFill(Color.BLUE);
                gc3.fillRect(0, 0, canvas3.getWidth(), canvas3.getHeight());
                // Clear away portions as the user drags the mouse
                canvas3.addEventHandler(MouseEvent.MOUSE_DRAGGED, event ->
                        gc3.clearRect(event.getX() - 2, event.getY() - 2, 4, 4));
                // Fill the Canvas with a Blue rectangle when the user double-clicks
                canvas3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getClickCount() > 1) {
                        gc3.setFill(Color.BLUE);
                        gc3.fillRect(0, 0, canvas3.getWidth(), canvas3.getHeight());
                    }
                });

                // not part of the orig code
                Rectangle rect = new Rectangle(canvas3.getWidth() + 108, canvas3.getHeight() + 108);
                //

                StackPane pane = new StackPane(rect, rect3, canvas3);

                return new StackPane(pane);
            case 4:
                // Layers 1&2 are the same size
                Canvas canvas4_1 = new Canvas(500, 500);
                Canvas canvas4_2 = new Canvas(500, 500);

                // Obtain Graphics Contexts
                GraphicsContext gc4_1 = canvas4_1.getGraphicsContext2D();
                gc4_1.setFill(Color.GREEN);
                gc4_1.fillOval(50, 50, 20, 20);
                GraphicsContext gc4_2 = canvas4_2.getGraphicsContext2D();
                gc4_2.setFill(Color.BLUE);
                gc4_2.fillOval(100, 100, 20, 20);

                // Handler for Layer 1 & 2
                canvas4_1.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> gc4_1.fillOval(e.getX() - 10, e.getY() - 10, 20, 20));
                canvas4_2.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> gc4_2.fillOval(e.getX() - 10, e.getY() - 10, 20, 20));

                // not part of the orig code
                gc4_1.setStroke(Color.GREEN);
                gc4_1.setLineWidth(8);
                gc4_1.strokeRect(0, 0, canvas4_1.getWidth(), canvas4_1.getHeight());
                gc4_2.setStroke(Color.BLUE);
                gc4_2.setLineWidth(8);
                gc4_2.strokeRect(0, 0, canvas4_2.getWidth(), canvas4_2.getHeight());
                //

                StackPane stackPane4 = new StackPane(canvas4_1, canvas4_2);
                stackPane4.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.G)
                        canvas4_1.toFront();
                    if (event.getCode() == KeyCode.B)
                        canvas4_2.toFront();
                });

                return stackPane4;
        }
        return new Region();
    }

}
