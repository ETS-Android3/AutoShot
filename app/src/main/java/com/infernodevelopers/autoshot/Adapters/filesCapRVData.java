package com.infernodevelopers.autoshot.Adapters;

public class filesCapRVData {
  public boolean checked = false;
  
  String fileSize;
  
  public String filename;
  
  boolean isDirectory;
  
  String no_of_ss;
  
  public boolean showAds = false;
  
  String time_interval;
  
  public filesCapRVData(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) {
    this.filename = paramString1;
    this.isDirectory = paramBoolean;
    this.fileSize = paramString2;
    this.no_of_ss = paramString3;
    this.time_interval = paramString4;
  }
  
  public filesCapRVData(String paramString1, String paramString2, boolean paramBoolean) {
    this.filename = paramString1;
    this.fileSize = paramString2;
    this.isDirectory = paramBoolean;
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Adapters\filesCapRVData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */