package com.infernodevelopers.autoshot.Adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdView;

public class filesCapRVHolder extends RecyclerView.ViewHolder {
  AdView adView;
  
  ImageView adViewBGIV;
  
  ConstraintLayout capItemBG;
  
  ImageButton editFileNameIB;
  
  TextView fileSizeTV;
  
  ImageView fileTypeIV;
  
  TextView filenameTV;
  
  ConstraintLayout filesItemBG;
  
  TextView no_of_ssTV;
  
  CheckBox selectFileCB;
  
  ImageButton shareIB;
  
  TextView timeIntervalTV;
  
  View view;
  
  filesCapRVHolder(View paramView) {
    super(paramView);
    this.filesItemBG = (ConstraintLayout)paramView.findViewById(2131296489);
    this.filenameTV = (TextView)paramView.findViewById(2131296446);
    this.fileSizeTV = (TextView)paramView.findViewById(2131296445);
    this.editFileNameIB = (ImageButton)paramView.findViewById(2131296441);
    this.shareIB = (ImageButton)paramView.findViewById(2131296444);
    this.fileTypeIV = (ImageView)paramView.findViewById(2131296442);
    this.selectFileCB = (CheckBox)paramView.findViewById(2131296443);
    this.adViewBGIV = (ImageView)paramView.findViewById(2131296448);
    this.adView = (AdView)paramView.findViewById(2131296447);
    this.view = paramView;
  }
  
  filesCapRVHolder(View paramView, int paramInt) {
    super(paramView);
    this.capItemBG = (ConstraintLayout)paramView.findViewById(2131296482);
    this.filenameTV = (TextView)paramView.findViewById(2131296364);
    this.fileSizeTV = (TextView)paramView.findViewById(2131296363);
    this.no_of_ssTV = (TextView)paramView.findViewById(2131296361);
    this.timeIntervalTV = (TextView)paramView.findViewById(2131296358);
    this.editFileNameIB = (ImageButton)paramView.findViewById(2131296359);
    this.fileTypeIV = (ImageView)paramView.findViewById(2131296360);
    this.selectFileCB = (CheckBox)paramView.findViewById(2131296362);
    this.adViewBGIV = (ImageView)paramView.findViewById(2131296366);
    this.adView = (AdView)paramView.findViewById(2131296365);
    this.view = paramView;
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\filesCapRVHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */