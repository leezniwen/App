package com.example.msiqlab;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class littlesupper_Assist {

    ArrayList result_Assist = new ArrayList(); //原先是String result;



    public String get_DATA(String getdata) {  //原先ArrayList 是string
        String enter = getdata; // 抓到資料第一關要判斷的地方
        String result_Assist_string = "";
            //if(enter.indexOf("今年") ==-1 ||enter.indexOf("這個月") ==-1 ){
            //    result_Assist.add("目前機器人不提供查詢整年新聞或是整個月的新聞QQQQQ eX要看當月某日新聞直接說幾號或日就好") ;
            //    return result_Assist_string;
            //}

            if(enter.indexOf("你好") >-1 || enter.indexOf("哈囉")>-1){
                 result_Assist_string = ("你好～我是你的助理")+"\n"+("目前能處理的是 [ 新聞 ]、[ 實驗室配備名稱(簡易) ]、[實驗室配備的預約狀況]")
                        +"\n"+"你也可以指定某個配備[ Ex. 我想看機構配備有哪些?]"
                        +"\n"+"你也可以問某天的新聞Ex [我想看2月的新聞]，我會列出2月所有的新聞"
                        +"\n"+"最後你也能說 [查看配備六大項]";
                 return result_Assist_string;
            }

            else if( enter.indexOf("看")>-1 && enter.indexOf("配備")>-1 ){
                result_Assist_string = find_lab_Machine(getdata);
                result_Assist.add(result_Assist_string);
            }


            else if ((enter.indexOf("尋") > -1 || enter.indexOf("查") > -1 || enter.indexOf("看") > -1) && (enter.indexOf("新聞") > -1)) { //因新聞沒有新增功能所以只能用查看的
                if(enter.indexOf("星期")> -1 || enter.indexOf("禮拜")> -1 ){
                    result_Assist_string = ("目前機器人未提供以星期 or 禮拜來做查詢");
                }
                else {
                    result_Assist_string = find(getdata);
                    result_Assist.add(result_Assist_string);
                }
            }


            else
            {
                result_Assist_string = ("嗶哩嗶哩....目前無法辨認你所說的  目前只開放 [新聞] 和 [實驗室查看] 可以換另一種問法嗎(Sorry) Ex 想看 ..... 或是 想預約...之類的") ;
            }






        return result_Assist_string;

    }
    private String get_result_Array(int i){
        return result_Assist.get(i).toString();
    }



    protected String find(String find_data) { // 找尋新聞 ? 找尋實驗室?? 找尋是不是有人預約??  // 原先是String 轉 Arraylist
        Boolean into = true;  //這個是看是否可以直接進到預設

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");  //用成系統時間  格式
        String[] week = {"日", "一", "二", "三", "四", "五", "六"}; //用陣列表示星期

        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DATE, 0);
        Date date = ca.getTime();
        String How_day = "";
        int today_week = ca.get(Calendar.DAY_OF_WEEK)-1;
        Log.d("星期",today_week+"");
        String now_year = "";
        String now_month = "";
        String now_day = "";
        List date_list = new ArrayList<>();  //專門取得今天的日期   v 確定沒問題
        String find_result="" ;  //原本是String 轉成 Arraylist
        //要先訂起來是星期幾的
        String it_this = "";//這個是定位後的星期
        if(find_data.indexOf("昨天")> -1 || find_data.indexOf("明天")> -1){
            if(find_data.indexOf("昨天")>-1){
                ca.add(Calendar.DATE , -1);
                date = ca.getTime();
                How_day = simpleDateFormat.format(date);
                Log.d("How_day" , How_day);
                date_list.add(How_day);
                into = false ;
            }
            if(find_data.indexOf("明天")>-1){
                ca.add(Calendar.DATE , +1);
                date = ca.getTime();
                How_day = simpleDateFormat.format(date);
                Log.d("How_day" , How_day);
                date_list.add(How_day);
                into = false;
            }

        }
        if(find_data.indexOf("年")==-1 || find_data.indexOf("月")==-1 || find_data.indexOf("日")==-1){
            into = false ;
        }

        if(find_data.indexOf("今天")==-1 && into == true && find_data.indexOf("號")==-1 && find_data.indexOf("今年")==-1 &&find_data.indexOf("這個月")==-1 && find_data.indexOf("這周")==-1 ){  //先定今天是星期幾
            for (int i = 0; i < find_data.length(); i++) {
                if (find_data.indexOf(week[i]) > -1) {
                    it_this = week[i].toString();
                    break;
                }
            }
        }



        //模糊方式去說
        if(find_data.indexOf("明年") > -1){
            int year =  ca.get(Calendar.YEAR)+1;
            date_list.add(year);
            Log.d("時間" , date_list.get(0)+"");
        }

        if(find_data.indexOf("今年") > -1){
            int year =  ca.get(Calendar.YEAR);
            date_list.add(year);
            Log.d("時間" , date_list.get(0)+"");
        }
        if(find_data.indexOf("去年") > -1){
            int year =  ca.get(Calendar.YEAR)-1;
            date_list.add(year);
            Log.d("時間" , date_list.get(0)+"");
        }

        if(find_data.indexOf("上個月") > -1){
            int year =  ca.get(Calendar.YEAR);
            String month =ca.get(Calendar.MONTH)+"";
            month = "0"+ month;
            if(Integer.valueOf(month) > 10){
                month = month.substring(1,2);  //處理 大於10會變成010
            }
            date_list.add(year+month);
            Log.d("時間" , date_list.get(0)+"");
        }

        if(find_data.indexOf("這個月") > -1){
            int year =  ca.get(Calendar.YEAR);
            String month =ca.get(Calendar.MONTH)+1+"";
            month = "0"+ month;
            if(Integer.valueOf(month) > 10){  //處理 大於10會變成010
                month = month.substring(1,2);
            }
            date_list.add(year+month);
            Log.d("時間" , date_list.get(0)+"");
        }

        if(find_data.indexOf("下個月") > -1){
            int year =  ca.get(Calendar.YEAR);
            String month =ca.get(Calendar.MONTH)+2+"";
            month = "0"+ month;
            if(Integer.valueOf(month) > 10){
                month = month.substring(1,2);  //處理 大於10會變成010
            }
            date_list.add(year+month);
            Log.d("時間" , date_list.get(0)+"");
        }


        if (find_data.indexOf("今天") > -1) {
             How_day=(simpleDateFormat.format(date));
             date_list.add(How_day);
        }


        if (find_data.indexOf("星期") > -1 ) {  //接收星期幾並轉成date格式
            int count = 0;
            for (int i = today_week; i < week.length; i--) {
                if (i < 0) {
                    i += 7;
                }
                if (week[i] == it_this) {
                    break;
                }
                count++;
            }
            ca.add(Calendar.DATE, -count);
            date = ca.getTime();
            How_day = simpleDateFormat.format(date);
            date_list.add(How_day);
        }

        //模糊方式

        //精準日期

        if(find_data.indexOf("年")>-1){
            for(int i = ca.get(Calendar.YEAR)-10 ; i < ca.get(Calendar.YEAR)+20 ;i++){
                String year = i +"";
                if(find_data.indexOf(year)>-1){
                    now_year = year ;
                    How_day = How_day + now_year ;
                    Log.d("年分" , How_day);
                    break;
                }
            }
        }
        else
            now_year= ca.get(Calendar.YEAR)+"";

        if(find_data.indexOf("月")>-1){
            for(int i= 12 ; i > 0 ; i-- ){
                if(find_data.indexOf( i+"月")>-1){
                    if(i<10){
                        now_month = "0"+i ;
                        How_day = now_month+"";
                        break;
                    }
                    now_month = i+"" ;
                    How_day = now_month+"";
                    break;
                }
            }

        }
        else  if(find_data.indexOf("號")>-1 || find_data.indexOf("日")>-1)   {
            now_month= "0" + (ca.get(Calendar.MONTH)+1)+"";
        }


        for(int i= 31 ; i > 0 ; i-- ){
            if( find_data.indexOf( i+"號")>-1 || find_data.indexOf( i+"日")>-1){
                if(i<10){
                    now_day = "0"+i ;
                    How_day = now_day+"";
                    break;
                }
                now_day = i+"" ;
                How_day = now_day+"";
                break;
            }
        }



        date_list.add(now_year + now_month + now_day);
        Log.d("250:>" ,date_list.get(0)+"");
        //改變格式
        How_day = date_list.get(0).toString();
        Log.d("星期",How_day);

        if (find_data.indexOf("新聞") > -1) {
            //  時間   年月日 格式 20200302
            int date_now_into = Integer.valueOf(date_list.get(0).toString());  //這個是User輸入的時間
            int date_now = Integer.valueOf(simpleDateFormat.format(date));       //這個是今天的時間
            Log.d("使用者輸入", date_now_into + "");
            Log.d("今日時間", date_now + "");

            //從新聞判斷說這個日期是否超過今天日期
            if (date_now_into > date_now) {
                String cut_year = date_list.get(0).toString().substring(0, 4);
                String cut_month = date_list.get(0).toString().substring(4, 6);
                String cut_day = date_list.get(0).toString().substring(6, 8);

                if ((Integer.valueOf(cut_month).intValue() - 1) < 10) {
                    cut_month = "0" + ca.get(Calendar.MONTH + 1 - 1);
                }
                if ((Integer.valueOf(cut_day).intValue() > 30) && (Integer.valueOf(cut_month) == 4 || Integer.valueOf(cut_month) == 6 || Integer.valueOf(cut_month) == 9 || Integer.valueOf(cut_month) == 11)) {
                    cut_day = "30";
                }
                if ((Integer.valueOf(cut_day).intValue() > 29) && (Integer.valueOf(cut_month) == 2)) {
                    if (Integer.valueOf(cut_year).intValue() % 4 == 0) {
                        cut_day = "29";
                    } else
                        cut_day = "28";
                }

                date_list.clear();
                date_list.add(cut_year + cut_month + cut_day);
                Log.d("目標查詢時間", date_list.get(0).toString());
            }
            //find_result.add(date_list.get(0).toString());
            find_result = date_list.get(0).toString();
        }




        return find_result;

    }


    protected String find_lab_Machine(String find_lab_Macinme){
        String lab_name = find_lab_Macinme;
        if(lab_name.indexOf("機構配備")>-1){
            lab_name = "機構配備";
        }
        else if(lab_name.indexOf("環測配備")>-1){
            lab_name = "環測配備";
        }
        else if(lab_name.indexOf("熱流配備")>-1){
            lab_name = "熱流配備";
        }
        else if(lab_name.indexOf("無響室配備")>-1){
            lab_name = "無響室配備";
        }
        else if(lab_name.indexOf("電子配備")>-1){
            lab_name = "電子配備";
        }
        else if(lab_name.indexOf("音像配備")>-1){
            lab_name = "音像配備";
        }

        return lab_name;
    }


}