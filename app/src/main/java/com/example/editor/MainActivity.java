package com.example.editor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sticker.BitmapStickerIcon;
import com.example.sticker.DeleteIconEvent;
import com.example.sticker.DrawableSticker;
import com.example.sticker.FlipHorizontallyEvent;
import com.example.sticker.Sticker;
import com.example.sticker.StickerView;
import com.example.sticker.TextSticker;
import com.example.sticker.ZoomIconEvent;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    StickerView stickerView;
    String picturePath;
    String fileName="image";
    Uri outuri;
    int MY_PERMISSIONS_REQUEST_CAMERA=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void creation(View view) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},6);
        }
        else
        {
            opencreation();
        }


    }

    public void opencreation()
    {
        Intent i = new Intent(this, Grid.class);
        startActivity(i);
    }

    public void camera(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions( new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_CAMERA);
        }
        else
        {
            picturePath=null;
            openCamera();
        }
    }

    public void gallery(View view) {
        gal();
    }

    private void gal() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 2);
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},12);
        }
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
            fileName = fileName + System.currentTimeMillis() + ".jpg";
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Image Editor/");
            dir.mkdirs();
            File outFile = new File(dir, fileName);

            outuri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", outFile);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},99);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_PERMISSIONS_REQUEST_CAMERA)
        {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(grantResults[1]==PackageManager.PERMISSION_GRANTED)
                    openCamera();
            }
            else {
                Toast.makeText(this, "Permission rejected", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==6)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                opencreation();
            }
            else
            {
                Toast.makeText(this, "Permission rejected", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==99)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                openCamera();
            }
            else
            {
                Toast.makeText(this, "Storage Permission not given", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==12)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                gal();
            }
            else
            {
                Toast.makeText(this, "Storage Permission not given", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK)
        {
            if(outuri!=null)
            {
                picturePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Image Editor/"+fileName;
                 Intent i = new Intent(MainActivity.this,Edit.class);
                 i.putExtra("uri",outuri);
                 i.putExtra("path",picturePath);
                 startActivity(i);
            }
        }
        if(requestCode==2 && data!=null) {
            outuri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(outuri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            Intent i = new Intent(MainActivity.this,Edit.class);
            i.putExtra("uri",outuri);
            i.putExtra("path",picturePath);
            startActivity(i);
        }
    }
}

