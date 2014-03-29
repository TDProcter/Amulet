package procter.thomas.amulet;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

class MainThread extends Thread {
	
	private SurfaceHolder surfaceHolder;
	private DrawingPanel gamePanel;
	private boolean running;
	
	public MainThread(SurfaceHolder surfaceHolder, DrawingPanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
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
					else if(gamePanel != null){
						gamePanel.onDraw(canvas);
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