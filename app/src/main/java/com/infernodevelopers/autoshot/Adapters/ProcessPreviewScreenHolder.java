package com.infernodevelopers.autoshot.Adapters;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

public class ProcessPreviewScreenHolder extends RecyclerView.ViewHolder {
  ImageView img_item;
  
  ImageView img_sel;
  
  ProcessPreviewScreenHolder(View paramView) {
    super(paramView);
    this.img_item = (ImageView)paramView.findViewById(2131296686);
    this.img_sel = (ImageView)paramView.findViewById(2131296685);
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\ProcessPreviewScreenHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */