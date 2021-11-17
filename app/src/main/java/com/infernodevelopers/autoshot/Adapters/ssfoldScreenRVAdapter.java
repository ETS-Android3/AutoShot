package com.infernodevelopers.autoshot.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ssfoldScreenRVAdapter extends RecyclerView.Adapter<ssfoldScreenRVHolder> implements ssfoldScreenRVDragnDrop.ItemTouchHelperAdapter {
  public static final int RENAME_MODE_RANDOM = 101;
  
  public static final int RENAME_MODE_SWAP = 100;
  
  private static final String TAG = "ssfoldScreenRVAdapter";
  
  Context context;
  
  List<ssfoldScreenRVData> list = Collections.emptyList();
  
  SharedPreferences ssfoldscreen_RV_pref;
  
  public ssfoldScreenRVAdapter(List<ssfoldScreenRVData> paramList, Context paramContext) {
    this.context = paramContext;
    this.list = paramList;
    this.ssfoldscreen_RV_pref = paramContext.getSharedPreferences("SSFOLDSCREEN_RV", 0);
  }
  
  public int getItemCount() {
    return this.list.size();
  }
  
  public void onAttachedToRecyclerView(RecyclerView paramRecyclerView) {
    super.onAttachedToRecyclerView(paramRecyclerView);
  }
  
  public void onBindViewHolder(ssfoldScreenRVHolder paramssfoldScreenRVHolder, final int position) {
    Glide.with(this.context).load(new File(((ssfoldScreenRVData)this.list.get(position)).image_path)).into(paramssfoldScreenRVHolder.img_to_disp);
    paramssfoldScreenRVHolder.img_num.setText(Integer.toString(position + 1));
    paramssfoldScreenRVHolder.foldItemBG.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Uri uri = Uri.fromFile(new File(((ssfoldScreenRVData)ssfoldScreenRVAdapter.this.list.get(position)).image_path));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uri, "image/*");
            intent.setFlags(1342177281);
            if (intent.resolveActivity(ssfoldScreenRVAdapter.this.context.getPackageManager()) != null)
              ssfoldScreenRVAdapter.this.context.startActivity(intent); 
          }
        });
    if (((ssfoldScreenRVData)this.list.get(position)).image_path.equals("delete_list_process")) {
      paramssfoldScreenRVHolder.img_item.setVisibility(8);
    } else {
      paramssfoldScreenRVHolder.img_item.setVisibility(0);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("onBindViewHolder: ");
    List<ssfoldScreenRVData> list2 = this.list;
    stringBuilder.append(((ssfoldScreenRVData)list2.get(list2.size() - 1)).image_path);
    Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append("onBindViewHolder: ");
    stringBuilder.append(this.list.size());
    Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
    List<ssfoldScreenRVData> list1 = this.list;
    if (((ssfoldScreenRVData)list1.get(list1.size() - 1)).image_path.equals("delete_list_process")) {
      Log.d("ssfoldScreenRVAdapter", "onBindViewHolder: delete_list_process");
      paramssfoldScreenRVHolder.select_item.setChecked(((ssfoldScreenRVData)this.list.get(position)).delete);
    } else {
      paramssfoldScreenRVHolder.select_item.setChecked(((ssfoldScreenRVData)this.list.get(position)).checked);
    } 
    paramssfoldScreenRVHolder.select_item.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ((ssfoldScreenRVData)ssfoldScreenRVAdapter.this.list.get(position)).delete ^= 0x1;
            ((ssfoldScreenRVData)ssfoldScreenRVAdapter.this.list.get(position)).checked ^= 0x1;
          }
        });
  }
  
  public ssfoldScreenRVHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    Context context = paramViewGroup.getContext();
    this.context = context;
    return new ssfoldScreenRVHolder(LayoutInflater.from(context).inflate(2131492988, paramViewGroup, false));
  }
  
  public void onRowClear(ssfoldScreenRVHolder paramssfoldScreenRVHolder) {}
  
  public void onRowMoved(int paramInt1, int paramInt2) {
    if (paramInt1 < paramInt2) {
      for (int j = paramInt1; j < paramInt2; j++) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onRowMoved: ");
        stringBuilder.append(j);
        stringBuilder.append("->");
        stringBuilder.append(j + 1);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
        rename_image(j, j + 1, 100, 0);
      } 
    } else {
      for (int j = paramInt1; j > paramInt2; j--) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onRowMoved: ");
        stringBuilder.append(j);
        stringBuilder.append("->");
        stringBuilder.append(j - 1);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
        rename_image(j, j - 1, 100, 0);
      } 
    } 
    String str2 = ((ssfoldScreenRVData)this.list.get(paramInt1)).image_path;
    String str1 = str2.split("/")[(str2.split("/")).length - 1];
    str1 = str2.substring(0, str2.length() - str1.length());
    str2 = str2.split("/")[(str2.split("/")).length - 2];
    int i = (new Random()).nextInt(800 - 100 + 1) + 100;
    if (paramInt1 < paramInt2) {
      for (int j = paramInt1; j <= paramInt2; j++) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onRowMoved:2 ");
        stringBuilder.append(j);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
        rename_image(j, 0, 101, i);
      } 
    } else {
      for (int j = paramInt1; j >= paramInt2; j--) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onRowMoved:2 ");
        stringBuilder.append(j);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
        rename_image(j, 0, 101, i);
      } 
    } 
    notifyItemMoved(paramInt1, paramInt2);
    refreshFoldDataSet(ssfoldScreenRVDataList(str2, str1));
  }
  
  public void onRowSelected(ssfoldScreenRVHolder paramssfoldScreenRVHolder) {}
  
  public void refreshFoldDataSet(List<ssfoldScreenRVData> paramList) {
    this.list.clear();
    this.list.addAll(paramList);
    notifyDataSetChanged();
  }
  
  public void rename_image(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    String str1 = ((ssfoldScreenRVData)this.list.get(paramInt1)).image_path;
    String str7 = str1.split("/")[(str1.split("/")).length - 1];
    String str2 = str7.split("_")[(str7.split("_")).length - 1];
    String str3 = str1.substring(0, str1.length() - str7.length());
    String str6 = ((ssfoldScreenRVData)this.list.get(paramInt2)).image_path;
    String str5 = str6.split("/")[(str6.split("/")).length - 1];
    String str4 = str5.split("_")[(str5.split("_")).length - 1];
    str4 = str1.split("/")[(str1.split("/")).length - 2];
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("rename_image: ");
    stringBuilder2.append(str4);
    Log.d("ssfoldScreenRVAdapter", stringBuilder2.toString());
    if (paramInt3 == 100) {
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str3);
      stringBuilder2.append(str4);
      stringBuilder2.append(".jpg");
      String str = stringBuilder2.toString();
      if ((new File(str1)).renameTo(new File(str))) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onClick: rename done ");
        stringBuilder.append(str1);
        stringBuilder.append("-->");
        stringBuilder.append(str);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rename_image: fail");
        stringBuilder.append(str1);
        stringBuilder.append("-->");
        stringBuilder.append(str);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
      } 
      StringBuilder stringBuilder3 = new StringBuilder();
      stringBuilder3.append(str3);
      stringBuilder3.append(str7);
      str1 = stringBuilder3.toString();
      if ((new File(str6)).renameTo(new File(str1))) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onClick: rename done ");
        stringBuilder.append(str6);
        stringBuilder.append("-->");
        stringBuilder.append(str1);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rename_image: fail");
        stringBuilder.append(str6);
        stringBuilder.append("-->");
        stringBuilder.append(str1);
        Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
      } 
      StringBuilder stringBuilder4 = new StringBuilder();
      stringBuilder4.append(str3);
      stringBuilder4.append(str5);
      str5 = stringBuilder4.toString();
      if ((new File(str)).renameTo(new File(str5))) {
        stringBuilder4 = new StringBuilder();
        stringBuilder4.append("onClick: rename done ");
        stringBuilder4.append(str);
        stringBuilder4.append("-->");
        stringBuilder4.append(str5);
        Log.d("ssfoldScreenRVAdapter", stringBuilder4.toString());
      } else {
        stringBuilder4 = new StringBuilder();
        stringBuilder4.append("rename_image: fail");
        stringBuilder4.append(str);
        stringBuilder4.append("-->");
        stringBuilder4.append(str5);
        Log.d("ssfoldScreenRVAdapter", stringBuilder4.toString());
      } 
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(str3);
    stringBuilder1.append(str4);
    stringBuilder1.append(paramInt4);
    stringBuilder1.append("_");
    stringBuilder1.append(str2);
    str2 = stringBuilder1.toString();
    if (paramInt3 == 101 && (new File(str1)).renameTo(new File(str2))) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("rename_image: ");
      stringBuilder.append(str1);
      stringBuilder.append("-->");
      stringBuilder.append(str2);
      Log.d("ssfoldScreenRVAdapter", stringBuilder.toString());
      return;
    } 
  }
  
  public void setCheckboxModeDeselectAll() {
    Iterator<ssfoldScreenRVData> iterator = this.list.iterator();
    while (iterator.hasNext())
      ((ssfoldScreenRVData)iterator.next()).checked = false; 
    notifyDataSetChanged();
  }
  
  public void setCheckboxModeSelectAll() {
    Iterator<ssfoldScreenRVData> iterator = this.list.iterator();
    while (iterator.hasNext())
      ((ssfoldScreenRVData)iterator.next()).checked = true; 
    notifyDataSetChanged();
  }
  
  public List<ssfoldScreenRVData> ssfoldScreenRVDataList(String paramString1, String paramString2) {
    ArrayList<ssfoldScreenRVData> arrayList = new ArrayList();
    File[] arrayOfFile = (new File(paramString2)).listFiles();
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
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\ssfoldScreenRVAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */