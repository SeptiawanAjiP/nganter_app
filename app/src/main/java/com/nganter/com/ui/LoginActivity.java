package com.nganter.com.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private SessionManager session;
    private EditText email,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        button = (Button)findViewById(R.id.btn_login);
        textView = (TextView)findViewById(R.id.blm_punya_akun);
        email = (EditText)findViewById(R.id.email_register);
        password= (EditText)findViewById(R.id.password_register);
        session = new SessionManager(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty()){
                    if(!password.getText().toString().isEmpty()){

                    }else{
                        Toast.makeText(LoginActivity.this, "Password Kosong", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Email Kosong", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),HalamanUtama.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(final String email,  final String password){
        StringRequest string = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject j = new JSONObject(response);
                    if(!j.isNull("respon")){
                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                        session.createLoginSession(j.getString("id_pelanggan"),j.getString("alamat"),j.getString("username"),j.getString("no_telp"),j.getString("no_wa"));
                        Intent intent = new Intent(getApplicationContext(),HalamanUtama.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
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
                map.put("kode","login");
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };

        AppContoller.getInstance(getApplicationContext()).addToRequestQueue(string);
    }
}
