package com.example.msiqlab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class qlab_laboratory_page extends AppCompatActivity {
    String img;
    ImageView imageView;
    private RelativeLayout mLoadingBar;
ArrayList<qlab_laboratory_map> arrayList =new ArrayList<>();
    mylistview mylistview ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_laboratory_page);
        mLoadingBar = findViewById(R.id.loading_bar_rl);
        mylistview =findViewById(R.id.qlab_laboratory_mylv);

        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        TextView updata =findViewById(R.id.updata_but_tv);
        if(!UserData.WorkID.equals("10018042")){
            updata.setVisibility(View.GONE);
        }
        final Bundle bundle = this.getIntent().getExtras();
        final String title = bundle.getString("title");
        final String con = bundle.getString("con").replaceAll("<br>", "\n");
        final String masterid = bundle.getString("masterid");
        img = bundle.getString("img");
        TextView title_tv = findViewById(R.id.title_tv);
        title_tv.setText(title);
        TextView synopsis_tv = findViewById(R.id.synopsis_tv);
        synopsis_tv.setText(con);
        imageView = findViewById(R.id.img);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getBitmapFromURL(img);

                runOnUiThread(new Runnable() {
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
        String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_LabCenter_Detail?F_Master_ID=" + masterid;
        qlab_laboratory_page.qlab_Find_LabCenter_Detail getNetworkJson = new qlab_laboratory_page.qlab_Find_LabCenter_Detail();
        getNetworkJson.execute(url);

        updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qlab_laboratory_page.this,qlab_laboratory_page_updata.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("title",title);
                bundle1.putString("con",con);
                bundle1.putString("img",img);
                bundle1.putString("masterid",masterid);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }

    public class qlab_Find_LabCenter_Detail extends AsyncTask<String, Void, String> {
        String data = "";

        int total = 0;
        String title;
        String con;
        @Override
        protected String doInBackground(String... urlStrings) {
            try {
                arrayList.clear();
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
                total = UserArray.length();
                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject IssueData = UserArray.getJSONObject(i);
                    title = IssueData.getString("F_IntroColumn");
                    con = IssueData.getString("F_IntroContent");
                    con = con.replaceAll("<h3>", "");
                    con = con.replaceAll("</h3>", "");
                    con = con.replaceAll("<p>", "");
                    con = con.replaceAll("</p>", "");
                    con = con.replaceAll("<p class=\"cert-items\">", "");
                    con = con.replaceAll(" ", "");
                    con = con.replaceAll("&#160;", "");
                    con = con.replaceAll("<br>", "\n");
                    con = con.replaceAll("<br/>", "\n");
                    con = con.replaceAll(";", ";\n");
                    con = con.replaceAll("。", "。\n");
                    con = con.replaceAll("　", "\n");
                    if(con.equals("null")){
                        con="\n";
                    }
                    arrayList.add(new qlab_laboratory_map(title,con));

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
            qlab_laboratory_Adapter adapter =new qlab_laboratory_Adapter(qlab_laboratory_page.this,arrayList);
            mylistview.setAdapter(adapter);
            mLoadingBar.setVisibility(View.GONE);//gone loading

        }
    }

    private static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //map
    public class qlab_laboratory_map{
        //大標題
        private String tital;
        //內容
        private String conn;
        //照片



        //建構式
        public qlab_laboratory_map(String totital, String toconn  ) {
            tital = totital;
            conn = toconn;
        }
        public String gettital() {
            return tital;
        }
        public String getconn() {
            return conn;
        }

    }

    //adapter
    public class qlab_laboratory_Adapter extends ArrayAdapter<qlab_laboratory_map> {
        private final Activity context;

        //建構式
        public qlab_laboratory_Adapter(Activity context, ArrayList<qlab_laboratory_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_laboratory_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final qlab_laboratory_map value = getItem(position);
            //找到list_item.xml中的TextView()
            TextView tital = listItemView.findViewById(R.id.qlab_lab_tital);
            tital.setText(value.gettital());

            TextView conn = listItemView.findViewById(R.id.qlab_lab_content);
            conn.setText(value.getconn());
            return listItemView;
        }
    }




}
