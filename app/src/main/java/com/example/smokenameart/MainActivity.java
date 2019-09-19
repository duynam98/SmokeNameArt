package com.example.smokenameart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION = 1;
    private TextView txtTitlte, txtPolicy;
    private ArrayList<Typeface> typefaceArrayList;
    private Button btnNew, btnRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        checkPermission();
        btnNew = findViewById(R.id.btnNew);
        btnRecent =findViewById(R.id.btnRecent);
        txtPolicy =findViewById(R.id.txtPolicy);
        txtTitlte = findViewById(R.id.txtTitlte);
        typefaceArrayList = new ArrayList<>();
        typefaceArrayList = readAllAssetFont(this,"fonts");
        txtTitlte.setTypeface(typefaceArrayList.get(42));

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNameSmokeActivity.class));
            }
        });
        
        txtPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Copyright belongs to Tapbi company", Toast.LENGTH_SHORT).show();
            }
        });

        btnRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditRecentActivity.class));
            }
        });
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER},
                    STORAGE_PERMISSION);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                }else{
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public ArrayList<Typeface> readAllAssetFont(Context mContext, String folderPath) {
        ArrayList<Typeface> pathList = new ArrayList<>();
        try {
            String[] files = mContext.getAssets().list(folderPath);
            for (String name : files) {
                pathList.add(Typeface.createFromAsset(getAssets(), folderPath + File.separator + name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }
}
