package com.example.msiqlab;

import android.os.AsyncTask;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;

public  class littlesupper_calender extends AsyncTask<String ,Void , String>{
    String data = "";

    @Override
    protected String doInBackground(String... urlStrings) {
        try {

            URL url = new URL(urlStrings[0]); //初始化
            HttpURLConnection httpURLConnection =
                    (HttpURLConnection) url.openConnection(); //取得連線之物件
            InputStream inputStream = httpURLConnection.getInputStream();
            //對取得的資料進行讀取
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));
            JSONObject IssueData = UserArray.getJSONObject(0);
            String F_Is_Restrict=IssueData.getString("F_Is_Restrict");

        } catch (InputMismatchException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(data);

    }

}



