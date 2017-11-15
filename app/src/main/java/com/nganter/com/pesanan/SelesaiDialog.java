package com.nganter.com.pesanan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nganter.com.GPSTracker;
import com.nganter.com.PermissionUtils;
import com.nganter.com.R;
import com.nganter.com.objek.Pesanan;
import com.nganter.com.pesanbarang.PesanBarang;
import com.vistrav.ask.Ask;

/**
 * Created by aji on 11/10/2017.
 */

public class SelesaiDialog extends Dialog {
    private Activity activity;
    private Pesanan pesanan;
    private Button pesanKembali,tutup;
    private TextView judul,pesananTv;
    private Pesanan newPesanan;
    GPSTracker gps;
    double latitude;
    double longitude;
    public SelesaiDialog(Activity activity, Pesanan pesanan){
        super(activity);
        this.activity = activity;
        this.pesanan = pesanan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detil_selesai);
        setCancelable(false);
        pesanKembali = (Button)findViewById(R.id.pesan_kembali);
        judul = (TextView)findViewById(R.id.judul_detil);
        pesananTv = (TextView)findViewById(R.id.pesanan_detail);
        tutup = (Button)findViewById(R.id.tutup);
        newPesanan = new Pesanan();
        tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if(pesanan.getJenis().equals("beli_barang")){
            judul.setText("Belanja Barang");
        }else if(pesanan.getJenis().equals("pesan_makanan")){
            judul.setText("Beli Makan");
        }else if(pesanan.getJenis().equals("antar_barang")){
            judul.setText("Antar Barang");
        }

        pesananTv.setText(pesanan.getIsiPesanan());




        pesanKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] text = pesanan.getIsiPesanan().split(" :");
                if(pesanan.getJenis().equals("beli_barang")){
                    newPesanan.setToko(text[1].replace(",Pesanan",""));
                    newPesanan.setIsiPesanan(text[2]);
                    PesanBarang cdd = new PesanBarang(activity,"Beli Barang",newPesanan.getToko(),newPesanan.getIsiPesanan(),latitude,longitude);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }else if(pesanan.getJenis().equals("pesan_makanan")){
                    newPesanan.setToko(text[1].replace(",Pesanan",""));
                    newPesanan.setIsiPesanan(text[2]);
                    PesanBarang cdd = new PesanBarang(activity,"Pesan Makanan",newPesanan.getToko(),newPesanan.getIsiPesanan(),latitude,longitude);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }else if(pesanan.getJenis().equals("antar_barang")){
                    Toast.makeText(activity, "Hanya bisa untuk pemesanan barang atau makanan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void locationPermission(){
        PermissionUtils.checkPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionUtils.PermissionAskListener() {
                    @Override
                    public void onPermissionGranted() {
                        if(adaKoneksi()){
                            gps = new GPSTracker(activity);
                            //                            // check if GPS enabled
                            if (gps.canGetLocation()) {
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                            } else {
                                gps.showSettingsAlert();
                            }
                        }else {
                            Toast.makeText(getContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRequest() {
                        Ask.on(activity)
                                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
                                .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                .withRationales("Anda perlu mengizinkan Location Permission")//optional
                                .go();
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        Ask.on(activity)
                                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
                                .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                .withRationales("Anda perlu mengizinkan Location Permission")//optional
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

    public boolean adaKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }



}
