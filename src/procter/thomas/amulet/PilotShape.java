package procter.thomas.amulet;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class PilotShape {
	Paint colour;
	Rect position;
	Point centre;
	int size;
	Point direction;

	public PilotShape(int centreX, int centreY, int newSize, Paint paint, Point newDirection){
		Rect tempRect = calculateRect(centreX, centreY, newSize);
		this.position = new Rect(tempRect.left, tempRect.top, tempRect.right, tempRect.bottom);
		this.colour = paint;
		this.centre = new Point(centreX, centreY);
		this.size= newSize;
		this.direction = newDirection;
	}
	
	public void setPosition(int centreX, int centreY){
		Rect newPos = calculateRect(centreX, centreY, this.size);
		this.position.left =newPos.left;
		this.position.top = newPos.top;
		this.position.right = newPos.right;
		this.position.bottom = newPos.bottom;
		this.centre = new Point(centreX, centreY);
	}
	
	
	
	private Rect calculateRect(int centreX, int centreY, int size){
		
		int halfSize = size/2;
		Rect returnRect = new Rect((centreX - halfSize), (centreY - halfSize), (centreX + halfSize), (centreY + halfSize));
		
		return returnRect;
	}
	
	public Rect getRect(){
		return this.position;
	}
	
	
}
