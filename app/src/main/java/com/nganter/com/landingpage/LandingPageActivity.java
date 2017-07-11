package com.nganter.com.landingpage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.nganter.com.R;
import com.nganter.com.ui.HalamanUtama;
import com.nganter.com.ui.LoginActivity;
import com.nganter.com.ui.RegisterActivity;

/**
 * Created by Septiawan Aji Pradan on 5/31/2017.
 */

public class LandingPageActivity extends AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage1= new SliderPage();


        addSlide(AppIntroFragment.newInstance("Pengin makan ? pesan makanan lewat kami","",R.drawable.ic_noodles,Color.TRANSPARENT));


        addSlide(AppIntroFragment.newInstance("Beli dan Antar Barang ? hubungi kami","",R.drawable.ic_open_cardboard_box,Color.TRANSPARENT));


        addSlide(AppIntroFragment.newInstance("Pengin nonton ? kami pesankan tiketnya","",R.drawable.ic_tickets,Color.TRANSPARENT));

        // Declare a new image view
        ImageView imageView = new ImageView(this);

        // Bind a drawable to the imageview
        imageView.setImageResource(R.drawable.back_ground);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Set background color
        // Set layout params
        imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Bind the background to the intro
        setBackgroundView(imageView);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
