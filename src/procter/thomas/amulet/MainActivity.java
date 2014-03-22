package procter.thomas.amulet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void createAccountButton(View view){
		Intent intent = new Intent(this, CreateAccountActivity.class);
		startActivity(intent);
	}
	
	public void loginButton(View view){
		Intent intent = new Intent(this, MenuActivity.class);////test only
		startActivity(intent);
	}
}
