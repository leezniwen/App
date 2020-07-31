package com.example.msiqlab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

public class qlab_booking_device extends AppCompatActivity {
    ArrayList<String> startDate = new ArrayList();
    ArrayList<String> endDate = new ArrayList();
    ArrayList<qlab_booking_map> dateList = new ArrayList();
    ArrayList<String> booking_value = new ArrayList();
    ArrayList<String> user_name = new ArrayList();
    int total = 0;
    int week;
    int days;
    int thismonth;
    int thisyear;
    int datano = 0;
    int lastposition = -1;
    private RelativeLayout mLoadingBar;
    Button tobooking_but;
    String bookingdate = "";

    String seqNo;
    String F_AssetNo;
    String F_Facility;
    String F_Is_Restrict;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_booking_device);
        mLoadingBar = findViewById(R.id.loading_rl);
        mLoadingBar.setVisibility(View.VISIBLE);//show loading
        Bundle bundle = this.getIntent().getExtras();
        seqNo = bundle.getString("seqNo");
        tobooking_but = findViewById(R.id.booking_but);

        String url = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Fac_Detail?F_SeqNo=" + seqNo;
        qlab_booking_device.device_value getJson = new qlab_booking_device.device_value();
        getJson.execute(url);

        Calendar mCal = Calendar.getInstance();
        String dateformat = "yyyyMMdd";
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        String today = df.format(mCal.getTime());
        thisyear = Integer.valueOf(today.substring(0, 4));
        thismonth = Integer.valueOf(today.substring(4, 6));
        addGridView();

        Button lastmon = findViewById(R.id.last_month);
        Button nextmon = findViewById(R.id.next_month);
        lastmon.setOnClickListener(lastmononclick);
        nextmon.setOnClickListener(nextmononclick);
        tobooking_but.setOnClickListener(tobookingonclick);
    }

    private void addGridView() {
        String strmon = String.valueOf(thismonth);
        String stryear = String.valueOf(thisyear);
        TextView month_tv = findViewById(R.id.month);
        TextView year_tv = findViewById(R.id.year);
        month_tv.setText(strmon);
        year_tv.setText(stryear);
        dateList = new ArrayList();
        dateList.add(new qlab_booking_map("日", "0"));
        dateList.add(new qlab_booking_map("一", "0"));
        dateList.add(new qlab_booking_map("二", "0"));
        dateList.add(new qlab_booking_map("三", "0"));
        dateList.add(new qlab_booking_map("四", "0"));
        dateList.add(new qlab_booking_map("五", "0"));
        dateList.add(new qlab_booking_map("六", "0"));
        //每個月一號禮拜幾{日=1,一=2二=3,三=4,四=5,五=6,六=0}
        Date date = new Date(thisyear, thismonth - 1, 1);
        week = date.getDay();
        if (date.getDay() == 0) {
            week = 7;
        }
        for (int i = 0; i < week - 1; i++) {
            dateList.add(new qlab_booking_map("  ", "0"));
        }
        //一個月幾天
        days = 0;
        switch (thismonth) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (thisyear % 400 == 0) {
                    days = 29;
                    break;
                } else if (thisyear % 100 == 0) {
                    days = 28;
                    break;
                } else if (thisyear % 4 == 0) {
                    days = 29;
                    break;
                } else {
                    days = 28;
                    break;
                }
        }
        for (int i = 0; i < days; i++) {
            String thedays = String.valueOf(i + 1);
            dateList.add(new qlab_booking_map(thedays, "0"));
        }
        //畫出月曆
        GridView gv = findViewById(R.id.booking_gv);
        qlab_booking_Adapter booking_adapter = new qlab_booking_Adapter(qlab_booking_device.this, dateList);
        gv.setAdapter(booking_adapter);
        String ur1l = "http://wtsc.msi.com.tw/IMS/MSI_QLAB_Service.asmx/Find_Fac_Schedule_List?F_Master_ID=" + seqNo;
        qlab_booking_device.booking_value getBookingJson = new qlab_booking_device.booking_value();
        getBookingJson.execute(ur1l);
    }

    private View.OnClickListener lastmononclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            thismonth--;
            if (thismonth < 1) {
                thisyear--;
                thismonth = 12;
            }
            addGridView();
        }
    };
    private View.OnClickListener nextmononclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            thismonth++;
            if (thismonth > 12) {
                thisyear++;
                thismonth = 1;
            }
            addGridView();
        }
    };
    private View.OnClickListener tobookingonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(qlab_booking_device.this, qlab_tobooking.class);
            Bundle bundle = new Bundle();
            bundle.putString("seqNo", seqNo);
            bundle.putString("F_AssetNo", F_AssetNo);
            bundle.putString("F_Facility", F_Facility);
            bundle.putString("F_Is_Restrict", F_Is_Restrict);
            bundle.putString("bookingdate", bookingdate);
            bundle.putString("bookingEdate", bookingdate);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        }
    };


    public class device_value extends AsyncTask<String, Void, String> {
        String data = "";
        String device_title = "";

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
                    data = data + line;
                }
                JSONObject jsonObject = new JSONObject(data);
                JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));
                JSONObject IssueData = UserArray.getJSONObject(0);
                device_title = "財編:  " + IssueData.getString("F_AssetNo") + "\n" +
                        IssueData.getString("F_Facility");
                F_AssetNo = IssueData.getString("F_AssetNo");
                F_Facility = IssueData.getString("F_Facility");
                F_Is_Restrict = IssueData.getString("F_Is_Restrict");
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
            TextView device_title = findViewById(R.id.booking_title);
            device_title.setText(this.device_title);
        }
    }

    public class booking_value extends AsyncTask<String, Void, String> {
        String data = "";

        @Override
        protected String doInBackground(String... urlStrings) {
            startDate = new ArrayList();
            endDate = new ArrayList();
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
                    data = data + line;
                }
                JSONObject jsonObject = new JSONObject(data);
                JSONArray UserArray = new JSONArray(jsonObject.getString("Key"));
                total = UserArray.length();

                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject IssueData = UserArray.getJSONObject(i);
                    startDate.add(IssueData.getString("F_StartDate").substring(0, IssueData.getString("F_StartDate").indexOf("T")));
                    endDate.add(IssueData.getString("F_EndDate").substring(0, IssueData.getString("F_StartDate").indexOf("T")));
                    user_name.add(IssueData.getString("F_Owner"));
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
            int total1 = total;
            int no = 0;
            for (int i = 0; i < total1; i++) {
                int datamon = Integer.valueOf(startDate.get(i).substring(5, 7));
                booking_value.add("n" + no + "s" + startDate.get(i) + "e" + endDate.get(i) + "m" + datamon + "n" + user_name.get(i));
                no++;
                int sd = Integer.valueOf(startDate.get(i).substring(0, 10).replaceAll("-", ""));
                int ed = Integer.valueOf(endDate.get(i).substring(0, 10).replaceAll("-", ""));
                int thisdays = days;
                while (sd < ed) {
                    int year = sd / 10000;
                    int mon = sd % 1000 / 100;
                    int day = sd % 100;
                    day++;
                    total++;
                    switch (mon) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            thisdays = 31;
                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            thisdays = 30;
                            break;
                        case 2:
                            if (thisyear % 400 == 0) {
                                thisdays = 29;
                                break;
                            } else if (thisyear % 100 == 0) {
                                thisdays = 28;
                                break;
                            } else if (thisyear % 4 == 0) {
                                thisdays = 29;
                                break;
                            } else {
                                thisdays = 28;
                                break;
                            }
                    }
                    if (day > thisdays) {
                        day = 1;
                        mon++;
                    }
                    String dayten;
                    if (day < 10) {
                        dayten = "0";
                    } else {
                        dayten = "";
                    }
                    int ten = mon / 10;
                    if (mon > 12) {
                        mon = 0;
                        year++;
                    }
                    String stryear = String.valueOf(year);
                    String strmon = String.valueOf(mon);
                    String strday = String.valueOf(day);
                    String strten = String.valueOf(ten);
                    String newdate = stryear + "-" + strten + strmon + "-" + dayten + strday;
                    no++;
                    booking_value.add("n" + no + "s" + newdate + "e" + endDate.get(i) + "m" + mon + "n" + user_name.get(i));
                    sd = Integer.valueOf(newdate.replaceAll("-", ""));
                }
                datano = no;
            }
            inputgridview();
            //畫出月曆
        }
    }

    private void inputgridview() {
        for (int i = 0; i < datano; i++) {
            int datayear = Integer.valueOf(booking_value.get(i).substring(booking_value.get(i).indexOf("s") + 1, booking_value.get(i).indexOf("-")));
            int datamon = Integer.valueOf(booking_value.get(i).substring(booking_value.get(i).indexOf("m") + 1, booking_value.get(i).lastIndexOf("n")));
            int getday = Integer.valueOf(booking_value.get(i).substring(booking_value.get(i).indexOf("s") + 9, booking_value.get(i).indexOf("s") + 11));
            if (datayear == thisyear) {
                if (datamon == thismonth) {
                    int no = 7 + week + getday - 1 - 1;
                    //加7是因為陣列開頭是日~六,-1是因為week從是1開始陣列是0,-1是因為theday是1開始陣列是0開始
                    int stime_int = Integer.valueOf(booking_value.get(i).substring(booking_value.get(i).indexOf("s") + 9, booking_value.get(i).indexOf("s") + 11));
                    String stime = String.valueOf(stime_int);
                    String name = booking_value.get(i).substring(booking_value.get(i).lastIndexOf("n") + 1);
                    if (UserData.Name.equals(name)) {
                        dateList.set(no, new qlab_booking_map(stime, "1"));

                    } else {
                        dateList.set(no, new qlab_booking_map(stime, "2"));
                    }
                }
            }
        }
        final GridView gv = findViewById(R.id.booking_gv);
        qlab_booking_Adapter booking_adapter = new qlab_booking_Adapter(qlab_booking_device.this, dateList);
        gv.setAdapter(booking_adapter);
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mLoadingBar.setVisibility(View.GONE);//end loading

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((position - 7 - week + 2) > 0) {

                    if (position > 6) {
                        if (lastposition > 6) {
                            final LinearLayout lastitem = gv.getChildAt(lastposition).findViewById(R.id.booking_ll);
                            lastitem.setBackgroundResource(R.drawable.booking_item_background);
                        }

                        final LinearLayout date_linearlayout_inside = gv.getChildAt(position).findViewById(R.id.booking_ll);
                        date_linearlayout_inside.setBackgroundResource(R.drawable.ic_circle);
                        lastposition = position;
                        boolean booked = false;
                        ScrollView booked_sv = findViewById(R.id.booked_sv);
                        ScrollView booking_sv = findViewById(R.id.booking_sv);
                        TextView booked_v = findViewById(R.id.booked_value);
                        TextView booking_v = findViewById(R.id.booking_value);
                        for (int i = 0; i < datano; i++) {
                            int datayear = Integer.valueOf(booking_value.get(i).substring(booking_value.get(i).indexOf("s") + 1, booking_value.get(i).indexOf("-")));
                            if (datayear == thisyear) {
                                int datamon = Integer.valueOf(booking_value.get(i).substring(booking_value.get(i).indexOf("m") + 1, booking_value.get(i).lastIndexOf("n")));
                                if (datamon == thismonth) {
                                    int getday = Integer.valueOf(booking_value.get(i).substring(booking_value.get(i).indexOf("s") + 9, booking_value.get(i).indexOf("s") + 11));
                                    int no = 7 + week + getday - 1 - 1;
                                    if (no == position) {
                                        //加7是因為陣列開頭是日~六,-1是因為week從是1開始陣列是0,-1是因為theday是1開始陣列是0開始
                                        String name = "";
                                        name = booking_value.get(i).substring(booking_value.get(i).lastIndexOf("n") + 1);
                                        String stime = booking_value.get(i).substring(booking_value.get(i).indexOf("s") + 1, booking_value.get(i).indexOf("e"));
                                        String etime = booking_value.get(i).substring(booking_value.get(i).indexOf("e") + 1, booking_value.get(i).indexOf("m"));
                                        booked_v.setText("預約人員: " + name + "\n" + "預約時間: " + stime + "\n" + "結束時間: " + etime);
                                        if (!name.equals("")) {
                                            booking_sv.setVisibility(View.GONE);
                                            booked_sv.setVisibility(View.VISIBLE);
                                            tobooking_but.setVisibility(View.GONE);
                                            booked = true;
                                            break;
                                        }

                                    } else {
                                        booked_sv.setVisibility(View.GONE);
                                        booking_sv.setVisibility(View.GONE);
                                        booking_v.setText("預約人員: " + UserData.Name + "\n" + "預約時間: ");
                                        tobooking_but.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        String strDate = sdFormat.format(date);
                        strDate = strDate.replaceAll("/", "");
                        int intDate = Integer.valueOf(strDate);
                        String monten = "";
                        if (thismonth < 10) {
                            monten = "0";
                        }
                        String dayten = "";
                        if ((position - 7 - week + 2) < 10) {
                            dayten = "0";
                        }
                        String strthisdays = String.valueOf(thisyear) + monten + String.valueOf(thismonth) + dayten + String.valueOf(position - 7 - week + 2);
                        int thisdays = Integer.valueOf(strthisdays);
                        boolean x = (position > 7 + week - 2);
                        boolean y = thisdays >= intDate;
                        if (position > 7 + week - 2 && thisdays >= intDate && !booked) {
                            booking_sv.setVisibility(View.VISIBLE);
                            if (thismonth < 10) {
                                monten = "0";
                            }
                            int thisday = position - 7 - week + 2;

                            if (thisday < 10) {
                                dayten = "0";
                            }
                            bookingdate = thisyear + "-" + monten + thismonth + "-" + dayten + thisday;
                            booking_v.setText("預約人員: " + UserData.Name + "\n" + "預約時間: " + bookingdate);
                            tobooking_but.setVisibility(View.VISIBLE);
                        } else if (!booked) {
                            booking_sv.setVisibility(View.GONE);
                            tobooking_but.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }


    public class qlab_booking_map {

        private String date;

        private String mod;

        //建構式
        public qlab_booking_map(String todate, String tomod) {

            date = todate;
            mod = tomod;
        }

        public String getdate() {
            return date;
        }

        public String getmod() {
            return mod;
        }
    }


    public class qlab_booking_Adapter extends ArrayAdapter<qlab_booking_map> {
        TextView date;
        private int clickTemp = -1;//標識被選擇的item

        //建構式
        public qlab_booking_Adapter(Activity context, ArrayList<qlab_booking_map> tortoises) {
            super(context, 0, tortoises);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.qlab_booking_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final qlab_booking_map value = getItem(position);
            //找到list_item.xml中的TextView()
            date = listItemView.findViewById(R.id.booking_date_tv);
            date.setText(value.getdate());
            TextView mod = listItemView.findViewById(R.id.booking_mod_tv);
            if (value.getmod().equals("1")) {
                mod.setBackgroundResource(R.drawable.ic_person_green);
            } else if (value.getmod().equals("2")) {
                mod.setBackgroundResource(R.drawable.ic_person_yellow);
            }
            return listItemView;
        }
    }
}