package procter.thomas.amulet;

import java.security.SecureRandom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PilotSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	
	public PilotThread thread;
	private PilotShape pilotPlayer;
	private PilotShape[] badGuys;
	private final int noOfBadGuys = 6;
	private boolean touchedSquare = false;
	private Time startTime, endTime;
	private int badGuySize = 500; //Perimeter divided by 2
	private int playerSize = 200; //Perimeter divided by 2
	private boolean stopOnTouch = false;
	private int screenWidth;
	private int screenHeight;
	private boolean declare;
	
	public PilotSurfaceView(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		declare = true;
		// create the game loop thread
		thread = new PilotThread(getHolder(), this, declare);
		
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
		
		if (!thread.isAlive()) {
            thread = new PilotThread(getHolder(), this, declare);
        }
		thread.setRunning(true);
		thread.start();
		declare = false;
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
	
	public void setObjects(Canvas canvas) {
		PilotActivity activity = (PilotActivity) super.getContext();
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		screenHeight = canvas.getHeight();
		screenWidth = canvas.getWidth();
		
		
		playerSize = screenWidth/6;
		badGuySize = (playerSize*5)/2;
		Paint playerColour = new Paint();
		playerColour.setColor(0xffffffff);
		pilotPlayer = new PilotShape((screenWidth - playerSize),
				(screenHeight - playerSize), playerSize, playerSize, playerColour, null);
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
			
			do {
				SecureRandom random = new SecureRandom();
				height = random.nextInt(badGuySize) + 1;
				width = badGuySize-height;
			} while (height > screenHeight || width > screenWidth);
			//Log.i("size", height + ",  " + width);
			badGuys[i] = new PilotShape((badGuySize)+1, (badGuySize)+1, width, height,
					enemyColour, new Point(dirX*speed, dirY*speed));

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
				Point p = pilotPlayer.getPosition();
				pilotPlayer.setPosition((int)event.getX(), (int)event.getY());
				if (pilotPlayer.getRect().left < 0
						|| pilotPlayer.getRect().right > screenWidth) {
					pilotPlayer.setPosition(p.x, pilotPlayer.getPosition().y);
				}
				if (pilotPlayer.getRect().top < 0
						|| pilotPlayer.getRect().bottom > screenHeight) {
					pilotPlayer.setPosition(pilotPlayer.getPosition().x, p.y);
				}
				
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

			for (int i = 0; i < badGuys.length; i++) {
				Point dir = badGuys[i].getDirection();
				Point lastPos = badGuys[i].getPosition();
				badGuys[i].setPosition(badGuys[i].centre.x + dir.x,
						badGuys[i].centre.y + dir.y);

				if (badGuys[i].getRect().left < 0
						|| badGuys[i].getRect().right > canvas.getWidth()) {
					badGuys[i].setPosition(lastPos.x, lastPos.y);
					badGuys[i].setDirection(new Point(-dir.x, dir.y));
				}
				if (badGuys[i].getRect().top < 0
						|| badGuys[i].getRect().bottom > canvas.getHeight()) {
					badGuys[i].setPosition(lastPos.x, lastPos.y);
					badGuys[i].setDirection(new Point(dir.x, -dir.y));
				}
				Rect goodGuyRect = pilotPlayer.getRect();
				Rect badGuyRect = badGuys[i].getRect();
				if (Rect.intersects(goodGuyRect, badGuyRect)) {
					pilotPlayer.colour.setColor(0xffff0000);
					endCondition();
				}
			}
		
	}
	
	private void endCondition(){
		stopOnTouch = true;
		endTime.setToNow();
		long newTime = (endTime.toMillis(false) - startTime.toMillis(false));
		PilotActivity activity = (PilotActivity) super.getContext();//context;
		activity.endCondition(newTime);
		//
	}
	

}
