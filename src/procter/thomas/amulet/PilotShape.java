package procter.thomas.amulet;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class PilotShape {
	Paint colour;
	Rect position;
	Point centre;
	int width;
	int height;
	Point direction;

	public PilotShape(int centreX, int centreY, int newWidth, int newHeight, Paint paint, Point newDirection){
		Rect tempRect = calculateRect(centreX, centreY, newWidth, newHeight);
		this.position = new Rect(tempRect.left, tempRect.top, tempRect.right, tempRect.bottom);
		this.colour = paint;
		this.centre = new Point(centreX, centreY);
		this.width= newWidth;
		this.height = newHeight;
		this.direction = newDirection;
	}
	
	public void setDirection(Point newDir){
		this.direction = newDir;
	}
	
	public Point getDirection(){
		return this.direction;
	}
	
	public Point getPosition(){
		return this.centre;
	}
	
	public void setPosition(int centreX, int centreY){
		Rect newPos = calculateRect(centreX, centreY, this.width, this.height);
		this.position.left =newPos.left;
		this.position.top = newPos.top;
		this.position.right = newPos.right;
		this.position.bottom = newPos.bottom;
		this.centre = new Point(centreX, centreY);
	}
	
	
	
	private Rect calculateRect(int centreX, int centreY, int newWidth, int newHeight){
		
		int halfWidth = newWidth/2;
		int halfHeight = newHeight/2;
		Rect returnRect = new Rect((centreX - halfWidth), (centreY - halfHeight), (centreX + halfWidth), (centreY + halfHeight));
		
		return returnRect;
	}
	
	public Rect getRect(){
		return this.position;
	}
	
	
}
