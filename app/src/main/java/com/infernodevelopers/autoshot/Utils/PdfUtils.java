package com.infernodevelopers.autoshot.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.util.Log;
import com.infernodevelopers.autoshot.Adapters.ssfoldScreenRVData;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PdfUtils {
  private static final String TAG = "PdfUtils";
  
  Context context;
  
  List<ssfoldScreenRVData> data_for_pdf;
  
  String folder_to_store_pdf;
  
  public PdfUtils(Context paramContext, List<ssfoldScreenRVData> paramList, String paramString) {
    this.context = paramContext;
    this.data_for_pdf = paramList;
    this.folder_to_store_pdf = paramString;
  }
  
  public static Bitmap decodeSampledBitmapFromResource(String paramString, int paramInt1, int paramInt2) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramString, options);
    int k = options.outHeight;
    int m = options.outWidth;
    int j = 1;
    int i = 1;
    if (k > paramInt2 || m > paramInt1) {
      k /= 2;
      m /= 2;
      while (true) {
        j = i;
        if (k / i >= paramInt2) {
          j = i;
          if (m / i >= paramInt1) {
            i *= 2;
            continue;
          } 
        } 
        break;
      } 
    } 
    options.inSampleSize = j;
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(paramString, options);
  }
  
  public static Bitmap doSharpen(Bitmap paramBitmap, float[] paramArrayOffloat, Context paramContext) {
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    RenderScript renderScript = RenderScript.create(paramContext);
    Allocation allocation1 = Allocation.createFromBitmap(renderScript, paramBitmap);
    Allocation allocation2 = Allocation.createFromBitmap(renderScript, bitmap);
    ScriptIntrinsicConvolve3x3 scriptIntrinsicConvolve3x3 = ScriptIntrinsicConvolve3x3.create(renderScript, Element.U8_4(renderScript));
    scriptIntrinsicConvolve3x3.setInput(allocation1);
    scriptIntrinsicConvolve3x3.setCoefficients(paramArrayOffloat);
    scriptIntrinsicConvolve3x3.forEach(allocation2);
    allocation2.copyTo(bitmap);
    renderScript.destroy();
    return bitmap;
  }
  
  private static Bitmap rotateImage(Bitmap paramBitmap, int paramInt) {
    Matrix matrix = new Matrix();
    matrix.postRotate(paramInt);
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
    paramBitmap.recycle();
    return bitmap;
  }
  
  private static Bitmap rotateImageIfRequired(Context paramContext, Bitmap paramBitmap, Uri paramUri) throws IOException {
    ExifInterface exifInterface;
    InputStream inputStream = paramContext.getContentResolver().openInputStream(paramUri);
    if (Build.VERSION.SDK_INT > 23) {
      exifInterface = new ExifInterface(inputStream);
    } else {
      exifInterface = new ExifInterface(paramUri.getPath());
    } 
    int i = exifInterface.getAttributeInt("Orientation", 1);
    return (i != 3) ? ((i != 6) ? ((i != 8) ? paramBitmap : rotateImage(paramBitmap, 270)) : rotateImage(paramBitmap, 90)) : rotateImage(paramBitmap, 180);
  }
  
  public Bitmap createCompressedBitmap(Bitmap paramBitmap, String paramString) {
    int i = 1;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
    byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
    Bitmap bitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    if (options.outHeight > 1024 || options.outWidth > 1024) {
      int j = Math.min(options.outHeight / 1024, options.outWidth / 1024);
      float f1 = (options.outHeight * options.outWidth);
      float f2 = (1024 * 1024 * 2);
      while (true) {
        i = j;
        if (f1 / (j * j) > f2) {
          j++;
          continue;
        } 
        break;
      } 
    } 
    options.inSampleSize = i;
    options.inJustDecodeBounds = false;
    try {
      return rotateImageIfRequired(this.context.getApplicationContext(), bitmap, Uri.fromFile(new File(paramString)));
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return bitmap;
    } 
  }
  
  public void createPdf() {
    PdfDocument pdfDocument = new PdfDocument();
    for (int i = 0; i < this.data_for_pdf.size(); i++) {
      int j;
      char c;
      Bitmap bitmap1 = BitmapFactory.decodeFile(((ssfoldScreenRVData)this.data_for_pdf.get(i)).image_path);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("createPdf:dd ");
      stringBuilder.append(bitmap1.getWidth());
      stringBuilder.append(" ");
      stringBuilder.append(bitmap1.getHeight());
      Log.d("PdfUtils", stringBuilder.toString());
      float f = bitmap1.getWidth() / bitmap1.getHeight();
      if (bitmap1.getWidth() > bitmap1.getHeight()) {
        j = 1024;
        c = Math.round(j / f);
      } else {
        c = 'Ѐ';
        j = Math.round(c * f);
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("createPdf:d2 ");
      stringBuilder.append(j);
      stringBuilder.append(" ");
      stringBuilder.append(c);
      Log.d("PdfUtils", stringBuilder.toString());
      Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, j, c, false);
      PdfDocument.Page page = pdfDocument.startPage((new PdfDocument.PageInfo.Builder(bitmap2.getWidth(), bitmap2.getHeight(), 0)).create());
      Canvas canvas = page.getCanvas();
      Paint paint = new Paint();
      paint.setColor(Color.parseColor("#ffffff"));
      canvas.drawPaint(paint);
      bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), bitmap2.getHeight(), true);
      paint.setColor(-16776961);
      canvas.drawBitmap(bitmap2, 0.0F, 0.0F, null);
      pdfDocument.finishPage(page);
    } 
    File file = new File(this.folder_to_store_pdf);
    try {
      pdfDocument.writeTo(new FileOutputStream(file));
      Log.d("PdfUtils", "createPdf: success");
    } catch (IOException iOException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("createPdf: ");
      stringBuilder.append(iOException);
      Log.d("PdfUtils", stringBuilder.toString());
    } 
    pdfDocument.close();
  }
  
  public void deleteCreatePdfImages() {
    for (int i = 0; i < this.data_for_pdf.size(); i++) {
      if ((new File(((ssfoldScreenRVData)this.data_for_pdf.get(i)).image_path)).delete()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("deleteCreatePdfImages: ");
        stringBuilder.append(((ssfoldScreenRVData)this.data_for_pdf.get(i)).image_path);
        Log.d("PdfUtils", stringBuilder.toString());
      } 
    } 
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Utils\PdfUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */