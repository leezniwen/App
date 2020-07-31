package com.example.msiqlab;

import android.util.Log;

public class little_supper_supper {
    String content;  // 使用者文字全抓取
    String Keywords; //關鍵字

    public String getContent(String content) {
        this.content = content;  //文字的輸入內容
        little_supper_supper_database db = new little_supper_supper_database(); // 資料小助理 內部的文字庫

        try {
            Log.d("lss", content);
            if (content.indexOf("新聞") > -1){
                Keywords = db.little_supper_supper_database_news(content);
                return Keywords;
            } else if (content.indexOf("設備") > -1 && content.indexOf("預約")==-1) {
                Keywords = db.little_supper_supper_database_machine(content);
                return Keywords;

            }else if (content.indexOf("配備") > -1 && content.indexOf("預約")==-1) {
                Keywords = db.little_supper_supper_database_machine(content);
                return Keywords;
            }else if (content.indexOf("實驗室") > -1) {
                Keywords = db.little_supper_supper_database_lab(content);
                return Keywords;
            }else if (content.indexOf("我的預約") > -1) {
                return "我的預約";
            }else if (content.indexOf("我的認證") > -1) {
                return "我的預約";
            } else if (content.indexOf("你好") > -1) {
                return "你好~~~~ \n 請問您想要「查看新聞」、「查看開放預約的設備」 或是「我的預約」呢? ";
            }else if (content.indexOf("使用") > -1) {
                return "你好~~~~ \n 如果您想要「查看新聞」可以告訴小助理您想要查看關於什麼的新聞，或是哪一天的新聞。" +
                        "\n\n如果您想要查看您預約的設備以及您申請的認證可以告訴小助理「查看我的預約」或「查看我的認證」。" +
                        "\n\n如果您想要查看目前可以預約的設備可以告訴小助理「查看設備」或「預約某一天到某一天的配備」。";
            }
            else if ((content.indexOf("設備") > -1||content.indexOf("配備") > -1) && content.indexOf("預約")>-1) {
                Keywords = db.little_supper_supper_database_machine_book(content);
                return Keywords;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "不好意思，小助理沒有聽懂 \n 請問您想要「查看新聞」、「查看開放預約的設備」 或是「我的預約」呢?\n如果不知道怎麼使用可以問小助理「如何使用」";
    }

}
