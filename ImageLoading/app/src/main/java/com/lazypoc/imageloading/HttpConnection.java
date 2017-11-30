package com.lazypoc.imageloading;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Avinash on 3/14/2016.
 */

public class HttpConnection {

    private static final String TAG = "Http Connection";


    private String[] blogTitles;
    HttpUrlConnectionResponce mHttpUrlConnectionResponce = null;
    Context mContext;

    public HttpConnection(HttpUrlConnectionResponce connectionResponce, Context context) {
        mHttpUrlConnectionResponce = connectionResponce;
        mContext = context;
    }
//            new AsyncHttpTask().execute(url);

    public int getHttpGetConnection(String url) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        int statusCode = 0;
        try {
                /* forming th java.net.URL object */
            URL urlConnection = new URL(url);
            httpURLConnection = (HttpURLConnection) urlConnection.openConnection();

                /* optional request header */
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
                /* optional request header */
            httpURLConnection.setRequestProperty("Accept", "application/json");

                /* setting http connection time out */
            httpURLConnection.setConnectTimeout(60000);
                /* for Get request */
            httpURLConnection.setRequestMethod("GET");
            statusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, " getHttpGetConnection statusCode=" + statusCode);
                /* 200 represents HTTP OK */
            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                String response = convertInputStreamToString(inputStream);
                mHttpUrlConnectionResponce.httpResponceResult(response, statusCode);
            }

        } catch (Exception e) {
            Log.d(TAG, " getHttpGetConnection error=" + e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return statusCode; //"Failed to fetch data!";
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    public interface HttpUrlConnectionResponce {
        public void httpResponceResult(String resultData, int statusCode);
    }
}




