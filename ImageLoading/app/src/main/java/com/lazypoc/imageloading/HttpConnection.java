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
    public HttpConnection(HttpUrlConnectionResponce connectionResponce,Context context) {
        mHttpUrlConnectionResponce = connectionResponce;
        mContext=context;
        Log.d(TAG, " HttpConnection");
    }
//            new AsyncHttpTask().execute(url);

    public int getHttpGetConnection(String url) {
        InputStream inputStream = null;
        Log.d(TAG, " getHttpGetConnection 1");
        HttpURLConnection httpURLConnection = null;
        int statusCode = 0;
        try {
                /* forming th java.net.URL object */
            URL urlConnection = new URL(url);
            Log.d(TAG, " getHttpGetConnection 2");
            httpURLConnection = (HttpURLConnection) urlConnection.openConnection();

                /* optional request header */
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            Log.d(TAG, " getHttpGetConnection 3");
                /* optional request header */
            httpURLConnection.setRequestProperty("Accept", "application/json");

                /* setting http connection time out */
            httpURLConnection.setConnectTimeout(60000);
            Log.d(TAG, " getHttpGetConnection 4");
                /* for Get request */
            httpURLConnection.setRequestMethod("GET");
            Log.d(TAG, " getting status....");
            statusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, " getHttpGetConnection statusCode=" + statusCode);
                /* 200 represents HTTP OK */
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, " getHttpGetConnection 6");
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                Log.d(TAG, " getHttpGetConnection 7");
                String response = convertInputStreamToString(inputStream);
                Log.d(TAG, " getHttpGetConnection 8");
                mHttpUrlConnectionResponce.httpResponceResult(response, statusCode);
                Log.d(TAG, " getHttpGetConnection 9");
            }

        } catch (Exception e) {
            Log.d(TAG, " getHttpGetConnection catch 5 e=" + e.getMessage());
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
//        LocalJsonReader localJsonReader=new LocalJsonReader(mContext);
//        mHttpUrlConnectionResponce.httpResponceResult(localJsonReader.loadJSONFromAsset(), 200);
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


//    private int httpPostConnection(String url, JSONObject JsonDATA) {
//        BufferedReader reader = null;
//        String JsonResponse = null;
//        HttpsURLConnection conn = null;
//        int statusCode = 0;
//        try {
//            URL urlConnection = new URL(url);
//            conn = (HttpsURLConnection) urlConnection.openConnection();
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Accept", "application/json");
//            //set headers and method
//            Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
//            writer.write(JsonDATA.toString());
//            // json data
//            writer.close();
//            InputStream inputStream = conn.getInputStream();
//            //input stream
//            StringBuffer buffer = new StringBuffer();
////            if (inputStream == null) {
////                // Nothing to do.
////                return null;
////            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String inputLine;
//            while ((inputLine = reader.readLine()) != null)
//                buffer.append(inputLine + "\n");
////            if (buffer.length() == 0) {
////                // Stream was empty. No point in parsing.
////                return null;
////            }
//            JsonResponse = buffer.toString();
////response data
////            Log.i(TAG, JsonResponse);
////            try {
//////send to post execute
////                return JsonResponse;
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////            return null;
//
//            statusCode = conn.getResponseCode();
//            mHttpUrlConnectionResponce.httpResponceResult(JsonResponse, statusCode);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(TAG, "Error closing stream", e);
//                }
//            }
//        }
//        return statusCode;
//    }

    public interface HttpUrlConnectionResponce {
        public void httpResponceResult(String resultData, int statusCode);
    }
}




