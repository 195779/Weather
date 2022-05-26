package com.example.weather.db;

import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Weather extends LitePalSupport {
    //常用城市的存储表
    private String weathrId;
    private String countyName;
    private String cityName;
    private String temp;
    private String text;

    public String getWeathrId() {
        return weathrId;
    }

    public void setWeathrId(String weathrId) {
        this.weathrId = weathrId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
