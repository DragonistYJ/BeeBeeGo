package love.dragonist.beebeego.net;

import android.os.Handler;
import android.os.Message;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import love.dragonist.beebeego.code.ConnectionCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LogReg {
    private Handler handler;
    private OkHttpClient client;

    public LogReg(Handler handler) {
        this.handler = handler;
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
    }

    public void login(String telephone, String password) {
        Request request = new Request.Builder()
                .url(Addr.IP + "/user/login?telephone=" + telephone + "&password=" + password)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }

    public void loginByCode(String telephone) {
        Request request = new Request.Builder()
                .url("http://" + Addr.IP + "/user/login?telephone=" + telephone)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }

    public void register(String telephone, String username, String password) {
        Request request = new Request.Builder()
                .url("http://" + Addr.IP + "/user/register?telephone=" + telephone + "&password=" + password + "&username=" + username)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }

    public void sendSMS(String telephone) {
        Request request = new Request.Builder()
                .url("http://" + Addr.IP + "/user/sms?telephone=" + telephone)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }
}
