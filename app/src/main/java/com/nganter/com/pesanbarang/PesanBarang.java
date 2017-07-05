package com.nganter.com.pesanbarang;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nganter.com.R;
import com.nganter.com.objek.Order;

import java.util.Calendar;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class PesanBarang extends Dialog {
    private Activity activity;
    private Button pesan,batal;
    private TimePickerDialog getPukul;
    private TextView waktuAntar,keteranganTv;
    private String keterangan;
    private EditText toko,pesanan,alamatAntar;
    public PesanBarang(Activity activity,String keterangan){
        super(activity);
        this.activity =activity;
        this.keterangan = keterangan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_order);
        keteranganTv = (TextView)findViewById(R.id.keterangan);
        if(keterangan.equals("Pesan Makanan")){
            keteranganTv.setText("Tempat/Warung Makan");
        }else{
            keteranganTv.setText("Toko/Warung");
        }
        setCancelable(false);
        pesan = (Button)findViewById(R.id.btn_pesan);
        batal = (Button)findViewById(R.id.btn_batal);
        toko = (EditText)findViewById(R.id.toko_warung_et);
        pesanan = (EditText)findViewById(R.id.keterangan_order);
        alamatAntar = (EditText)findViewById(R.id.alamat_order);
        waktuAntar = (TextView)findViewById(R.id.jam_dialog);
        getWaktu();
        waktuAntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPukul.show();
            }
        });
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toko.getText().toString().isEmpty()){
                    if(!pesanan.getText().toString().isEmpty()){
                        if(!waktuAntar.getText().toString().isEmpty()){
                            if(!alamatAntar.getText().toString().isEmpty()){
                                Order order = new Order(toko.getText().toString(),pesanan.getText().toString(),waktuAntar.getText().toString(),
                                        alamatAntar.getText().toString());
                                insertPesanan(order);
                            }
                        }
                    }
                }
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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

    public void insertPesanan(Order order){

    }
}
