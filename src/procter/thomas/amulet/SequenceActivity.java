package procter.thomas.amulet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
		ArrayAdapter<String> adapter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.placeholder);
		arrayPos = 0;
	    
		
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

					float newTime = (currentTime.toMillis(false) - startTime
							.toMillis(false)) / 1000;

					endClause((int) newTime);
				}

			}
		});
	}

	private void endClause(int endTime){
		//intent results
		
		Intent intent = new Intent(this, ResultsActivity.class);
		intent.putExtra("mode",getIntent().getStringExtra("mode"));
		intent.putExtra("score", endTime);
		intent.putExtra("task", "Sequence");
		intent.putExtra("unit", "s");
		startActivity(intent);
	}
	
}
