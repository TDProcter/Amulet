package procter.thomas.amulet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class PilotActivity extends Activity{
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set our MainGamePanel as the View
        setContentView(new DrawingPanel(this));
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
}
