package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import love.dragonist.beebeego.R;

public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout linearTitle;
    private LinearLayout linearLogo;
    private TextView textIntroduce;
    private ImageView imgBg;
    private ImageView imgTrain;
    private ImageView imgSubway;
    private ImageView imgFlight;
    private ImageView imgBee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
        initListener();
    }

    private void initView() {
        linearTitle = findViewById(R.id.welcome_linear_title);
        linearLogo = findViewById(R.id.welcome_linear_logo);
        textIntroduce = findViewById(R.id.welcome_text_introduce);
        imgBg = findViewById(R.id.welcome_img_bg);
        imgTrain = findViewById(R.id.welcome_img_train);
        imgSubway = findViewById(R.id.welcome_img_subway);
        imgFlight = findViewById(R.id.welcome_img_flight);
        imgBee = findViewById(R.id.welcome_img_bee);
    }

    private void initListener() {
        CountDownTimer countDownTimer = new CountDownTimer(3000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished >= 2800) {
                    linearTitle.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_bottom_alpha_500));
                    linearTitle.setVisibility(View.VISIBLE);
                    textIntroduce.setAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_top_alpha_500));
                    textIntroduce.setVisibility(View.VISIBLE);
                } else if (millisUntilFinished >= 2300 && millisUntilFinished <= 2400) {
                    linearLogo.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_left_alpha_500));
                    linearLogo.setVisibility(View.VISIBLE);
                } else if (millisUntilFinished >= 2200 && millisUntilFinished <= 2300) {
                    imgBg.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.alpha_500));
                    imgBg.setVisibility(View.VISIBLE);
                } else if (millisUntilFinished >= 1700 && millisUntilFinished <= 1800) {
                    imgTrain.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_bottom_alpha_500));
                    imgTrain.setVisibility(View.VISIBLE);
                } else if (millisUntilFinished >= 1500 && millisUntilFinished <= 1600) {
                    imgSubway.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_bottom_alpha_500));
                    imgSubway.setVisibility(View.VISIBLE);
                } else if (millisUntilFinished >= 1300 && millisUntilFinished <= 1400) {
                    imgFlight.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_bottom_alpha_500));
                    imgFlight.setVisibility(View.VISIBLE);
                } else if (millisUntilFinished >= 1100 && millisUntilFinished <= 1200) {
                    imgBee.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_bottom_alpha_500));
                    imgBee.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        };
        countDownTimer.start();
    }
}
