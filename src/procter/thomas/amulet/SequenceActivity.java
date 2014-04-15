package procter.thomas.amulet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class SequenceActivity extends Activity{
		private int arrayPos;
		private String[] numbers = {"1", "2", "3","4","5","6","7","8","9", "10", "11", "12","13","14","15","16"};
		private Time startTime;
		private long timePaused = 0;
		private Time pauseTime;
		ArrayAdapter<String> adapter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.placeholder);
		arrayPos = 0;
		pauseTime = new Time();
        pauseTime.setToNow();
		
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
	
	
	
	
	public void startButton(View view) {
		setContentView(R.layout.activity_sequence);
		startTime = new Time();
		startTime.setToNow();

		GridView gridview = (GridView) findViewById(R.id.gridview);

		ArrayList<String> arrayList = new ArrayList<String>(
				Arrays.asList(numbers));
		Collections.shuffle(arrayList);
		
		adapter = new ArrayAdapter<String>(this, R.layout.sequence_text_view,
				arrayList);
		
		gridview.setAdapter(adapter);

		
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				TextView item = (TextView) v;
				if (item.getText().equals(numbers[arrayPos])) {
					arrayPos++;
					item.setBackgroundColor(256);

				}
				if ((arrayPos) == numbers.length) {
					Time currentTime = new Time();
					currentTime.setToNow();

					long newTime = (currentTime.toMillis(false) - startTime
							.toMillis(false));

					endClause(newTime);
				}

			}
		});
	}

	private void endClause(long endTime){
		//intent results
		int finalTime = (int) ((endTime - timePaused)/1000);
		Intent intent = new Intent(this, ResultsActivity.class);
		intent.putExtra("mode",getIntent().getStringExtra("mode"));
		intent.putExtra("score", finalTime);
		intent.putExtra("task", "Sequence");
		intent.putExtra("unit", "s");
		startActivity(intent);
	}
	
}
