package com.example.msiqlab;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class myAdapter extends ArrayAdapter<message_map> {
    //建構式
    public myAdapter(Activity context, ArrayList<message_map> tortoises){
        super(context, 0, tortoises);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        //listItemView可能會是空的，例如App剛啟動時，沒有預先儲存的view可使用
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //找到data，並在View上設定正確的data
        message_map currentName = getItem(position);
        //找到list_item.xml中的TextView()
        TextView tital = listItemView.findViewById(R.id.item_tital);
        tital.setText(currentName.gettital());

        TextView time = listItemView.findViewById(R.id.item_time);
        time.setText(currentName.gettime());

        TextView conn = listItemView.findViewById(R.id.item_text);
        conn.setText(currentName.getconn());
        return listItemView;
    }
}
