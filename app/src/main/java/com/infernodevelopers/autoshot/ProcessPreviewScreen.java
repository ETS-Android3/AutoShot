package com.infernodevelopers.autoshot;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVData;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVItemSpacing;
import com.infernodevelopers.autoshot.Utils.ProcessUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessPreviewScreen extends AppCompatActivity implements OnUserEarnedRewardListener {
  private static final String TAG = "ProcessPreviewScreen";
  
  List<ssfoldScreenRVData> delete_list = new ArrayList<ssfoldScreenRVData>();
  
  RecyclerView delete_list_rv;
  
  int deleted_files = 0;
  
  String foldscreen_path;
  SharedPreferences homescreen_rv_pref;
  
  Handler preview_handle;
  
  ConstraintLayout preview_loading_ui;
  
  boolean process_done = false;
  
  List<ssfoldScreenRVData> sort_list = new ArrayList<ssfoldScreenRVData>();
  
  RecyclerView sort_list_rv;
  
  SharedPreferences ssfoldscreen_RV_pref;
  
  public void closeButton(View paramView) {
    if (this.process_done)
      closeDialog(); 
  }
  
  public void closeDialog() {
    (new AlertDialog.Builder((Context)this)).setTitle("Are you sure you want to cancel processing?").setPositiveButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
          }
        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            ProcessPreviewScreen.this.ssfoldscreen_RV_pref.edit().putBoolean("PROCESS_ACTIVE", false).apply();
            param1DialogInterface.dismiss();
            ProcessPreviewScreen.this.finish();
          }
        }).create().show();
  }
  
  public void doneProcess(View paramView) {
    if (this.process_done)
      (new AlertDialog.Builder((Context)this)).setTitle("Are you sure you want to delete selected images?").setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
              param1DialogInterface.dismiss();
            }
          }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
              for (param1Int = 0; param1Int < ProcessPreviewScreen.this.delete_list.size(); param1Int++) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("doneProcess: ");
                stringBuilder1.append(((ssfoldScreenRVData)ProcessPreviewScreen.this.delete_list.get(param1Int)).image_path);
                Log.d("ProcessPreviewScreen", stringBuilder1.toString());
                if (((ssfoldScreenRVData)ProcessPreviewScreen.this.delete_list.get(param1Int)).delete && (new File(((ssfoldScreenRVData)ProcessPreviewScreen.this.delete_list.get(param1Int)).image_path)).delete()) {
                  ProcessPreviewScreen processPreviewScreen1 = ProcessPreviewScreen.this;
                  processPreviewScreen1.deleted_files++;
                } 
              } 
              ProcessPreviewScreen processPreviewScreen = ProcessPreviewScreen.this;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Images Deleted: ");
              stringBuilder.append(ProcessPreviewScreen.this.deleted_files);
              Toast.makeText((Context)processPreviewScreen, stringBuilder.toString(), 0).show();
              param1DialogInterface.dismiss();
              ProcessPreviewScreen.this.finish();
            }
          }).create().show(); 
  }
  
  public void findViewByIds() {
    this.preview_loading_ui = (ConstraintLayout)findViewById(2131296771);
    this.delete_list_rv = (RecyclerView)findViewById(2131296683);
    this.sort_list_rv = (RecyclerView)findViewById(2131296687);
  }
  
  public void onBackPressed() {
    closeDialog();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492981);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.hide(); 
    this.preview_handle = new Handler();
    findViewByIds();
    this.ssfoldscreen_RV_pref = getApplicationContext().getSharedPreferences("SSFOLDSCREEN_RV", 0);
    this.homescreen_rv_pref = getSharedPreferences("HOMESCREEN_RV", 0);
    this.foldscreen_path = getIntent().getExtras().getString("DIR_PATH");
    this.sort_list = ssfoldScreenRVDataList();
    if (this.homescreen_rv_pref.getBoolean("processImagesRewardedAd", false))
      MobileAds.initialize((Context)this, new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus param1InitializationStatus) {
              ProcessPreviewScreen processPreviewScreen = ProcessPreviewScreen.this;
              RewardedInterstitialAd.load((Context)processPreviewScreen, processPreviewScreen.getResources().getString(2131820723), (new AdRequest.Builder()).build(), new RewardedInterstitialAdLoadCallback() {
                    public void onAdFailedToLoad(LoadAdError param2LoadAdError) {
                      Log.e("ProcessPreviewScreen", "onAdFailedToLoad");
                    }
                    
                    public void onAdLoaded(RewardedInterstitialAd param2RewardedInterstitialAd) {
                      param2RewardedInterstitialAd.show((Activity)ProcessPreviewScreen.this, ProcessPreviewScreen.this);
                      Log.e("ProcessPreviewScreen", "onAdLoaded");
                    }
                  });
            }
          }); 
    if (this.homescreen_rv_pref.getBoolean("processImagesAd", false)) {
      AdRequest adRequest = (new AdRequest.Builder()).build();
      InterstitialAd.load((Context)this, getResources().getString(2131820640), adRequest, new InterstitialAdLoadCallback() {
            public void onAdFailedToLoad(LoadAdError param1LoadAdError) {
              Log.i("ProcessPreviewScreen", param1LoadAdError.getMessage());
            }
            
            public void onAdLoaded(InterstitialAd param1InterstitialAd) {
              Log.i("ProcessPreviewScreen", "onAdLoaded");
              param1InterstitialAd.show((Activity)ProcessPreviewScreen.this);
            }
          });
    } 
    this.delete_list_rv.addItemDecoration((RecyclerView.ItemDecoration)new ssfoldScreenRVItemSpacing(2, 50, true));
    this.sort_list_rv.addItemDecoration((RecyclerView.ItemDecoration)new ssfoldScreenRVItemSpacing(this.sort_list.size(), 50, true));
    (new Process()).execute((Object[])new String[] { this.foldscreen_path });
  }
  
  public void onUserEarnedReward(RewardItem paramRewardItem) {}
  
  public List<ssfoldScreenRVData> ssfoldScreenRVDataList() {
    ArrayList<ssfoldScreenRVData> arrayList = new ArrayList();
    File[] arrayOfFile = (new File(this.foldscreen_path)).listFiles();
    if (arrayOfFile != null) {
      int j = arrayOfFile.length;
      int i;
      for (i = 0; i < j; i++) {
        File file = arrayOfFile[i];
        if (file.getName().contains(".jpg") || file.getName().contains(".jpeg") || file.getName().contains(".png"))
          arrayList.add(new ssfoldScreenRVData(file.getAbsolutePath())); 
      } 
      for (i = 0; i < arrayList.size(); i++) {
        for (j = 0; j < arrayList.size() - 1; j++) {
          String[] arrayOfString = ((ssfoldScreenRVData)arrayList.get(j)).image_path.split("/")[(((ssfoldScreenRVData)arrayList.get(j)).image_path.split("/")).length - 1].split("_");
          int k = Integer.parseInt(arrayOfString[arrayOfString.length - 1].split("\\.")[0]);
          arrayOfString = ((ssfoldScreenRVData)arrayList.get(j + 1)).image_path.split("/")[(((ssfoldScreenRVData)arrayList.get(j + 1)).image_path.split("/")).length - 1].split("_");
          if (Integer.parseInt(arrayOfString[arrayOfString.length - 1].split("\\.")[0]) < k)
            Collections.swap(arrayList, j, j + 1); 
        } 
      } 
    } 
    return arrayList;
  }
  
  class Process extends AsyncTask<String, String, String> {
    protected String doInBackground(String... param1VarArgs) {
      List<ssfoldScreenRVData> list = ProcessPreviewScreen.this.sort_list;
      ProcessPreviewScreen processPreviewScreen = ProcessPreviewScreen.this;
      ProcessUtils processUtils = new ProcessUtils(list, (Context)processPreviewScreen, processPreviewScreen.delete_list_rv, ProcessPreviewScreen.this.sort_list_rv, ProcessPreviewScreen.this.preview_loading_ui, ProcessPreviewScreen.this.preview_handle, (TextView)ProcessPreviewScreen.this.findViewById(2131296678), (TextView)ProcessPreviewScreen.this.findViewById(2131296676), (ImageView)ProcessPreviewScreen.this.findViewById(2131296679), (ImageView)ProcessPreviewScreen.this.findViewById(2131296677));
      ProcessPreviewScreen.this.delete_list = processUtils.Process();
      ProcessPreviewScreen.this.process_done = true;
      Log.d("ProcessPreviewScreen", "doInBackground: ");
      return null;
    }
    
    protected void onPostExecute(String param1String) {
      super.onPostExecute(param1String);
    }
    
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
}
