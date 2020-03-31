package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.google.gson.Gson;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.Step;
import love.dragonist.beebeego.util.Format;
import love.dragonist.beebeego.view.ChooseInsideView;
import love.dragonist.beebeego.view.InsideCityView;
import love.dragonist.beebeego.view.ItemInsideView;

public class InsideBrief2Activity extends AppCompatActivity {
    private ChooseInsideView chooseInsideView;
    private InsideCityView insideCityView;

    private InsideRoute insideRoute;
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_brief2);

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
        chooseInsideView = findViewById(R.id.inside_brief_2_title);
        chooseInsideView.setView(insideRoute, InsideBrief2Activity.this);
        insideCityView = findViewById(R.id.inside_brief_2_context);
        insideCityView.setTextTitleText(Format.getNameByReverseGeoCodeResult(departPosition) + "-" + Format.getNameByReverseGeoCodeResult(arrivePosition));
        insideCityView.setTextDistance("共" + String.format("%.1f", insideRoute.getDistance() * 1.0 / 1000) + "千米");
        insideCityView.setTextTimeText("约" + String.format("%.1f", insideRoute.getDuration() * 1.0 / 60) + "分");
        for (final Step step : insideRoute.getSteps()) {
            ItemInsideView itemInsideView = new ItemInsideView(getBaseContext());
            itemInsideView.setView(step);
            itemInsideView.getTextGuide().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalkRouteNodeInfo startPt = new WalkRouteNodeInfo();
                    startPt.setLocation(step.getPaths().get(0));
                    WalkRouteNodeInfo endPt = new WalkRouteNodeInfo();
                    endPt.setLocation(step.getPaths().get(step.getPaths().size() - 1));
                    final WalkNaviLaunchParam param = new WalkNaviLaunchParam().startNodeInfo(startPt).endNodeInfo(endPt);
                    WalkNavigateHelper.getInstance().initNaviEngine(InsideBrief2Activity.this, new IWEngineInitListener() {
                        @Override
                        public void engineInitSuccess() {
                            WalkNavigateHelper.getInstance().routePlanWithRouteNode(param, new IWRoutePlanListener() {
                                @Override
                                public void onRoutePlanStart() {

                                }

                                @Override
                                public void onRoutePlanSuccess() {
                                    startActivity(new Intent(InsideBrief2Activity.this, GuideActivity.class));
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
            insideCityView.addItemInsideView(itemInsideView);
        }
        insideCityView.getBtnChange().setVisibility(View.INVISIBLE);
        insideCityView.getImgArrow().setVisibility(View.INVISIBLE);
        insideCityView.getLinearStep().setVisibility(View.VISIBLE);
        insideCityView.getTextTitle().setClickable(false);
    }

    private void initListener() {
        chooseInsideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
