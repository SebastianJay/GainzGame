package edu.virginia.cs.runner;

public class Point {

	int x, y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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
	
	public Point copy() {
		return new Point(x, y);
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public boolean equals(Object o) {
		if(o instanceof Point) {
			Point p = (Point) o;
			return p.getX() == x && p.getY() == y;
		}
		
		return false;
	}
}
