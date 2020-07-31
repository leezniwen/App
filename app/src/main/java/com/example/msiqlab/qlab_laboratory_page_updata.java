package com.example.msiqlab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

public class qlab_laboratory_page_updata extends AppCompatActivity {
    String img;
    ImageView imageView;
    private RelativeLayout mLoadingBar;

    EditText synopsis_et;
    EditText test_et;
    EditText cost_et;
    EditText equip_et;
    EditText organ_et;
    EditText record_et;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_laboratory_page_updata);
        mLoadingBar = findViewById(R.id.loading_bar_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading

        Bundle bundle = this.getIntent().getExtras();
        String title = bundle.getString("title");
        String con = bundle.getString("con");
        img = bundle.getString("img");
        String masterid = bundle.getString("masterid");
        TextView title_tv = findViewById(R.id.title_tv);
        title_tv.setText(title);
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
        qlab_laboratory_page_updata.qlab_Find_LabCenter_Detail getNetworkJson = new qlab_laboratory_page_updata.qlab_Find_LabCenter_Detail();
        getNetworkJson.execute(url);
        synopsis_et = findViewById(R.id.synopsis_et);
        test_et = findViewById(R.id.test_et);
        cost_et = findViewById(R.id.cost_et);
        equip_et = findViewById(R.id.equip_et);
        organ_et = findViewById(R.id.organ_et);
        record_et = findViewById(R.id.record_et);
        synopsis_et.setText(con);
        LinearLayout ll = findViewById(R.id.ll);//Relative lay out  登入頁面
        ll.setOnTouchListener(new View.OnTouchListener() {  //Relative lay out  點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(synopsis_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(test_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(cost_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(equip_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(organ_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(record_et.getWindowToken(), 0);

                return false;
            }
        });
        ScrollView sv = findViewById(R.id.sv);
        sv.setOnTouchListener(new View.OnTouchListener() {  //Relative lay out  點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(synopsis_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(test_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(cost_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(equip_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(organ_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(record_et.getWindowToken(), 0);

                return false;
            }
        });

        TextView updata = findViewById(R.id.updata_but_tv);
        updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(qlab_laboratory_page_updata.this)
                        .setTitle("確定更新?")
                        .setNegativeButton("取消", null)
                        .setNeutralButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create()
                        .show();
            }
        });
    }

    public class qlab_Find_LabCenter_Detail extends AsyncTask<String, Void, String> {
        String data = "";

        int total = 0;
        String title;
        String con;
        ArrayList<String> item_arr = new ArrayList<>();
        ArrayList<String> con_arr = new ArrayList<>();

        @Override
        protected String doInBackground(String... urlStrings) {
            try {
                item_arr.clear();
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
                    con = con.replaceAll("<br>", "\n");
                    con = con.replaceAll("<br/>", "\n");
                    con = con.replaceAll(";", ";\n");
                    con = con.replaceAll("。", "。\n");
                    if (con.equals("null")) {
                        con = "\n";
                    }

                    con_arr.add(con + "\n");
                    String item = IssueData.getString("F_IntroColumn");
                    item = item.replaceAll("\r", "");
                    item = item.replaceAll("\n", "");
                    item_arr.add(item);
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
            for (int i = 0; i < total; i++) {
                if (item_arr.get(i).contains("測試目的")) {
                    test_et.setText(con_arr.get(i));
                }
                if (item_arr.get(i).contains("外測費用")) {
                    cost_et.setText(con_arr.get(i));
                }
                if (item_arr.get(i).contains("測試儀器設備")) {
                    equip_et.setText(con_arr.get(i));
                }
                if (item_arr.get(i).contains("實驗室組織")) {
                    organ_et.setText(con_arr.get(i));
                }
                if (item_arr.get(i).contains("歷史紀錄/大事記")) {
                    record_et.setText(con_arr.get(i));
                }
            }
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
}
