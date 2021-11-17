package com.infernodevelopers.autoshot.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class ColorBall {
  static int count = 0;
  
  Bitmap bitmap;
  
  int id;
  
  Context mContext;
  
  Point point;
  
  public ColorBall(Context paramContext, int paramInt1, Point paramPoint, int paramInt2) {
    this.id = paramInt2;
    this.bitmap = BitmapFactory.decodeResource(paramContext.getResources(), paramInt1);
    this.mContext = paramContext;
    this.point = paramPoint;
  }
  
  public void addX(int paramInt) {
    Point point = this.point;
    point.x += paramInt;
  }
  
  public void addY(int paramInt) {
    Point point = this.point;
    point.y += paramInt;
  }
  
  public Bitmap getBitmap() {
    return this.bitmap;
  }
  
  public int getHeightOfBall() {
    return this.bitmap.getHeight();
  }
  
  public int getID() {
    return this.id;
  }
  
  public int getWidthOfBall() {
    return this.bitmap.getWidth();
  }
  
  public int getX() {
    return this.point.x;
  }
  
  public int getY() {
    return this.point.y;
  }
  
  public void setX(int paramInt) {
    this.point.x = paramInt;
  }
  
  public void setY(int paramInt) {
    this.point.y = paramInt;
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Views\ColorBall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */