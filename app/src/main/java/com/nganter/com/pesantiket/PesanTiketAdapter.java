package com.nganter.com.pesantiket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nganter.com.R;
import com.nganter.com.handler.AppContoller;
import com.nganter.com.objek.Film;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class PesanTiketAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Film> films;
    private Activity activity;
    private NetworkImageView filmFoto;
    private TextView filmJudul;
    private String namaBioskop;
    ImageLoader imageLoader;
    ProgressDialog progressDialog;
    int def;
    PesanTiketAdapter(Context context, ArrayList<Film> films,Activity activity,String namaBioskop){
        this.context = context;
        this.films = films;
        this.activity = activity;
        this.namaBioskop = namaBioskop;
    }

    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.film_grid, null);
        }else {
            view = (View) convertView;
        }

        imageLoader = AppContoller.getInstance(activity.getApplicationContext()).getImageLoader();

        filmFoto = (NetworkImageView) view.findViewById(R.id.foto_film);
        filmJudul = (TextView)view.findViewById(R.id.film_nama);

        filmFoto.setImageUrl(films.get(position).getPathFoto(),imageLoader);
        filmJudul.setText(films.get(position).getNamaFilm());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModalPesanTiket modalPesanTiket = new ModalPesanTiket(films.get(position),activity,namaBioskop);
                modalPesanTiket.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                modalPesanTiket.show();

            }
        });
        return view;
    }


}
