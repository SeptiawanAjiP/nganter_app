package com.nganter.com.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nganter.com.R;
import com.nganter.com.SessionManager;
import com.nganter.com.handler.AppContoller;
import com.nganter.com.koneksi.Alamat;
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
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_utama);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setSessionInstall();
        expandGridView = (ExpandGridView)findViewById(R.id.grid_menu_utama);
        expandGridView.setExpanded(true);
        expandGridView.setFocusable(false);
        setData();
        cekBukaTutup();
        showProgress();
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
        MenuUtama menuUtama = new MenuUtama("Pesan Makanan",R.drawable.pesan_makanan);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("Antar Barang",R.drawable.antar_barang);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("Beli Barang",R.drawable.beli_barang);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("Pesan Tiket",R.drawable.pesan_tiket);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("Kontak Kami",R.drawable.kontak_kami);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("Akun",R.drawable.akun);
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
}
