package com.example.msiqlab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class qlab_lab_visit extends AppCompatActivity {


    TextView time_tv;
    EditText company_ev;
    EditText people_ev;
    EditText purpose_ev;
    String lab = "";
    String Guide = "0";

    private RelativeLayout mLoadingBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_lab_visit);
        TextView lab_precautions_tv = findViewById(R.id.lab_precautions_tv);

        lab_precautions_tv.setText(
                "1. 進行參觀時請勿隨意碰觸任何測試中機器。\n" +
                        "2. 實驗室嚴禁拍照。\n" +
                        "3. 因專案進行中，實驗室參觀申請請最晚三天前提出申請，若無法三天前提出申請，恕無法受理。\n" +
                        "4. 實驗室嚴禁飲食。\n" +
                        "5. 因專案進行、設備維修及其它不可抗力因素，DQA保留實驗室參觀權利，若無法參觀實驗室將會與需求者協調告知，請見諒。");

        //申請人資料
        final TextView name_tv = findViewById(R.id.visit_name_tv);
        TextView workid_tv = findViewById(R.id.visit_workid_tv);
        final TextView phone_ev = findViewById(R.id.visit_phone_ev);
        TextView email_tv = findViewById(R.id.visit_email_tv);
        TextView department_tv = findViewById(R.id.visit_department_tv);
        TextView supervisor_tv = findViewById(R.id.visit_supervisor_tv);
        name_tv.setText(UserData.Name);
        workid_tv.setText(UserData.WorkID);
        phone_ev.setText(UserData.Phone);
        email_tv.setText(UserData.Email);
        department_tv.setText(UserData.Dept);
        supervisor_tv.setText(UserData.WebFlowBossName);
        //參訪人員資料
        final EditText company_ev = findViewById(R.id.visit_company_ev);
        final EditText people_ev = findViewById(R.id.visit_people_ev);
        final EditText purpose_ev = findViewById(R.id.visit_purpose_ev);
        ListView lab_list_lv = findViewById(R.id.visit_lab_list);
        time_tv = findViewById(R.id.visit_time_tv);
        RadioGroup visit_rg = findViewById(R.id.visit_rg);


        ArrayList<radiogroup_map> arrayList = new ArrayList<>();
        String[] lab_list = {"環測實驗室 (位於三廠六樓)", "熱流實驗室 (位於三廠六樓)", "噪音實驗室 (位於宿舍旁)", "機構實驗室 (位於一廠B1)", "聆聽室 (位於三廠一樓)"};
        for (int i = 0; i < lab_list.length; i++) {
            arrayList.add(new radiogroup_map("0", lab_list[i]));
        }
        radiogroup_Adapter adapter = new radiogroup_Adapter(qlab_lab_visit.this, arrayList);
        lab_list_lv.setAdapter(adapter);
        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int SorE = 3;
                Calendar mCal = Calendar.getInstance();
                String dateformat = "yyyy-MM-dd";
                SimpleDateFormat df = new SimpleDateFormat(dateformat);
                String today = df.format(mCal.getTime());
                Intent intent = new Intent(qlab_lab_visit.this, qlab_tobooking_setdate.class);
                Bundle bundle = new Bundle();
                bundle.putString("bookingdate", "2020-03-30");
                bundle.putInt("SorE", SorE);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        visit_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.visit_rb1:
                        Guide = "1";
                        break;
                    case R.id.visit_rb2:
                        Guide = "2";
                        break;
                    case R.id.visit_rb3:
                        Guide = "0";
                        break;
                }
            }
        });

        //備註
        final EditText note = findViewById(R.id.visit_note_ev);

        ScrollView sv = findViewById(R.id.sv);
        sv.setOnTouchListener(new View.OnTouchListener() {  //監聽事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(phone_ev.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(company_ev.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(purpose_ev.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(people_ev.getWindowToken(), 0);
                return false;
            }
        });

        TextView okbut = findViewById(R.id.ok_but_tv);
        okbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCal = Calendar.getInstance();
                String dateformat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat df = new SimpleDateFormat(dateformat);
                String today = df.format(mCal.getTime());
                String getday = String.valueOf(time_tv.getText());
                if (getday.contains("上午")) {
                    getday = getday.replace(" 上午", "");
                } else if (getday.contains("下午")) {
                    int time = Integer.valueOf(getday.substring(11, 13));
                    time += 12;
                    getday = getday.replace(getday.substring(12, 14), String.valueOf(time));
                }
                String F_Keyin = UserData.WorkID;//工號
                String F_Cname = UserData.Name;//申請人中文名
                String F_Ext = UserData.Phone;//分機
                String F_Mail = UserData.Email;//email
                String F_Dept = UserData.Dept;//部門名
                String F_Boss = UserData.WebFlowBossName;//主管
                String F_Company = String.valueOf(company_ev.getText());//參訪者
                String F_People = String.valueOf(people_ev.getText());//人數
                String F_Purpose = String.valueOf(purpose_ev.getText());//參訪目
                String F_Lab = lab;//參訪的實驗室
                String F_VisitDate = getday;//參訪日期
                String F_Guide = Guide;//參訪指南(012)
                String F_Note = String.valueOf(note.getText());//備註

                String error = "系統提示:\n";


                if (!getday.equals("選擇時間")) {
                    Long loggetday = Long.valueOf(getday.substring(0, 10).replaceAll("-", ""));
                    Long logtoday = Long.valueOf(today.substring(0, 10).replaceAll("-", ""));
                    if (loggetday - logtoday < 3) {
                        error += "實驗室參觀申請請最晚三天前提出申請\n";
                    }
                } else {
                    error += "請選擇時間\n ";
                }
                if (lab.equals("")) {
                    error += "請選擇至少一個實驗室\n";
                }
                if (F_Company.equals("")) {
                    error += "參訪者不得為白\n";
                }
                if (F_People.equals("")) {
                    error += "人數不得為白\n";
                }
                if (F_Purpose.equals("")) {
                    error += "目的不得為白\n";
                }
                if (error.equals("系統提示:\n")) {
                    mLoadingBar = findViewById(R.id.loading_bar_rl);
                    mLoadingBar.setVisibility(View.VISIBLE);//show loading
                    String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Insert_LabVisit?F_Keyin="
                            + F_Keyin + "&F_Cname="
                            + F_Cname + "&F_Ext="
                            + F_Ext + "&F_Mail="
                            + F_Mail + "&F_Dept="
                            + F_Dept + "&F_Boss="
                            + F_Boss + "&F_Company="
                            + F_Company + "&F_People="
                            + F_People + "&F_Purpose="
                            + F_Purpose + "&F_Lab="
                            + F_Lab + "&F_VisitDate="
                            + F_VisitDate + "&F_Guide="
                            + F_Guide + "&F_Note=" + F_Note;
                    qlab_lab_visit.qlab_Insert_LabVisit getNetworkJson = new qlab_lab_visit.qlab_Insert_LabVisit();
                    getNetworkJson.execute(url);
                } else {
                    Toast.makeText(qlab_lab_visit.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("Booking_Check"));
            if (CheckBooking == 0) {
                String value = bundle.getString("value");
                time_tv.setText(value);
            }
        }
    }
    public class qlab_Insert_LabVisit extends AsyncTask<String, Void, String> {
        String data = "";
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
            mLoadingBar.setVisibility(View.GONE);//gone loading
            new AlertDialog.Builder(qlab_lab_visit.this)
                    .setTitle("申請成功")
                    .setNegativeButton("cancel",null)
                    .setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create()
                    .show();
        }
    }

    //map
    public class radiogroup_map {
        private String mod;
        private String value;

        private radiogroup_map(String mod, String value) {
            this.mod = mod;
            this.value = value;
        }

        public String getMod() {
            return mod;
        }

        public String getValue() {
            return value;
        }
    }

    //adapter
    public class radiogroup_Adapter extends ArrayAdapter<radiogroup_map> {
        private final Activity context;

        //建構式
        public radiogroup_Adapter(Activity context, ArrayList<radiogroup_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_lab_visit_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final radiogroup_map value = getItem(position);
            //找到list_item.xml中的TextView()
            LinearLayout ll = listItemView.findViewById(R.id.ll);
            TextView item_value = listItemView.findViewById(R.id.item_value);
            item_value.setText(value.getValue());
            final TextView mod = listItemView.findViewById(R.id.chick_mod);
            if (value.getMod().equals("0")) {
                mod.setBackgroundResource(R.drawable.ic_adjust);
            } else {
                mod.setBackgroundResource(R.drawable.ic_check);
            }
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (value.getMod().equals("0")) {
                        mod.setBackgroundResource(R.drawable.ic_check);
                        value.mod = "1";
                        lab += value.getValue() + ";";
                    } else {
                        mod.setBackgroundResource(R.drawable.ic_adjust);
                        value.mod = "0";
                        String string = value.getValue() + ";";
                        lab = lab.replace(string, "");
                    }
                }
            });
            return listItemView;
        }
    }

}
