package com.nganter.com.pesanbarang;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import com.nganter.com.objek.Order;
import com.nganter.com.ui.RegisterActivity;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
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
        sessionManager = new SessionManager(activity.getApplicationContext());
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
                                showProgress();
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

    public void insertPesanan(final Order order){
        final String kategori;
        if(keterangan.equals("Pesan Makanan")){
            kategori = "pesan_makanan";
        }else{
            kategori = "beli_barang";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__pesan_barang",response);
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
                maps.put("id_pelanggan",sessionManager.getUserAkun().getIdPelanggan());
                maps.put("kategori",kategori);
                maps.put("pesanan","Toko/Warung : "+order.getToko()+"" +
                        "\n" +
                        "Pesanan : "+order.getPesanan());
                maps.put("jam_antar",order.getJamAntar());
                maps.put("lokasi_antar",order.getJamAntar());
                maps.put("nama_penerima",sessionManager.getUserAkun().getNama());
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
