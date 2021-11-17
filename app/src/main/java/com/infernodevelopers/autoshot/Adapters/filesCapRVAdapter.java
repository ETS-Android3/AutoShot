package com.infernodevelopers.autoshot.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.infernodevelopers.autoshot.ssfoldscreen;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;

public class filesCapRVAdapter extends RecyclerView.Adapter<filesCapRVHolder> {
  public static final String CHECKBOX_CAP_RV = "CHECKBOX_CAP_RV";
  
  public static final String CHECKBOX_FILES_RV = "CHECKBOX_FILES_RV";
  
  public static final int CHECKBOX_MODE_DEFAULT = 20;
  
  public static final int CHECKBOX_MODE_DESELECT_ALL = 22;
  
  public static final int CHECKBOX_MODE_SELECT_ALL = 21;
  
  public static final String FILE_NAME = "FILE_NAME";
  
  public static final String HOMESCREEN_RV = "HOMESCREEN_RV";
  
  public static final int MODE_CAP = 101;
  
  public static final int MODE_FILES = 100;
  
  public static final String PDF_CREATED = "PDF_CREATED";
  
  private static final String TAG = "filesCapRVAdapter";
  
  public static final String TOP_PANEL_FILES_CURRENT_DIR = "TOP_PANEL_FILES_CURRENT_DIR";
  
  AdRequest adRequest;
  
  boolean adloaded = false;
  
  ImageButton cap_ripple_button;
  
  int checkBoxMode = 20;
  
  ConstraintLayout checkBoxPanel;
  
  Context context;
  
  String current_tab;
  
  ImageButton files_ripple_button;
  
  SharedPreferences homescreen_rv_pref;
  
  List<filesCapRVData> list = Collections.emptyList();
  
  int mode;
  
  ImageView play_ripple_button;
  
  public filesCapRVAdapter(List<filesCapRVData> paramList, final Context context, int paramInt, ConstraintLayout paramConstraintLayout, ImageButton paramImageButton1, ImageButton paramImageButton2, ImageView paramImageView) {
    this.list = paramList;
    this.context = context;
    this.mode = paramInt;
    this.checkBoxPanel = paramConstraintLayout;
    this.files_ripple_button = paramImageButton2;
    this.cap_ripple_button = paramImageButton1;
    this.play_ripple_button = paramImageView;
    this.homescreen_rv_pref = context.getSharedPreferences("HOMESCREEN_RV", 0);
    (new Thread(new Runnable() {
          public void run() {
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                  public void onInitializationComplete(InitializationStatus param2InitializationStatus) {}
                });
            filesCapRVAdapter.this.adRequest = (new AdRequest.Builder()).build();
          }
        })).start();
  }
  
  private void edit_name_alert_dialog(final String root_path_cap, final String root_path_files, final String fileName, boolean paramBoolean) {
    String str;
    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
    final EditText fileNameEditText = new EditText(this.context);
    editText.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -1));
    editText.setEms(10);
    editText.setMaxLines(1);
    if (!paramBoolean) {
      str = fileName.split("\\.")[0];
    } else {
      str = fileName;
    } 
    editText.setText(str);
    builder.setView((View)editText).setTitle("Type In New Name!").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            File file1;
            File file2;
            filesCapRVAdapter filesCapRVAdapter1 = filesCapRVAdapter.this;
            filesCapRVAdapter1.current_tab = filesCapRVAdapter1.homescreen_rv_pref.getString("CURRENT_TAB", "Captures");
            if (filesCapRVAdapter.this.current_tab.equals("Files")) {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append(filesCapRVAdapter.this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", root_path_files));
              stringBuilder1.append("/");
              stringBuilder1.append(fileName);
              file1 = new File(stringBuilder1.toString());
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append(filesCapRVAdapter.this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", root_path_files));
              stringBuilder2.append("/");
              stringBuilder2.append(fileNameEditText.getText().toString());
              stringBuilder2.append(".pdf");
              file2 = new File(stringBuilder2.toString());
            } else {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append(root_path_cap);
              stringBuilder1.append("/");
              stringBuilder1.append(fileName);
              file1 = new File(stringBuilder1.toString());
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append(root_path_cap);
              stringBuilder2.append("/");
              stringBuilder2.append(fileNameEditText.getText().toString());
              file2 = new File(stringBuilder2.toString());
            } 
            if (file1.renameTo(file2))
              Log.d("filesCapRVAdapter", "onClick: done"); 
            String str = filesCapRVAdapter.this.current_tab;
            param1Int = -1;
            int i = str.hashCode();
            if (i != 14910989) {
              if (i == 67881559 && str.equals("Files"))
                param1Int = 0; 
            } else if (str.equals("Captures")) {
              param1Int = 1;
            } 
            if (param1Int != 0) {
              filesCapRVAdapter.this.refreshCapDataSet();
              return;
            } 
            filesCapRVAdapter.this.refreshFilesDataSet();
          }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.cancel();
          }
        }).setCancelable(true).create().show();
  }
  
  public void changeDataSetForFoldIn(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", ""));
    stringBuilder.append("/");
    stringBuilder.append(paramString);
    paramString = stringBuilder.toString();
    this.homescreen_rv_pref.edit().putString("TOP_PANEL_FILES_CURRENT_DIR", paramString).apply();
    refreshFilesDataSet();
  }
  
  public void changeDataSetForFoldUp() {
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(Environment.getExternalStorageDirectory());
    stringBuilder1.append("/");
    stringBuilder1.append(this.context.getResources().getString(2131820571));
    stringBuilder1.append("/");
    stringBuilder1.append("Files");
    String str1 = stringBuilder1.toString();
    String str2 = this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", "");
    String str3 = str2.split("/")[(str2.split("/")).length - 1];
    if (!str1.equals(str2))
      str1 = str2.substring(0, str2.length() - str3.length() - 1); 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("changeDataSetForFoldUp: ");
    stringBuilder2.append(str1);
    Log.d("filesCapRVAdapter", stringBuilder2.toString());
    this.homescreen_rv_pref.edit().putString("TOP_PANEL_FILES_CURRENT_DIR", str1).apply();
    refreshFilesDataSet();
  }
  
  public int getItemCount() {
    return this.list.size();
  }
  
  public void onAttachedToRecyclerView(RecyclerView paramRecyclerView) {
    super.onAttachedToRecyclerView(paramRecyclerView);
  }
  
  public void onBindViewHolder(final filesCapRVHolder viewHolder, final int position) {
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(Environment.getExternalStorageDirectory());
    stringBuilder1.append("/");
    stringBuilder1.append(this.context.getResources().getString(2131820571));
    stringBuilder1.append("/");
    stringBuilder1.append("Files");
    final String root_path_files = stringBuilder1.toString();
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append(Environment.getExternalStorageDirectory());
    stringBuilder1.append("/");
    stringBuilder1.append(this.context.getResources().getString(2131820571));
    stringBuilder1.append("/");
    stringBuilder1.append("Captures");
    final String root_path_cap = stringBuilder1.toString();
    viewHolder.adView.setAdListener(new AdListener() {
          public void onAdLoaded() {
            super.onAdLoaded();
            filesCapRVAdapter.this.adloaded = true;
          }
        });
    viewHolder.adView.loadAd(this.adRequest);
    final String fileName = ((filesCapRVData)this.list.get(position)).filename;
    String str1 = str2;
    StringBuilder stringBuilder2 = new StringBuilder();
    if (!((filesCapRVData)this.list.get(position)).showAds) {
      if (!((filesCapRVData)this.list.get(position)).isDirectory)
        str1 = str2.substring(0, str2.length() - 4); 
      if (str1.length() > 20) {
        for (int i = 0; i < 20; i++)
          stringBuilder2.append(str1.substring(i, i + 1)); 
        if (((filesCapRVData)this.list.get(position)).isDirectory) {
          stringBuilder2.append("---");
        } else {
          stringBuilder2.append("---.pdf");
        } 
      } else {
        stringBuilder2.append(str2);
      } 
    } 
    viewHolder.filenameTV.setText(stringBuilder2.toString());
    viewHolder.fileSizeTV.setText(((filesCapRVData)this.list.get(position)).fileSize);
    viewHolder.editFileNameIB.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            filesCapRVAdapter filesCapRVAdapter1 = filesCapRVAdapter.this;
            filesCapRVAdapter1.edit_name_alert_dialog(root_path_cap, root_path_files, fileName, ((filesCapRVData)filesCapRVAdapter1.list.get(position)).isDirectory);
          }
        });
    if (this.mode == 100) {
      viewHolder.shareIB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              if (!((filesCapRVData)filesCapRVAdapter.this.list.get(position)).showAds) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("application/pdf");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("file://");
                stringBuilder.append(filesCapRVAdapter.this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", root_path_files));
                stringBuilder.append("/");
                stringBuilder.append(((filesCapRVData)filesCapRVAdapter.this.list.get(position)).filename);
                intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.parse(stringBuilder.toString()));
                intent.putExtra("android.intent.extra.SUBJECT", "Sharing File...");
                intent.putExtra("android.intent.extra.TEXT", "Sharing File...");
                filesCapRVAdapter.this.context.startActivity(Intent.createChooser(intent, "Share File"));
              } 
            }
          });
      if (((filesCapRVData)this.list.get(position)).filename.equals("ad")) {
        if (this.homescreen_rv_pref.getBoolean("filesRVAds", false)) {
          viewHolder.adViewBGIV.setVisibility(0);
          viewHolder.adView.setVisibility(0);
          viewHolder.adView.loadAd(this.adRequest);
        } else {
          viewHolder.adViewBGIV.setVisibility(4);
          viewHolder.adView.setVisibility(4);
        } 
      } else {
        viewHolder.adViewBGIV.setVisibility(4);
        viewHolder.adView.setVisibility(4);
      } 
      if (!this.homescreen_rv_pref.getBoolean("filesRVAds", false) || position != 0) {
        if (((filesCapRVData)this.list.get(position)).filename.contains(".pdf")) {
          viewHolder.fileTypeIV.setImageDrawable(this.context.getResources().getDrawable(2131165346));
          viewHolder.shareIB.setVisibility(0);
        } 
        if (((filesCapRVData)this.list.get(position)).isDirectory) {
          viewHolder.fileTypeIV.setImageDrawable(this.context.getResources().getDrawable(2131165345));
          viewHolder.shareIB.setVisibility(4);
        } 
        viewHolder.filesItemBG.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                if (((filesCapRVData)filesCapRVAdapter.this.list.get(position)).isDirectory) {
                  filesCapRVAdapter.this.changeDataSetForFoldIn(fileName);
                  filesCapRVAdapter.this.homescreen_rv_pref.edit().putString("CHECKBOX_FILES_RV", "").apply();
                  return;
                } 
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(filesCapRVAdapter.this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", root_path_files));
                stringBuilder.append("/");
                stringBuilder.append(((filesCapRVData)filesCapRVAdapter.this.list.get(position)).filename);
                Uri uri = Uri.fromFile(new File(stringBuilder.toString()));
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(268435457);
                if (intent.resolveActivity(filesCapRVAdapter.this.context.getPackageManager()) != null)
                  filesCapRVAdapter.this.context.startActivity(intent); 
              }
            });
      } 
    } 
    if (this.mode == 101) {
      if (((filesCapRVData)this.list.get(position)).filename.equals("ad")) {
        if (this.homescreen_rv_pref.getBoolean("capRVAds", false)) {
          viewHolder.adViewBGIV.setVisibility(0);
          viewHolder.adView.setVisibility(0);
          viewHolder.adView.loadAd(this.adRequest);
        } else {
          viewHolder.adViewBGIV.setVisibility(4);
          viewHolder.adView.setVisibility(4);
        } 
      } else {
        viewHolder.adViewBGIV.setVisibility(4);
        viewHolder.adView.setVisibility(4);
      } 
      if (!this.homescreen_rv_pref.getBoolean("capRVAds", false) || position != 0) {
        viewHolder.fileTypeIV.setImageDrawable(this.context.getResources().getDrawable(2131165318));
        viewHolder.no_of_ssTV.setText(((filesCapRVData)this.list.get(position)).no_of_ss);
        viewHolder.timeIntervalTV.setText(((filesCapRVData)this.list.get(position)).time_interval);
        viewHolder.capItemBG.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onClick:");
                stringBuilder.append(filesCapRVAdapter.this.homescreen_rv_pref.getString("CHECKBOX_CAP_RV", "").equals(""));
                Log.d("filesCapRVAdapter", stringBuilder.toString());
                if (filesCapRVAdapter.this.homescreen_rv_pref.getString("CHECKBOX_CAP_RV", "").equals("")) {
                  Intent intent = new Intent(filesCapRVAdapter.this.context, ssfoldscreen.class);
                  intent.putExtra("FILE_NAME", fileName);
                  intent.putExtra("PDF_CREATED", filesCapRVAdapter.this.homescreen_rv_pref.getBoolean(fileName, false));
                  filesCapRVAdapter.this.context.startActivity(intent);
                } 
              }
            });
      } 
    } 
    viewHolder.selectFileCB.setChecked(((filesCapRVData)this.list.get(position)).checked);
    viewHolder.selectFileCB.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ((filesCapRVData)filesCapRVAdapter.this.list.get(position)).checked ^= 0x1;
            filesCapRVAdapter filesCapRVAdapter1 = filesCapRVAdapter.this;
            filesCapRVAdapter1.current_tab = filesCapRVAdapter1.homescreen_rv_pref.getString("CURRENT_TAB", "Captures");
            if (viewHolder.selectFileCB.isChecked()) {
              filesCapRVAdapter.this.checkBoxPanel.setVisibility(0);
              if (filesCapRVAdapter.this.current_tab.equals("Files"))
                filesCapRVAdapter.this.cap_ripple_button.setVisibility(4); 
              if (filesCapRVAdapter.this.current_tab.equals("Captures"))
                filesCapRVAdapter.this.files_ripple_button.setVisibility(4); 
              filesCapRVAdapter.this.play_ripple_button.setVisibility(4);
              return;
            } 
            if (filesCapRVAdapter.this.homescreen_rv_pref.getString("CHECKBOX_FILES_RV", "").equals("") && filesCapRVAdapter.this.homescreen_rv_pref.getString("CHECKBOX_CAP_RV", "").equals(""))
              filesCapRVAdapter.this.checkBoxPanel.setVisibility(8); 
            if (filesCapRVAdapter.this.current_tab.equals("Files"))
              filesCapRVAdapter.this.cap_ripple_button.setVisibility(0); 
            if (filesCapRVAdapter.this.current_tab.equals("Captures"))
              filesCapRVAdapter.this.files_ripple_button.setVisibility(0); 
            filesCapRVAdapter.this.play_ripple_button.setVisibility(0);
          }
        });
  }
  
  public filesCapRVHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    LayoutInflater layoutInflater = LayoutInflater.from(paramViewGroup.getContext());
    return (this.mode != 101) ? new filesCapRVHolder(layoutInflater.inflate(2131492917, paramViewGroup, false)) : new filesCapRVHolder(layoutInflater.inflate(2131492915, paramViewGroup, false), 101);
  }
  
  public void refreshCapDataSet() {
    this.list.clear();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Environment.getExternalStorageDirectory());
    stringBuilder.append("/");
    stringBuilder.append(this.context.getResources().getString(2131820571));
    stringBuilder.append("/");
    stringBuilder.append("Captures");
    String str = stringBuilder.toString();
    File[] arrayOfFile = (new File(str)).listFiles();
    if (arrayOfFile != null) {
      File[] arrayOfFile1;
      String str1;
      DecimalFormat decimalFormat = new DecimalFormat("#.##");
      if (this.homescreen_rv_pref.getBoolean("capRVAds", false)) {
        filesCapRVData filesCapRVData = new filesCapRVData("ad", "1MB", "22", "1-2pm", false);
        filesCapRVData.showAds = true;
        this.list.add(filesCapRVData);
      } 
      int j = 0;
      int k = arrayOfFile.length;
      int i = 0;
      while (true) {
        int m = 0;
        if (i < k) {
          String str2;
          File[] arrayOfFile3;
          String str5 = str1[i];
          String str6 = str5.getName();
          if (str5.isDirectory()) {
            Object object;
            float f = (float)(str5.length() / 1024.0D);
            File[] arrayOfFile4 = str5.listFiles();
            Objects.requireNonNull(arrayOfFile4);
            arrayOfFile4 = arrayOfFile4;
            int n = arrayOfFile4.length;
            boolean bool = false;
            while (true) {
              f += (float)(SYNTHETIC_LOCAL_VARIABLE_15.length() / 1024.0D);
              m++;
              object = SYNTHETIC_LOCAL_VARIABLE_6;
            } 
            String str7 = str1;
            if (f > 1000.0D) {
              f = (float)(f / 1024.0D);
              str1 = " MB";
            } else {
              str1 = " KB";
            } 
            List<filesCapRVData> list = this.list;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(decimalFormat.format(f));
            stringBuilder1.append(str1);
            str1 = stringBuilder1.toString();
            stringBuilder1 = new StringBuilder();
            stringBuilder1.append(object);
            stringBuilder1.append(" ss");
            list.add(new filesCapRVData(str6, str1, stringBuilder1.toString(), "1-3pm", str5.isDirectory()));
            if (this.homescreen_rv_pref.getBoolean("capRVAds", false) && j == 3) {
              filesCapRVData filesCapRVData = new filesCapRVData("ad", "1MB", "22", "1-2pm", false);
              filesCapRVData.showAds = true;
              this.list.add(filesCapRVData);
            } 
            j++;
            arrayOfFile3 = arrayOfFile1;
            str2 = str7;
            continue;
          } 
          String str4 = str2;
          File[] arrayOfFile2 = arrayOfFile3;
          String str3 = str4;
          continue;
        } 
        break;
        i++;
        str2 = str;
        arrayOfFile1 = arrayOfFile;
        str1 = str2;
      } 
    } 
    notifyDataSetChanged();
  }
  
  public void refreshFilesDataSet() {
    String str = this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", "");
    this.list.clear();
    File[] arrayOfFile = (new File(str)).listFiles();
    if (arrayOfFile != null) {
      DecimalFormat decimalFormat = new DecimalFormat("#.##");
      boolean bool = this.homescreen_rv_pref.getBoolean("filesRVAds", false);
      String str1 = "1MB";
      if (bool) {
        filesCapRVData filesCapRVData = new filesCapRVData("ad", "1MB", false);
        filesCapRVData.showAds = true;
        this.list.add(filesCapRVData);
      } 
      int k = 0;
      int i = arrayOfFile.length;
      int j;
      for (j = 0; j < i; j++) {
        File file = arrayOfFile[j];
        String str2 = file.getName();
        if (str2.contains(".pdf") || file.isDirectory() || str2.contains(".PDF")) {
          float f1;
          String str3 = " KB";
          if (file.isDirectory()) {
            f1 = (float)(FileUtils.sizeOfDirectory(file) / 1024.0D);
          } else {
            f1 = (float)(file.length() / 1024.0D);
          } 
          float f2 = f1;
          if (f1 > 1000.0D) {
            f2 = (float)(f1 / 1024.0D);
            str3 = " MB";
          } 
          List<filesCapRVData> list = this.list;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(decimalFormat.format(f2));
          stringBuilder.append(str3);
          list.add(new filesCapRVData(str2, stringBuilder.toString(), file.isDirectory()));
          if (this.homescreen_rv_pref.getBoolean("filesRVAds", false) && k == 3) {
            filesCapRVData filesCapRVData = new filesCapRVData("ad", str1, false);
            filesCapRVData.showAds = true;
            this.list.add(filesCapRVData);
          } 
          k++;
        } 
      } 
    } 
    notifyDataSetChanged();
  }
  
  public void setCheckboxModeDeselectAll() {
    Iterator<filesCapRVData> iterator = this.list.iterator();
    while (iterator.hasNext())
      ((filesCapRVData)iterator.next()).checked = false; 
    notifyDataSetChanged();
  }
  
  public void setCheckboxModeSelectAll() {
    Iterator<filesCapRVData> iterator = this.list.iterator();
    while (iterator.hasNext())
      ((filesCapRVData)iterator.next()).checked = true; 
    notifyDataSetChanged();
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\filesCapRVAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */