package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;

import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.Step;
import love.dragonist.beebeego.view.ChooseInsideView;
import love.dragonist.beebeego.view.InsideCityView;

public class InsideBriefActivity extends AppCompatActivity {
    private ChooseInsideView chooseInsideView;
    private MapView mapView;

    private InsideRoute insideRoute;
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private Gson gson;
    private BaiduMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_brief);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        gson = new Gson();
        Intent intent = getIntent();
        insideRoute = gson.fromJson(intent.getStringExtra("insideRoute"), InsideRoute.class);
        departPosition = gson.fromJson(intent.getStringExtra("departPosition"), ReverseGeoCodeResult.class);
        arrivePosition = gson.fromJson(intent.getStringExtra("arrivePosition"), ReverseGeoCodeResult.class);
    }

    private void initView() {
        mapView = findViewById(R.id.inside_brief_mapView);
        mapView.showZoomControls(false);
        map = mapView.getMap();
        paintInside(insideRoute.getSteps());
        chooseInsideView = findViewById(R.id.inside_brief_content);
        chooseInsideView.setView(insideRoute, InsideBriefActivity.this);
    }

    private void initListener() {
        chooseInsideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsideBriefActivity.this, InsideBrief2Activity.class)
                        .putExtra("insideRoute", gson.toJson(insideRoute))
                        .putExtra("departPosition", gson.toJson(departPosition))
                        .putExtra("arrivePosition", gson.toJson(arrivePosition)));
            }
        });
    }

    private void paintInside(List<Step> steps) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Step step : steps) {
            //画线
            if (step.getVehicle_info().getType() == 5) {
                map.addOverlay(new PolylineOptions()
                        .width(6).color(0xff00ff00).points(step.getPaths()));
            } else {
                map.addOverlay(new PolylineOptions()
                        .width(6).color(0xff0000ff).points(step.getPaths()));
            }

            //画图标
            if (step.getVehicle_info().getType() == 5) {
                map.addOverlay(new MarkerOptions()
                        .position(step.getPaths().get(0))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_walk_route)));
            } else if (step.getVehicle_info().getType() == 3) {
                if (step.getVehicle_info().getDetail().getType() == 0) {
                    map.addOverlay(new MarkerOptions()
                            .position(step.getPaths().get(0))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_bus_station)));
                } else {
                    map.addOverlay(new MarkerOptions()
                            .position(step.getPaths().get(0))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_subway_station)));
                }
            }

            for (LatLng path : step.getPaths()) {
                builder.include(path);
            }
        }
        map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()), 1000);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
}