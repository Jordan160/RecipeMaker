package com.jvetter2.recipemaker.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeList extends AsyncTask<String, String, String> {
    String result;

    public RecipeList() {}

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            result = total.toString();

            return  result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//
//        try {
//            JSONObject jsonObject = new JSONObject(s);
////                String movieTitle = jsonObject.getString("title");
////                Log.i("Movie Title", movieTitle);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.i("JSON", s);
//    }
//    public String downloadContent(String url) {
//        DownloadTask task = new DownloadTask();
//        try {
//            result = task.execute(url).get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}