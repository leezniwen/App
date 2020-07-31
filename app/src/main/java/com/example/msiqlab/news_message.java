package com.example.msiqlab;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;

public class news_message extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_message);
        Button go_back = findViewById(R.id.go_back_but);
        go_back.setOnClickListener(go_back_onclick);
        Button message = findViewById(R.id.go_message_but);
        message.setOnClickListener(go_message_onclick);
    }

    View.OnClickListener go_message_onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(news_message.this)
                    .setTitle("確定留言?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = findViewById(R.id.message_et);
                            Bundle bundle = getIntent().getExtras();
                            String news_no = bundle.getString("news_no");
                            String user = UserData.Name;
                            String message_conn = editText.getText().toString();
                            if(!message_conn.equals("")){
                               String url = "https://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Insert_Board?News_No=" + news_no + "&board_person=" + user + "&board_content=" + message_conn;
                               message_value getNetworkJson = new message_value();
                               getNetworkJson.execute(url);
                            }
                            finish();
                        }
                    })
                    .setNegativeButton("否", null)
                    .create()
                    .show();
        }
    };

    View.OnClickListener go_back_onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(news_message.this)
                    .setTitle("確定離開留言?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("否", null)
                    .create()
                    .show();
        }
    };

    public class message_value extends AsyncTask<String, Void, String> {
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
                    if (line == " ") {
                        break;
                    }
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
            finish();

        }
    }
}