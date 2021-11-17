package com.infernodevelopers.autoshot.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProcessPreviewScreenAdapter extends RecyclerView.Adapter<ProcessPreviewScreenHolder> {
  Context context;
  
  List<ssfoldScreenRVData> delete_list = new ArrayList<ssfoldScreenRVData>();
  
  ssfoldScreenRVAdapter delete_list_adapter;
  
  List<String> delete_list_index = new ArrayList<String>();
  
  RecyclerView delete_list_rv;
  
  int selected_pos = 0;
  
  List<ssfoldScreenRVData> sort_list = new ArrayList<ssfoldScreenRVData>();
  
  public ProcessPreviewScreenAdapter(List<ssfoldScreenRVData> paramList1, List<ssfoldScreenRVData> paramList2, List<String> paramList, Context paramContext, RecyclerView paramRecyclerView) {
    this.sort_list = paramList1;
    this.delete_list = paramList2;
    this.delete_list_index = paramList;
    this.context = paramContext;
    this.delete_list_rv = paramRecyclerView;
  }
  
  public int getItemCount() {
    return this.sort_list.size();
  }
  
  public void onBindViewHolder(ProcessPreviewScreenHolder paramProcessPreviewScreenHolder, final int position) {
    Glide.with(this.context).load(new File(((ssfoldScreenRVData)this.sort_list.get(position)).image_path)).into(paramProcessPreviewScreenHolder.img_item);
    paramProcessPreviewScreenHolder.img_item.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ProcessPreviewScreenAdapter.this.selected_pos = position;
            ArrayList<ssfoldScreenRVData> arrayList = new ArrayList();
            int i = Integer.parseInt(((String)ProcessPreviewScreenAdapter.this.delete_list_index.get(position)).split(":")[0]);
            int j = Integer.parseInt(((String)ProcessPreviewScreenAdapter.this.delete_list_index.get(position)).split(":")[1]);
            while (i < j) {
              arrayList.add(ProcessPreviewScreenAdapter.this.delete_list.get(i));
              i++;
            } 
            arrayList.add(new ssfoldScreenRVData("delete_list_process"));
            ProcessPreviewScreenAdapter processPreviewScreenAdapter = ProcessPreviewScreenAdapter.this;
            processPreviewScreenAdapter.delete_list_adapter = new ssfoldScreenRVAdapter(arrayList, processPreviewScreenAdapter.context);
            ProcessPreviewScreenAdapter.this.delete_list_rv.setAdapter(ProcessPreviewScreenAdapter.this.delete_list_adapter);
            ProcessPreviewScreenAdapter.this.delete_list_rv.setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager(ProcessPreviewScreenAdapter.this.context, 2));
            ProcessPreviewScreenAdapter.this.delete_list_adapter.setCheckboxModeSelectAll();
            ProcessPreviewScreenAdapter.this.notifyDataSetChanged();
          }
        });
    if (this.selected_pos == position) {
      paramProcessPreviewScreenHolder.img_sel.setVisibility(0);
      return;
    } 
    paramProcessPreviewScreenHolder.img_sel.setVisibility(4);
  }
  
  public ProcessPreviewScreenHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    Context context = paramViewGroup.getContext();
    this.context = context;
    return new ProcessPreviewScreenHolder(LayoutInflater.from(context).inflate(2131492982, paramViewGroup, false));
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\ProcessPreviewScreenAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */