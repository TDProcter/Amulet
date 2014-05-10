package procter.thomas.amulet;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String>{
Context context;
int resource;

ArrayList<String> objects;
	public CustomArrayAdapter(Context context, int resource,
			ArrayList<String> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.objects = objects;
		
		
	}
	
	@Override  
	public View getView(int position, View view, ViewGroup viewGroup)
	{
		SequenceActivity activity = (SequenceActivity) super.getContext();
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.i("screen", height+"");
	 View v = super.getView(position, view, viewGroup);
	 
	 if(width>height){
		 
		
		 ((TextView)v).setTextSize(TypedValue.COMPLEX_UNIT_SP,height/20);
	 }
	 else{
		 ((TextView)v).setTextSize(TypedValue.COMPLEX_UNIT_SP, width/20);
		 
	 }
	 
	 return v;
	}
	
}
