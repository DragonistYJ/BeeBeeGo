package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.InsideRoutesHolder;
import love.dragonist.beebeego.net.Transit;
import love.dragonist.beebeego.util.Format;
import love.dragonist.beebeego.view.ChooseInsideView;

public class InsideActivity extends AppCompatActivity {
    private TextView textStart;
    private TextView textEnd;
    private ImageView imgBack;
    private LinearLayout linearChoice;

    private List<InsideRoute> insideRoutes;
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        gson = new Gson();
        Intent intent = getIntent();
        departPosition = gson.fromJson(intent.getStringExtra("departPosition"), ReverseGeoCodeResult.class);
        arrivePosition = gson.fromJson(intent.getStringExtra("arrivePosition"), ReverseGeoCodeResult.class);
        insideRoutes = new ArrayList<>();
    }

    private void initView() {
        imgBack = findViewById(R.id.inside_img_back);
        textStart = findViewById(R.id.inside_text_start);
        textStart.setText(Format.getNameByReverseGeoCodeResult(departPosition));
        textEnd = findViewById(R.id.inside_text_end);
        textEnd.setText(Format.getNameByReverseGeoCodeResult(arrivePosition));

        linearChoice = findViewById(R.id.item_inside_linear);
        for (InsideRoute insideRoute : insideRoutes) {
            ChooseInsideView chooseInsideView = new ChooseInsideView(this);
            chooseInsideView.setView(insideRoute, this);
            linearChoice.addView(chooseInsideView);
        }
        new Transit(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                insideRoutes.addAll((Collection<? extends InsideRoute>) msg.obj);
                for (final InsideRoute insideRoute : insideRoutes) {
                    ChooseInsideView chooseInsideView = new ChooseInsideView(getBaseContext());
                    chooseInsideView.setView(insideRoute, getBaseContext());
                    chooseInsideView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(InsideActivity.this, InsideBriefActivity.class)
                                    .putExtra("insideRoute", gson.toJson(insideRoute))
                                    .putExtra("departPosition", gson.toJson(departPosition))
                                    .putExtra("arrivePosition", gson.toJson(arrivePosition)));
                        }
                    });
                    linearChoice.addView(chooseInsideView);
                }
            }
        }).get(departPosition.getLocation(), arrivePosition.getLocation());
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
