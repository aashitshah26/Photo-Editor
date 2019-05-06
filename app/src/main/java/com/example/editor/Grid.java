package com.example.editor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;

public class Grid extends AppCompatActivity {
    ArrayList<String> paths = new ArrayList<>();
    ProgressBar bar ;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        bar=findViewById(R.id.progress);
        new getPics().execute();
    }
    private class getPics extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }



        @Override
        protected Void doInBackground(Void... voids) {
            File filepath = Environment.getExternalStorageDirectory();
            String path=filepath.getAbsolutePath() + "/Pictures/Sticker/";
            File directory = new File(path); //path is the string specifying your directory path.
            File[] files = directory.listFiles();
            if(files!=null) {
                for (int i = 0; i < files.length; i++) {
                    Log.e("Files", "FileName:" + files[i].getName()); //these are the different filenames in the directory
                    File imgFile = new File(path + files[i].getName());
                    if (imgFile.exists()) {
                        paths.add(imgFile.getAbsolutePath());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView = (RecyclerView) findViewById(R.id.recycle1);

            imageadapter adapter = new imageadapter(paths,Grid.this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Grid.this, 3);
            recyclerView.setLayoutManager(mLayoutManager);



            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addOnItemTouchListener(new RecylerOnClickListener(Grid.this, recyclerView, new RecylerOnClickListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent i = new Intent(Grid.this,ShowOne.class);
                    i.putExtra("path",paths.get(position));
                    startActivity(i);

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            recyclerView.setAdapter(adapter);
            bar.setVisibility(View.INVISIBLE);
        }
    }
}
