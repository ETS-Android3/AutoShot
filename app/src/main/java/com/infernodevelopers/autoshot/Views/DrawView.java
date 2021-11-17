package com.infernodevelopers.autoshot.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class DrawView extends View {
  private int balID = 0;
  
  Canvas canvas;
  
  private ArrayList<ColorBall> colorballs;
  
  int groupId = 2;
  
  Paint paint;
  
  public Point point1;
  
  public Point point2;
  
  public Point point3;
  
  public Point point4;
  
  Point startMovePoint;
  
  public DrawView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public DrawView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public DrawView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void init(Context paramContext) {
    this.paint = new Paint();
    setFocusable(true);
    this.canvas = new Canvas();
    Point point = new Point();
    this.point1 = point;
    point.x = 50;
    this.point1.y = 20;
    point = new Point();
    this.point2 = point;
    point.x = 150;
    this.point2.y = 20;
    point = new Point();
    this.point3 = point;
    point.x = 150;
    this.point3.y = 120;
    point = new Point();
    this.point4 = point;
    point.x = 50;
    this.point4.y = 120;
    ArrayList<ColorBall> arrayList = new ArrayList();
    this.colorballs = arrayList;
    arrayList.add(0, new ColorBall(paramContext, 2131165356, this.point1, 0));
    this.colorballs.add(1, new ColorBall(paramContext, 2131165356, this.point2, 1));
    this.colorballs.add(2, new ColorBall(paramContext, 2131165356, this.point3, 2));
    this.colorballs.add(3, new ColorBall(paramContext, 2131165356, this.point4, 3));
  }
  
  protected void onDraw(Canvas paramCanvas) {
    this.paint.setAntiAlias(true);
    this.paint.setDither(true);
    this.paint.setColor(Color.parseColor("#55000000"));
    this.paint.setStyle(Paint.Style.FILL);
    this.paint.setStrokeJoin(Paint.Join.ROUND);
    this.paint.setStrokeWidth(5.0F);
    paramCanvas.drawPaint(this.paint);
    this.paint.setColor(Color.parseColor("#55FFFFFF"));
    if (this.groupId == 1) {
      paramCanvas.drawRect((this.point1.x + ((ColorBall)this.colorballs.get(0)).getWidthOfBall() / 2), (this.point3.y + ((ColorBall)this.colorballs.get(2)).getWidthOfBall() / 2), (this.point3.x + ((ColorBall)this.colorballs.get(2)).getWidthOfBall() / 2), (this.point1.y + ((ColorBall)this.colorballs.get(0)).getWidthOfBall() / 2), this.paint);
    } else {
      paramCanvas.drawRect((this.point2.x + ((ColorBall)this.colorballs.get(1)).getWidthOfBall() / 2), (this.point4.y + ((ColorBall)this.colorballs.get(3)).getWidthOfBall() / 2), (this.point4.x + ((ColorBall)this.colorballs.get(3)).getWidthOfBall() / 2), (this.point2.y + ((ColorBall)this.colorballs.get(1)).getWidthOfBall() / 2), this.paint);
    } 
    new BitmapDrawable();
    for (ColorBall colorBall : this.colorballs)
      paramCanvas.drawBitmap(colorBall.getBitmap(), colorBall.getX(), colorBall.getY(), new Paint()); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int k = paramMotionEvent.getAction();
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    if (k != 0) {
      if (k == 2) {
        k = this.balID;
        if (k > -1) {
          ((ColorBall)this.colorballs.get(k)).setX(i);
          ((ColorBall)this.colorballs.get(this.balID)).setY(j);
          this.paint.setColor(-16711681);
          if (this.groupId == 1) {
            ((ColorBall)this.colorballs.get(1)).setX(((ColorBall)this.colorballs.get(0)).getX());
            ((ColorBall)this.colorballs.get(1)).setY(((ColorBall)this.colorballs.get(2)).getY());
            ((ColorBall)this.colorballs.get(3)).setX(((ColorBall)this.colorballs.get(2)).getX());
            ((ColorBall)this.colorballs.get(3)).setY(((ColorBall)this.colorballs.get(0)).getY());
            this.canvas.drawRect(this.point1.x, this.point3.y, this.point3.x, this.point1.y, this.paint);
          } else {
            ((ColorBall)this.colorballs.get(0)).setX(((ColorBall)this.colorballs.get(1)).getX());
            ((ColorBall)this.colorballs.get(0)).setY(((ColorBall)this.colorballs.get(3)).getY());
            ((ColorBall)this.colorballs.get(2)).setX(((ColorBall)this.colorballs.get(3)).getX());
            ((ColorBall)this.colorballs.get(2)).setY(((ColorBall)this.colorballs.get(1)).getY());
            this.canvas.drawRect(this.point2.x, this.point4.y, this.point4.x, this.point2.y, this.paint);
          } 
          invalidate();
        } else if (this.startMovePoint != null) {
          this.paint.setColor(-16711681);
          k = i - this.startMovePoint.x;
          int m = j - this.startMovePoint.y;
          this.startMovePoint.x = i;
          this.startMovePoint.y = j;
          ((ColorBall)this.colorballs.get(0)).addX(k);
          ((ColorBall)this.colorballs.get(1)).addX(k);
          ((ColorBall)this.colorballs.get(2)).addX(k);
          ((ColorBall)this.colorballs.get(3)).addX(k);
          ((ColorBall)this.colorballs.get(0)).addY(m);
          ((ColorBall)this.colorballs.get(1)).addY(m);
          ((ColorBall)this.colorballs.get(2)).addY(m);
          ((ColorBall)this.colorballs.get(3)).addY(m);
          if (this.groupId == 1) {
            this.canvas.drawRect(this.point1.x, this.point3.y, this.point3.x, this.point1.y, this.paint);
          } else {
            this.canvas.drawRect(this.point2.x, this.point4.y, this.point4.x, this.point2.y, this.paint);
          } 
          invalidate();
        } 
      } 
    } else {
      this.balID = -1;
      this.startMovePoint = new Point(i, j);
      for (ColorBall colorBall : this.colorballs) {
        k = colorBall.getX() + colorBall.getWidthOfBall();
        int m = colorBall.getY() + colorBall.getHeightOfBall();
        this.paint.setColor(-16711681);
        if (Math.sqrt(((k - i) * (k - i) + (m - j) * (m - j))) < colorBall.getWidthOfBall()) {
          i = colorBall.getID();
          this.balID = i;
          if (i == 1 || i == 3) {
            this.groupId = 2;
            this.canvas.drawRect(this.point1.x, this.point3.y, this.point3.x, this.point1.y, this.paint);
          } else {
            this.groupId = 1;
            this.canvas.drawRect(this.point2.x, this.point4.y, this.point4.x, this.point2.y, this.paint);
          } 
          invalidate();
          break;
        } 
        invalidate();
      } 
    } 
    invalidate();
    return true;
  }
  
  public void shade_region_between_points() {
    this.canvas.drawRect(this.point1.x, this.point3.y, this.point3.x, this.point1.y, this.paint);
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Views\DrawView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */