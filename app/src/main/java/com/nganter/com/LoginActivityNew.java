package com.nganter.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nganter.com.ui.RegisterActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivityNew extends AppCompatActivity implements OnClickListener, TextWatcher, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int RC_SIGN_IN = 22;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private SessionManager sessionManager;

    //parameter
    private final String TYPE = "type";
    private final String TELEPON = "telepon";
    private final String FB = "fb";
    private final String GOOGLE = "google";
    private final String ID_VERIFICATION = "id_verification";
    private final String USERNAME = "username";

    private String idVerification;
    // UI references.
    private EditText mPhone;
    private Button mButtonDaftar;
    private SignInButton mButtonGoogle;
    private TextView mButtomAction;
    private TextView notRegistered;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        pg = new ProgressDialog(this);
        pg.setMessage("Loading...");

        sessionManager = new SessionManager(getApplicationContext());
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        // Set up the login form.
        mPhone = (EditText) findViewById(R.id.input_phoneemail);
        mButtonDaftar = (Button) findViewById(R.id.input_daftar_button);
        mButtonGoogle = (SignInButton) findViewById(R.id.sign_in_google);
        mButtomAction = (TextView) findViewById(R.id.text_action);
        notRegistered = (TextView)findViewById(R.id.phone_not_registered);

        mPhone.addTextChangedListener(this);
        mButtonGoogle.setOnClickListener(this);
        mButtonDaftar.setOnClickListener(this);
        mButtomAction.setOnClickListener(this);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                updateUI(STATE_VERIFY_SUCCESS, phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(LoginActivityNew.this, "Invalid number phone", Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]

                }

                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(TAG, "onCodeSent:" + s);
                mVerificationId = s;
                mResendToken = forceResendingToken;
            }
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 11/25/2017 check is user already logged in

        //example code for logout
        //begin
        FirebaseAuth.getInstance().signOut();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            Log.d(TAG, "signout");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }

        //end


        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhone.getText().toString());
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

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhone.setError("Invalid phone number.");
            //mPhoneNumberField.setTextColor(Color.parseColor("#ff1744"));
            return false;
        }

        return true;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]

                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int  uiState,FirebaseUser user,PhoneAuthCredential cred){
        switch (uiState){
            case STATE_INITIALIZED:
                break;
            case STATE_CODE_SENT:
                Log.d("Shinta","kode terkirim");
                break;
            case STATE_VERIFY_FAILED:
                Log.d("Shinta","verifikasi gagal");
                break;
            case STATE_VERIFY_SUCCESS:
                Log.d("Shinta","Verifikasi sukses");
                if(cred !=null){
                    if(cred.getSmsCode() != null){
                        Log.d("Shinta","smscode : "+cred.getSmsCode());
                    }else {
                        Log.d("Shinta","creednull");
                    }
                }
                break;
            case STATE_SIGNIN_FAILED:
                notRegistered.setText("Verifikasi gagal");
                mButtonDaftar.setVisibility(View.VISIBLE);
                Log.d("Shinta","sign in failde");
                break;
            case STATE_SIGNIN_SUCCESS:
                notRegistered.setText("Verifikasi sukses");
                Toast.makeText(this, "Anda telah login !", Toast.LENGTH_SHORT).show();
                Log.d("Shinta","Anda telah login !");
                break;
        }

        if(user == null){
            Log.d("Shinta","Anda tidak loginn");
        }else{
            getAccessToken(mPhone.getText().toString(),user.getUid());

        }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    //mengirim kode jika tidak bisa otomatis
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.input_daftar_button:{
                accountCheck(mPhone.getText().toString());
                break;
            }
            case R.id.text_action:{
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            }
            case R.id.sign_in_google:{
                signInGoogle();
                pg.show();
                break;
            }
        }
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length() > 0){
            mButtonDaftar.setEnabled(true);
            mButtonDaftar.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorAccent));
        }else{
            mButtonDaftar.setEnabled(false);
            mButtonDaftar.setBackgroundTintList(this.getResources().getColorStateList(android.R.color.darker_gray));
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() > 8;
    }

    public void accountCheck(final String userInput){

        if (!TextUtils.isEmpty(userInput) && !isPhoneValid(userInput)) {
            mPhone.setError(getString(R.string.error_invalid_phone));
        }else{
            notRegistered.setVisibility(View.GONE);
            mButtonDaftar.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{

                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("success")){
                            notRegistered.setVisibility(View.VISIBLE);
                            notRegistered.setText(jsonObject.getString("message"));
                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            startPhoneNumberVerification(userInput);

                        }else{
                            notRegistered.setVisibility(View.VISIBLE);
                            notRegistered.setText(jsonObject.getString("message"));
                            mButtonDaftar.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        Log.d("accontCheckError",e.toString());
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
                    map.put(TELEPON,userInput);
                    return map;
                }
            };

            NganterApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }

    }

    public void getAccessToken(final String phone,final String idVerification){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Toast.makeText(LoginActivityNew.this, response, Toast.LENGTH_SHORT).show();

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
                map.put(ID_VERIFICATION,idVerification);
                return map;
            }
        };

        NganterApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Login Google Failed");
        pg.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //pass to facebook callback
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "on result");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInGoogleResult(result);
        }
    }

    private void handleSignInGoogleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d(TAG, "result success");
            // Signed in successfully, show authenticated UI.
            pg.show();
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            pg.dismiss();
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
                        pg.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(GOOGLE, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(GOOGLE, "login with Google success");
                            Log.d(GOOGLE, "name : "+user.getDisplayName());
                            Log.d(GOOGLE, "Uid : "+user.getUid());
                            authUid(user.getDisplayName(),user.getUid(),"google");
                            // TODO: get user detail
//                            user.getEmail();
//                            user.getPhoneNumber();
//                            user.getPhotoUrl();
                            // TODO: start activity
//                            startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
//                            finish();
                            if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
                                Log.d(TAG, "signout");
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivityNew.this, "Log in fails", Toast.LENGTH_SHORT).show();
                        }
//                        mGoogleApiClient.clearDefaultAccountAndReconnect();
                    }
                });
    }

    public void authUid(final String username, final String uid,final String type){
        Log.d("yusi",username);
        Log.d("yusi",uid);
        Log.d("yusi",type);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

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
                map.put(ID_VERIFICATION,uid);
                map.put(USERNAME,username);
                map.put(TYPE,type);
                return map;
            }
        };

        NganterApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}

