package com.example.msiqlab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class littlesupper_Onboard_Adapter extends RecyclerView.Adapter<littlesupper_Onboard_Adapter.Onboard_ViewHolder>{

    private List<littlesupper_Onboard_ITEM> onboard_items;

    public littlesupper_Onboard_Adapter(List<littlesupper_Onboard_ITEM> onboard_items){
        this.onboard_items = onboard_items;
    }


    @NonNull
    @Override
    public Onboard_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Onboard_ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.littlesupper_lab_inform_item , parent , false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull Onboard_ViewHolder holder, int position) {
        holder.setOnboard_DATA(onboard_items.get(position));
    }

    @Override
    public int getItemCount() {
        return onboard_items.size();
    }

    class Onboard_ViewHolder extends RecyclerView.ViewHolder {
        private TextView title ;
        private TextView content ;
        private ImageView image ;

        public Onboard_ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            content = itemView.findViewById(R.id.textContent);
            image = itemView.findViewById(R.id.lab_image);
        }
        void setOnboard_DATA(littlesupper_Onboard_ITEM onboard_item){
            title.setText(onboard_item.getTitle());
            content.setText(onboard_item.getContent());
            image.setImageResource(onboard_item.getImage());
        }
    }
}
