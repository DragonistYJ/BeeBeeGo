package love.dragonist.beebeego.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TimeTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.bean.Way;
import love.dragonist.beebeego.code.ConnectionCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Nonstop {
    private Handler handler;
    private OkHttpClient client;
    private Gson gson;

    public Nonstop(Handler handler) {
        this.handler = handler;
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
        this.gson = new Gson();
    }

    public void post(String fromCity, String toCity) {
        Request request = new Request.Builder()
                .url(Addr.IP + "/search/nonstop?fromCity=" + fromCity + "&toCity=" + toCity)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONArray jsonArray = null;
                List<Route> routes = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String s = jsonArray.getJSONObject(i).toString();
                        s = s.replaceAll("AM", "上午");
                        s = s.replaceAll("PM", "下午");
                        Route route = new Route(gson.fromJson(s, Way.class));
                        routes.add(route);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = routes;
                handler.sendMessage(message);
            }
        });
    }

    public void betweenStation(String fromStation, String toStation) {
        Request request = new Request.Builder()
                .url(Addr.IP + "/search/wayStation?fromStation=" + fromStation + "&toStation=" + toStation)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONArray jsonArray = null;
                List<Route> routes = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String s = jsonArray.getJSONObject(i).toString();
                        s = s.replaceAll("AM", "上午");
                        s = s.replaceAll("PM", "下午");
                        Route route = new Route(gson.fromJson(s, Way.class));
                        routes.add(route);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = routes;
                handler.sendMessage(message);
            }
        });
    }
}
