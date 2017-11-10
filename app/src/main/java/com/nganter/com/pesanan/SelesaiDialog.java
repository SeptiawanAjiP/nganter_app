package com.nganter.com.pesanan;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nganter.com.R;
import com.nganter.com.objek.Pesanan;

/**
 * Created by aji on 11/10/2017.
 */

public class SelesaiDialog extends Dialog {
    private Activity activity;
    private Pesanan pesanan;
    private Button pesanKembali,tutup;
    public SelesaiDialog(Activity activity, Pesanan pesanan){
        super(activity);
        this.activity = activity;
        this.pesanan = pesanan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detil_selesai);
        setCancelable(false);
        pesanKembali = (Button)findViewById(R.id.pesan_kembali);
        tutup = (Button)findViewById(R.id.tutup);
        tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        pesanKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
