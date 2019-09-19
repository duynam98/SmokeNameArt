package com.example.smokenameart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.smokenameart.adapter.EditRecentAdapter;
import com.example.smokenameart.interfacee.GetPositionInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class EditRecentActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rclShowImage;
    private ArrayList<String> pathList;
    private EditRecentAdapter editRecentAdapter;

    private Dialog dialog;
    private ImageView imgBackShow, imgShare, imgShowImageDL;

    private ImageView imgBackEdit;
    private int pos = -1;
    private String bitmapPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_recent);

        init();
        setDialogShowImage();
        imgBackEdit.setOnClickListener(this);
    }

    private void setDialogShowImage() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_show_image);
        int w = getResources().getDisplayMetrics().widthPixels;
        Window window = dialog.getWindow();
        window.setLayout(w, w+200);
        imgBackShow = dialog.findViewById(R.id.imgBackShow);
        imgShare = dialog.findViewById(R.id.imgShare);
        imgShowImageDL = dialog.findViewById(R.id.imgShowImageDL);
        imgBackShow.setOnClickListener(this);
        imgShare.setOnClickListener(this);
    }

    private void init() {
        imgBackEdit = findViewById(R.id.imgBackEdit);
        rclShowImage = findViewById(R.id.rclEdtRecent);
        pathList = new ArrayList<>();
        pathList = getAllShownImagesPath();
        if (pathList.size() == 0) {
            rclShowImage.setVisibility(View.GONE);
        } else {
            rclShowImage.setVisibility(View.VISIBLE);
        }
        editRecentAdapter = new EditRecentAdapter(pathList, this, new GetPositionInterface() {
            @Override
            public void getPosition(int position) {
                pos = position;
                dialog.show();
                Glide.with(EditRecentActivity.this).load(pathList.get(position)).placeholder(R.drawable.load).into(imgShowImageDL);
            }
        });
        rclShowImage.setLayoutManager(new GridLayoutManager(this, 3));
        rclShowImage.setAdapter(editRecentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBackEdit:
                onBackPressed();
                break;
            case R.id.imgBackShow:
                dialog.dismiss();
                break;
            case R.id.imgShare:
                shareImage(pathList.get(pos));
                break;
        }
    }

    private ArrayList<String> getAllShownImagesPath() {
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        File file = new File(android.os.Environment.getExternalStorageDirectory() + "/smoke_name/");
        if (file.exists()) {
            String[] projection = file.list();
            for (int i = 0; i < projection.length; i++) {
                listOfAllImages.add("/storage/emulated/0/smoke_name/" + projection[i]);
            }
        }
        return listOfAllImages;
    }
    private void shareImage(String path){
        File requestFile = new File(path);
        Uri fileUri = FileProvider.getUriForFile(
                this,getPackageName() + ".provider",
                requestFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        startActivity(Intent.createChooser(intent, "Share"));
    }

}
