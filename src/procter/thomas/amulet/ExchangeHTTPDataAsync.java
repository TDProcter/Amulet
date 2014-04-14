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

import procter.thomas.amulet.OnExchangeHTTPData.OnExchangeHttpData;

import android.content.ContentResolver;
import android.os.AsyncTask;
import android.util.Log;

public class ExchangeHTTPDataAsync extends AsyncTask<String, Void,  String>
{
	private OnExchangeHttpData listener;
	private ContentResolver contentResolver;
    
	public ExchangeHTTPDataAsync(OnExchangeHttpData listener, ContentResolver cr){
		this.listener = listener;
		this.contentResolver = cr;
	}
	public ExchangeHTTPDataAsync(OnExchangeHttpData listener){
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
		    Log.i("statusCode", statusCode +"");
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
		Log.i("get data string", builder.toString());
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
	    Log.i("post data string", builder.toString());
		return dataString;
	}

	public void updateTaskTable(String responseData, String httpData){
		
		if(responseData.contains("tasks received")){
		StorageMethods meth = new StorageMethods();
		meth.updateSyncedTasks(contentResolver, httpData);
		}
	}
	
public boolean updateDiaryTable(String responseData, String httpData){
		
		if(responseData.contains("entries received")){
		StorageMethods meth = new StorageMethods();
		return meth.updateSyncedDiaryEntries(contentResolver, httpData);
		}
		return false;
	}
	
	@Override
	protected String doInBackground(String... dataStrings) {
		
		String responseData = null;
		if (dataStrings.length > 0) 
		{
			if(dataStrings[0].toString().equals("GET&SAVETASK")){
				responseData = getHTTPData(dataStrings[1]);
				StorageMethods meth = new StorageMethods();
				meth.syncTasksFromServer(contentResolver, responseData);
			}
			else if(dataStrings[0].toString().equals("GET&SAVEDIARY")){
				responseData = getHTTPData(dataStrings[1]);
				StorageMethods meth = new StorageMethods();
				meth.syncDiaryFromServer(contentResolver, responseData);
				
			}
			else if(dataStrings[0].toString().equals("GET")){
				responseData = getHTTPData(dataStrings[1]);
			}
			else if(dataStrings[0].toString().equals("POST&UPDATETASK") && dataStrings.length > 2){
				responseData = postHTTPData(dataStrings[1], dataStrings[2]);
				updateTaskTable(responseData, dataStrings[2]);
			}
			else if(dataStrings[0].toString().equals("POST&UPDATEDIARY") && dataStrings.length > 2){
				responseData = postHTTPData(dataStrings[1], dataStrings[2]);
				updateDiaryTable(responseData, dataStrings[2]);
				
			}
			else if(dataStrings[0].toString().equals("POST") && dataStrings.length > 2){
				responseData = postHTTPData(dataStrings[1], dataStrings[2]);
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
