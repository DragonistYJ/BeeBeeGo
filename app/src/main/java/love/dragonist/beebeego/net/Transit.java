package love.dragonist.beebeego.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.Price_detail;
import love.dragonist.beebeego.bean.Step;
import love.dragonist.beebeego.bean.Vehicle_info;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Transit {
    private OkHttpClient client;
    private Handler handler;
    private Gson gson;

    public Transit(Handler handler) {
        this.handler = handler;
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
        this.gson = new Gson();
    }

    public void get(LatLng from, LatLng to) {
        String origin = String.format("%.6f", from.latitude) + "," + String.format("%.6f", from.longitude);
        String destination = String.format("%.6f", to.latitude) + "," + String.format("%.6f", to.longitude);

        Request request = new Request.Builder()
                .url("http://api.map.baidu.com/direction/v2/transit?origin=" + origin + "&destination=" + destination + "&ak=ri0iIBgGa0zS4GcO0xER87prUdvZp3DP")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject result = null;
                try {
                    result = new JSONObject(response.body().string()).getJSONObject("result");
                    if (result == null) return;

                    List<InsideRoute> insideRoutes = new ArrayList<>();

                    JSONArray array = result.getJSONArray("routes");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        InsideRoute insideRoute = new InsideRoute();
                        insideRoute.setDistance(object.getInt("distance"));
                        insideRoute.setDuration(object.getInt("duration"));
                        insideRoute.setPrice(object.getInt("price"));

                        JSONArray array1 = object.getJSONArray("price_detail");
                        List<Price_detail> price_details = new ArrayList<>();
                        for (int j = 0; j < array1.length(); j++) {
                            price_details.add(gson.fromJson(array1.getString(j), Price_detail.class));
                        }
                        insideRoute.setPrice_details(price_details);

                        List<Step> steps = new ArrayList<>();
                        array1 = object.getJSONArray("steps");
                        for (int j = 0; j < array1.length(); j++) {
                            Step step = new Step();
                            JSONObject object1 = array1.getJSONArray(j).getJSONObject(0);
                            step.setDistance(object1.getInt("distance"));
                            step.setDuration(object1.getInt("duration"));
                            step.setPaths(object1.getString("path"));
                            step.setInstruction(object1.getString("instructions"));
                            step.setVehicle_info(gson.fromJson(object1.getString("vehicle_info"), Vehicle_info.class));
                            steps.add(step);
                        }
                        insideRoute.setSteps(steps);

                        insideRoutes.add(insideRoute);
                    }

                    Message message = new Message();
                    message.obj = insideRoutes;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
