package sample.java2d.game1;

public enum Direction {
	up, right, left, down;

	public Direction turnLeft(){
		switch(this){
		case up:
			return left;
		case left:
			return down;
		case down:
			return right;
		case right:
			return up;
		default:
			throw new IllegalStateException("The direction was not supported!");
		}
	}


	public Direction turnRight(){
		switch(this){
		case up:
			return right;
		case left:
			return up;
		case down:
			return left;
		case right:
			return down;
		default:
			throw new IllegalStateException("The direction was not supported!");
		}
	}

	public Direction turnBack(){
		switch(this){
		case up:
			return down;
		case left:
			return right;
		case down:
			return up;
		case right:
			return left;
		default:
			throw new IllegalStateException("The direction was not supported!");
		}
	}

}