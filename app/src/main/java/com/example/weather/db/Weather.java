package com.example.weather.db;

import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Weather extends LitePalSupport {
    private String countyName;
    private String weatherid;
    private String updateTime;//getUpadateTime() in WeatherDailyBean.Basic
    private String date;
    private String aqi;//空气质量//NOW
    private String pm25;//pm2.5//NOW
    /*private String temperature;//温度
    private String info;//天气状态
    private String comf;
    private String cw;
    private String sport;
    private String max;
    private String min;*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherid() {
        return weatherid;
    }

    public void setWeatherid(String weatherid) {
        this.weatherid = weatherid;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherid;
    }

    public void setWeatherId(String weatherId) {
        this.weatherid = weatherId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


}
