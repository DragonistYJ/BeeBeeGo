package love.dragonist.beebeego.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.adapter.GoingListAdapter;
import love.dragonist.beebeego.adapter.RecordListAdapter;
import love.dragonist.beebeego.bean.Record;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.bean.User;
import love.dragonist.beebeego.net.RecordNet;

public class RecordFragment extends Fragment {
    private ListView listView;
    private List<Record> routesRecord;
    private List<ReverseGeoCodeResult> departRecord;
    private List<ReverseGeoCodeResult> arriveRecord;
    private List<Integer> zans;
    private List<Record> routesGoing;
    private List<ReverseGeoCodeResult> departGoing;
    private List<ReverseGeoCodeResult> arriveGoing;

    private RecordListAdapter recordListAdapter;
    private GoingListAdapter goingListAdapter;
    private int selected = 1;
    private User user;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private OnFragmentInteractionListener mListener;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = gson.fromJson(getArguments().getString("user"), User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        initData();

        listView = view.findViewById(R.id.record_list);
        listView.setAdapter(recordListAdapter);

        initListener();
        return view;
    }

    private void initData() {
        routesRecord = new ArrayList<>();
        departRecord = new ArrayList<>();
        arriveRecord = new ArrayList<>();
        zans = new ArrayList<>();
        routesGoing = new ArrayList<>();
        departGoing = new ArrayList<>();
        arriveGoing = new ArrayList<>();
        if (user.getState() == 1) {
            new RecordNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray((String) msg.obj);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            zans.add(jsonObject.getInt("zan"));
                            routesRecord.add(gson.fromJson(jsonObject.getString("record"), Record.class));
                            departRecord.add(gson.fromJson(routesRecord.get(i).getDepartPosition(), ReverseGeoCodeResult.class));
                            arriveRecord.add(gson.fromJson(routesRecord.get(i).getArrivePosition(), ReverseGeoCodeResult.class));
                        }
                        recordListAdapter = new RecordListAdapter(getContext(), routesRecord, zans);
                        listView.setAdapter(recordListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).getCollection(user.getTelephone());

            new RecordNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    routesGoing.addAll((Collection<? extends Record>) msg.obj);
                    for (Record record : routesGoing) {
                        departGoing.add(gson.fromJson(record.getDepartPosition(), ReverseGeoCodeResult.class));
                        arriveGoing.add(gson.fromJson(record.getArrivePosition(), ReverseGeoCodeResult.class));
                    }
                    goingListAdapter = new GoingListAdapter(getContext(), routesGoing);
                }
            }).getGoing(user.getTelephone());
        }
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("fragment", "record");
                    jsonObject.put("action", "brief");
                    if (selected == 1) {
                        jsonObject.put("departPosition",departRecord.get(position));
                        jsonObject.put("arrivePosition",arriveRecord.get(position));
                        jsonObject.put("route", routesRecord.get(position).getRoute());
                    } else {
                        jsonObject.put("departPosition",departGoing.get(position));
                        jsonObject.put("arrivePosition",arriveGoing.get(position));
                        jsonObject.put("route", routesGoing.get(position).getRoute());
                    }
                    mListener.onFragmentInteraction(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setAdapter(boolean isRecord) {
        if (isRecord) {
            selected = 1;
            listView.setAdapter(recordListAdapter);
            listView.setDividerHeight(48);
        } else {
            selected = 2;
            listView.setAdapter(goingListAdapter);
            listView.setDividerHeight(0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(JSONObject jsonObject);
    }
}
