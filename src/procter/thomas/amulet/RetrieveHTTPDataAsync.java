package procter.thomas.amulet;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.os.AsyncTask;
import android.util.Log;

public class RetrieveHTTPDataAsync extends AsyncTask<String, Void,  String>
{
	private OnRetrieveHttpData listener;
	
	public RetrieveHTTPDataAsync(OnRetrieveHttpData listener){
		this.listener = listener;
	}
	public String getHTTPData(String url){
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(httpGet);
		    StatusLine statusLine = response.getStatusLine();
		    int statusCode = statusLine.getStatusCode();
		    if (statusCode == 200) {
		    	HttpEntity entity = response.getEntity();
		       	InputStream content = entity.getContent();
		       	BufferedReader reader = 
					new BufferedReader(new InputStreamReader(content));
		       	String line;
		       	while ((line = reader.readLine()) != null) {
		       	  builder.append(line);
		       	}
		   	} 
		}
		catch (Exception e){
			Log.i("MyActivity", "exception in get HTTPData");
		}
		
		String dataString =  builder.toString();
		Log.i("data string", builder.toString());
		return dataString;
	}
	
	public String postHTTPData(String url, String postString){
		StringBuilder builder = new StringBuilder();
		
		try{
			
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		

		
		StringEntity httpString = new StringEntity(postString);
		
		httpPost.setEntity(httpString);
		
		httpPost.setHeader("Content-type", "application/json");
		
		HttpResponse response = client.execute(httpPost);
		StatusLine statusLine = response.getStatusLine();
	    int statusCode = statusLine.getStatusCode();
	    Log.i("statusCode", statusCode +"");
	    if (statusCode == 202) {
	    	HttpEntity entity = response.getEntity();
	       	InputStream content = entity.getContent();
	       	BufferedReader reader = 
				new BufferedReader(new InputStreamReader(content));
	       	String line;
	       	while ((line = reader.readLine()) != null) {
	       	  builder.append(line);
	       	}
	   	} 
		}
		catch(Exception e){
			Log.i("post error", "string entity error");
		}
		
	    String dataString =  builder.toString();
	    Log.i("data string", builder.toString());
		return dataString;
	}

	
	@Override
	protected String doInBackground(String... urls) {
		
		String responseData = null;
		if (urls.length > 0) 
		{
			
			if(urls[0].toString().equals("GET")){
				
			responseData = getHTTPData(urls[1]);
			}
			else if(urls[0].equals("POST") && urls.length > 2){

				responseData = postHTTPData(urls[1], urls[2]);
			}
		}
		return responseData;

	}
	
	@Override
	protected void onPostExecute(String httpData){
		listener.onTaskCompleted(httpData);
	}
	
	public interface OnRetrieveDataCompleted
	{
		void onTaskCompleted(String responseData);
	}



}
