package procter.thomas.amulet;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewDrinkDiaryActivity extends Activity {

	private Cursor drinkDiaryCursor;
	SimpleCursorAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_drink_diary);
		setupList();
	}
	
	@Override
	protected void onStop() {

		super.onStop();
		drinkDiaryCursor.close();
	}

	private void setupList() {

		ContentResolver cr = getContentResolver();
		drinkDiaryCursor = StorageMethods.getDrinkDiaryComplete(cr,
				AmuletContentProvider.KEY_DIARY_TIMESTAMP_COLUMN);
		if (drinkDiaryCursor.getCount() > 0) {

			// The desired columns to be bound
			String[] columns = new String[] {
					AmuletContentProvider.KEY_DIARY_TIMESTAMP_COLUMN,
					AmuletContentProvider.KEY_DIARY_DRINKTYPE_COLUMN,
					AmuletContentProvider.KEY_DIARY_UNITSCONSUMED_COLUMN };

			// the XML defined views which the data will be bound to
			int[] to = new int[] { 
					R.id.lblDryLstTimeStamp,
					R.id.lblDryLstDrinkType, 
					R.id.lblDryLstUnitsConsumed };

			// create the adapter using the cursor pointing to the desired data
			// as well as the layout information
			dataAdapter = new SimpleCursorAdapter(this,
					R.layout.drink_diary_list_view, drinkDiaryCursor, columns,
					to, 0);

			ListView listView = (ListView) findViewById(R.id.lstDrinkDiary);
			// Assign adapter to ListView
			listView.setAdapter(dataAdapter);

		}
	}

}
