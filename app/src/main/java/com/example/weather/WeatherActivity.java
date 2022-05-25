package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.weather.db.City;
import com.example.weather.db.County;
import com.example.weather.db.Weather;
import com.example.weather.util.HttpUtil;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.AbstractList;
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

    public WeatherActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
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

        ImageView title_image = (ImageView) findViewById(R.id.title_image);
        ImageView forecast_image = (ImageView) findViewById(R.id.forecast_image);
        ImageView now_image = (ImageView) findViewById(R.id.now_image);
        ImageView aqi_image = (ImageView) findViewById(R.id.aqi_image);
        ImageView suggestion = (ImageView)findViewById(R.id.suggestion_image);
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

        show_Weather_List(getIntent());

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
                show_Weather_List(getIntent());
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
                                        Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("weatherId", weatherId);
                                        startActivity(intent);
                                    } else {
                                        for (int i = 0; i < locationBeanList.size(); i++) {
                                            if (bdLocation.getCity().equals(locationBeanList.get(i).getAdm2())) {
                                                String weatherId = locationBeanList.get(i).getId();
                                                Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra("weatherId", weatherId);
                                                startActivity(intent);
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
        });
    }


    public void show_Weather_List(Intent intent){
        String mWeatherId = intent.getStringExtra("weatherId");
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
                titleUpdateTime.setText(weatherNowBean.getBasic().getUpdateTime());
                degreeText.setText(weatherNowBean.getNow().getTemp() + "'C");
                weatherInfoText.setText(weatherNowBean.getNow().getText());
            }
        });
        //forecastLayout.removeAllViews();
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
                    dateText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    dateText.setText(dailyBeanList.get(i).getFxDate());
                    infoText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    infoText.setText(dailyBeanList.get(i).getTextDay()+"/"+dailyBeanList.get(i).getTextNight());
                    maxText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    maxText.setText(dailyBeanList.get(i).getTempMax()+"'C");
                    minText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    minText.setText(dailyBeanList.get(i).getTempMin()+"'C");
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

    @Override
    protected void onStop() {
        super.onStop();
        /*if(locationClient.isStarted()){
            locationClient.stop();
        }*/
    }
}