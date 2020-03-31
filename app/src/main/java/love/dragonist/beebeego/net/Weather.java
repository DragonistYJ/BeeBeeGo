package love.dragonist.beebeego.net;

import android.os.Handler;
import android.os.Message;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Weather {
    private OkHttpClient client;
    private Handler handler;

    public Weather(Handler handler) {
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
        this.handler = handler;
    }

    public void get() {
        Request request = new Request.Builder()
                .url("https://www.tianqiapi.com/api/?version=v1")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject jsonObject = null;
                JSONObject result = new JSONObject();
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                    result.put("air_level", "空气质量" + data.getString("air_level"));
                    result.put("temperature", data.getString("tem"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }
}
