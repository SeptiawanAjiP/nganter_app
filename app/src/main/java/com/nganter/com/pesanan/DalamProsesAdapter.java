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

public class DalamProsesAdapter extends RecyclerView.Adapter<DalamProsesAdapter.MyViewHolder> {

    private ArrayList<Pesanan> pesananArrayList;
    private Context context;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView toko,isiPesanan,waktu;
        public MyViewHolder(View view){
            super(view);
            icon = (ImageView)view.findViewById(R.id.icon_service);
            toko = (TextView)view.findViewById(R.id.toko);
            waktu = (TextView)view.findViewById(R.id.waktu);
            isiPesanan = (TextView)view.findViewById(R.id.pesanan);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DalamProsesDialog dalamProsesDialog = new DalamProsesDialog(activity,"");
                    dalamProsesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dalamProsesDialog.show();
                }
            });
        }
    }

    public DalamProsesAdapter(Activity activity, Context context, ArrayList<Pesanan> pesananArrayList){
        this.activity = activity;
        this.context = context;
        this.pesananArrayList = pesananArrayList;
        Log.d("sinta",pesananArrayList.toString());
    }

    @Override
    public DalamProsesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dalam_proses_list,parent,false);
        return new DalamProsesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DalamProsesAdapter.MyViewHolder holder, int position) {
        Pesanan pesanan = pesananArrayList.get(position);
        if(pesanan.getJenis().equals("makanan")){
            holder.icon.setImageResource(R.drawable.ic_food);
        }else if(pesanan.getJenis().equals("barang")){
            holder.icon.setImageResource(R.drawable.ic_box);
        }
        Log.d("sinta2",pesanan.getIsiPesanan().toString());
        holder.toko.setText(pesanan.getToko());
        holder.waktu.setText(pesanan.getWaktu());
        holder.isiPesanan.setText(pesanan.getIsiPesanan());
    }

    @Override
    public int getItemCount() {
        return pesananArrayList.size();
    }
}
