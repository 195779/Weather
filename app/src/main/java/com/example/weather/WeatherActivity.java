package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.weather.db.Weather;
import com.example.weather.gson.TencentData;
import com.example.weather.util.HttpUtil;
import com.google.gson.Gson;
import com.kwai.opensdk.sdk.constants.KwaiPlatform;
import com.mob.MobSDK;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.WarningBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    LocationClient locationClient = null;

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView pm10Text;

    private TextView pm25Text;

    private TextView SO2Text;

    private TextView NO2Text;

    private TextView COText;

    private TextView O3Text;

    private TextView qualityText;

    private TextView show_more_aqi;

    private TextView primaryText;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    private ImageView Now_weather_image;

    private TextView Warnning_text;

    private ImageView shezhi;

    public String thisWeatherId;
    //给每个界面一个固定weatherID，更新天气的时候依据这个ID更新界面数据
    //返回本地的时候，更新这个ID，再使用这个ID更新界面数据


    private TextView CityName_yiqing;
    private TextView city_2;
    private TextView yiqing_confirm_add;
    private TextView yiqing_wz_add;
    private TextView yiqing_total_confirm;
    private TextView yiqing_confirm;
    private TextView yiqing_medium;
    private TextView yiqing_height;
    private TextView show_more_yiqing;

    private TextView show_more_suggestion;



    private MyPagerAdapter_new myPagerAdapter_new;
    private ViewPager viewPager;
    private LinearLayout viewGroup;
    private ArrayList<View> viewArrayList = new ArrayList<>();


    public WeatherActivity() {
    }



    List<String> weatherIdList = new ArrayList<>();
    List<Weather> weatherList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
        context = this;
        manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        viewPager = findViewById(R.id.viewPager);
        viewGroup = findViewById(R.id.viewGroup);
        thisWeatherId = getIntent().getStringExtra("weatherId");
        if(!thisWeatherId.isEmpty())
        {weatherIdList.add(thisWeatherId);}
        weatherList = LitePal.findAll(Weather.class);
        for(Weather weather : weatherList){
            if(!weather.getWeatherId().equals(thisWeatherId)){
                weatherIdList.add(weather.getWeatherId());
            }
        }
        for(String weatherId : weatherIdList){
            @SuppressLint("InflateParams") View carousel_weather = LayoutInflater.from(this).inflate(R.layout.activity_weather, null);
            viewArrayList.add(carousel_weather);
        }
        //将点点的图添加到视图ViewGroup中
        for (int i = 0; i < viewArrayList.size(); i++) {
            ImageView imageView = new ImageView(this);
            //设置图片的宽高 为10像素
            imageView.setLayoutParams(new ViewGroup.LayoutParams(2, 2));
            if (i == 0) {
                //第一个为默认选中状态
                imageView.setImageResource(R.drawable.img_2);
            } else {
                imageView.setImageResource(R.drawable.img_3);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            viewGroup.addView(imageView, params);
        }
        myPagerAdapter_new = new MyPagerAdapter_new(viewArrayList);
        //设置缓存页数
        //viewPager.setOffscreenPageLimit(viewArrayList.size() - 1);
        viewPager.setAdapter(myPagerAdapter_new);
        //添加页面更改监听器
        viewPager.addOnPageChangeListener(onPageChangeListener);

        ImageView title_image = (ImageView) findViewById(R.id.title_image);
        BaseRequestOptions optionsBlur = new RequestOptions().transform(new BlurTransformation(5, 35));
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(title_image);
        for(int i=0;i<viewArrayList.size();i++){
            View view = viewArrayList.get(i);
            ImageView forecast_image = (ImageView) view.findViewById(R.id.forecast_image);
            ImageView now_image = (ImageView) view.findViewById(R.id.now_image);
            ImageView aqi_image = (ImageView) view.findViewById(R.id.aqi_image);
            ImageView suggestion = (ImageView) view.findViewById(R.id.suggestion_image);
            ImageView yiqing_image = (ImageView) view.findViewById(R.id.YiQing_image);
            //图像模糊化处理Glide
            Glide.with(this).load(getDrawable(R.drawable.ic_home))
                    .apply(MainActivity.optionsBlur)
                    .into(forecast_image);
            Glide.with(this).load(getDrawable(R.drawable.ic_home))
                    .apply(MainActivity.optionsBlur)
                    .into(now_image);
            Glide.with(this).load(getDrawable(R.drawable.ic_home))
                    .apply(MainActivity.optionsBlur)
                    .into(suggestion);
            Glide.with(this).load(getDrawable(R.drawable.ic_home))
                    .apply(MainActivity.optionsBlur)
                    .into(aqi_image);
            Glide.with(this).load(getDrawable(R.drawable.ic_home))
                    .apply(MainActivity.optionsBlur)
                    .into(yiqing_image);
        }

        shezhi = (ImageView) findViewById(R.id.image_shezhi);
        titleCity = (TextView) findViewById(R.id.title_city);
        navButton = (Button) findViewById(R.id.nav_button);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_test);
        //有问题
        titleCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drawerLayout.openDrawer(Gravity.LEFT);
                //点击城市名称，弹出左滑窗口选择城市
                Intent intent = new Intent(WeatherActivity.this,ChooseArea.class);
                startActivity(intent);
                //drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        MobSDK.submitPolicyGrantResult(true, null);
        //设置的按键
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(shezhi);
            }
        });
        //调用百度定位SDK的方法，获取设备当前的位置信息并根据当前位置所在城市的weatherID来更新天气显示界面
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LocationClient.setAgreePrivacy(true);
                    locationClient = new LocationClient(WeatherActivity.this);
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
                            QWeather.getGeoCityLookup(WeatherActivity.this, district, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
                                @Override
                                public void onError(Throwable throwable) {
                                }
                                @Override
                                public void onSuccess(GeoBean geoBean) {
                                    List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                                    if (locationBeanList.size() == 1) {
                                        String weatherId = locationBeanList.get(0).getId();
                                        //Intent intent = new Intent();
                                        //Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        show_Weather_List(weatherId);
                                        thisWeatherId = weatherId;
                                        locationClient.stop();
                                        //intent.putExtra("weatherId", weatherId);
                                        //startActivity(intent);
                                    } else {
                                        for (int i = 0; i < locationBeanList.size(); i++) {
                                            if (bdLocation.getCity().equals(locationBeanList.get(i).getAdm2())) {
                                                String weatherId = locationBeanList.get(i).getId();
                                                //Intent intent = new Intent();
                                                //Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                //intent.putExtra("weatherId", weatherId);
                                                //String mWeatherId = intent.getStringExtra("weatherId");
                                                show_Weather_List(weatherId);
                                                thisWeatherId = weatherId;
                                                locationClient.stop();
                                               // startActivity(intent);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    });
                    if(!locationClient.isStarted())
                    {
                        locationClient.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        setView(thisWeatherId,0);

    }



    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //当新页面选中时调用此方法，position 为新选中页面的位置索引
            //在所选页面的时候,点点图片也要发生变化
            setImageBackground(position);
            String weatherId = weatherIdList.get(position);
            setView(weatherId,position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 改变点点点的切换效果
     *
     * @param selectItem 当前选中的页面索引
     */
    private void setImageBackground(int selectItem) {
        for (int i = 0; i < viewArrayList.size(); i++) {
            ImageView imageView = (ImageView) viewGroup.getChildAt(i);
            imageView.setBackground(null);
            if (i == selectItem) {
                imageView.setImageResource(R.drawable.img_2);
            } else {
                imageView.setImageResource(R.drawable.img_3);
            }
        }
    }

    private void setView(String weatherId,int position){
        View view = viewArrayList.get(position);
        thisWeatherId = weatherId;
        bingPicImg = (ImageView) view.findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) view.findViewById(R.id.weather_layout);
        degreeText = (TextView) view.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) view.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        pm10Text = (TextView) view.findViewById(R.id.pm10_text);
        pm25Text = (TextView) view.findViewById(R.id.pm25_text);
        SO2Text = (TextView) view.findViewById(R.id.SO2_text);
        NO2Text = (TextView) view.findViewById(R.id.NO2_text);
        COText = (TextView) view.findViewById(R.id.CO_text);
        O3Text = (TextView) view.findViewById(R.id.O3_text);
        qualityText = (TextView) view.findViewById(R.id.quality_text);
        primaryText = (TextView) view.findViewById(R.id.primary_text);
        show_more_aqi = (TextView) view.findViewById(R.id.show_more_aqi);
        comfortText = (TextView) view.findViewById(R.id.comfort_text);
        carWashText = (TextView) view.findViewById(R.id.car_wash_text);
        sportText = (TextView) view.findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        Now_weather_image = (ImageView) view.findViewById(R.id.Now_weather_image);
        Warnning_text = (TextView) view.findViewById(R.id.Warnning_text);



        CityName_yiqing = (TextView) view.findViewById(R.id.City);
        city_2 = (TextView) view.findViewById(R.id.city_2);
        yiqing_confirm_add = (TextView) view.findViewById(R.id.YiQing_Head_text_confirm_add);
        yiqing_wz_add = (TextView) view.findViewById(R.id.YiQing_Head_text_wz_add);
        yiqing_confirm = (TextView) view.findViewById(R.id.YiQing_Head_text_confirm);
        yiqing_height = (TextView) view.findViewById(R.id.YiQing_Head_text_Height);
        yiqing_total_confirm = (TextView) view.findViewById(R.id.YiQing_Head_text_total_confirm);
        yiqing_medium = (TextView) view.findViewById(R.id.YiQing_Head_text_Medium);
        show_more_yiqing = (TextView) view.findViewById(R.id.show_more_yiqing);
        show_more_suggestion = (TextView) view.findViewById(R.id.show_more_suggestion);



        show_Weather_List(weatherId);


        loadBingPic();
        bingPicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新壁纸
                loadBingPic();
            }
        });


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉界面，请求天气信息，更新天气信息
                show_Weather_List(weatherId);
                loadBingPic();
                //更新之后隐藏下拉刷新的进度条
                swipeRefresh.setRefreshing(false);
            }
        });

        //跳转浏览器显示更多天气信息
        show_more_aqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QWeather.getGeoCityLookup(WeatherActivity.this, weatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                        if(locationBeanList.size()==1) {
                            String name =  locationBeanList.get(0).getName();
                            String mWeatherid = locationBeanList.get(0).getId();
                            String sonOfUri = "https://www.qweather.com/air/" + name.toLowerCase() + "-" + mWeatherid + ".html";
                            Uri uri = Uri.parse(sonOfUri);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        //点击跳转浏览器显示更多生活建议
        show_more_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QWeather.getGeoCityLookup(WeatherActivity.this, weatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                        if(locationBeanList.size()==1) {
                            String name =  locationBeanList.get(0).getName();
                            String mWeatherid = locationBeanList.get(0).getId();
                            String sonOfUri = "https://www.qweather.com/indices/" + name.toLowerCase() + "-" + mWeatherid + ".html";
                            Uri uri = Uri.parse(sonOfUri);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        //点击预警信息跳转浏览器显示该城市详细预警信息
        Warnning_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QWeather.getGeoCityLookup(WeatherActivity.this, weatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                        if (locationBeanList.size() == 1) {
                            String name = locationBeanList.get(0).getName();
                            String mWeatherid = locationBeanList.get(0).getId();
                            String sonOfUri = "https://www.qweather.com/severe-weather/" + name.toLowerCase() + "-" + mWeatherid + ".html";
                            Uri uri = Uri.parse(sonOfUri);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }


    private Activity context;
    ClipboardManager manager;
    public String getCopyString(){
        ClipData clipData = manager.getPrimaryClip();
        if(clipData != null && clipData.getItemCount() > 0 ){
            return clipData.getItemAt(0).getText().toString();
        }
        return null;
    }

    //点击设置之后弹出的菜单显示
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.shezhi, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                if(item.getTitle().equals("添加为常用城市")){
                    String weatherId = thisWeatherId;
                    String ccName = titleCity.getText().toString();
                    String text = degreeText.getText().toString();
                    text += " ¦ " + weatherInfoText.getText().toString().substring(0,2);
                    Weather weather = new Weather();
                    weather.setCCName(ccName);
                    weather.setWeatherId(weatherId);
                    weather.setText(text);
                    weather.saveOrUpdate("weatherid=?",weatherId);
                    Toast.makeText(WeatherActivity.this,ccName + "已添加为常用城市",Toast.LENGTH_SHORT).show();
                }
                if(item.getTitle().equals("管理常用城市")){
                    //点击管理常用城市之后，更新并显示常用城市的天气情况
                    updateWeather();
                }
                if(item.getTitle().equals("分享")){
                    /**
                     * 用于一键分享
                     */
                    String text = titleCity.getText().toString() + "的温度为："+degreeText.getText().toString() +"\n" + "天气情况为：" +  weatherInfoText.getText().toString() + "\n预警信息为：" + Warnning_text.getText().toString() ;
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    manager.setPrimaryClip(mClipData);
                    Toast.makeText(WeatherActivity.this, "天气信息已复制到剪贴板，内容为：\n"+   getCopyString(),Toast.LENGTH_LONG).show();
                    MobSDK.submitPolicyGrantResult(true, null);
                    final OnekeyShare oks = new OnekeyShare();
                    // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                    oks.setTitle("标题");
                    // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
                    oks.setTitleUrl("http://sharesdk.cn");
                    // text是分享文本，所有平台都需要这个字段
                    oks.setText("我是分享文本");
                    //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                    oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
                    // url仅在微信（包括好友和朋友圈）中使用
                    oks.setUrl("http://sharesdk.cn");
                    //分享回调
                    // 启动分享
                    oks.show(MobSDK.getContext());
                }
                if(item.getTitle().equals("保存图片")){

                }
                return false;
            }
        });
        popupMenu.show();
    }



    //显示全部天气+疫情信息
    public void show_Weather_List(String mWeatherId){
        //显示城市名称
        QWeather.getGeoCityLookup(this, mWeatherId, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(GeoBean geoBean) {
                List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                if(locationBeanList.size()==1) {
                    titleCity.setTextColor(android.graphics.Color.rgb(0,0,0));
                    titleCity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    titleCity.setText(locationBeanList.get(0).getAdm2() + "/" + locationBeanList.get(0).getName());
                    CityName_yiqing.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    CityName_yiqing.setTextColor(android.graphics.Color.rgb(255,255,255));
                    CityName_yiqing.setText(locationBeanList.get(0).getAdm2());
                    city_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    show_YiQing(locationBeanList.get(0).getAdm2(),locationBeanList.get(0).getAdm1());
                }
            }
        });


        //显示当前今天的天气信息
        QWeather.getWeatherNow(this, mWeatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onSuccess(WeatherNowBean weatherNowBean) {
                degreeText.setText(weatherNowBean.getNow().getTemp() + "℃");
                switch (weatherNowBean.getNow().getText()){
                    case "多云": {
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_cloudy));
                        break;
                    }
                    case "晴": {
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_sun));
                        break;
                    }
                    case "阴": {
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_overcast));
                        break;
                    }
                    case "阵雨": {
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_showerrain));
                        break;
                    }
                    case "雷阵雨" :{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_thundershower));
                        break;
                    }
                    case "小雨":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_ligthrain));
                        break;
                    }
                    case "中雨":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_moderaterain));
                        break;
                    }
                    case "大雨":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavyrain));
                        break;
                    }
                    case "暴雨":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_stormrian));
                        break;
                    }
                    case "大暴雨":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavystorm));
                        break;
                    }
                    case "雨夹雪":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_snow_rain));
                        break;
                    }
                    case "晴间多云":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_sun_cloud));
                        break;
                    }
                    case "小雪": {
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_ligthsnow));
                        break;
                    }
                    case "中雪":{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_moderatesnow));
                        break;
                    }
                    case "大雪" :{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavysnow));
                        break;
                    }
                    case "暴雪" :{
                        Now_weather_image.setBackground(getResources().getDrawable(R.drawable.now_weather_image_snowstorm));
                        break;
                    }
                    default:break;
                }
                weatherInfoText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                weatherInfoText.setText(weatherNowBean.getNow().getText() + " ¦ " + weatherNowBean.getNow().getWindDir()+ weatherNowBean.getNow().getWindScale() + "级" + " ¦ " + "相对湿度：" + weatherNowBean.getNow().getHumidity() + " ¦ " + "体感：" +weatherNowBean.getNow().getFeelsLike() + "℃");
            }
        });

        //七天天气预报显示，点击每行，都去跳转浏览器显示详细的天气信息
        QWeather.getWeather7D(this, mWeatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                List<WeatherDailyBean.DailyBean> dailyBeanList = weatherDailyBean.getDaily();
                forecastLayout.removeAllViews();
                //清空所有子view，防止手动下滑更新的时候再次调用此函数时造成七天的天气数据被变成N*7条七天的数据
                for (int i=0;i<dailyBeanList.size();i++){
                    View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, forecastLayout, false);
                    TextView dateText = (TextView) view.findViewById(R.id.date_text);
                    TextView infoText = (TextView) view.findViewById(R.id.info_text);
                    TextView maxText = (TextView) view.findViewById(R.id.max_text);
                    TextView minText = (TextView) view.findViewById(R.id.min_text);
                    ImageView imageView_forecast = (ImageView)view.findViewById(R.id.image_view_forecast_item);
                    switch (dailyBeanList.get(i).getTextDay()){
                        case "多云": {
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_cloudy));
                            break;
                        }
                        case "晴": {
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_sun));
                            break;
                        }
                        case "阴": {
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_overcast));
                            break;
                        }
                        case "阵雨": {
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_showerrain));
                            break;
                        }
                        case "雷阵雨" :{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_thundershower));
                            break;
                        }
                        case "小雨":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_ligthrain));
                            break;
                        }
                        case "中雨":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_moderaterain));
                            break;
                        }
                        case "大雨":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavyrain));
                            break;
                        }
                        case "暴雨":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_stormrian));
                            break;
                        }
                        case "大暴雨":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavystorm));
                            break;
                        }
                        case "雨夹雪":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_snow_rain));
                            break;
                        }
                        case "晴间多云":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_sun_cloud));
                            break;
                        }
                        case "小雪": {
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_ligthsnow));
                            break;
                        }
                        case "中雪":{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_moderatesnow));
                            break;
                        }
                        case "大雪" :{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavysnow));
                            break;
                        }
                        case "暴雪" :{
                            imageView_forecast.setBackground(getResources().getDrawable(R.drawable.now_weather_image_snowstorm));
                            break;
                        }
                        default:break;
                    }
                    ImageView imageView_forecast2 = (ImageView)view.findViewById(R.id.image_view_forecast_item2);
                    switch (dailyBeanList.get(i).getTextNight()){
                        case "多云": {
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_cloudy));
                            break;
                        }
                        case "晴": {
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_sun));
                            break;
                        }
                        case "阴": {
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_overcast));
                            break;
                        }
                        case "阵雨": {
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_showerrain));
                            break;
                        }
                        case "雷阵雨" :{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_thundershower));
                            break;
                        }
                        case "小雨":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_ligthrain));
                            break;
                        }
                        case "中雨":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_moderaterain));
                            break;
                        }
                        case "大雨":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavyrain));
                            break;
                        }
                        case "暴雨":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_stormrian));
                            break;
                        }
                        case "大暴雨":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavystorm));
                            break;
                        }
                        case "雨夹雪":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_snow_rain));
                            break;
                        }
                        case "晴间多云":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_sun_cloud));
                            break;
                        }
                        case "小雪": {
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_ligthsnow));
                            break;
                        }
                        case "中雪":{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_moderatesnow));
                            break;
                        }
                        case "大雪" :{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_heavysnow));
                            break;
                        }
                        case "暴雪" :{
                            imageView_forecast2.setBackground(getResources().getDrawable(R.drawable.now_weather_image_snowstorm));
                            break;
                        }
                        default:break;
                    }
                    dateText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    dateText.setText(dailyBeanList.get(i).getFxDate());
                    infoText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    infoText.setText(" / ");
                    maxText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    maxText.setText(dailyBeanList.get(i).getTempMax()+"℃");
                    minText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    minText.setText(dailyBeanList.get(i).getTempMin()+"℃");
                    if(!dateText.getText().toString().isEmpty()){
                        view.setOnClickListener(new View.OnClickListener() {
                            //点击七天预报的每行，打开浏览器，显示该城市30天详细天气预报界面
                            @Override
                            public void onClick(View view) {
                                QWeather.getGeoCityLookup(WeatherActivity.this, mWeatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
                                    @Override
                                    public void onError(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(GeoBean geoBean) {
                                        List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                                        if(locationBeanList.size()==1) {
                                            String name =  locationBeanList.get(0).getName();
                                            String mWeatherid = locationBeanList.get(0).getId();
                                            String sonOfUri = "https://www.qweather.com/weather30d/" + name.toLowerCase() + "-" + mWeatherid + ".html";
                                            Uri uri = Uri.parse(sonOfUri);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        Log.e("onError","onSuccess in 7D is onError!!!");
                    }
                    forecastLayout.addView(view);
                }
            }
        });

        //空气指数显示
        QWeather.getAirNow(this, mWeatherId, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                AirNowBean.NowBean nowBean = airNowBean.getNow();
                pm10Text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pm10Text.setText(nowBean.getPm10());
                pm25Text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pm25Text.setText(nowBean.getPm2p5());
                SO2Text.setText(nowBean.getSo2());
                NO2Text.setText(nowBean.getNo2());
                COText.setText(nowBean.getCo());
                O3Text.setText(nowBean.getO3());
                qualityText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                qualityText.setText(nowBean.getCategory());
                primaryText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                //Toast.makeText(WeatherActivity.this,nowBean.getPrimary(),Toast.LENGTH_SHORT).show();
                if(nowBean.getPrimary().equals("NA")){
                    primaryText.setText("无");
                }else{
                    primaryText.setText(nowBean.getPrimary());
                }
            }
        });

        //生活指数显示
        List<IndicesType> indicesTypeList = new ArrayList<>();
        indicesTypeList.add(IndicesType.SPT);
        indicesTypeList.add(IndicesType.CW);
        indicesTypeList.add(IndicesType.COMF);
        QWeather.getIndices1D(this,mWeatherId,Lang.ZH_HANS,indicesTypeList,new QWeather.OnResultIndicesListener(){

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onSuccess(IndicesBean indicesBean) {
                List<IndicesBean.DailyBean> dailyBeanList =  indicesBean.getDailyList();
                sportText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                sportText.setText(dailyBeanList.get(0).getText());
                carWashText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                carWashText.setText(dailyBeanList.get(1).getText());
                comfortText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                comfortText.setText(dailyBeanList.get(2).getText());
            }
        });


        //预警信息显示
        QWeather.getWarning(this, thisWeatherId, Lang.ZH_HANS, new QWeather.OnResultWarningListener() {
            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onSuccess(WarningBean warningBean) {
                List<WarningBean.WarningBeanBase> warningBeanBaseList = warningBean.getWarningList();
                String warnningText = "";
                for (int i = 0; i < warningBeanBaseList.size(); i++) {
                    warnningText += warningBeanBaseList.get(i).getTitle();
                    warnningText += "\n";
                }
                Warnning_text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                Warnning_text.setTextColor(android.graphics.Color.rgb(255,255,255));
                if (warnningText.isEmpty()) {
                    Warnning_text.setText("暂无预警信息\n");
                } else {
                    Warnning_text.setText(warnningText);
                }
            }
        });
    }



     //加载必应每日一图

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic)
                                .into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void updateWeather(){
        //取出weather表的全部元素之后，逐个遍历，根据每个weather元素的weatherid去获取该id的城市的天气信息
        // 将天气信息封装进一个新的weather对象，然后存入weather表中（saveOrUpdate : 没有这个id的话存入，有的话更新这个id的元素的全部信息）
        // 当然了这里相当于只用到了update
        List<Weather> new_WeatherList = LitePal.findAll(Weather.class);
        if(new_WeatherList.size()>0){
            for (Weather weather : new_WeatherList) {
                String meatherId = weather.getWeatherId();
                QWeather.getWeatherNow(this, meatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(WeatherNowBean weatherNowBean) {
                        Weather weather1 = new Weather();
                        weather1.setText(weatherNowBean.getNow().getTemp() + "℃" + " ¦ " + weatherNowBean.getNow().getText());
                        weather1.setCCName(weather.getCCName());
                        weather1.setWeatherId(meatherId);
                        weather1.saveOrUpdate("weatherId=?",meatherId);
                    }
                });
            }
        }
        //更新fragment的view显示
        //ManageFragment manageFragment = new ManageFragment();
       // manageFragment.qureyWeather_manage();
        //打开右侧的fragment显示常用城市界面
        //Intent intent = new Intent(WeatherActivity.this,show_manage_activity.class);
        //startActivity(intent);
        ManageFragment.qureyWeather_manage();
        drawerLayout.openDrawer(Gravity.RIGHT);
    }


    protected String load() {
        //读取test.json
        StringBuilder content = new StringBuilder();
        FileInputStream in = null;
        BufferedReader reader = null;
        try {
            in = openFileInput("test.json");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }

    protected void show_YiQing(String cityName,String provinceName){
        //显示疫情信息
        //读出test.json中的数据显示
        //textView.setText(load());
        Gson gson = new Gson();
        //将json解析对象,之后需要什么就从实例拿什么
        TencentData tencentData = gson.fromJson(load(), TencentData.class);
        try {
            System.out.println(tencentData.getRet());
        } catch (Exception e) {
            Log.d("Error", "空指针异常了兄弟！！");
            e.printStackTrace();
        }
        //TencentData tencentData对象中拿出DataDTO集合
        TencentData.DataDTO dataDTO = tencentData.getData();
          //类目录
           // TencentData
            //public static class DataDTO {
            //private Diseaseh5ShelfDTO diseaseh5Shelf;                       //DataDTO 的一个内部静态类
            //private List<LocalCityNCOVDataListDTO> localCityNCOVDataList;   //DataDTO 的一个内部静态类数组

        TencentData.DataDTO.Diseaseh5ShelfDTO diseaseh5ShelfDTO = dataDTO.getDiseaseh5Shelf();
        //取出LocalCityNCOVDataListDTO
        List<TencentData.DataDTO.LocalCityNCOVDataListDTO> localCityNCOVDataListDTOS = dataDTO.getLocalCityNCOVDataList();
        //输出list例
        //printlocalCityNCOVDataListDTOS(localCityNCOVDataListDTOS);
        //取出AreaTreeDTO
        List<TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO> areaTreeDTOList = diseaseh5ShelfDTO.getAreaTree();
        //输出例子
        printDiseaseh5ShelfDTO(areaTreeDTOList,cityName,provinceName);
        Log.d("System.out", "MAIN结束");
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


    private void printDiseaseh5ShelfDTO(List<TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO> areaTreeDTOList,String cityName,String provinceName) {
        //取出
//        从嵌套数组中取出数组ChildrenDTO
//        areaTreeDTOList里只有一个元素
        for (TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO areaTreeDTO : areaTreeDTOList) {
            List<TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO> childrenDTOList = areaTreeDTO.getChildren();
            if(provinceName.length() > 1){
                provinceName = provinceName.substring(0,provinceName.length() - 1);
            }
            yiqing_confirm_add.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            yiqing_wz_add.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            yiqing_confirm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            yiqing_total_confirm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            yiqing_medium.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            yiqing_height.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            show_more_yiqing.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            show_more_yiqing.setTextColor(android.graphics.Color.rgb(255,255,255));
            if(cityName.equals("北京")||cityName.equals("上海")||cityName.equals("重庆")||cityName.equals("天津")||cityName.equals("香港")||cityName.equals("澳门"))
            {
                show_more_yiqing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(cityName.equals("台湾")||cityName.equals("香港")||cityName.equals("澳门")){
                            String sonOfUri = "https://news.qq.com/zt2020/page/feiyan.htm#/?ADTAG=quanqiuyimiao";
                            Uri uri = Uri.parse(sonOfUri);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        else{
                            String pool = "";
                            if(cityName.equals("北京")){
                                pool = "bj";
                            }
                            else if(cityName.equals("上海")){
                                pool = "sh";
                            }
                            else if(cityName.equals("重庆")){
                                pool = "cq";
                            }
                            else if(cityName.equals("天津")){
                                pool = "tj";
                            }
                            String sonOfUri = "https://news.qq.com/zt2020/page/feiyan.htm#/area?pool=" + pool;
                            Uri uri = Uri.parse(sonOfUri);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                });

                for (TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO childrenDTO : childrenDTOList) {
                    if(childrenDTO.getName().equals(cityName)) {
                        TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO.TodayDTO todayDTO = childrenDTO.getToday();
                        yiqing_confirm_add.setText(todayDTO.getLocal_confirm_add()+"");
                        yiqing_wz_add.setText(todayDTO.getWzz_add()+"");
                        TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO.TotalDTO totalDTO = childrenDTO.getTotal();
                        yiqing_confirm.setText(totalDTO.getNowConfirm()+"");
                        yiqing_total_confirm.setText(totalDTO.getConfirm()+"");
                        yiqing_medium.setText(totalDTO.getMediumRiskAreaNum()+"");
                        yiqing_height.setText(totalDTO.getHighRiskAreaNum()+"");
                        break;
                    }
                }
            }
            else {
                for (TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO childrenDTO : childrenDTOList) {

                    if(childrenDTO.getName().equals(provinceName)) {
                        int t = 1;
//                取出Son
                        List<TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO.ChildrenDTO_Son> childrenDTO_sons = childrenDTO.getChildren();
//                输出看看Son
                        for (TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO.ChildrenDTO_Son childrenDTO_son : childrenDTO_sons) {
                            if (childrenDTO_son.getName().equals(cityName)) {
                                show_more_yiqing.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String sonOfUri = "https://news.qq.com/zt2020/page/feiyan.htm#/area?adcode=" + childrenDTO_son.getAdcode();
                                        Uri uri = Uri.parse(sonOfUri);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                });
                                TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO.ChildrenDTO_Son.TodayDTO todayDTO = childrenDTO_son.getToday();
                                yiqing_confirm_add.setText(todayDTO.getLocal_confirm_add()+"");
                                yiqing_wz_add.setText(todayDTO.getWzz_add()+"");
                                TencentData.DataDTO.Diseaseh5ShelfDTO.AreaTreeDTO.ChildrenDTO.ChildrenDTO_Son.TotalDTO totalDTO = childrenDTO_son.getTotal();
                                yiqing_confirm.setText(totalDTO.getNowConfirm()+"");
                                yiqing_total_confirm.setText(totalDTO.getConfirm()+"");
                                yiqing_medium.setText(totalDTO.getMediumRiskAreaNum()+"");
                                yiqing_height.setText(totalDTO.getHighRiskAreaNum()+"");
                                t = 2;
                                break;
                            }
                        }
                        if(t==2){
                            break;
                        }
                    }
                }
            }
        }

    }




}

class MyPagerAdapter_new extends PagerAdapter {
    private ArrayList<View> viewArrayList = new ArrayList<>();

    public MyPagerAdapter_new(ArrayList<View> viewArrayList) {
        this.viewArrayList = viewArrayList;
    }

    @Override
    public int getCount() {
        return viewArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewArrayList.get(position),0);
        return viewArrayList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewArrayList.get(position));
    }
}