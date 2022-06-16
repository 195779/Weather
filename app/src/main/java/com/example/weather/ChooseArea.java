package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.weather.util.HttpUtil;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.QWeather;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseArea extends AppCompatActivity {

    public Button button_choose;
    public DrawerLayout drawerLayout;
    public ImageView bingPicImg_new;
    public ImageView Sousuo_image;
    public LinearLayout city_layout;
    public ImageView city_image;
    public EditText edit_text;
    public ImageButton sousuo_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        button_choose = (Button) findViewById(R.id.button_choose);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        bingPicImg_new = (ImageView) findViewById(R.id.bing_pic_img_new);
        Sousuo_image = (ImageView) findViewById(R.id.Sousuo_image);
        sousuo_button = (ImageButton) findViewById(R.id.sousuo_button);
        edit_text = (EditText) findViewById(R.id.edit_city);
        city_image = (ImageView) findViewById(R.id.city_sousuo_image);
        city_layout = (LinearLayout) findViewById(R.id.city_layout);
        button_choose.setText("手动切换");
        loadBingPic();
        BaseRequestOptions optionsBlur = new RequestOptions().transform(new BlurTransformation(5, 35));
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(Sousuo_image);
        Glide.with(this).load(getDrawable(R.drawable.ic_home))
                .apply(optionsBlur)
                .into(city_image);
        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ChooseArea.this,"what happend",Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        ManageFragment manageFragment = new ManageFragment();
        manageFragment.qureyWeather_manage();

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String city_name = s.toString();
                if(start == 0){
                    city_layout.removeAllViews();
                }
                QWeather.getGeoCityLookup(ChooseArea.this, city_name, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {
                    }
                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                        city_layout.removeAllViews();
                        for(int i = 0; i < locationBeanList.size(); i++){
                            View view = LayoutInflater.from(ChooseArea.this).inflate(R.layout.city_text_item, city_layout, false);
                            TextView city_text = (TextView) view.findViewById(R.id.city_text_textView);
                            city_text.setText(locationBeanList.get(i).getCountry()+"/"+locationBeanList.get(i).getAdm1()+
                                    "/"+locationBeanList.get(i).getName());
                            city_text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                            view.setTag(locationBeanList.get(i).getId());
                            city_layout.addView(view);
                        }
                        for(int i=0; i < city_layout.getChildCount(); i++){
                            String weatherId = city_layout.getChildAt(i).getTag().toString();
                            if(!weatherId.isEmpty()){
                                city_layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ChooseArea.this,WeatherActivity.class);
                                        intent.putExtra("weatherId",weatherId);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        sousuo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city_name = edit_text.getText().toString();
                city_layout.removeAllViews();
                QWeather.getGeoCityLookup(ChooseArea.this, city_name, Range.CN, 20, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {
                    }
                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                        city_layout.removeAllViews();
                        for(int i = 0; i < locationBeanList.size(); i++){
                            View view = LayoutInflater.from(ChooseArea.this).inflate(R.layout.city_text_item, city_layout, false);
                            TextView city_text = (TextView) view.findViewById(R.id.city_text_textView);
                            city_text.setText(locationBeanList.get(i).getCountry()+"/"+locationBeanList.get(i).getAdm1()+
                                    "/"+locationBeanList.get(i).getName());
                            city_text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                            view.setTag(locationBeanList.get(i).getId());
                            city_layout.addView(view);
                        }
                        for(int i=0; i < city_layout.getChildCount(); i++){
                            String weatherId = city_layout.getChildAt(i).getTag().toString();
                            if(!weatherId.isEmpty()){
                                city_layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ChooseArea.this,WeatherActivity.class);
                                        intent.putExtra("weatherId",weatherId);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }


    //加载必应每日一图
    //加载必应每日一图

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChooseArea.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(ChooseArea.this).load(bingPic)
                                .into(bingPicImg_new);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

}