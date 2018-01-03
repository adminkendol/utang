package com.source.utang.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.source.utang.R;

/**
 * Created by Chandra on 7/30/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private final String[] nasabah_id;
    private final String[] nasabah_name;
    private final String[] phone;

    public RecyclerViewAdapter(Context context, String[] nasabah_id, String[] nasabah_name, String[] phone) {
        this.nasabah_id= nasabah_id;
        this.nasabah_name= nasabah_name;
        this.phone= phone;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        //listItem.setOnClickListener();
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nasabahId.setText(nasabah_id[position]);
        holder.nasabahName.setText(nasabah_name[position]);
        holder.nasabahPhone.setText(phone[position]);


    }

    @Override
    public int getItemCount() {
        return nasabah_id.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nasabahId;
        private TextView nasabahName;
        private TextView nasabahPhone;
        public MyViewHolder(View itemView) {
            super(itemView);
            nasabahId = (TextView)itemView.findViewById(R.id.myId);
            nasabahName = (TextView)itemView.findViewById(R.id.text_name);
            nasabahPhone = (TextView)itemView.findViewById(R.id.text_phone);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, Arsip_cam.class);
                    intent.putExtra("camId", String.valueOf(camIdText.getText()));
                    intent.putExtra("camName", String.valueOf(camNameText.getText()));
                    intent.putExtra("camLoc", String.valueOf(locNameText.getText()));
                    intent.putExtra("camImg", String.valueOf(camImgText.getText()));
                    mContext.startActivity(intent);
                }
            });*/
        }
    }
}
