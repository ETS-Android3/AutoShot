package com.infernodevelopers.autoshot.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.infernodevelopers.autoshot.Adapters.ProcessPreviewScreenAdapter;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVAdapter;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVData;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {
  public static final String DELETE_LIST_IDENTIFIER = "delete_list_process";
  
  public static final String INDEX_SPLITTER = ":";
  
  private static final String TAG = "ProcessUtils";
  
  TextView cmp_file_process;
  
  int cmp_size = 300;
  
  Context context;
  
  List<ssfoldScreenRVData> delete_list = new ArrayList<ssfoldScreenRVData>();
  
  ssfoldScreenRVAdapter delete_list_adapter;
  
  List<String> delete_list_index = new ArrayList<String>();
  
  RecyclerView delete_list_rv;
  
  TextView file_to_process;
  
  List<Bitmap> image_bitmap_list = new ArrayList<Bitmap>();
  
  int img_inner_loop;
  
  int img_outer_loop;
  
  ImageView preview_cmp_img;
  
  Handler preview_handle;
  
  ConstraintLayout preview_loading_ui;
  
  ImageView preview_original_img;
  
  float similar_threshold_percent = 75.0F;
  
  List<ssfoldScreenRVData> sort_list = new ArrayList<ssfoldScreenRVData>();
  
  ProcessPreviewScreenAdapter sort_list_adapter;
  
  RecyclerView sort_list_rv;
  
  List<ssfoldScreenRVData> sorted_list = new ArrayList<ssfoldScreenRVData>();
  
  public ProcessUtils(List<ssfoldScreenRVData> paramList, Context paramContext, RecyclerView paramRecyclerView1, RecyclerView paramRecyclerView2, ConstraintLayout paramConstraintLayout, Handler paramHandler, TextView paramTextView1, TextView paramTextView2, ImageView paramImageView1, ImageView paramImageView2) {
    this.context = paramContext;
    this.sort_list = paramList;
    this.delete_list_rv = paramRecyclerView1;
    this.sort_list_rv = paramRecyclerView2;
    this.preview_loading_ui = paramConstraintLayout;
    this.preview_handle = paramHandler;
    this.file_to_process = paramTextView1;
    this.cmp_file_process = paramTextView2;
    this.preview_original_img = paramImageView1;
    this.preview_cmp_img = paramImageView2;
  }
  
  public List<ssfoldScreenRVData> Process() {
    int m = this.sort_list.size();
    ArrayList<Integer> arrayList = new ArrayList();
    int j = 0;
    this.preview_handle.post(new Runnable() {
          public void run() {
            ProcessUtils.this.file_to_process.setText("Initialising...");
          }
        });
    int i;
    for (i = 0; i < m; i++) {
      List<Bitmap> list = this.image_bitmap_list;
      Bitmap bitmap = BitmapFactory.decodeFile(((ssfoldScreenRVData)this.sort_list.get(i)).image_path);
      int n = this.cmp_size;
      list.add(Bitmap.createScaledBitmap(bitmap, n, n, true));
    } 
    this.preview_handle.post(new Runnable() {
          public void run() {
            ProcessUtils.this.file_to_process.setText((new File(((ssfoldScreenRVData)ProcessUtils.this.sort_list.get(0)).image_path)).getName());
          }
        });
    int k = 0;
    i = m;
    while (k < i) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Process: ");
      stringBuilder2.append(k);
      Log.d("ProcessUtils", stringBuilder2.toString());
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Process: ");
      stringBuilder2.append(i);
      stringBuilder2.append(" img:");
      stringBuilder2.append(0);
      stringBuilder2.append(" p:");
      stringBuilder2.append(((ssfoldScreenRVData)this.sort_list.get(0)).image_path);
      Log.d("ProcessUtils", stringBuilder2.toString());
      final String i1_path = ((ssfoldScreenRVData)this.sort_list.get(0)).image_path;
      this.img_outer_loop = k;
      this.preview_handle.post(new Runnable() {
            public void run() {
              ProcessUtils.this.file_to_process.setText((new File(i1_path)).getName());
              ProcessUtils.this.preview_original_img.setImageBitmap(ProcessUtils.this.image_bitmap_list.get(ProcessUtils.this.img_outer_loop));
            }
          });
      StringBuilder stringBuilder3 = new StringBuilder();
      stringBuilder3.append(j);
      stringBuilder3.append(":");
      String str2 = stringBuilder3.toString();
      k = 1;
      while (k < i) {
        final String i2_path = ((ssfoldScreenRVData)this.sort_list.get(k)).image_path;
        float f = compare_images(0, k);
        StringBuilder stringBuilder5 = new StringBuilder();
        stringBuilder5.append("Process: ");
        stringBuilder5.append(k);
        Log.d("ProcessUtils", stringBuilder5.toString());
        stringBuilder5 = new StringBuilder();
        stringBuilder5.append("Process: sim:");
        stringBuilder5.append(f);
        stringBuilder5.append("% p:");
        stringBuilder5.append(str);
        Log.d("ProcessUtils", stringBuilder5.toString());
        m = j;
        if (f >= this.similar_threshold_percent) {
          Log.d("ProcessUtils", "Process: del");
          this.delete_list.add(new ssfoldScreenRVData(str));
          arrayList.add(Integer.valueOf(k));
          m = j + 1;
        } 
        this.img_inner_loop = k;
        stringBuilder5 = new StringBuilder();
        stringBuilder5.append("Process: img_inner_loop:");
        stringBuilder5.append(this.img_inner_loop);
        stringBuilder5.append(" img2:");
        stringBuilder5.append(k);
        stringBuilder5.append(" sort_list:");
        stringBuilder5.append(i);
        Log.d("ProcessUtils", stringBuilder5.toString());
        this.preview_handle.post(new Runnable() {
              public void run() {
                try {
                  ProcessUtils.this.cmp_file_process.setText((new File(i2_path)).getName());
                  ProcessUtils.this.preview_cmp_img.setImageBitmap(ProcessUtils.this.image_bitmap_list.get(ProcessUtils.this.img_inner_loop));
                  return;
                } catch (Exception exception) {
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("run: ");
                  stringBuilder.append(exception.getMessage());
                  Log.d("ProcessUtils", stringBuilder.toString());
                  return;
                } 
              }
            });
        k++;
        j = m;
      } 
      StringBuilder stringBuilder4 = new StringBuilder();
      stringBuilder4.append(str2);
      stringBuilder4.append(j);
      str2 = stringBuilder4.toString();
      this.delete_list_index.add(str2);
      for (i = 0; i < arrayList.size(); i++) {
        arrayList.set(i, Integer.valueOf(((Integer)arrayList.get(i)).intValue() - i));
        this.sort_list.remove(((Integer)arrayList.get(i)).intValue());
        this.image_bitmap_list.remove(((Integer)arrayList.get(i)).intValue());
      } 
      this.sorted_list.add(new ssfoldScreenRVData(str1));
      this.sort_list.remove(0);
      this.image_bitmap_list.remove(0);
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Process: ");
      stringBuilder1.append(arrayList);
      Log.d("ProcessUtils", stringBuilder1.toString());
      arrayList.clear();
      i = this.sort_list.size();
      k = 0 + 1;
    } 
    if (this.sort_list.size() == 1) {
      this.sorted_list.add(this.sort_list.get(0));
      List<String> list = this.delete_list_index;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(j + 1);
      stringBuilder1.append(":");
      stringBuilder1.append(j + 1);
      list.add(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Process:s ");
    stringBuilder.append(this.sort_list);
    Log.d("ProcessUtils", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("Process:d ");
    stringBuilder.append(this.delete_list);
    Log.d("ProcessUtils", stringBuilder.toString());
    this.context.getSharedPreferences("SSFOLDSCREEN_RV", 0).edit().putBoolean("PROCESS_ACTIVE", false).apply();
    this.preview_handle.post(new Runnable() {
          public void run() {
            ProcessUtils.this.file_to_process.setText("");
            ProcessUtils.this.preview_loading_ui.setVisibility(8);
            ArrayList<ssfoldScreenRVData> arrayList = new ArrayList();
            int i = Integer.parseInt(((String)ProcessUtils.this.delete_list_index.get(0)).split(":")[0]);
            int j = Integer.parseInt(((String)ProcessUtils.this.delete_list_index.get(0)).split(":")[1]);
            while (i < j) {
              arrayList.add(ProcessUtils.this.delete_list.get(i));
              i++;
            } 
            arrayList.add(new ssfoldScreenRVData("delete_list_process"));
            ProcessUtils processUtils2 = ProcessUtils.this;
            processUtils2.delete_list_adapter = new ssfoldScreenRVAdapter(arrayList, processUtils2.context);
            ProcessUtils.this.delete_list_rv.setAdapter((RecyclerView.Adapter)ProcessUtils.this.delete_list_adapter);
            ProcessUtils.this.delete_list_rv.setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager(ProcessUtils.this.context, 2));
            ProcessUtils.this.delete_list_adapter.setCheckboxModeSelectAll();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("run: ");
            stringBuilder.append(ProcessUtils.this.delete_list_index);
            Log.d("ProcessUtils", stringBuilder.toString());
            ProcessUtils processUtils1 = ProcessUtils.this;
            processUtils1.sort_list_adapter = new ProcessPreviewScreenAdapter(processUtils1.sorted_list, ProcessUtils.this.delete_list, ProcessUtils.this.delete_list_index, ProcessUtils.this.context, ProcessUtils.this.delete_list_rv);
            ProcessUtils.this.sort_list_rv.setAdapter((RecyclerView.Adapter)ProcessUtils.this.sort_list_adapter);
            ProcessUtils.this.sort_list_rv.setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager(ProcessUtils.this.context, 1, 0, false));
          }
        });
    return this.delete_list;
  }
  
  public float compare_images(int paramInt1, int paramInt2) {
    int j = 0;
    int i = 0;
    Bitmap bitmap1 = this.image_bitmap_list.get(paramInt1);
    Bitmap bitmap2 = this.image_bitmap_list.get(paramInt2);
    paramInt1 = 0;
    paramInt2 = i;
    while (paramInt1 < this.cmp_size) {
      i = 0;
      while (i < this.cmp_size) {
        int m = bitmap1.getPixel(paramInt1, i);
        int n = bitmap2.getPixel(paramInt1, i);
        int k = j;
        if (Color.red(m) == Color.red(n)) {
          k = j;
          if (Color.green(m) == Color.green(n)) {
            k = j;
            if (Color.blue(m) == Color.blue(n))
              k = j + 1; 
          } 
        } 
        paramInt2++;
        i++;
        j = k;
      } 
      paramInt1++;
    } 
    return (float)(j / paramInt2 * 1.0D) * 100.0F;
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Utils\ProcessUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */