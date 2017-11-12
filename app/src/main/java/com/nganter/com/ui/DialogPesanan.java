package com.nganter.com.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nganter.com.R;
import com.nganter.com.objek.Pesanan;

/**
 * Created by aji on 11/11/2017.
 */

public class DialogPesanan extends Dialog {
    private Activity activity;
    private Pesanan pesanan;
    private TextView user,pesananTv;
    private Button berikanRating;
    public DialogPesanan(Activity activity,Pesanan pesanan){
        super(activity);
        this.activity = activity;
        this.pesanan = pesanan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pesanan);
        user = (TextView)findViewById(R.id.nama_pemesan);
        pesananTv = (TextView)findViewById(R.id.pesanan_detail);
        berikanRating = (Button)findViewById(R.id.berikan_rating);
        user.setText("Halo "+pesanan.getPemesan());
        pesananTv.setText(pesanan.getIsiPesanan());

        berikanRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogRating dialogRating = new DialogRating(activity,pesanan.getIdPesanan());
                dialogRating.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogRating.show();
                dismiss();
            }
        });
    }
}
