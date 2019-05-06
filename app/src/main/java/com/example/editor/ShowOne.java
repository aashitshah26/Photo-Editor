package com.example.editor;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ShowOne extends AppCompatActivity {
    ImageView img;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         path =getIntent().getStringExtra("path");
        img=findViewById(R.id.image1);
        Glide.with(this).load(path).into(img);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.inflateMenu(R.menu.share);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    if(menuItem.getItemId()==R.id.item_save)
                    {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        File imageFileToShare = new File(path);
                        Uri uri = FileProvider.getUriForFile(ShowOne.this,BuildConfig.APPLICATION_ID + ".provider",imageFileToShare);
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(share, "Share Image!"));
                    }
                    return true;
                }
            });
        }
    }

}
