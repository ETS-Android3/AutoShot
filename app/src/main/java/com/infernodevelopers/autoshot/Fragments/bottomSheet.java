package com.infernodevelopers.autoshot.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.esafirm.imagepicker.features.ImagePickerActivity;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;

public class bottomSheet extends BottomSheetDialogFragment {
  public static final int IMAGE_CHOOSER_REQUEST = 120;
  
  private static final String TAG = "bottomSheet";
  
  Context context;
  
  String fold_screen_path = "";
  
  ArrayList<Image> images = new ArrayList<Image>();
  
  SharedPreferences overlayscreen_pref;
  
  SharedPreferences ssfoldscreen_RV_pref;
  
  public bottomSheet(Activity paramActivity) {
    this.context = (Context)paramActivity;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    this.fold_screen_path = getArguments().getString("DIR_PATH");
    return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }
  
  public void setupDialog(final Dialog dialog, int paramInt) {
    super.setupDialog(dialog, paramInt);
    View view = View.inflate(getContext(), 2131492987, null);
    dialog.setContentView(view);
    this.ssfoldscreen_RV_pref = this.context.getSharedPreferences("SSFOLDSCREEN_RV", 0);
    this.overlayscreen_pref = this.context.getSharedPreferences("OVERLAY_SCREEN", 0);
    ImageButton imageButton1 = (ImageButton)view.findViewById(2131296753);
    ImageButton imageButton2 = (ImageButton)view.findViewById(2131296756);
    imageButton1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent(bottomSheet.this.context, ImagePickerActivity.class);
            intent.putExtra("folderMode", true);
            intent.putExtra("mode", 2);
            intent.putExtra("limit", 50);
            intent.putExtra("showCamera", true);
            intent.putExtra("selectedImages", bottomSheet.this.images);
            intent.putExtra("folderTitle", "Album");
            intent.putExtra("imageTitle", "Tap to select images");
            intent.putExtra("imageDirectory", "Camera");
            intent.putExtra("returnAfterFirst", true);
            bottomSheet.this.startActivityForResult(intent, 120);
            dialog.dismiss();
          }
        });
    imageButton2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            bottomSheet.this.ssfoldscreen_RV_pref.edit().putString("SSFOLDSCREEN_BOTTOM_SHEET", "AUTOCAPTURE_FOLD").apply();
            bottomSheet.this.overlayscreen_pref.edit().putString("DIR_PATH", bottomSheet.this.fold_screen_path).apply();
            bottomSheet.this.overlayscreen_pref.edit().putBoolean("AS_ACTIVE", true).apply();
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)bottomSheet.this.context.getSystemService("media_projection");
            bottomSheet.this.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 200);
            dialog.dismiss();
          }
        });
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Fragments\bottomSheet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */