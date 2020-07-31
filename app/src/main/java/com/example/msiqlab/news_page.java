package com.example.msiqlab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class news_page extends AppCompatActivity {
    public static TextView news_text;
    public static TextView news_title;
    public static TextView news_time;
    public static ImageView news_img;
    public static TextView news_source;
    public static String news_no;
    public static TextView user_n;
    public static boolean updata = false;
    private RelativeLayout mLoadingBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_page);
        Bundle bun = this.getIntent().getExtras();
        news_no = bun.getString("news_no");
        mLoadingBar = findViewById(R.id.news_page_loading_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        String url ="http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_News";
        news_page_value getNetworkJson = new news_page_value();
        getNetworkJson.execute(url);
        news_text = findViewById(R.id.news_text_tv);
        news_title = findViewById(R.id.news_title_tv);
        news_time = findViewById(R.id.news_time_tv);
        news_img =findViewById(R.id.news_page_img);
        news_source = findViewById(R.id.news_source_tv);
        String url2 ="http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_Board?News_No="+news_no;
        news_page_message_value getNetworkJson2 = new news_page_message_value();
        getNetworkJson2.execute(url2);
        user_n = findViewById(R.id.news_message_name_tv);
        user_n.setText(" "+UserData.Name+" ");

        View ll = findViewById(R.id.news_message_ll);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(news_page.this, news_message.class);
                Bundle bundle = new Bundle();
                String str = news_no;
                bundle.putString("news_no",str);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    public class news_page_value extends AsyncTask<String, Void, String> {
        String data="";
        String news_title ;
        String news_time ;
        String news_value;
        String news_con;
        String news_no;
        String news_source;
        String news_img;
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
                news_no = news_page.news_no;
                JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));
                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject IssueData = UserArray.getJSONObject(i);
                    news_value = String.valueOf(IssueData.getInt("News_No"));
                    if(news_value.equals(news_no)){
                        news_title = IssueData.getString("News_title");
                        news_time = IssueData.getString("Column1");
                        news_con = IssueData.getString("News_content");
                        news_source = IssueData.getString("News_source");
                        news_img = IssueData.getString("News_Image");
                        break;
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
            news_page.news_title.setText(news_title);
            news_page.news_text.setText(news_con);
            news_page.news_time.setText(news_time);
            news_page.news_source.setText(news_source);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap IMG_bitmap = getBitmapUrl(news_img);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            news_page.news_img = findViewById(R.id.news_page_img);
                            news_page.news_img.setImageBitmap(IMG_bitmap);
                        }
                    });
                }
            }).start();
        }
        public  Bitmap getBitmapUrl(String src){
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                Bitmap IMG_bitmap = BitmapFactory.decodeStream(is);
                return IMG_bitmap;
            }
            catch (IOException e){
                e.printStackTrace();
                news_page.news_img = findViewById(R.id.news_page_img);
                news_page.news_img.setVisibility(View.GONE);
            }
            return null;
        }
    }
    public class news_page_message_value extends AsyncTask<String, Void, String> {
        String data="";
        String name = "";
        String con ="";
        String time = "";
        int total;
        ArrayList<message_map> alist = new ArrayList<message_map>();
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
                total = UserArray.length();
                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject IssueData = UserArray.getJSONObject(i);

                    name = IssueData.getString("board_person");
                    con = IssueData.getString("board_content");
                    time =IssueData.getString("board_time");
                    alist.add(new message_map(name,time,con));
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
            myAdapter adapter = new myAdapter(news_page.this,alist);
            ListView lv = findViewById(R.id.news_board_lv);
            lv.setAdapter(adapter);
            TextView textView = findViewById(R.id.news_nomessage_tv);
            if(total == 0){
                textView.setText("目前沒有人留言喔~\n趕快留下你的心得吧~");
            }else {
                textView.setText("留言板");
            }
        }
    }
    public void onPause(){
        super.onPause();
        updata = true;
    }
    protected void onResume(){
        super.onResume();
        if(updata){
            String url2 ="http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_Board?News_No="+news_no;
            news_page_message_value getNetworkJson2 = new news_page_message_value();
            getNetworkJson2.execute(url2);
        }
        updata =false;
    }
}
