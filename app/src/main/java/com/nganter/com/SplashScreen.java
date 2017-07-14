package com.nganter.com;

/**
 * Created by Kaddafi on 10/28/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nganter.com.landingpage.WelcomeActivity;
import com.nganter.com.ui.HalamanUtama;
import com.nganter.com.ui.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    HashMap<String,String> hm ;
    private ArrayList<String> tipeKuliner;
    private ArrayList<String> kawasan;
    private RequestQueue queue;

    private View mTarget;
    SessionManager sessionManager;

    private HashMap<String,String> tempatMakanHashmapFavorit;

//    private SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
//        mTarget = findViewById(R.id.foto_terbaru_1);
//        sm = new SessionManager(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());
        startAnimation();
    }


    private void startAnimation() {
//        rope =  YoYo.with(Techniques.FadeInUp)
//                .duration(1500)
//                .interpolate(new AccelerateDecelerateInterpolator())
//                .playOn(mTarget);

        Thread splash = new Thread(){
            public void run(){
                try{
                    sleep(2000);

//                    if(sm.getFirstInstall()!=null){
//                        if (adaKoneksi()){
//                            if(sm.getStatusKirim()==null){
//                                StringRequest login = new StringRequest(Request.Method.POST, AlamatServer.getAlamatServer()+ FixApiMethod.getKirimDeviceToken(), new Response.Listener<String>(){
//                                    @Override
//                                    public void onResponse(String response) {
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(response.toString());
//                                            Log.d("device_token_first",jsonObject.getString("status"));
//                                            sm.sudahKirim();
//                                            Log.d("device_token_status",sm.getStatusKirim());
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                            Log.d("error", e.toString());
//                                        }
//                                    }
//                                }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//
//                                    }
//                                }) {
//                                    @Override
//                                    protected Map<String, String> getParams() throws AuthFailureError {
//                                        Map<String, String> param = new HashMap<String, String>();
//                                        param.put("device_token",sm.getDeviceToken());
//                                        return param;
//                                    }
//                                };
//                                queue.add(login);
//                            }else{
//                                Log.d("device_token_status",sm.getStatusKirim());
//                            }
//                            Intent intent = new Intent(getApplicationContext(), Homepage.class);
//                            startActivity(intent);
//                            finish();
//                        }else {
//                            Intent intent = new Intent(getApplicationContext(), TidakAdaKoneksi.class);
//                            startActivity(intent);
//                            finish();
//                        }
//
//
//                    }else{
//                        Intent intent = new Intent(getApplicationContext(), WelcomScreen.class);
//                        startActivity(intent);
//                        finish();
//                    }


                        if(sessionManager.getUserAkun().getNo_wa()!=null){
                            Intent intent = new Intent(getApplicationContext(), HalamanUtama.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }


                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }

    public boolean adaKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
