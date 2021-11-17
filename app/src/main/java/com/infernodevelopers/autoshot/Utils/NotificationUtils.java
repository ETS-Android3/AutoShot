package com.infernodevelopers.autoshot.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.util.Pair;

public class NotificationUtils {
  private static final String NOTIFICATION_CHANNEL_ID = "com.infernodevelopers.autoshot.app";
  
  private static final String NOTIFICATION_CHANNEL_NAME = "com.infernodevelopers.autoshot.app";
  
  public static final int NOTIFICATION_ID = 1337;
  
  private static Notification createNotification(Context paramContext) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(paramContext, "com.infernodevelopers.autoshot.app");
    builder.setSmallIcon(2131165277);
    builder.setContentTitle(paramContext.getString(2131820571));
    builder.setContentText("AutoCapture On");
    builder.setOngoing(true);
    builder.setCategory("service");
    builder.setPriority(-1);
    builder.setShowWhen(true);
    return builder.build();
  }
  
  private static void createNotificationChannel(Context paramContext) {
    if (Build.VERSION.SDK_INT >= 26) {
      NotificationChannel notificationChannel = new NotificationChannel("com.infernodevelopers.autoshot.app", "com.infernodevelopers.autoshot.app", 2);
      notificationChannel.setLockscreenVisibility(0);
      ((NotificationManager)paramContext.getSystemService("notification")).createNotificationChannel(notificationChannel);
    } 
  }
  
  public static Pair<Integer, Notification> getNotification(Context paramContext) {
    createNotificationChannel(paramContext);
    Notification notification = createNotification(paramContext);
    ((NotificationManager)paramContext.getSystemService("notification")).notify(1337, notification);
    return new Pair(Integer.valueOf(1337), notification);
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Utils\NotificationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */