package com.example.msiqlab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

public class qlab_certification extends AppCompatActivity {
    ArrayList<qlab_certification.qlab_cert_map> arrayList = new ArrayList();
    ArrayList<String> logo_arr;
    ArrayList<String> img_arr;
    ArrayList<String> expense_arr;
    ArrayList<String> time_arr;

    ListView lv;
    int total;
    int expensetotal=0;
    qlab_cert_Adapter adapter;
    ArrayList<String> ModelID = new ArrayList();
    ArrayList<String> Model = new ArrayList();
    ArrayList<String> F_RWorkID_arr = new ArrayList();
    Spinner spinner;
    String modelID;
    String model;
    String QueryLab_Temp_Info="";
    String errorstr ="";

    private RelativeLayout mLoadingBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_certification);
        mLoadingBar = findViewById(R.id.loading_bar_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading

        lv = findViewById(R.id.cert_list_lv);
        spinner = findViewById(R.id.PM_ID_spinner);
        TextView cert_total_tv =findViewById(R.id.cert_total_tv);
        Bundle bundle = this.getIntent().getExtras();
        String img = bundle.getString("img");
        String logo = bundle.getString("logo");
        String expense = bundle.getString("expense");
        String time = bundle.getString("time");
        String F_RWorkID = bundle.getString("F_RWorkID");
        final String seqNo = bundle.getString("seqNo");
        qlab_cert_arr arr = new qlab_cert_arr(logo);
        logo_arr = arr.getarr();
        arr = new qlab_cert_arr(img);
        img_arr = arr.getarr();
        arr = new qlab_cert_arr(expense);
        expense_arr = arr.getarr();
        arr = new qlab_cert_arr(time);
        time_arr = arr.getarr();
        total = img_arr.size();

        arr = new qlab_cert_arr(F_RWorkID);
        F_RWorkID_arr = arr.getarr();


        for (int i = 0; i < total; i++) {
            expensetotal +=Integer.valueOf(expense_arr.get(i));
        }
        for (int i = 0; i < total; i++) {
            QueryLab_Temp_Info +=logo_arr.get(i)+"。"+F_RWorkID_arr.get(i)+"。"+expense_arr.get(i)+"。"+time_arr.get(i)+";";
            System.out.println(QueryLab_Temp_Info);
        }
        cert_total_tv.setText("小計: USD$"+expensetotal);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < total; i++) {
                    arrayList.add(new qlab_cert_map(img_arr.get(i), logo_arr.get(i), expense_arr.get(i), time_arr.get(i)));
                     adapter = new qlab_cert_Adapter(qlab_certification.this, arrayList);
                }
                runOnUiThread(new Runnable(){
                    public void run() {
                        lv.setAdapter(adapter);
                        mLoadingBar.setVisibility(View.GONE);//gone loading
                    }});
            }
        }).start();
        String workid = UserData.WorkID;
        String ur1 = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_My_Fac_Model?F_Keyin=" + workid;
        qlab_certification.getPM_ID getJson = new qlab_certification.getPM_ID();
        getJson.execute(ur1);
        Button button = findViewById(R.id.cert_next_but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //項目SeqNo,
                //認證項目銘。負責人工號。價格。週次;
                System.out.println("modelID="+modelID);
                if(modelID.equals("")){
                    errorstr="失敗:沒有專案";
                }
               String ur1l = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Certification_Insert?QueryLab_Temp="+seqNo
                        +"&QueryLab_Temp_Info="+QueryLab_Temp_Info+"&QueryLab_ModelID="+modelID+"&QueryWorkID="+"10003130"+"&QueryWorkIDName="+"劉慶忠 kevinliu"+"&QuerystrUserID="+UserData.WorkID+"&QuerystrUserName="+UserData.EName;
                qlab_certification.tocert getJson = new qlab_certification.tocert();
                getJson.execute(ur1l);
            }
        });
    }

    //處裡圖片


    //字串轉陣列
    public class qlab_cert_arr {
        private String str;

        private qlab_cert_arr(String str) {
            this.str = str;
        }

        public ArrayList<String> getarr() {
            ArrayList<String> arr = new ArrayList<String>();
            int total = str.length();
            String string = "";
            for (int i = 0; i < total; i++) {
                if (str.substring(i, i + 1).equals(",")) {
                    arr.add(string);
                    string = "";
                } else {
                    string += str.substring(i, i + 1);
                }
            }
            return arr;
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
                ModelID.set(0,IssueData.getString("ModelID"));
                Model.set(0,IssueData.getString("Model"));
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
            qlab_certification.DropDownModelAdapter adapter = new qlab_certification.DropDownModelAdapter(qlab_certification.this, Model, ModelID);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(spinneritem);
        }
    }

    public class tocert extends AsyncTask<String, Void, String> {
        String data = "";
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        protected void onPostExecute(String s) {
            super.onPostExecute(data);
            if(errorstr.equals("")){
                errorstr ="成功";
            }
            new AlertDialog.Builder(qlab_certification.this)
                    .setTitle(errorstr)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            qlab_certification.this.finish();
                        }
                    }).show();
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

    //adapter
    public class qlab_cert_Adapter extends ArrayAdapter<qlab_certification.qlab_cert_map> {
        private final Activity context;

        //建構式
        public qlab_cert_Adapter(Activity context, ArrayList<qlab_certification.qlab_cert_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_cert_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final qlab_certification.qlab_cert_map value = getItem(position);
            //找到list_item.xml中的TextView()
            ImageView img = listItemView.findViewById(R.id.cert_img);
            Glide.with(getContext())
                    .load(value.getImg())
                    .error(R.drawable.ic_image)//load失敗的Drawable
                    .placeholder(R.drawable.ic_image)//loading時候的Drawable
                    .into(img);
            TextView logo = listItemView.findViewById(R.id.cert_logo);
            logo.setText(value.getLogo());

            TextView expense = listItemView.findViewById(R.id.cert_expense);
            expense.setText("費用:USD " + value.getExpense());

            TextView time = listItemView.findViewById(R.id.cert_time);
            time.setText("工時: " + value.getTime()+"週");

            return listItemView;
        }
    }

    public class qlab_cert_map {
        private String img;
        private String logo;
        private String expense;
        private String time;

        private qlab_cert_map(String toimg, String tologo, String toexpense, String totime) {
            this.img = toimg;
            this.logo = tologo;
            this.expense = toexpense;
            this.time = totime;
        }

        public String getImg() {
            return img;
        }

        public String getLogo() {
            return logo;
        }

        public String getExpense() {
            return expense;
        }

        public String getTime() {
            return time;
        }
    }
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
