package com.example.msiqlab;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;

public class qlab_booking_last extends AppCompatActivity {
    String F_Master_ID,F_Desc,F_StartDate,F_EndDate,F_PM_ID,F_Is_Restrict,F_Keyin,F_Facility;

    private LinearLayout list_ll;
    private TextView last_title;
    private RelativeLayout mLoadingBar;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_booking_last);
        mLoadingBar = findViewById(R.id.news_list_loading_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        Bundle bundle =this.getIntent().getExtras();
        F_Master_ID = bundle.getString("F_Master_ID");
        F_Desc = bundle.getString("F_Desc");
        F_StartDate = bundle.getString("F_StartDate");
        F_EndDate = bundle.getString("F_EndDate");
        F_PM_ID = bundle.getString("F_PM_ID");
        F_Is_Restrict = bundle.getString("F_Is_Restrict");
        F_Facility =bundle.getString("F_Facility");
        F_Keyin=UserData.Name;

        TextView pm_id = findViewById(R.id.last_PM_ID);
        TextView facility = findViewById(R.id.last_facility);
        TextView sdate = findViewById(R.id.last_startdate_tv);
        TextView edate = findViewById(R.id.last_enddate_tv);
        TextView con = findViewById(R.id.last_con_tv);
        list_ll =findViewById(R.id.last_list_ll);
        last_title=findViewById(R.id.last_title);

        pm_id.setText(F_PM_ID);
        facility.setText(F_Facility);
        sdate.setText(F_StartDate);
        edate.setText(F_EndDate);
        con.setText(F_Desc);

        String ur1 = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_Fac_Schedule?F_Keyin=" + F_Keyin+"&F_Master_ID="+F_Master_ID+"&F_Subject="+""+
                "&F_Desc="+F_Desc+"&F_StartDate="+F_StartDate+"&F_EndDate="+F_EndDate+"&F_PM_ID"+F_PM_ID+"@F_Is_Restrict"+F_Is_Restrict;
        qlab_booking_last.Insert_Fac_Schedule getBookingJson = new qlab_booking_last.Insert_Fac_Schedule();
        getBookingJson.execute(ur1);

        TextView ok_tv = findViewById(R.id.last_ok_tv);
        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public class Insert_Fac_Schedule extends AsyncTask<String, Void, String> {
        String data = "";
        @SuppressLint("WrongThread")
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

            } catch (InputMismatchException e) {
                e.printStackTrace();
                list_ll.setVisibility(View.GONE);
                last_title.setText("預約失敗");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                list_ll.setVisibility(View.GONE);
                last_title.setText("預約失敗");
            } catch (IOException e) {
                e.printStackTrace();
                list_ll.setVisibility(View.GONE);
                last_title.setText("預約失敗");
            }
            return data;
        }
        protected void onPostExecute(String s) {
            super.onPostExecute(data);
            mLoadingBar.setVisibility(View.GONE);//gone loading
        }
    }

}
