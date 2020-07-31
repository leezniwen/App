package com.example.msiqlab;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class video extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
        ListView lv =findViewById(R.id.video_lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

       final String url="https://www.youtube.com/watch?v=TuASEqVET1U";
    }

    //map
    public class video_map{
        //大標題
        private String tital;
        //內容
        private String link;
        //照片
        private Bitmap photo;
        //建構式
        public video_map(String totital, String tolink ,Bitmap tophoto ) {
            tital = totital;
            link = tolink;
            photo=tophoto;
        }
        public String gettital() {
            return tital;
        }
        public String getlink() {
            return link;
        }
        public Bitmap getPhoto(){
            return photo;
        }
    }

    //adapter
    public class video_Adapter extends ArrayAdapter<video_map> {
        private final Activity context;
        ImageView img;

        //建構式
        public video_Adapter(Activity context, ArrayList<video_map> tortoises) {
            super(context, 0, tortoises);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.video_item, parent, false);
            }
            //找到data，並在View上設定正確的data
            final video_map value = getItem(position);
            //找到list_item.xml中的TextView()
            TextView tital = listItemView.findViewById(R.id.video_title);
            tital.setText(value.gettital());

            img = listItemView.findViewById(R.id.video_img);
            Glide.with(getContext())
                    .load(value.getPhoto())
                    .error(R.drawable.ic_image)//load失敗的Drawable
                    .placeholder(R.drawable.ic_image)//loading時候的Drawable
                    .into(img);



            return listItemView;
        }
    }



}
