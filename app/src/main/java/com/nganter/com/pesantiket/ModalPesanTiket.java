package com.nganter.com.pesantiket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nganter.com.R;
import com.nganter.com.SessionManager;
import com.nganter.com.handler.AppContoller;
import com.nganter.com.koneksi.Alamat;
import com.nganter.com.objek.Film;
import com.nganter.com.objek.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class ModalPesanTiket extends Dialog {
    Film film;
    private EditText jumlahTiket,alamatAntar;
    private Button pesan,batal;
    private Activity activity;
    private TextView waktuAntar,jamtayang;
    private String namaBioskop;

    private TimePickerDialog getPukul;
    ArrayList<String> jamTayangs;
    private SessionManager sesionManager;
    private ProgressDialog progressDialog;

    int def;
    public ModalPesanTiket(Film film, Activity activity,String namaBioskop){
        super(activity);
        this.film = film;
        this.activity = activity;
        this.namaBioskop = namaBioskop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_jumlah_film);
        jamTayangs = new ArrayList<>();
        jamTayangs = getJamTayang();
        sesionManager = new SessionManager(activity.getApplicationContext());

        jumlahTiket = (EditText) findViewById(R.id.jumlah_tiket);
        alamatAntar = (EditText)findViewById(R.id.alamat_antar_tiket);
        waktuAntar = (TextView)findViewById(R.id.jam_dialog_beli_tiket);
        jamtayang = (TextView)findViewById(R.id.jam_tayang);
        pesan = (Button)findViewById(R.id.btn_pesan_tiket);
        batal = (Button)findViewById(R.id.btn_batal_tiket);

        setCancelable(false);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        jamtayang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] pilihan = new String[jamTayangs.size()];

                for(int i=0;i<jamTayangs.size();i++){
                    pilihan[i] = jamTayangs.get(i).toString();
                }

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
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!jumlahTiket.getText().toString().isEmpty()){
                    if(!jamtayang.getText().toString().isEmpty()){
                        if(!waktuAntar.getText().toString().isEmpty()){
                            if(!alamatAntar.getText().toString().isEmpty()){
                                Order order = new Order(namaBioskop,film.getNamaFilm()+"--"+jamtayang.getText().toString()+"-- "+jumlahTiket.getText().toString()+" tiket",
                                        waktuAntar.getText().toString(),alamatAntar.getText().toString());
                                insertPesanan(order);
                                showProgress();
                            }else{
                                Toast.makeText(activity, "Lengkapi Isian", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity, "Lengkapi Isian", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "Lengkapi Isian", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, "Lengkapi Isian", Toast.LENGTH_SHORT).show();
                }
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

    public ArrayList<String> getJamTayang(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__jam_tayang",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("respon");
                    if(jsonArray.length()!=0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject j = jsonArray.getJSONObject(i);
                            jamTayangs.add(j.getString("jam_tayang"));
                        }
                    }
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("kode","jam_tayang");
                map.put("id_film",film.getIdFilm());
                return map;
            }
        };
        AppContoller.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
        return jamTayangs;
    }

    public void insertPesanan(final Order order){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__pesan_tiket",response);
                try{
                    JSONObject j = new JSONObject(response);
                    if(j.getString("status").equals("1")){
                        Toast.makeText(activity, "Berhasil pesan, tunggu konfirmasi dari kami", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        dismiss();
                    }else{
                        Toast.makeText(activity, "Pesanan Gagal, mohon ulangi lagi", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> maps = new HashMap<>();
                maps.put("kode","pesanan");
                maps.put("id_pelanggan",sesionManager.getUserAkun().getIdPelanggan());
                maps.put("kategori","pesan_tiket");
                maps.put("pesanan",order.getToko()+"--("+order.getPesanan()+")");
                maps.put("jam_antar",order.getJamAntar());
                maps.put("lokasi_antar",order.getJamAntar());
                maps.put("nama_penerima",sesionManager.getUserAkun().getNama());
                return maps;
            }
        };
        AppContoller.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void showProgress() {
        progressDialog = null;// Initialize to null
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
