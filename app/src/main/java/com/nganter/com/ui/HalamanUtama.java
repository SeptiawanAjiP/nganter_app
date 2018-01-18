package com.nganter.com.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import com.nganter.com.GPSTracker;
import com.nganter.com.NganterApp;
import com.nganter.com.PermissionUtils;
import com.nganter.com.R;
import com.nganter.com.SessionManager;
import com.nganter.com.handler.AppContoller;
import com.nganter.com.koneksi.Alamat;
import com.nganter.com.landingpage.WelcomeActivity;
import com.nganter.com.objek.MenuUtama;
import com.nganter.com.objek.Pesanan;
import com.vistrav.ask.Ask;

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
    GPSTracker gps;
    double latitude;
    double longitude;

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
                    getPesanan();
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
        MenuUtama menuUtama = new MenuUtama("BELI MAKAN",R.drawable.ic_food_);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("NGANTER BARANG",R.drawable.ic_send);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("BELANJA",R.drawable.ic_buy);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("PESANAN ANDA",R.drawable.ic_my_order);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("KONTAK",R.drawable.ic_contact_);
        menuUtamas.add(menuUtama);

        menuUtama = new MenuUtama("AKUN",R.drawable.ic_profile);
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

    public void getPesanan(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("respon");
                    if(jsonArray.length()!=0){
                        JSONObject object = jsonArray.getJSONObject(0);
                        Pesanan pesanan = new Pesanan();
                        pesanan.setPemesan(sessionManager.getUserAkun().getNama());
                        pesanan.setIsiPesanan(object.getString("pesanan"));
                        pesanan.setTanggal(object.getString("create_at"));
                        pesanan.setIdPesanan(Integer.parseInt(object.getString("id_order")));

                        DialogPesanan dialogPesanan = new DialogPesanan(HalamanUtama.this,pesanan);
                        dialogPesanan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogPesanan.show();
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
                map.put("kode","cek_belum_rating");
                map.put("id_pelanggan",sessionManager.getUserAkun().getIdPelanggan());
                return map;
            }
        };
        NganterApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void locationPermission(){

        PermissionUtils.checkPermission(HalamanUtama.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionUtils.PermissionAskListener() {
                    @Override
                    public void onPermissionGranted() {
                        if(adaKoneksi()){
                            gps = new GPSTracker(HalamanUtama.this);
                            if (gps.canGetLocation()) {
                                if(gps.getLatitude()!=0 && gps.getLongitude()!=0){
                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();
                                }
                            } else {
                                gps.showSettingsAlert();
                            }


                        }else {
                            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRequest() {
                        Ask.on(HalamanUtama.this)
                                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
                                .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                .withRationales("Anda perlu mengizinkan Location Permission untuk melakukan order")//optional
                                .go();
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        Ask.on(HalamanUtama.this)
                                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
                                .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                .withRationales("Anda perlu mengizinkan Location Permission untuk melakukan order")//optional
                                .go();
                    }

                    @Override
                    public void onPermissionDisabled() {
                        AlertDialog dialog = new AlertDialog.Builder(HalamanUtama.this)
                                .setTitle("Location Permission")
                                .setCancelable(false)
                                .setMessage("Location Permission dibutuhkan. Aktifkan Location Permission melalui menu setting ?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.parse("package:" + HalamanUtama.this.getPackageName()));
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                });

    }





}
