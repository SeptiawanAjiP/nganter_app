package com.nganter.com.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 7/13/2017.
 */

public class ModalEditProfil extends Dialog {
    private Activity activity;
    private SessionManager sessionManager;
    private EditText nama,telp,alamat;
    private Button logout,update;
    private ProgressDialog progressDialog;
    public ModalEditProfil(Activity activity){
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_akun);
        sessionManager = new SessionManager(activity.getApplicationContext());
        nama = (EditText)findViewById(R.id.et_nama);
        telp = (EditText)findViewById(R.id.et_telp);
        alamat = (EditText)findViewById(R.id.et_alamat);
        logout = (Button)findViewById(R.id.btn_logout);
        update = (Button)findViewById(R.id.btn_update);
        nama.setText(sessionManager.getUserAkun().getNama());
        telp.setText(sessionManager.getUserAkun().getNo_telp());
        alamat.setText(sessionManager.getUserAkun().getAlamat());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.deleteSession();
                Intent intent = new Intent(activity,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nama.getText().toString().equals("")){
                    if(!telp.getText().toString().equals("")){
                        if(!alamat.getText().toString().equals("")){
                            updateProfil(sessionManager.getUserAkun().getIdPelanggan(),nama.getText().toString(),
                                    telp.getText().toString(),alamat.getText().toString());
                        }
                    }
                }
            }
        });
    }

    public void updateProfil(final String id, final String nama, final String telp, final String alamat){
        showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__update_profil",response);
                try{
                    JSONObject j = new JSONObject(response);
                    if(j.getString("respon").equals("1")){
                        JSONArray jsonArray = j.getJSONArray("data");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        sessionManager.createLoginSession(jsonObject.getString("id_pelanggan"),
                                jsonObject.getString("alamat"),jsonObject.getString("username"),
                                jsonObject.getString("no_telp"),jsonObject.getString("no_wa"));
                        Toast.makeText(activity, "Berhasil Update Profil", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        dismiss();
                    }else{
                        Toast.makeText(activity, "Update profil gagal", Toast.LENGTH_SHORT).show();
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
                Map<String,String> maps = new HashMap<>();
                maps.put("kode","update_profil");
                maps.put("id_pelanggan",id);
                maps.put("username",nama);
                maps.put("no_wa",telp);
                maps.put("alamat",alamat);
                return maps;
            }
        };

        AppContoller.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void showProgress() {
        progressDialog = null;// Initialize to null
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
