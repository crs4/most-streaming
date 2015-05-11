package org.crs4.most.streaming.enums;

public enum PTZ_Direction {
	
	STOP(0, 0),
	UP(0, 1),
    DOWN(0, -1),
    RIGHT(1, 0),
    LEFT(-1, 0),

    UP_RIGHT(1, 1),
    UP_LEFT (-1, 1),
    DOWN_RIGHT(1, -1),
    DOWN_LEFT (-1, -1);
	

	private int x_dir = 0;
	private int y_dir = 0;
	
	private PTZ_Direction(int x_dir, int y_dir)
		{
		this.x_dir = x_dir;
		this.y_dir = y_dir;
		}
	
	public int getX() {return this.x_dir;}
	public int getY() {return this.y_dir;}
}
