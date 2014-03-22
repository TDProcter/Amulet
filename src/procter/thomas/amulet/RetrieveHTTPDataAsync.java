package procter.thomas.amulet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
		
		return dataString;
	}

	
	@Override
	protected String doInBackground(String... urls) {
		// TODO Auto-generated method stub
		
		String responseData = null;
		if (urls.length > 0) 
		{
			responseData = getHTTPData(urls[0]);
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
