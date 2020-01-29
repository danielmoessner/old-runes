package sample.java2d.game1;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tribe {

	private String RULESET = "RRRRLLLL";
	private String name;
	private int[] gridMove;
	private int strength;
	private int speed;
	private int birthrate;
	private int sickness;
	private boolean alive;
	private ArrayList<Ant> ants;

	Tribe(String name, int x, int y, int strength, int speed, int birthrate, int sickness) {
		this.name = name;
		this.setBirthrate(birthrate);
		this.setStrength(strength);
		this.setSickness(sickness);
		this.setSpeed(speed);
		this.gridMove = new int[AntCalculator.WIDTH * AntCalculator.HEIGHT];
		this.setAlive(true);
		setAnts(new ArrayList<>());
		getAnts().add(new Ant(this, x, y, gridMove));
		getAnts().add(new Ant(this, x, y, gridMove));
	}
	Tribe(String name, String ruleset, int x, int y, int strength, int speed, int birthrate, int sickness) {
		this.name = name;
		this.RULESET=ruleset;
		this.setBirthrate(birthrate);
		this.setStrength(strength);
		this.setSickness(sickness);
		this.setSpeed(speed);
		this.gridMove = new int[AntCalculator.WIDTH * AntCalculator.HEIGHT];
		this.setAlive(true);
		setAnts(new ArrayList<>());
		getAnts().add(new Ant(this, x, y, gridMove));
		getAnts().add(new Ant(this, x, y, gridMove));
	}

	public void update(GraphicsContext gcm) {
		if(getAnts().size()==0)setAlive(false);
		nationalStruggle();
		renderCanvasMove(gcm);
		renderCanvasMoveBorder(gcm);
	}

	private void renderCanvasMove(GraphicsContext gcm) {
		// canvasMove
		for (int i = 0; i < AntCalculator.WIDTH; i++) {
			for (int k = 0; k < AntCalculator.HEIGHT; k++) {
				if (this.gridMove[i* AntCalculator.WIDTH+k] != 0) {
					float colorValue = 1 - (float) this.gridMove[i* AntCalculator.WIDTH+k] / (getRULESET().length() - 1);
					gcm.setFill(new Color(colorValue, colorValue, colorValue, 1));
					gcm.fillRect(i * AntCalculator.CELLSIZE, k * AntCalculator.CELLSIZE, AntCalculator.CELLSIZE, AntCalculator.CELLSIZE);
				}
			}
		}
	}

	private void renderCanvasMoveBorder(GraphicsContext gcm) {
		gcm.setStroke(Color.DARKGOLDENROD);
		gcm.setLineWidth(5);
		gcm.strokeRect(0, 0, AntCalculator.WIDTH * AntCalculator.CELLSIZE, AntCalculator.HEIGHT * AntCalculator.CELLSIZE);
	}

	private void nationalStruggle() {

		int collision = 0;
		int[] whichAnt = new int[this.getAnts().size()];
		int size = this.getAnts().size();

		for (int i = 0; i < size; i++) {
			for (int k = 0; k < size; k++) {
				if (i == k)
					continue;
				if (this.getAnts().get(i).getX() == this.getAnts().get(k).getX() && this.getAnts().get(i).getY() == this.getAnts().get(k).getY()) {
					whichAnt[collision] = k;
					collision++;
				}
			}
			if (collision >= 1 && collision <= this.getAnts().get(i).getTribe().getBirthrate()) {
				this.getAnts().get(i).Birth(this.getAnts(), this.getAnts().get(i), this.getAnts().get(whichAnt[0]));
			}
			if (collision > this.getAnts().get(i).getTribe().getSickness()) {
				this.getAnts().get(i).localDeath(this.getAnts(), collision, whichAnt);
				size -= collision;
			}
			collision = 0;
			whichAnt = new int[this.getAnts().size()];
		}

		for (int i = 0; i < this.getAnts().size(); i++) {
			this.getAnts().get(i).setBirth(false);
		}
	}

	void suicide(){
		while(getAnts().size()>0){
			getAnts().get(getAnts().size()-1).antDeath(getAnts().size()-1, getAnts());
		}
	}

	//Setter and Getter
	public String getName(){
		return name;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public String getRULESET() {
		return RULESET;
	}

	public void setRULESET(String rULESET) {
		RULESET = rULESET;
	}

	public ArrayList<Ant> getAnts() {
		return ants;
	}

	public void setAnts(ArrayList<Ant> ants) {
		this.ants = ants;
	}

	public int getSickness() {
		return sickness;
	}

	public void setSickness(int sickness) {
		this.sickness = sickness;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getBirthrate() {
		return birthrate;
	}

	public void setBirthrate(int birthrate) {
		this.birthrate = birthrate;
	}
}
