package com.example.msiqlab;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;

public class qlab_inquire_Fragment extends Fragment {
    ListView inquire_device_lv;
    ListView inquire_cert_lv;
    ArrayList<inguire_map> inguire_list = new ArrayList();
    ArrayList<inguire_cert_map> inguire_cert_list = new ArrayList();
    TextView hasdata;
    private RelativeLayout mLoadingBar;
    int finish = 0;
    public static TabLayout tab;
    int todayint;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.qlab_inquire, container, false);
        finish = 0;
        inquire_device_lv = view.findViewById(R.id.inquire_device_lv);
        inquire_cert_lv = view.findViewById(R.id.inquire_cert_lv);
        hasdata = view.findViewById(R.id.hasdata_tv);
        tab = view.findViewById(R.id.qlab_inquire_tabl);
        qlab_inquire_Fragment.tab.addTab(qlab_inquire_Fragment.tab.newTab().setText("設備"));
        qlab_inquire_Fragment.tab.addTab(qlab_inquire_Fragment.tab.newTab().setText("認證"));

        Calendar mCal = Calendar.getInstance();
        String dateformat = "yyyyMMdd";
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        String today = df.format(mCal.getTime());
        todayint =Integer.valueOf(today);

        String workID = UserData.WorkID;
        mLoadingBar = view.findViewById(R.id.loading_bar_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_My_Schedule_List?F_Keyin=" + workID;
        qlab_inquire_Fragment.inquire_myschedule getdeviceJson = new qlab_inquire_Fragment.inquire_myschedule();
        getdeviceJson.execute(url);
        final String finalWorkID = workID;
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                //選擇時觸發
                String position = String.valueOf(tab.getPosition());
                mLoadingBar.setVisibility(View.VISIBLE);//gone loading
                if (position.equals("0")) {
                    mLoadingBar = view.findViewById(R.id.loading_bar_rl);
                    mLoadingBar.setVisibility(View.VISIBLE);//show loading
                    String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_My_Schedule_List?F_Keyin=" + finalWorkID;
                    qlab_inquire_Fragment.inquire_myschedule getdeviceJson = new qlab_inquire_Fragment.inquire_myschedule();
                    getdeviceJson.execute(url);
                    inquire_device_lv.setVisibility(View.VISIBLE);
                    inquire_cert_lv.setVisibility(View.GONE);
                } else if (position.equals("1")) {
                    String urll = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Certification_Inquire?WorkID=" + finalWorkID;
                    qlab_inquire_Fragment.inquire_cert getcertJson = new qlab_inquire_Fragment.inquire_cert();
                    getcertJson.execute(urll);
                    inquire_device_lv.setVisibility(View.GONE);
                    inquire_cert_lv.setVisibility(View.VISIBLE);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
                //未選擇時觸發
            }

            public void onTabReselected(TabLayout.Tab tab) {
                //選中之後再次點擊即複選時觸發
            }
        });

        return view;
    }

    public class inquire_myschedule extends AsyncTask<String, Void, String> {
        String data = "";
        int total = 0;
        String title;
        String location;
        String time;

        @Override
        protected String doInBackground(String... urlStrings) {
            inguire_list.clear();
            try {
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
                total = 0;
                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject IssueData = UserArray.getJSONObject(i);
                    String string =IssueData.getString("F_EndDate");
                    string =string.replaceAll("-","");
                    string =string.substring(0,8);
                    int et = Integer.valueOf(string);

                    if(et>=todayint){
                    total++;

                    title = IssueData.getString("F_Facility")
                            + "\n財編: " + IssueData.getString("F_AssetNo");
                    location = "存放位置: " + IssueData.getString("F_Location");
                    time = "預約時間: " + IssueData.getString("F_StartDate") + "\n至" + IssueData.getString("F_EndDate");
                    time = time.replaceAll("T", " ");
                    inguire_list.add(new inguire_map(title, location, time, "刪除", IssueData.getString("F_SeqNo")));
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
            inquire_Adapter adapter = new inquire_Adapter(getActivity(), inguire_list);
            if (total > 1) {
                hasdata.setVisibility(View.GONE);
                inquire_device_lv.setAdapter(adapter);
            } else {
                hasdata.setVisibility(View.VISIBLE);
            }
            mLoadingBar.setVisibility(View.GONE);//gone loading
        }
    }

    public class inquire_cert extends AsyncTask<String, Void, String> {
        String data = "";
        String model;
        String respuser;
        String createdate;
        String status;
        String logo;

        int total = 0;

        @Override
        protected String doInBackground(String... urlStrings) {
            inguire_cert_list.clear();
            try {
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
                total = 0;
                for (int i = 0; i < UserArray.length(); i++) {
                    total++;
                    JSONObject IssueData = UserArray.getJSONObject(i);
                    model = "專案: " + IssueData.getString("Model");
                    logo = "項目: " + IssueData.getString("F_Cer_Logo");
                    respuser = IssueData.getString("F_RespUser");
                    String time = IssueData.getString("F_CreateDate");
                    time = time.replaceAll("T", " ");
                    createdate = "申請時間: " + time;
                    status = IssueData.getString("Status");
                    inguire_cert_list.add(new inguire_cert_map(model, logo, respuser, createdate, status));
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
            inquire_cert_Adapter adapter = new inquire_cert_Adapter(getActivity(), inguire_cert_list);
            if (total > 1) {
                hasdata.setVisibility(View.GONE);
                inquire_cert_lv.setAdapter(adapter);
            } else {
                hasdata.setVisibility(View.VISIBLE);
            }
            mLoadingBar.setVisibility(View.GONE);//gone loading

        }
    }

    public class inguire_map {
        //標題
        private String title;
        //地點
        private String location;
        //時間
        private String time;
        //值
        private String value;
        //
        private String seqNo;

        //建構式
        public inguire_map(String totitle, String tolocation, String totime, String tovalue, String toseqNo) {

            title = totitle;
            location = tolocation;
            time = totime;
            value = tovalue;
            seqNo = toseqNo;
        }

        public String gettitle() {
            return title;
        }

        public String getlocation() {
            return location;
        }

        public String gettime() {
            return time;
        }

        public String getbut_value() {
            return value;
        }

        public String getseqNo() {
            return seqNo;
        }

    }

    public class inguire_cert_map {
        //
        private String model;

        private String logo;
        //
        private String createdate;
        //
        private String respuser;
        //
        private String status;

        //建構式
        public inguire_cert_map(String tomodel, String tologo, String torespuser, String tocreatedate, String tostatus) {
            this.model = tomodel;
            this.logo = tologo;
            this.respuser = torespuser;
            this.createdate = tocreatedate;
            this.status = tostatus;
        }

        public String getmodel() {
            return model;
        }

        public String getlogo() {
            return logo;
        }

        public String getrespuser() {
            return respuser;
        }

        public String getcreatedate() {
            return createdate;
        }

        public String getstatus() {
            return status;
        }
    }

    public class inquire_Adapter extends ArrayAdapter<inguire_map> {
        private final Activity context;
        String seqNo;

        //建構式
        public inquire_Adapter(Activity context, ArrayList<inguire_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.inquire_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final inguire_map value = getItem(position);

            //找到list_item.xml中的TextView()
            TextView title = listItemView.findViewById(R.id.inquire_title);
            title.setText(value.gettitle());
            Button but_value = listItemView.findViewById(R.id.inquire_but);
            but_value.setText(value.getbut_value());

            seqNo = value.getseqNo();

            TextView location = listItemView.findViewById(R.id.inquire_location);
            location.setText(value.getlocation());

            TextView time = listItemView.findViewById(R.id.inquire_time);
            time.setText(value.gettime());
            if (value.getbut_value().equals("刪除")) {
                but_value.setBackgroundResource(R.drawable.red);
                but_value.setTextColor(0xFFFFFFFF);
                but_value.setOnClickListener(new qlab_inquire_Fragment.inquire_Adapter.ItemButton_Click(context, position, seqNo));
            } else {
                but_value.setBackgroundResource(R.drawable.white);
                but_value.setOnClickListener(new qlab_inquire_Fragment.inquire_Adapter.falsebutclick(context, position, seqNo));
            }

            return listItemView;
        }

        class ItemButton_Click implements View.OnClickListener {
            private int position;
            private Activity mainActivity;
            private String Seano;

            public ItemButton_Click(Activity activity, int position, String seqNo) {
                this.mainActivity = activity;
                this.position = position;
                this.Seano = seqNo;
            }

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mainActivity)
                        .setTitle("是否刪除")
                        .setNeutralButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Cancel_Fac_Schedule?F_SeqNo=" + seqNo;
                                qlab_inquire_Fragment.inquire_myschedule getJson = new qlab_inquire_Fragment.inquire_myschedule();
                                getJson.execute(url);
                            }
                        }).setNegativeButton("否", null).create()
                        .show();
            }
        }

        //按下後不會有反應
        class falsebutclick implements View.OnClickListener {
            private int position;
            private Activity mainActivity;
            private String Seano;

            public falsebutclick(Activity activity, int position, String seqNo) {
                this.mainActivity = activity;
                this.position = position;
                this.Seano = seqNo;
            }

            @Override
            public void onClick(View v) {
            }
        }
    }

    public class inquire_cert_Adapter extends ArrayAdapter<inguire_cert_map> {
        private final Activity context;
        String seqNo;

        //建構式
        public inquire_cert_Adapter(Activity context, ArrayList<inguire_cert_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.inquire_cert_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final inguire_cert_map value = getItem(position);

            //找到list_item.xml中的TextView()
            TextView model = listItemView.findViewById(R.id.inquire_model);
            model.setText(value.getmodel());

            TextView logo = listItemView.findViewById(R.id.inquire_logo);
            logo.setText(value.getlogo());

            TextView respuser = listItemView.findViewById(R.id.inquire_respuser);
            respuser.setText(value.getrespuser());

            TextView createdate = listItemView.findViewById(R.id.inquire_createdate);
            createdate.setText(value.getcreatedate());

            TextView status = listItemView.findViewById(R.id.inquire_status);
            status.setText(value.getstatus());
            return listItemView;
        }
    }

}
