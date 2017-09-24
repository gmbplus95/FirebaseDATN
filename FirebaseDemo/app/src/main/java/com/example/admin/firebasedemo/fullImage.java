package com.example.admin.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

public class fullImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        ZoomageView imageView= (ZoomageView) findViewById(R.id.fullImage);
        Intent i =getIntent();
        String img=i.getStringExtra("img");
        Glide.with(this)
                .load(img)
                .into(imageView);

    }
}
