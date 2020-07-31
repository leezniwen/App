package com.example.msiqlab;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class user_Fragment extends Fragment {
    public static TextView update;
    public static TextView name;
    public static TextView workid;
    private SQLiteDatabase db;
    public static final String TABLE_NAME = "UserData";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    String Version;
    String Url;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user, container, false);
        update = view.findViewById(R.id.user_update);

        workid = view.findViewById(R.id.user_WID);
        workid.setText("工號: "+UserData.WorkID);
        name = view.findViewById(R.id.user_EN);
        name.setText(UserData.Name + "(" + UserData.EName + ")");
        TextView logout = view.findViewById(R.id.user_logout);
        logout.setOnClickListener(logout_click);
        TextView user_question = view.findViewById(R.id.user_question);
        TextView user_update = view.findViewById(R.id.user_update);
        user_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Get_Version_last?type=Android";
                user_Fragment.getversionCode getNetworkJson = new user_Fragment.getversionCode();
                getNetworkJson.execute(url);

            }
        });
        user_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail();
            }
        });
        return view;
    }

    private TextView.OnClickListener logout_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Dialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("確定是否登出?")
                    .setPositiveButton("登出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //先刪除資料表
                            String deleteTable0 = "delete from " + TABLE_NAME;    //刪除UserDB
                            db = DBHelper.getDatabase(getActivity());
                            db.execSQL(deleteTable0);//刪除資料表
                            db.close();
                            news_Fragment.adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
                            news_Fragment.lv.setAdapter(news_Fragment.adapter1);
                            Intent intent = new Intent(getActivity(), login_main.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        }
    };

    public int getversionCode() {
        int versionCode = 0;
        try {
            versionCode = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public class getversionCode extends AsyncTask<String, Void, String> {
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
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                JSONObject jsonObject = new JSONObject(data);
                JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));
                JSONObject IssueData = UserArray.getJSONObject(0);
                Version = IssueData.getString("Version");
                Url = IssueData.getString("Install_Url");
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

        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            int versionCode = getversionCode();

            if (Integer.valueOf(Version) != versionCode) {
                //showDialog(); Google play的更新
                int permission = ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 無權限，向使用者請求
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE
                    );

                } else {
                    UpdateManager  mUpdateManager = new UpdateManager(getActivity(),Url);
                    mUpdateManager.checkUpdateInfo();
                }

            }else{
                Toast.makeText(getActivity(), "目前為最新版本", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void mail() {
        //指定與電子郵件相關的變量值
        String[] emails = new String[]{
                "albertchien@msi.com", "jefflee@msi.com"
        };
        String mailSubject = "Android msiQlab 問題回報";
        String mailBody = "問題描述 :";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, mailBody);
        intent.setData(Uri.parse("mailto:"));
        startActivity(intent);
    }

}
