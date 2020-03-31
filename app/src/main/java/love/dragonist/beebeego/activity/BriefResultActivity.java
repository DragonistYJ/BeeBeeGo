package love.dragonist.beebeego.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.google.gson.Gson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.InsideRoutesHolder;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.bean.Step;
import love.dragonist.beebeego.bean.User;
import love.dragonist.beebeego.bean.Way;
import love.dragonist.beebeego.net.RecordNet;
import love.dragonist.beebeego.net.Transit;
import love.dragonist.beebeego.util.Format;
import love.dragonist.beebeego.view.InsideCityView;
import love.dragonist.beebeego.view.InterCityView;
import love.dragonist.beebeego.view.ItemInsideView;

public class BriefResultActivity extends AppCompatActivity {
    private MapView mapView;
    private ConstraintLayout constraintWhole;
    private ConstraintLayout constraintTitle;
    private TextView textChangeTime;
    private TextView textPrice;
    private TextView textTime;
    private TextView textSt1;
    private TextView textEn1;
    private TextView textSt2;
    private TextView textEn2;
    private TextView textSt3;
    private TextView textEn3;
    private InterCityView interCityView1;
    private InterCityView interCityView2;
    private InterCityView interCityView3;
    private InsideCityView insideCityView1;
    private InsideCityView insideCityView2;
    private InsideCityView insideCityView3;
    private InsideCityView insideCityView4;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linearCollection;
    private LinearLayout linearGoing;

    private ConstraintSet constraintSet;
    private AutoTransition transition;
    private BaiduMap map;
    private Gson gson = new Gson();
    private Route route;
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private List<InsideRoute> insideRoutes1;
    private List<InsideRoute> insideRoutes2;
    private List<InsideRoute> insideRoutes3;
    private List<InsideRoute> insideRoutes4;
    private boolean expended = false;
    private User user;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_result);

        initData();
        initView();
        initMap();
        initListener();
    }

    private void initData() {
        constraintSet = new ConstraintSet();
        transition = new AutoTransition();
        Intent intent = getIntent();
        route = gson.fromJson(intent.getStringExtra("route"), Route.class);
        departPosition = gson.fromJson(intent.getStringExtra("departPosition"), ReverseGeoCodeResult.class);
        arrivePosition = gson.fromJson(intent.getStringExtra("arrivePosition"), ReverseGeoCodeResult.class);
        user = gson.fromJson(intent.getStringExtra("user"), User.class);
    }

    private void initView() {
        constraintWhole = findViewById(R.id.brief_constraint_whole);
        constraintSet.clone(constraintWhole);
        constraintTitle = findViewById(R.id.brief_constraint_1);
        mapView = findViewById(R.id.brief_mapView);
        textChangeTime = findViewById(R.id.brief_text_changeTimes);
        if (route.getSecondWay() == null) textChangeTime.setText("换乘0次");
        else if (route.getThirdWay() == null) textChangeTime.setText("换乘1次");
        else textChangeTime.setText("换乘2次");
        textPrice = findViewById(R.id.brief_text_money);
        textPrice.setText("￥" + route.getPrice() + "起");
        textTime = findViewById(R.id.brief_text_time);
        textTime.setText(getDurationFormat(route.getTime()));
        textSt1 = findViewById(R.id.brief_text_start_1);
        textSt2 = findViewById(R.id.brief_text_start_2);
        textSt3 = findViewById(R.id.brief_text_start_3);
        textEn1 = findViewById(R.id.brief_text_end_1);
        textEn2 = findViewById(R.id.brief_text_end_2);
        textEn3 = findViewById(R.id.brief_text_end_3);
        linear1 = findViewById(R.id.brief_linear_1);
        linear2 = findViewById(R.id.brief_linear_2);
        linear3 = findViewById(R.id.brief_linear_3);
        linearCollection = findViewById(R.id.brief_linear_collection);
        linearGoing = findViewById(R.id.brief_linear_going);

        insideCityView1 = findViewById(R.id.brief_insidecity_1);
        interCityView1 = findViewById(R.id.brief_intercity_1);
        insideCityView2 = findViewById(R.id.brief_insidecity_2);
        interCityView2 = findViewById(R.id.brief_intercity_2);
        insideCityView3 = findViewById(R.id.brief_insidecity_3);
        interCityView3 = findViewById(R.id.brief_intercity_3);
        insideCityView4 = findViewById(R.id.brief_insidecity_4);

        Intent intent = getIntent();
        if (intent.hasExtra("record")) {
            linearCollection.setEnabled(false);
            linearGoing.setEnabled(false);
        } else {
            date = intent.getStringExtra("date");
        }
    }

    private void initMap() {
        map = mapView.getMap();

        if (route.getSecondWay() == null) {
            initOneWay();
        } else if (route.getThirdWay() == null) {
            initTwoWay();
        } else {
            initThreeWay();
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(departPosition.getLocation());
        builder.include(arrivePosition.getLocation());
        map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
        map.animateMapStatus(MapStatusUpdateFactory.zoomTo(6.0f));
    }

    private void initListener() {
        //第一个城市之间的交通的切换按钮
        interCityView1.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BriefResultActivity.this, ChooseInterActivity.class)
                        .putExtra("departStation", route.getFirstWay().getFromStation().getName())
                        .putExtra("arriveStation", route.getFirstWay().getToStation().getName()), 21);
            }
        });

        //第二个城市之间的交通的切换按钮
        interCityView2.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BriefResultActivity.this, ChooseInterActivity.class)
                        .putExtra("departStation", route.getSecondWay().getFromStation().getName())
                        .putExtra("arriveStation", route.getSecondWay().getToStation().getName()), 22);
            }
        });

        //第三个城市之间的交通的切换按钮
        interCityView3.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BriefResultActivity.this, ChooseInterActivity.class)
                        .putExtra("departStation", route.getThirdWay().getFromStation().getName())
                        .putExtra("arriveStation", route.getThirdWay().getToStation().getName()), 23);
            }
        });

        //第一个城市内的交通切换按钮
        insideCityView1.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsideRoutesHolder.getInstance().clear();
                InsideRoutesHolder.getInstance().addAll(insideRoutes1);
                startActivityForResult(new Intent(BriefResultActivity.this, ChooseInsideActivity.class)
                        .putExtra("departPosition", Format.getNameByReverseGeoCodeResult(departPosition))
                        .putExtra("arrivePosition", route.getFirstWay().getFromStation().getName()), 11);
            }
        });

        //第二个城市内的交通切换按钮
        insideCityView2.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsideRoutesHolder.getInstance().clear();
                InsideRoutesHolder.getInstance().addAll(insideRoutes2);
                if (route.getSecondWay() == null) {
                    startActivityForResult(new Intent(BriefResultActivity.this, ChooseInsideActivity.class)
                            .putExtra("departPosition", route.getFirstWay().getToStation().getName())
                            .putExtra("arrivePosition", Format.getNameByReverseGeoCodeResult(arrivePosition)), 12);
                } else {
                    startActivityForResult(new Intent(BriefResultActivity.this, ChooseInsideActivity.class)
                            .putExtra("departPosition", route.getFirstWay().getToStation().getName())
                            .putExtra("arrivePosition", route.getSecondWay().getFromStation().getName()), 12);
                }
            }
        });

        //第三个城市内的交通切换按钮
        insideCityView3.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsideRoutesHolder.getInstance().clear();
                InsideRoutesHolder.getInstance().addAll(insideRoutes3);
                if (route.getThirdWay() == null) {
                    startActivityForResult(new Intent(BriefResultActivity.this, ChooseInsideActivity.class)
                            .putExtra("departPosition", route.getSecondWay().getToStation().getName())
                            .putExtra("arrivePosition", Format.getNameByReverseGeoCodeResult(arrivePosition)), 13);
                } else {
                    startActivityForResult(new Intent(BriefResultActivity.this, ChooseInsideActivity.class)
                            .putExtra("departPosition", route.getSecondWay().getToStation().getName())
                            .putExtra("arrivePosition", route.getThirdWay().getFromStation().getName()), 13);
                }
            }
        });

        //第四个城市内的交通切换按钮
        insideCityView4.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsideRoutesHolder.getInstance().clear();
                InsideRoutesHolder.getInstance().addAll(insideRoutes4);
                startActivityForResult(new Intent(BriefResultActivity.this, ChooseInsideActivity.class)
                        .putExtra("departPosition", route.getThirdWay().getToStation().getName())
                        .putExtra("arrivePosition", Format.getNameByReverseGeoCodeResult(arrivePosition)), 14);
            }
        });

        insideCityView1.getLinearStep().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStepList();
                showInterRouteOnMap(insideRoutes1);
            }
        });

        insideCityView2.getLinearStep().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStepList();
                showInterRouteOnMap(insideRoutes2);
            }
        });

        insideCityView3.getLinearStep().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStepList();
                showInterRouteOnMap(insideRoutes3);
            }
        });

        insideCityView4.getLinearStep().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStepList();
                showInterRouteOnMap(insideRoutes4);
            }
        });

        interCityView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStepList();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(route.getFirstWay().getFromStation().getLatLng());
                builder.include(route.getFirstWay().getToStation().getLatLng());
                map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build(), 16, 16, 16, 16), 1000);
            }
        });

        interCityView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStepList();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(route.getSecondWay().getFromStation().getLatLng());
                builder.include(route.getSecondWay().getToStation().getLatLng());
                map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build(), 16, 16, 16, 16), 1000);
            }
        });

        interCityView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStepList();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(route.getThirdWay().getFromStation().getLatLng());
                builder.include(route.getThirdWay().getToStation().getLatLng());
                map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build(), 16, 16, 16, 16), 1000);
            }
        });

        constraintTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expended) {
                    constraintSet.setVerticalBias(R.id.brief_constraint_1, 1);
                    expended = false;
                } else {
                    constraintSet.setVerticalBias(R.id.brief_constraint_1, 0);
                    expended = true;
                }
                transition.setDuration(0);
                TransitionManager.beginDelayedTransition(constraintWhole, transition);
                constraintSet.applyTo(constraintWhole);
            }
        });

        linearGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BriefResultActivity.this);
                View view = LayoutInflater.from(BriefResultActivity.this).inflate(R.layout.dialog_confirm, null);
                TextView cancel = view.findViewById(R.id.dialog_confirm_text_cancel);
                cancel.setText("取消操作");
                TextView confirm = view.findViewById(R.id.dialog_confirm_text_confirm);
                confirm.setText("加入待出行");
                ((ImageView) view.findViewById(R.id.dialog_confirm_img)).setImageResource(R.drawable.ic_bee_going);
                ((TextView) view.findViewById(R.id.dialog_confirm_text_title)).setText("待出行提示");
                ((TextView) view.findViewById(R.id.dialog_confirm_text_hint)).setText("您是否将该路径加入待出行？");
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user.getState() == 0) return;
                        new RecordNet(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                Toast.makeText(BriefResultActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                            }
                        }).addGoing(
                                user.getTelephone(),
                                gson.toJson(departPosition),
                                gson.toJson(arrivePosition),
                                route, date);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });

        linearCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BriefResultActivity.this);
                View view = LayoutInflater.from(BriefResultActivity.this).inflate(R.layout.dialog_confirm, null);
                TextView cancel = view.findViewById(R.id.dialog_confirm_text_cancel);
                cancel.setText("取消操作");
                TextView confirm = view.findViewById(R.id.dialog_confirm_text_confirm);
                confirm.setText("收藏点赞");
                ((ImageView) view.findViewById(R.id.dialog_confirm_img)).setImageResource(R.drawable.ic_bee_collection);
                ((TextView) view.findViewById(R.id.dialog_confirm_text_title)).setText("收藏提示");
                ((TextView) view.findViewById(R.id.dialog_confirm_text_hint)).setText("您是否收藏该路径？");
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user.getState() == 0) return;
                        new RecordNet(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                Toast.makeText(BriefResultActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                            }
                        }).addCollection(
                                user.getTelephone(),
                                gson.toJson(departPosition),
                                gson.toJson(arrivePosition),
                                route, date);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        int x = data.getIntExtra("index", -1);
        if (x == -1) return;

        InsideRoute insideRoute;
        switch (requestCode) {
            case 11:
                insideRoute = insideRoutes1.get(0);
                insideRoutes1.set(0, insideRoutes1.get(x));
                insideRoutes1.set(x, insideRoute);
                rePaintMap();
                break;
            case 12:
                insideRoute = insideRoutes2.get(0);
                insideRoutes2.set(0, insideRoutes2.get(x));
                insideRoutes2.set(x, insideRoute);
                rePaintMap();
                break;
            case 13:
                insideRoute = insideRoutes3.get(0);
                insideRoutes3.set(0, insideRoutes3.get(x));
                insideRoutes3.set(x, insideRoute);
                rePaintMap();
                break;
            case 14:
                insideRoute = insideRoutes4.get(0);
                insideRoutes4.set(0, insideRoutes4.get(x));
                insideRoutes4.set(x, insideRoute);
                rePaintMap();
                break;
            case 21:
                route.setFirstWay(gson.fromJson(data.getStringExtra("route"), Route.class).getFirstWay());
                route.setPrice();
                route.setTime();
                route.setComfort();
                rePaintMap();
                break;
            case 22:
                route.setSecondWay(gson.fromJson(data.getStringExtra("route"), Route.class).getFirstWay());
                route.setPrice();
                route.setTime();
                route.setComfort();
                rePaintMap();
                break;
            case 23:
                route.setThirdWay(gson.fromJson(data.getStringExtra("route"), Route.class).getFirstWay());
                route.setPrice();
                route.setTime();
                route.setComfort();
                rePaintMap();
                break;
        }
    }

    private void paintInter(Way way) {
        List<LatLng> points = new ArrayList<>();
        points.add(way.getFromStation().getLatLng());
        points.add(way.getToStation().getLatLng());

        BitmapDescriptor bitmapDescriptor;
        if (way.getType().equals("F")) {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.road_red_arrow);
        } else if (way.getType().equals("K")) {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.road_green_arrow);
        } else {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.road_blue_arrow);
        }

        map.addOverlay(new PolylineOptions()
                .width(12)
                .points(points)
                .dottedLine(true)
                .keepScale(true)
                .customTexture(bitmapDescriptor));
    }

    private void paintInside(List<Step> steps) {
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
        }

        map.addOverlay(new MarkerOptions()
                .position(steps.get(steps.size() - 1).getPaths().get(steps.get(steps.size() - 1).getPaths().size() - 1))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_end_point))
                .scaleX(0.2f).scaleY(0.2f));
    }

    private void setStationBg(TextView view, String type) {
        if (type.equals("F")) {
            view.setBackgroundResource(R.drawable.border_green_4);
            view.setTextColor(0xff1FC756);
        } else {
            view.setBackgroundResource(R.drawable.border_orange_4);
            view.setTextColor(0xffEd6a00);
        }
    }

    private String getDurationFormat(long time) {
        long hour = time / 3600000;
        long min = (time % 3600000) / 1000 / 60;
        String t = "";
        t = t + hour + "时";
        t = t + min + "分";
        return t;
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    private void initThreeWay() {
        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes1 = (List<InsideRoute>) msg.obj;
                if (insideRoutes1.size() != 0) {
                    insideCityView1.setTextTitleText(Format.getNameByReverseGeoCodeResult(departPosition) + "-" + route.getFirstWay().getFromStation().getName());
                    showInside1(insideRoutes1.get(0));
                } else {
                    insideCityView1.setTextTitleText("站内乘坐");
                    insideCityView1.setTextDistance("共0千米");
                    insideCityView1.setTextTimeText("约0分");
                    insideCityView1.getTextTitle().setClickable(false);
                }
                insideCityView1.setVisibility(View.VISIBLE);
            }
        }).get(departPosition.getLocation(), route.getFirstWay().getFromStation().getLatLng());

        showInter1(route.getFirstWay());

        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes2 = (List<InsideRoute>) msg.obj;
                if (insideRoutes2.size() != 0) {
                    insideCityView2.setTextTitleText(route.getFirstWay().getToStation().getName() + "-" + route.getSecondWay().getFromStation().getName());
                    showInside2(insideRoutes2.get(0));
                } else {
                    insideCityView2.setTextTitleText("站内换乘");
                    insideCityView2.setTextDistance("共0千米");
                    insideCityView2.setTextTimeText("约0分");
                    insideCityView2.getTextTitle().setClickable(false);
                }
                insideCityView2.setVisibility(View.VISIBLE);
            }
        }).get(route.getFirstWay().getToStation().getLatLng(), route.getSecondWay().getFromStation().getLatLng());

        showInter2(route.getSecondWay());

        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes3 = (List<InsideRoute>) msg.obj;
                if (insideRoutes3.size() != 0) {
                    insideCityView3.setTextTitleText(route.getSecondWay().getToStation().getName() + "-" + route.getThirdWay().getFromStation().getName());
                    showInside3(insideRoutes3.get(0));
                } else {
                    insideCityView3.setTextTitleText("站内换乘");
                    insideCityView3.setTextDistance("共0千米");
                    insideCityView3.setTextTimeText("约0分");
                    insideCityView3.getTextTitle().setClickable(false);
                }
                insideCityView3.setVisibility(View.VISIBLE);
            }
        }).get(route.getSecondWay().getToStation().getLatLng(), route.getThirdWay().getFromStation().getLatLng());

        showInter3(route.getThirdWay());

        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes4 = (List<InsideRoute>) msg.obj;
                if (insideRoutes4.size() != 0) {
                    insideCityView4.setTextTitleText(route.getSecondWay().getToStation().getName() + "-" + Format.getNameByReverseGeoCodeResult(arrivePosition));
                    showInside4(insideRoutes4.get(0));
                } else {
                    insideCityView4.setTextTitleText("站内到达");
                    insideCityView4.setTextDistance("共0千米");
                    insideCityView4.setTextTimeText("约0分");
                    insideCityView4.getTextTitle().setClickable(false);
                }
                insideCityView4.setVisibility(View.VISIBLE);
            }
        }).get(route.getThirdWay().getToStation().getLatLng(), arrivePosition.getLocation());
    }

    //一次转乘
    private void initTwoWay() {
        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes1 = (List<InsideRoute>) msg.obj;
                if (insideRoutes1.size() != 0) {
                    insideCityView1.setTextTitleText(Format.getNameByReverseGeoCodeResult(departPosition) + "-" + route.getFirstWay().getFromStation().getName());
                    showInside1(insideRoutes1.get(0));
                } else {
                    insideCityView1.setTextTitleText("站内乘坐");
                    insideCityView1.setTextDistance("共0千米");
                    insideCityView1.setTextTimeText("约0分");
                    insideCityView1.getTextTitle().setClickable(false);
                }
                insideCityView1.setVisibility(View.VISIBLE);
            }
        }).get(departPosition.getLocation(), route.getFirstWay().getFromStation().getLatLng());

        showInter1(route.getFirstWay());

        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes2 = (List<InsideRoute>) msg.obj;
                if (insideRoutes2.size() != 0) {
                    insideCityView2.setTextTitleText(route.getFirstWay().getToStation().getName() + "-" + route.getSecondWay().getFromStation().getName());
                    showInside2(insideRoutes2.get(0));
                } else {
                    insideCityView2.setTextTitleText("站内换乘");
                    insideCityView2.setTextDistance("共0千米");
                    insideCityView2.setTextTimeText("约0分");
                    insideCityView2.getTextTitle().setClickable(false);
                }
                insideCityView2.setVisibility(View.VISIBLE);
            }
        }).get(route.getFirstWay().getToStation().getLatLng(), route.getSecondWay().getFromStation().getLatLng());

        showInter2(route.getSecondWay());

        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes3 = (List<InsideRoute>) msg.obj;
                if (insideRoutes3.size() != 0) {
                    insideCityView3.setTextTitleText(route.getSecondWay().getToStation().getName() + "-" + Format.getNameByReverseGeoCodeResult(arrivePosition));
                    showInside3(insideRoutes3.get(0));
                } else {
                    insideCityView3.setTextTitleText("站内到达");
                    insideCityView3.setTextDistance("共0千米");
                    insideCityView3.setTextTimeText("约0分");
                    insideCityView3.getTextTitle().setClickable(false);
                }
                insideCityView3.setVisibility(View.VISIBLE);
            }
        }).get(route.getSecondWay().getToStation().getLatLng(), arrivePosition.getLocation());
    }

    //零次转乘
    private void initOneWay() {
        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes1 = (List<InsideRoute>) msg.obj;
                if (insideRoutes1.size() != 0) {
                    insideCityView1.setTextTitleText(Format.getNameByReverseGeoCodeResult(departPosition) + "-" + route.getFirstWay().getFromStation().getName());
                    showInside1(insideRoutes1.get(0));
                } else {
                    insideCityView1.setTextTitleText("站内乘坐");
                    insideCityView1.setTextDistance("共0千米");
                    insideCityView1.setTextTimeText("约0分");
                    insideCityView1.getTextTitle().setClickable(false);
                }
                insideCityView1.setVisibility(View.VISIBLE);
            }
        }).get(departPosition.getLocation(), route.getFirstWay().getFromStation().getLatLng());

        showInter1(route.getFirstWay());

        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes2 = (List<InsideRoute>) msg.obj;
                if (insideRoutes2.size() != 0) {
                    insideCityView2.setTextTitleText(route.getFirstWay().getToStation().getName() + "-" + Format.getNameByReverseGeoCodeResult(arrivePosition));
                    showInside2(insideRoutes2.get(0));
                } else {
                    insideCityView2.setTextTitleText("站内到达");
                    insideCityView2.setTextDistance("共0千米");
                    insideCityView2.setTextTimeText("约0分");
                    insideCityView2.getTextTitle().setClickable(false);
                }
                insideCityView2.setVisibility(View.VISIBLE);
            }
        }).get(route.getFirstWay().getToStation().getLatLng(), arrivePosition.getLocation());
    }

    private void showInside1(InsideRoute insideRoute) {
        insideCityView1.getLinearStep().removeAllViews();
        insideCityView1.setTextDistance("共" + String.format("%.1f", insideRoute.getDistance() * 1.0 / 1000) + "千米");
        insideCityView1.setTextTimeText("约" + String.format("%.1f", insideRoute.getDuration() * 1.0 / 60) + "分");
        paintInside(insideRoute.getSteps());
        for (Step step : insideRoute.getSteps()) {
            final Step tmp = step;
            ItemInsideView itemInsideView = new ItemInsideView(getBaseContext());
            itemInsideView.setView(step);
            itemInsideView.getTextGuide().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalkRouteNodeInfo startPt = new WalkRouteNodeInfo();
                    startPt.setLocation(tmp.getPaths().get(0));
                    WalkRouteNodeInfo endPt = new WalkRouteNodeInfo();
                    endPt.setLocation(tmp.getPaths().get(tmp.getPaths().size() - 1));
                    final WalkNaviLaunchParam param = new WalkNaviLaunchParam().startNodeInfo(startPt).endNodeInfo(endPt);
                    WalkNavigateHelper.getInstance().initNaviEngine(BriefResultActivity.this, new IWEngineInitListener() {
                        @Override
                        public void engineInitSuccess() {
                            WalkNavigateHelper.getInstance().routePlanWithRouteNode(param, new IWRoutePlanListener() {
                                @Override
                                public void onRoutePlanStart() {

                                }

                                @Override
                                public void onRoutePlanSuccess() {
                                    startActivity(new Intent(BriefResultActivity.this, GuideActivity.class));
                                }

                                @Override
                                public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {

                                }
                            });
                        }

                        @Override
                        public void engineInitFail() {
                        }
                    });
                }
            });
            insideCityView1.addItemInsideView(itemInsideView);
        }
    }

    private void showInside2(InsideRoute insideRoute) {
        insideCityView2.getLinearStep().removeAllViews();
        insideCityView2.setTextDistance("共" + String.format("%.1f", insideRoute.getDistance() * 1.0 / 1000) + "千米");
        insideCityView2.setTextTimeText("约" + String.format("%.1f", insideRoute.getDuration() * 1.0 / 60) + "分");
        paintInside(insideRoute.getSteps());
        for (Step step : insideRoute.getSteps()) {
            final Step tmp = step;
            ItemInsideView itemInsideView = new ItemInsideView(getBaseContext());
            itemInsideView.setView(step);
            itemInsideView.getTextGuide().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalkRouteNodeInfo startPt = new WalkRouteNodeInfo();
                    startPt.setLocation(tmp.getPaths().get(0));
                    WalkRouteNodeInfo endPt = new WalkRouteNodeInfo();
                    endPt.setLocation(tmp.getPaths().get(tmp.getPaths().size() - 1));
                    final WalkNaviLaunchParam param = new WalkNaviLaunchParam().startNodeInfo(startPt).endNodeInfo(endPt);
                    WalkNavigateHelper.getInstance().initNaviEngine(BriefResultActivity.this, new IWEngineInitListener() {
                        @Override
                        public void engineInitSuccess() {
                            WalkNavigateHelper.getInstance().routePlanWithRouteNode(param, new IWRoutePlanListener() {
                                @Override
                                public void onRoutePlanStart() {

                                }

                                @Override
                                public void onRoutePlanSuccess() {
                                    startActivity(new Intent(BriefResultActivity.this, GuideActivity.class));
                                }

                                @Override
                                public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {

                                }
                            });
                        }

                        @Override
                        public void engineInitFail() {
                        }
                    });
                }
            });
            insideCityView2.addItemInsideView(itemInsideView);
        }
    }

    private void showInside3(InsideRoute insideRoute) {
        insideCityView3.getLinearStep().removeAllViews();
        insideCityView3.setTextDistance("共" + String.format("%.1f", insideRoute.getDistance() * 1.0 / 1000) + "千米");
        insideCityView3.setTextTimeText("约" + String.format("%.1f", insideRoute.getDuration() * 1.0 / 60) + "分");
        paintInside(insideRoute.getSteps());
        for (Step step : insideRoute.getSteps()) {
            final Step tmp = step;
            ItemInsideView itemInsideView = new ItemInsideView(getBaseContext());
            itemInsideView.setView(step);
            itemInsideView.getTextGuide().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalkRouteNodeInfo startPt = new WalkRouteNodeInfo();
                    startPt.setLocation(tmp.getPaths().get(0));
                    WalkRouteNodeInfo endPt = new WalkRouteNodeInfo();
                    endPt.setLocation(tmp.getPaths().get(tmp.getPaths().size() - 1));
                    final WalkNaviLaunchParam param = new WalkNaviLaunchParam().startNodeInfo(startPt).endNodeInfo(endPt);
                    WalkNavigateHelper.getInstance().initNaviEngine(BriefResultActivity.this, new IWEngineInitListener() {
                        @Override
                        public void engineInitSuccess() {
                            WalkNavigateHelper.getInstance().routePlanWithRouteNode(param, new IWRoutePlanListener() {
                                @Override
                                public void onRoutePlanStart() {

                                }

                                @Override
                                public void onRoutePlanSuccess() {
                                    startActivity(new Intent(BriefResultActivity.this, GuideActivity.class));
                                }

                                @Override
                                public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {

                                }
                            });
                        }

                        @Override
                        public void engineInitFail() {
                        }
                    });
                }
            });
            insideCityView3.addItemInsideView(itemInsideView);
        }
    }

    private void showInside4(InsideRoute insideRoute) {
        insideCityView4.getLinearStep().removeAllViews();
        insideCityView4.setTextDistance("共" + String.format("%.1f", insideRoute.getDistance() * 1.0 / 1000) + "千米");
        insideCityView4.setTextTimeText("约" + String.format("%.1f", insideRoute.getDuration() * 1.0 / 60) + "分");
        paintInside(insideRoute.getSteps());
        for (Step step : insideRoute.getSteps()) {
            final Step tmp = step;
            ItemInsideView itemInsideView = new ItemInsideView(getBaseContext());
            itemInsideView.setView(step);
            itemInsideView.getTextGuide().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalkRouteNodeInfo startPt = new WalkRouteNodeInfo();
                    startPt.setLocation(tmp.getPaths().get(0));
                    WalkRouteNodeInfo endPt = new WalkRouteNodeInfo();
                    endPt.setLocation(tmp.getPaths().get(tmp.getPaths().size() - 1));
                    final WalkNaviLaunchParam param = new WalkNaviLaunchParam().startNodeInfo(startPt).endNodeInfo(endPt);
                    WalkNavigateHelper.getInstance().initNaviEngine(BriefResultActivity.this, new IWEngineInitListener() {
                        @Override
                        public void engineInitSuccess() {
                            WalkNavigateHelper.getInstance().routePlanWithRouteNode(param, new IWRoutePlanListener() {
                                @Override
                                public void onRoutePlanStart() {

                                }

                                @Override
                                public void onRoutePlanSuccess() {
                                    startActivity(new Intent(BriefResultActivity.this, GuideActivity.class));
                                }

                                @Override
                                public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {

                                }
                            });
                        }

                        @Override
                        public void engineInitFail() {
                        }
                    });
                }
            });
            insideCityView4.addItemInsideView(itemInsideView);
        }
    }

    private void showInter1(Way way) {
        interCityView1.showView(way);
        textSt1.setText(way.getFromStation().getName());
        textEn1.setText(way.getToStation().getName());
        setStationBg(textSt1, way.getType());
        setStationBg(textEn1, way.getType());
        paintInter(way);
        interCityView1.setVisibility(View.VISIBLE);
        linear1.setVisibility(View.VISIBLE);
    }

    private void showInter2(Way way) {
        interCityView2.showView(way);
        textSt2.setText(way.getFromStation().getName());
        textEn2.setText(way.getToStation().getName());
        setStationBg(textSt2, way.getType());
        setStationBg(textEn2, way.getType());
        paintInter(way);
        interCityView2.setVisibility(View.VISIBLE);
        linear2.setVisibility(View.VISIBLE);
    }

    private void showInter3(Way way) {
        interCityView3.showView(way);
        textSt3.setText(way.getFromStation().getName());
        textEn3.setText(way.getToStation().getName());
        setStationBg(textSt3, way.getType());
        setStationBg(textEn3, way.getType());
        paintInter(way);
        interCityView3.setVisibility(View.VISIBLE);
        linear3.setVisibility(View.VISIBLE);
    }

    private void hideStepList() {
        constraintSet.setVerticalBias(R.id.brief_constraint_1, 1);
        expended = false;
        transition.setDuration(0);
        TransitionManager.beginDelayedTransition(constraintWhole, transition);
        constraintSet.applyTo(constraintWhole);
    }

    private void showInterRouteOnMap(List<InsideRoute> insideRoutes) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (InsideRoute insideRoute : insideRoutes) {
            for (Step step : insideRoute.getSteps()) {
                for (LatLng path : step.getPaths()) {
                    builder.include(path);
                }
            }
        }
        map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build(), 16, 16, 16, 16), 1000);
    }

    private void rePaintMap() {
        map.clear();
        if (insideRoutes1 != null && insideRoutes1.size() != 0) showInside1(insideRoutes1.get(0));
        if (insideRoutes2 != null && insideRoutes2.size() != 0) showInside2(insideRoutes2.get(0));
        if (insideRoutes3 != null && insideRoutes3.size() != 0) showInside3(insideRoutes3.get(0));
        if (insideRoutes4 != null && insideRoutes4.size() != 0) showInside4(insideRoutes4.get(0));
        showInter1(route.getFirstWay());
        if (route.getSecondWay() != null) showInter2(route.getSecondWay());
        if (route.getThirdWay() != null) showInter3(route.getThirdWay());
        textPrice.setText("￥" + route.getPrice() + "起");
        textTime.setText(getDurationFormat(route.getTime()));
    }
}
