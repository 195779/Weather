package com.example.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.weather.db.Weather;
import com.example.weather.gson.TencentData;
import com.example.weather.util.HttpUtil;
import com.google.gson.Gson;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Weather_Fragement extends Fragment {



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

    public void setThisWeatherId(String thisWeatherId) {
        this.thisWeatherId = thisWeatherId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_weather, container, false);

        bingPicImg = (ImageView) view.findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) view.findViewById(R.id.weather_layout);
        titleCity = (TextView) view.findViewById(R.id.title_city);
        degreeText = (TextView) view.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) view.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        pm10Text = (TextView) view.findViewById(R.id.pm10_text);
        pm25Text = (TextView) view.findViewById(R.id.pm25_text);
        qualityText = (TextView) view.findViewById(R.id.quality_text);
        primaryText = (TextView) view.findViewById(R.id.primary_text);
        show_more_aqi = (TextView) view.findViewById(R.id.show_more_aqi);
        comfortText = (TextView) view.findViewById(R.id.comfort_text);
        carWashText = (TextView) view.findViewById(R.id.car_wash_text);
        sportText = (TextView) view.findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        navButton = (Button) view.findViewById(R.id.nav_button);
        Now_weather_image = (ImageView) view.findViewById(R.id.Now_weather_image);
        Warnning_text = (TextView) view.findViewById(R.id.Warnning_text);
        shezhi = (ImageView) view.findViewById(R.id.image_shezhi);
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

        //设置的按键
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(shezhi);
            }
        });


        ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
        ImageView forecast_image = (ImageView) view.findViewById(R.id.forecast_image);
        ImageView now_image = (ImageView) view.findViewById(R.id.now_image);
        ImageView aqi_image = (ImageView) view.findViewById(R.id.aqi_image);
        ImageView suggestion = (ImageView)view.findViewById(R.id.suggestion_image);
        ImageView yiqing_image = (ImageView)view.findViewById(R.id.YiQing_image);
        //图像模糊化处理Glide
        BaseRequestOptions optionsBlur = new RequestOptions().transform(new BlurTransformation(5, 35));
        Glide.with(this).load(getActivity().getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(title_image);
        Glide.with(this).load(getActivity().getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(forecast_image);
        Glide.with(this).load(getActivity().getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(now_image);
        Glide.with(this).load(getActivity().getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(suggestion);
        Glide.with(this).load(getActivity().getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(aqi_image);
        Glide.with(this).load(getActivity().getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(yiqing_image);


        show_Weather_List(thisWeatherId);

        titleCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
                //点击城市名称，弹出左滑窗口选择城市
            }
        });

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
                show_Weather_List(thisWeatherId);
                loadBingPic();
                //更新之后隐藏下拉刷新的进度条
                swipeRefresh.setRefreshing(false);
            }
        });

        //跳转浏览器显示更多天气信息
        show_more_aqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QWeather.getGeoCityLookup(getActivity(), thisWeatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
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
                QWeather.getGeoCityLookup(getActivity(), thisWeatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
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
                QWeather.getGeoCityLookup(getActivity(), thisWeatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
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

        //调用百度定位SDK的方法，获取设备当前的位置信息并根据当前位置所在城市的weatherID来更新天气显示界面
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LocationClient.setAgreePrivacy(true);
                    locationClient = new LocationClient(getActivity());
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
                            QWeather.getGeoCityLookup(getActivity(), district, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
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
                                                //startActivity(intent);
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

        return view;
    }


    //显示全部天气+疫情信息
    public void show_Weather_List(String mWeatherId){
        //显示城市名称
        QWeather.getGeoCityLookup(getActivity(), mWeatherId, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
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
        QWeather.getWeatherNow(getActivity(), mWeatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
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
        QWeather.getWeather7D(getActivity(), mWeatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                List<WeatherDailyBean.DailyBean> dailyBeanList = weatherDailyBean.getDaily();
                forecastLayout.removeAllViews();
                //清空所有子view，防止手动下滑更新的时候再次调用此函数时造成七天的天气数据被变成N*7条七天的数据
                for (int i=0;i<dailyBeanList.size();i++){
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item, forecastLayout, false);
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
                                QWeather.getGeoCityLookup(getActivity(), mWeatherId, Range.CN, 20, Lang.EN, new QWeather.OnResultGeoListener() {
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
        QWeather.getAirNow(getActivity(), mWeatherId, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
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
        QWeather.getIndices1D(getActivity(),mWeatherId,Lang.ZH_HANS,indicesTypeList,new QWeather.OnResultIndicesListener(){

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
        QWeather.getWarning(getActivity(), thisWeatherId, Lang.ZH_HANS, new QWeather.OnResultWarningListener() {
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
                    Warnning_text.setText("暂无预警信息");
                } else {
                    Warnning_text.setText(warnningText);
                }
            }
        });
    }


    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingPic)
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


    //点击设置之后弹出的菜单显示
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
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
                }
                if(item.getTitle().equals("管理常用城市")){
                    //点击管理常用城市之后，更新并显示常用城市的天气情况
                    updateWeather();
                }
                return false;
            }
        });
        popupMenu.show();
    }



    public void updateWeather(){
        //取出weather表的全部元素之后，逐个遍历，根据每个weather元素的weatherid去获取该id的城市的天气信息
        // 将天气信息封装进一个新的weather对象，然后存入weather表中（saveOrUpdate : 没有这个id的话存入，有的话更新这个id的元素的全部信息）
        // 当然了这里相当于只用到了update
        List<Weather> new_WeatherList = LitePal.findAll(Weather.class);
        if(new_WeatherList.size()>0){
            for (Weather weather : new_WeatherList) {
                String meatherId = weather.getWeatherId();
                QWeather.getWeatherNow(getActivity(), meatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
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
        ManageFragment.qureyWeather_manage();
        //打开右侧的fragment显示常用城市界面
        drawerLayout.openDrawer(Gravity.RIGHT);
    }


    protected String load() {
        //读取test.json
        StringBuilder content = new StringBuilder();
        FileInputStream in = null;
        BufferedReader reader = null;
        try {
            in = getActivity().openFileInput("test.json");
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
        /*  类目录
            TencentData
            public static class DataDTO {
            private Diseaseh5ShelfDTO diseaseh5Shelf;                       //DataDTO 的一个内部静态类
            private List<LocalCityNCOVDataListDTO> localCityNCOVDataList;   //DataDTO 的一个内部静态类数组
            */
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
