package com.nganter.com.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nganter.com.NganterApp;
import com.nganter.com.R;
import com.nganter.com.koneksi.Alamat;
import com.nganter.com.objek.Pesanan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aji on 11/11/2017.
 */

public class DialogRating extends Dialog {
    private Activity activity;
    private RatingBar ratingBar;
    private EditText saran;
    private Button kirim;
    private String rating;
    private int idOrder;
    public DialogRating(Activity activity,int idOrder){
        super(activity);
        this.activity = activity;
        this.idOrder = idOrder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_rating);
        rating = "0";
        ratingBar = (RatingBar)findViewById(R.id.ratingBar_modal);
        saran = (EditText)findViewById(R.id.rating_input);
        kirim = (Button)findViewById(R.id.btn_kirim);
        addListernerOnRatingBar();
        addListernerOnButton();
    }

    public void addListernerOnRatingBar(){
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = String.valueOf(v).replace(".0","");
//                Toast.makeText(activity, rating, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addListernerOnButton(){

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rating.equals("0")||saran.getText().toString().equals("")){
                    Toast.makeText(activity, "Rating dan saran harus terisi", Toast.LENGTH_SHORT).show();
                }
                kirim(rating,saran.getText().toString());
            }
        });
    }

    public void kirim(final String rating, final String saran){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        Toast.makeText(activity, "Input rating dan saran berhasil", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }catch (Exception e){
                    Toast.makeText(activity, "Input rating gagal,mohon ulangi kembali", Toast.LENGTH_SHORT).show();
                    dismiss();
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
                map.put("kode","input_rating");
                map.put("id_order",Integer.toString(idOrder));
                map.put("rating",rating);
                map.put("saran",saran);
                return map;
            }
        };
        NganterApp.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
