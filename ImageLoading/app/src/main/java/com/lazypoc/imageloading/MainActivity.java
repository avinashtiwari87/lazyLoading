package com.lazypoc.imageloading;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lazypoc.imageloading.modle.ImageDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HttpConnection.HttpUrlConnectionResponce {
    private ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private HttpConnection mHttpConnection;
    private String BaseURL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
    private String mTitle;
    ArrayList<ImageDetails> mImageList;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHttpConnection = new HttpConnection((HttpConnection.HttpUrlConnectionResponce) this, this);
        initViews();
        pllToRefresh();
    }

    /**
     * This method is used to refresh the data on pull down the view
     */
    private void pllToRefresh() {
        //https://guides.codepath.com/android/Implementing-Pull-to-Refresh-Guide
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpConnectionTask httpConnectionTask = new HttpConnectionTask(MainActivity.this);
                httpConnectionTask.execute(BaseURL);
                Toast.makeText(MainActivity.this, "Image List Refreshed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        HttpConnectionTask httpConnectionTask = new HttpConnectionTask(MainActivity.this);
        httpConnectionTask.execute(BaseURL);

    }

    /**
     * @param resultData json response with image url,title and description
     * @return list of image details
     */
    private ArrayList<ImageDetails> prepareData(String resultData) {
        ArrayList<ImageDetails> imageDetailsArrayList = new ArrayList<>();
        JSONArray imageList;
        JSONObject imageData = null;
        JSONObject pageDetails = null;
        try {
            pageDetails = new JSONObject(resultData);
            mTitle = pageDetails.getString("title");
            //getActionBar().setTitle(""+mTitle);
            //getSupportActionBar().setTitle(""+mTitle);
            imageList = pageDetails.getJSONArray("rows");
            int size = imageList.length();
            for (int i = 0; i < size; i++) {
                ImageDetails imageDetails = new ImageDetails();
                imageData = imageList.getJSONObject(i);
                imageDetails.setDescription(imageData.getString("description"));
                imageDetails.setTitle(imageData.getString("title"));
                imageDetails.setImagePath(replaceWithHttps(imageData.getString("imageHref")));
                imageDetailsArrayList.add(imageDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageDetailsArrayList;
    }

    private String replaceWithHttps(String imageHref) {
        return "https"+imageHref.substring(4,imageHref.length());
    }

    @Override
    public void httpResponceResult(String resultData, int statusCode) {
        mImageList = prepareData(resultData);

    }


    private class HttpConnectionTask extends AsyncTask<String, Void, Integer> {
        Context mContext = null;
        HttpConnection mHttpConnection = null;
        private ProgressDialog mDialog;
        private String mMethod;
        private JSONObject mJsonDATA;

        public HttpConnectionTask(Context context) {
            mContext = context;
            mHttpConnection = new HttpConnection((HttpConnection.HttpUrlConnectionResponce) mContext, mContext);
            mDialog = new ProgressDialog( mContext);
        }

        @Override
        protected void onPreExecute() {
            mDialog.setMessage("Data Syncing...");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected Integer doInBackground(String... params) {
            int statusCode = 0;
            statusCode = mHttpConnection.getHttpGetConnection(params[0]);
            return statusCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mDialog.dismiss();
            DataAdapter adapter = new DataAdapter(getApplicationContext(), mImageList);
            mRecyclerView.setAdapter(adapter);
        }
    }

}
