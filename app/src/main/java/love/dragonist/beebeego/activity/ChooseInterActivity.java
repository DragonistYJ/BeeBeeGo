package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.adapter.NonstopListAdapter;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.bean.Way;
import love.dragonist.beebeego.net.Nonstop;

public class ChooseInterActivity extends AppCompatActivity {
    private TextView textStart;
    private TextView textEnd;
    private TextView textDepartTime;
    private TextView textPrice;
    private TextView textTime;
    private ImageView imgBack;
    private ListView listView;

    private String departStation;
    private String arriveStation;
    private List<Route> routes;
    private NonstopListAdapter nonstopListAdapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_inter);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        departStation = intent.getStringExtra("departStation");
        arriveStation = intent.getStringExtra("arriveStation");
        routes = new ArrayList<>();
        nonstopListAdapter = new NonstopListAdapter(this, routes);
        gson = new Gson();

        new Nonstop(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                routes.addAll((Collection<? extends Route>) msg.obj);
                for (Route route : routes) {
                    route.setPrice();
                    Log.e("fsd", String.valueOf(route.getPrice()));
                    Log.e("dsfs", String.valueOf(route.getFirstWay().getPrice()));
                }
                Collections.sort(routes, new Comparator<Route>() {
                    @Override
                    public int compare(Route o1, Route o2) {
                        return Long.compare(o1.getFirstWay().getStartTime().getTime(), o2.getFirstWay().getStartTime().getTime());
                    }
                });
                nonstopListAdapter.notifyDataSetChanged();
            }
        }).betweenStation(departStation, arriveStation);
    }

    private void initView() {
        textStart = findViewById(R.id.choose_inter_text_start);
        textStart.setText(departStation);
        textEnd = findViewById(R.id.choose_inter_text_end);
        textEnd.setText(arriveStation);
        textDepartTime = findViewById(R.id.choose_inter_text_departTime);
        textPrice = findViewById(R.id.choose_inter_text_money);
        textTime = findViewById(R.id.choose_inter_text_time_cost);
        imgBack = findViewById(R.id.choose_inter_img_back);
        listView = findViewById(R.id.choose_inter_list);
        listView.setAdapter(nonstopListAdapter);
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(0, new Intent()
                        .putExtra("index", 0)
                        .putExtra("route", gson.toJson(routes.get(position))));
                finish();
            }
        });

        textDepartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(routes, new Comparator<Route>() {
                    @Override
                    public int compare(Route o1, Route o2) {
                        return Long.compare(o1.getFirstWay().getStartTime().getTime(), o2.getFirstWay().getStartTime().getTime());
                    }
                });
                nonstopListAdapter.notifyDataSetChanged();
                setAllWhite();
                textDepartTime.setTextColor(0xff1FC756);
            }
        });

        textPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(routes, new Comparator<Route>() {
                    @Override
                    public int compare(Route o1, Route o2) {
                        return Double.compare(o1.getPrice(), o2.getPrice());
                    }
                });
                nonstopListAdapter.notifyDataSetChanged();
                setAllWhite();
                textPrice.setTextColor(0xff1FC756);
            }
        });

        textTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(routes, new Comparator<Route>() {
                    @Override
                    public int compare(Route o1, Route o2) {
                        return Long.compare(o1.getTime(), o2.getTime());
                    }
                });
                nonstopListAdapter.notifyDataSetChanged();
                setAllWhite();
                textTime.setTextColor(0xff1FC756);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setAllWhite() {
        textTime.setTextColor(0xffffffff);
        textPrice.setTextColor(0xffffffff);
        textDepartTime.setTextColor(0xffffffff);
    }
}
