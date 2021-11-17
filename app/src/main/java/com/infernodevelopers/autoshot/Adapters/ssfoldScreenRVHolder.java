package com.infernodevelopers.autoshot.Adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ssfoldScreenRVHolder extends RecyclerView.ViewHolder {
  ImageView foldItemBG;
  
  ConstraintLayout img_item;
  
  TextView img_num;
  
  ImageView img_to_disp;
  
  CheckBox select_item;
  
  ssfoldScreenRVHolder(View paramView) {
    super(paramView);
    this.img_to_disp = (ImageView)paramView.findViewById(2131296768);
    this.foldItemBG = (ImageView)paramView.findViewById(2131296766);
    this.img_num = (TextView)paramView.findViewById(2131296769);
    this.select_item = (CheckBox)paramView.findViewById(2131296767);
    this.img_item = (ConstraintLayout)paramView.findViewById(2131296765);
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\ssfoldScreenRVHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */