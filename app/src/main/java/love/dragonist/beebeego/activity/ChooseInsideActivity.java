package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.InsideRoutesHolder;
import love.dragonist.beebeego.view.ChooseInsideView;

public class ChooseInsideActivity extends AppCompatActivity {
    private TextView textStart;
    private TextView textEnd;
    private ImageView imgBack;
    private LinearLayout linearChoice;

    private List<InsideRoute> insideRoutes;
    private String departPosition;
    private String arrivePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_inside);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        departPosition = intent.getStringExtra("departPosition");
        arrivePosition = intent.getStringExtra("arrivePosition");
        insideRoutes = new ArrayList<>();
        insideRoutes.addAll(InsideRoutesHolder.getInstance());
    }

    private void initView() {
        imgBack = findViewById(R.id.choose_inside_img_back);
        textStart = findViewById(R.id.choose_inside_text_start);
        textStart.setText(departPosition);
        textEnd = findViewById(R.id.choose_inside_text_end);
        textEnd.setText(arrivePosition);
        linearChoice = findViewById(R.id.item_choose_inside_linear);
        for (InsideRoute insideRoute : insideRoutes) {
            ChooseInsideView chooseInsideView = new ChooseInsideView(this);
            chooseInsideView.setView(insideRoute, this);
            linearChoice.addView(chooseInsideView);
        }
    }

    private void initListener() {
        for (int i = 0; i < insideRoutes.size(); i++) {
            final int finalI = i;
            linearChoice.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(0, new Intent().putExtra("index", finalI));
                    finish();
                }
            });
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
