package com.example.editor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.editor.util.FileUtil;
import com.example.sticker.DrawableSticker;
import com.example.sticker.Sticker;
import com.example.sticker.StickerView;
import com.example.sticker.TextSticker;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.IOException;

public class Edit extends AppCompatActivity {
     RecyclerView recyclerView;
     StickerView stickerView;
     EditText editText;
     ImageView imageView;
    public int mThumbnails[] = {R.drawable.angry,R.drawable.angry1,R.drawable.bored,R.drawable.bored1
            ,R.drawable.bored2,R.drawable.confused,R.drawable.confused1,R.drawable.crying,R.drawable.crying1
            ,R.drawable.embarrassed,R.drawable.emoticons,R.drawable.happy,R.drawable.happy1,R.drawable.happy2
            ,R.drawable.happy3,R.drawable.happy4,R.drawable.ill,R.drawable.inlove,R.drawable.kissing,R.drawable.mad
            ,R.drawable.nerd,R.drawable.unhappy,R.drawable.tongueout1,R.drawable.suspicious1,R.drawable.wink
            ,R.drawable.surprised1,R.drawable.smiling,R.drawable.smile,R.drawable.smart,R.drawable.secret
            ,R.drawable.sad,R.drawable.quiet,R.drawable.ninja};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
       stickerView = findViewById(R.id.sticker);
       stickerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
           @Override
           public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
               stickerView.getLayoutParams().width=stickerView.getWidth();
               stickerView.getLayoutParams().height=stickerView.getHeight();
           }
       });
       editText = findViewById(R.id.edt);
       imageView = findViewById(R.id.edit_image);
       Uri uri = getIntent().getParcelableExtra("uri");
       if(uri!=null)
       {
           String path = getIntent().getStringExtra("path");
           imageView.setImageURI(uri);
           Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
           try {
               ExifInterface ei = new ExifInterface(path);
               int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
               switch (orientation) {
                   case ExifInterface.ORIENTATION_ROTATE_90:
                       Toast.makeText(this, "90", Toast.LENGTH_SHORT).show();
                       bitmap = rotateImage(bitmap, 90);
                       break;
                   case ExifInterface.ORIENTATION_ROTATE_180:
                       Toast.makeText(this, "180", Toast.LENGTH_SHORT).show();
                       bitmap =  rotateImage(bitmap, 180);
                       break;
                   case ExifInterface.ORIENTATION_ROTATE_270:
                       Toast.makeText(this, "270", Toast.LENGTH_SHORT).show();
                       bitmap = rotateImage(bitmap, 270);
                       break;
                   default:

               }
           } catch (IOException e) {
               e.printStackTrace();
           }

           imageView.setImageBitmap(bitmap);

       }

        recyclerView=findViewById(R.id.recycle);
        imageadapter adapter = new imageadapter(mThumbnails,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecylerOnClickListener(this, recyclerView, new RecylerOnClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Drawable drawable = ContextCompat.getDrawable(Edit.this,mThumbnails[position]);
                stickerView.addSticker(new DrawableSticker(drawable));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setAdapter(adapter);


        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerClicked(@NonNull final Sticker sticker) {
                if(sticker instanceof TextSticker) {
                    ColorPickerDialogBuilder
                            .with(Edit.this)
                            .setTitle("Choose color")
                            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                            .density(12)
                            .setPositiveButton("ok", new ColorPickerClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                    ((TextSticker) sticker).setTextColor(selectedColor);
                                    stickerView.replace(sticker);
                                    stickerView.invalidate();
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .build()
                            .show();
                }
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {

            }
        });
    }

    private Bitmap rotateImage(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedImg;
    }

    public void add(View view) {
        String val = editText.getText().toString();
        if(!val.trim().equals("") && val!=null && !val.isEmpty()) {
            TextSticker sticker = new TextSticker(this);
            sticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.sticker_transparent_background));
            sticker.setText(val.trim());
            sticker.setTextColor(Color.BLACK);
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
            sticker.resizeText();
            stickerView.addSticker(sticker);
        }
    }

    public void save(View view) {
        File file = FileUtil.getNewFile(this, "Sticker");
        if (file != null) {
            stickerView.save(file);
            Toast.makeText(this, "saved in " + file.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
            stickerView.removeAllStickers();
            Intent i =new Intent(this,ShowOne.class);
            i.putExtra("path",file.getAbsolutePath());
            startActivity(i);
        } else {
            Toast.makeText(this, "Problem in storing", Toast.LENGTH_SHORT).show();
        }

    }
}
