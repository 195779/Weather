package com.example.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weather.util.HttpUtil;
import com.google.gson.Gson;
import com.qweather.sdk.bean.Basic;
import com.qweather.sdk.bean.air.AirDailyBean;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.concurrent.Task;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LocationClient locationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeConfig.init("HE2205192124421095", "75901f8fc05649029011d79f8571f871");
        HeConfig.switchToDevService();
        try {
            LocationClient.setAgreePrivacy(true);
            locationClient = new LocationClient(this);
            LocationClientOption option = locationClient.getLocOption();
            option.setIsNeedLocationDescribe(true);
            //可选，是否需要位置描述信息，默认为不需要，即参数为false
            //如果开发者需要获得当前点的位置信息，此处必须为true
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            option.setIsNeedAddress(true);
            option.setAddrType("all");
            locationClient.setLocOption(option);
            //mLocationClient为第二步初始化过的LocationClient对象
            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            //注册位置监听器
            locationClient.registerLocationListener(new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    String district  = bdLocation.getDistrict();
                    QWeather.getGeoCityLookup(MainActivity.this, district, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
                        @Override
                        public void onError(Throwable throwable) {
                        }
                        @Override
                        public void onSuccess(GeoBean geoBean) {
                            List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                            if (locationBeanList.size() == 1) {
                                String weatherId = locationBeanList.get(0).getId();
                                Intent intent = new Intent(MainActivity.this,WeatherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("weatherId",weatherId);
                                locationClient.stop();//停止位置服务
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startWeatherActivity(intent);
                                    }
                                },1200);
                            } else {
                                for (int i = 0; i < locationBeanList.size(); i++) {
                                    if (bdLocation.getCity().equals(locationBeanList.get(i).getAdm2())) {
                                        String weatherId = locationBeanList.get(i).getId();
                                        Intent intent = new Intent(MainActivity.this,WeatherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("weatherId",weatherId);
                                        locationClient.stop();//停止位置服务
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startWeatherActivity(intent);
                                            }
                                        },2000);
                                    }
                                }
                            }
                        }
                    });
                }
            });
            locationClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locationClient.stop();
    }
    @Override
    public void onClick(View view) {

    }
    private void startWeatherActivity(Intent intent){
        startActivity(intent);
    }


}