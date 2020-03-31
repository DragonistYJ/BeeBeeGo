package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.haibin.calendarview.CalendarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.adapter.ResultViewPagerAdapter;
import love.dragonist.beebeego.bean.User;
import love.dragonist.beebeego.fragment.CustomFragment;
import love.dragonist.beebeego.fragment.NonstopFragment;
import love.dragonist.beebeego.fragment.RecommendFragment;
import love.dragonist.beebeego.util.Format;

public class ResultActivity extends AppCompatActivity implements CustomFragment.OnFragmentInteractionListener, RecommendFragment.OnFragmentInteractionListener, NonstopFragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private TextView textCustom;
    private TextView textRecommend;
    private TextView textNonstop;
    private TextView textDepartPosition;
    private TextView textArrivePosition;
    private TextView textTrain;
    private TextView textFlight;
    private TextView textMonthDate;
    private TextView textWeekday;
    private TextView textPreDay;
    private TextView textNextDay;
    private TextView textBigMonth;
    private CalendarView calendarView;
    private ImageView imgBack;
    private LinearLayout linearNonstopChoice;
    private LinearLayout linearDate;
    private ConstraintLayout constraintCalendar;

    private List<TextView> textMethods;
    private List<Fragment> fragments;
    private CustomFragment customFragment;
    private RecommendFragment recommendFragment;
    private NonstopFragment nonstopFragment;
    private ResultViewPagerAdapter resultViewPagerAdapter;
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private Gson gson;
    private Animation animation;
    private Calendar calendar = Calendar.getInstance();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        gson = new Gson();
        Intent intent = getIntent();
        departPosition = gson.fromJson(intent.getStringExtra("departPosition"), ReverseGeoCodeResult.class);
        arrivePosition = gson.fromJson(intent.getStringExtra("arrivePosition"), ReverseGeoCodeResult.class);
        user = gson.fromJson(intent.getStringExtra("user"), User.class);
        Bundle bundle = new Bundle();
        bundle.putString("departPosition", intent.getStringExtra("departPosition"));
        bundle.putString("arrivePosition", intent.getStringExtra("arrivePosition"));

        fragments = new ArrayList<>();
        customFragment = new CustomFragment();
        customFragment.setArguments(bundle);
        recommendFragment = new RecommendFragment();
        recommendFragment.setArguments(bundle);
        nonstopFragment = new NonstopFragment();
        nonstopFragment.setArguments(bundle);
        fragments.add(customFragment);
        fragments.add(recommendFragment);
        fragments.add(nonstopFragment);
        resultViewPagerAdapter = new ResultViewPagerAdapter(getSupportFragmentManager(), fragments);
        textMethods = new ArrayList<>();
    }

    private void initView() {
        viewPager = findViewById(R.id.result_viewPager);
        viewPager.setAdapter(resultViewPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);

        imgBack = findViewById(R.id.result_img_back);
        textCustom = findViewById(R.id.result_text_method_custom);
        textRecommend = findViewById(R.id.result_text_method_recommend);
        textNonstop = findViewById(R.id.result_text_method_nonstop);
        textMethods.add(textCustom);
        textMethods.add(textRecommend);
        textMethods.add(textNonstop);
        textDepartPosition = findViewById(R.id.result_text_depart_position);
        textDepartPosition.setText(Format.getNameByReverseGeoCodeResult(departPosition));
        textArrivePosition = findViewById(R.id.result_text_arrive_position);
        textArrivePosition.setText(Format.getNameByReverseGeoCodeResult(arrivePosition));
        textTrain = findViewById(R.id.result_text_train);
        textFlight = findViewById(R.id.result_text_flight);
        textMonthDate = findViewById(R.id.result_text_month_date);
        textWeekday = findViewById(R.id.result_text_weekday);
        textBigMonth = findViewById(R.id.result_calendar_month);
        showDate();
        textPreDay = findViewById(R.id.result_text_date_pre);
        textNextDay = findViewById(R.id.result_text_date_next);
        calendarView = findViewById(R.id.result_calendar);
        linearNonstopChoice = findViewById(R.id.result_linear_choice);
        linearDate = findViewById(R.id.result_linear_date_);
        constraintCalendar = findViewById(R.id.result_constraint_calendar);
    }

    private void initListener() {
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(com.haibin.calendarview.Calendar c, boolean isClick) {
                if (!isClick) return;
                calendar.set(c.getYear(), c.getMonth() - 1, c.getDay());
                showDate();
                animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_top);
                constraintCalendar.startAnimation(animation);
                constraintCalendar.setVisibility(View.INVISIBLE);
            }
        });

        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                textBigMonth.setText(month + "");
            }
        });

        linearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (constraintCalendar.getVisibility() == View.INVISIBLE) {
                    animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_in_top_slow);
                    constraintCalendar.startAnimation(animation);
                    constraintCalendar.setVisibility(View.VISIBLE);
                } else {
                    animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_top);
                    constraintCalendar.startAnimation(animation);
                    constraintCalendar.setVisibility(View.INVISIBLE);
                }
            }
        });

        textPreDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                showDate();
                calendarView.scrollToCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            }
        });

        textNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                showDate();
                calendarView.scrollToCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            }
        });

        textCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearNonstopChoice.setVisibility(View.INVISIBLE);
                changePage(0);
                viewPager.setCurrentItem(0);
            }
        });

        textRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearNonstopChoice.setVisibility(View.INVISIBLE);
                changePage(1);
                viewPager.setCurrentItem(1);
            }
        });

        textNonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(2);
                viewPager.setCurrentItem(2);

                linearNonstopChoice.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_in_top);
                linearNonstopChoice.startAnimation(animation);
            }
        });

        textTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonstopFragment.tranOrFlight(true);
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_top);
                linearNonstopChoice.startAnimation(animation);
                linearNonstopChoice.setVisibility(View.INVISIBLE);
            }
        });

        textFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonstopFragment.tranOrFlight(false);
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_top);
                linearNonstopChoice.startAnimation(animation);
                linearNonstopChoice.setVisibility(View.INVISIBLE);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i != 2) linearNonstopChoice.setVisibility(View.INVISIBLE);
                changePage(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onFragmentInteraction(JSONObject jsonObject) {
        try {
            startActivity(new Intent(ResultActivity.this, BriefResultActivity.class)
                    .putExtra("route", jsonObject.getString("route"))
                    .putExtra("departPosition", gson.toJson(departPosition))
                    .putExtra("arrivePosition", gson.toJson(arrivePosition))
                    .putExtra("user", gson.toJson(user))
                    .putExtra("date", new SimpleDateFormat("YYYY-MM-dd").format(calendar.getTime()))
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDate() {
        textMonthDate.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
        textWeekday.setText(chineseWeekday(calendar.get(Calendar.DAY_OF_WEEK)));
        textBigMonth.setText((calendar.get(Calendar.MONTH) + 1) + "");
    }

    private void changePage(int i) {
        for (TextView textMethod : textMethods) {
            textMethod.setTextColor(0xffffffff);
            textMethod.setBackground(null);
        }
        textMethods.get(i).setTextColor(0xff000000);
        textMethods.get(i).setBackgroundResource(R.drawable.bg_gray_10_topleft_topright);
    }

    private String chineseWeekday(int i) {
        if (i == 2) return "星期一";
        else if (i == 3) return "星期二";
        else if (i == 4) return "星期三";
        else if (i == 5) return "星期四";
        else if (i == 6) return "星期五";
        else if (i == 7) return "星期六";
        else return "星期天";
    }
}
