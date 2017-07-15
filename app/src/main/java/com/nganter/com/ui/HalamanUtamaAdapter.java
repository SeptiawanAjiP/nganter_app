package com.nganter.com.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nganter.com.SessionManager;
import com.nganter.com.antarbarang.AntarBarang;
import com.nganter.com.R;
import com.nganter.com.hubungikami.HubungiKami;
import com.nganter.com.objek.MenuUtama;
import com.nganter.com.pesanbarang.PesanBarang;
import com.nganter.com.pesantiket.PesanTiketActivity;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class HalamanUtamaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MenuUtama> menuUtamas;
    private Activity activity;
    String status;
    int def;
    private SessionManager sessionManager;
    public HalamanUtamaAdapter(Context context, ArrayList<MenuUtama> menuUtamas, Activity activity,String status){
        this.context = context;
        this.menuUtamas = menuUtamas;
        this.activity = activity;
        this.status = status;

        Log.d("__status",status);
    }

    @Override
    public int getCount() {
        return menuUtamas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;


        Log.d("__status",status);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.halaman_utama_grid, null);
        }else {
            view = (View) convertView;
        }
        sessionManager = new SessionManager(activity.getApplicationContext());

        TextView nama = (TextView)view.findViewById(R.id.nama_menu_utama);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        RelativeLayout t = (RelativeLayout)view.findViewById(R.id.relative);
        icon.setImageResource(menuUtamas.get(position).getDrawabel());
        nama.setText(menuUtamas.get(position).getNama());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("_akun",sessionManager.getUserAkun().getNama());
                if(menuUtamas.get(position).getNama().equals("TIKET BIOSKOP")){
                    if(status.equals(HalamanUtama.TUTUP)){
                        Toast.makeText(context, "Maaf, pada jam ini layanan kami sedang tidak beroperasi, silakan hubungi cs kami", Toast.LENGTH_LONG).show();
                    }else{
                        final String[] pilihan = {"Rajawali","CGV Rita Mall"};
                        def = 0;
                        AlertDialog dialog = new AlertDialog.Builder(activity)
                                .setTitle("Pilih Bioskop")
                                .setSingleChoiceItems(pilihan, 0,  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        def = which;
                                    }
                                })
                                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(activity,PesanTiketActivity.class);
                                        intent.putExtra(PesanTiketActivity.BIOSKOP,pilihan[def]);

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create();
                        dialog.show();
                    }
                }else if(menuUtamas.get(position).getNama().equals("BELI MAKAN")){
                    if(status.equals(HalamanUtama.TUTUP)){
                        Toast.makeText(context, "Maaf, pada jam ini layanan kami sedang tidak beroperasi, silakan hubungi cs kami", Toast.LENGTH_LONG).show();
                    }else{
                        PesanBarang cdd = new PesanBarang(activity,"Pesan Makanan");
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.show();
                    }
                }else if(menuUtamas.get(position).getNama().equals("NGANTER BARANG")){
                    if(status.equals(HalamanUtama.TUTUP)){
                        Toast.makeText(context, "Maaf, pada jam ini layanan kami sedang tidak beroperasi, silakan hubungi cs kami", Toast.LENGTH_LONG).show();
                    }else{
                        AntarBarang cdd = new AntarBarang(activity);
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.show();
                    }
                }else if(menuUtamas.get(position).getNama().equals("BELANJA")) {
                    if(status.equals(HalamanUtama.TUTUP)){
                        Toast.makeText(context, "Maaf, pada jam ini layanan kami sedang tidak beroperasi, silakan hubungi cs kami", Toast.LENGTH_LONG).show();
                    }else{
                        PesanBarang cdd = new PesanBarang(activity, "Beli Barang");
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.show();
                    }
                }else if(menuUtamas.get(position).getNama().equals("KONTAK")){
                    Intent intent = new Intent(activity, HubungiKami.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }else if(menuUtamas.get(position).getNama().equals("AKUN")){
                    ModalEditProfil cdd = new ModalEditProfil(activity);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }
            }
        });
        return view;
    }
}
