package com.nganter.com.pesantiket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nganter.com.R;
import com.nganter.com.objek.Film;
import com.nganter.com.ui.ExpandGridView;
import com.nganter.com.ui.HalamanUtama;
import com.nganter.com.ui.HalamanUtamaAdapter;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class PesanTiketActivity extends AppCompatActivity {
    private ExpandGridView expandGridView;
    private ArrayList<Film> films;
    public static final String BIOSKOP = "bioskop";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_activity);

        expandGridView = (ExpandGridView)findViewById(R.id.grid_film);
        expandGridView.setExpanded(true);
        expandGridView.setFocusable(false);
        setData();
        expandGridView.setAdapter(new PesanTiketAdapter(getApplicationContext(),films,PesanTiketActivity.this));
    }

    public void setData(){
        films = new ArrayList<>();
        Film film = new Film(R.drawable.akun,"Iron Man");
        films.add(film);

        film = new Film(R.drawable.antar_barang,"Kirana");
        films.add(film);
    }
}
