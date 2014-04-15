package procter.thomas.amulet;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

class PilotThread extends Thread {
	
	private SurfaceHolder surfaceHolder;
	private PilotSurfaceView surfaceView;
	private boolean running;
	private boolean declare;
	
	public PilotThread(SurfaceHolder surfaceHolder, PilotSurfaceView surfaceView, boolean declare) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.surfaceView = surfaceView;
		this.declare = declare;
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
		while(declare){
			Log.i("test", "magic");
			canvas = null;
			try{
				canvas = surfaceHolder.lockCanvas(null);
				synchronized(surfaceHolder){
					if(canvas == null){
						running = false;
						Thread.sleep(20);
					}
					else if(surfaceView != null){
						surfaceView.setObjects(canvas);//called once
						declare = false;
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