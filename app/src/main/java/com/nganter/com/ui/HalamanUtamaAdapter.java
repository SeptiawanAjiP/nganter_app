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
    int def;
    private SessionManager sessionManager;
    public HalamanUtamaAdapter(Context context, ArrayList<MenuUtama> menuUtamas, Activity activity){
        this.context = context;
        this.menuUtamas = menuUtamas;
        this.activity = activity;
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

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.halaman_utama_grid, null);
        }else {
            view = (View) convertView;
        }
        sessionManager = new SessionManager(activity.getApplicationContext());
        Log.d("_akun",sessionManager.getUserAkun().getNama());
        ImageView foto = (ImageView)view.findViewById(R.id.foto_menu_utama);
        TextView nama = (TextView)view.findViewById(R.id.nama_menu_utama);

        foto.setImageResource(menuUtamas.get(position).getDrawabel());
        nama.setText(menuUtamas.get(position).getNama());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuUtamas.get(position).getNama().equals("Pesan Tiket")){

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
                }else if(menuUtamas.get(position).getNama().equals("Pesan Makanan")){
                    PesanBarang cdd = new PesanBarang(activity,"Pesan Makanan");
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }else if(menuUtamas.get(position).getNama().equals("Antar Barang")){
                    AntarBarang cdd = new AntarBarang(activity);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }else if(menuUtamas.get(position).getNama().equals("Beli Barang")) {
                    PesanBarang cdd = new PesanBarang(activity, "Beli Barang");
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }else if(menuUtamas.get(position).getNama().equals("Kontak Kami")){
                    Intent intent = new Intent(activity, HubungiKami.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }else if(menuUtamas.get(position).getNama().equals("Akun")){
                    ModalEditProfil cdd = new ModalEditProfil(activity);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();

                }

            }
        });
        return view;
    }
}
