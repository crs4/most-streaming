package org.crs4.most.streaming.enums;

public enum PTZ_Zoom {
	
	IN(1),
	STOP(0),
	OUT(-1);
   
	private int val = 0;
	
	
	private PTZ_Zoom(int val)
		{
		this.val = val;
		
		}
	
	public int intValue() {return this.val;};
}
