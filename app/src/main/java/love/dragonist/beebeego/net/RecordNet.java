package love.dragonist.beebeego.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import love.dragonist.beebeego.bean.Record;
import love.dragonist.beebeego.bean.Route;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordNet {
    private Handler handler;
    private OkHttpClient client;
    private Gson gson;

    public RecordNet(Handler handler) {
        this.handler = handler;
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    public void addGoing(String telephone, String departPosition, String arrivePosition, Route route, String date) {
        String routeString = gson.toJson(route);
        routeString = routeString.replaceAll("上午", "AM");
        routeString = routeString.replaceAll("下午", "PM");

        RequestBody requestBody = new FormBody.Builder()
                .add("telephone", telephone)
                .add("departPosition", departPosition)
                .add("arrivePosition", arrivePosition)
                .add("route", routeString)
                .add("date", date)
                .build();
        Request request = new Request.Builder()
                .url(Addr.IP + "/record/addGoing")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.obj = "加入待出行成功";
                handler.sendMessage(message);
            }
        });
    }

    public void addCollection(String telephone, String departPosition, String arrivePosition, Route route, String date) {
        String routeString = gson.toJson(route);
        routeString = routeString.replaceAll("上午", "AM");
        routeString = routeString.replaceAll("下午", "PM");

        RequestBody requestBody = new FormBody.Builder()
                .add("telephone", telephone)
                .add("departPosition", departPosition)
                .add("arrivePosition", arrivePosition)
                .add("route", routeString)
                .add("date", date)
                .build();
        Request request = new Request.Builder()
                .url(Addr.IP + "/record/addCollection")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.obj = "加入收藏成功";
                handler.sendMessage(message);
            }
        });
    }

    public void getCollection(String telephone) {
        RequestBody requestBody = new FormBody.Builder()
                .add("telephone", telephone)
                .build();
        Request request = new Request.Builder()
                .url(Addr.IP + "/record/collection")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s = response.body().string();
                s = s.replaceAll("AM", "上午");
                s = s.replaceAll("PM", "下午");
                Message message = new Message();
                message.obj = s;
                handler.sendMessage(message);
            }
        });
    }

    public void getGoing(String telephone) {
        RequestBody requestBody = new FormBody.Builder()
                .add("telephone", telephone)
                .build();
        Request request = new Request.Builder()
                .url(Addr.IP + "/record/going")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s = response.body().string();
                s = s.replaceAll("AM", "上午");
                s = s.replaceAll("PM", "下午");

                JSONArray jsonArray = null;
                List<Record> records = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        records.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Record.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.obj = records;
                handler.sendMessage(message);
            }
        });
    }

    public void deleteCollection(Record record) {
        String recordString = gson.toJson(record);
        recordString = recordString.replaceAll("上午", "AM");
        recordString = recordString.replaceAll("下午", "PM");

        RequestBody requestBody = new FormBody.Builder()
                .add("record", recordString)
                .build();
        Request request = new Request.Builder()
                .url(Addr.IP + "/record/deleteCollection")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Message message = new Message();
                message.obj = "success";
                handler.sendMessage(message);
            }
        });
    }

    public void deleteGoing(Record record) {
        String recordString = gson.toJson(record);
        recordString = recordString.replaceAll("上午", "AM");
        recordString = recordString.replaceAll("下午", "PM");

        RequestBody requestBody = new FormBody.Builder()
                .add("record", recordString)
                .build();
        Request request = new Request.Builder()
                .url(Addr.IP + "/record/deleteGoing")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Message message = new Message();
                message.obj = "success";
                handler.sendMessage(message);
            }
        });
    }
}
