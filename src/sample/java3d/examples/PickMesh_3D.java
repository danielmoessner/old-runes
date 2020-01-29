package sample.java3d.examples;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PickMesh_3D {

    MeshView meshView;
    Text caption, data;
    final static float minX = -10;
    final static float minY = -10;
    final static float maxX = 10;
    final static float maxY = 10;

    static TriangleMesh buildTriangleMesh(int subDivX, int subDivY, float scale) {

        final int pointSize = 3;
        final int texCoordSize = 2;
        // 3 point indices and 3 texCoord indices per triangle
        final int faceSize = 6;
        int numDivX = subDivX + 1;
        int numVerts = (subDivY + 1) * numDivX;
        float points[] = new float[numVerts * pointSize];
        float texCoords[] = new float[numVerts * texCoordSize];
        int faceCount = subDivX * subDivY * 2;
        int faces[] = new int[faceCount * faceSize];

        // Create points and texCoords
        for (int y = 0; y <= subDivY; y++) {
            float dy = (float) y / subDivY;
            double fy = (1 - dy) * minY + dy * maxY;

            for (int x = 0; x <= subDivX; x++) {
                float dx = (float) x / subDivX;
                double fx = (1 - dx) * minX + dx * maxX;

                int index = y * numDivX * pointSize + (x * pointSize);
                points[index] = (float) fx * scale;
                points[index + 1] = (float) fy * scale;
                points[index + 2] = 0.0f;

                index = y * numDivX * texCoordSize + (x * texCoordSize);
                texCoords[index] = dx;
                texCoords[index + 1] = dy;
            }
        }

        // Create faces
        for (int y = 0; y < subDivY; y++) {
            for (int x = 0; x < subDivX; x++) {
                int p00 = y * numDivX + x;
                int p01 = p00 + 1;
                int p10 = p00 + numDivX;
                int p11 = p10 + 1;
                int tc00 = y * numDivX + x;
                int tc01 = tc00 + 1;
                int tc10 = tc00 + numDivX;
                int tc11 = tc10 + 1;

                int index = (y * subDivX * faceSize + (x * faceSize)) * 2;
                faces[index] = p00;
                faces[index + 1] = tc00;
                faces[index + 2] = p10;
                faces[index + 3] = tc10;
                faces[index + 4] = p11;
                faces[index + 5] = tc11;

                index += faceSize;
                faces[index] = p11;
                faces[index + 1] = tc11;
                faces[index + 2] = p01;
                faces[index + 3] = tc01;
                faces[index + 4] = p00;
                faces[index + 5] = tc00;
            }
        }

        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        return triangleMesh;
    }

    private void activateShape(final Shape3D shape, final String name) {
        shape.setId(name);

        EventHandler<MouseEvent> moveHandler = me -> {
            PickResult res = me.getPickResult();
            updateOverlay(res);
            me.consume();
        };

        shape.setOnMouseMoved(moveHandler);
        shape.setOnMouseDragOver(moveHandler);

        shape.setOnMouseEntered(me -> {
            PickResult res = me.getPickResult();
            if (res == null) {
                System.err.println("Mouse entered has not pickResult");
            }
            updateOverlay(res);
        });

        shape.setOnMouseExited(me -> {
            PickResult res = me.getPickResult();
            if (res == null) {
                System.err.println("Mouse exited has not pickResult");
            }
            updateOverlay(res);
            me.consume();
        });
    }

    private SubScene buildScene() {
        meshView = new MeshView(buildTriangleMesh(2, 2, 30));
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setCullFace(CullFace.NONE);
        activateShape(meshView, "MeshView");

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GOLD);
        material.setSpecularColor(Color.rgb(30, 30, 30));
        meshView.setMaterial(material);

        final PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(176);
        pointLight.setTranslateY(114);
        pointLight.setTranslateZ(-100);

        Group group = new Group(meshView);
        group.setTranslateX(200);
        group.setTranslateY(200);
        group.setTranslateZ(400);

        final Group root = new Group(group, pointLight);

        final SubScene subScene = new SubScene(root, 396, 396, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(new PerspectiveCamera());
        subScene.setFill(Color.TRANSPARENT);
        subScene.setId("SubScene");

        return subScene;
    }

    private Node createOverlay() {
        HBox hBox = new HBox(10);

        caption = new Text("Node:\n\nPoint:\nTexture Coord:\nFace:\nDistance:");
        caption.setFont(Font.font("Times New Roman", 18));
        caption.setTextOrigin(VPos.TOP);
        caption.setTextAlignment(TextAlignment.RIGHT);

        data = new Text("-- None --\n\n\n\n");
        data.setFont(Font.font("Times New Roman", 18));
        data.setTextOrigin(VPos.TOP);
        data.setTextAlignment(TextAlignment.LEFT);

        Rectangle rect = new Rectangle(300, 150, Color.color(0.2, 0.5, 0.3, 0.8));
        hBox.getChildren().addAll(caption, data);
        return new Group(rect, hBox);
    }

    final void updateOverlay(PickResult result) {
        if (result.getIntersectedNode() == null) {
            data.setText("Scene\n\n"
                    + point3DToString(result.getIntersectedPoint()) + "\n"
                    + point2DToString(result.getIntersectedTexCoord()) + "\n"
                    + result.getIntersectedFace() + "\n"
                    + String.format("%.1f", result.getIntersectedDistance()));
        } else {
            data.setText(result.getIntersectedNode().getId() + "\n"
                    + getCullFace(result.getIntersectedNode()) + "\n"
                    + point3DToString(result.getIntersectedPoint()) + "\n"
                    + point2DToString(result.getIntersectedTexCoord()) + "\n"
                    + result.getIntersectedFace() + "\n"
                    + String.format("%.1f", result.getIntersectedDistance()));
        }
    }

    private static String point3DToString(Point3D pt) {
        if (pt == null) {
            return "null";
        }
        return String.format("%.1f; %.1f; %.1f", pt.getX(), pt.getY(), pt.getZ());
    }

    private static String point2DToString(Point2D pt) {
        if (pt == null) {
            return "null";
        }
        return String.format("%.2f; %.2f", pt.getX(), pt.getY());
    }

    private static String getCullFace(Node n) {
        if (n instanceof Shape3D) {
            return "(CullFace." + ((Shape3D) n).getCullFace() + ")";
        }
        return "";
    }

    public SubScene loadPickMesh() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            throw new RuntimeException("*** ERROR: common conditional SCENE3D is not supported");
        }

        BorderPane root = new BorderPane(buildScene(), null, null, null, createOverlay());

        SubScene scene = new SubScene(root, 0, 0);
        scene.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            switch (event.getCharacter().toLowerCase()) {
                case "x":
                    boolean wireframe = meshView.getDrawMode() == DrawMode.LINE;
                    meshView.setDrawMode(wireframe ? DrawMode.FILL : DrawMode.LINE);
            }
        });
        return scene;
    }

}
