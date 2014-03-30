package procter.thomas.amulet;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback{

	
	private MainThread thread;
	private PilotShape pilotPlayer;
	private PilotShape[] badGuys;
	private final int noOfBadGuys = 40;
	private boolean touchedSquare = false;
	private Time startTime, endTime;
	
	public DrawingPanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		Paint playerColour = new Paint();
		playerColour.setColor(0xffffffff);
		pilotPlayer = new PilotShape((this.getWidth()/2), (this.getHeight()/2), 200, 200, playerColour, null);
		badGuys = new PilotShape[noOfBadGuys];
		Paint enemyColour = new Paint();
		enemyColour.setColor(0xffffff00);
		for(int i = 0; i < badGuys.length; i++){
			int dirX;
			int dirY;
			do{
			Random random = new Random();
			dirX = (random.nextInt(2)-1);
			random = new Random();
			dirY = (random.nextInt(2)-1);
			}while(dirX == 0 && dirY == 0);
			badGuys[i] = new PilotShape((i+2)*100, (i+2)*100, 20, 30, enemyColour, new Point(dirX, dirY));
			
			
		}
		startTime = new Time();
		endTime = new Time();
		startTime.setToNow();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			if(pilotPlayer.getRect().contains((int)event.getX(), (int)event.getY())){
				touchedSquare = true;
			}
			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			touchedSquare = false;
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(touchedSquare){
				pilotPlayer.setPosition((int)event.getX(), (int)event.getY());
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRGB(43, 223, 255);
		
		for(int i = 0; i < badGuys.length; i++){
			canvas.drawRect(badGuys[i].getRect(), badGuys[i].colour);
		}
		canvas.drawRect(pilotPlayer.getRect(), pilotPlayer.colour);
		
	}
	
	public void onUpdate(Canvas canvas){
		for(int i = 0; i < badGuys.length; i++){
			Point dir = badGuys[i].getDirection();
			badGuys[i].setPosition(badGuys[i].centre.x + dir.x, badGuys[i].centre.y + dir.y);
			
			if(badGuys[i].getRect().left < 0 || badGuys[i].getRect().right > canvas.getWidth()){
				badGuys[i].setDirection(new Point(-dir.x, dir.y));
			}
			if(badGuys[i].getRect().top < 0 || badGuys[i].getRect().bottom > canvas.getHeight()){
				badGuys[i].setDirection(new Point(dir.x, -dir.y));
			}
			Rect goodGuyRect = pilotPlayer.getRect();
			Rect badGuyRect = badGuys[i].getRect();
			if(Rect.intersects(goodGuyRect, badGuyRect)){
				pilotPlayer.colour.setColor(0xffff0000);
				finish();
			}
		}
	}
	
	private void finish(){
		
		endTime.setToNow();
		int newTime = (int)(endTime.toMillis(false) - startTime.toMillis(false))/1000;
		Log.i("Time", String.valueOf(newTime));
		Intent intent = new Intent(super.getContext(), ResultsActivity.class);
		intent.putExtra("score", newTime);
		intent.putExtra("task", "Pilot");
		intent.putExtra("unit", "s");
		super.getContext().startActivity(intent);
	}
	

}
