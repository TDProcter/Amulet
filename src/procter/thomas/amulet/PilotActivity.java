package procter.thomas.amulet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PilotActivity extends Activity{
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set our MainGamePanel as the View
        
        setContentView(new PilotSurfaceView(this));
        Log.d("TAG", "View added");
    }

	@Override
	protected void onDestroy() {
		Log.d("TAG", "Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d("TAG", "Stopping...");
		super.onStop();
	}
	
	public void finish(int newTime){
		Intent intent = new Intent(this, ResultsActivity.class);
		intent.putExtra("mode",getIntent().getStringExtra("mode"));
		intent.putExtra("score", newTime);
		intent.putExtra("task", "Pilot");
		intent.putExtra("unit", "s");
		startActivity(intent);
	}
}
