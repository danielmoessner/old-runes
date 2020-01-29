package sample.java2d.examples;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SliderExample {

    private final Slider opacityLevel = new Slider(0, 1, 1);
    private final Slider sepiaTone = new Slider(0, 1, 1);
    private final Slider scaling = new Slider(0.5, 1, 1);

    private final Label opacityCaption = new Label("Opacity Level:");
    private final Label sepiaCaption = new Label("Sepia Tone:");
    private final Label scalingCaption = new Label("Scaling Factor:");

    private final Label opacityValue = new Label(Double.toString(opacityLevel.getValue()));
    private final Label sepiaValue = new Label(Double.toString(sepiaTone.getValue()));
    private final Label scalingValue = new Label(Double.toString(scaling.getValue()));

    private final static Color textColor = Color.WHEAT;
    private final static SepiaTone sepiaEffect = new SepiaTone();

    public GridPane load() {
        GridPane grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0, 0, 0, 0))));
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);

        final ImageView imageView = new ImageView("/pictures/HotChick.jpg");
        imageView.setEffect(sepiaEffect);
        GridPane.setConstraints(imageView, 0, 0, 3, 1);
        grid.getChildren().add(imageView);

        opacityCaption.setTextFill(textColor);
        opacityValue.setTextFill(textColor);
        GridPane.setConstraints(opacityCaption, 0, 1);
        GridPane.setConstraints(opacityLevel, 1, 1);
        GridPane.setConstraints(opacityValue, 2, 1);
        grid.getChildren().addAll(opacityCaption, opacityLevel, opacityValue);

        sepiaCaption.setTextFill(textColor);
        sepiaValue.setTextFill(textColor);
        GridPane.setConstraints(sepiaCaption, 0, 2);
        GridPane.setConstraints(sepiaTone, 1, 2);
        GridPane.setConstraints(sepiaValue, 2, 2);
        grid.getChildren().addAll(sepiaCaption, sepiaTone, sepiaValue);

        scalingCaption.setTextFill(textColor);
        scalingValue.setTextFill(textColor);
        GridPane.setConstraints(scalingCaption, 0, 3);
        GridPane.setConstraints(scaling, 1, 3);
        GridPane.setConstraints(scalingValue, 2, 3);
        grid.getChildren().addAll(scalingCaption, scaling, scalingValue);

        opacityLevel.valueProperty().addListener(change -> {
            imageView.setOpacity(opacityLevel.getValue());
            opacityValue.setText(String.format("%.1f", opacityLevel.getValue()));
        });
        sepiaTone.valueProperty().addListener(change -> {
            sepiaEffect.setLevel(sepiaTone.getValue());
            sepiaValue.setText(String.format("%.1f", sepiaTone.getValue()));
        });
        scaling.valueProperty().addListener(change -> {
            imageView.setScaleX(scaling.getValue());
            imageView.setScaleY(scaling.getValue());
            scalingValue.setText(String.format("%.1f", scaling.getValue()));
        });

        return grid;
    }

}