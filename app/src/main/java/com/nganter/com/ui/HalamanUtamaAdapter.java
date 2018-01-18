package com.nganter.com.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nganter.com.GPSTracker;
import com.nganter.com.PermissionUtils;
import com.nganter.com.SessionManager;
import com.nganter.com.antarbarang.AntarBarang;
import com.nganter.com.R;
import com.nganter.com.handler.AppContoller;
import com.nganter.com.hubungikami.HubungiKami;
import com.nganter.com.koneksi.Alamat;
import com.nganter.com.objek.MenuUtama;
import com.nganter.com.objek.Pesanan;
import com.nganter.com.pesanan.PesananActivity;
import com.nganter.com.pesanbarang.PesanBarang;
import com.nganter.com.pesantiket.PesanTiketActivity;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class HalamanUtamaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MenuUtama> menuUtamas;
    private Activity activity;
    String status;
    int def;
    private SessionManager sessionManager;
    GPSTracker gps;
    double latitude;
    double longitude;
    private Handler mHandler;
    private Runnable runnable;
    public HalamanUtamaAdapter(Context context, ArrayList<MenuUtama> menuUtamas, Activity activity,String status){
        this.context = context;
        this.menuUtamas = menuUtamas;
        this.activity = activity;
        this.status = status;


        Log.d("__status",status);
    }

    @Override
    public int getCount() {
        return menuUtamas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;


        Log.d("__status",status);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.halaman_utama_grid, null);
        }else {
            view = (View) convertView;
        }
        sessionManager = new SessionManager(activity.getApplicationContext());

        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        RelativeLayout t = (RelativeLayout)view.findViewById(R.id.relative);
        icon.setImageResource(menuUtamas.get(position).getDrawabel());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPermission();

                Log.d("_akun",sessionManager.getUserAkun().getNama());
                if(menuUtamas.get(position).getNama().equals("PESANAN ANDA")){
                    if(adaKoneksi()){
                        cekBukaTutup("pesanan_anda");
                    }else{
                        Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                    }

                }else if(menuUtamas.get(position).getNama().equals("BELI MAKAN")){
                    if(adaKoneksi()){
                        cekBukaTutup("beli_makan");
                    }else{
                        Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                    }

                }else if(menuUtamas.get(position).getNama().equals("NGANTER BARANG")){
                   if(adaKoneksi()){
                       cekBukaTutup("antar_barang");
                   }else{
                       Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                   }
                }else if(menuUtamas.get(position).getNama().equals("BELANJA")) {
                   if(adaKoneksi()){
                       cekBukaTutup("belanja");
                   }else{
                       Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                   }
                }else if(menuUtamas.get(position).getNama().equals("KONTAK")){
                    Intent intent = new Intent(activity, HubungiKami.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }else if(menuUtamas.get(position).getNama().equals("AKUN")){
                    ModalEditProfil cdd = new ModalEditProfil(activity);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }
            }
        });
        return view;
    }

    public void cekBukaTutup(final String jenisLayanan){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__buka_tutup",response);
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    status = jsonObject.getString("status");
                    if(status.equals("buka")){
                        gps = new GPSTracker(activity);
                        if (gps.canGetLocation()) {
                            if(gps.getLatitude()!=0 && gps.getLongitude()!=0){
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                            }
                        } else {
                            gps.showSettingsAlert();
                        }
                        if(jenisLayanan.equals("beli_makan")){
                            PesanBarang cdd = new PesanBarang(activity,"Pesan Makanan","","",latitude,longitude);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();
                        }else if(jenisLayanan.equals("antar_barang")){
                            AntarBarang cdd = new AntarBarang(activity,latitude,longitude);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();
                        }else if(jenisLayanan.equals("belanja")){
                            PesanBarang cdd = new PesanBarang(activity, "Beli Barang","","",latitude,longitude);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();
                        }else if(jenisLayanan.equals("pesanan_anda")){
                            Intent intent = new Intent(activity, PesananActivity.class);
                            activity.startActivity(intent);
                        }
                    }else{
                        Toast.makeText(context, "Maaf, pada jam ini layanan kami sedang tidak beroperasi, silakan hubungi cs kami", Toast.LENGTH_LONG).show();
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
                map.put("kode","cek_buka_tutup");
                return map;
            }
        };
        AppContoller.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public boolean adaKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void locationPermission(){

        PermissionUtils.checkPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionUtils.PermissionAskListener() {
                    @Override
                    public void onPermissionGranted() {
                        if(adaKoneksi()){
                            gps = new GPSTracker(activity);
                            if (gps.canGetLocation()) {
                                if(gps.getLatitude()!=0 && gps.getLongitude()!=0){
                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();
                                }
                            } else {
                                gps.showSettingsAlert();
                            }


                        }else {
                            Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRequest() {
                        Ask.on(activity)
                                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
                                .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                .withRationales("Anda perlu mengizinkan Location Permission untuk melakukan order")//optional
                                .go();
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        Ask.on(activity)
                                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
                                .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                .withRationales("Anda perlu mengizinkan Location Permission untuk melakukan order")//optional
                                .go();
                    }

                    @Override
                    public void onPermissionDisabled() {
                        AlertDialog dialog = new AlertDialog.Builder(activity)
                                .setTitle("Location Permission")
                                .setCancelable(false)
                                .setMessage("Location Permission dibutuhkan. Aktifkan Location Permission melalui menu setting ?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.parse("package:" + activity.getPackageName()));
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
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
