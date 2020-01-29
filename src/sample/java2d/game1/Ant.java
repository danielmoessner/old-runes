package sample.java2d.game1;

import java.util.ArrayList;
import java.util.Random;

public class Ant {

	private Direction dir = Direction.left;
	private int[] gridMove;
	private Tribe tribe;

	private int x;
	private int y;
	private boolean birth;


	public Ant(Tribe tribe, int x, int y, int[] gridMove) {
		this.gridMove = gridMove;

		this.setX(x);
		this.setY(y);

		this.setTribe(tribe);
	}


	public void update(int pos) {
		if (getTribe().getRULESET().charAt(gridMove[AntCalculator.WIDTH*getX()+getY()]) == 'R') {
			dir = dir.turnRight();
		} else if (getTribe().getRULESET().charAt(gridMove[AntCalculator.WIDTH*getX()+getY()]) == 'L') {
			dir = dir.turnLeft();
		} else if (getTribe().getRULESET().charAt(gridMove[AntCalculator.WIDTH*getX()+getY()]) == 'B') {
			dir = dir.turnBack();
		} else if (getTribe().getRULESET().charAt(gridMove[AntCalculator.WIDTH*getX()+getY()]) == 'F') {
			// DO NOTHING!
		} else {
			throw new IllegalStateException();
		}

		gridMove[AntCalculator.WIDTH*getX()+getY()]++;
		if (gridMove[AntCalculator.WIDTH*getX()+getY()] >= getTribe().getRULESET().length()) {
			gridMove[AntCalculator.WIDTH*getX()+getY()] = 0;
		}

		if (AntCalculator.gridLook[AntCalculator.WIDTH*getX()+getY()] == 0) {
			// Ameise erobert unbekannte Zelle
			AntCalculator.gridLook[AntCalculator.WIDTH*getX()+getY()] = pos * 10 + 1;
		} else if (AntCalculator.gridLook[AntCalculator.WIDTH*getX()+getY()] > pos * 10 && AntCalculator.gridLook[AntCalculator.WIDTH*getX()+getY()] <= 10 * (pos + 1)) {
			// Ameise l�uft erneut �ber erobertes Gebiet
			if (AntCalculator.gridLook[AntCalculator.WIDTH*getX()+getY()] != 10 * (pos + 1)) {
				// Ameise verf�rbt diese Zelle bis zum Maximum
				AntCalculator.gridLook[AntCalculator.WIDTH*getX()+getY()]++;
			}
		} else {
			// Ameise �bernimmt Zelle eines anderen Stammes
			AntCalculator.gridLook[AntCalculator.WIDTH*getX()+getY()] = pos * 10 + 1;
		}

		moveForward();
	}

	private void moveForward() {
		switch (dir) {
		case left:
			setX(getX() - 1);
			break;
		case right:
			setX(getX() + 1);
			break;
		case up:
			setY(getY() - 1);
			break;
		case down:
			setY(getY() + 1);
			break;
		}

		if (getX() < 0) {
			// antDeath(this, ants);
			setX(AntCalculator.WIDTH - 1);
		}
		if (getX() >= AntCalculator.WIDTH) {
			// antDeath(this, ants);
			setX(0);
		}

		if (getY() < 0) {
			// antDeath(this, ants);
			setY(AntCalculator.HEIGHT - 1);
		}
		if (getY() >= AntCalculator.HEIGHT) {
			// antDeath(this, ants);
			setY(0);
		}
	}

	void Birth(ArrayList<Ant> ants, Ant antMan, Ant antWoman) {
		if (antMan.isBirth() == false && antWoman.isBirth() == false)
			ants.add(new Ant(antMan.getTribe(), antMan.getX(), antMan.getY(), antMan.gridMove));
		antMan.setBirth(true);
		antWoman.setBirth(true);
	}

	void localDeath(ArrayList<Ant> ants, int collision, int[] whichAnt) {
		for (int i = collision - 1; i >= 0; i--) {
			antDeath(whichAnt[i], ants);
		}
	}

	void antDeath(Ant ant, ArrayList<Ant> ants) {
		ants.remove(ant);
	}

	void antDeath(int number, ArrayList<Ant> ants) {
		ants.remove(number);
	}



















	@SuppressWarnings("unused")
	private void randomRuleset() {
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < 32; i++) {
			int val = r.nextInt(4);

			switch (val) {
			case 0:
				sb.append('L');
				break;
			case 1:
				sb.append('R');
				break;
			case 2:
				sb.append('B');
				break;
			case 3:
				sb.append('F');
				break;
			}
		}
		getTribe().setRULESET(sb.toString());
		// RULESET = "FBRRRRLFRLBBFBFFRBLBBLLFRBFBRFFR";
		// RULESET = "LLRR";
	}


	public Tribe getTribe() {
		return tribe;
	}


	public void setTribe(Tribe tribe) {
		this.tribe = tribe;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public boolean isBirth() {
		return birth;
	}


	public void setBirth(boolean birth) {
		this.birth = birth;
	}
}