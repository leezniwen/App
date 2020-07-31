package com.example.msiqlab;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import java.util.InputMismatchException;

public class news_Fragment extends Fragment {
    public static ListView lv;
    public static ArrayAdapter adapter;
    public static String[] news_no = {"", "", "", "", ""};
    public static ArrayAdapter adapter1;
    TextView news_tv ;
    TextView error_tv ;
    private RelativeLayout mLoadingBar;

    private boolean lookout = false;

    int touch=0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.news, container, false);
        lv = view.findViewById(R.id.news_lv);
        mLoadingBar = view.findViewById(R.id.news_loading_rl);
        news_tv =view.findViewById(R.id.news_tv);
        error_tv =view.findViewById(R.id.error_tv);
        error_tv.setVisibility(View.GONE);

        lv.setVisibility(View.GONE);

        news_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touch++;
                if(touch%2==0){
                    lv.setVisibility(View.GONE);
                }else {
                    lv.setVisibility(View.VISIBLE);
                    mLoadingBar.setVisibility(View.VISIBLE);
                    HttpsTrustManager.allowAllSSL();//憑證
                    String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_News";
                    final news_value getNetworkJson = new news_value();
                    getNetworkJson.execute(url);
                }

            }
        });

        TextView more = view.findViewById(R.id.news_more_tv);
        Button littlesupper = view.findViewById(R.id.littlesupper_but);
        littlesupper.setOnClickListener(littlesuppercuick);
        more.setOnClickListener(morechick);
        final Button video =view.findViewById(R.id.video_but);
        final Button facebook =view.findViewById(R.id.facebook_but);
        final Button checkin_but =view.findViewById(R.id.checkin_but);
        final Button checkin_but2 =view.findViewById(R.id.checkin_but2);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //開啟豬Qa的youtube
                WebView web = new WebView(getActivity());
                web.loadUrl(" https://www.youtube.com/channel/UCmORzj3VQ_nqs-xX_xCUbwA");
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            //開啟豬Qa的facebook
            @Override
            public void onClick(View v) {
                WebView web = new WebView(getActivity());
                web.loadUrl("https://www.facebook.com/DqaPG/?modal=admin_todo_tour");
            }
        });

        checkin_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),checkin.class);
                getActivity().startActivity(intent);
            }
        });
        checkin_but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),checkin2.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    private View.OnClickListener littlesuppercuick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), littlesupper_main.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener morechick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), news_list.class);
            Bundle bundle =new Bundle();
            bundle.putString("key","");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), news_page.class);
            String str = news_no[position];

            Bundle bun = new Bundle();
            bun.putString("news_no", str);
            intent.putExtras(bun);
            getActivity().startActivity(intent);
        }
    };

    public class news_value extends AsyncTask<String, Void, String> {
        String data = "";
        String news_title = "";
        String news_time = "";
        String news_value = "";

        String[] str = {"", "", "", "", ""};
        String[] news_no = {"", "", "", "", ""};
        int error =0;

        @Override
        protected String doInBackground(String... urlStrings) {
            try {
                URL url = new URL(urlStrings[0]); //初始化
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection(); //取得連線之物件
                HttpsTrustManager.allowAllSSL();//信任所有证书，信任憑證

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
                for (int i = 0; i < 5; i++) {
                    JSONObject IssueData = UserArray.getJSONObject(i);
                    news_title = IssueData.getString("News_title");
                    news_time = IssueData.getString("Column1");
                    news_value = String.valueOf(IssueData.getInt("News_No"));
                    news_no[i] = news_value;
                    str[i] = news_time + "\n" + news_title;
                }
            } catch (InputMismatchException e) {
                e.printStackTrace();
                error++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                error++;
            } catch (IOException e) {
                e.printStackTrace();
                error++;
            } catch (JSONException e) {
                e.printStackTrace();
                error++;
            }
            return data;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(data);
            mLoadingBar.setVisibility(View.GONE);//gone loading
            error_tv.setVisibility(View.GONE);
            if(error >0){
                linkerror();
            }
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
            for (int i = 0; i < 5; i++) {
                news_Fragment.adapter.add(str[i]);
                if (news_no[i] == null) {
                    news_no[i] = "ERROR";
                }
                news_Fragment.news_no[i] = news_no[i];
            }

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(onClickListView);
        }

    }

    public void onPause(){
        super.onPause();
        lookout =true;
    }
    public void onResume(){
        super.onResume();
        if(lookout){
            mLoadingBar.setVisibility(View.VISIBLE);
            String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_News";
            final news_value getNetworkJson = new news_value();
            getNetworkJson.execute(url);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(onClickListView);
            lookout =false;
        }
    }

    public void linkerror() {
        mLoadingBar.setVisibility(View.GONE);//gone loading
        error_tv.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "連線異常，伺服器連線中斷", Toast.LENGTH_SHORT).show();
    }

}
