package com.infernodevelopers.autoshot.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.util.Pair;
import com.infernodevelopers.autoshot.PreferencesScreen;
import com.infernodevelopers.autoshot.Utils.NotificationUtils;
import java.util.Objects;

public class AutoCaptureService extends Service {
  private static final String ACTION = "ACTION";
  
  public static final String AS_ACTIVE = "AS_ACTIVE";
  
  private static final String DATA = "DATA";
  
  public static final String DIR_PATH = "DIR_PATH";
  
  private static int IMAGES_PRODUCED = 0;
  
  public static final String OVERLAY_SCREEN = "OVERLAY_SCREEN";
  
  public static final String PAUSE = "PAUSE";
  
  private static final String RESULT_CODE = "RESULT_CODE";
  
  private static final String START = "START";
  
  private static final String STOP = "STOP";
  
  private static final String TAG = "AutoCaptureService";
  
  String PARENT_DIR_PATH;
  
  String SS_PREFIX;
  
  boolean auto_capture_timer_up = true;
  
  Handler capture_handler;
  
  boolean capture_override = false;
  
  ImageButton close_time_interval;
  
  int current_screen_height;
  
  int current_screen_width;
  
  RelativeLayout drag_panel_layout;
  
  RelativeLayout expand_panel_layout;
  
  SharedPreferences homescreen_rv_pref;
  
  Bitmap image_to_disp;
  
  TextView interval_time;
  
  private int mDensity;
  
  private Display mDisplay;
  
  private int mHeight;
  
  private ImageReader mImageReader;
  
  private MediaProjection mMediaProjection;
  
  private OrientationChangeCallback mOrientationChangeCallback;
  
  private View mOverlayView;
  
  private int mRotation;
  
  private VirtualDisplay mVirtualDisplay;
  
  private int mWidth;
  
  private WindowManager mWindowManager;
  
  boolean manual_capture_timer_up = false;
  
  ImageButton minus_time;
  
  TextView no_of_ss_drag_panel;
  
  TextView no_of_ss_expand_panel;
  
  Handler overlay_handler;
  
  SharedPreferences overlayscreen_pref;
  
  ImageButton pause_button;
  
  boolean play = true;
  
  ImageButton play_button;
  
  ImageButton plus_time;
  
  ImageView preview_iv;
  
  RelativeLayout preview_layout;
  
  int preview_ss_visibility = 0;
  
  double preview_time_in_secs = 1.0D;
  
  boolean save_image = false;
  
  SharedPreferences settings;
  
  ImageButton stop_button;
  
  RelativeLayout time_interval_layout;
  
  private int width_for_drag_panel;
  
  private void createVirtualDisplay() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("createVirtualDisplay: rot");
    stringBuilder.append(this.mRotation);
    Log.d("AutoCaptureService", stringBuilder.toString());
    if (this.mRotation == 0) {
      this.mWidth = this.current_screen_width;
      this.mHeight = this.current_screen_height;
    } else {
      this.mWidth = this.current_screen_height;
      this.mHeight = this.current_screen_width;
    } 
    this.mImageReader = ImageReader.newInstance(this.mWidth, this.mHeight, 1, 2);
    this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay(this.SS_PREFIX, this.mWidth, this.mHeight, this.mDensity, getVirtualDisplayFlags(), this.mImageReader.getSurface(), null, this.capture_handler);
    this.mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), this.capture_handler);
  }
  
  public static Intent getStartIntent(Context paramContext, int paramInt, Intent paramIntent) {
    Intent intent = new Intent(paramContext, AutoCaptureService.class);
    intent.putExtra("ACTION", "START");
    intent.putExtra("RESULT_CODE", paramInt);
    intent.putExtra("DATA", (Parcelable)paramIntent);
    return intent;
  }
  
  public static Intent getStopIntent(Context paramContext) {
    Intent intent = new Intent(paramContext, AutoCaptureService.class);
    intent.putExtra("ACTION", "STOP");
    return intent;
  }
  
  private static int getVirtualDisplayFlags() {
    return 9;
  }
  
  private static boolean isStartCommand(Intent paramIntent) {
    return (paramIntent.hasExtra("RESULT_CODE") && paramIntent.hasExtra("DATA") && paramIntent.hasExtra("ACTION") && Objects.equals(paramIntent.getStringExtra("ACTION"), "START"));
  }
  
  private static boolean isStopCommand(Intent paramIntent) {
    return (paramIntent.hasExtra("ACTION") && Objects.equals(paramIntent.getStringExtra("ACTION"), "STOP"));
  }
  
  private void startProjection(int paramInt, Intent paramIntent) {
    MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)getSystemService("media_projection");
    if (this.mMediaProjection == null) {
      MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(paramInt, paramIntent);
      this.mMediaProjection = mediaProjection;
      if (mediaProjection != null) {
        this.mDensity = (Resources.getSystem().getDisplayMetrics()).densityDpi;
        this.mDisplay = ((WindowManager)getSystemService("window")).getDefaultDisplay();
        createVirtualDisplay();
        OrientationChangeCallback orientationChangeCallback = new OrientationChangeCallback((Context)this);
        this.mOrientationChangeCallback = orientationChangeCallback;
        if (orientationChangeCallback.canDetectOrientation())
          this.mOrientationChangeCallback.enable(); 
        this.mMediaProjection.registerCallback(new MediaProjectionStopCallback(), this.capture_handler);
      } 
    } 
  }
  
  private void stopProjection() {
    Handler handler = this.capture_handler;
    if (handler != null)
      handler.post(new Runnable() {
            public void run() {
              if (AutoCaptureService.this.mMediaProjection != null)
                AutoCaptureService.this.mMediaProjection.stop(); 
            }
          }); 
  }
  
  public void findViewByIds() {
    this.no_of_ss_drag_panel = (TextView)this.mOverlayView.findViewById(2131296607);
    this.no_of_ss_expand_panel = (TextView)this.mOverlayView.findViewById(2131296611);
    this.interval_time = (TextView)this.mOverlayView.findViewById(2131296631);
    this.drag_panel_layout = (RelativeLayout)this.mOverlayView.findViewById(2131296424);
    this.expand_panel_layout = (RelativeLayout)this.mOverlayView.findViewById(2131296437);
    this.time_interval_layout = (RelativeLayout)this.mOverlayView.findViewById(2131296819);
    this.preview_layout = (RelativeLayout)this.mOverlayView.findViewById(2131296680);
    this.plus_time = (ImageButton)this.mOverlayView.findViewById(2131296630);
    this.minus_time = (ImageButton)this.mOverlayView.findViewById(2131296629);
    this.close_time_interval = (ImageButton)this.mOverlayView.findViewById(2131296627);
    this.play_button = (ImageButton)this.mOverlayView.findViewById(2131296613);
    this.pause_button = (ImageButton)this.mOverlayView.findViewById(2131296612);
    this.stop_button = (ImageButton)this.mOverlayView.findViewById(2131296615);
    this.preview_iv = (ImageView)this.mOverlayView.findViewById(2131296624);
  }
  
  public IBinder onBind(Intent paramIntent) {
    return null;
  }
  
  public void onClickListeners() {
    this.mOverlayView.findViewById(2131296602).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AutoCaptureService.this.manual_capture_timer_up = true;
          }
        });
    this.mOverlayView.findViewById(2131296616).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AutoCaptureService.this.time_interval_layout.setVisibility(0);
            AutoCaptureService.this.expand_panel_layout.setVisibility(4);
            TextView textView = AutoCaptureService.this.interval_time;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(AutoCaptureService.this.settings.getInt("DEFCAPTIME", PreferencesScreen.DEFAULT_CAP_DURATION.intValue()));
            stringBuilder.append(" seconds");
            textView.setText(stringBuilder.toString());
          }
        });
    this.plus_time.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            int i = Integer.parseInt(AutoCaptureService.this.interval_time.getText().toString().split(" ")[0]) + 1;
            TextView textView = AutoCaptureService.this.interval_time;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(i);
            stringBuilder.append(" seconds");
            textView.setText(stringBuilder.toString());
            AutoCaptureService.this.settings.edit().putInt("DEFCAPTIME", i).apply();
          }
        });
    this.minus_time.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            int i = Integer.parseInt(AutoCaptureService.this.interval_time.getText().toString().split(" ")[0]) - 1;
            if (i > 20) {
              TextView textView = AutoCaptureService.this.interval_time;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(i);
              stringBuilder.append(" seconds");
              textView.setText(stringBuilder.toString());
              AutoCaptureService.this.settings.edit().putInt("DEFCAPTIME", i).apply();
            } 
          }
        });
    this.close_time_interval.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AutoCaptureService.this.time_interval_layout.setVisibility(8);
            AutoCaptureService.this.expand_panel_layout.setVisibility(0);
          }
        });
    this.mOverlayView.findViewById(2131296614).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AutoCaptureService.this.drag_panel_layout.setVisibility(0);
            AutoCaptureService.this.expand_panel_layout.setVisibility(8);
          }
        });
    this.mOverlayView.findViewById(2131296603).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Log.d("AutoCaptureService", "onClick: ");
            AutoCaptureService.this.drag_panel_layout.setVisibility(8);
            AutoCaptureService.this.expand_panel_layout.setVisibility(0);
          }
        });
    this.play_button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AutoCaptureService.this.play = true;
            AutoCaptureService.this.auto_capture_timer_up = true;
            AutoCaptureService.this.play_button.setVisibility(4);
            AutoCaptureService.this.pause_button.setVisibility(0);
          }
        });
    this.pause_button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AutoCaptureService.this.play = false;
            AutoCaptureService.this.play_button.setVisibility(0);
            AutoCaptureService.this.pause_button.setVisibility(4);
          }
        });
    this.stop_button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Context context = AutoCaptureService.this.getApplicationContext();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("All screenshots stored in: ");
            stringBuilder.append(AutoCaptureService.this.PARENT_DIR_PATH);
            Toast.makeText(context, stringBuilder.toString(), 0).show();
            AutoCaptureService.this.homescreen_rv_pref.edit().putBoolean("CHANGE_UI_ON_STOP_FROM_OVERLAY", true).apply();
            AutoCaptureService.access$002(0);
            AutoCaptureService.this.play = false;
            AutoCaptureService.this.overlayscreen_pref.edit().putBoolean("AS_ACTIVE", false).apply();
            AutoCaptureService.this.stopProjection();
            AutoCaptureService.this.stopSelf();
          }
        });
    this.no_of_ss_drag_panel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AutoCaptureService autoCaptureService = AutoCaptureService.this;
            autoCaptureService.play ^= 0x1;
            if (AutoCaptureService.this.play) {
              Toast.makeText(AutoCaptureService.this.getApplicationContext(), "Play", 0).show();
              AutoCaptureService.this.auto_capture_timer_up = true;
              AutoCaptureService.this.pause_button.setVisibility(0);
              AutoCaptureService.this.play_button.setVisibility(4);
              return;
            } 
            Toast.makeText(AutoCaptureService.this.getApplicationContext(), "Pause", 0).show();
            AutoCaptureService.this.pause_button.setVisibility(4);
            AutoCaptureService.this.play_button.setVisibility(0);
          }
        });
  }
  
  public void onCreate() {
    super.onCreate();
    setTheme(2131886500);
    this.mOverlayView = LayoutInflater.from((Context)this).inflate(2131492968, null);
    this.overlay_handler = new Handler();
    this.overlayscreen_pref = getApplicationContext().getSharedPreferences("OVERLAY_SCREEN", 0);
    this.settings = getApplicationContext().getSharedPreferences("SETTINGS_FILE", 0);
    String str = this.overlayscreen_pref.getString("DIR_PATH", "");
    this.PARENT_DIR_PATH = str;
    this.SS_PREFIX = str.split("/")[(this.PARENT_DIR_PATH.split("/")).length - 1];
    SharedPreferences sharedPreferences = getSharedPreferences("HOMESCREEN_RV", 0);
    this.homescreen_rv_pref = sharedPreferences;
    IMAGES_PRODUCED = sharedPreferences.getInt("LAST_NUM_WITH_CONV", 0);
    findViewByIds();
    this.no_of_ss_expand_panel.setText(Integer.toString(IMAGES_PRODUCED));
    this.no_of_ss_drag_panel.setText(Integer.toString(IMAGES_PRODUCED));
    onClickListeners();
    overlay_views();
    (new Thread() {
        public void run() {
          Looper.prepare();
          AutoCaptureService.this.capture_handler = new Handler();
          Looper.loop();
        }
      }).start();
  }
  
  public void onDestroy() {
    super.onDestroy();
    View view = this.mOverlayView;
    if (view != null)
      this.mWindowManager.removeView(view); 
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    if (isStartCommand(paramIntent)) {
      Pair pair = NotificationUtils.getNotification((Context)this);
      startForeground(((Integer)pair.first).intValue(), (Notification)pair.second);
      startProjection(paramIntent.getIntExtra("RESULT_CODE", 0), (Intent)paramIntent.getParcelableExtra("DATA"));
    } else if (isStopCommand(paramIntent)) {
      stopProjection();
      stopSelf();
    } else {
      stopSelf();
    } 
    return 2;
  }
  
  public void overlay_views() {
    char c;
    if (Build.VERSION.SDK_INT >= 26) {
      c = '߶';
    } else {
      c = 'ߒ';
    } 
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(-2, -2, c, 264, -3);
    layoutParams.gravity = 8388611;
    layoutParams.x = 0;
    layoutParams.y = 100;
    WindowManager windowManager = (WindowManager)getSystemService("window");
    this.mWindowManager = windowManager;
    windowManager.addView(this.mOverlayView, (ViewGroup.LayoutParams)layoutParams);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    this.mWindowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
    this.current_screen_height = displayMetrics.heightPixels;
    this.current_screen_width = displayMetrics.widthPixels;
    this.no_of_ss_expand_panel.setOnTouchListener(new View.OnTouchListener() {
          private float initialTouchX;
          
          private float initialTouchY;
          
          private int initialX;
          
          private int initialY;
          
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            int i = param1MotionEvent.getAction();
            if (i != 0) {
              if (i != 1) {
                if (i != 2)
                  return false; 
                float f1 = Math.round(param1MotionEvent.getRawX() - this.initialTouchX);
                float f2 = Math.round(param1MotionEvent.getRawY() - this.initialTouchY);
                params.x = this.initialX + (int)f1;
                params.y = this.initialY + (int)f2;
                AutoCaptureService.this.mWindowManager.updateViewLayout(AutoCaptureService.this.mOverlayView, (ViewGroup.LayoutParams)params);
                return true;
              } 
              return true;
            } 
            this.initialX = params.x;
            this.initialY = params.y;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onTouch: ");
            stringBuilder.append(this.initialX);
            Log.d("AutoCaptureService", stringBuilder.toString());
            this.initialTouchX = param1MotionEvent.getRawX();
            this.initialTouchY = param1MotionEvent.getRawY();
            return true;
          }
        });
    this.mOverlayView.findViewById(2131296604).setOnTouchListener(new View.OnTouchListener() {
          private float initialTouchX;
          
          private float initialTouchY;
          
          private int initialX;
          
          private int initialY;
          
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            int i = param1MotionEvent.getAction();
            if (i != 0) {
              if (i != 1) {
                if (i != 2)
                  return false; 
                float f1 = Math.round(param1MotionEvent.getRawX() - this.initialTouchX);
                float f2 = Math.round(param1MotionEvent.getRawY() - this.initialTouchY);
                params.x = this.initialX + (int)f1;
                params.y = this.initialY + (int)f2;
                AutoCaptureService.this.mWindowManager.updateViewLayout(AutoCaptureService.this.mOverlayView, (ViewGroup.LayoutParams)params);
                return true;
              } 
              return true;
            } 
            this.initialX = params.x;
            this.initialY = params.y;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onTouch: ");
            stringBuilder.append(this.initialX);
            Log.d("AutoCaptureService", stringBuilder.toString());
            this.initialTouchX = param1MotionEvent.getRawX();
            this.initialTouchY = param1MotionEvent.getRawY();
            return true;
          }
        });
  }
  
  private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
    private ImageAvailableListener() {}
    
    public void onImageAvailable(ImageReader param1ImageReader) {
      // Byte code:
      //   0: aconst_null
      //   1: astore #24
      //   3: aconst_null
      //   4: astore #21
      //   6: aconst_null
      //   7: astore #18
      //   9: aconst_null
      //   10: astore #16
      //   12: aconst_null
      //   13: astore #17
      //   15: aconst_null
      //   16: astore #19
      //   18: aconst_null
      //   19: astore #20
      //   21: aconst_null
      //   22: astore #22
      //   24: aconst_null
      //   25: astore #15
      //   27: aconst_null
      //   28: astore #13
      //   30: aconst_null
      //   31: astore #23
      //   33: aload_0
      //   34: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   37: invokestatic access$700 : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;)Landroid/media/ImageReader;
      //   40: invokevirtual acquireLatestImage : ()Landroid/media/Image;
      //   43: astore #25
      //   45: aload #23
      //   47: astore #12
      //   49: aload #24
      //   51: astore #14
      //   53: aload #25
      //   55: ifnull -> 1272
      //   58: aload #22
      //   60: astore_1
      //   61: aload #21
      //   63: astore #13
      //   65: aload_0
      //   66: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   69: getfield play : Z
      //   72: ifeq -> 92
      //   75: aload #22
      //   77: astore_1
      //   78: aload #21
      //   80: astore #13
      //   82: aload_0
      //   83: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   86: getfield auto_capture_timer_up : Z
      //   89: ifne -> 117
      //   92: aload #23
      //   94: astore #12
      //   96: aload #24
      //   98: astore #14
      //   100: aload #22
      //   102: astore_1
      //   103: aload #21
      //   105: astore #13
      //   107: aload_0
      //   108: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   111: getfield manual_capture_timer_up : Z
      //   114: ifeq -> 1272
      //   117: aload #22
      //   119: astore_1
      //   120: aload #21
      //   122: astore #13
      //   124: aload #25
      //   126: invokevirtual getPlanes : ()[Landroid/media/Image$Plane;
      //   129: astore #12
      //   131: aload #22
      //   133: astore_1
      //   134: aload #21
      //   136: astore #13
      //   138: aload #12
      //   140: iconst_0
      //   141: aaload
      //   142: invokevirtual getBuffer : ()Ljava/nio/ByteBuffer;
      //   145: astore #14
      //   147: aload #22
      //   149: astore_1
      //   150: aload #21
      //   152: astore #13
      //   154: aload #12
      //   156: iconst_0
      //   157: aaload
      //   158: invokevirtual getPixelStride : ()I
      //   161: istore #4
      //   163: aload #22
      //   165: astore_1
      //   166: aload #21
      //   168: astore #13
      //   170: aload #12
      //   172: iconst_0
      //   173: aaload
      //   174: invokevirtual getRowStride : ()I
      //   177: istore #5
      //   179: aload #22
      //   181: astore_1
      //   182: aload #21
      //   184: astore #13
      //   186: aload_0
      //   187: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   190: invokestatic access$800 : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;)I
      //   193: istore #6
      //   195: aload #22
      //   197: astore_1
      //   198: aload #21
      //   200: astore #13
      //   202: aload_0
      //   203: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   206: invokestatic access$800 : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;)I
      //   209: iload #5
      //   211: iload #6
      //   213: iload #4
      //   215: imul
      //   216: isub
      //   217: iload #4
      //   219: idiv
      //   220: iadd
      //   221: aload_0
      //   222: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   225: invokestatic access$900 : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;)I
      //   228: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
      //   231: invokestatic createBitmap : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
      //   234: astore #12
      //   236: aload #12
      //   238: astore_1
      //   239: aload #21
      //   241: astore #13
      //   243: aload #12
      //   245: aload #14
      //   247: invokevirtual copyPixelsFromBuffer : (Ljava/nio/Buffer;)V
      //   250: aload #12
      //   252: astore_1
      //   253: aload #21
      //   255: astore #13
      //   257: aload_0
      //   258: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   261: getfield settings : Landroid/content/SharedPreferences;
      //   264: astore #14
      //   266: aload #12
      //   268: astore_1
      //   269: aload #21
      //   271: astore #13
      //   273: new java/lang/StringBuilder
      //   276: dup
      //   277: invokespecial <init> : ()V
      //   280: astore #15
      //   282: aload #12
      //   284: astore_1
      //   285: aload #21
      //   287: astore #13
      //   289: aload #15
      //   291: ldc '0_0_'
      //   293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   296: pop
      //   297: aload #12
      //   299: astore_1
      //   300: aload #21
      //   302: astore #13
      //   304: aload #15
      //   306: aload_0
      //   307: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   310: getfield current_screen_width : I
      //   313: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   316: pop
      //   317: aload #12
      //   319: astore_1
      //   320: aload #21
      //   322: astore #13
      //   324: aload #15
      //   326: ldc '_'
      //   328: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   331: pop
      //   332: aload #12
      //   334: astore_1
      //   335: aload #21
      //   337: astore #13
      //   339: aload #15
      //   341: aload_0
      //   342: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   345: getfield current_screen_height : I
      //   348: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   351: pop
      //   352: aload #12
      //   354: astore_1
      //   355: aload #21
      //   357: astore #13
      //   359: aload #14
      //   361: ldc 'SCREEN_CROP'
      //   363: aload #15
      //   365: invokevirtual toString : ()Ljava/lang/String;
      //   368: invokeinterface getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   373: ldc '_'
      //   375: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
      //   378: astore #14
      //   380: aload #12
      //   382: astore_1
      //   383: aload #21
      //   385: astore #13
      //   387: ldc 'AutoCaptureService'
      //   389: ldc 'onImageAvailable:scrop SCREEN_CROP'
      //   391: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   394: pop
      //   395: aload #12
      //   397: astore_1
      //   398: aload #21
      //   400: astore #13
      //   402: new java/lang/StringBuilder
      //   405: dup
      //   406: invokespecial <init> : ()V
      //   409: astore #15
      //   411: aload #12
      //   413: astore_1
      //   414: aload #21
      //   416: astore #13
      //   418: aload #15
      //   420: ldc 'onImageAvailable:calc '
      //   422: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   425: pop
      //   426: aload #12
      //   428: astore_1
      //   429: aload #21
      //   431: astore #13
      //   433: aload #15
      //   435: aload_0
      //   436: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   439: invokestatic access$800 : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;)I
      //   442: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   445: pop
      //   446: aload #12
      //   448: astore_1
      //   449: aload #21
      //   451: astore #13
      //   453: aload #15
      //   455: ldc ' '
      //   457: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   460: pop
      //   461: aload #12
      //   463: astore_1
      //   464: aload #21
      //   466: astore #13
      //   468: aload #15
      //   470: aload_0
      //   471: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   474: invokestatic access$900 : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;)I
      //   477: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   480: pop
      //   481: aload #12
      //   483: astore_1
      //   484: aload #21
      //   486: astore #13
      //   488: ldc 'AutoCaptureService'
      //   490: aload #15
      //   492: invokevirtual toString : ()Ljava/lang/String;
      //   495: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   498: pop
      //   499: aload #12
      //   501: astore_1
      //   502: aload #21
      //   504: astore #13
      //   506: new java/lang/StringBuilder
      //   509: dup
      //   510: invokespecial <init> : ()V
      //   513: astore #15
      //   515: aload #12
      //   517: astore_1
      //   518: aload #21
      //   520: astore #13
      //   522: aload #15
      //   524: ldc 'onImageAvailable:coord '
      //   526: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   529: pop
      //   530: aload #12
      //   532: astore_1
      //   533: aload #21
      //   535: astore #13
      //   537: aload #15
      //   539: aload #14
      //   541: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
      //   544: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   547: pop
      //   548: aload #12
      //   550: astore_1
      //   551: aload #21
      //   553: astore #13
      //   555: ldc 'AutoCaptureService'
      //   557: aload #15
      //   559: invokevirtual toString : ()Ljava/lang/String;
      //   562: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   565: pop
      //   566: aload #12
      //   568: astore_1
      //   569: aload #21
      //   571: astore #13
      //   573: new java/lang/StringBuilder
      //   576: dup
      //   577: invokespecial <init> : ()V
      //   580: astore #15
      //   582: aload #12
      //   584: astore_1
      //   585: aload #21
      //   587: astore #13
      //   589: aload #15
      //   591: ldc 'onImageAvailable:device '
      //   593: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   596: pop
      //   597: aload #12
      //   599: astore_1
      //   600: aload #21
      //   602: astore #13
      //   604: aload #15
      //   606: aload_0
      //   607: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   610: getfield current_screen_height : I
      //   613: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   616: pop
      //   617: aload #12
      //   619: astore_1
      //   620: aload #21
      //   622: astore #13
      //   624: aload #15
      //   626: ldc '_'
      //   628: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   631: pop
      //   632: aload #12
      //   634: astore_1
      //   635: aload #21
      //   637: astore #13
      //   639: aload #15
      //   641: aload_0
      //   642: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   645: getfield current_screen_width : I
      //   648: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   651: pop
      //   652: aload #12
      //   654: astore_1
      //   655: aload #21
      //   657: astore #13
      //   659: ldc 'AutoCaptureService'
      //   661: aload #15
      //   663: invokevirtual toString : ()Ljava/lang/String;
      //   666: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   669: pop
      //   670: aload #12
      //   672: astore_1
      //   673: aload #21
      //   675: astore #13
      //   677: aload_0
      //   678: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   681: invokestatic access$1000 : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;)I
      //   684: ifne -> 766
      //   687: aload #12
      //   689: astore_1
      //   690: aload #21
      //   692: astore #13
      //   694: ldc 'AutoCaptureService'
      //   696: ldc 'onImageAvailable: portrait'
      //   698: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   701: pop
      //   702: aload #12
      //   704: astore_1
      //   705: aload #21
      //   707: astore #13
      //   709: aload #12
      //   711: aload #14
      //   713: iconst_0
      //   714: aaload
      //   715: invokestatic parseInt : (Ljava/lang/String;)I
      //   718: aload #14
      //   720: iconst_1
      //   721: aaload
      //   722: invokestatic parseInt : (Ljava/lang/String;)I
      //   725: aload #14
      //   727: iconst_2
      //   728: aaload
      //   729: invokestatic parseInt : (Ljava/lang/String;)I
      //   732: aload #14
      //   734: iconst_0
      //   735: aaload
      //   736: invokestatic parseInt : (Ljava/lang/String;)I
      //   739: isub
      //   740: aload #14
      //   742: iconst_3
      //   743: aaload
      //   744: invokestatic parseInt : (Ljava/lang/String;)I
      //   747: aload #14
      //   749: iconst_1
      //   750: aaload
      //   751: invokestatic parseInt : (Ljava/lang/String;)I
      //   754: isub
      //   755: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIII)Landroid/graphics/Bitmap;
      //   758: astore #12
      //   760: aload #12
      //   762: astore_1
      //   763: goto -> 842
      //   766: aload #12
      //   768: astore_1
      //   769: aload #21
      //   771: astore #13
      //   773: ldc 'AutoCaptureService'
      //   775: ldc 'onImageAvailable: landscape'
      //   777: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   780: pop
      //   781: aload #12
      //   783: astore_1
      //   784: aload #21
      //   786: astore #13
      //   788: aload #12
      //   790: aload #14
      //   792: iconst_1
      //   793: aaload
      //   794: invokestatic parseInt : (Ljava/lang/String;)I
      //   797: aload #14
      //   799: iconst_0
      //   800: aaload
      //   801: invokestatic parseInt : (Ljava/lang/String;)I
      //   804: aload #14
      //   806: iconst_3
      //   807: aaload
      //   808: invokestatic parseInt : (Ljava/lang/String;)I
      //   811: aload #14
      //   813: iconst_1
      //   814: aaload
      //   815: invokestatic parseInt : (Ljava/lang/String;)I
      //   818: isub
      //   819: aload #14
      //   821: iconst_2
      //   822: aaload
      //   823: invokestatic parseInt : (Ljava/lang/String;)I
      //   826: aload #14
      //   828: iconst_0
      //   829: aaload
      //   830: invokestatic parseInt : (Ljava/lang/String;)I
      //   833: isub
      //   834: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIII)Landroid/graphics/Bitmap;
      //   837: astore #12
      //   839: aload #12
      //   841: astore_1
      //   842: new java/lang/StringBuilder
      //   845: dup
      //   846: invokespecial <init> : ()V
      //   849: astore #12
      //   851: aload #12
      //   853: ldc 'onImageAvailable:bit dimen '
      //   855: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   858: pop
      //   859: aload #12
      //   861: aload_1
      //   862: invokevirtual getWidth : ()I
      //   865: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   868: pop
      //   869: aload #12
      //   871: ldc ' '
      //   873: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   876: pop
      //   877: aload #12
      //   879: aload_1
      //   880: invokevirtual getHeight : ()I
      //   883: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   886: pop
      //   887: ldc 'AutoCaptureService'
      //   889: aload #12
      //   891: invokevirtual toString : ()Ljava/lang/String;
      //   894: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   897: pop
      //   898: aload_0
      //   899: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   902: getfield auto_capture_timer_up : Z
      //   905: istore #7
      //   907: iload #7
      //   909: ifeq -> 987
      //   912: aload_0
      //   913: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   916: getfield settings : Landroid/content/SharedPreferences;
      //   919: ldc 'DEFCAPTIME'
      //   921: bipush #20
      //   923: invokeinterface getInt : (Ljava/lang/String;I)I
      //   928: sipush #1000
      //   931: imul
      //   932: i2l
      //   933: lstore #8
      //   935: aload_0
      //   936: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   939: getfield preview_time_in_secs : D
      //   942: dstore_2
      //   943: dload_2
      //   944: ldc2_w 1000.0
      //   947: dmul
      //   948: d2l
      //   949: lstore #10
      //   951: new com/infernodevelopers/autoshot/Services/AutoCaptureService$ImageAvailableListener$1
      //   954: dup
      //   955: aload_0
      //   956: lload #8
      //   958: lload #10
      //   960: invokespecial <init> : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService$ImageAvailableListener;JJ)V
      //   963: invokevirtual start : ()Landroid/os/CountDownTimer;
      //   966: pop
      //   967: aload_0
      //   968: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   971: iconst_0
      //   972: putfield auto_capture_timer_up : Z
      //   975: goto -> 987
      //   978: astore #12
      //   980: aload #18
      //   982: astore #14
      //   984: goto -> 1359
      //   987: aload_1
      //   988: astore #12
      //   990: aload #19
      //   992: astore #14
      //   994: aload_0
      //   995: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   998: getfield manual_capture_timer_up : Z
      //   1001: istore #7
      //   1003: iload #7
      //   1005: ifeq -> 1071
      //   1008: aload_0
      //   1009: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1012: iconst_0
      //   1013: putfield preview_ss_visibility : I
      //   1016: aload_0
      //   1017: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1020: iconst_1
      //   1021: putfield save_image : Z
      //   1024: new com/infernodevelopers/autoshot/Services/AutoCaptureService$ImageAvailableListener$2
      //   1027: dup
      //   1028: aload_0
      //   1029: ldc2_w 1000
      //   1032: aload_0
      //   1033: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1036: getfield preview_time_in_secs : D
      //   1039: d2l
      //   1040: lmul
      //   1041: ldc2_w 10
      //   1044: invokespecial <init> : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService$ImageAvailableListener;JJ)V
      //   1047: invokevirtual start : ()Landroid/os/CountDownTimer;
      //   1050: pop
      //   1051: aload_0
      //   1052: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1055: iconst_0
      //   1056: putfield manual_capture_timer_up : Z
      //   1059: goto -> 1071
      //   1062: astore #12
      //   1064: aload #18
      //   1066: astore #14
      //   1068: goto -> 1359
      //   1071: aload #19
      //   1073: astore #14
      //   1075: aload_0
      //   1076: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1079: getfield save_image : Z
      //   1082: ifeq -> 1497
      //   1085: aload #19
      //   1087: astore #14
      //   1089: invokestatic access$008 : ()I
      //   1092: pop
      //   1093: aload #19
      //   1095: astore #14
      //   1097: new java/lang/StringBuilder
      //   1100: dup
      //   1101: invokespecial <init> : ()V
      //   1104: astore_1
      //   1105: aload #19
      //   1107: astore #14
      //   1109: aload_1
      //   1110: aload_0
      //   1111: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1114: getfield PARENT_DIR_PATH : Ljava/lang/String;
      //   1117: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1120: pop
      //   1121: aload #19
      //   1123: astore #14
      //   1125: aload_1
      //   1126: ldc '/'
      //   1128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1131: pop
      //   1132: aload #19
      //   1134: astore #14
      //   1136: aload_1
      //   1137: aload_0
      //   1138: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1141: getfield SS_PREFIX : Ljava/lang/String;
      //   1144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1147: pop
      //   1148: aload #19
      //   1150: astore #14
      //   1152: aload_1
      //   1153: ldc '_'
      //   1155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1158: pop
      //   1159: aload #19
      //   1161: astore #14
      //   1163: aload_1
      //   1164: invokestatic access$000 : ()I
      //   1167: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   1170: pop
      //   1171: aload #19
      //   1173: astore #14
      //   1175: aload_1
      //   1176: ldc '.jpg'
      //   1178: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1181: pop
      //   1182: aload #19
      //   1184: astore #14
      //   1186: new java/io/FileOutputStream
      //   1189: dup
      //   1190: aload_1
      //   1191: invokevirtual toString : ()Ljava/lang/String;
      //   1194: invokespecial <init> : (Ljava/lang/String;)V
      //   1197: astore #15
      //   1199: aload #15
      //   1201: astore #14
      //   1203: getstatic android/graphics/Bitmap$CompressFormat.JPEG : Landroid/graphics/Bitmap$CompressFormat;
      //   1206: astore #16
      //   1208: aload #12
      //   1210: astore #14
      //   1212: aload #14
      //   1214: astore_1
      //   1215: aload #15
      //   1217: astore #13
      //   1219: aload #14
      //   1221: aload #16
      //   1223: bipush #100
      //   1225: aload #15
      //   1227: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
      //   1230: pop
      //   1231: aload #14
      //   1233: astore_1
      //   1234: aload #15
      //   1236: astore #13
      //   1238: aload_0
      //   1239: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1242: aload #14
      //   1244: invokestatic createBitmap : (Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;
      //   1247: putfield image_to_disp : Landroid/graphics/Bitmap;
      //   1250: aload #15
      //   1252: astore #14
      //   1254: goto -> 1257
      //   1257: aload #12
      //   1259: astore_1
      //   1260: aload #14
      //   1262: astore #13
      //   1264: aload #12
      //   1266: invokevirtual recycle : ()V
      //   1269: goto -> 1272
      //   1272: aload #12
      //   1274: astore_1
      //   1275: aload #14
      //   1277: astore #13
      //   1279: aload_0
      //   1280: getfield this$0 : Lcom/infernodevelopers/autoshot/Services/AutoCaptureService;
      //   1283: getfield overlay_handler : Landroid/os/Handler;
      //   1286: new com/infernodevelopers/autoshot/Services/AutoCaptureService$ImageAvailableListener$3
      //   1289: dup
      //   1290: aload_0
      //   1291: invokespecial <init> : (Lcom/infernodevelopers/autoshot/Services/AutoCaptureService$ImageAvailableListener;)V
      //   1294: invokevirtual post : (Ljava/lang/Runnable;)Z
      //   1297: pop
      //   1298: aload #25
      //   1300: ifnull -> 1324
      //   1303: aload #12
      //   1305: astore #15
      //   1307: aload #14
      //   1309: astore #16
      //   1311: aload #12
      //   1313: astore #13
      //   1315: aload #14
      //   1317: astore #17
      //   1319: aload #25
      //   1321: invokevirtual close : ()V
      //   1324: aload #14
      //   1326: ifnull -> 1342
      //   1329: aload #14
      //   1331: invokevirtual close : ()V
      //   1334: goto -> 1342
      //   1337: astore_1
      //   1338: aload_1
      //   1339: invokevirtual printStackTrace : ()V
      //   1342: aload #12
      //   1344: ifnull -> 1464
      //   1347: aload #12
      //   1349: invokevirtual recycle : ()V
      //   1352: return
      //   1353: astore #12
      //   1355: aload #13
      //   1357: astore #14
      //   1359: aload #12
      //   1361: athrow
      //   1362: astore #18
      //   1364: aload #25
      //   1366: ifnull -> 1400
      //   1369: aload #25
      //   1371: invokevirtual close : ()V
      //   1374: goto -> 1400
      //   1377: astore #19
      //   1379: aload_1
      //   1380: astore #15
      //   1382: aload #14
      //   1384: astore #16
      //   1386: aload_1
      //   1387: astore #13
      //   1389: aload #14
      //   1391: astore #17
      //   1393: aload #12
      //   1395: aload #19
      //   1397: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
      //   1400: aload_1
      //   1401: astore #15
      //   1403: aload #14
      //   1405: astore #16
      //   1407: aload_1
      //   1408: astore #13
      //   1410: aload #14
      //   1412: astore #17
      //   1414: aload #18
      //   1416: athrow
      //   1417: astore_1
      //   1418: goto -> 1465
      //   1421: astore_1
      //   1422: aload #13
      //   1424: astore #15
      //   1426: aload #17
      //   1428: astore #16
      //   1430: aload_1
      //   1431: invokevirtual printStackTrace : ()V
      //   1434: aload #17
      //   1436: ifnull -> 1452
      //   1439: aload #17
      //   1441: invokevirtual close : ()V
      //   1444: goto -> 1452
      //   1447: astore_1
      //   1448: aload_1
      //   1449: invokevirtual printStackTrace : ()V
      //   1452: aload #13
      //   1454: ifnull -> 1464
      //   1457: aload #13
      //   1459: astore #12
      //   1461: goto -> 1347
      //   1464: return
      //   1465: aload #16
      //   1467: ifnull -> 1485
      //   1470: aload #16
      //   1472: invokevirtual close : ()V
      //   1475: goto -> 1485
      //   1478: astore #12
      //   1480: aload #12
      //   1482: invokevirtual printStackTrace : ()V
      //   1485: aload #15
      //   1487: ifnull -> 1495
      //   1490: aload #15
      //   1492: invokevirtual recycle : ()V
      //   1495: aload_1
      //   1496: athrow
      //   1497: aload #20
      //   1499: astore #14
      //   1501: goto -> 1257
      //   1504: astore #13
      //   1506: aload #12
      //   1508: astore_1
      //   1509: aload #13
      //   1511: astore #12
      //   1513: goto -> 1359
      //   1516: astore #12
      //   1518: aload #18
      //   1520: astore #14
      //   1522: goto -> 1359
      // Exception table:
      //   from	to	target	type
      //   33	45	1421	java/lang/Exception
      //   33	45	1417	finally
      //   65	75	1353	finally
      //   82	92	1353	finally
      //   107	117	1353	finally
      //   124	131	1353	finally
      //   138	147	1353	finally
      //   154	163	1353	finally
      //   170	179	1353	finally
      //   186	195	1353	finally
      //   202	236	1353	finally
      //   243	250	1353	finally
      //   257	266	1353	finally
      //   273	282	1353	finally
      //   289	297	1353	finally
      //   304	317	1353	finally
      //   324	332	1353	finally
      //   339	352	1353	finally
      //   359	380	1353	finally
      //   387	395	1353	finally
      //   402	411	1353	finally
      //   418	426	1353	finally
      //   433	446	1353	finally
      //   453	461	1353	finally
      //   468	481	1353	finally
      //   488	499	1353	finally
      //   506	515	1353	finally
      //   522	530	1353	finally
      //   537	548	1353	finally
      //   555	566	1353	finally
      //   573	582	1353	finally
      //   589	597	1353	finally
      //   604	617	1353	finally
      //   624	632	1353	finally
      //   639	652	1353	finally
      //   659	670	1353	finally
      //   677	687	1353	finally
      //   694	702	1353	finally
      //   709	760	1353	finally
      //   773	781	1353	finally
      //   788	839	1353	finally
      //   842	907	1516	finally
      //   912	943	978	finally
      //   951	975	1062	finally
      //   994	1003	1504	finally
      //   1008	1059	1062	finally
      //   1075	1085	1504	finally
      //   1089	1093	1504	finally
      //   1097	1105	1504	finally
      //   1109	1121	1504	finally
      //   1125	1132	1504	finally
      //   1136	1148	1504	finally
      //   1152	1159	1504	finally
      //   1163	1171	1504	finally
      //   1175	1182	1504	finally
      //   1186	1199	1504	finally
      //   1203	1208	1504	finally
      //   1219	1231	1353	finally
      //   1238	1250	1353	finally
      //   1264	1269	1353	finally
      //   1279	1298	1353	finally
      //   1319	1324	1421	java/lang/Exception
      //   1319	1324	1417	finally
      //   1329	1334	1337	java/io/IOException
      //   1359	1362	1362	finally
      //   1369	1374	1377	finally
      //   1393	1400	1421	java/lang/Exception
      //   1393	1400	1417	finally
      //   1414	1417	1421	java/lang/Exception
      //   1414	1417	1417	finally
      //   1430	1434	1417	finally
      //   1439	1444	1447	java/io/IOException
      //   1470	1475	1478	java/io/IOException
    }
  }
  
  class null extends CountDownTimer {
    null(long param1Long1, long param1Long2) {
      super(param1Long1, param1Long2);
    }
    
    public void onFinish() {
      if (AutoCaptureService.this.play) {
        AutoCaptureService.this.preview_ss_visibility = 0;
        AutoCaptureService.this.auto_capture_timer_up = true;
        AutoCaptureService.this.save_image = true;
      } 
    }
    
    public void onTick(long param1Long) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("onTick: ");
      stringBuilder.append(param1Long);
      Log.d("AutoCaptureService", stringBuilder.toString());
      AutoCaptureService.this.save_image = false;
      if (param1Long <= (AutoCaptureService.this.settings.getInt("DEFCAPTIME", 20) - AutoCaptureService.this.preview_time_in_secs) * 1000.0D && !AutoCaptureService.this.manual_capture_timer_up)
        AutoCaptureService.this.preview_ss_visibility = 8; 
      if (!AutoCaptureService.this.play)
        cancel(); 
      if (param1Long <= 3000L && param1Long > 2000L)
        Toast.makeText(AutoCaptureService.this.getApplicationContext(), "taking ss...", 0).show(); 
      AutoCaptureService.this.auto_capture_timer_up = false;
    }
  }
  
  class null extends CountDownTimer {
    null(long param1Long1, long param1Long2) {
      super(param1Long1, param1Long2);
    }
    
    public void onFinish() {
      AutoCaptureService.this.preview_ss_visibility = 8;
    }
    
    public void onTick(long param1Long) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("onTick: ");
      stringBuilder.append(param1Long);
      Log.d("AutoCaptureService", stringBuilder.toString());
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (AutoCaptureService.this.image_to_disp != null) {
        AutoCaptureService.this.no_of_ss_drag_panel.setText(Integer.toString(AutoCaptureService.IMAGES_PRODUCED));
        AutoCaptureService.this.no_of_ss_expand_panel.setText(Integer.toString(AutoCaptureService.IMAGES_PRODUCED));
        AutoCaptureService.this.preview_layout.setVisibility(AutoCaptureService.this.preview_ss_visibility);
        AutoCaptureService.this.preview_iv.setImageBitmap(AutoCaptureService.this.image_to_disp);
      } 
    }
  }
  
  private class MediaProjectionStopCallback extends MediaProjection.Callback {
    private MediaProjectionStopCallback() {}
    
    public void onStop() {
      Log.e("AutoCaptureService", "stopping projection.");
      AutoCaptureService.this.capture_handler.post(new Runnable() {
            public void run() {
              if (AutoCaptureService.this.mVirtualDisplay != null)
                AutoCaptureService.this.mVirtualDisplay.release(); 
              if (AutoCaptureService.this.mImageReader != null)
                AutoCaptureService.this.mImageReader.setOnImageAvailableListener(null, null); 
              if (AutoCaptureService.this.mOrientationChangeCallback != null)
                AutoCaptureService.this.mOrientationChangeCallback.disable(); 
              AutoCaptureService.this.mMediaProjection.unregisterCallback(AutoCaptureService.MediaProjectionStopCallback.this);
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (AutoCaptureService.this.mVirtualDisplay != null)
        AutoCaptureService.this.mVirtualDisplay.release(); 
      if (AutoCaptureService.this.mImageReader != null)
        AutoCaptureService.this.mImageReader.setOnImageAvailableListener(null, null); 
      if (AutoCaptureService.this.mOrientationChangeCallback != null)
        AutoCaptureService.this.mOrientationChangeCallback.disable(); 
      AutoCaptureService.this.mMediaProjection.unregisterCallback(this.this$1);
    }
  }
  
  private class OrientationChangeCallback extends OrientationEventListener {
    OrientationChangeCallback(Context param1Context) {
      super(param1Context);
    }
    
    public void onOrientationChanged(int param1Int) {
      param1Int = AutoCaptureService.this.mDisplay.getRotation();
      if (param1Int != AutoCaptureService.this.mRotation) {
        AutoCaptureService.access$1002(AutoCaptureService.this, param1Int);
        try {
          if (AutoCaptureService.this.mVirtualDisplay != null)
            AutoCaptureService.this.mVirtualDisplay.release(); 
          if (AutoCaptureService.this.mImageReader != null)
            AutoCaptureService.this.mImageReader.setOnImageAvailableListener(null, null); 
          AutoCaptureService.this.createVirtualDisplay();
          return;
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
      } 
    }
  }
}


/* Location:              D:\Apk_Decoder\dex2jar-2.0\classes2-dex2jar.jar!\com\infernodevelopers\autoshot\Services\AutoCaptureService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */