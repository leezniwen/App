package com.example.msiqlab;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class littlesupper_Time_reponse {
    //日期格式Ex >2020-03-21< >08:30:00< >上午<
    Calendar c = Calendar.getInstance(Locale.TAIWAN);
    int YEAR;
    int MONTH;
    int DAY;
    int Hour;
    int MINUTE;
    int sec = 00;
    String Default ; //預先設定的時間
    String Content =""; //用以抓取文字內容
    String Monring_Aftermoon = ""; //決定是上午還下午
    static String  start_time;
    static String end_time;
    public  littlesupper_Time_reponse()
    {

    }


    public String getTaiwan_time(){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(new Date());
    }

    public String setContent(String content) {
        Content = content ;
        return "OK";
    }

    public String getContent(){
        return Content;
    }



    public String get_time()
    {
        Default = getTaiwan_time();

        return Default;
    }

    //

    public String VS_TIME(String content){  //
        YEAR = 0;
        MONTH = 0 ;
        DAY = 0;
        String MORNING = "";
        String[] str_month = new String[]{"這個月","下個月"};
        String[] week = new String[]{"日","一","二","三","四","五","六"};  // 1-7  改0-6
        String[] str_day = new String[]{"今天","明天","後天"};
        String[] str_morn = new String[]{"早上","上午","下午","晚上"};
        ArrayList yyyy = new ArrayList();
        int choose = 0 ; // 預設有今天    > 如果... 有出現str_data等特殊字直接處理
        String today =get_time().substring(0,19); //今天日期預先設定 在沒有設定日期時回傳此

        //自訂時間
        Log.d("星期" , get_week()+"");
        YEAR = Integer.valueOf(get_time().substring(0,4));
        MONTH = Integer.valueOf(get_time().substring(5,7));
        DAY = Integer.valueOf(get_time().substring(8,10));
        String Hour = get_time().substring(11,13);
        String MINUTE = get_time().substring(14,16);
        //

        today = YEAR+"-"+MONTH+"-"+DAY+" "+8+":"+30+":00"; //預設今天

        Log.d("文件內容" , content);

        if(content.indexOf("禮拜")>-1 || content.indexOf("星期")>-1){
            // 數有幾個 [下]
            int mult = 0 ; //乘幾次
            int plus = 0 ; //加多少
            int total = 0; //總共加幾天
            Long time ;
            int week_chinese=get_week() ; // 星期幾? 預設為今天的星期
            for(int i = 0 ; i < content.length() ; i++){
                char key = content.charAt(i);
                if(String.valueOf(key).equals("下")){
                    mult++;
                }
            }

            for(int i = 0 ;  i < 7 ; i++){
                if(content.indexOf(week[i])>-1){
                    System.out.println(week_chinese);
                    week_chinese = i ;
                    break;
                }
            }
            plus = (week_chinese - get_week()) ;
            if(plus<0){
                plus = 7 + (plus);
            }
            total = (7*mult)+plus ;

            time = process_time(total);
            content = String.valueOf(time).substring(0,4)+"年"+String.valueOf(time).substring(4,6)+"月"+String.valueOf(time).substring(6,8)+"號";
        }

        if(content.indexOf("年")>-1) {
            if (content.indexOf("今年") > -1 || content.indexOf("明年") > -1) {
                if (content.indexOf("今年") > -1) {
                    YEAR = YEAR;
                }
                if (content.indexOf("明年") > -1) {
                    YEAR = YEAR + 1;
                }
            }
            else
            {
                for (int i = YEAR ; i < YEAR+1 ; i++){
                    if(content.indexOf(i+"年")>-1){
                        YEAR = i ;
                        break;
                    }
                }
            }
        }

        if(content.indexOf("月")>-1){
            if(content.indexOf("這個月")>-1 || content.indexOf("下個月")>-1){
                if(content.indexOf("這個月")>-1){
                    MONTH = MONTH ;
                }
                if(content.indexOf("下個月")>-1){
                    MONTH = MONTH+1 ;
                }
            }
            else {
                for(int i = 1 ; i <= 12 ; i++){
                    if(content.indexOf(i+"月")>-1){
                        if(i < MONTH && content.indexOf("年")==-1){
                            YEAR+=1;
                            MONTH = 1;
                        }
                        MONTH = i ;
                    }
                }
            }

        }





        Log.d("幾號" , DAY+"");
        Log.d("幾月" , MONTH+"");

        if(content.indexOf("號")>-1){
            int day = 1 ;
            if(MONTH==1 || MONTH==3 || MONTH==5 || MONTH==7 || MONTH==8 || MONTH==10 || MONTH==12 ){
                 day = 31;
            }
            else if(MONTH==4 || MONTH==6 || MONTH==9 || MONTH==11 ){
                 day = 30;
            }

            else{
                if(YEAR % 4 ==0){
                    day = 29;
                }
                else
                    day = 28 ;
            }

            for(int i = day ; i>0 ; i--){
                if(DAY > day){

                }
                if(content.indexOf(i+"號")>-1){
                    DAY = i ;
                    Log.d("幾號" , DAY+"");
                    break;
                }
            }
        }
        else
        {

            if(content.indexOf("號")==-1 && content.indexOf("今天")== -1){
                DAY = getDAY() ;
                Log.d("幾號" , DAY+"");
            }
            if(content.indexOf("今天")>-1){
                DAY = DAY ;
            }

        }
        //處理幾號的End


        if(content.indexOf("下午")>-1 || content.indexOf("晚上")>-1){
            Monring_Aftermoon = "下午";
        }
        else
        {
            Monring_Aftermoon = "上午";
        }

        if(content.indexOf("點")>-1){
            for(int i = 12 ; i >=1  ; i--){
                if(content.indexOf(i+"點")>-1){
                    if(content.indexOf(i+"點半")>-1){
                        Hour = i+"" ;
                        MINUTE = "30" ;
                        break;
                    }
                    Hour = i+"" ;
                    MINUTE = "00";
                    if(Monring_Aftermoon.equals("早上") && content.indexOf("8點")>-1){
                        MINUTE = "30";
                    }
                    break;
                }
            }
            //顯示10以前都要加0
            if(Integer.valueOf(Hour).intValue()<10){
                Hour = "0"+Hour;
            }
        }
        else
        {
            Hour = "08";
            MINUTE="30";
        }

        if(MONTH <10  && DAY <10){
            today = YEAR+"-"+"0"+MONTH+"-"+"0"+DAY+" "+Hour+":"+MINUTE+":00 "+Monring_Aftermoon;
        }
        else if(MONTH <10){
            today = YEAR+"-"+"0"+MONTH+"-"+DAY+" "+Hour+":"+MINUTE+":00 "+Monring_Aftermoon;
        }
        else if(DAY <10){
            today = YEAR+"-"+MONTH+"-"+"0"+DAY+" "+Hour+":"+MINUTE+":00 "+Monring_Aftermoon;
        }
        else
        {
            today = YEAR+"-"+MONTH+"-"+DAY+" "+Hour+":"+MINUTE+":00 "+Monring_Aftermoon;
        }

        return today;


    }

    //取得字串 需要轉換成時間回傳給VS_TIME

    public Boolean pk_time(String start_time , String end_time , String Appliear_start , String Appliear_end){
        // start_time == 預約時間的開始
        //end_time ==預約時間結束
        //appliear_start == 已預約時間開始
        //appliear_end == 已預約時間結束
        Boolean result = false ; //是否有衝突  (true) == 可以預約
        int check = 0 ;  //如果這行是check = 2 ; 表示可以預約
        String st_p1= start_time.replace("-","");
        String ed_p1= end_time.replace("-","");
        String ast_p2 = Appliear_start.replace("-","");
        String aed_p2 = Appliear_end.replace("-","");

        if(Long.valueOf(st_p1.substring(0,8)).longValue() > Long.valueOf(aed_p2.substring(0,8)).longValue()){
            Log.d("時間" ,Long.valueOf(st_p1.substring(0,8)).longValue()+"" );
            return true;
        }
        else if(Long.valueOf(st_p1.substring(0,8)).longValue() < Long.valueOf(ast_p2.substring(0,8)).longValue() && Long.valueOf(ed_p1.substring(0,8)).longValue() < Long.valueOf(ast_p2.substring(0,8)).longValue()){
            return true;
        }
        else
            return false;

    }


    //GET 資料寫在此

    public int getYEAR() {
        return YEAR;
    }

    public int getMONTH() {
        return MONTH;
    }

    public int getDAY() {
        return DAY;
    }

    public int getHour() {
        return Hour;
    }

    public int getMINUTE() {
        return MINUTE;
    }

    //處理特殊狀況


    //End 特殊處理情況

    //SET資料寫在此
    public  String set_start_time(String time){
        start_time = time;
        return start_time;
    }
    public String set_end_time(String time){
        if(time.indexOf("早上")>-1){
            String cut = " 18:00:00 下午";
            end_time = time.substring(0,10)+cut ;
        }
        else
            end_time = time ;

        return end_time ;
    }

    //get資料
    public String getStart_time(){
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }
    private int get_week(){
        int week= c.get(Calendar.DAY_OF_WEEK)-1;
        return week;
    }
    private Long process_time(int total){
        int year = getYEAR() , month = getMONTH() , day = getDAY() ;
        int virtual_day ;
        String time = get_time().substring(0,10);
        time = time.replace("-","");
        time = time.replace(" ","");
        Long ans = Long.valueOf(time).longValue()+total;

        year = Integer.valueOf(ans.toString().substring(0,4));
        month = Integer.valueOf(ans.toString().substring(4,6));
        day = Integer.valueOf(ans.toString().substring(6,8));
        System.out.println(year+"\n"+month+"\n"+day);

        for(int i = 0 ;  ; i++){
            virtual_day = Over_day(month);
            if(day > virtual_day){
                if(month>12){
                    year+=1;
                    month-=12;
                }
                month+=1;
                day = day -virtual_day ;
            }
            else
            {
                break;
            }
        }

        if(month<10 && day <10){
            ans = Long.valueOf(year+""+0+month+""+0+day).longValue();
        }
        else if(month<10){
            ans = Long.valueOf(year+""+0+month+""+day).longValue();
        }
        else if(day<10){
            ans = Long.valueOf(year+""+month+""+0+day).longValue();
        }
        else
        {
            ans = Long.valueOf(year+""+month+""+day).longValue();
        }

        return ans ;
    }

    private int Over_day(int month){
        Log.d("月份",month+"");
        if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12 ){
            return 31 ;
        }
        else if(month==4 || month==6 || month==9 || month==11 ){
            return  30;
        }

        else{
            if(YEAR % 4 ==0){
                return  29;
            }
            else
                return  28 ;
        }
    }
}

