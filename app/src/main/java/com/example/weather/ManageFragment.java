package com.example.weather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.example.weather.db.Weather;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class ManageFragment extends Fragment {

    private TextView titleText;
    public static  MangeAdapter adapter;
    public static ListView listView;
    public static List<String> nameDataList = new ArrayList<>();
    public static List<String> weaherIdDataList = new ArrayList<>();
    public static List<String> textDataList = new ArrayList<>();
    public static List<Weather> WeatherList = new ArrayList<>();
    public static List<Mange> mangeList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text_manage);
        //adapter = new ArrayAdapter<>(getContext(), R.layout.manage_item, nameDataList);
        //listView.setAdapter(adapter);
        //qureyWeather_manage();
        titleText.setText("常用城市");
        adapter = new MangeAdapter(getActivity(),listener,
                R.layout.manage_item, mangeList);
        /*将MangeAdapter类型的adapter对象作为适配器（adpater）传递给ListView*/
        listView = (ListView) view.findViewById(R.id.list_view_manage);
        listView.setAdapter(adapter);
        //this.registerForContextMenu(listView);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //qureyWeather_manage();
        //adapter.notifyDataSetChanged();
        //listView.setSelection(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int poistion, long id) {

            }
        });
    }

    public static void qureyWeather_manage(){
        WeatherList = LitePal.findAll(Weather.class);
        //Toast.makeText(getActivity(), "the size is " + WeatherList.size(), Toast.LENGTH_SHORT).show();
        if (WeatherList.size() > 0) {
            weaherIdDataList.clear();
            textDataList.clear();
            nameDataList.clear();
            mangeList.clear();
            for (Weather weather : WeatherList) {
                nameDataList.add(weather.getCCName()+"\n"+weather.getText());
                textDataList.add(weather.getText());
                weaherIdDataList.add(weather.getWeatherId());
                Mange mange = new Mange();
                mange.setName(weather.getCCName()+"\n"+weather.getText());
                mange.setImageId(R.drawable.guanbi);
                mangeList.add(mange);
            }
            adapter.notifyDataSetChanged();
            //listView.setSelection(0);
        }


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.text_manage_item:{
                    final int position = (int) view.getTag();
                    String weatherId = weaherIdDataList.get(position);
                    ((WeatherActivity) getActivity()).show_Weather_List(weatherId);
                    //更改界面的weatherID
                    ((WeatherActivity) getActivity()).thisWeatherId = weatherId;
                    ((WeatherActivity) getActivity()).drawerLayout.closeDrawer(Gravity.RIGHT);
                    //Toast.makeText(getActivity(), "the size is " + WeatherList.size(), Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.close_image:{
                    final int position = (int) view.getTag(); //获取被点击的控件所在item 的位置，setTag 存储的object，所以此处要强转
                    //点击删除按钮之后，给出dialog提示
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle( position + "号位置的删除按钮被点击，确认删除此常用城市?");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String weatherId = weaherIdDataList.get(position);
                            LitePal.deleteAll(Weather.class,"weatherId = ?",weatherId);
                            mangeList.remove(position);
                            weaherIdDataList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();
                    break;
                }
            }
        }
    };


}
