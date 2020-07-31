package com.example.msiqlab;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.InputMismatchException;

public class login_main extends AppCompatActivity {
    UserData UserDataClass = new UserData();
    private static final int REQUEST_CODE = 1;
    private static String login;
    private static String account;
    private static String passwd;
    public static String EName = "";
    public static boolean chick_login = false;

    private static EditText id_et;
    private static EditText pwd_et;

    private RelativeLayout mLoadingBar;

    TextView error;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        id_et = findViewById(R.id.login_account_et);
        pwd_et = findViewById(R.id.login_password_et);
        error =findViewById(R.id.login_error_tv);
        final Button login_but = findViewById(R.id.login_but);
        login_but.setOnClickListener(onclick);
        //縮鍵盤1
        LinearLayout login_main_r = findViewById(R.id.login_ll);//Relative lay out  登入頁面
        login_main_r.setOnTouchListener(new View.OnTouchListener() {  //Relative lay out  點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(id_et.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pwd_et.getWindowToken(), 0);

                return false;
            }
        });

        mLoadingBar = findViewById(R.id.login_rl);

    }
    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HttpsTrustManager.allowAllSSL();//憑證
            EditText id_et = findViewById(R.id.login_account_et);
            EditText pwd_et = findViewById(R.id.login_password_et);
            account = id_et.getText().toString();
            passwd = pwd_et.getText().toString();
            if (account.equals("")) {
                Toast.makeText(login_main.this, "帳號不得空白", Toast.LENGTH_SHORT).show();
            } else if (passwd.equals("")) {
                Toast.makeText(login_main.this, "密碼不得空白", Toast.LENGTH_SHORT).show();
            } else {
                mLoadingBar.setVisibility(View.VISIBLE);//show loading
                chick();
            }
        }
    };

    private boolean chick() {
        String url = "https://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/AuthenticateWTSC?OutlookID=" + account + "&OutlookPassword=" + passwd;
        login_value getNetworkJson = new login_value();
        getNetworkJson.execute(url);
        return chick_login;
    }

    public class login_value extends AsyncTask<String, Void, String> {
        String data = "";//一定要預設不然會有錯誤
        String CName;
        String EName = "";
        String Wid;

        @Override
        protected String doInBackground(String... urlStrings) {
            try {
                URL url = new URL(urlStrings[0]); //初始化
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //取得連線之物件
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                // Simulate network access.
                Thread.sleep(2000);
                InputStream inputStream = httpURLConnection.getInputStream();
                //對取得的資料進行讀取
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    if (line == " ") {
                        break;
                    }
                    data = data + line;
                }
                JSONObject jsonObject = new JSONObject(data);
                JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));
                JSONObject IssueData = UserArray.getJSONObject(0);
                CName = IssueData.getString("ChineseName");
                EName = IssueData.getString("EnglishName");
                Wid = IssueData.getString("WorkID");
                if (EName != "") {
                    login_main.chick_login = true;
                }
                String DeptID = String.valueOf(UserArray.getJSONObject(0).getInt("DeptID"));

                String F_OrgID = UserArray.getJSONObject(0).getString("F_OrgID");

                String Account = UserArray.getJSONObject(0).getString("EnglishName");

                String Email = UserArray.getJSONObject(0).getString("Email");

                String Password = "";

                String WorkID = UserArray.getJSONObject(0).getString("WorkID");

                String Name = UserArray.getJSONObject(0).getString("ChineseName");

                String Phone = UserArray.getJSONObject(0).getString("Tel");

                String Dept = UserArray.getJSONObject(0).getString("DeptName");

                String WebFlowBoss = UserArray.getJSONObject(0).getString("WebFlowBoss");

                String WebFlowBossName = UserArray.getJSONObject(0).getString("WebFlowBossName");

                String WebFlowBossTel = UserArray.getJSONObject(0).getString("WebFlowBossTel");

                String Region = UserArray.getJSONObject(0).getString("Region");

                String LastTab = "";

                UserDataClass = new UserData(DeptID, F_OrgID, Account, Email, Password, WorkID, Name, Dept, Phone, EName, WebFlowBoss, WebFlowBossName, WebFlowBossTel, Region, LastTab);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("1");
                again();
            } catch (InputMismatchException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
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
            mLoadingBar.setVisibility(View.GONE);//gone loading
            login_main.EName = EName;
            if (EName.equals("")) {
                again();
            } else {
                logon();
                UserDB UserDB = new UserDB(getApplicationContext());

                UserDB.insert(UserDataClass);

                UserData UserData = new UserData();

                UserData = UserDB.getAll().get(0);
            }
        }
    }

    private void logon() {
        if (UserData.Region.contains("MSIT")) {
            Intent intent = new Intent(login_main.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            again();
        }
    }

    private void again() {
        Intent intent = new Intent(login_main.this, login_main.class);
        Toast.makeText(login_main.this, "帳號或密碼有誤", Toast.LENGTH_SHORT).show();
        Bundle bundle =new Bundle();
        bundle.putString("errorname",account);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        // 鎖住Back鍵
        return;
    }

}
