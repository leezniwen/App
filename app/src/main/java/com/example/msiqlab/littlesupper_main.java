package com.example.msiqlab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class littlesupper_main extends AppCompatActivity {

    Button btn1; //equ1
    FloatingActionButton assist; //floatingActionButton2
    EditText et; //抓取輸入文字資料
    String content; //取得文字資料
    little_supper_supper_assist lss_assist = new little_supper_supper_assist();
    FrameLayout fl;

    TextView user;
    TextView littlesupper;
    TextView iferror;

    ArrayList<String> result;

    ListView lv;

    ArrayList<String> device_no = new ArrayList();
    ArrayList url_arr = new ArrayList();
    ArrayList<String> tital_arr = new ArrayList();
    ArrayList<String> con_arr = new ArrayList();
    ArrayList<String> but_arr = new ArrayList();
    ArrayList<String> F_Is_Restrict = new ArrayList();
    ArrayList<map> alist = new ArrayList();
    int total;

    boolean say =false;

    private RelativeLayout mLoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.littlesupper_activity_float_btn);
        mLoadingBar = findViewById(R.id.loading_bar_rl);

        fl = findViewById(R.id.littlesupper_fl);
        user = findViewById(R.id.user_say);
        lv =findViewById(R.id.littlesupper_lv);
        iferror =findViewById(R.id.iferror);
        littlesupper = findViewById(R.id.littlesupper_say);
        littlesupper.setText("小助理 : 你好~~~~ \n您可以告訴小助理您想要「查看新聞」、「查看設備」、「查看實驗室」或是「查看我的預約」喔。 " +
                "\n想知道詳細的使用方式可以告訴小助理「如何使用」喔。");

        btn1 = findViewById(R.id.equ1);
        et = findViewById(R.id.et);
        assist = findViewById(R.id.floatingActionButton2);


        //文字輸入區
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                say=false;
                lss_assist.assist_get_content(et.getText().toString());
                content = lss_assist.catch_content();
                find_say_date();
                System.out.println("content = " + content);

                et.setText("");
            }
        });
        //文字輸入結束

        //使用語音翻譯
        assist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "小助理 : 你可以開始說囉");
                try {
                    startActivityForResult(intent, 200);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "Intent problem", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    //語音助理翻譯
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                lss_assist.assist_get_content(result.get(0));
                content = lss_assist.catch_content();
                say=true;
                find_say_date();
            }
        }
    }
    //語音助理翻譯結束

     //輸出判斷
    private void find_say_date() {
        lv.setVisibility(View.GONE);
        if(say) {
            user.setText(UserData.Name + "說: " +result.get(0));
        }else {
            user.setText(UserData.Name + "說: " + et.getText());
        }
        if (content.equals("所有設備")){
            littlesupper.setText("小助理說: 好的，為您顯示目前開放預約的設備");
            qlab_lab_Fragment qlab_lab_fragment = new qlab_lab_Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.littlesupper_fl, qlab_lab_fragment)
                    .show(qlab_lab_fragment).commit();
            fl.setVisibility(View.VISIBLE);
        } else if (content.equals("我的預約")) {
            littlesupper.setText("小助理說: 好的，為您顯示「我的預約」");
            qlab_inquire_Fragment qlab_inquire_fragment = new qlab_inquire_Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.littlesupper_fl, qlab_inquire_fragment)
                    .show(qlab_inquire_fragment).commit();
            fl.setVisibility(View.VISIBLE);
        } else {
            littlesupper.setText("");
            fl.setVisibility(View.GONE);
        }
        String[] fac={"機構","環測","熱流","無響室","電子","音像","掃地機"};
        for(int i =0;i<fac.length;i++){
            if(content.equals(fac[i])){
                littlesupper.setText("小助理說: 好的，為您顯示"+content+"配備");
                mLoadingBar.setVisibility(View.VISIBLE);//show loading
                lv.setVisibility(View.VISIBLE);
                String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_List?Region=0&Type=" + i;
                qlab_list_value getjson =new qlab_list_value();
                getjson.execute(url);
            }
            else if(content.equals(fac[i]+"預約")){
                littlesupper.setText("小助理說: 好的，為您顯示可以預約的"+fac[i]+"配備");
                mLoadingBar.setVisibility(View.VISIBLE);//show loading
                lv.setVisibility(View.VISIBLE);
                String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_List?Region=0&Type=" + i;
                qlab_list_value getjson =new qlab_list_value();
                getjson.execute(url);
            }
        }

        if (content.contains("key")){
            Intent intent = new Intent(littlesupper_main.this, news_list.class);
            Bundle bundle = new Bundle();
            bundle.putString("key", content.substring(content.indexOf(":") + 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if (content.contains("time")) {
            Intent intent = new Intent(littlesupper_main.this, news_list.class);
            Bundle bundle = new Bundle();
            bundle.putString("key", content.substring(content.indexOf(":") + 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if (content.contains("所有實驗室")) {
            littlesupper.setText("小助理說: 好的，為您顯示實驗室列表");
            qlab_laboratory_Fragment qlab_laboratory_Fragment = new qlab_laboratory_Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.littlesupper_fl, qlab_laboratory_Fragment)
                    .show(qlab_laboratory_Fragment).commit();
            fl.setVisibility(View.VISIBLE);
        }
        if (content.contains("預約實驗室")) {
            Intent intent = new Intent(littlesupper_main.this, qlab_lab_visit.class);
            startActivity(intent);
        }
        if(content.contains("所有預約設備")){
            littlesupper.setText("小助理說: 好的，為您顯示所有可以預約的設備列表");
            littlesupper_qlab_lab_Fragment littlesupper_qlab_lab_Fragment = new littlesupper_qlab_lab_Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.littlesupper_fl, littlesupper_qlab_lab_Fragment)
                    .show(littlesupper_qlab_lab_Fragment).commit();
            littlesupper_qlab_lab_Fragment.littlesupper_qlab_Fragment_content = "所有預約設備";
            fl.setVisibility(View.VISIBLE);
        }

        if(content.contains("如果您想要")){
            littlesupper.setText("小助理說:"+content);
        }
        if(content.contains("請問")){
            littlesupper.setText("小助理說:"+content);
        }

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
                    while (line != null){
                        line = bufferedReader.readLine();
                        data = data + line;
                    }
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));
                    total = UserArray.length();
                    if(content.length()>3){
                        littlesupper_Time_reponse time_reponse = new littlesupper_Time_reponse();

                        for (int i = 0; i < UserArray.length(); i++) {
                            JSONObject IssueData = UserArray.getJSONObject(i);
                            String F_SeqNo = IssueData.getString("F_SeqNo");
                            String STATUS   = IssueData.getString("F_Status");
                            String buy_time = IssueData.getString("ApplierSDate");
                            String img_url = "http:"+IssueData.getString("IMG");
                            String ApplierSDate = "";
                            for (int j = 0; j < buy_time.indexOf("T"); j++) {
                                char c = buy_time.charAt(j);
                                ApplierSDate += c;
                            }
                            String Storage_time = IssueData.getString("ApplierEDate");
                            String ApplierEDate = "";
                            for (int j = 0; j < Storage_time.indexOf("T"); j++){
                                char c = Storage_time.charAt(j);
                                ApplierEDate += c;
                            }
                            if(STATUS.equals("1")){
                                but_value = "預約" ;
                                if(IssueData.getString("Using").equals("0")){
                                    littlesupper_assist_Schedule las = new littlesupper_assist_Schedule();
                                    if(las.result(F_SeqNo)){

                                        tital = IssueData.getString("F_Facility");
                                        tital_arr.add(tital);
                                        con = "財編: " + IssueData.getString("F_AssetNo") + "\n";
                                        con += "存放位置: " + IssueData.getString("F_Location") +
                                                "\n預約狀態:  [可以] 預約！"+
                                                "\n ";
                                        con_arr.add(con);
                                        url_arr.add(img_url);
                                        F_Is_Restrict.add(IssueData.getString("F_Is_Restrict"));
                                        device_no.add(IssueData.getString("F_SeqNo"));
                                        alist.add(new map(tital, con, but_value, img_url, IssueData.getString("F_SeqNo")));
                                    }
                                }
                                else if(IssueData.getString("Using").equals("1")){
                                    littlesupper_assist_Schedule las = new littlesupper_assist_Schedule();
                                    if(las.result(F_SeqNo)){
                                            tital = IssueData.getString("F_Facility");
                                            tital_arr.add(tital);
                                            con = "財編: " + IssueData.getString("F_AssetNo") + "\n";
                                            con += "存放位置: " + IssueData.getString("F_Location") +
                                                    "\n預約狀態:  [可以] 預約！"+
                                                    "\n ";
                                            con_arr.add(con);
                                            url_arr.add(img_url);
                                            F_Is_Restrict.add(IssueData.getString("F_Is_Restrict"));
                                            device_no.add(IssueData.getString("F_SeqNo"));
                                            alist.add(new map(tital, con, but_value, img_url, IssueData.getString("F_SeqNo")));
                                        }
                                }
                            }
                            else if(STATUS.equals("0")){
                                but_value = "未開放";
                                continue;
                            }
                        }
                    }


                    else {
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
                            alist.add(new map(tital, con, but_value, img_url, IssueData.getString("F_SeqNo")));
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
                catch (Exception e){

                }

            return data;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(data);
            if (total == 0) {
                iferror.setText("沒有資料");
                lv.setVisibility(View.GONE);
            } else {
                iferror.setText("");
                lv.setVisibility(View.VISIBLE);
            }
            qlab_lab_Adapter adapter = new qlab_lab_Adapter(littlesupper_main.this, alist);
            lv.setAdapter(adapter);
            mLoadingBar.setVisibility(View.GONE);//gone loading
            lv.setOnItemClickListener(onClickListView);
        }
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(littlesupper_main.this, qlab_lab_page.class);
            String str =  device_no.get(position);
            Bundle bun = new Bundle();
            bun.putString("device_no", str);
            intent.putExtras(bun);
            littlesupper_main.this.startActivity(intent);
        }
    };

    public class map {
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
        public map(String totital, String toconn, String tobutvalue, String tophoto, String toseqNo) {
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
    public class qlab_lab_Adapter extends ArrayAdapter<map> {
        private final Activity context;
        String seqNo;
        ImageView img;

        //建構式
        public qlab_lab_Adapter(Activity context, ArrayList<map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_list_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final map value = getItem(position);
            //找到list_item.xml中的TextView()
            TextView tital = listItemView.findViewById(R.id.qlab_lab_list_tital);
            tital.setText(value.gettital());
            seqNo = value.getseqNo();
            Button but_value = listItemView.findViewById(R.id.qlab_lab_list_booking_but);
            but_value.setText(value.getbut_value());
            if (value.getbut_value().equals("預約")) {
                but_value.setBackgroundResource(R.drawable.red);
                but_value.setTextColor(0xFFFFFFFF);
                but_value.setOnClickListener(new ItemButton_Click(context, position, seqNo));
            } else if (value.getbut_value().equals("未開放")) {
                but_value.setBackgroundResource(R.drawable.ash);
                but_value.setTextColor(0xFFFFFFFF);
                but_value.setOnClickListener(new falsebutclick(context, position, seqNo));
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
                if(content.indexOf("預約")>-1 && content.length()>3){
                    Log.d("預約" , content);
                    littlesupper_Time_reponse time_reponse = new littlesupper_Time_reponse();
                    littlesupper_calender  lsmc = new littlesupper_calender();
                    Intent intent = new Intent(mainActivity ,qlab_tobooking.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("seqNo",Seano);
                    bundle.putString("F_Facility" , tital_arr.get(position));
                    String con =con_arr.get(position).substring(0,con_arr.get(position).indexOf("存"));
                    bundle.putString("F_AssetNo" , con);
                    bundle.putString("bookingdate" , time_reponse.getStart_time().substring(0,10));
                    bundle.putString("bookingEdate" , time_reponse.getEnd_time().substring(0,10));
                    bundle.putString("F_Is_Restrict",F_Is_Restrict.get(position));
                    Log.d("編號" , Seano);
                    Log.d("文字" , tital_arr.get(position));
                    Log.d("時間" , time_reponse.getStart_time() +"\n"+time_reponse.getEnd_time());
                    intent.putExtras(bundle);
                    mainActivity.startActivity(intent);
                }
                else if(content.indexOf("預約")==-1)
                {
                    Intent intent = new Intent(mainActivity, qlab_booking_device.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("seqNo", Seano);
                    intent.putExtras(bundle);
                    mainActivity.startActivity(intent);
                }
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