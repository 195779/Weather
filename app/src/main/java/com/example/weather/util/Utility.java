package com.example.weather.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.weather.db.Province;
import com.example.weather.db.City;
import com.example.weather.db.County;
import com.example.weather.db.Weather;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.geo.GeoPoiBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.view.QWeather;

import java.util.List;

public class Utility {
    //解析省级数据
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.setProvinceName(provinceObject.getString("name"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析市级数据
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析县级数据
    public static boolean handleCountyResponse(String response, int cityId, String cityName, String provinceName, Context context) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);

                    County county = new County();
                    String CountyName = countyObject.getString("name");
                    county.setCountyName(CountyName);
                    county.setCityId(cityId);

                    QWeather.getGeoCityLookup(context, CountyName, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
                        @Override
                        public void onError(Throwable throwable) {
                            Log.e("onError", throwable.toString());
                        }

                        @Override
                        public void onSuccess(GeoBean geoBean) {
                            List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                            if (locationBeanList.size() == 1) {
                                //县级没有重名
                                String weatherId = locationBeanList.get(0).getId();
                                county.setWeatherId(weatherId);
                                county.saveOrUpdate("weatherid = ?",weatherId);
                                return;
                            } else {
                                for (int i = 0; i < locationBeanList.size(); i++) {
                                    if (cityName.equals(locationBeanList.get(i).getAdm2())) {
                                        String weatherId = locationBeanList.get(i).getId();
                                        county.setWeatherId(weatherId);
                                        county.saveOrUpdate("weatherid = ?",weatherId);
                                        return;
                                    }
                                }
                            }
                        }
                    });
                    //county.setWeatherId(countyObject.getString("weather_id"));
                    //county.setWeatherId(weatherId);
                    //county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleWeatherResponse(String weatherId, String countyName, Context context) {
        if (!weatherId.isEmpty() && !countyName.isEmpty()) {
            QWeather.getWeather7D(context, weatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
                @Override
                public void onError(Throwable throwable) {
                    Log.e("onError", throwable.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(WeatherDailyBean weatherDailyBean) {
                    Weather weather = new Weather();
                    weather.setWeatherId(weatherId);
                    weather.setCountyName(countyName);
                    weather.setUpdateTime(weatherDailyBean.getBasic().getUpdateTime());
                    List<WeatherDailyBean.DailyBean> dailyBeanList = weatherDailyBean.getDaily();
                    weather.save();
                }
            });
            return true;
        }
        return false;
    }

}
