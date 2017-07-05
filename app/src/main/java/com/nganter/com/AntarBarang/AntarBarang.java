package com.nganter.com.AntarBarang;

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
import com.nganter.com.objek.Antar;
import com.nganter.com.objek.Order;

import java.util.Calendar;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class AntarBarang extends Dialog {
    private Activity activity;
    private Button pesan,batal;
    private TimePickerDialog getPukul;
    private TextView waktuAntar,keteranganTv;
    private EditText alamatAmbil,alamatAntar,jenisBarang;
    public AntarBarang(Activity activity){
        super(activity);
        this.activity =activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_antar);

        setCancelable(false);
        pesan = (Button)findViewById(R.id.btn_pesan);
        batal = (Button)findViewById(R.id.btn_batal);
        alamatAmbil = (EditText)findViewById(R.id.alamat_ambil_et);
        alamatAntar = (EditText)findViewById(R.id.alamat_antar_et);
        waktuAntar = (TextView)findViewById(R.id.jam_dialog_antar);
        jenisBarang = (EditText)findViewById(R.id.jenis_barang_et);
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
                if(!alamatAmbil.getText().toString().isEmpty()){
                    if(!alamatAntar.getText().toString().isEmpty()){
                        if(!waktuAntar.getText().toString().isEmpty()){
                            if(!jenisBarang.getText().toString().isEmpty()){
                                Antar antar = new Antar(alamatAmbil.getText().toString(),alamatAntar.getText().toString(),waktuAntar.getText().toString(),
                                        alamatAntar.getText().toString());
                                insertAntar(antar);
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

    public void insertAntar(Antar antar){

    }
}
