package sample.java2d.game1;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Keyboard implements EventHandler<KeyEvent> {

	private static float zoomFactor = 1;
	private AntCalculator antCalculator;

	Keyboard(AntCalculator antCalculator) {
		this.antCalculator = antCalculator;
	}

	@Override
	public void handle(KeyEvent key) {
		if (key.getCode() == KeyCode.P) {
			antCalculator.setPause(!antCalculator.isPause());
		}
		if (key.getCode() == KeyCode.PLUS) {
			applyZoom(antCalculator.getScreenBox(), 0.1f);
		}
		if (key.getCode() == KeyCode.MINUS) {
			applyZoom(antCalculator.getScreenBox(), -0.1f);
		}
		if (key.getCode() == KeyCode.UP) {
			moveMap(antCalculator.getScreenBox(), 0, 1);
		}
		if (key.getCode() == KeyCode.DOWN) {
			moveMap(antCalculator.getScreenBox(), 0, -1);
		}
		if (key.getCode() == KeyCode.RIGHT) {
			moveMap(antCalculator.getScreenBox(), -1, 0);
		}
		if (key.getCode() == KeyCode.LEFT) {
			moveMap(antCalculator.getScreenBox(), 1, 0);
		}
		if (key.getCode() == KeyCode.S) {
			antCalculator.switchScreen();
		}
	}

	private void moveMap(Node n, double x, double y) {
		double xP = n.getTranslateX() + x;
		double yP = n.getTranslateY() + y;

		n.setTranslateX(xP);
		n.setTranslateY(yP);
	}

	private void applyZoom(Node n, float zoom) {
		zoomFactor += zoom;

		n.setScaleX(zoomFactor);
		n.setScaleY(zoomFactor);
	}
}
