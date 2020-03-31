package love.dragonist.beebeego.net;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.bean.Way;
import love.dragonist.beebeego.code.ConnectionCode;
import love.dragonist.beebeego.util.Format;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RoutePlan {
    private OkHttpClient client;
    private Handler handler;
    private Gson gson;

    public RoutePlan(Handler handler) {
        this.handler = handler;
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
        this.gson = new Gson();
    }

    public void post(String startCity, String endCity, double pPrice, double pTime, double pComfort, final int requestCode) {
        Request request = new Request.Builder()
                .url(Addr.IP + "/search/beebeego?fromCity=" + startCity + "&toCity=" + endCity + "&pPrice=" + pPrice + "&pTime=" + pTime + "&pComfort=" + pComfort)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                List<Route> routes = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    String s = response.body().string();
                    s = s.replaceAll("AM", "上午");
                    s = s.replaceAll("PM", "下午");
                    jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Route route = new Route();
                        route.setFirstWay(gson.fromJson(object.getJSONObject("firstWay").toString(), Way.class));
                        route.setSecondWay(gson.fromJson(object.getJSONObject("secondWay").toString(), Way.class));

                        if (object.has("thirdWay")) {
                            route.setThirdWay(gson.fromJson(object.getJSONObject("thirdWay").toString(), Way.class));
                        } else {
                            route.setThirdWay(null);
                        }
                        route.setPrice(object.getDouble("price"));
                        route.setTime(object.getLong("time"));
                        route.setComfort(object.getDouble("comfort"));
                        routes.add(route);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.what = requestCode;
                message.obj = routes;
                handler.sendMessage(message);
            }
        });
    }
}
