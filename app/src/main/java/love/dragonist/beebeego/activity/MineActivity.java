package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.adapter.MineViewPagerAdapter;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.bean.User;
import love.dragonist.beebeego.fragment.AccountFragment;
import love.dragonist.beebeego.fragment.RecordFragment;
import love.dragonist.beebeego.fragment.SystemFragment;

public class MineActivity extends AppCompatActivity implements AccountFragment.OnFragmentInteractionListener, RecordFragment.OnFragmentInteractionListener, SystemFragment.OnFragmentInteractionListener {
    private List<Fragment> fragments;
    private AccountFragment accountFragment;
    private RecordFragment recordFragment;
    private SystemFragment systemFragment;
    private ConstraintLayout constraintAccount;
    private ConstraintLayout constraintRecord;
    private ConstraintLayout constraintSystem;
    private MineViewPagerAdapter mineViewPagerAdapter;
    private TextView textTelephone;
    private Button btnLogReg;
    private ViewPager viewPager;
    private LinearLayout linearRecord;
    private LinearLayout linearGoing;

    private List<ImageView> imgIcons;
    private List<TextView> textTitles;
    private List<ConstraintLayout> constraintLayoutList;
    private List<ConstraintLayout> constraintMenus;
    private User user;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        gson = new Gson();
        Intent intent = getIntent();
        user = gson.fromJson(intent.getStringExtra("user"), User.class);
        Bundle bundle = new Bundle();
        bundle.putString("user", intent.getStringExtra("user"));
        fragments = new ArrayList<>();
        accountFragment = new AccountFragment();
        fragments.add(accountFragment);
        recordFragment = new RecordFragment();
        fragments.add(recordFragment);
        recordFragment.setArguments(bundle);
        systemFragment = new SystemFragment();
        fragments.add(systemFragment);
        imgIcons = new ArrayList<>();
        textTitles = new ArrayList<>();

        sharedPreferences = getSharedPreferences("beebeego", MODE_PRIVATE);
    }

    private void initView() {
        viewPager = findViewById(R.id.mine_viewPager);
        mineViewPagerAdapter = new MineViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mineViewPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        imgIcons.add((ImageView) findViewById(R.id.mine_img_account));
        imgIcons.add((ImageView) findViewById(R.id.mine_img_record));
        imgIcons.add((ImageView) findViewById(R.id.mine_img_system));
        textTitles.add((TextView) findViewById(R.id.mine_text_account));
        textTitles.add((TextView) findViewById(R.id.mine_text_record));
        textTitles.add((TextView) findViewById(R.id.mine_text_system));
        constraintAccount = findViewById(R.id.mine_constraint_account);
        constraintRecord = findViewById(R.id.mine_constraint_record);
        constraintSystem = findViewById(R.id.mine_constraint_system);
        constraintLayoutList = new ArrayList<>();
        constraintLayoutList.add(constraintAccount);
        constraintLayoutList.add(constraintRecord);
        constraintLayoutList.add(constraintSystem);
        constraintMenus = new ArrayList<>();
        constraintMenus.add((ConstraintLayout) findViewById(R.id.mine_constraint_account_));
        constraintMenus.add((ConstraintLayout) findViewById(R.id.mine_constraint_record_));
        constraintMenus.add((ConstraintLayout) findViewById(R.id.mine_constraint_system_));
        btnLogReg = findViewById(R.id.mine_btn_log_reg);
        textTelephone = findViewById(R.id.mine_text_telephone);
        linearRecord = findViewById(R.id.mine_linear_record_collection);
        linearGoing = findViewById(R.id.mine_linear_record_going);

        if (user.getState() == 1) {
            btnLogReg.setVisibility(View.GONE);
            textTelephone.setText(user.getTelephone());
        }
    }

    private void initListener() {
        linearRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView) linearRecord.getChildAt(0)).setImageTintList(ColorStateList.valueOf(0xff1fc756));
                linearRecord.getChildAt(0).setBackgroundResource(R.drawable.bg_green_10);
                ((TextView) linearRecord.getChildAt(1)).setTextColor(0xff1fc756);
                ((ImageView) linearGoing.getChildAt(0)).setImageTintList(ColorStateList.valueOf(0xffbfbfbf));
                linearGoing.getChildAt(0).setBackgroundResource(R.drawable.bg_white_10);
                ((TextView) linearGoing.getChildAt(1)).setTextColor(0xffbfbfbf);
                recordFragment.setAdapter(true);
            }
        });

        linearGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView) linearGoing.getChildAt(0)).setImageTintList(ColorStateList.valueOf(0xff1fc756));
                linearGoing.getChildAt(0).setBackgroundResource(R.drawable.bg_green_10);
                ((TextView) linearGoing.getChildAt(1)).setTextColor(0xff1fc756);
                ((ImageView) linearRecord.getChildAt(0)).setImageTintList(ColorStateList.valueOf(0xffbfbfbf));
                linearRecord.getChildAt(0).setBackgroundResource(R.drawable.bg_white_10);
                ((TextView) linearRecord.getChildAt(1)).setTextColor(0xffbfbfbf);
                recordFragment.setAdapter(false);
            }
        });

        constraintAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(0);
                viewPager.setCurrentItem(0);
            }
        });

        constraintRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(1);
                viewPager.setCurrentItem(1);
            }
        });

        constraintSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(2);
                viewPager.setCurrentItem(2);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changePage(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        btnLogReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MineActivity.this, LogRegActivity.class), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 1) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            assert data != null;
            edit.putString("telephone", data.getStringExtra("telephone"));
            edit.putString("password", data.getStringExtra("password"));
            edit.apply();
            user.setTelephone(data.getStringExtra("telephone"));
            user.setPassword(data.getStringExtra("password"));
            textTelephone.setText(data.getStringExtra("telephone"));
            btnLogReg.setVisibility(View.GONE);
        }
    }

    private void changePage(int i) {
        for (ImageView imgIcon : imgIcons) {
            imgIcon.setImageTintList(ColorStateList.valueOf(0xffffffff));
        }
        for (TextView textTitle : textTitles) {
            textTitle.setTextColor(0xffffffff);
        }
        for (ConstraintLayout constraintLayout : constraintLayoutList) {
            constraintLayout.setBackgroundResource(R.drawable.bg_white_10_t);
        }
        for (ConstraintLayout constraintMenu : constraintMenus) {
            constraintMenu.setVisibility(View.GONE);
        }
        imgIcons.get(i).setImageTintList(ColorStateList.valueOf(0xff1FC756));
        textTitles.get(i).setTextColor(0xff1FC756);
        constraintLayoutList.get(i).setBackgroundResource(R.drawable.bg_white_10);
        constraintMenus.get(i).setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(JSONObject jsonObject) {
        String fragment = "";
        String action = "";
        try {
            fragment = jsonObject.getString("fragment");
            action = jsonObject.getString("action");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (fragment.equals("account")) {
            if (action.equals("exit")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("telephone");
                editor.remove("password");
                editor.apply();
                textTelephone.setText("请登录");
                btnLogReg.setVisibility(View.VISIBLE);
            }
        } else if (fragment.equals("record")) {
            if (action.equals("brief")) {
                Route route = null;
                ReverseGeoCodeResult depart = null;
                ReverseGeoCodeResult arrive = null;
                try {
                    route = (Route) jsonObject.get("route");
                    depart = (ReverseGeoCodeResult) jsonObject.get("departPosition");
                    arrive = (ReverseGeoCodeResult) jsonObject.get("arrivePosition");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(MineActivity.this, BriefResultActivity.class)
                        .putExtra("route", gson.toJson(route))
                        .putExtra("departPosition", gson.toJson(depart))
                        .putExtra("arrivePosition", gson.toJson(arrive))
                        .putExtra("user", gson.toJson(user))
                        .putExtra("record", true));
            }
        }
    }
}
