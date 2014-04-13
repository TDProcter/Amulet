package procter.thomas.amulet;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

class PilotThread extends Thread {
	
	private SurfaceHolder surfaceHolder;
	private PilotSurfaceView surfaceView;
	private boolean running;
	
	public PilotThread(SurfaceHolder surfaceHolder, PilotSurfaceView surfaceView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.surfaceView = surfaceView;
	}

	// flag to hold game state
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		super.run();
		Canvas canvas;
		while (running) {
			canvas = null;
			try{
				canvas = surfaceHolder.lockCanvas(null);
				synchronized(surfaceHolder){
					if(canvas == null){
						running = false;
						Thread.sleep(20);
					}
					else if(surfaceView != null){
						surfaceView.onUpdate(canvas);
						surfaceView.onDraw(canvas);
					}
				}
			}
			catch(Exception e){
				
			}
			finally{
				if(canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			
		}
		
	}
	
}