package procter.thomas.amulet;

import java.security.SecureRandom;

import android.app.Activity;
import android.content.Context;
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
	private final int noOfBadGuys = 6;
	private boolean touchedSquare = false;
	private Time startTime, endTime;
	private boolean setObjects = true;
	private int badGuySize = 500; //Perimeter divided by 2
	private int playerSize = 200; //Perimeter divided by 2
	private boolean stopOnTouch = false;
	public DrawingPanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
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
	
	private void setObjects(Canvas canvas) {
		Paint playerColour = new Paint();
		playerColour.setColor(0xffffffff);
		pilotPlayer = new PilotShape((canvas.getWidth() - playerSize),
				(canvas.getHeight() - playerSize), playerSize, playerSize, playerColour, null);
		badGuys = new PilotShape[noOfBadGuys];
		Paint enemyColour = new Paint();
		enemyColour.setColor(0xffffff00);
		for (int i = 0; i < badGuys.length; i++) {
			int dirX;
			int dirY;
			int speed = i+1;
			int height;
			int width;
			do {
				SecureRandom random = new SecureRandom();
				dirX = (random.nextInt(3) - 1);
				dirY = (random.nextInt(3) - 1);
			} while (dirX == 0 || dirY == 0);
			Log.i("dir", dirX + ",  " + dirY);
			do {
				SecureRandom random = new SecureRandom();
				height = 50* (random.nextInt(10) + 1);
				width = 50 * (random.nextInt(10) + 1);
			} while (height+width != badGuySize);
			Log.i("size", height + ",  " + width);
			badGuys[i] = new PilotShape((badGuySize)+1, (badGuySize)+1, width, height,
					enemyColour, new Point(dirX*speed, dirY*speed));

			this.setObjects = false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!stopOnTouch){
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
	
	
	public void onUpdate(Canvas canvas) {
		
			if (setObjects) {
				setObjects(canvas);
			}

			for (int i = 0; i < badGuys.length; i++) {
				Point dir = badGuys[i].getDirection();
				badGuys[i].setPosition(badGuys[i].centre.x + dir.x,
						badGuys[i].centre.y + dir.y);

				if (badGuys[i].getRect().left < 0
						|| badGuys[i].getRect().right > canvas.getWidth()) {
					badGuys[i].setDirection(new Point(-dir.x, dir.y));
				}
				if (badGuys[i].getRect().top < 0
						|| badGuys[i].getRect().bottom > canvas.getHeight()) {
					badGuys[i].setDirection(new Point(dir.x, -dir.y));
				}
				Rect goodGuyRect = pilotPlayer.getRect();
				Rect badGuyRect = badGuys[i].getRect();
				if (Rect.intersects(goodGuyRect, badGuyRect)) {
					pilotPlayer.colour.setColor(0xffff0000);
					finish();
				}
			}
		
	}
	
	private void finish(){
		stopOnTouch = true;
		endTime.setToNow();
		int newTime = (int)(endTime.toMillis(false) - startTime.toMillis(false))/1000;
		PilotActivity activity = (PilotActivity) super.getContext();//context;
		activity.finish(newTime);
		//
	}
	

}
