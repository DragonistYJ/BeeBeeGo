package love.dragonist.beebeego.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.net.LogReg;

public class LogRegActivity extends AppCompatActivity {
    private ImageView imgBg;
    private ImageView imgLocate;
    private ImageView imgBack;
    private EditText editAccount;
    private EditText editPassword;
    private EditText editCode1;
    private EditText editCode2;
    private Button btnLogin;
    private Button btnCode1;
    private Button btnCode2;
    private TextView textChange;
    private TextView textRegister;

    private String telephone;
    private String password;
    private Boolean byPassword = true;
    private Boolean isLogin = true;
    private String code;
    private CountDownTimer countDownTimer1;
    private CountDownTimer countDownTimer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        countDownTimer1 = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnCode1.setEnabled(false);
                btnCode1.setText("已发送(" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                btnCode1.setEnabled(true);
                btnCode1.setText("获取验证码");
            }
        };
        countDownTimer2 = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnCode2.setEnabled(false);
                btnCode2.setText("已发送(" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                btnCode2.setEnabled(true);
                btnCode2.setText("获取验证码");
            }
        };
    }

    private void initView() {
        imgBg = findViewById(R.id.logreg_bg);
        imgLocate = findViewById(R.id.logreg_locate);
        imgBack = findViewById(R.id.logreg_back);
        editAccount = findViewById(R.id.logreg_phone);
        editPassword = findViewById(R.id.logreg_password);
        editCode1 = findViewById(R.id.logreg_code1);
        editCode2 = findViewById(R.id.logreg_code2);
        btnLogin = findViewById(R.id.logreg_btn);
        btnCode1 = findViewById(R.id.logreg_send_code1);
        btnCode2 = findViewById(R.id.logreg_send_code2);
        textChange = findViewById(R.id.logreg_change);
        textRegister = findViewById(R.id.logreg_register);
    }

    private void initListener() {
        btnCode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAccount.getText().toString().length() < 11) {
                    Toast.makeText(LogRegActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                telephone = editAccount.getText().toString();
                new LogReg(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        code = (String) msg.obj;
                    }
                }).sendSMS(telephone);
                countDownTimer1.start();
                Toast.makeText(LogRegActivity.this, "短信验证码已发送", Toast.LENGTH_SHORT).show();
            }
        });

        btnCode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAccount.getText().toString().length() < 11) {
                    Toast.makeText(LogRegActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                telephone = editAccount.getText().toString();
                new LogReg(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        code = (String) msg.obj;
                    }
                }).sendSMS(telephone);
                countDownTimer2.start();
                Toast.makeText(LogRegActivity.this, "短信验证码已发送", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin && byPassword) loginByPassword();
                if (isLogin && !byPassword) loginByCode();
                if (!isLogin) register();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (byPassword) {
                    editPassword.setVisibility(View.INVISIBLE);
                    editCode1.setVisibility(View.VISIBLE);
                    btnCode1.setVisibility(View.VISIBLE);
                    textChange.setText("密码登录");
                    byPassword = false;
                } else {
                    editPassword.setVisibility(View.VISIBLE);
                    editCode1.setVisibility(View.INVISIBLE);
                    btnCode1.setVisibility(View.INVISIBLE);
                    textChange.setText("验证码登录");
                    byPassword = true;
                }
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    isLogin = false;
                    textChange.setVisibility(View.GONE);
                    btnCode2.setVisibility(View.VISIBLE);
                    editCode2.setVisibility(View.VISIBLE);
                    btnLogin.setText("注        册");
                    textRegister.setText("登录");
                    editPassword.setVisibility(View.VISIBLE);
                    editCode1.setVisibility(View.INVISIBLE);
                    btnCode1.setVisibility(View.INVISIBLE);
                    textChange.setText("验证码登录");
                    byPassword = true;
                } else {
                    isLogin = true;
                    textChange.setVisibility(View.VISIBLE);
                    btnCode2.setVisibility(View.GONE);
                    editCode2.setVisibility(View.GONE);
                    btnLogin.setText("登        录");
                    textRegister.setText("新用户，注册");
                }
            }
        });
    }

    private void register() {
        String telephoneNew = editAccount.getText().toString();
        if (!telephoneNew.equals(telephone)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        password = editPassword.getText().toString();
        if (password.length() < 6) {
            Toast.makeText(this, "请输入6位以上的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (code == null || !code.equals(editCode2.getText().toString())) {
            Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        new LogReg(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                if (result.equals("success")) {
                    setResult(1, new Intent().putExtra("telephone", telephone).putExtra("password", password));
                    finish();
                } else {
                    Toast.makeText(LogRegActivity.this, "帐号已存在", Toast.LENGTH_SHORT).show();
                }
            }
        }).register(telephone, telephone, password);

    }

    private void loginByPassword() {
        telephone = editAccount.getText().toString();
        if (telephone.length() < 11) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        password = editPassword.getText().toString();

        new LogReg(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                if (!result.equals("fail")) {
                    setResult(1, new Intent()
                            .putExtra("telephone", telephone)
                            .putExtra("password", password)
                            .putExtra("p", result));
                    finish();
                }
            }
        }).login(telephone, password);

    }

    private void loginByCode() {
        telephone = editAccount.getText().toString();
        if (telephone.length() < 11) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (code != null && code.equals(editCode1.getText().toString())) {
            new LogReg(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String result = (String) msg.obj;
                    if (!result.equals("fail")) {
                        setResult(1, new Intent()
                                .putExtra("telephone", telephone)
                                .putExtra("password", "")
                                .putExtra("p", result));
                        finish();
                    }
                }
            }).loginByCode(telephone);
        } else {
            Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
        }
    }
}
