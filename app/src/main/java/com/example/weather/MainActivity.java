package com.example.weather;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class MainActivity extends AppCompatActivity {

    public LocationClient locationClient = null;
    public static  BaseRequestOptions optionsBlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        HeConfig.init("HE2205192124421095", "75901f8fc05649029011d79f8571f871");
        HeConfig.switchToDevService();
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
        optionsBlur = new RequestOptions().transform(new BlurTransformation(5, 35));

        try {
            test();
            //请求疫情数据并把数据写入内部存储的files的test.json文件中
        } catch (IOException e ) {
            e.printStackTrace();
        }

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
                                //打开天气显示界面
                                //startWeatherActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startWeatherActivity(intent);
                                    }
                                },1000);
                            } else {
                                for (int i = 0; i < locationBeanList.size(); i++) {
                                    if (bdLocation.getCity().equals(locationBeanList.get(i).getAdm2())) {
                                        String weatherId = locationBeanList.get(i).getId();
                                        Intent intent = new Intent(MainActivity.this,WeatherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("weatherId",weatherId);
                                        locationClient.stop();//停止位置服务
                                        //startWeatherActivity(intent);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startWeatherActivity(intent);
                                            }
                                        },1000);
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
    }

    private void startWeatherActivity(Intent intent) {
        startActivity(intent);
    }


    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    List<String> mPermissionList = new ArrayList<>();

    // private ImageView welcomeImg = null;
    private static final int PERMISSION_REQUEST = 1;
// 检查权限

    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
            if(ContextCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_DENIED){
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }
    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults != null && grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以做你要做的事情了。
                } else {
                    // 权限被用户拒绝了，可以提示用户,关闭界面等等。
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    System.exit(0);
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }



    public void test() throws IOException {
        //根据阿里API的curl命令来请求数据
        //发送请求的java代码为某个在线转换工具根据curl命令转换过来的
        //原来的curl命令为：curl -i -k --get --include 'https://ncovdata.market.alicloudapi.com/ncov/cityDiseaseInfoWithTrend'  -H 'Authorization:APPCODE 972fc105b0644a17a653153e70260dab'
        //发送http请求不能写在主线程里，所以写在了这个子线程里  https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list?modules=localCityNCOVDataList,diseaseh5Shelf
        new Thread() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list?modules=localCityNCOVDataList,diseaseh5Shelf");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection http = null;
                try {
                    http = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                http.setRequestProperty("Authorization", "APPCODE 972fc105b0644a17a653153e70260dab");
                String response = null;
                try {
                    response = http.getResponseCode() + " " + http.getResponseMessage();
                    int code = http.getResponseCode();
                    // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                    String msg = "";
                    if (code == 200) { // 正常响应
                        // 从流中读取响应信息
                        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) { // 循环从流中读取
                            msg += line + "\n";
                        }
                        reader.close(); // 关闭流
                        String str = msg;
                        System.out.println(msg);
                        //请求得到的数据存在msg里面

                        //写入内部存储files的test.json
                        FileOutputStream fos = null;
                        BufferedWriter writer = null;
                        try {
                            fos = openFileOutput("test.json", Context.MODE_PRIVATE);
                            writer = new BufferedWriter(new OutputStreamWriter(fos));
                            try {
                                writer.write(str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            if (writer != null) {
                                try {
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
                http.disconnect();
            }
        }.start();

    }

}