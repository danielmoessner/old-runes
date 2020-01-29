package sample.java3d.simple3d;

import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.util.Duration;

import java.util.ArrayList;

public class Simple3D {

    Translate translate = new Translate(0, 0, 0);
    Rotate rotateX = new Rotate(0, new Point3D(1, 0, 0));
    Rotate rotateY = new Rotate(0, new Point3D(0, 1, 0));
    Rotate rotateZ = new Rotate(0, new Point3D(0, 0, 1));
    Timeline animation = new Timeline();
    StringVector reference = new StringVector();

    Text tf = new Text("JavaFX 3D SUCKS!!");

    Group initGrid(int x, int y, int z) {
        if ((x + y + z != 2) || (x * y * z != 0) || (x == 2 || y == 2 || z == 2))
            return null;

        double x1, y1, z1;
        double x2, y2, z2;

        if (x == 0) {
            x1 = 0.1;
            x2 = 0.1;

            y1 = 100;
            z1 = 0.1;
            y2 = 0.1;
            z2 = 100;
        } else if (y == 0) {
            y1 = 0.1;
            y2 = 0.1;

            x1 = 0.1;
            z1 = 100;
            x2 = 100;
            z2 = 0.1;
        } else {
            z1 = 0.1;
            z2 = 0.1;

            x1 = 0.1;
            y1 = 100;
            x2 = 100;
            y2 = 0.1;
        }

        ArrayList<Box> boxes1 = new ArrayList<>();
        ArrayList<Box> boxes2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            boxes1.add(new Box(x1, y1, z1));
            boxes2.add(new Box(x2, y2, z2));

            boxes1.get(i).setDrawMode(DrawMode.FILL);
            boxes2.get(i).setDrawMode(DrawMode.FILL);

            if (x == 0) {
                boxes1.get(i).setTranslateZ(-50 + i);
                boxes2.get(i).setTranslateY(-50 + i);
                boxes1.get(i).setMaterial(new PhongMaterial(Color.RED));
                boxes2.get(i).setMaterial(new PhongMaterial(Color.RED));
            } else if (y == 0) {
                boxes1.get(i).setTranslateX(-50 + i);
                boxes2.get(i).setTranslateZ(-50 + i);
                boxes1.get(i).setMaterial(new PhongMaterial(Color.GREEN));
                boxes2.get(i).setMaterial(new PhongMaterial(Color.GREEN));
            } else {
                boxes1.get(i).setTranslateX(-50 + i);
                boxes2.get(i).setTranslateY(-50 + i);
                boxes1.get(i).setMaterial(new PhongMaterial(Color.BLUE));
                boxes2.get(i).setMaterial(new PhongMaterial(Color.BLUE));
            }
        }

        Group group = new Group();
        group.getChildren().addAll(boxes1);
        group.getChildren().addAll(boxes2);
        return group;
    }

    public void moveTranslate(double x, double y, double z) {
        double xx, yy, zz;

        xx = reference.applyX(x, y, z);
        yy = reference.applyY(x, y, z);
        zz = reference.applyZ(x, y, z);

        translate.xProperty().set(translate.xProperty().get() + xx);
        translate.yProperty().set(translate.yProperty().get() + yy);
        translate.zProperty().set(translate.zProperty().get() + zz);
    }

    public void rotate(Point3D axis) {
        if (rotateX.getAngle() % 90 != 0 || rotateY.getAngle() % 90 != 0 || rotateZ.getAngle() % 90 != 0)
            return;

        double xx, yy, zz;
        xx = reference.applyX(axis);
        yy = reference.applyY(axis);
        zz = reference.applyZ(axis);
        reference = reference.specialCrossProduct(axis);

        animation.getKeyFrames().removeAll(animation.getKeyFrames());
        System.out.print("The axe JavaFx should turn the cube around, with the grid as reference, is: ");
        if (Math.abs(xx) == 1) {
            System.out.println("x");
            animation.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(rotateX.angleProperty(), rotateX.angleProperty().getValue() + 90 * xx)));
        } else if (Math.abs(yy) == 1) {
            System.out.println("y");
            animation.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(rotateY.angleProperty(), rotateY.angleProperty().getValue() + 90 * yy)));
            animation.play();
        } else if (Math.abs(zz) == 1) {
            System.out.println("z");
            animation.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(rotateZ.angleProperty(), rotateZ.angleProperty().getValue() + 90 * zz)));
        }
        animation.play();

    }

    private void handleMouse(SubScene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            // move
            if (event.getCode() == KeyCode.A)
                moveTranslate(-1, 0, 0);
            if (event.getCode() == KeyCode.D)
                moveTranslate(1, 0, 0);
            if (event.getCode() == KeyCode.W)
                moveTranslate(0, 0, 1);
            if (event.getCode() == KeyCode.S)
                moveTranslate(0, 0, -1);
            if (event.getCode() == KeyCode.Q)
                moveTranslate(0, -1, 0);
            if (event.getCode() == KeyCode.E)
                moveTranslate(0, 1, 0);
            // rotate
            if (event.getCode() == KeyCode.F)
                rotate(new Point3D(0, -1, 0));
            if (event.getCode() == KeyCode.H)
                rotate(new Point3D(0, 1, 0));
            if (event.getCode() == KeyCode.T)
                rotate(new Point3D(-1, 0, 0));
            if (event.getCode() == KeyCode.G)
                rotate(new Point3D(1, 0, 0));
            if (event.getCode() == KeyCode.Z)
                rotate(new Point3D(0, 0, 1));
            if (event.getCode() == KeyCode.R)
                rotate(new Point3D(0, 0, -1));
        });
    }

    public void reset() {
        translate.setX(0);
        translate.setY(0);
        translate.setZ(0);
        rotateX.setAngle(0);
        rotateY.setAngle(0);
        rotateZ.setAngle(0);
        reference = new StringVector();
    }

    public SubScene start() {
        // Box
        Box redBox = new Box(1, 1, 1);
        redBox.setMaterial(new PhongMaterial(Color.AQUA));
        redBox.setDrawMode(DrawMode.FILL);
        redBox.getTransforms().addAll(translate, rotateX, rotateY, rotateZ);

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().addAll(initGrid(0, 1, 1), initGrid(1, 1, 0), initGrid(1, 0, 1), redBox);
        tf.setTranslateX(-50);
        tf.setTranslateZ(1);
        root.getChildren().add(tf);

        // Create and position camera
        Camera camera = new PerspectiveCamera(true);
        camera.translateXProperty().bind(translate.xProperty());
        camera.translateYProperty().bind(translate.yProperty().subtract(0.7));
        camera.translateZProperty().bind(translate.zProperty().subtract(1.7));

        // Use a SubScene       
        SubScene subScene = new SubScene(root, 0, 0, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        handleMouse(subScene);

        return subScene;
    }

}