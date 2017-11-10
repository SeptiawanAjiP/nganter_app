package com.nganter.com.pesanan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nganter.com.R;
import com.nganter.com.objek.Pesanan;

import java.util.ArrayList;

/**
 * Created by aji on 11/10/2017.
 */

public class SelesaiAdapter extends RecyclerView.Adapter<SelesaiAdapter.MyViewHolder> {

    private ArrayList<Pesanan> pesananArrayList;
    private Context context;
    private Activity activity;
    private Pesanan pesanan;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView toko,isiPesanan,waktu;
        public MyViewHolder(View view){
            super(view);
            icon = (ImageView)view.findViewById(R.id.icon_service);
            toko = (TextView)view.findViewById(R.id.toko);
            waktu = (TextView)view.findViewById(R.id.waktu);
            isiPesanan = (TextView)view.findViewById(R.id.pesanan);
            pesanan = new Pesanan();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelesaiDialog selesaiDialog = new SelesaiDialog(activity,pesanan);
                    selesaiDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    selesaiDialog.show();
                }
            });
        }
    }

    public SelesaiAdapter(Activity activity, Context context, ArrayList<Pesanan> pesananArrayList){
        this.activity = activity;
        this.context = context;
        this.pesananArrayList = pesananArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selesai_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pesanan pesanan = pesananArrayList.get(position);
        holder.toko.setText(pesanan.getToko());
        holder.waktu.setText(pesanan.getWaktu());
        holder.isiPesanan.setText(pesanan.getIsiPesanan());
    }

    @Override
    public int getItemCount() {
        return pesananArrayList.size();
    }
}
