package com.example.yogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

public class Main_Activity extends AppCompatActivity implements OnMapReadyCallback, View.OnTouchListener {
  private int click_count = 0;
  
  private TextView cur_view;
  
  private Button map_changer;
  
  private NaverMap nMap;
  
  public void make_Marker(LatLng paramLatLng, String paramString) {
    Marker marker = new Marker();
    marker.setPosition(paramLatLng);
    marker.setMap(this.nMap);
    marker.setCaptionText(paramString);
    marker.setCaptionColor(Color.rgb(5, 0, 153));
    marker.setCaptionTextSize(15.0F);
    marker.setCaptionHaloColor(Color.rgb(209, 178, 255));
    marker.setOnClickListener(new _$$Lambda$Main_Activity$rP5meTfKhS1Dmi9Gt9HfD7toQWA(this, paramString));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427358);
    this.cur_view = (TextView)findViewById(2131230802);
    Button button = (Button)findViewById(2131230845);
    this.map_changer = button;
    button.setOnTouchListener(this);
    FragmentManager fragmentManager = getSupportFragmentManager();
    MapFragment mapFragment2 = (MapFragment)fragmentManager.findFragmentById(2131230844);
    MapFragment mapFragment1 = mapFragment2;
    if (mapFragment2 == null) {
      mapFragment1 = MapFragment.newInstance();
      fragmentManager.beginTransaction().add(2131230844, (Fragment)mapFragment1).commit();
    } 
    mapFragment1.getMapAsync(this);
  }
  
  public void onMapReady(NaverMap paramNaverMap) {
    this.nMap = paramNaverMap;
    paramNaverMap.setMapType(NaverMap.MapType.Basic);
    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.3732165D, 128.0266935D));
    this.nMap.moveCamera(cameraUpdate);
    cameraUpdate = CameraUpdate.zoomTo(6.0D);
    this.nMap.moveCamera(cameraUpdate);
    make_Marker(new LatLng(35.795045D, 129.349667D), "석굴암");
    make_Marker(new LatLng(36.538851D, 128.518169D), "하회마을");
    make_Marker(new LatLng(33.373946D, 126.536452D), "한라산국립공원");
    make_Marker(new LatLng(37.241347D, 131.866241D), "독도");
    make_Marker(new LatLng(35.801498D, 128.098643D), "해인사");
    make_Marker(new LatLng(35.335717D, 127.723002D), "지리산국립공원");
    make_Marker(new LatLng(35.54971D, 128.412099D), "우포늪");
    make_Marker(new LatLng(35.158683D, 129.160403D), "해운대");
    make_Marker(new LatLng(34.885946D, 127.508978D), "순천만습지");
    make_Marker(new LatLng(34.769249D, 128.712319D), "외도 보타니아");
    make_Marker(new LatLng(35.279076D, 127.307532D), "섬진강 기차마을");
    make_Marker(new LatLng(35.756429D, 127.395198D), "마이산");
    make_Marker(new LatLng(36.461372D, 127.112619D), "송산리 고분군");
    make_Marker(new LatLng(36.978729D, 128.323799D), "단양 팔경");
    make_Marker(new LatLng(37.32917D, 129.043436D), "대이리 동굴지대");
    make_Marker(new LatLng(36.387974D, 129.166684D), "주왕산국립공원");
    make_Marker(new LatLng(38.13916D, 128.407294D), "설악산국립공원");
    make_Marker(new LatLng(37.791244D, 127.525542D), "남이섬");
    make_Marker(new LatLng(37.705492D, 128.720695D), "하늘목장");
    make_Marker(new LatLng(37.288584D, 127.013889D), "수원화성");
    make_Marker(new LatLng(34.314786D, 126.518928D), "땅끝마을");
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 0) {
      this.map_changer.setBackgroundResource(2131165281);
    } else if (paramMotionEvent.getAction() == 1) {
      this.map_changer.setBackgroundResource(2131165282);
      int i = (this.click_count + 1) % 5;
      this.click_count = i;
      if (i != 0) {
        if (i != 1) {
          if (i != 2) {
            if (i != 3) {
              this.cur_view.setText(2131558404);
              this.nMap.setMapType(NaverMap.MapType.Terrain);
              this.map_changer.setText("Turn to\nBasic\nView");
            } else {
              this.cur_view.setText(2131558401);
              this.nMap.setMapType(NaverMap.MapType.Hybrid);
              this.map_changer.setText("Turn to\nTerrain\nView");
            } 
          } else {
            this.cur_view.setText(2131558403);
            this.nMap.setMapType(NaverMap.MapType.Satellite);
            this.map_changer.setText("Turn to\nHybrid\nView");
          } 
        } else {
          this.cur_view.setText(2131558402);
          this.nMap.setMapType(NaverMap.MapType.Navi);
          this.map_changer.setText("Turn to\nSatellite\nView");
        } 
      } else {
        this.cur_view.setText(2131558400);
        this.nMap.setMapType(NaverMap.MapType.Basic);
        this.map_changer.setText("Turn to\nNavigation\nView");
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\myeon\Desktop\yogin\classes2-dex2jar.jar!\com\example\yogin\Main_Activity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */