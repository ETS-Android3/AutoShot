package com.infernodevelopers.autoshot.Adapters;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class ssfoldScreenRVItemSpacing extends RecyclerView.ItemDecoration {
  private boolean includeEdge;
  
  private int spacing;
  
  private int spanCount;
  
  public ssfoldScreenRVItemSpacing(int paramInt1, int paramInt2, boolean paramBoolean) {
    this.spanCount = paramInt1;
    this.spacing = paramInt2;
    this.includeEdge = paramBoolean;
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    int i = paramRecyclerView.getChildAdapterPosition(paramView);
    int k = this.spanCount;
    int j = i % k;
    if (this.includeEdge) {
      int m = this.spacing;
      paramRect.left = m - j * m / k;
      paramRect.right = (j + 1) * this.spacing / this.spanCount;
      if (i < this.spanCount)
        paramRect.top = this.spacing; 
      paramRect.bottom = this.spacing;
      return;
    } 
    paramRect.left = this.spacing * j / k;
    k = this.spacing;
    paramRect.right = k - (j + 1) * k / this.spanCount;
    if (i >= this.spanCount)
      paramRect.top = this.spacing; 
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\ssfoldScreenRVItemSpacing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */