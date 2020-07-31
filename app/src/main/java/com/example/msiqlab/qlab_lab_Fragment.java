package com.example.msiqlab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
import java.util.ArrayList;
import java.util.InputMismatchException;

public class qlab_lab_Fragment extends Fragment {
    private static TabLayout tabLayout;
    ArrayList F_ID = new ArrayList();
    ArrayList F_NAME = new ArrayList();
    String type = "";
    private static ListView lv;
    private static TextView iferror;
    private RelativeLayout mLoadingBar;
    ArrayList<String> device_no = new ArrayList();
    ArrayList url_arr = new ArrayList();
    ArrayList<String> tital_arr = new ArrayList();
    ArrayList<String> con_arr = new ArrayList();
    ArrayList<String> but_arr = new ArrayList();
    ArrayList<qlab_lab_map> alist = new ArrayList<qlab_lab_map>();

    String this_url;
    int total;
    int run = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.qlab_lab, container, false);
        mLoadingBar = view.findViewById(R.id.qlab_loading_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_Type_List?Region=0";
        qlab_lab_Fragment.qlab_tab_value getNetworkJson = new qlab_lab_Fragment.qlab_tab_value();
        getNetworkJson.execute(url);
        tabLayout = view.findViewById(R.id.qlab_lab_tabl);
        lv = view.findViewById(R.id.qlab_lab_lv);
        iferror = view.findViewById(R.id.iferror);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                mLoadingBar.setVisibility(View.VISIBLE);//show loading
                //選擇時觸發
                String position = String.valueOf(tab.getPosition());
                String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_List?Region=0&Type=" + position;
                this_url = position;
                qlab_lab_Fragment.qlab_list_value getNetworkJson_lsit = new qlab_lab_Fragment.qlab_list_value();
                getNetworkJson_lsit.execute(url);
        }

            public void onTabUnselected(TabLayout.Tab tab) {
                //未選擇時觸發
                if (tital_arr != null) {
                    tital_arr.clear();
                }
                if (con_arr != null) {
                    con_arr.clear();
                }
                if (alist != null) {
                    alist.clear();
                }
                if (device_no != null) {
                    device_no.clear();
                }
                if (url_arr != null) {
                    url_arr.clear();
                }
                if (but_arr != null) {
                    but_arr.clear();
                }
            }

            public void onTabReselected(TabLayout.Tab tab) {
                //選中之後再次點擊即複選時觸發
            }
        });
        return view;
    }

    public class qlab_tab_value extends AsyncTask<String, Void, String> {
        String data = "";
        int total = 0;

        @Override
        protected String doInBackground(String... urlStrings) {
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
                total = UserArray.length();
                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject IssueData = UserArray.getJSONObject(i);
                    F_ID.add(IssueData.getString("F_ID"));
                    F_NAME.add(IssueData.getString("F_Name"));
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
                String name = F_NAME.get(i).toString();
                qlab_lab_Fragment.tabLayout.addTab(qlab_lab_Fragment.tabLayout.newTab().setText(name));
            }
        }
    }

    public void linkerror() {
        mLoadingBar.setVisibility(View.GONE);//gone loading
        Toast.makeText(getActivity(), "連線異常，伺服器連線中斷", Toast.LENGTH_SHORT).show();
    }

    public class qlab_list_value extends AsyncTask<String, Void, String> {
        String data = "";
        String tital = "";
        String con = "";
        String but_value = "";

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urlStrings) {
            device_no.clear();
            url_arr.clear();
            tital_arr.clear();
            con_arr.clear();
            but_arr.clear();
            alist.clear();
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
                    String buy_time = IssueData.getString("ApplierSDate");
                    String ApplierSDate = "";
                    for (int j = 0; j < buy_time.indexOf("T"); j++) {
                        char c = buy_time.charAt(j);
                        ApplierSDate += c;
                    }
                    String Storage_time = IssueData.getString("ApplierEDate");
                    String ApplierEDate = "";
                    for (int j = 0; j < Storage_time.indexOf("T"); j++) {
                        char c = Storage_time.charAt(j);
                        ApplierEDate += c;
                    }
                    tital = IssueData.getString("F_Facility");
                    tital_arr.add(tital);
                    con = "財編: " + IssueData.getString("F_AssetNo") + "\n";

                    if (IssueData.getString("F_Status").equals("0")) {
                        continue;
                        //如果不開放，就不顯示
                    } else {
                        if (IssueData.getString("Using").equals("1")) {
                            con += "存放位置: " + IssueData.getString("F_Location") +
                                    "\n借出人員: " + IssueData.getString("Applier") +
                                    "\n借出日期: " + ApplierSDate +
                                    "\n            至:  " + ApplierEDate;

                            con_arr.add(con);
                        } else {
                            con += "存放位置: " + IssueData.getString("F_Location") +
                                    "\n借出人員: 無" +
                                    "\n借出日期: 無" +
                                    "\n ";

                        }
                        con_arr.add(con);
                    }

                    if (IssueData.getString("F_Status").equals("1")) {
                        but_value = "預約";
                    } else {
                        but_value = "未開放";
                    }
                    but_arr.add(but_value);
                    String img_url = "http:" + IssueData.getString("IMG");
                    url_arr.add(img_url);
                    device_no.add(IssueData.getString("F_SeqNo"));
                    alist.add(new qlab_lab_map(tital, con, but_value, img_url, IssueData.getString("F_SeqNo")));
                }
            } catch (InputMismatchException e) {
                e.printStackTrace();
                linkerror();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                linkerror();
            } catch (IOException e) {
                e.printStackTrace();
                linkerror();
            } catch (JSONException e) {
                e.printStackTrace();
                linkerror();
            }
            return data;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(data);
            run = 0;
            mLoadingBar.setVisibility(View.GONE);//gone loading

            if (total == 0) {
                iferror.setText("沒有資料");
                lv.setVisibility(View.GONE);
            } else {
                iferror.setText("");
                lv.setVisibility(View.VISIBLE);
            }
            qlab_lab_Adapter adapter = new qlab_lab_Adapter(getActivity(), alist);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(onClickListView);
        }
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), qlab_lab_page.class);
            String str = (String) device_no.get(position);
            Bundle bun = new Bundle();
            bun.putString("device_no", str);
            intent.putExtras(bun);
            getActivity().startActivity(intent);
        }
    };


    public class qlab_lab_map {
        //大標題
        private String tital;
        //內容
        private String conn;
        //按鈕
        private String but_value;
        //照片
        private String photo;
        //編號
        private String seqNo;


        //建構式
        public qlab_lab_map(String totital, String toconn, String tobutvalue, String tophoto, String toseqNo) {
            tital = totital;
            conn = toconn;
            but_value = tobutvalue;
            photo = tophoto;
            seqNo = toseqNo;
        }

        public String gettital() {
            return tital;
        }

        public String getconn() {
            return conn;
        }

        public String getbut_value() {
            return but_value;
        }

        public String getPhoto() {
            return photo;
        }

        public String getseqNo() {
            return seqNo;
        }

    }


    public class qlab_lab_Adapter extends ArrayAdapter<qlab_lab_map> {
        private final Activity context;
        String seqNo;
        ImageView img;

        //建構式
        public qlab_lab_Adapter(Activity context, ArrayList<qlab_lab_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_list_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final qlab_lab_map value = getItem(position);
            //找到list_item.xml中的TextView()
            TextView tital = listItemView.findViewById(R.id.qlab_lab_list_tital);
            tital.setText(value.gettital());
            seqNo = value.getseqNo();
            Button but_value = listItemView.findViewById(R.id.qlab_lab_list_booking_but);
            but_value.setText(value.getbut_value());
            if (value.getbut_value().equals("預約")) {
                but_value.setBackgroundResource(R.drawable.red);
                but_value.setTextColor(0xFFFFFFFF);
                but_value.setOnClickListener(new qlab_lab_Fragment.qlab_lab_Adapter.ItemButton_Click(context, position, seqNo));
            } else if (value.getbut_value().equals("未開放")) {
                but_value.setBackgroundResource(R.drawable.ash);
                but_value.setTextColor(0xFFFFFFFF);
                but_value.setOnClickListener(new qlab_lab_Fragment.qlab_lab_Adapter.falsebutclick(context, position, seqNo));
            }
            TextView conn = listItemView.findViewById(R.id.qlab_lab_list_content);
            conn.setText(value.getconn());

            img = listItemView.findViewById(R.id.qlab_lab_list_img);

            Glide.with(getContext())
                    .load(value.getPhoto())
                    .error(R.drawable.ic_image)//load失敗的Drawable
                    .placeholder(R.drawable.ic_image)//loading時候的Drawable
                    .into(img);


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
                Intent intent = new Intent(mainActivity, qlab_booking_device.class);
                Bundle bundle = new Bundle();
                bundle.putString("seqNo", Seano);
                intent.putExtras(bundle);
                mainActivity.startActivity(intent);
            }
        }

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


}



