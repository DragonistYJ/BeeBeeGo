package love.dragonist.beebeego.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.adapter.NonstopListAdapter;
import love.dragonist.beebeego.adapter.ResultListAdapter;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.net.Nonstop;

public class NonstopFragment extends Fragment {
    private ListView listResult;
    private ImageView imgBee;
    private ImageView imgLoading;
    private TextView textDepartTime;
    private TextView textPrice;
    private TextView textTime;

    private Gson gson = new Gson();
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private RotateAnimation rotateAnimation;
    private List<Route> routesTrain = new ArrayList<>();
    private List<Route> routesFlight = new ArrayList<>();
    private NonstopListAdapter nonstopListAdapter;
    private Boolean selectTrain = false;

    private OnFragmentInteractionListener mListener;

    public NonstopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            departPosition = gson.fromJson(getArguments().getString("departPosition"), ReverseGeoCodeResult.class);
            arrivePosition = gson.fromJson(getArguments().getString("arrivePosition"), ReverseGeoCodeResult.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nonstop, container, false);
        initData();

        imgBee = view.findViewById(R.id.nonstop_load_bee);
        imgLoading = view.findViewById(R.id.nonstop_load_loading);
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        imgLoading.setAnimation(rotateAnimation);
        textDepartTime = view.findViewById(R.id.nonstop_depart_time);
        textPrice = view.findViewById(R.id.nonstop_money);
        textTime = view.findViewById(R.id.nonstop_time_cost);
        listResult = view.findViewById(R.id.nonstop_list);
        listResult.setAdapter(nonstopListAdapter);

        initListener();
        return view;
    }

    private void initData() {
        nonstopListAdapter = new NonstopListAdapter(getContext(), routesFlight);

        new Nonstop(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Route> routes = (List<Route>) msg.obj;
                for (Route route : routes) {
                    if (route.getFirstWay().getType().equals("F")) {
                        routesFlight.add(route);
                    } else {
                        routesTrain.add(route);
                    }
                }
                sortByDepartTime();
                nonstopListAdapter.notifyDataSetChanged();
                imgBee.setVisibility(View.INVISIBLE);
                imgLoading.setVisibility(View.INVISIBLE);
                imgLoading.clearAnimation();
            }
        }).post(departPosition.getAddressDetail().city, arrivePosition.getAddressDetail().city);
    }

    private void initListener() {
        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject = new JSONObject();
                try {
                    if (selectTrain) {
                        jsonObject.put("route", gson.toJson(routesTrain.get(position)));
                    } else {
                        jsonObject.put("route", gson.toJson(routesFlight.get(position)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(jsonObject);
            }
        });

        textDepartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByDepartTime();
                nonstopListAdapter.notifyDataSetChanged();
                setAllWhite();
                textDepartTime.setTextColor(0xff1FC756);
            }
        });

        textPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPrice();
                nonstopListAdapter.notifyDataSetChanged();
                setAllWhite();
                textPrice.setTextColor(0xff1FC756);
            }
        });

        textTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByTime();
                nonstopListAdapter.notifyDataSetChanged();
                setAllWhite();
                textTime.setTextColor(0xff1FC756);
            }
        });
    }

    public void tranOrFlight(boolean selectTrain) {
        this.selectTrain = selectTrain;
        if (selectTrain) {
            nonstopListAdapter.setRoutes(routesTrain);
        } else {
            nonstopListAdapter.setRoutes(routesFlight);
        }
        nonstopListAdapter.notifyDataSetChanged();
    }

    private void sortByDepartTime() {
        Collections.sort(routesTrain, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return Long.compare(o1.getFirstWay().getStartTime().getTime(), o2.getFirstWay().getStartTime().getTime());
            }
        });

        Collections.sort(routesFlight, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return Long.compare(o1.getFirstWay().getStartTime().getTime(), o2.getFirstWay().getStartTime().getTime());
            }
        });
    }

    private void sortByPrice() {
        Collections.sort(routesTrain, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });

        Collections.sort(routesFlight, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });
    }

    private void sortByTime() {
        Collections.sort(routesTrain, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return Long.compare(o1.getTime(), o2.getTime());
            }
        });

        Collections.sort(routesFlight, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return Long.compare(o1.getTime(), o2.getTime());
            }
        });
    }

    private void setAllWhite() {
        textTime.setTextColor(0xffffffff);
        textPrice.setTextColor(0xffffffff);
        textDepartTime.setTextColor(0xffffffff);
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
