package com.example.msiqlab;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class qlab_laboratory_Fragment extends Fragment {
    ArrayList<qlab_laboratory_map> arrayList = new ArrayList<>();
    ArrayList<String> con_arr = new ArrayList<>();
    ArrayList<String> img_arr = new ArrayList<>();
    ArrayList<String> masterid_arr = new ArrayList<>();
    private RelativeLayout mLoadingBar;

    ListView lv;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qlab_laboratory, container, false);
        lv = view.findViewById(R.id.qlab_laboratory_lv);
        TextView visit = view.findViewById(R.id.visit_but_tv);
        mLoadingBar = view.findViewById(R.id.loading_bar_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading

        String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_LabCenter_List";
        qlab_laboratory_Fragment.qlab_Find_LabCenter_List getNetworkJson = new qlab_laboratory_Fragment.qlab_Find_LabCenter_List();
        getNetworkJson.execute(url);

        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), qlab_lab_visit.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public class qlab_Find_LabCenter_List extends AsyncTask<String, Void, String> {
        String data = "";

        int total = 0;
        String title;
        String con;
        String img_url;

        @Override
        protected String doInBackground(String... urlStrings) {
            try {
                con_arr.clear();
                img_arr.clear();
                masterid_arr.clear();
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

                    title = IssueData.getString("F_Title");
                    if (!title.contains("室")) {
                        continue;
                    }
                    masterid_arr.add(IssueData.getString("F_MasterID"));
                    con = IssueData.getString("F_PicIntro");
                    con = con.replaceAll("<h3>", "");
                    con = con.replaceAll("</h3>", "");
                    con = con.replaceAll("<p>", "");
                    con = con.replaceAll("</p>", "");
                    con = con.replaceAll("<p class=\"cert-items\">", "");
                    con = con.replaceAll(" ", "");
                    con_arr.add(con);
                    con = con.replaceAll("<br>", "");
                    con = con.replaceAll("<br/>", "");
                    if (con.length() > 50) {
                        con = con.substring(0, 50);
                    }
                    img_url = "http://172.16.98.4/Code/LabCenter/" + IssueData.getString("F_Pic");
                    img_arr.add(img_url);

                    arrayList.add(new qlab_laboratory_map(title, con, img_url));
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
            qlab_laboratory_Adapter adapter = new qlab_laboratory_Adapter(getActivity(), arrayList);
            lv.setAdapter(adapter);
            mLoadingBar.setVisibility(View.GONE);//gone loading

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), qlab_laboratory_page.class);
                    Bundle bundle = new Bundle();
                    String title = arrayList.get(position).tital;
                    String con = con_arr.get(position);
                    String img = img_arr.get(position);

                    String masterid = masterid_arr.get(position);
                    bundle.putString("title", title);
                    bundle.putString("con", con);
                    bundle.putString("img", img);
                    bundle.putString("masterid", masterid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    //map
    public class qlab_laboratory_map {
        //大標題
        private String tital;
        //內容
        private String conn;
        //照片
        private String photo;


        //建構式
        public qlab_laboratory_map(String totital, String toconn, String tophoto) {
            tital = totital;
            conn = toconn;
            photo = tophoto;
        }

        public String gettital() {
            return tital;
        }

        public String getconn() {
            return conn;
        }

        public String getPhoto() {
            return photo;
        }

    }

    //adapter
    public class qlab_laboratory_Adapter extends ArrayAdapter<qlab_laboratory_map> {
        private final Activity context;
        ImageView img;

        //建構式
        public qlab_laboratory_Adapter(Activity context, ArrayList<qlab_laboratory_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_laboratory_list_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final qlab_laboratory_map value = getItem(position);
            //找到list_item.xml中的TextView()
            TextView tital = listItemView.findViewById(R.id.qlab_lab_list_tital);
            tital.setText(value.gettital());

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
    }


}
