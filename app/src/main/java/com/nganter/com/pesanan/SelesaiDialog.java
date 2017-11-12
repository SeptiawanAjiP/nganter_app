package com.nganter.com.pesanan;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nganter.com.R;
import com.nganter.com.objek.Pesanan;
import com.nganter.com.pesanbarang.PesanBarang;

/**
 * Created by aji on 11/10/2017.
 */

public class SelesaiDialog extends Dialog {
    private Activity activity;
    private Pesanan pesanan;
    private Button pesanKembali,tutup;
    private TextView judul,pesananTv;
    private Pesanan newPesanan;
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
                    PesanBarang cdd = new PesanBarang(activity,"Beli Barang",newPesanan.getToko(),newPesanan.getIsiPesanan());
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }else if(pesanan.getJenis().equals("pesan_makanan")){
                    newPesanan.setToko(text[1].replace(",Pesanan",""));
                    newPesanan.setIsiPesanan(text[2]);
                    PesanBarang cdd = new PesanBarang(activity,"Pesan Makanan",newPesanan.getToko(),newPesanan.getIsiPesanan());
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }else if(pesanan.getJenis().equals("antar_barang")){
                    Toast.makeText(activity, "Hanya bisa untuk pemesanan barang atau makanan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
