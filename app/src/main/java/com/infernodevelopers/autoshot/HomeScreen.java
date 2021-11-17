package com.infernodevelopers.autoshot;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infernodevelopers.autoshot.Adapters.filesCapRVAdapter;
import com.infernodevelopers.autoshot.Adapters.filesCapRVData;
import com.infernodevelopers.autoshot.Licences.FreepikLicence;
import com.infernodevelopers.autoshot.Licences.GlideLicence;
import com.infernodevelopers.autoshot.Services.AutoCaptureService;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

public class HomeScreen extends AppCompatActivity {
    public static final String APP_LAUNCH_FOR_FIRST_TIME = "APP_LAUNCH_FOR_FIRST_TIME";

    public static final String AS_ICON_TITLE = "as_icon_title";

    public static final String BOTTOMNAV_BG = "bottomnav_bg";

    public static final String BOTTOMNAV_CAP = "bottomnav_cap";

    public static final String BOTTOMNAV_CAP_RV = "bottomnav_cap_rv";

    public static final String BOTTOMNAV_CAP_TV = "bottomnav_cap_text";

    public static final String BOTTOMNAV_FILES = "bottomnav_files";

    public static final String BOTTOMNAV_FILES_RV = "bottomnav_files_rv";

    public static final String BOTTOMNAV_FILES_TV = "bottomnav_files_text";

    public static final String CAP_RV_ADS = "capRVAds";

    public static final String CAP_TAB = "Captures";

    public static final String CHANGE_UI_ON_STOP_FROM_OVERLAY = "CHANGE_UI_ON_STOP_FROM_OVERLAY";

    public static final String CHECKBOX_CAP_RV = "CHECKBOX_CAP_RV";

    public static final String CHECKBOX_FILES_RV = "CHECKBOX_FILES_RV";

    public static final String CREATE_PDF_AD = "createPdfAd";

    public static final String CURRENT_TAB = "CURRENT_TAB";

    public static final Double DESIGN_DEVICE_DENSITY = Double.valueOf(2.625D);

    public static final int DESIGN_DEVICE_HEIGHT = 1920;

    public static final int DESIGN_DEVICE_WIDTH = 1080;

    public static final String EDITTEXT_DIALOG_MODE_ADD_FOLD = "ADD_FOLD";

    public static final String EDITTEXT_DIALOG_MODE_ENTER_FOLD_FOR_CAP = "FOLD_NAME_FOR_CAP";

    public static final String FILES_RV_ADS = "filesRVAds";

    public static final String FILES_TAB = "Files";

    public static final String FOLD_LAUNCH_FIRST_TIME = "FOLD_LAUNCH_FIRST_TIME";

    public static final String HOMESCREEN_RV = "HOMESCREEN_RV";

    public static final String LAST_NUM_WITH_CONV = "LAST_NUM_WITH_CONV";

    private static final int MEDIA_PROJECTION_REQUEST_CODE = 202;

    public static final int MODE_CAP = 101;

    public static final int MODE_FILES = 100;

    public static final String ONE_LINER_TV = "one_liner";

    private static final int OVERLAY_PERMISSION_CODE = 103;

    public static final String PLAY = "play";

    public static final String PLAY_BLUR_BG = "play_blur_bg";

    public static final String PLAY_ICON = "play_icon";

    public static final String PLAY_ICON_GUIDE = "play_icon_guide";

    public static final String PROCESS_IMAGES_AD = "processImagesAd";

    public static final String PROCESS_IMAGES_REWARDED_AD = "processImagesRewardedAd";

    public static final String SCREEN_MID_GUIDE = "screen_mid_guide";

    public static final String SETTINGS_ICON = "settings_icon";

    public static final String STOP = "stop";

    private static final int STORAGE_PERMISSION_CODE = 101;

    public static final String TAB_INFO_TV = "tab_info";

    private static final String TAG = "HomeScreen";

    public static final String TITLE_TV = "title";

    public static final String TOP_PANEL_FILES_CURRENT_DIR = "TOP_PANEL_FILES_CURRENT_DIR";

    int[] GuideLineIds;

    String[] GuidelineNames;

    float[][] GuidelineScaling;

    Guideline[] Guidelines;

    int[] ImageViewIds;

    String[] ImageViewNames = new String[]{"as_icon_title", "settings_icon", "play_icon", "play_blur_bg", "bottomnav_bg", "bottomnav_files", "bottomnav_cap"};

    float[][] ImageViewScaling;

    ImageView[] ImageViews;

    int[] RecyclerViewIds;

    String[] RecyclerViewNames;

    float[][] RecyclerViewScaling;

    RecyclerView[] RecyclerViews;

    int[] TextViewIds;

    String[] TextViewNames;

    TextView[] TextViews;

    DatabaseReference adRef;

    ImageView as_icon_title;

    int back_press_twice;

    ImageView bottomnav_bg;

    ImageView bottomnav_cap;

    RecyclerView bottomnav_cap_rv;

    TextView bottomnav_cap_text;

    ImageView bottomnav_files;

    RecyclerView bottomnav_files_rv;

    TextView bottomnav_files_text;

    List<filesCapRVData> cap_data;

    ImageButton cap_ripple_button;

    int cap_rv_no_of_items;

    filesCapRVAdapter cap_tab_adapter;

    ConstraintLayout checkbox_panel;

    ImageButton checkbox_panel_cancel;

    ImageButton checkbox_panel_delete;

    ImageButton checkbox_panel_more;

    ImageButton checkbox_panel_select;

    ImageButton checkbox_panel_share;

    int current_screen_height;

    int current_screen_width;

    String current_tab;

    FirebaseDatabase database;

    List<filesCapRVData> files_data;

    ImageButton files_ripple_button;

    int files_rv_no_of_items;

    filesCapRVAdapter files_tab_adapter;

    Handler handler;

    SharedPreferences homescreen_rv_pref;

    ImageView iv;

    TextView one_liner;

    SharedPreferences overlayscreen_pref;

    ImageView play_blur_bg;

    ImageView play_icon;

    Guideline play_icon_guide;

    TextView play_stop_ind;

    String root_path_cap;

    String root_path_files;

    Guideline screen_mid_guide;

    SharedPreferences settings;

    ImageView settings_icon;

    boolean show_cap_rv_ad;

    boolean show_create_pdf_ad;

    boolean show_files_rv_ad;

    boolean show_process_imgs_ad;

    boolean show_process_imgs_rewarded_ad;

    int storage_permission_denied_counter = 0;

    TextView tab_info;

    TextView title;

    ImageButton top_panel_add_fold;

    ImageButton top_panel_fold_up;

    ImageButton top_panel_refresh;

    public HomeScreen() {
        float[] arrayOfFloat1 = {0.0F, 0.0F, 0.0F, 0.0F, 0.06666667F, 1.0F};
        float[] arrayOfFloat2 = {0.0F, 0.0F, 0.0F, 0.0F, 0.09259259F, 1.0F};
        float[] arrayOfFloat3 = {0.0F, 0.0F, 0.0F, 0.0F, 0.4074074F, 0.2F};
        this.ImageViewScaling = new float[][]{{0.0F, 0.0F, 0.022222223F, 0.0F, 0.027777778F, 1.4666667F}, {0.0F, 0.0F, 0.0F, 0.0F, 0.02962963F, 1.0F}, arrayOfFloat1, arrayOfFloat2, arrayOfFloat3, {0.0F, 0.0F, 0.0F, 0.0F, 0.018518519F, 0.95F}, {0.0F, 0.0F, 0.0F, 0.0F, 0.022222223F, 1.0F}};
        this.ImageViews = new ImageView[]{this.as_icon_title, this.settings_icon, this.play_icon, this.play_blur_bg, this.bottomnav_bg, this.bottomnav_files, this.bottomnav_cap};
        this.ImageViewIds = new int[]{2131296471, 2131296496, 2131296480, 2131296481, 2131296472, 2131296478, 2131296475};
        this.GuidelineNames = new String[]{"screen_mid_guide", "play_icon_guide"};
        this.GuidelineScaling = new float[][]{{}, {}};
        this.Guidelines = new Guideline[]{this.screen_mid_guide, this.play_icon_guide};
        this.GuideLineIds = new int[]{2131296491, 2131296501};
        this.TextViewNames = new String[]{"title", "one_liner", "tab_info", "bottomnav_files_text", "bottomnav_cap_text"};
        this.TextViews = new TextView[]{this.title, this.one_liner, this.tab_info, this.bottomnav_files_text, this.bottomnav_cap_text};
        this.TextViewIds = new int[]{2131296497, 2131296492, 2131296490, 2131296479, 2131296476};
        this.RecyclerViewNames = new String[]{"bottomnav_files_rv", "bottomnav_cap_rv"};
        this.RecyclerViewScaling = new float[][]{{0.0F, 0.0F, 0.0F, 0.0F, 0.38095185F, 1.069446F}, {0.0F, 0.0F, 0.0F, 0.0F, 0.38095185F, 1.069446F}};
        this.RecyclerViews = new RecyclerView[]{this.bottomnav_files_rv, this.bottomnav_cap_rv};
        this.RecyclerViewIds = new int[]{2131296495, 2131296494};
        this.handler = new Handler();
        this.files_data = Collections.emptyList();
        this.cap_data = Collections.emptyList();
        this.files_rv_no_of_items = 0;
        this.cap_rv_no_of_items = 0;
        this.current_tab = "Captures";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        this.database = firebaseDatabase;
        this.adRef = firebaseDatabase.getReference();
        this.show_files_rv_ad = false;
        this.show_cap_rv_ad = false;
        this.show_create_pdf_ad = false;
        this.show_process_imgs_ad = false;
        this.show_process_imgs_rewarded_ad = false;
        this.back_press_twice = 0;
    }

    private List<filesCapRVData> capData() {
        this.cap_rv_no_of_items = 0;
        ArrayList<filesCapRVData> arrayList = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/");
        stringBuilder.append(getResources().getString(2131820571));
        stringBuilder.append("/");
        stringBuilder.append("Captures");
        String str = stringBuilder.toString();
        File[] arrayOfFile = (new File(str)).listFiles();
        if (this.show_cap_rv_ad)
            arrayList.add(new filesCapRVData("ad", "1MB", "22", "1-2pm", true));
        if (arrayOfFile != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            int j = arrayOfFile.length;
            for (int i = 0; i < j; i++) {
                File file = arrayOfFile[i];
                String str1 = file.getName();
                if (file.isDirectory()) {
                    Object object;
                    String str2;
                    float f = (float) (file.length() / 1024.0D);
                    File[] arrayOfFile1 = file.listFiles();
                    Objects.requireNonNull(arrayOfFile1);
                    arrayOfFile1 = arrayOfFile1;
                    int m = arrayOfFile1.length;
                    int k = 0;
                    boolean bool = false;
                    while (true) {
                        f += (float) (SYNTHETIC_LOCAL_VARIABLE_16.length() / 1024.0D);
                        k++;
                        object = SYNTHETIC_LOCAL_VARIABLE_6;
                    }
                    if (f > 1000.0D) {
                        f = (float) (f / 1024.0D);
                        str2 = " MB";
                    } else {
                        str2 = " KB";
                    }
                    this.cap_rv_no_of_items++;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(decimalFormat.format(f));
                    stringBuilder3.append(str2);
                    String str3 = stringBuilder3.toString();
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(object);
                    stringBuilder4.append(" ss");
                    String str4 = stringBuilder4.toString();
                    boolean bool1 = file.isDirectory();
                    arrayList.add(new filesCapRVData(str1, str3, str4, "1-3pm", bool1));
                    if (this.show_cap_rv_ad && this.cap_rv_no_of_items == 3)
                        arrayList.add(new filesCapRVData("ad", "1MB", true));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("capData: ");
                    stringBuilder2.append(str1);
                    Log.d("HomeScreen", stringBuilder2.toString());
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("capData: ");
                    stringBuilder1.append(f);
                    stringBuilder1.append(str2);
                    Log.d("HomeScreen", stringBuilder1.toString());
                }
                continue;
            }
        }
        this.cap_data = arrayList;
        return arrayList;
    }

    private List<filesCapRVData> filesData() {
        int i = 0;
        this.files_rv_no_of_items = 0;
        ArrayList<filesCapRVData> arrayList = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/");
        stringBuilder.append(getResources().getString(2131820571));
        stringBuilder.append("/");
        stringBuilder.append("Files");
        String str = stringBuilder.toString();
        this.homescreen_rv_pref.edit().putString("TOP_PANEL_FILES_CURRENT_DIR", str).apply();
        File[] arrayOfFile = (new File(str)).listFiles();
        if (this.show_files_rv_ad)
            arrayList.add(new filesCapRVData("ad", "1MB", true));
        if (arrayOfFile != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            int j = arrayOfFile.length;
            while (i < j) {
                File file = arrayOfFile[i];
                String str1 = file.getName();
                if (str1.contains(".pdf") || file.isDirectory() || str1.contains(".PDF")) {
                    float f1;
                    String str2 = " KB";
                    if (file.isDirectory()) {
                        f1 = (float) (FileUtils.sizeOfDirectory(file) / 1024.0D);
                    } else {
                        f1 = (float) (file.length() / 1024.0D);
                    }
                    float f2 = f1;
                    if (f1 > 1000.0D) {
                        f2 = (float) (f1 / 1024.0D);
                        str2 = " MB";
                    }
                    this.files_rv_no_of_items++;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(decimalFormat.format(f2));
                    stringBuilder3.append(str2);
                    arrayList.add(new filesCapRVData(str1, stringBuilder3.toString(), file.isDirectory()));
                    if (this.show_files_rv_ad && this.files_rv_no_of_items == 3)
                        arrayList.add(new filesCapRVData("ad", "1MB", true));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("filesData: ");
                    stringBuilder2.append(str1);
                    Log.d("HomeScreen", stringBuilder2.toString());
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("filesData: ");
                    stringBuilder1.append(f2);
                    stringBuilder1.append(str2);
                    Log.d("HomeScreen", stringBuilder1.toString());
                }
                i++;
            }
        }
        this.files_data = arrayList;
        return arrayList;
    }

    protected void DeviceScaling(View[] paramArrayOfView, float[][] paramArrayOffloat) {
        int i;
        for (i = 0; i < paramArrayOffloat.length; i++) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) paramArrayOfView[i].getLayoutParams();
            double d = (this.current_screen_width * paramArrayOffloat[i][0]);
            Double double_ = DESIGN_DEVICE_DENSITY;
            marginLayoutParams.setMargins((int) (d * double_.doubleValue()), (int) ((this.current_screen_height * paramArrayOffloat[i][1]) * double_.doubleValue()), (int) ((this.current_screen_width * paramArrayOffloat[i][2]) * double_.doubleValue()), (int) ((this.current_screen_height * paramArrayOffloat[i][3]) * double_.doubleValue()));
            (paramArrayOfView[i].getLayoutParams()).width = (int) ((this.current_screen_width * paramArrayOffloat[i][4]) * double_.doubleValue());
            float f = paramArrayOffloat[i][5];
            (paramArrayOfView[i].getLayoutParams()).height = (int) ((this.current_screen_width * paramArrayOffloat[i][4]) * double_.doubleValue() * f);
            paramArrayOfView[i].requestLayout();
        }
    }

    public void bottomnav_anim(int[] paramArrayOfint, final String mode) {
        ArrayList<BitmapDrawable> arrayList = new ArrayList();
        int j = paramArrayOfint.length;
        for (int i = 0; i < j; i++) {
            int k = paramArrayOfint[i];
            arrayList.add((BitmapDrawable) getResources().getDrawable(k));
        }
        final AnimationDrawable Anim = new AnimationDrawable();
        Iterator<BitmapDrawable> iterator = arrayList.iterator();
        while (iterator.hasNext())
            animationDrawable.addFrame((Drawable) iterator.next(), 25);
        animationDrawable.setOneShot(true);
        ImageView imageView = this.ImageViews[find_element_num(this.ImageViewNames, "bottomnav_bg")];
        imageView.setImageResource(0);
        imageView.setBackgroundDrawable((Drawable) animationDrawable);
        (new Handler()).post(new Runnable() {
            public void run() {
                Anim.start();
            }
        });
        (new CountDownTimer((paramArrayOfint.length * 25), 100L) {
            public void onFinish() {
                if (mode.equals("f2c")) {
                    HomeScreen.this.cap_ripple_button.setVisibility(4);
                    ImageView[] arrayOfImageView2 = HomeScreen.this.ImageViews;
                    HomeScreen homeScreen = HomeScreen.this;
                    arrayOfImageView2[homeScreen.find_element_num(homeScreen.ImageViewNames, "bottomnav_cap")].setImageDrawable(HomeScreen.this.getResources().getDrawable(R.drawable.bottomnav_captures_icon));
                    TextView[] arrayOfTextView3 = HomeScreen.this.TextViews;
                    homeScreen = HomeScreen.this;
                    arrayOfTextView3[homeScreen.find_element_num(homeScreen.TextViewNames, "bottomnav_cap_text")].setTextColor(HomeScreen.this.getResources().getColor(R.color.homescreen_bottomnav_active));
                    HomeScreen.this.files_ripple_button.setVisibility(0);
                    ImageView[] arrayOfImageView1 = HomeScreen.this.ImageViews;
                    homeScreen = HomeScreen.this;
                    arrayOfImageView1[homeScreen.find_element_num(homeScreen.ImageViewNames, "bottomnav_files")].setImageDrawable(HomeScreen.this.getResources().getDrawable(R.drawable.bottomnav_files_disabled_icon));
                    TextView[] arrayOfTextView2 = HomeScreen.this.TextViews;
                    homeScreen = HomeScreen.this;
                    arrayOfTextView2[homeScreen.find_element_num(homeScreen.TextViewNames, "bottomnav_files_text")].setTextColor(HomeScreen.this.getResources().getColor(R.color.homescreen_bottomnav_inactive));
                    RecyclerView[] arrayOfRecyclerView = HomeScreen.this.RecyclerViews;
                    homeScreen = HomeScreen.this;
                    arrayOfRecyclerView[homeScreen.find_element_num(homeScreen.RecyclerViewNames, "bottomnav_files_rv")].setVisibility(4);
                    arrayOfRecyclerView = HomeScreen.this.RecyclerViews;
                    homeScreen = HomeScreen.this;
                    arrayOfRecyclerView[homeScreen.find_element_num(homeScreen.RecyclerViewNames, "bottomnav_cap_rv")].setVisibility(0);
                    TextView[] arrayOfTextView1 = HomeScreen.this.TextViews;
                    homeScreen = HomeScreen.this;
                    arrayOfTextView1[homeScreen.find_element_num(homeScreen.TextViewNames, "tab_info")].setText(HomeScreen.this.getResources().getText(2131820576));
                    HomeScreen.this.top_panel_fold_up.setVisibility(4);
                    HomeScreen.this.checkbox_panel_share.setVisibility(4);
                    HomeScreen.this.checkbox_panel_delete.setVisibility(0);
                    HomeScreen.this.checkbox_panel_more.setVisibility(4);
                    HomeScreen.this.current_tab = "Captures";
                }
                if (mode.equals("c2f")) {
                    HomeScreen.this.cap_ripple_button.setVisibility(0);
                    ImageView[] arrayOfImageView2 = HomeScreen.this.ImageViews;
                    HomeScreen homeScreen = HomeScreen.this;
                    arrayOfImageView2[homeScreen.find_element_num(homeScreen.ImageViewNames, "bottomnav_cap")].setImageDrawable(HomeScreen.this.getResources().getDrawable(2131165284));
                    TextView[] arrayOfTextView3 = HomeScreen.this.TextViews;
                    homeScreen = HomeScreen.this;
                    arrayOfTextView3[homeScreen.find_element_num(homeScreen.TextViewNames, "bottomnav_cap_text")].setTextColor(HomeScreen.this.getResources().getColor(2131034235));
                    HomeScreen.this.files_ripple_button.setVisibility(4);
                    ImageView[] arrayOfImageView1 = HomeScreen.this.ImageViews;
                    homeScreen = HomeScreen.this;
                    arrayOfImageView1[homeScreen.find_element_num(homeScreen.ImageViewNames, "bottomnav_files")].setImageDrawable(HomeScreen.this.getResources().getDrawable(2131165288));
                    TextView[] arrayOfTextView2 = HomeScreen.this.TextViews;
                    homeScreen = HomeScreen.this;
                    arrayOfTextView2[homeScreen.find_element_num(homeScreen.TextViewNames, "bottomnav_files_text")].setTextColor(HomeScreen.this.getResources().getColor(2131034234));
                    RecyclerView[] arrayOfRecyclerView = HomeScreen.this.RecyclerViews;
                    homeScreen = HomeScreen.this;
                    arrayOfRecyclerView[homeScreen.find_element_num(homeScreen.RecyclerViewNames, "bottomnav_files_rv")].setVisibility(0);
                    arrayOfRecyclerView = HomeScreen.this.RecyclerViews;
                    homeScreen = HomeScreen.this;
                    arrayOfRecyclerView[homeScreen.find_element_num(homeScreen.RecyclerViewNames, "bottomnav_cap_rv")].setVisibility(4);
                    TextView[] arrayOfTextView1 = HomeScreen.this.TextViews;
                    homeScreen = HomeScreen.this;
                    arrayOfTextView1[homeScreen.find_element_num(homeScreen.TextViewNames, "tab_info")].setText(HomeScreen.this.getResources().getText(2131820628));
                    HomeScreen.this.top_panel_fold_up.setVisibility(0);
                    HomeScreen.this.checkbox_panel_share.setVisibility(0);
                    HomeScreen.this.checkbox_panel_delete.setVisibility(4);
                    HomeScreen.this.checkbox_panel_more.setVisibility(0);
                    HomeScreen.this.current_tab = "Files";
                }
                HomeScreen.this.homescreen_rv_pref.edit().putString("CURRENT_TAB", HomeScreen.this.current_tab).apply();
            }

            public void onTick(long param1Long) {
            }
        }).start();
    }

    public boolean checkForExistingDir(File[] paramArrayOfFile, String paramString) {
        boolean bool2 = true;
        boolean bool1 = true;
        try {
            int j = paramArrayOfFile.length;
            for (int i = 0; i < j; i++) {
                bool2 = bool1;
                boolean bool = paramString.equals(paramArrayOfFile[i].getName());
                if (bool)
                    bool1 = false;
            }
            return bool1;
        } catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkForExistingDir: ");
            stringBuilder.append(exception.getMessage());
            Log.d("HomeScreen", stringBuilder.toString());
            return bool2;
        }
    }

    public boolean checkPermission(String paramString, int paramInt) {
        boolean bool;
        if (ContextCompat.checkSelfPermission((Context) this, paramString) == -1) {
            bool = true;
        } else {
            bool = false;
        }
        if (bool)
            ActivityCompat.requestPermissions((Activity) this, new String[]{paramString}, paramInt);
        return !bool;
    }

    public void createFold() {
        checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", 101);
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(Environment.getExternalStorageDirectory());
        stringBuilder1.append("/");
        stringBuilder1.append(getResources().getString(2131820571));
        String str = stringBuilder1.toString();
        File file1 = new File(str);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append("/");
        stringBuilder2.append("Files");
        File file3 = new File(stringBuilder2.toString());
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str);
        stringBuilder3.append("/");
        stringBuilder3.append("Captures");
        File file2 = new File(stringBuilder3.toString());
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        if (!file1.exists()) {
            bool1 = file1.mkdirs();
            bool2 = file3.mkdirs();
            bool3 = file2.mkdirs();
        }
        if (bool1 || bool3 || bool2) {
            Log.d("HomeScreen", "createFold: AS folder");
            return;
        }
        Log.d("HomeScreen", "createFold: not done");
    }

    public void dirChooserMoveCopy(final String current_dir_path, final List<String> filesToMoveCopy, final String option_selected) {
        ChooserDialog chooserDialog = (new ChooserDialog((Activity) this)).withFilter(true, false, new String[0]);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/");
        stringBuilder.append(getResources().getString(2131820571));
        stringBuilder.append("/");
        stringBuilder.append("Files");
        chooserDialog.withStartFile(stringBuilder.toString()).titleFollowsDir(true).withChosenListener(new ChooserDialog.Result() {
            public void onChoosePath(String param1String, File param1File) {
                for (String str : filesToMoveCopy) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(current_dir_path);
                    stringBuilder.append("/");
                    stringBuilder.append(((filesCapRVData) HomeScreen.this.files_data.get(Integer.parseInt(str))).filename);
                    File file = new File(stringBuilder.toString());
                    if (!file.isDirectory()) {
                        try {
                            FileUtils.copyFileToDirectory(file, param1File);
                        } catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                        if (option_selected.equals(HomeScreen.this.getResources().getString(2131820637)) && file.delete())
                            Log.d("HomeScreen", "onChoosePath: ");
                        continue;
                    }
                    if (param1String.equals(file.getAbsolutePath())) {
                        Toast.makeText((Context) HomeScreen.this, "Incorrect Path", 0).show();
                        continue;
                    }
                    try {
                        FileUtils.copyDirectoryToDirectory(file, param1File);
                    } catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                    if (option_selected.equals(HomeScreen.this.getResources().getString(2131820637)))
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                }
                HomeScreen.this.files_tab_adapter.refreshFilesDataSet();
                HomeScreen.this.checkbox_panel.setVisibility(4);
                HomeScreen.this.cap_ripple_button.setVisibility(0);
                HomeScreen.this.files_tab_adapter.refreshFilesDataSet();
                ImageView[] arrayOfImageView = HomeScreen.this.ImageViews;
                HomeScreen homeScreen = HomeScreen.this;
                arrayOfImageView[homeScreen.find_element_num(homeScreen.ImageViewNames, "play_icon")].setVisibility(0);
                HomeScreen.this.homescreen_rv_pref.edit().putString("CHECKBOX_FILES_RV", "").apply();
                if (option_selected.equals(HomeScreen.this.getResources().getString(2131820635))) {
                    HomeScreen homeScreen1 = HomeScreen.this;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Files copied: ");
                    stringBuilder.append(filesToMoveCopy.size());
                    Toast.makeText((Context) homeScreen1, stringBuilder.toString(), 0).show();
                }
                if (option_selected.equals(HomeScreen.this.getResources().getString(2131820637))) {
                    HomeScreen homeScreen1 = HomeScreen.this;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Files moved: ");
                    stringBuilder.append(filesToMoveCopy.size());
                    Toast.makeText((Context) homeScreen1, stringBuilder.toString(), 0).show();
                }
            }
        }).build().show();
    }

    protected void findViewByIds() {
        this.play_stop_ind = (TextView) findViewById(2131296493);
        this.files_ripple_button = (ImageButton) findViewById(2131296477);
        this.cap_ripple_button = (ImageButton) findViewById(2131296474);
        this.top_panel_fold_up = (ImageButton) findViewById(2131296499);
        this.top_panel_refresh = (ImageButton) findViewById(2131296500);
        this.top_panel_add_fold = (ImageButton) findViewById(2131296498);
        this.checkbox_panel_cancel = (ImageButton) findViewById(2131296484);
        this.checkbox_panel_share = (ImageButton) findViewById(2131296488);
        this.checkbox_panel_more = (ImageButton) findViewById(2131296486);
        this.checkbox_panel_select = (ImageButton) findViewById(2131296487);
        this.checkbox_panel_delete = (ImageButton) findViewById(2131296485);
        this.checkbox_panel = (ConstraintLayout) findViewById(2131296483);
        int j = this.ImageViews.length;
        for (int i = 0; i < j; i++) {
            ImageView[] arrayOfImageView = this.ImageViews;
            if (i < arrayOfImageView.length)
                arrayOfImageView[i] = (ImageView) findViewById(this.ImageViewIds[i]);
            Guideline[] arrayOfGuideline = this.Guidelines;
            if (i < arrayOfGuideline.length)
                arrayOfGuideline[i] = (Guideline) findViewById(this.GuideLineIds[i]);
            TextView[] arrayOfTextView = this.TextViews;
            if (i < arrayOfTextView.length)
                arrayOfTextView[i] = (TextView) findViewById(this.TextViewIds[i]);
            RecyclerView[] arrayOfRecyclerView = this.RecyclerViews;
            if (i < arrayOfRecyclerView.length)
                arrayOfRecyclerView[i] = (RecyclerView) findViewById(this.RecyclerViewIds[i]);
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

    public void launchOverlayScreen(View paramView) {
        Log.d("HomeScreen", "launchOverlayScreen: 1");
        if (this.play_stop_ind.getText().toString().equals("play"))
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays((Context) this)) {
                (new AlertDialog.Builder((Context) this)).setCancelable(false).setTitle("Display Over Other Apps").setMessage("Turn on display over other apps in settings to start AutoCapture!").setPositiveButton("Take me there", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("package:");
                        stringBuilder.append(HomeScreen.this.getPackageName());
                        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(stringBuilder.toString()));
                        HomeScreen.this.startActivityForResult(intent, 103);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                        param1DialogInterface.dismiss();
                    }
                }).create().show();
            } else {
                showAlertDialogWithEditText("New Screen Shots Folder Name?", "FOLD_NAME_FOR_CAP");
            }
        if (this.play_stop_ind.getText().toString().equals("stop")) {
            startService(AutoCaptureService.getStopIntent((Context) this));
            this.overlayscreen_pref.edit().putBoolean("AS_ACTIVE", false).apply();
            this.play_stop_ind.setText("play");
            this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")].setImageResource(2131165290);
        }
    }

    public void launchPreferencesScreen(View paramView) {
        startActivity(new Intent((Context) this, PreferencesScreen.class));
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt1 == 202) {
            if (paramInt2 == -1) {
                this.homescreen_rv_pref.edit().putInt("LAST_NUM_WITH_CONV", 0).apply();
                startService(AutoCaptureService.getStartIntent((Context) this, paramInt2, paramIntent));
                this.play_stop_ind.setText("stop");
                this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")].setImageResource(2131165440);
                return;
            }
            Log.d("HomeScreen", "onActivityResult: failed to start autocapture");
            Toast.makeText(getApplicationContext(), "Failed to start AutoCapture", 0).show();
        }
    }

    public void onBackPressed() {
        int k = 0;
        int i = 0;
        Iterator<filesCapRVData> iterator = this.files_data.iterator();
        while (iterator.hasNext()) {
            int m = i;
            if (((filesCapRVData) iterator.next()).checked)
                m = i + 1;
            i = m;
        }
        iterator = this.cap_data.iterator();
        int j;
        for (j = k; iterator.hasNext(); j = k) {
            k = j;
            if (((filesCapRVData) iterator.next()).checked)
                k = j + 1;
        }
        if ((i == this.cap_data.size() && this.current_tab.equals("Captures")) || (j == this.files_data.size() && this.current_tab.equals("Files"))) {
            if (this.back_press_twice == 0) {
                Toast.makeText((Context) this, "Press back again to exit", 0).show();
                this.back_press_twice++;
                return;
            }
            super.onBackPressed();
            return;
        }
        this.checkbox_panel.setVisibility(8);
        this.checkbox_panel_select.setImageResource(2131165363);
        this.cap_tab_adapter.setCheckboxModeDeselectAll();
        this.files_tab_adapter.setCheckboxModeDeselectAll();
        if (this.current_tab.equals("Files"))
            this.cap_ripple_button.setVisibility(0);
        if (this.current_tab.equals("Captures"))
            this.files_ripple_button.setVisibility(0);
        this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")].setVisibility(0);
    }

    public void onClickListeners() {
        this.cap_ripple_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                HomeScreen.this.bottomnav_anim(new int[]{2131165295, 2131165303, 2131165304, 2131165305, 2131165306, 2131165307, 2131165308, 2131165309, 2131165293}, "f2c");
            }
        });
        this.files_ripple_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                HomeScreen.this.bottomnav_anim(new int[]{2131165293, 2131165296, 2131165297, 2131165298, 2131165299, 2131165300, 2131165301, 2131165302, 2131165295}, "c2f");
            }
        });
        this.top_panel_fold_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                HomeScreen.this.files_tab_adapter.changeDataSetForFoldUp();
            }
        });
        this.top_panel_refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                // Byte code:
                //   0: aload_0
                //   1: getfield this$0 : Lcom/infernodevelopers/autoshot/HomeScreen;
                //   4: getfield current_tab : Ljava/lang/String;
                //   7: astore_1
                //   8: aload_1
                //   9: invokevirtual hashCode : ()I
                //   12: istore_2
                //   13: iload_2
                //   14: ldc 14910989
                //   16: if_icmpeq -> 42
                //   19: iload_2
                //   20: ldc 67881559
                //   22: if_icmpeq -> 28
                //   25: goto -> 56
                //   28: aload_1
                //   29: ldc 'Files'
                //   31: invokevirtual equals : (Ljava/lang/Object;)Z
                //   34: ifeq -> 25
                //   37: iconst_0
                //   38: istore_2
                //   39: goto -> 58
                //   42: aload_1
                //   43: ldc 'Captures'
                //   45: invokevirtual equals : (Ljava/lang/Object;)Z
                //   48: ifeq -> 25
                //   51: iconst_1
                //   52: istore_2
                //   53: goto -> 58
                //   56: iconst_m1
                //   57: istore_2
                //   58: iload_2
                //   59: ifeq -> 75
                //   62: aload_0
                //   63: getfield this$0 : Lcom/infernodevelopers/autoshot/HomeScreen;
                //   66: getfield cap_tab_adapter : Lcom/infernodevelopers/autoshot/Adapters/filesCapRVAdapter;
                //   69: invokevirtual refreshCapDataSet : ()V
                //   72: goto -> 85
                //   75: aload_0
                //   76: getfield this$0 : Lcom/infernodevelopers/autoshot/HomeScreen;
                //   79: getfield files_tab_adapter : Lcom/infernodevelopers/autoshot/Adapters/filesCapRVAdapter;
                //   82: invokevirtual refreshFilesDataSet : ()V
                //   85: aload_0
                //   86: getfield this$0 : Lcom/infernodevelopers/autoshot/HomeScreen;
                //   89: getfield checkbox_panel : Landroidx/constraintlayout/widget/ConstraintLayout;
                //   92: iconst_4
                //   93: invokevirtual setVisibility : (I)V
                //   96: aload_0
                //   97: getfield this$0 : Lcom/infernodevelopers/autoshot/HomeScreen;
                //   100: getfield ImageViews : [Landroid/widget/ImageView;
                //   103: astore_1
                //   104: aload_0
                //   105: getfield this$0 : Lcom/infernodevelopers/autoshot/HomeScreen;
                //   108: astore_3
                //   109: aload_1
                //   110: aload_3
                //   111: aload_3
                //   112: getfield ImageViewNames : [Ljava/lang/String;
                //   115: ldc 'play_icon'
                //   117: invokevirtual find_element_num : ([Ljava/lang/String;Ljava/lang/String;)I
                //   120: aaload
                //   121: iconst_0
                //   122: invokevirtual setVisibility : (I)V
                //   125: aload_0
                //   126: getfield this$0 : Lcom/infernodevelopers/autoshot/HomeScreen;
                //   129: getfield homescreen_rv_pref : Landroid/content/SharedPreferences;
                //   132: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
                //   137: ldc 'CHECKBOX_FILES_RV'
                //   139: ldc ''
                //   141: invokeinterface putString : (Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
                //   146: ldc 'CHECKBOX_CAP_RV'
                //   148: ldc ''
                //   150: invokeinterface putString : (Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
                //   155: invokeinterface apply : ()V
                //   160: return
            }
        });
        this.top_panel_add_fold.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                HomeScreen.this.showAlertDialogWithEditText("Type In New Name!", "ADD_FOLD");
            }
        });
        this.checkbox_panel_select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                ImageButton imageButton = (ImageButton) param1View;
                if (HomeScreen.this.current_tab.equals("Captures")) {
                    int i = 0;
                    Iterator<filesCapRVData> iterator = HomeScreen.this.cap_data.iterator();
                    while (iterator.hasNext()) {
                        int j = i;
                        if (((filesCapRVData) iterator.next()).checked)
                            j = i + 1;
                        i = j;
                    }
                    if (i == HomeScreen.this.cap_data.size()) {
                        HomeScreen.this.cap_tab_adapter.setCheckboxModeDeselectAll();
                        imageButton.setImageResource(2131165363);
                    } else {
                        HomeScreen.this.cap_tab_adapter.setCheckboxModeSelectAll();
                        imageButton.setImageResource(2131165359);
                    }
                }
                if (HomeScreen.this.current_tab.equals("Files")) {
                    int i = 0;
                    Iterator<filesCapRVData> iterator = HomeScreen.this.files_data.iterator();
                    while (iterator.hasNext()) {
                        int j = i;
                        if (((filesCapRVData) iterator.next()).checked)
                            j = i + 1;
                        i = j;
                    }
                    if (i == HomeScreen.this.files_data.size()) {
                        HomeScreen.this.files_tab_adapter.setCheckboxModeDeselectAll();
                        imageButton.setImageResource(2131165363);
                        return;
                    }
                    HomeScreen.this.files_tab_adapter.setCheckboxModeSelectAll();
                    imageButton.setImageResource(2131165359);
                }
            }
        });
        this.checkbox_panel_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                HomeScreen.this.checkbox_panel.setVisibility(8);
                HomeScreen.this.checkbox_panel_select.setImageResource(2131165363);
                HomeScreen.this.cap_tab_adapter.setCheckboxModeDeselectAll();
                HomeScreen.this.files_tab_adapter.setCheckboxModeDeselectAll();
                if (HomeScreen.this.current_tab.equals("Files"))
                    HomeScreen.this.cap_ripple_button.setVisibility(0);
                if (HomeScreen.this.current_tab.equals("Captures"))
                    HomeScreen.this.files_ripple_button.setVisibility(0);
                ImageView[] arrayOfImageView = HomeScreen.this.ImageViews;
                HomeScreen homeScreen = HomeScreen.this;
                arrayOfImageView[homeScreen.find_element_num(homeScreen.ImageViewNames, "play_icon")].setVisibility(0);
            }
        });
        this.checkbox_panel_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                String str = HomeScreen.this.root_path_cap;
                ArrayList<String> arrayList = new ArrayList();
                for (int i = 0; i < HomeScreen.this.cap_data.size(); i++) {
                    if (((filesCapRVData) HomeScreen.this.cap_data.get(i)).checked)
                        arrayList.add(Integer.toString(i));
                }
                HomeScreen homeScreen = HomeScreen.this;
                homeScreen.showDeleteAlertDialogAndDelete(str, arrayList, "CHECKBOX_CAP_RV", homeScreen.cap_data);
            }
        });
        this.checkbox_panel_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                ArrayList<String> arrayList = new ArrayList();
                for (int i = 0; i < HomeScreen.this.files_data.size(); i++) {
                    if (((filesCapRVData) HomeScreen.this.files_data.get(i)).checked && HomeScreen.this.homescreen_rv_pref.getString("CURRENT_TAB", "Captures").equals("Files"))
                        arrayList.add(((filesCapRVData) HomeScreen.this.files_data.get(i)).filename);
                }
                if (arrayList.size() != 0) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.SEND_MULTIPLE");
                    intent.putExtra("android.intent.extra.SUBJECT", "Here are some files.");
                    intent.setType("*/*");
                    ArrayList<Uri> arrayList1 = new ArrayList();
                    for (String str : arrayList) {
                        if (!str.equals("0"))
                            arrayList1.add(Uri.fromFile(new File(str)));
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onClick: ");
                    stringBuilder.append(arrayList1.size());
                    Log.d("HomeScreen", stringBuilder.toString());
                    if (arrayList1.size() != 0) {
                        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList1);
                        HomeScreen.this.startActivity(Intent.createChooser(intent, "Share files to..."));
                        return;
                    }
                    Toast.makeText((Context) HomeScreen.this, "Select one or more pdf files to send!", 0).show();
                }
            }
        });
        this.checkbox_panel_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                HomeScreen homeScreen = HomeScreen.this;
                PopupMenu popupMenu = new PopupMenu((Context) homeScreen, (View) homeScreen.checkbox_panel_more);
                popupMenu.getMenuInflater().inflate(2131558400, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem param2MenuItem) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("onMenuItemClick: ");
                        stringBuilder.append(param2MenuItem.getTitle());
                        Log.d("HomeScreen", stringBuilder.toString());
                        String str = HomeScreen.this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", "");
                        ArrayList<String> arrayList = new ArrayList();
                        for (int i = 0; i < HomeScreen.this.files_data.size(); i++) {
                            if (((filesCapRVData) HomeScreen.this.files_data.get(i)).checked)
                                arrayList.add(Integer.toString(i));
                        }
                        if (param2MenuItem.getTitle().toString().equals(HomeScreen.this.getResources().getString(2131820636)))
                            HomeScreen.this.showDeleteAlertDialogAndDelete(str, arrayList, "CHECKBOX_FILES_RV", HomeScreen.this.files_data);
                        if (param2MenuItem.getTitle().toString().equals(HomeScreen.this.getResources().getString(2131820635)))
                            HomeScreen.this.dirChooserMoveCopy(str, arrayList, param2MenuItem.getTitle().toString());
                        if (param2MenuItem.getTitle().toString().equals(HomeScreen.this.getResources().getString(2131820637)))
                            HomeScreen.this.dirChooserMoveCopy(str, arrayList, param2MenuItem.getTitle().toString());
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(2131492914);
        StrictMode.setVmPolicy((new StrictMode.VmPolicy.Builder()).build());
        this.adRef.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {
            }

            public void onDataChange(DataSnapshot param1DataSnapshot) {
                HomeScreen.this.show_cap_rv_ad = param1DataSnapshot.child("capRVAds").getValue().toString().equals("1");
                HomeScreen.this.show_files_rv_ad = param1DataSnapshot.child("filesRVAds").getValue().toString().equals("1");
                HomeScreen.this.show_create_pdf_ad = param1DataSnapshot.child("createPdfAd").getValue().toString().equals("1");
                HomeScreen.this.show_process_imgs_ad = param1DataSnapshot.child("processImagesAd").getValue().toString().equals("1");
                HomeScreen.this.show_process_imgs_rewarded_ad = param1DataSnapshot.child("processImagesRewardedAd").getValue().toString().equals("1");
                HomeScreen.this.homescreen_rv_pref.edit().putBoolean("capRVAds", HomeScreen.this.show_cap_rv_ad).putBoolean("filesRVAds", HomeScreen.this.show_files_rv_ad).putBoolean("createPdfAd", HomeScreen.this.show_create_pdf_ad).putBoolean("processImagesAd", HomeScreen.this.show_process_imgs_ad).putBoolean("processImagesRewardedAd", HomeScreen.this.show_process_imgs_rewarded_ad).apply();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        this.current_screen_height = displayMetrics.heightPixels;
        this.current_screen_width = displayMetrics.widthPixels;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/");
        stringBuilder.append(getResources().getString(2131820571));
        stringBuilder.append("/");
        stringBuilder.append("Files");
        this.root_path_files = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/");
        stringBuilder.append(getResources().getString(2131820571));
        stringBuilder.append("/");
        stringBuilder.append("Captures");
        this.root_path_cap = stringBuilder.toString();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        this.settings = getApplicationContext().getSharedPreferences("SETTINGS_FILE", 0);
        this.homescreen_rv_pref = getApplicationContext().getSharedPreferences("HOMESCREEN_RV", 0);
        this.overlayscreen_pref = getApplicationContext().getSharedPreferences("OVERLAY_SCREEN", 0);
        this.homescreen_rv_pref.edit().putString("CURRENT_TAB", this.current_tab).apply();
        this.homescreen_rv_pref.edit().putBoolean("CHANGE_UI_ON_STOP_FROM_OVERLAY", false).apply();
        createFold();
        findViewByIds();
        DeviceScaling((View[]) this.ImageViews, this.ImageViewScaling);
        DeviceScaling((View[]) this.RecyclerViews, this.RecyclerViewScaling);
        onClickListeners();
        this.cap_tab_adapter = new filesCapRVAdapter(capData(), (Context) this, 101, this.checkbox_panel, this.cap_ripple_button, this.files_ripple_button, this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")]);
        this.RecyclerViews[find_element_num(this.RecyclerViewNames, "bottomnav_cap_rv")].setAdapter((RecyclerView.Adapter) this.cap_tab_adapter);
        this.RecyclerViews[find_element_num(this.RecyclerViewNames, "bottomnav_cap_rv")].setLayoutManager((RecyclerView.LayoutManager) new LinearLayoutManager((Context) this));
        this.files_tab_adapter = new filesCapRVAdapter(filesData(), (Context) this, 100, this.checkbox_panel, this.cap_ripple_button, this.files_ripple_button, this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")]);
        this.RecyclerViews[find_element_num(this.RecyclerViewNames, "bottomnav_files_rv")].setAdapter((RecyclerView.Adapter) this.files_tab_adapter);
        this.RecyclerViews[find_element_num(this.RecyclerViewNames, "bottomnav_files_rv")].setLayoutManager((RecyclerView.LayoutManager) new LinearLayoutManager((Context) this));
        this.ImageViews[find_element_num(this.ImageViewNames, "as_icon_title")].setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Notices notices = new Notices();
                notices.addNotice(new Notice("Freepik Icons", "https://www.flaticon.com", "freepikcompany", (License) new FreepikLicence()));
                notices.addNotice(new Notice("android-file-chooser", "https://github.com/hedzr/android-file-chooser", "Hedzr Yeh", (License) new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Glide", "http://bumptech.github.io/glide/", "Sam Judd", (License) new GlideLicence()));
                notices.addNotice(new Notice("ImagePicker", "https://github.com/bkhezry/android-image-picker", "Esa Firman", (License) new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Spotlight", "https://github.com/morristech/Spotlight-1", "Taku Semba", (License) new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Resizable Rectangle Overlay", "https://github.com/ChintanRathod/ResizableRectangleOverlay", "Esa Firman", (License) new MITLicense()));
                (new LicensesDialog.Builder((Context) HomeScreen.this)).setNotices(notices).setIncludeOwnLicense(true).build().show();
            }
        });
        this.TextViews[find_element_num(this.TextViewNames, "title")].setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                HomeScreen.this.homescreen_rv_pref.edit().putBoolean("APP_LAUNCH_FOR_FIRST_TIME", true).apply();
                HomeScreen.this.homescreen_rv_pref.edit().putBoolean("FOLD_LAUNCH_FIRST_TIME", true).apply();
            }
        });
        this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")].getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ImageView[] arrayOfImageView = HomeScreen.this.ImageViews;
                HomeScreen homeScreen = HomeScreen.this;
                arrayOfImageView[homeScreen.find_element_num(homeScreen.ImageViewNames, "play_icon")].getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (HomeScreen.this.homescreen_rv_pref.getBoolean("APP_LAUNCH_FOR_FIRST_TIME", true))
                    HomeScreen.this.spotlight();
            }
        });
    }

    public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
        super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
        if (paramInt == 101) {
            boolean bool;
            if (paramArrayOfint[0] == 0) {
                bool = true;
            } else {
                bool = false;
            }
            int i = ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE") ^ true;
            if (i != 0 && !bool)
                (new AlertDialog.Builder((Context) this)).setTitle("Permission Denied").setMessage("To use this app go to Settings->Apps->AutoShot->Permissions->Allow Storage Permissions").setPositiveButton("Take me there", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", HomeScreen.this.getPackageName(), null));
                        HomeScreen.this.startActivity(intent);
                    }
                }).setCancelable(false).create().show();
            if (paramArrayOfint[0] == -1 && i == 0) {
                this.storage_permission_denied_counter++;
                (new AlertDialog.Builder((Context) this)).setTitle("Permission Denied").setMessage("Without this permission the app is unable to show Captured ScreenShots or the folder containing all PDFs created by the app.Are you sure you want to deny this permission?").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                        HomeScreen.this.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", 101);
                    }
                }).setNegativeButton("I'm sure,Close the app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                        HomeScreen.this.finish();
                        System.exit(0);
                    }
                }).setCancelable(false).create().show();
            }
        }
        if (paramInt == 103) {
            Log.d("HomeScreen", "onRequestPermissionsResult: overlayPermissionResult ");
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays((Context) this)) {
                    Log.d("HomeScreen", "onRequestPermissionsResult: permission denied ");
                    Toast.makeText((Context) this, "Draw over other app permission not turned on. Can't start AutoCapture without the permission.", 1).show();
                    return;
                }
                Log.d("HomeScreen", "onRequestPermissionsResult: starting autocapture");
                startAutoCapture();
                return;
            }
            Log.d("HomeScreen", "onRequestPermissionsResult: not available");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onRequestPermissionsResult: ");
            stringBuilder.append(Build.VERSION.SDK_INT);
            Log.d("HomeScreen", stringBuilder.toString());
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.homescreen_rv_pref.getBoolean("CHANGE_UI_ON_STOP_FROM_OVERLAY", false)) {
            this.play_stop_ind.setText("play");
            this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")].setImageResource(2131165290);
        }
        this.back_press_twice = 0;
        this.checkbox_panel.setVisibility(4);
        this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")].setVisibility(0);
        this.checkbox_panel_select.setImageResource(2131165363);
        this.cap_tab_adapter.refreshCapDataSet();
        this.cap_tab_adapter.setCheckboxModeDeselectAll();
        this.files_tab_adapter.refreshFilesDataSet();
        this.files_tab_adapter.setCheckboxModeDeselectAll();
        if (this.current_tab.equals("Files"))
            this.cap_ripple_button.setVisibility(0);
        if (this.current_tab.equals("Captures"))
            this.files_ripple_button.setVisibility(0);
        this.homescreen_rv_pref.edit().putString("CHECKBOX_CAP_RV", "").putString("CHECKBOX_FILES_RV", "").apply();
    }

    public void showAlertDialogWithEditText(String paramString1, final String editTextTextDialogMode) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) this);
        final EditText dirNameEditText = new EditText((Context) this);
        editText.setLayoutParams((ViewGroup.LayoutParams) new LinearLayout.LayoutParams(-2, -1));
        editText.setEms(10);
        editText.setMaxLines(1);
        if (editTextTextDialogMode.equals("FOLD_NAME_FOR_CAP")) {
            String str = (new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())).format(new Date());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AS_");
            stringBuilder.append(str);
            editText.setText(stringBuilder.toString());
        }
        builder.setView((View) editText).setTitle(paramString1).setPositiveButton("Confirm", null).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.cancel();
            }
        }).setCancelable(true);
        final AlertDialog edit_name_dialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(final DialogInterface dialog) {
                edit_name_dialog.getButton(-1).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param2View) {
                        byte b2 = 1;
                        byte b1 = b2;
                        if (editTextTextDialogMode.equals("ADD_FOLD")) {
                            File[] arrayOfFile;
                            File file;
                            if (HomeScreen.this.current_tab.equals("Files")) {
                                arrayOfFile = (new File(HomeScreen.this.root_path_files)).listFiles();
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(HomeScreen.this.homescreen_rv_pref.getString("TOP_PANEL_FILES_CURRENT_DIR", HomeScreen.this.root_path_files));
                                stringBuilder.append("/");
                                stringBuilder.append(dirNameEditText.getText().toString());
                                file = new File(stringBuilder.toString());
                            } else {
                                arrayOfFile = (new File(HomeScreen.this.root_path_cap)).listFiles();
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(HomeScreen.this.root_path_cap);
                                stringBuilder.append("/");
                                stringBuilder.append(dirNameEditText.getText().toString());
                                file = new File(stringBuilder.toString());
                            }
                            if (HomeScreen.this.checkForExistingDir(arrayOfFile, dirNameEditText.getText().toString())) {
                                b1 = b2;
                                if (file.mkdirs()) {
                                    String str = HomeScreen.this.current_tab;
                                    b1 = -1;
                                    int i = str.hashCode();
                                    if (i != 14910989) {
                                        if (i == 67881559 && str.equals("Files"))
                                            b1 = 0;
                                    } else if (str.equals("Captures")) {
                                        b1 = 1;
                                    }
                                    if (b1 != 0) {
                                        HomeScreen.this.cap_tab_adapter.refreshCapDataSet();
                                    } else {
                                        HomeScreen.this.files_tab_adapter.refreshFilesDataSet();
                                    }
                                    b1 = b2;
                                }
                            } else {
                                dirNameEditText.setError("Folder already exists try a different name!");
                                b1 = 0;
                            }
                        }
                        b2 = b1;
                        if (editTextTextDialogMode.equals("FOLD_NAME_FOR_CAP")) {
                            File[] arrayOfFile = (new File(HomeScreen.this.root_path_cap)).listFiles();
                            String str = dirNameEditText.getText().toString();
                            if (HomeScreen.this.checkForExistingDir(arrayOfFile, str)) {
                                StringBuilder stringBuilder1 = new StringBuilder();
                                stringBuilder1.append(HomeScreen.this.root_path_cap);
                                stringBuilder1.append("/");
                                stringBuilder1.append(str);
                                if ((new File(stringBuilder1.toString())).mkdirs())
                                    HomeScreen.this.cap_tab_adapter.refreshCapDataSet();
                                SharedPreferences.Editor editor = HomeScreen.this.overlayscreen_pref.edit();
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(HomeScreen.this.root_path_cap);
                                stringBuilder2.append("/");
                                stringBuilder2.append(str);
                                editor.putString("DIR_PATH", stringBuilder2.toString()).apply();
                                HomeScreen.this.startAutoCapture();
                                b2 = b1;
                            } else {
                                dirNameEditText.setError("Folder already exists try a different name!");
                                b2 = 0;
                            }
                        }
                        if (b2 != 0)
                            dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void showDeleteAlertDialogAndDelete(final String current_dir_path, final List<String> filesToDelete, final String pref_clear_key, final List<filesCapRVData> data) {
        (new AlertDialog.Builder((Context) this)).setTitle("Delete Selected Files?").setCancelable(true).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                if (filesToDelete.size() != 0) {
                    for (String str : filesToDelete) {
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append(current_dir_path);
                        stringBuilder1.append("/");
                        stringBuilder1.append(((filesCapRVData) data.get(Integer.parseInt(str))).filename);
                        File file = new File(stringBuilder1.toString());
                        if (file.isDirectory()) {
                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException iOException) {
                                iOException.printStackTrace();
                            }
                            continue;
                        }
                        if (iOException.delete())
                            Log.d("HomeScreen", "onMenuItemClick: ");
                    }
                    HomeScreen.this.checkbox_panel.setVisibility(4);
                    if (HomeScreen.this.current_tab.equals("Files")) {
                        HomeScreen.this.cap_ripple_button.setVisibility(0);
                        HomeScreen.this.files_tab_adapter.refreshFilesDataSet();
                    }
                    if (HomeScreen.this.current_tab.equals("Captures")) {
                        HomeScreen.this.files_ripple_button.setVisibility(0);
                        HomeScreen.this.cap_tab_adapter.refreshCapDataSet();
                    }
                    ImageView[] arrayOfImageView = HomeScreen.this.ImageViews;
                    HomeScreen homeScreen2 = HomeScreen.this;
                    arrayOfImageView[homeScreen2.find_element_num(homeScreen2.ImageViewNames, "play_icon")].setVisibility(0);
                    HomeScreen.this.homescreen_rv_pref.edit().putString(pref_clear_key, "").apply();
                    HomeScreen homeScreen1 = HomeScreen.this;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Files deleted: ");
                    stringBuilder.append(filesToDelete.size());
                    Toast.makeText((Context) homeScreen1, stringBuilder.toString(), 0).show();
                    return;
                }
                Toast.makeText((Context) HomeScreen.this, "No files selected!", 0).show();
            }
        }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.dismiss();
            }
        }).create().show();
    }

    public void spotlight() {
        SimpleTarget simpleTarget1 = ((SimpleTarget.Builder) ((SimpleTarget.Builder) ((SimpleTarget.Builder) (new SimpleTarget.Builder((Activity) this)).setPoint((View) this.ImageViews[find_element_num(this.ImageViewNames, "play_icon")])).setRadius(150.0F)).setTitle("Play").setDescription("Start Automatically Capturing Screen").setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
            public void onEnded(SimpleTarget param1SimpleTarget) {
            }

            public void onStarted(SimpleTarget param1SimpleTarget) {
            }
        })).build();
        SimpleTarget simpleTarget2 = ((SimpleTarget.Builder) ((SimpleTarget.Builder) ((SimpleTarget.Builder) (new SimpleTarget.Builder((Activity) this)).setPoint((View) this.cap_ripple_button)).setRadius(150.0F)).setTitle("Captures Tab").setDescription("All Captures by App Display Here").setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
            public void onEnded(SimpleTarget param1SimpleTarget) {
            }

            public void onStarted(SimpleTarget param1SimpleTarget) {
            }
        })).build();
        SimpleTarget simpleTarget3 = ((SimpleTarget.Builder) ((SimpleTarget.Builder) ((SimpleTarget.Builder) (new SimpleTarget.Builder((Activity) this)).setPoint((View) this.files_ripple_button)).setRadius(150.0F)).setTitle("Files Tab").setDescription("Files Stored Display Here").setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
            public void onEnded(SimpleTarget param1SimpleTarget) {
            }

            public void onStarted(SimpleTarget param1SimpleTarget) {
            }
        })).build();
        Spotlight.with((Activity) this).setOverlayColor(ContextCompat.getColor((Context) this, 2131034142)).setDuration(100L).setAnimation((TimeInterpolator) new DecelerateInterpolator(2.0F)).setTargets((Target[]) new SimpleTarget[]{simpleTarget1, simpleTarget2, simpleTarget3}).setClosedOnTouchedOutside(true).setOnSpotlightStartedListener(new OnSpotlightStartedListener() {
            public void onStarted() {
            }
        }).setOnSpotlightEndedListener(new OnSpotlightEndedListener() {
            public void onEnded() {
                HomeScreen.this.homescreen_rv_pref.edit().putBoolean("APP_LAUNCH_FOR_FIRST_TIME", false).apply();
            }
        }).start();
    }

    public void startAutoCapture() {
        Log.d("HomeScreen", "startAutoCapture: starting autocapture");
        startActivityForResult(((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent(), 202);
    }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\HomeScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */