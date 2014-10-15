package com.ndroidme;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpReader {
    
	public HttpReader() {
		// TODO Auto-generated constructor stub
	}
    public boolean verifyConnection(Context context)
    {
    	ConnectivityManager connMgr = 
    			(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) {
    	        return true;
    	    } else {
    	        return false;
    	    	
    	    }
    }
    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        
        
        String contentAsString=null;    
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("PlacesTest", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            contentAsString = readIt(is);
//            if (is != null) {
//                is.close();
//            }
            
            
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
           
            
        } finally {
            if (is != null) {
                is.close();
            } 
        }
        return contentAsString;
    }
	private String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
		    Reader reader = null;
		    reader = new InputStreamReader(stream, "UTF-8");
		    
		    int result=500;
		    StringBuilder webString= new StringBuilder();
		    while(true)
		    {
		    	int buffer=-41;
		    	 buffer=reader.read();
		    	 if(buffer==-1)
		    	 {
		    		 break;
		    	 
		    	 }
		    	 webString.append((char)buffer);
		    	
		    }
		    
		    return webString.toString();
		    
		}

}

