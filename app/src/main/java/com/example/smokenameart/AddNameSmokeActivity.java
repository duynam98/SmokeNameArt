package com.example.smokenameart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.smokenameart.adapter.AddBackgroundColorAdapter;
import com.example.smokenameart.adapter.AddFrameAdapter;
import com.example.smokenameart.adapter.AddSmokeAdapter;
import com.example.smokenameart.adapter.AddSymbolAdapter;
import com.example.smokenameart.adapter.PickColorAdapter;
import com.example.smokenameart.adapter.PickFontAdapter;
import com.example.smokenameart.interfacee.GetPositionInterface;
import com.example.smokenameart.views.BubbleTextView;
import com.example.smokenameart.views.StickerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddNameSmokeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack, imgSave, imgBackground, imgFrame, imgSmokeEffect, imgImageBG, imgFramBG, imgText, imgTextStyle, imgSymbol, imgOverBackground;
    private RelativeLayout rllSave, rllAddName;
    private RecyclerView rclAddName;
    private AddBackgroundColorAdapter addBackgroundColorAdapter;
    private AddSmokeAdapter addSmokeAdapter;
    private AddFrameAdapter addFrameAdapter;
    private ArrayList<String> pathList;
    private ArrayList<String> pathListThumb;
    private ArrayList<String> pathListOver;
    private int border = R.drawable.bord_radius_10;
    private int notBorder = R.color.colorBlack;
    private LinearLayout lnNone;
    private AddSymbolAdapter addSymbolAdapter;
    private String checkAdd;
    // sticker view
    private StickerView mCurrentView;
    private BubbleTextView mCurrentEditTextView;
    private ArrayList<View> mViews;
    // dialog
    private Dialog dialog;
    private EditText edtInformation;
    private Button btnCancel, btnAccept;
    private ImageView imgPickColor;
    // pick color
    private ColorPickerDialog.Builder builder;
    private int color;
    //bottom sheet pick color
    private BottomSheetDialog bottomSheetDialog;
    private ArrayList<Integer> integerArrayList;
    private PickColorAdapter pickColorAdapter;
    private ImageView imgCloseBSheet, imgPickColorBS;
    private RecyclerView rclPickColorBS, rclPickFont;
    // Dialog save
    private AlertDialog.Builder alertDialog;
    private String checkSave="back";

    private ProgressDialog progressDialog;
    private ArrayList<Typeface> listFont;
    private PickFontAdapter pickFontAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_name_smoke);
        init();
        progressDialog.show();
        eventClick();
        pathListThumb = readAllAssetImage(this, "smokes_thumb");
        pathListOver = readAllAssetImage(this, "smokes_over");
        pathList = readAllAssetImage(this, "smokes");
        addSmokeAdapter.changedList(pathListThumb);
        Glide.with(this).load(pathListOver.get(0)).into(imgOverBackground);
        Glide.with(this).asBitmap().load(pathList.get(0)).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgBackground.setImageBitmap(resource);
                setSizeRllSave(resource);
                addBubble();
                progressDialog.dismiss();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void init() {
        lnNone = findViewById(R.id.lnNone);
        imgBack = findViewById(R.id.imgBack);
        imgSave = findViewById(R.id.imgSave);
        imgBackground = findViewById(R.id.imgBackground);
        imgFrame = findViewById(R.id.imgFrame);
        imgSmokeEffect = findViewById(R.id.imgSmokeEffect);
        imgImageBG = findViewById(R.id.imgImageBG);
        imgFramBG = findViewById(R.id.imgFrameBG);
        imgText = findViewById(R.id.imgText);
        imgTextStyle = findViewById(R.id.imgTextStyle);
        imgSymbol = findViewById(R.id.imgSymbol);
        imgOverBackground = findViewById(R.id.imgOverBackground);
        rllAddName = findViewById(R.id.rllAddName);
        rllSave = findViewById(R.id.rllSave);
        rclAddName = findViewById(R.id.rclAddName);
        mViews = new ArrayList<>();
        pathList = new ArrayList<>();
        pathListThumb = new ArrayList<>();
        pathListOver = new ArrayList<>();
        eventClickPositionInAdapter();
        rclAddName.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclAddName.setAdapter(addSmokeAdapter);

        // setup dialog
        dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.dialog_edit_text);
        edtInformation = dialog.findViewById(R.id.edtInformation);
        imgPickColor = dialog.findViewById(R.id.imgPickColor);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnAccept = dialog.findViewById(R.id.btnAccept);
        eventDialog();
        // bottomsheet dialog
        setupBottomSheetPickColor();

        setupAlertDialogSave();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading .....");

        listFont = new ArrayList<>();
        listFont = readAllAssetFont(this,"fonts");
    }

    private void setupAlertDialogSave() {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.ic_help_outline_black_24dp);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(checkSave.equals("back")){
                    onBackPressed();
                }else{
                    checkSave="save";
                    if (mCurrentView != null) {
                        mCurrentView.setInEdit(false);
                    }
                    if (mCurrentEditTextView != null) {
                        mCurrentEditTextView.setInEdit(false);
                    }
                    saveImage();
                    Toast.makeText(AddNameSmokeActivity.this, "Save finish", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

    }

    private void setupBottomSheetPickColor() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_pick_color);
        imgCloseBSheet = bottomSheetDialog.findViewById(R.id.imgBackBottomSheet);
        imgPickColorBS =bottomSheetDialog.findViewById(R.id.imgPickColorBS);
        rclPickColorBS = bottomSheetDialog.findViewById(R.id.rclPickColor);
        rclPickFont = bottomSheetDialog.findViewById(R.id.rclFont);
        listFont = new ArrayList<>();
        listFont = readAllAssetFont(this,"fonts");
        pickFontAdapter = new PickFontAdapter(listFont, this, new GetPositionInterface() {
            @Override
            public void getPosition(int position) {
                mCurrentEditTextView.setFont(listFont.get(position));
            }
        });
        rclPickFont.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclPickFont.setAdapter(pickFontAdapter);
        integerArrayList = new ArrayList<>();
        integerArrayList.add(R.drawable.ic_lens_red_24dp);
        integerArrayList.add(R.drawable.ic_lens_orange_24dp);
        integerArrayList.add(R.drawable.ic_lens_yellow_24dp);
        integerArrayList.add(R.drawable.ic_lens_yx_24dp);
        integerArrayList.add(R.drawable.ic_lens_green_24dp);
        integerArrayList.add(R.drawable.ic_lens_xd_24dp);
        integerArrayList.add(R.drawable.ic_lens_blue_24dp);
        integerArrayList.add(R.drawable.ic_lens_purle_24dp);
        integerArrayList.add(R.drawable.ic_lens_prink_24dp);
        pickColorAdapter = new PickColorAdapter(integerArrayList, this, new GetPositionInterface() {
            @Override
            public void getPosition(int position) {
                if(position==0){
                    mCurrentEditTextView.setColor(Color.RED);
                }else if(position==1){
                    mCurrentEditTextView.setColor(Color.parseColor("#EE711C"));
                }else if(position==2){
                    mCurrentEditTextView.setColor(Color.YELLOW);
                }else if(position==3){
                    mCurrentEditTextView.setColor(Color.parseColor("#BEF006"));
                }else if(position==4){
                    mCurrentEditTextView.setColor(Color.GREEN);
                }else if(position==5){
                    mCurrentEditTextView.setColor(Color.parseColor("#0FF0BC"));
                }else if(position==6){
                    mCurrentEditTextView.setColor(Color.BLUE);
                }else if(position==7){
                    mCurrentEditTextView.setColor(Color.parseColor("#B70ADD"));
                }else if(position==8){
                    mCurrentEditTextView.setColor(Color.parseColor("#D914E7"));
                }
            }
        });
        rclPickColorBS.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclPickColorBS.setAdapter(pickColorAdapter);

        imgCloseBSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        imgPickColorBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickColorAdapter.changeId();
                pickColor();
                builder.show();
            }
        });

    }

    private void pickColor() {
        builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton("Choose", new ColorEnvelopeListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                        edtInformation.setTextColor(envelope.getColor());
                        mCurrentEditTextView.setColor(envelope.getColor());
                        color = envelope.getColor();
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true);
    }

    private void eventDialog() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentEditTextView.setText(edtInformation.getText().toString());
                mCurrentEditTextView.setColor(color);
                edtInformation.setText("");
                dialog.dismiss();
            }
        });
        imgPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickColor();
                builder.show();
            }
        });
    }

    private void eventClickPositionInAdapter() {
        addBackgroundColorAdapter = new AddBackgroundColorAdapter(pathListThumb, this, new GetPositionInterface() {
            @Override
            public void getPosition(int position) {
                imgOverBackground.setVisibility(View.GONE);
                Glide.with(AddNameSmokeActivity.this).load(pathList.get(position)).into(imgBackground);
            }
        });
        addFrameAdapter = new AddFrameAdapter(pathListThumb, this, new GetPositionInterface() {
            @Override
            public void getPosition(int position) {
                imgFrame.setVisibility(View.VISIBLE);
                lnNone.setBackgroundResource(R.color.colorBlack);
                Glide.with(AddNameSmokeActivity.this).load(pathList.get(position)).into(imgFrame);
            }
        });
        addSmokeAdapter = new AddSmokeAdapter(pathListThumb, this, new GetPositionInterface() {
            @Override
            public void getPosition(int position) {
                imgOverBackground.setVisibility(View.VISIBLE);
                Glide.with(AddNameSmokeActivity.this).load(pathListOver.get(position)).into(imgOverBackground);
                Glide.with(AddNameSmokeActivity.this).load(pathList.get(position)).into(imgBackground);
            }
        });

        addSymbolAdapter = new AddSymbolAdapter(pathListOver, this, new GetPositionInterface() {
            @Override
            public void getPosition(int position) {
                if(checkAdd.equals("symbol")) {
                    addStickerView(pathListOver.get(position).substring(22));
                }else if(checkAdd.equals("text")){
                    addStickerView(pathList.get(position).substring(22));

                }
            }
        });
    }

    private void eventClick() {
        imgBack.setOnClickListener(this);
        imgSave.setOnClickListener(this);
        imgSmokeEffect.setOnClickListener(this);
        imgImageBG.setOnClickListener(this);
        imgFramBG.setOnClickListener(this);
        imgText.setOnClickListener(this);
        imgTextStyle.setOnClickListener(this);
        imgSymbol.setOnClickListener(this);
        lnNone.setOnClickListener(this);
        imgBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                if(!checkSave.equals("save")){
                    checkSave = "back";
                    alertDialog.setMessage("Do you want to exit without saving the image?");
                    alertDialog.show();
                }else{
                    onBackPressed();
                }
                break;
            case R.id.imgSave:
                checkSave="nosave";
                alertDialog.setMessage("Do you want to save the image?");
                alertDialog.show();
                break;
            case R.id.imgSmokeEffect:
                setAddChoose(border, notBorder, notBorder, notBorder, notBorder, notBorder);
                lnNone.setVisibility(View.GONE);
                changedList("smokes", "smokes_thumb");
                addSmokeAdapter.changedList(pathListThumb);
                rclAddName.setAdapter(addSmokeAdapter);
                break;
            case R.id.imgImageBG:
                setAddChoose(notBorder, notBorder, border, notBorder, notBorder, notBorder);
                lnNone.setVisibility(View.GONE);
                changedList("pattern", "pattern_thumb");
                addBackgroundColorAdapter.changedList(pathListThumb);
                rclAddName.setAdapter(addBackgroundColorAdapter);
                break;
            case R.id.imgFrameBG:
                setAddChoose(notBorder, notBorder, notBorder, border, notBorder, notBorder);
                lnNone.setVisibility(View.VISIBLE);
                changedList("frames", "frames_thumb");
                Glide.with(this).load(pathList.get(0)).placeholder(R.drawable.load).into(imgFrame);
                addFrameAdapter.changedList(pathListThumb);
                rclAddName.setAdapter(addFrameAdapter);
                break;
            case R.id.imgText:
                setAddChoose(notBorder, notBorder, notBorder, notBorder, notBorder, border);
                addBubble();
                break;
            case R.id.imgTextStyle:
                checkAdd="text";
                setAddChoose(notBorder, notBorder, notBorder, notBorder, border, notBorder);
                lnNone.setVisibility(View.GONE);
                changedList("texts","texts_thumb");
                addSymbolAdapter.changedList(pathListThumb);
                rclAddName.setAdapter(addSymbolAdapter);
                break;
            case R.id.imgSymbol:
                checkAdd="symbol";
                setAddChoose(notBorder, border, notBorder, notBorder, notBorder, notBorder);
                lnNone.setVisibility(View.GONE);
                addSymbolAdapter.changedList(pathListOver);
                rclAddName.setAdapter(addSymbolAdapter);
                break;
            case R.id.lnNone:
                imgFrame.setVisibility(View.GONE);
                lnNone.setBackgroundResource(R.color.colorWhite);
                addFrameAdapter.setId(-1);
                break;
            case R.id.imgBackground:
                if (mCurrentView != null) {
                    mCurrentView.setInEdit(false);
                }
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                break;
        }
    }

    public ArrayList<String> readAllAssetImage(Context mContext, String folderPath) {
        ArrayList<String> pathList = new ArrayList<>();
        try {
            String[] files = mContext.getAssets().list(folderPath);
            for (String name : files) {
                pathList.add("file:///android_asset/" + folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }
    public ArrayList<Typeface> readAllAssetFont(Context mContext, String folderPath) {
        ArrayList<Typeface> pathList = new ArrayList<>();
        try {
            String[] files = mContext.getAssets().list(folderPath);
            for (String name : files) {
            pathList.add(Typeface.createFromAsset(getAssets(),folderPath + File.separator + name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    public void setAddChoose(int a1, int a2, int a3, int a4, int a5, int a6) {
        imgSmokeEffect.setBackgroundResource(a1);
        imgSymbol.setBackgroundResource(a2);
        imgImageBG.setBackgroundResource(a3);
        imgFramBG.setBackgroundResource(a4);
        imgTextStyle.setBackgroundResource(a5);
        imgText.setBackgroundResource(a6);
    }

    public void changedList(String name, String nameThumb) {
        pathList.clear();
        pathListThumb.clear();
        pathListThumb = readAllAssetImage(this, nameThumb);
        pathList = readAllAssetImage(this, name);
    }
    private void addStickerView(String filePath) {
        final StickerView stickerView = new StickerView(this);
        Bitmap bitmap = getBitmapFromAsset(AddNameSmokeActivity.this, filePath);
        stickerView.setBitmap(bitmap);
        stickerView.setColorFilter(ContextCompat.getColor(this, R.color.colorRed));
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                rllAddName.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rllAddName.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void addBubble() {
        final BubbleTextView bubbleTextView = new BubbleTextView(this,
                Color.WHITE, 0);
        bubbleTextView.setImageResource(R.mipmap.none_image);
        bubbleTextView.setOperationListener(new BubbleTextView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(bubbleTextView);
                rllAddName.removeView(bubbleTextView);
            }

            @Override
            public void onEdit(BubbleTextView bubbleTextView) {
                if (mCurrentView != null) {
                    mCurrentView.setInEdit(false);
                }
                mCurrentEditTextView.setInEdit(false);
                mCurrentEditTextView = bubbleTextView;
                mCurrentEditTextView.setInEdit(true);
                bottomSheetDialog.show();
            }

            @Override
            public void onClick(BubbleTextView bubbleTextView) {
                dialog.show();
                String text = bubbleTextView.getText();
                if(!text.equals("double_click")) {
                    edtInformation.setText(bubbleTextView.getText());
                }

            }

            @Override
            public void onTop(BubbleTextView bubbleTextView) {
                int position = mViews.indexOf(bubbleTextView);
                if (position == mViews.size() - 1) {
                    return;
                }
                BubbleTextView textView = (BubbleTextView) mViews.remove(position);
                mViews.add(mViews.size(), textView);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rllAddName.addView(bubbleTextView, lp);
        mViews.add(bubbleTextView);
        setCurrentEdit(bubbleTextView);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    private void setCurrentEdit(BubbleTextView bubbleTextView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentEditTextView = bubbleTextView;
        mCurrentEditTextView.setInEdit(true);

    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {

        }
        return bitmap;
    }

    public void setSizeRllSave(Bitmap bitmap) {
        int widthScreen = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthScreen, widthScreen);
        params.gravity = Gravity.CENTER;
        rllSave.setLayoutParams(params);
    }

    public String saveImage() {
        Bitmap bitmap = Bitmap.createBitmap(rllSave.getWidth(), rllSave.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        rllSave.draw(canvas);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "SmokeNameArt", null);
        return path;
    }

}
