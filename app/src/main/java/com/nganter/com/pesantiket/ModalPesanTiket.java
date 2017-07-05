package com.nganter.com.pesantiket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nganter.com.R;
import com.nganter.com.objek.Film;

import java.util.Calendar;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class ModalPesanTiket extends Dialog {
    Film film;
    private EditText jumlahTiket;
    private Activity activity;
    private TextView waktuAntar,jamtayang;

    private TimePickerDialog getPukul;
    int def;
    public ModalPesanTiket(Film film, Activity activity){
        super(activity);
        this.film = film;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_jumlah_film);
        jumlahTiket = (EditText) findViewById(R.id.jumlah_tiket);
        waktuAntar = (TextView)findViewById(R.id.jam_dialog_beli_tiket);
        jamtayang = (TextView)findViewById(R.id.jam_tayang);
        jamtayang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] pilihan = {"18:00-20:00","20:00-22:00"};
                def = 0;
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setTitle("Pilih Jam Tayang")
                        .setSingleChoiceItems(pilihan, 0,  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                def = which;
                            }
                        })
                        .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            jamtayang.setText(pilihan[def]);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            }
        });
        getWaktu();
        waktuAntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPukul.show();
            }
        });
    }

    public void getWaktu(){
        final Calendar calendar = Calendar.getInstance();
        getPukul = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if(i<10){
                    if(i1<10){
                        waktuAntar.setText("0"+i+":0"+i1);
                    }else{
                        waktuAntar.setText("0"+i+":"+i1);
                    }
                }else {
                    if(i1<10){
                        waktuAntar.setText(i+":0"+i1);
                    }else{
                        waktuAntar.setText(i+":"+i1);
                    }
                }

            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
    }
}
