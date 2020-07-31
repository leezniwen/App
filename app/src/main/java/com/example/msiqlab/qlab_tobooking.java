package com.example.msiqlab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import java.util.List;

public class qlab_tobooking extends AppCompatActivity {
    String seqNo;
    String F_AssetNo;
    String F_Facility;
    String F_Is_Restrict;
    String bookingdate;
    String bookingEdate;
    TextView startdate_tv;
    TextView enddate_tv;

    String modelID;
    String model;

    int SorE;

    ArrayList<String> ModelID = new ArrayList();
    ArrayList<String> Model = new ArrayList();
    private EditText con_et;
    private Spinner spinner;
    private RelativeLayout mLoadingBar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_tobooking);
        mLoadingBar = findViewById(R.id.news_list_loading_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        Bundle bundle = this.getIntent().getExtras();

        seqNo = bundle.getString("seqNo");
        F_AssetNo = bundle.getString("F_AssetNo");
        F_Facility = bundle.getString("F_Facility");
        F_Is_Restrict = bundle.getString("F_Is_Restrict");
        bookingdate = bundle.getString("bookingdate");
        bookingEdate = bundle.getString("bookingEdate");

        TextView attention = findViewById(R.id.attention_tv);
        startdate_tv = findViewById(R.id.startdate_tv);
        enddate_tv = findViewById(R.id.enddate_tv);
        Button chick_but = findViewById(R.id.tobooking_chick_but);
        TextView title = findViewById(R.id.tobooking_title);
        title.setText(F_Facility + "\n" + F_AssetNo);
        chick_but.setOnClickListener(chick_butOnclick);
        if (Integer.valueOf(F_Is_Restrict) == 0) {
            attention.setText("申請預約有效時間為3個月內");
        } else {
            attention.setText("申請預約有效時間為3日內");
        }
        startdate_tv.setText(bookingdate + " 08:30:00 上午");
        startdate_tv.setOnClickListener(startonchick);
        enddate_tv.setText(bookingEdate + " 17:30:00 下午");
        enddate_tv.setOnClickListener(endonchick);
        spinner = findViewById(R.id.PM_ID_spinner);

        String workid = UserData.WorkID;

        String ur1 = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_My_Fac_Model?F_Keyin=" + workid;
        getPM_ID getBookingJson = new getPM_ID();
        getBookingJson.execute(ur1);
        con_et = findViewById(R.id.con_et);
        con_et.setText("");
        LinearLayout tobooking_ll = findViewById(R.id.tobooking_ll);
        tobooking_ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(con_et.getWindowToken(), 0);
                return false;
            }
        });
    }

    private View.OnClickListener startonchick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SorE = 1;
            Intent intent = new Intent(qlab_tobooking.this, qlab_tobooking_setdate.class);
            Bundle bundle = new Bundle();
            bundle.putString("bookingdate", bookingdate);
            bundle.putInt("SorE", SorE);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);

        }
    };

    private View.OnClickListener endonchick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SorE = 2;
            Intent intent = new Intent(qlab_tobooking.this, qlab_tobooking_setdate.class);
            Bundle bundle = new Bundle();
            bundle.putString("bookingdate", bookingdate);
            bundle.putInt("SorE", SorE);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        }
    };

    private View.OnClickListener chick_butOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String errorstr = "";
            int error = 0;

            String startdate = startdate_tv.getText().toString();
            String enddate = enddate_tv.getText().toString();


            String start = startdate_tv.getText().toString();
            String end = enddate_tv.getText().toString();
            String con = con_et.getText().toString();
            int s = 0;
            int e = 0;

            if (startdate_tv.getText().equals("")) {
                errorstr += "起始時間  ";
                error++;
            } else {
                start = start.replaceAll("-", "");
                start = start.replaceAll(":", "");
                start = start.replaceAll("上午", "");
                start = start.replaceAll("下午", "");
                start = start.replaceAll(" ", "");
                start =start.substring(0,9);
                s = Integer.valueOf(start);
            }
            if (enddate_tv.getText().equals("")) {
                errorstr += "結束時間  ";
                error++;
            } else {
                end = end.replaceAll("-", "");
                end = end.replaceAll(":", "");
                end = end.replaceAll("上午", "");
                end = end.replaceAll("下午", "");
                end = end.replaceAll(" ", "");
                end =end.substring(0,9);
                e = Integer.valueOf(end);
            }
            if (con_et.getText().toString().length() < 1) {
                errorstr += "描述  ";
                error++;
            }
            if (error != 0) {
                errorstr += "不得為空白。";
            }
            if (s > 0 && e > 0 && s >= e) {
                errorstr += " 結束時間不能早於開始時間。";
                error++;
            }
            if (error != 0) {
                Toast.makeText(qlab_tobooking.this, "系統: " + errorstr, Toast.LENGTH_SHORT).show();
            }
            if (error == 0) {
                Intent intent = new Intent(qlab_tobooking.this, qlab_booking_last.class);
                Bundle bundle = new Bundle();
                bundle.putString("F_Master_ID", seqNo);
                bundle.putString("F_Desc", con);
                bundle.putString("F_StartDate", startdate);
                bundle.putString("F_EndDate", enddate);
                bundle.putString("F_PM_ID", modelID);
                bundle.putString("F_Is_Restrict", F_Is_Restrict);
                bundle.putString("F_Facility", F_Facility);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("Booking_Check"));
            if (CheckBooking == 1) {
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("Booking_Check", "1");
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
            String value = bundle.getString("value");
            if (SorE == 1) {
                startdate_tv.setText(value);
            } else {
                enddate_tv.setText(value);
            }
        }
    }

    public class getPM_ID extends AsyncTask<String, Void, String> {
        String data = "";

        @Override
        protected String doInBackground(String... urlStrings) {
            ModelID.clear();
            Model.clear();
            try {
                ModelID.add("");
                Model.add("");
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
                ModelID.set(0, IssueData.getString("ModelID"));
                Model.set(0, IssueData.getString("Model"));
                for (int i = 1; i < UserArray.length(); i++) {
                    IssueData = UserArray.getJSONObject(i);
                    ModelID.add(IssueData.getString("ModelID"));
                    Model.add(IssueData.getString("Model"));
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
            DropDownModelAdapter adapter = new DropDownModelAdapter(qlab_tobooking.this, Model, ModelID);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(spinneritem);
            mLoadingBar.setVisibility(View.GONE);//gone loading
        }
    }

    private Spinner.OnItemSelectedListener spinneritem = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            modelID = ModelID.get(position);
            model = Model.get(position);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public class DropDownModelAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayModel;

        private List<String> ArrayModelID;

        private Context ProjectContext;

        public int selectedItemdp;

        public DropDownModelAdapter(Context context, List<String> ArrayModel, List<String> ArrayModelID) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.ArrayModel = ArrayModel;

            this.ArrayModelID = ArrayModelID;
        }

        @Override
        public int getCount() {
            return ArrayModel.size();
        }

        @Override
        public Object getItem(int position) {
            return ArrayModel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.tobooking_spinner_item, parent, false);

            TextView style = (TextView) v.findViewById(R.id.spinner_item_tv);

            style.setText(ArrayModel.get(position));

            Log.w("test", "test");

            return v;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = null;
            v = mLayInf.inflate(R.layout.tobooking_spinner_item, parent, false);

            TextView style = (TextView) v.findViewById(R.id.spinner_item_tv);

            style.setText(ArrayModel.get(position));

            // If this is the selected item position
            if (position == selectedItemdp) {
                style.setBackgroundColor(Color.parseColor("#e2e2e2"));//灰色
            } else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#ffffff"));//藍色
            }
            return v;
        }

    }



}
