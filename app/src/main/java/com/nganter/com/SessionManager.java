package com.nganter.com;

import android.content.Context;
import android.content.SharedPreferences;

import com.nganter.com.objek.User;

/**
 * Created by Septiawan Aji P on 12/6/2016.
 */
public class SessionManager {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String STATUS_INSTALL = "status_install";
    public static final String KIRIM_DEVICE = "kirim_device";
    public static final String SUDAH = "sudah";
    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void sudahKirim(){
        editor.putString(KIRIM_DEVICE,SUDAH);
        editor.commit();
    }

    public String getStatusKirim() {
        return sharedPreferences.getString(KIRIM_DEVICE,null);
    }

    public void setSessionInstall(){
        editor.putString("install","yes");
        editor.commit();
    }

    public String getInstallStatus(){
        String install = sharedPreferences.getString("install",null);
        return  install;
    }

    public void createLoginSession(String id,String alamat,String nama,String telepon,String wa){
        editor.putString("id",id);
        editor.putString("nama",nama);
        editor.putString("telepon",telepon);
        editor.putString("wa",wa);
        editor.putString("alamat",alamat);
        editor.commit();
    }

    public void setAlamat(String alamat){
        editor.putString("alamat",alamat);
        editor.commit();
    }

    public String getAlamat(){
        String alamat = sharedPreferences.getString("alamat",null);
        return alamat;
    }

    public User getUserAkun(){
        User user = new User();

        user.setIdPelanggan(sharedPreferences.getString("id",null));
        user.setNama(sharedPreferences.getString("nama",null));
        user.setNo_telp(sharedPreferences.getString("telepon",null));
        user.setNo_wa(sharedPreferences.getString("wa",null));
        user.setAlamat(sharedPreferences.getString("alamat",""));
        return user;
    }

    public void deleteSession (){
        sharedPreferences.edit().remove("email").commit();
        sharedPreferences.edit().remove("nama").commit();
        sharedPreferences.edit().remove("path_foto").commit();
        sharedPreferences.edit().remove("telepon").commit();
        sharedPreferences.edit().remove("id").commit();
        sharedPreferences.edit().remove("alamat").commit();

    }

    public void deleteToken(){
        sharedPreferences.edit().remove("token").commit();
    }

}
