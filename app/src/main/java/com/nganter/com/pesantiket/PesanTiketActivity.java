package com.nganter.com.pesantiket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nganter.com.R;
import com.nganter.com.handler.AppContoller;
import com.nganter.com.koneksi.Alamat;
import com.nganter.com.objek.Film;
import com.nganter.com.ui.ExpandGridView;
import com.nganter.com.ui.HalamanUtama;
import com.nganter.com.ui.HalamanUtamaAdapter;
import com.nganter.com.ui.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class PesanTiketActivity extends AppCompatActivity {
    private ExpandGridView expandGridView;
    private TextView namaBioskop;
    private ArrayList<Film> films;
    public static final String BIOSKOP = "bioskop";
    private ProgressDialog progressDialog;
    int idBioskop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_activity);
        namaBioskop = (TextView)findViewById(R.id.nama_bioskop);
        namaBioskop.setText(getIntent().getStringExtra(BIOSKOP));
        films = new ArrayList<>();
        if(getIntent().getStringExtra(BIOSKOP).equals("Rajawali")){
            idBioskop=1;
        }else{
            idBioskop=2;
        }

        expandGridView = (ExpandGridView)findViewById(R.id.grid_film);
        expandGridView.setExpanded(true);
        expandGridView.setFocusable(false);
        setData(idBioskop);
        Log.d("__film_arra",films.toString());

    }

    public void setData(final int idBisokop){
        Log.d("jalan","kl");
        showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject k = new JSONObject(response);
                    JSONArray array = k.getJSONArray("respon");
                    Log.d("__film",array.toString());
                    if(array.length()!=0){
                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            Log.d("__film_1",jsonObject.toString());
                            Film film = new Film(jsonObject.getString("id_film"),jsonObject.getString("judul_film"),jsonObject.getString("path_foto"));
                            films.add(film);
                            Log.d("__film_arra_2",films.toString());

                        }
                        expandGridView.setAdapter(new PesanTiketAdapter(getApplicationContext(),films,PesanTiketActivity.this,getIntent().getStringExtra(BIOSKOP)));
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(PesanTiketActivity.this, "Tidak dapat load data, mohon ulangi lagi", Toast.LENGTH_SHORT).show();
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
                map.put("kode","film");
                map.put("id_bioskop",Integer.toString(idBisokop));
                return map;
            }
        };
        AppContoller.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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
