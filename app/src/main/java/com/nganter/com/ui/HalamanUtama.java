package com.nganter.com.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.nganter.com.landingpage.WelcomeActivity;
import com.nganter.com.objek.MenuUtama;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class HalamanUtama extends AppCompatActivity {
    private ExpandGridView expandGridView;
    private ArrayList<MenuUtama> menuUtamas;
    SessionManager sessionManager;
    String status;
    public static final String BUKA = "buka";
    public static final String TUTUP = "tutup";
    private LinearLayout petunjuk;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_utama);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setSessionInstall();
        expandGridView = (ExpandGridView)findViewById(R.id.grid_menu_utama);
        petunjuk = (LinearLayout)findViewById(R.id.petunjuk);
        expandGridView.setExpanded(true);
        expandGridView.setFocusable(false);
        setData();
        if(adaKoneksi()){
            cekBukaTutup();
            showProgress();
        }else{
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }

        petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void cekBukaTutup(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__buka_tutup",response);
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    status = jsonObject.getString("status");
                    expandGridView.setAdapter(new HalamanUtamaAdapter(getApplicationContext(),menuUtamas,HalamanUtama.this,status));
                    progressDialog.dismiss();
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
                map.put("kode","cek_buka_tutup");
                return map;
            }
        };
        AppContoller.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void setData(){
        menuUtamas = new ArrayList<>();
        MenuUtama menuUtama = new MenuUtama("BELI MAKAN",R.drawable.ic_beli_makan);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("NGANTER BARANG",R.drawable.ic_antar_barang);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("BELANJA",R.drawable.ic_beli_barang);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("PESANAN ANDA",R.drawable.ic_pesanan);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("KONTAK",R.drawable.ic_contact);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("AKUN",R.drawable.ic_akun);
        menuUtamas.add(menuUtama);
    }
    private void showProgress() {
        progressDialog = null;// Initialize to null
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public boolean adaKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
