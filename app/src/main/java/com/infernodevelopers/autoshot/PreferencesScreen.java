package com.infernodevelopers.autoshot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infernodevelopers.autoshot.Views.DrawView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class PreferencesScreen extends AppCompatActivity {
  public static final String AUTOSS2PDF = "AUTOSS2PDF";
  
  public static final String AUTOSSDELEXPORT = "AUTOSSDELEXPORT";
  
  public static final String AUTOSSDELPDF = "AUTOSSDELPDF";
  
  public static final Integer DEFAULT_CAP_DURATION = Integer.valueOf(20);
  
  public static final String DEFCAPTIME = "DEFCAPTIME";
  
  public static final String SCREEN_CROP = "SCREEN_CROP";
  
  public static final String SETTINGS_FILE = "SETTINGS_FILE";
  
  private static final String TAG = "PreferencesScreen";
  
  TextView captime;
  
  int[] checkBoxIds = new int[] { 2131296660 };
  
  String[] checkBoxNames = new String[] { "AUTOSSDELPDF" };
  
  ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
  
  FloatingActionButton crop_cancel;
  
  TextView crop_current_mode;
  
  FloatingActionButton crop_done;
  
  DrawView crop_view;
  
  ImageButton crop_view_choose_pref;
  
  ConstraintLayout crop_view_layout;
  
  Integer current_duration;
  
  int current_screen_height;
  
  int current_screen_width;
  
  ImageButton minus;
  
  ImageButton plus;
  
  TextView reset_to_default;
  
  SharedPreferences settings;
  
  protected void checkBoxListeners() {
    this.crop_view_choose_pref.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            PreferencesScreen.this.crop_view_layout.setVisibility(0);
          }
        });
    ((CheckBox)this.checkBoxes.get(find_element_num(this.checkBoxNames, "AUTOSSDELPDF"))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onCheckedChanged: autossdelpdf: ");
            stringBuilder.append(param1Boolean);
            Log.d("PreferencesScreen", stringBuilder.toString());
            PreferencesScreen.this.settings.edit().putBoolean("AUTOSSDELPDF", param1Boolean).apply();
            if (param1Boolean) {
              Toast.makeText(PreferencesScreen.this.getApplicationContext(), "Screenshots will be automatically deleted after being converted to pdf", 0).show();
              return;
            } 
            Toast.makeText(PreferencesScreen.this.getApplicationContext(), "Screenshots will not be automatically deleted after being converted to pdf", 0).show();
          }
        });
  }
  
  protected void findViewByIds() {
    this.plus = (ImageButton)findViewById(2131296653);
    this.minus = (ImageButton)findViewById(2131296652);
    this.captime = (TextView)findViewById(2131296655);
    this.reset_to_default = (TextView)findViewById(2131296672);
    this.crop_view_choose_pref = (ImageButton)findViewById(2131296657);
    this.crop_current_mode = (TextView)findViewById(2131296668);
    this.crop_view_layout = (ConstraintLayout)findViewById(2131296670);
    this.crop_done = (FloatingActionButton)findViewById(2131296665);
    this.crop_cancel = (FloatingActionButton)findViewById(2131296664);
    this.crop_view = (DrawView)findViewById(2131296669);
    int j = this.checkBoxIds.length;
    for (int i = 0; i < j; i++) {
      int[] arrayOfInt = this.checkBoxIds;
      if (i < arrayOfInt.length) {
        CheckBox checkBox = (CheckBox)findViewById(arrayOfInt[i]);
        this.checkBoxes.add(checkBox);
      } 
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
  
  public void finishPreferences(View paramView) {
    finish();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492974);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
    this.current_screen_height = displayMetrics.heightPixels;
    this.current_screen_width = displayMetrics.widthPixels;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.hide(); 
    this.settings = getApplicationContext().getSharedPreferences("SETTINGS_FILE", 0);
    findViewByIds();
    restorePreferences();
    checkBoxListeners();
    otherListeners();
  }
  
  protected void otherListeners() {
    this.crop_done.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("onClick:1 ");
            stringBuilder2.append(PreferencesScreen.this.crop_view.point1);
            Log.d("PreferencesScreen", stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("onClick:2 ");
            stringBuilder2.append(PreferencesScreen.this.crop_view.point2);
            Log.d("PreferencesScreen", stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("onClick:3 ");
            stringBuilder2.append(PreferencesScreen.this.crop_view.point3);
            Log.d("PreferencesScreen", stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("onClick:4 ");
            stringBuilder2.append(PreferencesScreen.this.crop_view.point4);
            Log.d("PreferencesScreen", stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("onClick: ");
            stringBuilder2.append(PreferencesScreen.this.current_screen_height);
            stringBuilder2.append(" ");
            stringBuilder2.append(PreferencesScreen.this.current_screen_width);
            Log.d("PreferencesScreen", stringBuilder2.toString());
            ArrayList<Integer> arrayList1 = new ArrayList();
            arrayList1.add(Integer.valueOf(PreferencesScreen.this.crop_view.point1.x));
            arrayList1.add(Integer.valueOf(PreferencesScreen.this.crop_view.point2.x));
            arrayList1.add(Integer.valueOf(PreferencesScreen.this.crop_view.point3.x));
            arrayList1.add(Integer.valueOf(PreferencesScreen.this.crop_view.point4.x));
            ArrayList<Integer> arrayList2 = new ArrayList();
            arrayList2.add(Integer.valueOf(PreferencesScreen.this.crop_view.point1.y));
            arrayList2.add(Integer.valueOf(PreferencesScreen.this.crop_view.point2.y));
            arrayList2.add(Integer.valueOf(PreferencesScreen.this.crop_view.point3.y));
            arrayList2.add(Integer.valueOf(PreferencesScreen.this.crop_view.point4.y));
            Collections.sort(arrayList1);
            Collections.sort(arrayList2);
            Integer integer = Integer.valueOf(0);
            if (((Integer)arrayList1.get(0)).intValue() < 0)
              arrayList1.set(0, integer); 
            if (((Integer)arrayList1.get(3)).intValue() > PreferencesScreen.this.current_screen_width)
              arrayList1.set(3, Integer.valueOf(PreferencesScreen.this.current_screen_width)); 
            if (((Integer)arrayList2.get(0)).intValue() < 0)
              arrayList2.set(0, integer); 
            if (((Integer)arrayList2.get(3)).intValue() > PreferencesScreen.this.current_screen_height)
              arrayList2.set(3, Integer.valueOf(PreferencesScreen.this.current_screen_height)); 
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(arrayList1.get(0));
            stringBuilder1.append("_");
            stringBuilder1.append(arrayList2.get(0));
            String str1 = stringBuilder1.toString();
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(arrayList1.get(3));
            stringBuilder4.append("_");
            stringBuilder4.append(arrayList2.get(3));
            String str2 = stringBuilder4.toString();
            SharedPreferences.Editor editor = PreferencesScreen.this.settings.edit();
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append(str1);
            stringBuilder5.append("_");
            stringBuilder5.append(str2);
            editor.putString("SCREEN_CROP", stringBuilder5.toString()).apply();
            Toast.makeText((Context)PreferencesScreen.this, "Screen Crop Settings Applied!", 0).show();
            if (((Integer)arrayList2.get(3)).intValue() - ((Integer)arrayList2.get(0)).intValue() < PreferencesScreen.this.current_screen_height || ((Integer)arrayList1.get(3)).intValue() - ((Integer)arrayList1.get(0)).intValue() < PreferencesScreen.this.current_screen_width) {
              PreferencesScreen.this.crop_current_mode.setText(PreferencesScreen.this.getResources().getString(2131820717));
            } else {
              SharedPreferences.Editor editor1 = PreferencesScreen.this.settings.edit();
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("0_0_");
              stringBuilder.append(PreferencesScreen.this.current_screen_width);
              stringBuilder.append("_");
              stringBuilder.append(PreferencesScreen.this.current_screen_height);
              editor1.putString("SCREEN_CROP", stringBuilder.toString()).apply();
              PreferencesScreen.this.crop_current_mode.setText(PreferencesScreen.this.getResources().getString(2131820718));
            } 
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("onClick: ");
            stringBuilder3.append(str1);
            stringBuilder3.append("_");
            stringBuilder3.append(str2);
            Log.d("PreferencesScreen", stringBuilder3.toString());
            PreferencesScreen.this.crop_view_layout.setVisibility(8);
          }
        });
    this.crop_cancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            PreferencesScreen.this.crop_view_layout.setVisibility(8);
            Toast.makeText((Context)PreferencesScreen.this, "Screen Crop Settings Reverted!", 0).show();
          }
        });
    this.reset_to_default.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Iterator<CheckBox> iterator = PreferencesScreen.this.checkBoxes.iterator();
            while (iterator.hasNext())
              ((CheckBox)iterator.next()).setChecked(false); 
            PreferencesScreen.this.current_duration = PreferencesScreen.DEFAULT_CAP_DURATION;
            TextView textView = PreferencesScreen.this.captime;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(PreferencesScreen.DEFAULT_CAP_DURATION);
            stringBuilder.append(" seconds");
            textView.setText(stringBuilder.toString());
            PreferencesScreen.this.settings.edit().putBoolean("AUTOSS2PDF", false).apply();
            PreferencesScreen.this.settings.edit().putBoolean("AUTOSSDELPDF", false).apply();
            PreferencesScreen.this.settings.edit().putBoolean("AUTOSSDELEXPORT", false).apply();
            PreferencesScreen.this.settings.edit().putInt("DEFCAPTIME", PreferencesScreen.DEFAULT_CAP_DURATION.intValue()).apply();
            SharedPreferences.Editor editor = PreferencesScreen.this.settings.edit();
            stringBuilder = new StringBuilder();
            stringBuilder.append("0_0_");
            stringBuilder.append(PreferencesScreen.this.current_screen_width);
            stringBuilder.append("_");
            stringBuilder.append(PreferencesScreen.this.current_screen_height);
            editor.putString("SCREEN_CROP", stringBuilder.toString()).apply();
            PreferencesScreen.this.crop_current_mode.setText(PreferencesScreen.this.getResources().getString(2131820718));
            Toast.makeText(PreferencesScreen.this.getApplicationContext(), "Everything Reset to Default", 0).show();
          }
        });
    this.plus.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            PreferencesScreen preferencesScreen = PreferencesScreen.this;
            preferencesScreen.current_duration = Integer.valueOf(preferencesScreen.current_duration.intValue() + 1);
            TextView textView = PreferencesScreen.this.captime;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(PreferencesScreen.this.current_duration);
            stringBuilder.append(" seconds");
            textView.setText(stringBuilder.toString());
            PreferencesScreen.this.settings.edit().putInt("DEFCAPTIME", PreferencesScreen.this.current_duration.intValue()).apply();
          }
        });
    this.minus.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (PreferencesScreen.this.current_duration.intValue() > PreferencesScreen.DEFAULT_CAP_DURATION.intValue()) {
              PreferencesScreen preferencesScreen = PreferencesScreen.this;
              preferencesScreen.current_duration = Integer.valueOf(preferencesScreen.current_duration.intValue() - 1);
              TextView textView = PreferencesScreen.this.captime;
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append(PreferencesScreen.this.current_duration);
              stringBuilder1.append(" seconds");
              textView.setText(stringBuilder1.toString());
              PreferencesScreen.this.settings.edit().putInt("DEFCAPTIME", PreferencesScreen.this.current_duration.intValue()).apply();
              return;
            } 
            Context context = PreferencesScreen.this.getApplicationContext();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("minimum time interval is ");
            stringBuilder.append(PreferencesScreen.DEFAULT_CAP_DURATION);
            stringBuilder.append(" seconds");
            Toast.makeText(context, stringBuilder.toString(), 0).show();
          }
        });
  }
  
  protected void restorePreferences() {
    this.current_duration = Integer.valueOf(this.settings.getInt("DEFCAPTIME", DEFAULT_CAP_DURATION.intValue()));
    ((CheckBox)this.checkBoxes.get(find_element_num(this.checkBoxNames, "AUTOSSDELPDF"))).setChecked(this.settings.getBoolean("AUTOSSDELPDF", false));
    TextView textView = this.captime;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.current_duration);
    stringBuilder.append(" seconds");
    textView.setText(stringBuilder.toString());
    SharedPreferences sharedPreferences = this.settings;
    stringBuilder = new StringBuilder();
    stringBuilder.append("0_0");
    stringBuilder.append(this.current_screen_height);
    stringBuilder.append("_");
    stringBuilder.append(this.current_screen_width);
    String[] arrayOfString = sharedPreferences.getString("SCREEN_CROP", stringBuilder.toString()).split("_");
    try {
      if (Integer.parseInt(arrayOfString[3]) - Integer.parseInt(arrayOfString[1]) < this.current_screen_height || Integer.parseInt(arrayOfString[2]) - Integer.parseInt(arrayOfString[0]) < this.current_screen_width) {
        this.crop_current_mode.setText(getResources().getString(2131820717));
        return;
      } 
      this.crop_current_mode.setText(getResources().getString(2131820718));
      return;
    } catch (Exception exception) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("restorePreferences: ");
      stringBuilder2.append(arrayOfString);
      Log.d("PreferencesScreen", stringBuilder2.toString());
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("restorePreferences: ");
      stringBuilder1.append(exception.getMessage());
      Log.d("PreferencesScreen", stringBuilder1.toString());
      return;
    } 
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\PreferencesScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */