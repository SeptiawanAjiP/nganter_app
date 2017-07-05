package com.nganter.com.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class RegisterActivity extends AppCompatActivity {
    private Button button;
    private EditText username,email,password,telepon,alamat;
    SessionManager session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        button = (Button)findViewById(R.id.btn_daftar);
        username = (EditText) findViewById(R.id.nama_register);
        email = (EditText)findViewById(R.id.email_register);
        password = (EditText)findViewById(R.id.password_register);
        telepon = (EditText)findViewById(R.id.telepon_register);
        alamat = (EditText)findViewById(R.id.alamat_register);
        button = (Button)findViewById(R.id.btn_daftar);
        session = new SessionManager(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.getText().toString().isEmpty()){
                    if(!email.getText().toString().isEmpty()){
                        if(!password.getText().toString().isEmpty()){
                                if(!telepon.getText().toString().isEmpty()){
                                    if(!alamat.getText().toString().isEmpty()){
                                        daftar(username.getText().toString()
                                                ,email.getText().toString()
                                                ,password.getText().toString()
                                                ,telepon.getText().toString(),alamat.getText().toString());
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Alamat Kosong", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Telepon Kosong", Toast.LENGTH_SHORT).show();
                                }
                        }else{
                            Toast.makeText(RegisterActivity.this, "Password Kosong", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Email Kosong", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Nama Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void daftar(final String nama, final String email, final String password, final String no_telp,final String alamat){
        StringRequest string = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject j = new JSONObject(response);
                    if(j.getString("status").equals("1")){
                        Toast.makeText(RegisterActivity.this, "Register Berhasil", Toast.LENGTH_LONG).show();
                        login(email,password);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("preeet",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("kode","register");
                map.put("username",nama);
                map.put("email",email);
                map.put("password",password);
                map.put("no_telp",no_telp);
                map.put("no_wa",no_telp);
                map.put("alamat",alamat);
                return map;
            }
        };

        AppContoller.getInstance(getApplicationContext()).addToRequestQueue(string);
    }

    public void login(final String email,  final String password){
        StringRequest string = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject j = new JSONObject(response);
                        session.createLoginSession(j.getString("id_pelanggan"),j.getString("alamat"),j.getString("username"),j.getString("no_telp"),j.getString("no_wa"));
                        Intent intent = new Intent(getApplicationContext(),HalamanUtama.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("preeet",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("kode","login");
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };

        AppContoller.getInstance(getApplicationContext()).addToRequestQueue(string);
    }
}
