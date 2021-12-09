package com.example.yogin;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Place extends AppCompatActivity {
  protected void onCreate(Bundle paramBundle) {
    StringBuilder stringBuilder8;
    String str7;
    StringBuilder stringBuilder7;
    String str6;
    StringBuilder stringBuilder6;
    String str5;
    StringBuilder stringBuilder5;
    String str4;
    StringBuilder stringBuilder4;
    String str3;
    StringBuilder stringBuilder3;
    String str2;
    StringBuilder stringBuilder2;
    String str1;
    String str16;
    StringBuilder stringBuilder15;
    String str15;
    StringBuilder stringBuilder14;
    String str14;
    StringBuilder stringBuilder13;
    String str13;
    StringBuilder stringBuilder12;
    String str12;
    StringBuilder stringBuilder11;
    String str11;
    StringBuilder stringBuilder10;
    String str10;
    StringBuilder stringBuilder9;
    byte b;
    super.onCreate(paramBundle);
    setContentView(2131427385);
    TextView textView1 = (TextView)findViewById(2131230945);
    TextView textView2 = (TextView)findViewById(2131230946);
    TextView textView3 = (TextView)findViewById(2131230948);
    ImageView imageView2 = (ImageView)findViewById(2131230834);
    ImageView imageView3 = (ImageView)findViewById(2131230835);
    ImageView imageView4 = (ImageView)findViewById(2131230837);
    LinearLayout linearLayout = (LinearLayout)findViewById(2131230838);
    TextView textView4 = (TextView)findViewById(2131230947);
    ImageView imageView1 = (ImageView)findViewById(2131230836);
    WebView webView = new WebView((Context)this);
    linearLayout.addView((View)webView);
    webView.setPadding(15, 5, 15, 0);
    webView.setBackgroundColor(0);
    webView.setFocusable(false);
    webView.setFocusableInTouchMode(false);
    webView.setVerticalScrollBarEnabled(false);
    webView.setHorizontalScrollBarEnabled(false);
    String str8 = getIntent().getStringExtra("지역");
    DownloadfromWeb downloadfromWeb = new DownloadfromWeb((Context)this, new TextView[] { textView1, textView2, textView3 }, new ImageView[] { imageView2, imageView3, imageView4 });
    switch (str8.hashCode()) {
      default:
        b = -1;
        break;
      case 1681372592:
        if (str8.equals("하회마을")) {
          b = 1;
          break;
        } 
      case 1672534620:
        if (str8.equals("하늘목장")) {
          b = 18;
          break;
        } 
      case 1530916629:
        if (str8.equals("수원화성")) {
          b = 19;
          break;
        } 
      case 1427364916:
        if (str8.equals("땅끝마을")) {
          b = 20;
          break;
        } 
      case 1316594146:
        if (str8.equals("주왕산국립공원")) {
          b = 15;
          break;
        } 
      case 875858607:
        if (str8.equals("지리산국립공원")) {
          b = 5;
          break;
        } 
      case 856078336:
        if (str8.equals("단양 팔경")) {
          b = 13;
          break;
        } 
      case 254723703:
        if (str8.equals("순천만습지")) {
          b = 8;
          break;
        } 
      case 54145192:
        if (str8.equals("해인사")) {
          b = 4;
          break;
        } 
      case 54135616:
        if (str8.equals("해운대")) {
          b = 7;
          break;
        } 
      case 50607854:
        if (str8.equals("우포늪")) {
          b = 6;
          break;
        } 
      case 48935997:
        if (str8.equals("석굴암")) {
          b = 0;
          break;
        } 
      case 47337348:
        if (str8.equals("마이산")) {
          b = 11;
          break;
        } 
      case 45092576:
        if (str8.equals("남이섬")) {
          b = 17;
          break;
        } 
      case 1472671:
        if (str8.equals("독도")) {
          b = 3;
          break;
        } 
      case -92619236:
        if (str8.equals("외도 보타니아")) {
          b = 9;
          break;
        } 
      case -513370359:
        if (str8.equals("송산리 고분군")) {
          b = 12;
          break;
        } 
      case -608513609:
        if (str8.equals("섬진강 기차마을")) {
          b = 10;
          break;
        } 
      case -1720543877:
        if (str8.equals("한라산국립공원")) {
          b = 2;
          break;
        } 
      case -1871531238:
        if (str8.equals("설악산국립공원")) {
          b = 16;
          break;
        } 
      case -1896414557:
        if (str8.equals("대이리 동굴지대")) {
          b = 14;
          break;
        } 
    } 
    switch (b) {
      default:
        return;
      case 20:
        textView4.setText(2131558494);
        str16 = getString(2131558456);
        stringBuilder8 = new StringBuilder();
        stringBuilder8.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder8.append(str16);
        stringBuilder8.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder8.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165378);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EC%A0%84%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=261&", "해남군" });
      case 19:
        str16.setText(2131558463);
        str7 = getString(2131558444);
        stringBuilder15 = new StringBuilder();
        stringBuilder15.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder15.append(str7);
        stringBuilder15.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder15.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165300);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EA%B8%B0&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=119&", "수원시" });
      case 18:
        stringBuilder15.setText(2131558489);
        str15 = getString(2131558453);
        stringBuilder7 = new StringBuilder();
        stringBuilder7.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder7.append(str15);
        stringBuilder7.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder7.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165373);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B0%95%EC%9B%90&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=100&", "평창군" });
      case 17:
        str15.setText(2131558467);
        str15 = getString(2131558448);
        stringBuilder7 = new StringBuilder();
        stringBuilder7.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder7.append(str15);
        stringBuilder7.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder7.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165310);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B0%95%EC%9B%90&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=101&", "춘천시" });
      case 16:
        str15.setText(2131558488);
        str6 = getString(2131558452);
        stringBuilder14 = new StringBuilder();
        stringBuilder14.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder14.append(str6);
        stringBuilder14.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder14.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165372);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B0%95%EC%9B%90&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=211&", "인제군" });
      case 15:
        stringBuilder14.setText(2131558465);
        str6 = getString(2131558446);
        stringBuilder14 = new StringBuilder();
        stringBuilder14.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder14.append(str6);
        stringBuilder14.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder14.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165304);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%B6%81&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=276&", "청송군" });
      case 14:
        stringBuilder14.setText(2131558433);
        str6 = getString(2131558437);
        stringBuilder14 = new StringBuilder();
        stringBuilder14.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder14.append(str6);
        stringBuilder14.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder14.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165284);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B0%95%EC%9B%90&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=106&", "삼척시" });
      case 13:
        stringBuilder14.setText(2131558434);
        str14 = getString(2131558438);
        stringBuilder6 = new StringBuilder();
        stringBuilder6.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder6.append(str14);
        stringBuilder6.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder6.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165285);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EC%B6%A9%EB%B6%81&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=221&", "단양군" });
      case 12:
        str14.setText(2131558490);
        str5 = getString(2131558454);
        stringBuilder13 = new StringBuilder();
        stringBuilder13.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder13.append(str5);
        stringBuilder13.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder13.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165375);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EC%B6%A9%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=133&", "공주시" });
      case 11:
        stringBuilder13.setText(2131558466);
        str13 = getString(2131558447);
        stringBuilder5 = new StringBuilder();
        stringBuilder5.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder5.append(str13);
        stringBuilder5.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder5.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165308);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EC%A0%84%EB%B6%81&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=244&", "진안군" });
      case 10:
        str13.setText(2131558495);
        str4 = getString(2131558457);
        stringBuilder12 = new StringBuilder();
        stringBuilder12.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder12.append(str4);
        stringBuilder12.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder12.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165381);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EC%A0%84%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=254&", "곡성군" });
      case 9:
        stringBuilder12.setText(2131558484);
        str4 = getString(2131558449);
        stringBuilder12 = new StringBuilder();
        stringBuilder12.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder12.append(str4);
        stringBuilder12.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder12.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165368);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=294&", "거제시" });
      case 8:
        stringBuilder12.setText(2131558492);
        str12 = getString(2131558455);
        stringBuilder4 = new StringBuilder();
        stringBuilder4.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder4.append(str12);
        stringBuilder4.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder4.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165376);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EC%A0%84%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=174&", "순천시" });
      case 7:
        str12.setText(2131558460);
        str3 = getString(2131558441);
        stringBuilder11 = new StringBuilder();
        stringBuilder11.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder11.append(str3);
        stringBuilder11.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder11.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165297);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EB%B6%80%EC%82%B0&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=159&", "해운대구" });
      case 6:
        stringBuilder11.setText(2131558485);
        str11 = getString(2131558450);
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder3.append(str11);
        stringBuilder3.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder3.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165369);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=285&", "창녕군" });
      case 5:
        str11.setText(2131558464);
        str2 = getString(2131558445);
        stringBuilder10 = new StringBuilder();
        stringBuilder10.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder10.append(str2);
        stringBuilder10.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder10.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165303);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=264&", "함양군" });
      case 4:
        stringBuilder10.setText(2131558459);
        str10 = getString(2131558440);
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder2.append(str10);
        stringBuilder2.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder2.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165296);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%82%A8&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=285&", "합천군" });
      case 3:
        str10.setText(2131558435);
        str1 = getString(2131558439);
        stringBuilder9 = new StringBuilder();
        stringBuilder9.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder9.append(str1);
        stringBuilder9.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder9.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165286);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%B6%81&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=115&", "울릉군" });
      case 2:
        stringBuilder9.setText(2131558462);
        str1 = getString(2131558443);
        stringBuilder9 = new StringBuilder();
        stringBuilder9.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder9.append(str1);
        stringBuilder9.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder9.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165299);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=10&pageNo=1&sidoName=%EC%A0%9C%EC%A3%BC&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=184&", "서귀포시" });
      case 1:
        stringBuilder9.setText(2131558461);
        str1 = getString(2131558442);
        stringBuilder9 = new StringBuilder();
        stringBuilder9.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
        stringBuilder9.append(str1);
        stringBuilder9.append("</body></html>");
        webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder9.toString(), "text/html;charset=UTF-8", "UTF-8", null);
        imageView1.setImageResource(2131165298);
        downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%B6%81&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=136&", "안동시" });
      case 0:
        break;
    } 
    stringBuilder9.setText(2131558487);
    String str9 = getString(2131558451);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("<html><style type='text/css'>@font-face {font-family:Bazzi; src:url('Bazzi.ttf');} body {font-family:Bazzi;}</style><body style=\"font-size:20px\" align=\"justify\">");
    stringBuilder1.append(str9);
    stringBuilder1.append("</body></html>");
    webView.loadDataWithBaseURL("file:///android_asset/fonts/", stringBuilder1.toString(), "text/html;charset=UTF-8", "UTF-8", null);
    imageView1.setImageResource(2131165371);
    downloadfromWeb.execute((Object[])new String[] { "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&numOfRows=100&pageNo=1&sidoName=%EA%B2%BD%EB%B6%81&searchCondition=HOUR&", "http://apis.data.go.kr/1360000/SfcInfoService/getWntyNcst?serviceKey=l5ri6rtwHIdjr%2BhO11KwG5UDB%2BkyKHAEUxSTI8GBsbf4J4NzeK9cZjJD8NBAZgz%2F61EP1Tla2fJhs5Oa2WAs1A%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=283&", "경주시" });
  }
}


/* Location:              C:\Users\myeon\Desktop\yogin\classes2-dex2jar.jar!\com\example\yogin\Place.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */