package com.nganter.com.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nganter.com.LoginActivityNew;
import com.nganter.com.R;
import com.nganter.com.SessionManager;
import com.nganter.com.handler.AppContoller;
import com.nganter.com.koneksi.Alamat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button button;
    private TextView textView;
    private SessionManager session;
    private EditText email,password;
    private ProgressDialog progressDialog;
    private SignInButton mButtonGoogle;
    private GoogleApiClient mGoogleApiClient;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private static final String TAG = "nganter_new";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;
    private static final int RC_SIGN_IN = 22;
    private final String GOOGLE = "google";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        button = (Button)findViewById(R.id.btn_login);
        textView = (TextView)findViewById(R.id.blm_punya_akun);
        email = (EditText)findViewById(R.id.email_register);
        password= (EditText)findViewById(R.id.password_register);
        session = new SessionManager(getApplicationContext());
        mButtonGoogle = (SignInButton) findViewById(R.id.sign_in_google);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
                showProgress();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty()){
                    if(!password.getText().toString().isEmpty()){

                        if(adaKoneksi()){
                            login(email.getText().toString(),password.getText().toString());
                        }else{
                            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(LoginActivity.this, "Password Kosong", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Email Kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            Log.d(TAG, "signout");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS,mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInGoogleResult(result);
        }
    }

    private void handleSignInGoogleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d(TAG, "result success");
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            progressDialog.dismiss();
            Log.d(TAG, "result failed");
            Log.d(TAG, result.getStatus().toString());
            Log.d(TAG, "status code : "+result.getStatus().getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(GOOGLE, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(GOOGLE, "login with Google success");
                            Log.d(GOOGLE, "name : "+user.getDisplayName());
                            Log.d(GOOGLE, "Uid : "+user.getUid());
                            loginGoogle(user.getEmail());
                            if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
                                Log.d(TAG, "signout");
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            }
                            progressDialog.dismiss();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Log in fails", Toast.LENGTH_SHORT).show();
                        }
//                        mGoogleApiClient.clearDefaultAccountAndReconnect();
                    }
                });
    }


    public void login(final String email, final String password){
        showProgress();
        StringRequest string = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__login",response);
                try{
                    JSONObject k = new JSONObject(response);
                    JSONArray array = k.getJSONArray("respon");
                    Log.d("__login_array",""+array.length());
                    if(array.length()!=0){
                        for (int i=0;i<array.length();i++){
                            JSONObject j = array.getJSONObject(i);
                            Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                            session.createLoginSession(j.getString("id_pelanggan"),j.getString("alamat"),j.getString("username"),j.getString("no_telp"),j.getString("no_wa"));
                            Intent intent = new Intent(getApplicationContext(),HalamanUtama.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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

    public void loginGoogle(final String email){
        StringRequest string = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("__login",response);
                try{
                    JSONObject k = new JSONObject(response);
                    JSONArray array = k.getJSONArray("respon");
                    Log.d("__login_array",""+array.length());
                    if(array.length()!=0){
                        for (int i=0;i<array.length();i++){
                            JSONObject j = array.getJSONObject(i);
                            Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                            session.createLoginSession(j.getString("id_pelanggan"),j.getString("alamat"),j.getString("username"),j.getString("no_telp"),j.getString("no_wa"));
                            Intent intent = new Intent(getApplicationContext(),HalamanUtama.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Email belum terdaftar. Lakukan registrasi terlebih dulu", Toast.LENGTH_SHORT).show();
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
                map.put("kode","sign_in_google");
                map.put("email",email);
                return map;
            }
        };

        AppContoller.getInstance(getApplicationContext()).addToRequestQueue(string);
    }
    private void showProgress() {
        progressDialog = null;// Initialize to null
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public boolean adaKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
