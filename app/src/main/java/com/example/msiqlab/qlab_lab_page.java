package com.example.msiqlab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class qlab_lab_page extends AppCompatActivity {
    private RelativeLayout mLoadingBar;
    private String seqNo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_lab_page);
        mLoadingBar = findViewById(R.id.qlab_loading_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        Bundle bun = this.getIntent().getExtras();
        String seqNo = bun.getString("device_no");
        String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_Detail?F_SeqNo=" + seqNo;
        qlab_lab_page.device_value getNetworkJson = new qlab_lab_page.device_value();
        getNetworkJson.execute(url);
    }

    public class device_value extends AsyncTask<String, Void, String> {
        String data = "";
        String device_basic = "";
        String  device_url= "";
        String device_Cost = "";
        String device_Standard = "";
        String device_canuse = "";
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
                JSONObject IssueData = UserArray.getJSONObject(0);
                device_basic = "類別:  " + IssueData.getString("F_Type") + "\n" +
                        "名稱:     " + IssueData.getString("F_Facility") + "\n" +
                        "財編:     " + IssueData.getString("F_AssetNo") + "\n" +
                        "設備型號: " + IssueData.getString("F_Model") + "\n" +
                        "存放位置: " + IssueData.getString("F_Location") + "\n" +
                        "保管單位: " + IssueData.getString("F_Dept") + "\n" +
                        "保管人員: " + IssueData.getString("F_Owner") + "\n";
                alist.add(new message_map("","",device_basic));
                device_url = "http:"+IssueData.getString("IMG");
                alist.add(new message_map("規格","\n",IssueData.getString("F_Spec")));
                seqNo = IssueData.getString("F_SeqNo");
                String buy_time =IssueData.getString("F_Buy_Date");
                String F_Buy_Date ="";
                for(int i =0;i<buy_time.indexOf("T");i++){
                    char c = buy_time.charAt(i);
                    F_Buy_Date += c;
                }
                String Storage_time =IssueData.getString("F_Storage_Date");
                String F_Storage_Date ="";
                for(int i =0;i<Storage_time.indexOf("T");i++){
                    char c = Storage_time.charAt(i);
                    F_Storage_Date += c;
                }
                device_Cost = "購入費用: "+IssueData.getString("F_Cost")+" NTD\n"+
                        "購買日期: "+F_Buy_Date+"\n"+
                        "入庫日期: "+F_Storage_Date+"\n"+
                        "折舊年限: "+IssueData.getString("F_Use_Year")+"年\n"+
                        "設備廠商: "+IssueData.getString("F_Factory")+"\n";
                alist.add(new message_map("費用/年限","\n",device_Cost));
                device_Standard = IssueData.getString("F_Standard");
                alist.add(new message_map("規範","\n",device_Standard));
                if (IssueData.getString("F_Status").equals("1")) {
                    device_canuse = "預約";
                } else {
                    device_canuse = "未開放";
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
            myAdapter adapter = new myAdapter(qlab_lab_page.this,alist);
            ListView lv = findViewById(R.id.lab_page_lv);
            lv.setAdapter(adapter);
            Button button =findViewById(R.id.lab_page_booking_but);
            button.setText(device_canuse);
            if (device_canuse.equals("預約")) {
                button.setBackgroundResource(R.drawable.red);
                button.setOnClickListener(onchick);
            } else if (device_canuse.equals("未開放")) {
                button.setBackgroundResource(R.drawable.ash);

            }
            qlab_lab_page.getimg getNetworkJson = new qlab_lab_page.getimg();
            getNetworkJson.execute(device_url);
        }
    }
    private View.OnClickListener onchick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(qlab_lab_page.this,qlab_booking_device.class);
            Bundle bundle = new Bundle();
            bundle.putString("seqNo",seqNo);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        }
    };
    public class getimg extends AsyncTask<String, Void, String> {
        String data = "";
        Bitmap bitmap;
        @Override
        protected String doInBackground(String... urlStrings) {
            try {
                URL url = new URL(urlStrings[0]); //初始化
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection(); //取得連線之物件
                InputStream inputStream = httpURLConnection.getInputStream();
                //對取得的資料進行讀取
                bitmap= BitmapFactory.decodeStream(inputStream);
            } catch (InputMismatchException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        protected void onPostExecute(String s) {
            super.onPostExecute(data);
            ImageView imageView = findViewById(R.id.lab_page_img);
            imageView.setImageBitmap(bitmap);
            mLoadingBar.setVisibility(View.GONE);//show loading

        }
    }
}
