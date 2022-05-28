package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.weather.db.Weather;
import com.example.weather.util.HttpUtil;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private TextView aqiText;

    private TextView pm25Text;

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




    public WeatherActivity() {
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        Now_weather_image = (ImageView) findViewById(R.id.Now_weather_image);
        Warnning_text = (TextView) findViewById(R.id.Warnning_text);
        shezhi = (ImageView) findViewById(R.id.image_shezhi);


        //设置的按键
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(shezhi);
            }
        });


        ImageView title_image = (ImageView) findViewById(R.id.title_image);
        ImageView forecast_image = (ImageView) findViewById(R.id.forecast_image);
        ImageView now_image = (ImageView) findViewById(R.id.now_image);
        ImageView aqi_image = (ImageView) findViewById(R.id.aqi_image);
        ImageView suggestion = (ImageView)findViewById(R.id.suggestion_image);
        //图像模糊化处理Glide
        BaseRequestOptions optionsBlur = new RequestOptions().transform(new BlurTransformation(5, 35));
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(title_image);
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(forecast_image);
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(now_image);
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(suggestion);
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(aqi_image);

        //从MainActivity转过来的时候显示天气信息
        thisWeatherId = getIntent().getStringExtra("weatherId");
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
                //更新之后隐藏下拉刷新的进度条
                swipeRefresh.setRefreshing(false);
            }
        });

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
    }


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
                }
                if(item.getTitle().equals("管理常用城市")){
                    updateWeather();
                }
                return false;
            }
        });
        popupMenu.show();
    }



    //显示天气信息
    public void show_Weather_List(String mWeatherId){
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
                }
            }
        });


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
        QWeather.getAirNow(this, mWeatherId, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                AirNowBean.NowBean nowBean = airNowBean.getNow();
                aqiText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                aqiText.setText(nowBean.getAqi());
                pm25Text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pm25Text.setText(nowBean.getPm2p5());
            }
        });

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
        QWeather.getWarning(this, mWeatherId, Lang.ZH_HANS, new QWeather.OnResultWarningListener() {
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
                    Warnning_text.setOnClickListener(new View.OnClickListener() {
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
                //Toast.makeText(this,weather.getText(),Toast.LENGTH_SHORT).show();
            }
        }
        ManageFragment.qureyWeather_manage();
        drawerLayout.openDrawer(Gravity.RIGHT);
    }


}