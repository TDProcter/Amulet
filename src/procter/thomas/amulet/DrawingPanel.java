package procter.thomas.amulet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback{

	private MainThread thread;
	private PilotShape pilotPlayer;
	private PilotShape[] badGuys;
	boolean touchedSquare = false;
	public DrawingPanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		Paint playerColour = new Paint();
		playerColour.setColor(0xffff0000);
		pilotPlayer = new PilotShape((this.getWidth()/2), (this.getHeight()/2), 200, playerColour, null);
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
		canvas.drawRect(pilotPlayer.getRect(), pilotPlayer.colour);
		
	}
	
	public void onUpdate(Canvas canvas){
		
	}

}
