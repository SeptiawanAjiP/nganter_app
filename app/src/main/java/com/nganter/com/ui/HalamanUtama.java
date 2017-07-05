package com.nganter.com.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.nganter.com.R;
import com.nganter.com.SessionManager;
import com.nganter.com.objek.MenuUtama;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class HalamanUtama extends AppCompatActivity {
    private ExpandGridView expandGridView;
    private ArrayList<MenuUtama> menuUtamas;
    SessionManager sessionManager;
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
        expandGridView.setAdapter(new HalamanUtamaAdapter(getApplicationContext(),menuUtamas,HalamanUtama.this));
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
}
