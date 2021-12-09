package com.example.yogin;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class DownloadfromWeb extends AsyncTask<String, Void, String> {
  private int cloudAmount;
  
  private Context context;
  
  private int dust = -1;
  
  private String dust_info;
  
  private boolean fail = false;
  
  private int fine_dust = -1;
  
  private boolean isRaining = false;
  
  private boolean isSnowing = false;
  
  private ImageView[] iv;
  
  private String separator;
  
  private String tmp_weather_info;
  
  private TextView[] tv;
  
  DownloadfromWeb(Context paramContext, TextView[] paramArrayOfTextView, ImageView[] paramArrayOfImageView) {
    this.context = paramContext;
    this.tv = paramArrayOfTextView;
    this.iv = paramArrayOfImageView;
  }
  
  private String get_Dust_info(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      URL uRL = new URL();
      this(paramString);
      HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
      BufferedInputStream bufferedInputStream = new BufferedInputStream();
      this(httpURLConnection.getInputStream());
      BufferedReader bufferedReader = new BufferedReader();
      InputStreamReader inputStreamReader = new InputStreamReader();
      this(bufferedInputStream, StandardCharsets.UTF_8);
      this(inputStreamReader);
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          stringBuilder.append(str);
          continue;
        } 
        httpURLConnection.disconnect();
        return stringBuilder.toString();
      } 
    } catch (UnknownHostException unknownHostException) {
      this.fail = true;
      (new Handler(Looper.getMainLooper())).postDelayed(new _$$Lambda$DownloadfromWeb$RGExsGs7fRzDg8Z0gKAtqYwvN4Y(this), 0L);
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    return stringBuilder.toString();
  }
  
  private String get_WeatherTmp_info(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      URL uRL = new URL();
      this(paramString);
      HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
      BufferedInputStream bufferedInputStream = new BufferedInputStream();
      this(httpURLConnection.getInputStream());
      BufferedReader bufferedReader = new BufferedReader();
      InputStreamReader inputStreamReader = new InputStreamReader();
      this(bufferedInputStream, StandardCharsets.UTF_8);
      this(inputStreamReader);
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          stringBuilder.append(str);
          continue;
        } 
        httpURLConnection.disconnect();
        return stringBuilder.toString();
      } 
    } catch (UnknownHostException unknownHostException) {
      this.fail = true;
      (new Handler(Looper.getMainLooper())).postDelayed(new _$$Lambda$DownloadfromWeb$a3N_Z5uyzvGe8IXHojiQHz5SFxM(this), 0L);
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    return stringBuilder.toString();
  }
  
  private void show_Dust_info() {
    try {
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParserFactory.setNamespaceAware(true);
      XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
      StringReader stringReader = new StringReader();
      this(this.dust_info);
      xmlPullParser.setInput(stringReader);
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      int i = xmlPullParser.getEventType();
      while (i != 1) {
        String str;
        boolean bool4;
        boolean bool5;
        boolean bool6;
        if (i == 2) {
          if (xmlPullParser.getName().equals("pm10Value") && bool1) {
            bool4 = true;
            bool5 = bool1;
            bool6 = bool3;
          } else {
            bool5 = bool1;
            bool4 = bool2;
            bool6 = bool3;
            if (xmlPullParser.getName().equals("pm25Value")) {
              bool5 = bool1;
              bool4 = bool2;
              bool6 = bool3;
              if (bool1) {
                bool6 = true;
                bool5 = bool1;
                bool4 = bool2;
              } 
            } 
          } 
        } else {
          bool5 = bool1;
          bool4 = bool2;
          bool6 = bool3;
          if (i == 4) {
            boolean bool = xmlPullParser.getText().equals(this.separator);
            if (bool) {
              bool5 = true;
              bool4 = bool2;
              bool6 = bool3;
            } else {
              if (bool2) {
                String str1 = xmlPullParser.getText();
                if (!str1.equals("-"))
                  this.dust = Integer.parseInt(str1); 
                this.tv[0].setText(str1);
                bool2 = false;
              } else if (bool3) {
                str = xmlPullParser.getText();
                if (!str.equals("-"))
                  this.fine_dust = Integer.parseInt(str); 
                this.tv[1].setText(str);
                break;
              } 
              bool5 = bool1;
              bool4 = bool2;
              bool6 = bool3;
            } 
          } 
        } 
        i = str.next();
        bool1 = bool5;
        bool2 = bool4;
        bool3 = bool6;
      } 
      if (this.dust == -1) {
        this.iv[0].setImageResource(2131165295);
        this.tv[0].setTextColor(Color.rgb(0, 0, 0));
      } else if (this.dust <= 15) {
        this.iv[0].setImageResource(2131165287);
        this.tv[0].setTextColor(Color.rgb(1, 0, 255));
      } else if (this.dust <= 30) {
        this.iv[0].setImageResource(2131165288);
        this.tv[0].setTextColor(Color.rgb(0, 84, 255));
      } else if (this.dust <= 40) {
        this.iv[0].setImageResource(2131165289);
        this.tv[0].setTextColor(Color.rgb(0, 216, 255));
      } else if (this.dust <= 50) {
        this.iv[0].setImageResource(2131165290);
        this.tv[0].setTextColor(Color.rgb(34, 116, 28));
      } else if (this.dust <= 75) {
        this.iv[0].setImageResource(2131165291);
        this.tv[0].setTextColor(Color.rgb(237, 169, 0));
      } else if (this.dust <= 100) {
        this.iv[0].setImageResource(2131165292);
        this.tv[0].setTextColor(Color.rgb(237, 76, 0));
      } else if (this.dust <= 150) {
        this.iv[0].setImageResource(2131165293);
        this.tv[0].setTextColor(Color.rgb(255, 0, 0));
      } else {
        this.iv[0].setImageResource(2131165294);
        this.tv[0].setTextColor(Color.rgb(0, 0, 0));
      } 
      if (this.fine_dust == -1) {
        this.iv[1].setImageResource(2131165295);
        this.tv[1].setTextColor(Color.rgb(0, 0, 0));
      } else if (this.fine_dust <= 8) {
        this.iv[1].setImageResource(2131165287);
        this.tv[1].setTextColor(Color.rgb(1, 0, 255));
      } else if (this.fine_dust <= 15) {
        this.iv[1].setImageResource(2131165288);
        this.tv[1].setTextColor(Color.rgb(0, 84, 255));
      } else if (this.fine_dust <= 20) {
        this.iv[1].setImageResource(2131165289);
        this.tv[1].setTextColor(Color.rgb(0, 216, 255));
      } else if (this.fine_dust <= 25) {
        this.iv[1].setImageResource(2131165290);
        this.tv[1].setTextColor(Color.rgb(34, 116, 28));
      } else if (this.fine_dust <= 37) {
        this.iv[1].setImageResource(2131165291);
        this.tv[1].setTextColor(Color.rgb(237, 169, 0));
      } else if (this.fine_dust <= 50) {
        this.iv[1].setImageResource(2131165292);
        this.tv[1].setTextColor(Color.rgb(237, 76, 0));
      } else if (this.fine_dust <= 75) {
        this.iv[1].setImageResource(2131165293);
        this.tv[1].setTextColor(Color.rgb(255, 0, 0));
      } else {
        this.iv[1].setImageResource(2131165294);
        this.tv[1].setTextColor(Color.rgb(0, 0, 0));
      } 
    } catch (Exception exception) {
      (new Handler(Looper.getMainLooper())).postDelayed(new _$$Lambda$DownloadfromWeb$K_jXFE_XhTuvkQzgKqhEaWQCBR4(this), 0L);
    } 
  }
  
  private void show_WeatherTmp_info() {
    try {
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParserFactory.setNamespaceAware(true);
      XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
      StringReader stringReader = new StringReader();
      this(this.tmp_weather_info);
      xmlPullParser.setInput(stringReader);
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      int i = 0;
      int j;
      for (j = xmlPullParser.getEventType(); j != 1; j = k) {
        boolean bool;
        if (j == 2) {
          String str = xmlPullParser.getName();
          j = -1;
          switch (str.hashCode()) {
            case 321701236:
              if (str.equals("temperature"))
                j = 0; 
              break;
            case 266601485:
              if (str.equals("rainfallDay"))
                j = 3; 
              break;
            case -1461198503:
              if (str.equals("newSnowDay"))
                j = 1; 
              break;
            case -2045714707:
              if (str.equals("cloudAmount"))
                j = 2; 
              break;
          } 
          if (j != 0) {
            if (j != 1) {
              if (j != 2) {
                if (j != 3) {
                  j = i;
                } else {
                  bool3 = true;
                  j = i;
                } 
              } else {
                bool2 = true;
                j = i;
              } 
            } else {
              j = 1;
            } 
          } else {
            bool1 = true;
            j = i;
          } 
          bool = bool3;
        } else if (j == 4) {
          if (bool1) {
            this.tv[2].setText(xmlPullParser.getText());
            bool1 = false;
            bool = bool3;
            j = i;
          } else if (bool2) {
            this.cloudAmount = Integer.parseInt(xmlPullParser.getText());
            bool2 = false;
            bool = bool3;
            j = i;
          } else if (i) {
            this.isSnowing = true;
            j = 0;
            bool = bool3;
          } else {
            bool = bool3;
            j = i;
            if (bool3) {
              this.isRaining = true;
              break;
            } 
          } 
        } else if (i) {
          j = 0;
          bool = bool3;
        } else {
          bool = bool3;
          j = i;
          if (bool3)
            break; 
        } 
        int k = xmlPullParser.next();
        bool3 = bool;
        i = j;
      } 
      if (this.isSnowing) {
        this.iv[2].setImageResource(2131165374);
      } else if (this.isRaining) {
        this.iv[2].setImageResource(2131165370);
      } else if (this.cloudAmount >= 0 && this.cloudAmount < 3) {
        this.iv[2].setImageResource(2131165377);
      } else if (this.cloudAmount >= 3 && this.cloudAmount < 6) {
        this.iv[2].setImageResource(2131165307);
      } else if (this.cloudAmount >= 6 && this.cloudAmount < 9) {
        this.iv[2].setImageResource(2131165309);
      } else {
        this.iv[2].setImageResource(2131165283);
      } 
    } catch (Exception exception) {
      (new Handler(Looper.getMainLooper())).postDelayed(new _$$Lambda$DownloadfromWeb$0jd7YaiLZQ_j41QtdD5cfvuXN_Q(this), 0L);
    } 
  }
  
  protected String doInBackground(String... paramVarArgs) {
    this.dust_info = get_Dust_info(paramVarArgs[0]);
    this.tmp_weather_info = get_WeatherTmp_info(paramVarArgs[1]);
    this.separator = paramVarArgs[2];
    return null;
  }
  
  protected void onPostExecute(String paramString) {
    if (!this.fail) {
      show_Dust_info();
      show_WeatherTmp_info();
    } 
  }
}


/* Location:              C:\Users\myeon\Desktop\yogin\classes2-dex2jar.jar!\com\example\yogin\DownloadfromWeb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */