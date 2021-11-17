package com.infernodevelopers.autoshot.Adapters;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ssfoldScreenRVDragnDrop extends ItemTouchHelper.Callback {
  private final ItemTouchHelperAdapter adapter;
  
  public ssfoldScreenRVDragnDrop(ItemTouchHelperAdapter paramItemTouchHelperAdapter) {
    this.adapter = paramItemTouchHelperAdapter;
  }
  
  public void clearView(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder) {
    super.clearView(paramRecyclerView, paramViewHolder);
    if (paramViewHolder instanceof ssfoldScreenRVHolder) {
      ssfoldScreenRVHolder ssfoldScreenRVHolder = (ssfoldScreenRVHolder)paramViewHolder;
      this.adapter.onRowClear(ssfoldScreenRVHolder);
    } 
  }
  
  public int getMovementFlags(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder) {
    return makeMovementFlags(51, 0);
  }
  
  public boolean isItemViewSwipeEnabled() {
    return false;
  }
  
  public boolean isLongPressDragEnabled() {
    return true;
  }
  
  public boolean onMove(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder1, RecyclerView.ViewHolder paramViewHolder2) {
    this.adapter.onRowMoved(paramViewHolder1.getAdapterPosition(), paramViewHolder2.getAdapterPosition());
    return true;
  }
  
  public void onSelectedChanged(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if (paramInt != 0 && paramViewHolder instanceof ssfoldScreenRVHolder) {
      ssfoldScreenRVHolder ssfoldScreenRVHolder = (ssfoldScreenRVHolder)paramViewHolder;
      this.adapter.onRowSelected(ssfoldScreenRVHolder);
    } 
    super.onSelectedChanged(paramViewHolder, paramInt);
  }
  
  public void onSwiped(RecyclerView.ViewHolder paramViewHolder, int paramInt) {}
  
  public static interface ItemTouchHelperAdapter {
    void onRowClear(ssfoldScreenRVHolder param1ssfoldScreenRVHolder);
    
    void onRowMoved(int param1Int1, int param1Int2);
    
    void onRowSelected(ssfoldScreenRVHolder param1ssfoldScreenRVHolder);
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\ssfoldScreenRVDragnDrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */