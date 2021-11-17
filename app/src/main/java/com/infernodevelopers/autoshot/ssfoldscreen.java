package com.infernodevelopers.autoshot;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVAdapter;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVData;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVDragnDrop;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVItemSpacing;
import com.infernodevelopers.autoshot.Fragments.bottomSheet;
import com.infernodevelopers.autoshot.Services.AutoCaptureService;
import com.infernodevelopers.autoshot.Utils.PdfUtils;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class ssfoldscreen extends AppCompatActivity {
  public static final String AUTOCAPTURE_FOLD = "AUTOCAPTURE_FOLD";
  
  public static final String BACK_IV = "back_icon";
  
  public static final String BOTTOM_BLUR_IV = "bottom_blur_bg";
  
  public static final String CHECKBOX_SELECTED_RV = "CHECKBOX_SELECTED_RV";
  
  public static final String DELETE_IV = "delete_icon";
  
  public static final String DELETE_SELECTED_IV = "delete_selected_icon";
  
  public static final Double DESIGN_DEVICE_DENSITY = Double.valueOf(2.625D);
  
  public static final int DESIGN_DEVICE_HEIGHT = 1920;
  
  public static final int DESIGN_DEVICE_WIDTH = 1080;
  
  public static final String DIR_PATH = "DIR_PATH";
  
  public static final String EDIT_IV = "edit_icon";
  
  public static final String EXPORT_IMGS_IV = "export_images_icon";
  
  public static final String FILE_NAME = "FILE_NAME";
  
  public static final String IMAGES_VIEW_RV = "images_view_rv";
  
  public static final String PDF_CREATED = "PDF_CREATED";
  
  public static final String PROCESS_ACTIVE = "PROCESS_ACTIVE";
  
  public static final String SELECT_ALL_IV = "select_all_icon";
  
  public static final String SELECT_ALL_TV = "select_all_tv";
  
  public static final String SSFOLDSCREEN_BOTTOM_SHEET = "SSFOLDSCREEN_BOTTOM_SHEET";
  
  public static final String SSFOLDSCREEN_RV = "SSFOLDSCREEN_RV";
  
  private static final String TAG = "ssfoldscreen";
  
  public static final String TITLE_TV = "title_tv";
  
  int[] ImageViewIds;
  
  String[] ImageViewNames = new String[] { "back_icon", "edit_icon", "delete_selected_icon", "select_all_icon", "export_images_icon", "bottom_blur_bg" };
  
  float[][] ImageViewScaling;
  
  ImageView[] ImageViews;
  
  int[] RecyclerViewIds;
  
  String[] RecyclerViewNames;
  
  float[][] RecyclerViewScaling;
  
  RecyclerView[] RecyclerViews;
  
  int[] TextViewIds = new int[] { 2131296776, 2131296775 };
  
  String[] TextViewNames = new String[] { "title_tv", "select_all_tv" };
  
  TextView[] TextViews = new TextView[] { this.title_tv, this.select_all_tv };
  
  ImageView back_icon;
  
  ImageView bottom_blur_bg;
  
  String current_fold_name;
  
  int current_screen_height;
  
  int current_screen_width;
  
  List<ssfoldScreenRVData> data_for_pdf;
  
  ImageView delete_icon;
  
  ImageView delete_selected_icon;
  
  ImageView edit_icon;
  
  ImageView export_images_icon;
  
  ssfoldScreenRVAdapter fold_screen_adapter;
  
  int fold_screen_no_of_items;
  
  String foldscreen_path;
  
  Handler handler;
  
  SharedPreferences homescreen_rv_pref;
  
  RecyclerView images_view_rv;
  
  int last_num_with_convention_followed;
  
  private InterstitialAd mInterstitialAd;
  
  int number_of_files_deleted;
  
  int number_of_files_to_share;
  
  SharedPreferences overlayscreen_pref;
  
  ImageView select_all_icon;
  
  TextView select_all_tv;
  
  SharedPreferences settings;
  
  SharedPreferences ssfoldscreen_RV_pref;
  
  TextView title_tv;
  
  public ssfoldscreen() {
    float[] arrayOfFloat1 = { 0.0074074073F, 0.0F, 0.0F, 0.0F, 0.022222223F, 1.0F };
    float[] arrayOfFloat2 = { 0.0F, 0.016666668F, 0.0F, 0.0F, 0.025925925F, 1.0F };
    this.ImageViewScaling = new float[][] { { 0.014814815F, 0.008333334F, 0.0F, 0.0F, 0.022222223F, 1.0F }, arrayOfFloat1, { 0.0F, 0.0F, 0.0F, 0.0F, 0.025925925F, 1.0F }, arrayOfFloat2, { 0.0F, 0.0F, 0.0F, 0.0F, 0.025925925F, 1.0F }, { 0.0F, 0.0F, 0.0F, 0.0F, 0.38095185F, 0.165278F } };
    this.ImageViews = new ImageView[] { this.back_icon, this.edit_icon, this.delete_selected_icon, this.select_all_icon, this.export_images_icon, this.bottom_blur_bg };
    this.ImageViewIds = new int[] { 2131296750, 2131296762, 2131296760, 2131296774, 2131296763, 2131296752 };
    this.RecyclerViewNames = new String[] { "images_view_rv" };
    this.RecyclerViewScaling = new float[][] { { 0.014814815F, 0.008333334F, 0.014814815F, 0.0F, 0.38095185F, 1.2638906F } };
    this.RecyclerViews = new RecyclerView[] { this.images_view_rv };
    this.RecyclerViewIds = new int[] { 2131296773 };
    this.fold_screen_no_of_items = 0;
    this.last_num_with_convention_followed = 0;
    this.current_fold_name = "";
    this.data_for_pdf = Collections.emptyList();
    this.number_of_files_deleted = 0;
    this.number_of_files_to_share = 0;
  }
  
  protected void DeviceScaling(View[] paramArrayOfView, float[][] paramArrayOffloat) {
    int i;
    for (i = 0; i < paramArrayOffloat.length; i++) {
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramArrayOfView[i].getLayoutParams();
      double d = (this.current_screen_width * paramArrayOffloat[i][0]);
      Double double_ = DESIGN_DEVICE_DENSITY;
      marginLayoutParams.setMargins((int)(d * double_.doubleValue()), (int)((this.current_screen_height * paramArrayOffloat[i][1]) * double_.doubleValue()), (int)((this.current_screen_width * paramArrayOffloat[i][2]) * double_.doubleValue()), (int)((this.current_screen_height * paramArrayOffloat[i][3]) * double_.doubleValue()));
      (paramArrayOfView[i].getLayoutParams()).width = (int)((this.current_screen_width * paramArrayOffloat[i][4]) * double_.doubleValue());
      float f = paramArrayOffloat[i][5];
      (paramArrayOfView[i].getLayoutParams()).height = (int)((this.current_screen_width * paramArrayOffloat[i][4]) * double_.doubleValue() * f);
      paramArrayOfView[i].requestLayout();
    } 
  }
  
  public void createPDFDialog(View paramView) {
    if (this.data_for_pdf.size() != 0) {
      ChooserDialog chooserDialog = (new ChooserDialog((Activity)this)).withFilter(true, false, new String[0]);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Environment.getExternalStorageDirectory());
      stringBuilder.append("/");
      stringBuilder.append(getResources().getString(2131820571));
      stringBuilder.append("/");
      stringBuilder.append("Files");
      chooserDialog.withStartFile(stringBuilder.toString()).titleFollowsDir(true).withChosenListener(new ChooserDialog.Result() {
            public void onChoosePath(final String path, File param1File) {
              ssfoldscreen ssfoldscreen1 = ssfoldscreen.this;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Creating ");
              stringBuilder.append(ssfoldscreen.this.current_fold_name);
              stringBuilder.append(".pdf please wait....");
              Toast.makeText((Context)ssfoldscreen1, stringBuilder.toString(), 0).show();
              if (ssfoldscreen.this.homescreen_rv_pref.getBoolean("createPdfAd", false))
                if (ssfoldscreen.this.mInterstitialAd != null) {
                  ssfoldscreen.this.mInterstitialAd.show((Activity)ssfoldscreen.this);
                } else {
                  Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }  
              (new Thread(new Runnable() {
                    public void run() {
                      Looper.prepare();
                      ssfoldscreen ssfoldscreen = ssfoldscreen.this;
                      List<ssfoldScreenRVData> list = ssfoldscreen.this.data_for_pdf;
                      StringBuilder stringBuilder = new StringBuilder();
                      stringBuilder.append(path);
                      stringBuilder.append("/");
                      stringBuilder.append(ssfoldscreen.this.current_fold_name);
                      stringBuilder.append(".pdf");
                      PdfUtils pdfUtils = new PdfUtils((Context)ssfoldscreen, list, stringBuilder.toString());
                      pdfUtils.createPdf();
                      ssfoldscreen.this.handler.post(new Runnable() {
                            public void run() {
                              ssfoldscreen ssfoldscreen = ssfoldscreen.this;
                              StringBuilder stringBuilder = new StringBuilder();
                              stringBuilder.append(ssfoldscreen.this.current_fold_name);
                              stringBuilder.append(".pdf created & stored at ");
                              stringBuilder.append(path);
                              Toast.makeText((Context)ssfoldscreen, stringBuilder.toString(), 0).show();
                            }
                          });
                      if (ssfoldscreen.this.settings.getBoolean("AUTOSSDELPDF", false)) {
                        pdfUtils.deleteCreatePdfImages();
                        ssfoldscreen.this.handler.post(new Runnable() {
                              public void run() {
                                ssfoldscreen.this.refreshTriggerRV();
                                ssfoldscreen ssfoldscreen = ssfoldscreen.this;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("All images from ");
                                stringBuilder.append(ssfoldscreen.this.current_fold_name);
                                stringBuilder.append(" deleted!");
                                Toast.makeText((Context)ssfoldscreen, stringBuilder.toString(), 0).show();
                              }
                            });
                      } 
                    }
                  })).start();
            }
          }).build().show();
      return;
    } 
    Toast.makeText(getApplicationContext(), "Add at least 1 image to create pdf", 0).show();
  }
  
  public void deleteSelected(View paramView) {
    (new AlertDialog.Builder((Context)this)).setTitle("Delete Selected Files ?").setCancelable(true).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            for (ssfoldScreenRVData ssfoldScreenRVData : ssfoldscreen.this.data_for_pdf) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("onClick: ");
              stringBuilder.append(ssfoldScreenRVData.image_path);
              Log.d("ssfoldscreen", stringBuilder.toString());
              if (ssfoldScreenRVData.checked && (new File(ssfoldScreenRVData.image_path)).delete()) {
                ssfoldscreen ssfoldscreen1 = ssfoldscreen.this;
                ssfoldscreen1.number_of_files_deleted++;
              } 
            } 
            if (ssfoldscreen.this.number_of_files_deleted != 0) {
              ssfoldscreen.this.refreshTriggerRV();
              ssfoldscreen ssfoldscreen1 = ssfoldscreen.this;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(ssfoldscreen.this.number_of_files_deleted);
              stringBuilder.append(" files deleted!");
              Toast.makeText((Context)ssfoldscreen1, stringBuilder.toString(), 0).show();
              return;
            } 
            Toast.makeText((Context)ssfoldscreen.this, "No files selected!", 0).show();
          }
        }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
          }
        }).create().show();
  }
  
  public void edit_name_alert_dialog(View paramView) {
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    final EditText fileNameEditText = new EditText((Context)this);
    editText.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -1));
    editText.setEms(10);
    editText.setMaxLines(1);
    editText.setText(this.current_fold_name);
    if (!this.overlayscreen_pref.getBoolean("AS_ACTIVE", false)) {
      builder.setView((View)editText).setTitle("Type In New Name!").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
              ssfoldscreen ssfoldscreen1 = ssfoldscreen.this;
              String str = ssfoldscreen1.foldscreen_path;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(Environment.getExternalStorageDirectory());
              stringBuilder.append("/");
              stringBuilder.append(ssfoldscreen.this.getResources().getString(2131820571));
              stringBuilder.append("/");
              stringBuilder.append("Captures");
              stringBuilder.append("/");
              stringBuilder.append(fileNameEditText.getText().toString());
              ssfoldscreen1.rename_image(str, stringBuilder.toString());
              TextView[] arrayOfTextView = ssfoldscreen.this.TextViews;
              ssfoldscreen ssfoldscreen2 = ssfoldscreen.this;
              arrayOfTextView[ssfoldscreen2.find_element_num(ssfoldscreen2.TextViewNames, "title_tv")].setText(fileNameEditText.getText().toString());
            }
          }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
              param1DialogInterface.cancel();
            }
          }).setCancelable(true).create().show();
      return;
    } 
    Toast.makeText(getApplicationContext(), "Cannot Edit Folder Name When AutoCapture is Active!", 0).show();
  }
  
  public void export_selected(View paramView) {
    ArrayList<String> arrayList = new ArrayList();
    for (ssfoldScreenRVData ssfoldScreenRVData : this.data_for_pdf) {
      if (ssfoldScreenRVData.checked) {
        this.number_of_files_to_share++;
        arrayList.add(ssfoldScreenRVData.image_path);
      } 
    } 
    if (this.number_of_files_to_share != 0) {
      Intent intent = new Intent();
      intent.setAction("android.intent.action.SEND_MULTIPLE");
      intent.setType("image/*");
      ArrayList<Uri> arrayList1 = new ArrayList();
      Iterator<String> iterator = arrayList.iterator();
      while (iterator.hasNext())
        arrayList1.add(Uri.fromFile(new File(iterator.next()))); 
      intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList1);
      intent.setFlags(268435456);
      startActivity(Intent.createChooser(intent, "Share images to..."));
    } 
  }
  
  protected void findViewByIds() {
    int j = this.ImageViews.length;
    for (int i = 0; i < j; i++) {
      ImageView[] arrayOfImageView = this.ImageViews;
      if (i < arrayOfImageView.length)
        arrayOfImageView[i] = (ImageView)findViewById(this.ImageViewIds[i]); 
      TextView[] arrayOfTextView = this.TextViews;
      if (i < arrayOfTextView.length)
        arrayOfTextView[i] = (TextView)findViewById(this.TextViewIds[i]); 
      RecyclerView[] arrayOfRecyclerView = this.RecyclerViews;
      if (i < arrayOfRecyclerView.length)
        arrayOfRecyclerView[i] = (RecyclerView)findViewById(this.RecyclerViewIds[i]); 
    } 
  }
  
  protected int find_element_num(String[] paramArrayOfString, String paramString) {
    int j = 0;
    int k = paramArrayOfString.length;
    for (int i = 0; i < k; i++) {
      if (paramArrayOfString[i].equals(paramString))
        return j; 
      j++;
    } 
    return paramArrayOfString.length;
  }
  
  public void finishActivity(View paramView) {
    this.ssfoldscreen_RV_pref.edit().clear().apply();
    finish();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ssfoldscreen: onActivityResult: ");
    stringBuilder.append(paramInt1);
    Log.d("ssfoldscreen", stringBuilder.toString());
    if (this.ssfoldscreen_RV_pref.getString("SSFOLDSCREEN_BOTTOM_SHEET", "").equals("AUTOCAPTURE_FOLD")) {
      if (paramInt2 == -1) {
        this.homescreen_rv_pref.edit().putInt("LAST_NUM_WITH_CONV", this.last_num_with_convention_followed).apply();
        startService(AutoCaptureService.getStartIntent((Context)this, paramInt2, paramIntent));
      } else {
        Log.d("ssfoldscreen", "onActivityResult: failed to start autocapture");
        Toast.makeText(getApplicationContext(), "Failed to start AutoCapture", 0).show();
      } 
      this.ssfoldscreen_RV_pref.edit().putString("SSFOLDSCREEN_BOTTOM_SHEET", "").apply();
      return;
    } 
    if (paramIntent != null)
      for (Image image : paramIntent.getParcelableArrayListExtra("selectedImages")) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("ssfoldscreen: onActivityResult:image chooser ");
        stringBuilder1.append(image.getPath());
        Log.d("ssfoldscreen", stringBuilder1.toString());
        try {
          Log.d("ssfoldscreen", "ssfoldscreen: onActivityResult: copied");
          this.last_num_with_convention_followed++;
          FileUtils.copyFileToDirectory(new File(image.getPath()), new File(this.foldscreen_path));
        } catch (IOException iOException) {
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("ssfoldscreen: onActivityResult:error  ");
          stringBuilder1.append(iOException);
          Log.d("ssfoldscreen", stringBuilder1.toString());
        } 
      }  
  }
  
  public void onBackPressed() {
    super.onBackPressed();
    this.ssfoldscreen_RV_pref.edit().clear().apply();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    this.current_fold_name = getIntent().getStringExtra("FILE_NAME");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Environment.getExternalStorageDirectory());
    stringBuilder.append("/");
    stringBuilder.append(getResources().getString(2131820571));
    stringBuilder.append("/");
    stringBuilder.append("Captures");
    stringBuilder.append("/");
    stringBuilder.append(getIntent().getStringExtra("FILE_NAME"));
    this.foldscreen_path = stringBuilder.toString();
    setContentView(2131492986);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
    this.current_screen_height = displayMetrics.heightPixels;
    this.current_screen_width = displayMetrics.widthPixels;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.hide(); 
    findViewByIds();
    DeviceScaling((View[])this.ImageViews, this.ImageViewScaling);
    DeviceScaling((View[])this.RecyclerViews, this.RecyclerViewScaling);
    this.handler = new Handler();
    this.ssfoldscreen_RV_pref = getApplicationContext().getSharedPreferences("SSFOLDSCREEN_RV", 0);
    this.settings = getApplicationContext().getSharedPreferences("SETTINGS_FILE", 0);
    this.overlayscreen_pref = getApplicationContext().getSharedPreferences("OVERLAY_SCREEN", 0);
    this.ssfoldscreen_RV_pref.edit().putString("DIR_PATH", this.foldscreen_path).apply();
    SharedPreferences sharedPreferences = getSharedPreferences("HOMESCREEN_RV", 0);
    this.homescreen_rv_pref = sharedPreferences;
    if (sharedPreferences.getBoolean("createPdfAd", false)) {
      AdRequest adRequest = (new AdRequest.Builder()).build();
      InterstitialAd.load((Context)this, getResources().getString(2131820640), adRequest, new InterstitialAdLoadCallback() {
            public void onAdFailedToLoad(LoadAdError param1LoadAdError) {
              Log.i("ssfoldscreen", param1LoadAdError.getMessage());
              ssfoldscreen.access$002(ssfoldscreen.this, (InterstitialAd)null);
            }
            
            public void onAdLoaded(InterstitialAd param1InterstitialAd) {
              ssfoldscreen.access$002(ssfoldscreen.this, param1InterstitialAd);
              Log.i("ssfoldscreen", "onAdLoaded");
            }
          });
    } 
    List<ssfoldScreenRVData> list = ssfoldScreenRVDataList();
    this.data_for_pdf = list;
    Log.d("ssfoldscreen", "run: create");
    if (list != null) {
      this.fold_screen_adapter = new ssfoldScreenRVAdapter(list, (Context)this);
      this.RecyclerViews[find_element_num(this.RecyclerViewNames, "images_view_rv")].setAdapter((RecyclerView.Adapter)this.fold_screen_adapter);
      this.RecyclerViews[find_element_num(this.RecyclerViewNames, "images_view_rv")].setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager((Context)this, 2));
      this.RecyclerViews[find_element_num(this.RecyclerViewNames, "images_view_rv")].addItemDecoration((RecyclerView.ItemDecoration)new ssfoldScreenRVItemSpacing(2, 50, true));
      (new ItemTouchHelper((ItemTouchHelper.Callback)new ssfoldScreenRVDragnDrop((ssfoldScreenRVDragnDrop.ItemTouchHelperAdapter)this.fold_screen_adapter))).attachToRecyclerView(this.RecyclerViews[find_element_num(this.RecyclerViewNames, "images_view_rv")]);
    } 
    this.TextViews[find_element_num(this.TextViewNames, "title_tv")].setText(getIntent().getStringExtra("FILE_NAME"));
    ((FloatingActionButton)findViewById(2131296772)).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          public void onGlobalLayout() {
            ((FloatingActionButton)ssfoldscreen.this.findViewById(2131296772)).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            if (ssfoldscreen.this.homescreen_rv_pref.getBoolean("FOLD_LAUNCH_FIRST_TIME", true))
              ssfoldscreen.this.spotlight(); 
          }
        });
  }
  
  protected void onResume() {
    super.onResume();
    Log.d("ssfoldscreen", "onResume: ");
    refreshTriggerRV();
    ((ImageButton)findViewById(2131296774)).setImageResource(2131165363);
    this.TextViews[find_element_num(this.TextViewNames, "select_all_tv")].setText("select all");
    this.ssfoldscreen_RV_pref.edit().clear().apply();
  }
  
  public void openBottomSheet(View paramView) {
    Bundle bundle = new Bundle();
    bundle.putString("DIR_PATH", this.foldscreen_path);
    bottomSheet bottomSheet = new bottomSheet((Activity)this);
    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    bottomSheet.setArguments(bundle);
  }
  
  public void processImagesDialog(View paramView) {
    if (this.data_for_pdf.size() > 1) {
      this.ssfoldscreen_RV_pref.edit().putBoolean("PROCESS_ACTIVE", true).apply();
      Intent intent = new Intent((Context)this, ProcessPreviewScreen.class);
      intent.putExtra("DIR_PATH", this.foldscreen_path);
      startActivity(intent);
      Toast.makeText(getApplicationContext(), "Processing...", 0).show();
    } else {
      Toast.makeText(getApplicationContext(), "Need more than 1 image for processing!", 0).show();
    } 
    if (this.homescreen_rv_pref.getBoolean("processImagesAd", false)) {
      InterstitialAd interstitialAd = this.mInterstitialAd;
      if (interstitialAd != null) {
        interstitialAd.show((Activity)this);
        return;
      } 
      Log.d("TAG", "The interstitial ad wasn't ready yet.");
    } 
  }
  
  public void refreshTriggerRV() {
    (new Thread(new Runnable() {
          public void run() {
            ssfoldscreen.this.last_num_with_convention_followed = 0;
            final List<ssfoldScreenRVData> data = ssfoldscreen.this.ssfoldScreenRVDataList();
            ssfoldscreen.this.data_for_pdf = list;
            ssfoldscreen.this.handler.post(new Runnable() {
                  public void run() {
                    ssfoldscreen.this.fold_screen_adapter.refreshFoldDataSet(data);
                    ssfoldscreen.this.fold_screen_adapter.setCheckboxModeDeselectAll();
                  }
                });
          }
        })).start();
  }
  
  public void rename_image(String paramString1, String paramString2) {
    if ((new File(paramString1)).renameTo(new File(paramString2)))
      Log.d("ssfoldscreen", "onClick: rename done"); 
  }
  
  public void selectAll(View paramView) {
    ImageButton imageButton = (ImageButton)paramView;
    if ((this.ssfoldscreen_RV_pref.getString("CHECKBOX_SELECTED_RV", "").split("\\.")).length == this.fold_screen_no_of_items) {
      this.fold_screen_adapter.setCheckboxModeDeselectAll();
      imageButton.setImageResource(2131165363);
      this.ssfoldscreen_RV_pref.edit().clear().apply();
      this.TextViews[find_element_num(this.TextViewNames, "select_all_tv")].setText("select all");
      return;
    } 
    this.fold_screen_adapter.setCheckboxModeSelectAll();
    imageButton.setImageResource(2131165359);
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < this.fold_screen_no_of_items; i++) {
      stringBuilder.append(i);
      if (i != this.fold_screen_no_of_items - 1)
        stringBuilder.append("."); 
    } 
    this.ssfoldscreen_RV_pref.edit().putString("CHECKBOX_SELECTED_RV", stringBuilder.toString()).apply();
    this.TextViews[find_element_num(this.TextViewNames, "select_all_tv")].setText("un-select all");
  }
  
  public void spotlight() {
    SimpleTarget simpleTarget1 = ((SimpleTarget.Builder)((SimpleTarget.Builder)((SimpleTarget.Builder)(new SimpleTarget.Builder((Activity)this)).setPoint(findViewById(2131296772))).setRadius(150.0F)).setTitle("Process Images").setDescription("Delete Similar Images").setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
          public void onEnded(SimpleTarget param1SimpleTarget) {}
          
          public void onStarted(SimpleTarget param1SimpleTarget) {}
        })).build();
    SimpleTarget simpleTarget2 = ((SimpleTarget.Builder)((SimpleTarget.Builder)((SimpleTarget.Builder)(new SimpleTarget.Builder((Activity)this)).setPoint(findViewById(2131296749))).setRadius(150.0F)).setTitle("Add Images").setDescription("").setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
          public void onEnded(SimpleTarget param1SimpleTarget) {}
          
          public void onStarted(SimpleTarget param1SimpleTarget) {}
        })).build();
    SimpleTarget simpleTarget3 = ((SimpleTarget.Builder)((SimpleTarget.Builder)((SimpleTarget.Builder)(new SimpleTarget.Builder((Activity)this)).setPoint(findViewById(2131296759))).setRadius(150.0F)).setTitle("Create Pdf").setDescription("").setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
          public void onEnded(SimpleTarget param1SimpleTarget) {}
          
          public void onStarted(SimpleTarget param1SimpleTarget) {}
        })).build();
    SimpleTarget simpleTarget4 = ((SimpleTarget.Builder)((SimpleTarget.Builder)((SimpleTarget.Builder)(new SimpleTarget.Builder((Activity)this)).setPoint(findViewById(2131296763))).setRadius(150.0F)).setTitle("Export Selected").setDescription("Share Images to Other apps").setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
          public void onEnded(SimpleTarget param1SimpleTarget) {}
          
          public void onStarted(SimpleTarget param1SimpleTarget) {}
        })).build();
    Spotlight.with((Activity)this).setOverlayColor(ContextCompat.getColor((Context)this, 2131034142)).setDuration(100L).setAnimation((TimeInterpolator)new DecelerateInterpolator(2.0F)).setTargets((Target[])new SimpleTarget[] { simpleTarget2, simpleTarget1, simpleTarget3, simpleTarget4 }).setClosedOnTouchedOutside(true).setOnSpotlightStartedListener(new OnSpotlightStartedListener() {
          public void onStarted() {}
        }).setOnSpotlightEndedListener(new OnSpotlightEndedListener() {
          public void onEnded() {
            ssfoldscreen.this.homescreen_rv_pref.edit().putBoolean("FOLD_LAUNCH_FIRST_TIME", false).apply();
          }
        }).start();
  }
  
  public List<ssfoldScreenRVData> ssfoldScreenRVDataList() {
    ArrayList<ssfoldScreenRVData> arrayList = new ArrayList();
    File[] arrayOfFile = (new File(this.foldscreen_path)).listFiles();
    if (arrayOfFile != null) {
      this.fold_screen_no_of_items = 0;
      int j = arrayOfFile.length;
      int i;
      for (i = 0; i < j; i++) {
        File file = arrayOfFile[i];
        if (file.getName().contains(".jpg") || file.getName().contains(".jpeg") || file.getName().contains(".png")) {
          this.fold_screen_no_of_items++;
          arrayList.add(new ssfoldScreenRVData(file.getAbsolutePath()));
          if (file.getName().contains(this.current_fold_name))
            try {
              int k = Integer.parseInt(file.getName().split("_")[(file.getName().split("_")).length - 1].split("\\.")[0]);
              if (k > this.last_num_with_convention_followed)
                this.last_num_with_convention_followed = k; 
            } catch (Exception exception) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("ssfoldScreenRVDataList: ");
              stringBuilder.append(exception.getMessage());
              Log.d("ssfoldscreen", stringBuilder.toString());
            }  
        } 
      } 
      for (i = 0; i < arrayList.size(); i++) {
        String str = ((ssfoldScreenRVData)arrayList.get(i)).image_path.split("/")[(((ssfoldScreenRVData)arrayList.get(i)).image_path.split("/")).length - 1];
        if (!str.contains(this.current_fold_name)) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("ssfoldScreenRVDataList: c ");
          stringBuilder2.append(str);
          Log.d("ssfoldscreen", stringBuilder2.toString());
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append(this.current_fold_name);
          stringBuilder2.append("_");
          stringBuilder2.append(this.last_num_with_convention_followed + 1);
          stringBuilder2.append(".jpg");
          String str1 = stringBuilder2.toString();
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append(this.foldscreen_path);
          stringBuilder3.append("/");
          stringBuilder3.append(str);
          str = stringBuilder3.toString();
          stringBuilder3 = new StringBuilder();
          stringBuilder3.append(this.foldscreen_path);
          stringBuilder3.append("/");
          stringBuilder3.append(str1);
          rename_image(str, stringBuilder3.toString());
          ssfoldScreenRVData ssfoldScreenRVData = arrayList.get(i);
          stringBuilder3 = new StringBuilder();
          stringBuilder3.append(this.foldscreen_path);
          stringBuilder3.append("/");
          stringBuilder3.append(str1);
          ssfoldScreenRVData.image_path = stringBuilder3.toString();
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("ssfoldScreenRVDataList: c ");
          stringBuilder1.append(((ssfoldScreenRVData)arrayList.get(i)).image_path);
          Log.d("ssfoldscreen", stringBuilder1.toString());
          this.last_num_with_convention_followed++;
        } 
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
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\ssfoldscreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */