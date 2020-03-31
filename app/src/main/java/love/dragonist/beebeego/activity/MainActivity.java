package love.dragonist.beebeego.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.adapter.ChoosePositionListAdapter;
import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.InsideRoutesHolder;
import love.dragonist.beebeego.bean.User;
import love.dragonist.beebeego.net.LogReg;
import love.dragonist.beebeego.net.Transit;
import love.dragonist.beebeego.net.Weather;
import love.dragonist.beebeego.util.Format;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private TextView textDepartPosition;
    private TextView textArrivePosition;
    private TextView textCancelChoose;
    private TextView textTemperature;
    private TextView textAir;
    private ImageView imgLocate;
    private ImageView imgScaleUp;
    private ImageView imgScaleDown;
    private ImageView imgSelf;
    private ImageView imgSearch;
    private ImageView imgChange;
    private ListView listPositions;
    private EditText editChoosePosition;
    private ConstraintLayout constraintChoosePosition;
    private LinearLayout linearPeriphery;

    private BaiduMap map;
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;
    private LocationListener locationListener;
    private SuggestionSearch suggestionSearch;
    private GeoCoder geoCoderLocate;
    private GeoCoder geoCoderSuggest;
    private Animation animation;
    private SharedPreferences sharedPreferences;

    private ChoosePositionListAdapter choosePositionListAdapter;

    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private List<ReverseGeoCodeResult> suggestPositions;
    private boolean isDestination = false;
    private User user;
    private Gson gson;
    private long backPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initMap();
        initListener();

        locationClient.stop();
        locationClient.start();
    }

    private void initData() {
        gson = new Gson();

        suggestionSearch = SuggestionSearch.newInstance();
        geoCoderLocate = GeoCoder.newInstance();
        geoCoderSuggest = GeoCoder.newInstance();

        suggestPositions = new ArrayList<>();
        choosePositionListAdapter = new ChoosePositionListAdapter(this, suggestPositions);

        sharedPreferences = getSharedPreferences("beebeego", Context.MODE_PRIVATE);
        user = new User();
        user.setTelephone(sharedPreferences.getString("telephone", null));
        user.setPassword(sharedPreferences.getString("password", null));
        new LogReg(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                if (result.equals("fail")) {
                    user.setState(0);
                } else {
                    String[] s = result.split(" ");
                    user.setpPrice(Double.parseDouble(s[0]));
                    user.setpTime(Double.parseDouble(s[1]));
                    user.setpComfort(Double.parseDouble(s[2]));
                    user.setState(1);
                }
            }
        }).login(user.getTelephone(), user.getPassword());
    }

    private void initView() {
        mapView = findViewById(R.id.main_map);
        mapView.showZoomControls(false);
        imgLocate = findViewById(R.id.main_img_locate);
        imgScaleUp = findViewById(R.id.main_img_scale_up);
        imgScaleDown = findViewById(R.id.main_img_scale_down);
        imgSelf = findViewById(R.id.main_img_self);
        imgSearch = findViewById(R.id.main_img_search);
        imgChange = findViewById(R.id.main_img_change_arrow);
        textDepartPosition = findViewById(R.id.main_text_depart_position);
        textArrivePosition = findViewById(R.id.main_text_arrive_position);
        textCancelChoose = findViewById(R.id.main_text_cancel_choose_position);
        textTemperature = findViewById(R.id.main_text_temperature);
        textAir = findViewById(R.id.main_text_air);
        listPositions = findViewById(R.id.main_list_choose_position);
        listPositions.setAdapter(choosePositionListAdapter);
        editChoosePosition = findViewById(R.id.main_edit_choose_position);
        constraintChoosePosition = findViewById(R.id.main_constrain_choose_location);
        linearPeriphery = findViewById(R.id.main_linear_periphery);

        new Weather(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject jsonObject = (JSONObject) msg.obj;
                try {
                    textAir.setText(jsonObject.getString("air_level"));
                    textTemperature.setText(jsonObject.getString("temperature"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).get();
    }

    private void initMap() {
        map = mapView.getMap();
        map.setTrafficEnabled(true);
        map.setMyLocationEnabled(true);

        locationClient = new LocationClient(this);
        //设置定位选项
        locationClientOption = new LocationClientOption();
        locationClientOption.setOpenGps(true);
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setScanSpan(0);
        locationClient.setLocOption(locationClientOption);
        //注册定位监听
        locationListener = new LocationListener();
        locationClient.registerLocationListener(locationListener);
    }

    private void initListener() {
        linearPeriphery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebActivity.class));
            }
        });

        //交换出发地与目的地
        imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReverseGeoCodeResult tmp = departPosition;
                departPosition = arrivePosition;
                arrivePosition = tmp;
                if (departPosition == null) {
                    textDepartPosition.setText("出发地点");
                } else {
                    textDepartPosition.setText(Format.getNameByReverseGeoCodeResult(departPosition));
                }
                if (arrivePosition == null) {
                    textArrivePosition.setText("出发地点");
                } else {
                    textArrivePosition.setText(Format.getNameByReverseGeoCodeResult(arrivePosition));
                }
                paintRoute();
            }
        });

        /*
        输入简略的位置信息
        计算推荐的位置
         */
        editChoosePosition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) return;
                if (departPosition == null || departPosition.getAddressDetail() == null) return;
                suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(departPosition.getAddressDetail().city).keyword(s.toString()));
            }
        });

        /*
        推荐结果的返回
         */
        suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                List<SuggestionResult.SuggestionInfo> infos = suggestionResult.getAllSuggestions();
                if (infos == null) return;
                suggestPositions.clear();

                for (SuggestionResult.SuggestionInfo info : infos) {
                    if (info.getPt() == null) continue;
                    geoCoderSuggest.reverseGeoCode(new ReverseGeoCodeOption().location(info.getPt()).radius(5000));
                }
            }
        });

        textDepartPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_in_bottom);
                constraintChoosePosition.startAnimation(animation);
                constraintChoosePosition.setVisibility(View.VISIBLE);
                isDestination = false;
            }
        });

        textArrivePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_in_bottom);
                constraintChoosePosition.startAnimation(animation);
                constraintChoosePosition.setVisibility(View.VISIBLE);
                isDestination = true;
            }
        });

        /*
        点击取消关闭地点选择栏
         */
        textCancelChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_bottom);
                constraintChoosePosition.startAnimation(animation);
                constraintChoosePosition.setVisibility(View.INVISIBLE);
                editChoosePosition.setText("");
                choosePositionListAdapter.notifyDataSetChanged();
            }
        });

        /*
        推荐地点的返回监听
         */
        geoCoderSuggest.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                for (ReverseGeoCodeResult position : suggestPositions) {
                    if (position.getLocation() != null && Format.getNameByReverseGeoCodeResult(position).equals(Format.getNameByReverseGeoCodeResult(reverseGeoCodeResult)))
                        return;
                }
                suggestPositions.add(reverseGeoCodeResult);
                choosePositionListAdapter.notifyDataSetChanged();
            }
        });

        geoCoderLocate.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                departPosition = reverseGeoCodeResult;
                textDepartPosition.setText(Format.getNameByReverseGeoCodeResult(departPosition));
                paintRoute();
            }
        });

        listPositions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDestination) {
                    arrivePosition = suggestPositions.get(position);
                    textArrivePosition.setText(Format.getNameByReverseGeoCodeResult(arrivePosition));
                } else {
                    departPosition = suggestPositions.get(position);
                    textDepartPosition.setText(Format.getNameByReverseGeoCodeResult(departPosition));
                }
                animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_bottom);
                constraintChoosePosition.startAnimation(animation);
                constraintChoosePosition.setVisibility(View.INVISIBLE);
                editChoosePosition.setText("");
                choosePositionListAdapter.notifyDataSetChanged();
                paintRoute();
            }
        });

        /*
        定位按钮
        定位到当前位置
         */
        imgLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrivePosition = null;
                textArrivePosition.setText("到达地点");
                locationClient.stop();
                locationClient.start();
            }
        });

        imgScaleUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.animateMapStatus(MapStatusUpdateFactory.zoomIn(), 200);
            }
        });

        imgScaleDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.animateMapStatus(MapStatusUpdateFactory.zoomOut(), 200);
            }
        });

        imgSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MineActivity.class).putExtra("user", gson.toJson(user)));
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (departPosition == null) {
                    Toast.makeText(MainActivity.this, "未选择出发地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (arrivePosition == null) {
                    Toast.makeText(MainActivity.this, "未选择到达地点", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (departPosition.getCityCode() == arrivePosition.getCityCode()) {
                    startActivity(new Intent(MainActivity.this, InsideActivity.class)
                            .putExtra("departPosition", gson.toJson(departPosition))
                            .putExtra("arrivePosition", gson.toJson(arrivePosition)));
                } else {
                    startActivity(new Intent(MainActivity.this, ResultActivity.class)
                            .putExtra("departPosition", gson.toJson(departPosition))
                            .putExtra("arrivePosition", gson.toJson(arrivePosition))
                            .putExtra("user", gson.toJson(user)));
                }
            }
        });
    }

    /*
    绘制路径
    如果有起点和终点，就画路
    如果只有起点就只花起点
     */
    private void paintRoute() {
        map.clear();

        if (departPosition != null && arrivePosition != null) {
            List<LatLng> points = new ArrayList<>();
            points.add(departPosition.getLocation());
            points.add(arrivePosition.getLocation());

            map.addOverlay(new PolylineOptions()
                    .width(16)
                    .points(points)
                    .dottedLine(true)
                    .color(0xFF000000)
                    .customTexture(BitmapDescriptorFactory.fromResource(R.mipmap.road_red_arrow))
                    .keepScale(true));

            map.addOverlay(new MarkerOptions()
                    .position(departPosition.getLocation())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start_point)).scaleX(0.2f).scaleY(0.2f)
                    .animateType(MarkerOptions.MarkerAnimateType.jump));

            map.addOverlay(new MarkerOptions()
                    .position(arrivePosition.getLocation())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_end_point)).scaleX(0.2f).scaleY(0.2f)
                    .animateType(MarkerOptions.MarkerAnimateType.jump));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(departPosition.getLocation());
            builder.include(arrivePosition.getLocation());

            map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build(), 40, mapView.getHeight() / 4, 40, mapView.getHeight() / 8));

            return;
        }

        if (departPosition != null) {
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_start_point);
            OverlayOptions option = new MarkerOptions()
                    .position(departPosition.getLocation())
                    .icon(bitmap).scaleX(0.2f).scaleY(0.2f)
                    .animateType(MarkerOptions.MarkerAnimateType.jump);
            map.addOverlay(option);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(departPosition.getLocation());
            map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build(), 16, 16, 16, 16));
        }
    }

    public class LocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mapView == null) {
                return;
            }
            geoCoderLocate.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        user.setTelephone(sharedPreferences.getString("telephone", null));
        user.setPassword(sharedPreferences.getString("password", null));
        new LogReg(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                if (result.equals("fail")) {
                    user.setState(0);
                } else {
                    String[] s = result.split(" ");
                    user.setpPrice(Double.parseDouble(s[0]));
                    user.setpTime(Double.parseDouble(s[1]));
                    user.setpComfort(Double.parseDouble(s[2]));
                    user.setState(1);
                }
            }
        }).login(user.getTelephone(), user.getPassword());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        locationClient.stop();
        geoCoderLocate.destroy();
        geoCoderSuggest.destroy();
    }

    @Override
    public void onBackPressed() {
        long pressTime = System.currentTimeMillis();
        if ((pressTime - backPressTime) > 2000) {
            Toast.makeText(this, "再按一次返回建退出", Toast.LENGTH_SHORT).show();
            backPressTime = pressTime;
        } else {
            finish();
        }
    }
}
