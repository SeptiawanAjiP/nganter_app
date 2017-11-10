package com.nganter.com.pesanan;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nganter.com.R;

/**
 * Created by aji on 11/10/2017.
 */

public class DalamProsesDialog extends Dialog {
    private Activity activity;
    private String pesanan;
    private Button telp,wa;
    private static final String NOMOR = "6282227330933";

    public DalamProsesDialog(Activity activity,String pesanan){
        super(activity);
        this.activity = activity;
        this.pesanan = pesanan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detil_dalam_proses);
        telp = (Button)findViewById(R.id.telp_admin);
        wa = (Button)findViewById(R.id.wa_admin);

        telp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callContact(NOMOR ,activity.getApplicationContext());
            }
        });

        wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp(NOMOR);
            }
        });


    }

    public void callContact(String nomor, Context mContext){
        Uri uri = Uri.parse("tel:" + "+"+nomor);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);

        try {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(it);
        }catch(android.content.ActivityNotFoundException ex){
            Toast.makeText(mContext,"Failed Call",Toast.LENGTH_SHORT).show();
        }
    }

    private void openWhatsApp(String waNumber) {

        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(NOMOR) + "@s.whatsapp.net");//phone number without "+" prefix
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(activity, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            activity.startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = activity.getApplicationContext().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
