package com.example.msiqlab;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class littlesupper_assist_Schedule {

    public Boolean result(String las_F_SeqNo){
        littlesupper_Time_reponse time_reponse = new littlesupper_Time_reponse();
        String data = "";
        Boolean decide = true ;
        String start_booking = time_reponse.getStart_time();
        String end_booking = time_reponse.getEnd_time();
        String sch_url = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_Fac_Schedule_List?F_Master_ID="+las_F_SeqNo;
        int total = 0 ;
        System.out.println("編號"+ sch_url);
        try {
            URL url = new URL(sch_url); //初始化
            HttpURLConnection httpURLConnection =
                    (HttpURLConnection) url.openConnection(); //取得連線之物件
            InputStream inputStream = httpURLConnection.getInputStream();
            //對取得的資料進行讀取
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));

            for(int i =0 ; i < UserArray.length() ; i++){
                JSONObject IssueData = UserArray.getJSONObject(i);
                String buy_time = IssueData.getString("F_StartDate");
                String ApplierSDate = "";
                for (int j = 0; j < buy_time.indexOf("T"); j++) {
                    char c = buy_time.charAt(j);
                    ApplierSDate += c;
                }
                String Storage_time = IssueData.getString("F_EndDate");
                String ApplierEDate = "";
                for (int j = 0; j < Storage_time.indexOf("T"); j++){
                    char c = Storage_time.charAt(j);
                    ApplierEDate += c;
                }
                if(time_reponse.pk_time(start_booking , end_booking , ApplierSDate , ApplierEDate)){
                    // 如果這裡時間沒衝突到就不做事情
                }
                else
                {
                    //時間衝突直接回傳有衝突 也就是false
                    return false;
                }
            }
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true ;
    }
}
