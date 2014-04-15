package procter.thomas.amulet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

public class PilotActivity extends Activity{
	
	PilotSurfaceView pilotSurface;
	private long timePaused = 0;
	private Time pauseTime;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pauseTime = new Time();
        pauseTime.setToNow();
        // set our MainGamePanel as the View
        getActionBar().hide();
        pilotSurface = new PilotSurfaceView(this);
        setContentView(pilotSurface);
        Log.d("TAG", "View added");
    }

	@Override
	protected void onDestroy() {
		Log.d("TAG", "Destroying...");
		super.onDestroy();
	}

	@Override 
	protected void onPause(){
		super.onPause();
		Log.i("paused", "paused");
		pauseTime.setToNow();
		Log.i("onpause", timePaused+"");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Time resumeTime = new Time();
		resumeTime.setToNow();
		timePaused = timePaused + resumeTime.toMillis(false) - pauseTime.toMillis(false);
		Log.i("resume pause", timePaused+"");
	}
	@Override
	protected void onStop() {
		Log.d("TAG", "Stopping...");
		super.onStop();
	}
	
	public void endCondition(long newTime){
		Log.i("final pause", timePaused+"");
		int finalTime = (int) ((newTime - timePaused)/1000);
		Intent intent = new Intent(this, ResultsActivity.class);
		intent.putExtra("mode",getIntent().getStringExtra("mode"));
		intent.putExtra("score", finalTime);
		intent.putExtra("task", "Pilot");
		intent.putExtra("unit", "s");
		startActivity(intent);
	}
}
