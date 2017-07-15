package com.nganter.com.antarbarang;

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
import com.nganter.com.objek.Antar;
import com.nganter.com.objek.Order;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class AntarBarang extends Dialog {
    private Activity activity;
    private Button pesan,batal;
    private TimePickerDialog getPukul;
    private TextView waktuAntar,keteranganTv;
    private EditText alamatAmbil,alamatAntar,jenisBarang;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
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
        sessionManager = new SessionManager(activity.getApplicationContext());
        alamatAntar.setText(sessionManager.getUserAkun().getAlamat());
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
                                Order order = new Order(alamatAmbil.getText().toString(),jenisBarang.getText().toString(),waktuAntar.getText().toString(),
                                        alamatAntar.getText().toString());
                                insertPesanan(order);
                                insertDatabase(order);
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
                maps.put("kategori","antar_barang");
                maps.put("pesanan","Alamat Ambil : "+order.getToko()+"" +
                        "\n" +
                        ",Jenis Barang : "+order.getPesanan()+
                        ",Jam Ambil : "+order.getJamAntar());
                maps.put("jam_antar","");
                maps.put("lokasi_antar",order.getAlamatAntar());
                maps.put("nama_penerima",sessionManager.getUserAkun().getNama());
                return maps;
            }
        };

        AppContoller.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void insertDatabase(final Order order){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__database_pesan_barang",response);
                            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> maps = new HashMap<>();
                maps.put("kode","kirim_barang");
                maps.put("id_pelanggan",sessionManager.getUserAkun().getIdPelanggan());
                maps.put("alamat_jemput",alamatAmbil.getText().toString());
                maps.put("alamat_tujuan",alamatAntar.getText().toString());
                maps.put("jenis_barang",jenisBarang.getText().toString());
                maps.put("waktu_antar",waktuAntar.getText().toString());
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
