package com.example.weather.db;

import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Weather extends LitePalSupport {
    //常用城市的存储表
    private String weatherId;
    private String CCName;
    private String text;

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getCCName() {
        return CCName;
    }

    public void setCCName(String CCName) {
        this.CCName = CCName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
