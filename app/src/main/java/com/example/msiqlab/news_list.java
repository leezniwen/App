package com.example.msiqlab;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

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

public class news_list extends AppCompatActivity {
    public static ArrayAdapter adapter;
    public static int total;
    public static ArrayList news_no = new ArrayList();
    public static ListView lv;
    private RelativeLayout mLoadingBar;

    private String findnews = "";

     EditText findnews_et;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        Bundle bundle = this.getIntent().getExtras();
        String key = bundle.getString("key");
          findnews_et = findViewById(R.id.findnews_et);
        findnews_et.setText(key);
        findnews = findnews_et.getText().toString();
        mLoadingBar = findViewById(R.id.news_list_loading_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_News";
        news_list_value getNetworkJson = new news_list_value();
        getNetworkJson.execute(url);
        Button button = findViewById(R.id.findnews_but);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findnews = findnews_et.getText().toString();
                mLoadingBar.setVisibility(View.VISIBLE);//show loading
                String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_News";
                news_list_value getNetworkJson = new news_list_value();
                getNetworkJson.execute(url);
            }
        });

    }

    public class news_list_value extends AsyncTask<String, Void, String> {
        String data = "";
        String news_title = "";
        String news_time = "";
        String news_value = "00";
        ArrayList str = new ArrayList();

        @Override
        protected String doInBackground(String... urlStrings) {

            try {
                news_no.clear();
                str.clear();
                URL url = new URL(urlStrings[0]); //初始化
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection(); //取得連線之物件
                HttpsTrustManager.allowAllSSL();//信任憑證

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
                for (int i = 0; i < UserArray.length(); i++) {

                    JSONObject IssueData = UserArray.getJSONObject(i);
                    news_title = IssueData.getString("News_title");
                    news_time = IssueData.getString("Column1");
                    news_value = String.valueOf(IssueData.getInt("News_No"));

                    if (news_title.contains(findnews)) {
                        news_no.add(news_value);
                        str.add(news_time + "\n" + news_title);
                    }else if (news_value.contains(findnews)) {
                        news_no.add(news_value);
                        str.add(news_time + "\n" + news_title);
                    }
                }
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

        protected void onPostExecute(String s) {
            super.onPostExecute(data);
            mLoadingBar.setVisibility(View.GONE);//gone loading
            findnews_et.setText("");
            adapter = new ArrayAdapter(news_list.this, android.R.layout.simple_list_item_1);
            total = str.size();
            for (int i = 0; i < total; i++) {
                adapter.add(str.get(i));
            }
            if (total != 0) {
                lv = findViewById(R.id.news_list_lv);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(onClickListView);
            } else {
                adapter.add("沒有相關新聞");
                lv = findViewById(R.id.news_list_lv);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }

        }
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(news_list.this, news_page.class);
            String str = (String) news_no.get(position);
            Bundle bun = new Bundle();
            bun.putString("news_no", str);
            intent.putExtras(bun);
            news_list.this.startActivity(intent);
        }
    };
}



