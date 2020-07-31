package com.example.msiqlab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class qlab_certification_Fragment extends Fragment {
    GridView gv;
    TextView total_tv;
    Button next_but;

    ArrayList<qlab_cert_map> arrayList = new ArrayList();
    ArrayList<Bitmap> img_arr = new ArrayList();
    ArrayList<String> url_arr = new ArrayList();
    ArrayList<String> logo_arr = new ArrayList();
    ArrayList<String> cert_class_arr = new ArrayList();
    ArrayList<String> expense_arr = new ArrayList();
    ArrayList<String> time_arr = new ArrayList();
    ArrayList<String> seqNo_arr = new ArrayList();
    ArrayList<String> F_RWorkID_arr = new ArrayList();

    int expense_total = 0;
    int chick = 0;
    int total;

    private boolean look=false;

    private RelativeLayout mLoadingBar;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qlab_certification_fragment, container, false);
        gv = view.findViewById(R.id.certification_gv);
        total_tv = view.findViewById(R.id.cert_total_tv);
        next_but = view.findViewById(R.id.cert_next_but);
        mLoadingBar = view.findViewById(R.id.loading_bar_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading


        String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_New_Certification";
        qlab_certification_Fragment.cert_item getJson = new qlab_certification_Fragment.cert_item();
        getJson.execute(url);
        next_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chick == 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("ERROR: 必須至少選擇一個")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                    String img = "";
                    String logo = "";
                    String expense = "";
                    String time = "";
                    String seqNo = "";
                    String F_RWorkID = "";
                    for (int i = 0; i < total; i++) {
                        if (arrayList.get(i).getmod().equals("1")) {
                            img += url_arr.get(i) + ",";
                            logo += arrayList.get(i).getLogo() + ",";
                            expense += arrayList.get(i).getExpense() + ",";
                            time += arrayList.get(i).getTime() + ",";
                            seqNo +=seqNo_arr.get(i)+";";
                            F_RWorkID+=F_RWorkID_arr.get(i)+",";
                        }
                    }
                    Intent intent = new Intent(getActivity(), qlab_certification.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("img", img);
                    bundle.putString("logo", logo);
                    bundle.putString("expense", expense);
                    time = time.replaceAll("週","");
                    bundle.putString("time", time);
                    bundle.putString("seqNo", seqNo);
                    bundle.putString("F_RWorkID", F_RWorkID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    public class cert_item extends AsyncTask<String, Void, String> {
        String data = "";
        String img;
        String logo;
        String cert_class;
        String expense;
        String time;

        String seqNo;

        @Override
        protected String doInBackground(String... urlStrings) {
            try {
               expense_total=0;
               chick =0;
                img_arr.clear();
                logo_arr.clear();
                cert_class_arr.clear();
                expense_arr.clear();
                time_arr.clear();
                url_arr.clear();
                seqNo_arr.clear();
                arrayList.clear();
                F_RWorkID_arr.clear();
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
                    img = "http:" + IssueData.getString("F_Cer_Pic");
                    logo = IssueData.getString("F_Cer_Logo");
                    cert_class = IssueData.getString("F_Cer_Class");
                    expense = IssueData.getString("F_Cer_Expense");
                    time = IssueData.getString("F_Cer_Time");
                    seqNo = IssueData.getString("F_SeqNo");
                    F_RWorkID_arr.add(IssueData.getString("F_RWorkID"));
                    url_arr.add(img);
                    logo_arr.add(logo);
                    cert_class_arr.add(cert_class);
                    expense_arr.add(expense);
                    time_arr.add(time);
                    seqNo_arr.add(seqNo);
                    arrayList.add(new qlab_cert_map(img, logo, cert_class, expense, time+"週", "0"));
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
            final qlab_cert_Adapter adapter = new qlab_cert_Adapter(getActivity(), arrayList);
            gv.setAdapter(adapter);
            total_tv.setText("小計" );
            mLoadingBar.setVisibility(View.GONE);//gone loading
        }
    }

    //處裡圖片
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

    //adapter
    public class qlab_cert_Adapter extends ArrayAdapter<qlab_cert_map> {
        private final Activity context;

        //建構式
        public qlab_cert_Adapter(Activity context, ArrayList<qlab_cert_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_certification_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final qlab_cert_map value = getItem(position);
            //找到list_item.xml中的TextView()
            ImageView img = listItemView.findViewById(R.id.cert_img);
            Glide.with(getContext())
                    .load(value.getImg())
                    .error(R.drawable.ic_image)//load失敗的Drawable
                    .placeholder(R.drawable.ic_image)//loading時候的Drawable
                    .into(img);

            TextView icom = listItemView.findViewById(R.id.cert_icon);
            icom.setText(value.getCert_class());

            TextView logo = listItemView.findViewById(R.id.cert_logo);
            logo.setText(value.getLogo());

            TextView expense = listItemView.findViewById(R.id.cert_expense);
            expense.setText("費用: " + value.getExpense());

            TextView time = listItemView.findViewById(R.id.cert_time);
            time.setText("工時: " + value.getTime());

            LinearLayout ll = listItemView.findViewById(R.id.cert_list_ll);
            final TextView mod = listItemView.findViewById(R.id.cert_mod);
            if (value.getmod().equals("0")) {
                mod.setBackgroundResource(R.drawable.ic_adjust);
            } else {
               mod.setBackgroundResource(R.drawable.ic_check);
            }
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (value.getmod().equals("0")) {
                        mod.setBackgroundResource(R.drawable.ic_check);
                        expense_total = expense_total + Integer.valueOf(expense_arr.get(position));
                        chick++;
                       value.mod ="1";
                        total_tv.setText("費用:USD.$" + expense_total + ",總數量:" + chick);
                    } else {
                        mod.setBackgroundResource(R.drawable.ic_adjust);
                        expense_total = expense_total - Integer.valueOf(expense_arr.get(position));
                        chick--;
                        value.mod ="0";
                        total_tv.setText("費用:USD.$" + expense_total + ",總數量:" + chick);

                    }
                }
            });




            return listItemView;
        }
    }


    //map
    public class qlab_cert_map {
        private String img;
        private String logo;
        private String cert_class;
        private String expense;
        private String time;
        private String mod;

        private qlab_cert_map(String toimg, String tologo, String tocert_class, String toexpense, String totime, String tomod) {
            this.img = toimg;
            this.logo = tologo;
            this.cert_class = tocert_class;
            this.expense = toexpense;
            this.time = totime;
            this.mod = tomod;
        }

        public String getImg() {
            return img;
        }

        public String getLogo() {
            return logo;
        }

        public String getCert_class() {
            return cert_class;
        }

        public String getExpense() {
            return expense;
        }

        public String getTime() {
            return time;
        }

        public String getmod() {
            return mod;
        }
    }
    public void onPause() {
        look = true;
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        if(look) {
            mLoadingBar.setVisibility(View.VISIBLE);
            String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_New_Certification";
            qlab_certification_Fragment.cert_item getJson = new qlab_certification_Fragment.cert_item();
            getJson.execute(url);
            look =false;
        }
    }


}
