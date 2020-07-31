package com.example.msiqlab;

import android.util.Log;

public class little_supper_supper_database {
    String[] Action1 = new String[]{"看", "我的預約", "預約"};//第一個執行動作
    String[] Action2_time = new String[]{"今天", "明天", "今年", "明年", "這個月", "下個月"};//日期時間 看有沒有
    String[] Action2_time_1 = new String[]{"今天", "昨天", "上個月"};//日期新聞格式
    String[] Action2_time_2 = new String[]{"年", "月", "號"}; //日期 自訂格式
    String[] Action2_1 = new String[]{"機構", "環測", "熱流", "無響室", "電子", "音像", "掃地機"};//設備
    String[] Action3 = new String[]{"聲學", "半無響", "環測", "熱流", "機構"};//實驗室
    String keywords; //這邊要抓取關鍵字回傳

    public String little_supper_supper_database_news(String content) {
        if (content.indexOf("關於") > -1 && content.indexOf("的") > -1) {
            int start = content.indexOf("關於");
            int end = content.indexOf("的");
            keywords = content.substring(start + 2, end);
            Log.d("關鍵字", keywords);
            return "news_key:" + keywords;
        } else {
            keywords = Calender_time(content);
            return "news_time:" + keywords;
        }
    }

    public String little_supper_supper_database_machine(String content) {
        for (int i = 0; i < Action2_1.length; i++) {
            if (content.indexOf(Action2_1[i]) > -1) {
                keywords = Action2_1[i];
                return keywords;
            }
        }
        return "所有設備";
    }

    public String little_supper_supper_database_machine_book(String content) {
        for (int i = 0; i < Action2_1.length; i++) {
            if (content.indexOf(Action2_1[i]) > -1) {
                String start_time, end_time;
                Log.d("開始", content);

                int length_s = content.indexOf("到"), length_d = content.indexOf("的"), length_origin = content.indexOf("預約");
                littlesupper_Time_reponse time_reponse = new littlesupper_Time_reponse();

                if (length_s > -1) {
                    start_time = time_reponse.VS_TIME(content.substring(length_origin, length_s));
                    end_time = time_reponse.VS_TIME(content.substring(length_s + 1, length_d));

                    time_reponse.set_start_time(start_time);
                    time_reponse.set_end_time(end_time);
                } else if (length_d > -1) {


                    start_time = time_reponse.VS_TIME(content.substring(length_origin, length_d));
                    end_time = start_time;

                    time_reponse.set_start_time(start_time);
                    time_reponse.set_end_time(end_time);
                    Log.d("結束", time_reponse.getEnd_time());
                }

                keywords = Action2_1[i];
                return keywords + "預約";
            }
        }
        String start_time, end_time;
        Log.d("開始", content);

        int length_s = content.indexOf("到"), length_d = content.indexOf("的"), length_origin = content.indexOf("預約");
        littlesupper_Time_reponse time_reponse = new littlesupper_Time_reponse();

        if (length_s > -1) {
            start_time = time_reponse.VS_TIME(content.substring(length_origin, length_s));
            end_time = time_reponse.VS_TIME(content.substring(length_s + 1, length_d));
            time_reponse.set_start_time(start_time);
            time_reponse.set_end_time(end_time);
        } else if (length_d > -1) {
            start_time = time_reponse.VS_TIME(content.substring(length_origin, length_d));
            end_time = start_time;
            time_reponse.set_start_time(start_time);
            time_reponse.set_end_time(end_time);
            Log.d("結束", time_reponse.getEnd_time());
        }

        return "所有預約設備";
    }

    public String little_supper_supper_database_lab(String content) {
        if (content.contains("預約")||content.contains("參觀")) {
            return "預約實驗室";
        } else {
            return "所有實驗室";
        }
    }


    public String Calender_time(String news_or_machine) {
        //這邊的主要工作是要把日期轉成long去看
        int count = 1;
        ; //順序

        String time;

        if (news_or_machine.indexOf("新聞") > -1) {
            count = 0;
        }
        if (count == 0) {
            //專門給新聞的
            littlesupper_Assist assist = new littlesupper_Assist();
            time = assist.get_DATA(news_or_machine);
            return time;
        }
        return "";
    }
}
