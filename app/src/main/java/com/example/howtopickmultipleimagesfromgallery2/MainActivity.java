package com.example.howtopickmultipleimagesfromgallery2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    Button pick;
    ArrayList<Uri> uri = new ArrayList<>();
    RecyclerAdapter adapter;
    private static final int Read_Permission = 101;
    private static final int PICK_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.totalPhotos);
        recyclerView = findViewById(R.id.recycler_view_Gallery_Images);
        pick = findViewById(R.id.pick);
        adapter = new RecyclerAdapter(uri,getApplicationContext());
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        recyclerView.setAdapter(adapter);




            pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
                    return;
                    }

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }
                    //intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                }
    });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE && resultCode== RESULT_OK&& null!= data){if (data.getClipData()!=null) {
            int countOfImages = data.getClipData().getItemCount();
            for (int i = 0; i < countOfImages; i++) {
                Uri imageuri = data.getClipData().getItemAt(i).getUri();
                uri.add(imageuri);

            }
            adapter.notifyDataSetChanged();
            textView.setText("Photos(" + uri.size() + ")");
        }else{
            Uri imageuri =data.getData();
            uri.add(imageuri);
        }
            adapter.notifyDataSetChanged();
            textView.setText("Photos(" + uri.size() + ")");
        }
        else{
            Toast.makeText(this,"you havent picked any image",Toast.LENGTH_LONG).show();

        }
        }

    }
