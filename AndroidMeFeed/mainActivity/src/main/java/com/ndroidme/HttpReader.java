package com.ndroidme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpReader {

    public HttpReader() {
        // TODO Auto-generated constructor stub
    }

    public boolean verifyConnection(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        HttpURLConnection conn = null;
        String contentAsString = null;
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setDoInput(true);

            // Starts the
            int response = conn.getResponseCode();
            Log.d("HttpReader", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            contentAsString = readIt(is);


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            if (is != null) {
                is.close();
            }
        }
        return contentAsString;
    }

    private String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        InputStreamReader reader;
        reader = new InputStreamReader(stream, "UTF-8");

        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];

        StringBuilder webString = new StringBuilder();
        while (true) {

            int readBytes = reader.read(buffer, 0, bufferSize);

                if (readBytes > 0) {
                    webString.append(buffer, 0, readBytes);

                }
                else
                {
                    break;
                }





        }
        reader.close();
        Log.d("HTTP_READER", "Done with getting String");
       return webString.toString().trim();

    }

}

