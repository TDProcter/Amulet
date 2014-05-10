package procter.thomas.amulet;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String>{
Context context;
int resource;
int textSize;
ArrayList<String> objects;
	public CustomArrayAdapter(Context context, int resource,
			ArrayList<String> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.objects = objects;
		this.textSize = 54;//textSize;
		
	}
	/*
	@Override  
	public View getView(int position, View view, ViewGroup viewGroup)
	{
	 //View v = super.getView(position, view, viewGroup);
	// ((TextView)view).setTextSize(54);;
	 return view;
	}
*/
	
	@SuppressLint("NewApi")
	@Override  
	public View getView(int position, View view, ViewGroup viewGroup)
	{
		SequenceActivity activity = (SequenceActivity) super.getContext();
		//DisplayMetrics metrics = new DisplayMetrics();
		//activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.i("screen", height+"");
	 View v = super.getView(position, view, viewGroup);
	 
	 if(width>height){
		 ((TextView)v).setTextSize(width/20);
	 }
	 else{
		 
		 ((TextView)v).setTextSize(height/20);
	 }
	 
	 return v;
	}
	
}
