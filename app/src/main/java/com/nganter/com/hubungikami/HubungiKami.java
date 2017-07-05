package com.nganter.com.hubungikami;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nganter.com.R;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class HubungiKami extends AppCompatActivity {

    private static final String NOMOR = "6282227330933";
    private LinearLayout wa,sms,telp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hubungi_kami_activity);
        wa = (LinearLayout) findViewById(R.id.rl_kontak_cp_dev);
        sms = (LinearLayout)findViewById(R.id.rl_kontak_cp_dev_sms);
        telp = (LinearLayout)findViewById(R.id.rl_kontak_cp_dev_telp);
        wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp(NOMOR);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(NOMOR,getApplicationContext());
            }
        });
        telp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContact(NOMOR,getApplicationContext());
            }
        });

    }

    private void openWhatsApp(String waNumber) {

        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(NOMOR) + "@s.whatsapp.net");//phone number without "+" prefix
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getApplicationContext().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void sendMessage(String nomor, Context mContext){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , nomor);
        smsIntent.putExtra("sms_body"  , "");

        try {
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(smsIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
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
}
