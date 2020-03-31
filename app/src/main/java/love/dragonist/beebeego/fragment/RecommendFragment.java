package love.dragonist.beebeego.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.adapter.ResultListAdapter;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.net.Nonstop;
import love.dragonist.beebeego.net.RoutePlan;
import love.dragonist.beebeego.util.Format;

public class RecommendFragment extends Fragment {
    private ListView listResult;
    private ImageView imgBee;
    private ImageView imgLoading;
    private TextView textComprehensive;
    private TextView textTime;
    private TextView textPrice;
    private TextView textComfort;
    private TextView textZero;
    private TextView textOne;
    private TextView textTwo;

    private List<Route> comprehensiveRoutes = new ArrayList<>();
    private List<Route> priceRoutes = new ArrayList<>();
    private List<Route> timeRoutes = new ArrayList<>();
    private List<Route> comfortRoutes = new ArrayList<>();
    private List<Route> showedRoutes = new ArrayList<>();
    private RoutePlan routePlan;
    private Gson gson = new Gson();
    private ResultListAdapter resultListAdapter;
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private RotateAnimation rotateAnimation;
    private int selected = 1;
    private boolean zeroSelected = false;
    private boolean oneSelected = true;
    private boolean twoSelected = true;

    private OnFragmentInteractionListener mListener;

    public RecommendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        initData();

        listResult = view.findViewById(R.id.recommend_list);
        listResult.setAdapter(resultListAdapter);
        imgBee = view.findViewById(R.id.recommend_img_load_bee);
        imgLoading = view.findViewById(R.id.recommend_img_loading);
        textComprehensive = view.findViewById(R.id.recommend_text_comprehensive);
        textPrice = view.findViewById(R.id.recommend_text_price);
        textTime = view.findViewById(R.id.recommend_text_time);
        textComfort = view.findViewById(R.id.recommend_text_comfort);
        textZero = view.findViewById(R.id.recommend_text_zero);
        textOne = view.findViewById(R.id.recommend_text_one);
        textTwo = view.findViewById(R.id.recommend_text_two);
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        imgLoading.setAnimation(rotateAnimation);

        initListener();

        return view;
    }

    private void initData() {
        resultListAdapter = new ResultListAdapter(getContext(), showedRoutes);

        routePlan = new RoutePlan(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        comprehensiveRoutes.addAll((Collection<? extends Route>) msg.obj);
                        Format.sortRoute(comprehensiveRoutes, 0.33, 0.33, 0.34);
                        showRoutes();
                        break;
                    case 2:
                        priceRoutes.addAll((Collection<? extends Route>) msg.obj);
                        Format.sortRoute(priceRoutes, 1, 0, 0);
                        break;
                    case 3:
                        timeRoutes.addAll((Collection<? extends Route>) msg.obj);
                        Format.sortRoute(timeRoutes, 0, 1, 0);
                        break;
                    case 4:
                        comfortRoutes.addAll((Collection<? extends Route>) msg.obj);
                        Format.sortRoute(comfortRoutes, 0, 0, 1);
                        break;
                }
                imgBee.setVisibility(View.GONE);
                imgLoading.setVisibility(View.GONE);
                imgLoading.clearAnimation();
            }
        });

        routePlan.post(departPosition.getAddressDetail().city,
                arrivePosition.getAddressDetail().city,
                0.3, 0.3, 0.4, 1);
        routePlan.post(departPosition.getAddressDetail().city,
                arrivePosition.getAddressDetail().city,
                1, 0, 0, 2);
        routePlan.post(departPosition.getAddressDetail().city,
                arrivePosition.getAddressDetail().city,
                0, 1, 0, 3);
        routePlan.post(departPosition.getAddressDetail().city,
                arrivePosition.getAddressDetail().city,
                0, 0, 1, 4);

        new Nonstop(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                comprehensiveRoutes.addAll((Collection<? extends Route>) msg.obj);
                Format.sortRoute(comprehensiveRoutes, 0.33, 0.33, 0.34);
                priceRoutes.addAll((Collection<? extends Route>) msg.obj);
                Format.sortRoute(priceRoutes, 1, 0, 0);
                timeRoutes.addAll((Collection<? extends Route>) msg.obj);
                Format.sortRoute(timeRoutes, 0, 1, 0);
                comfortRoutes.addAll((Collection<? extends Route>) msg.obj);
                Format.sortRoute(comfortRoutes, 0, 0, 1);
                showRoutes();
            }
        }).post(departPosition.getAddressDetail().city, arrivePosition.getAddressDetail().city);
    }

    private void initListener() {
        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("route", gson.toJson(showedRoutes.get(position)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(jsonObject);
            }
        });

        textZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zeroSelected) {
                    textZero.setTextColor(0xff000000);
                    textZero.setBackground(null);
                } else {
                    textZero.setTextColor(0xffffffff);
                    textZero.setBackgroundResource(R.drawable.bg_yellow_24);
                }
                zeroSelected = !zeroSelected;
                showRoutes();
            }
        });

        textOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oneSelected) {
                    textOne.setTextColor(0xff000000);
                    textOne.setBackground(null);
                } else {
                    textOne.setTextColor(0xffffffff);
                    textOne.setBackgroundResource(R.drawable.bg_yellow_24);
                }
                oneSelected = !oneSelected;
                showRoutes();
            }
        });

        textTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twoSelected) {
                    textTwo.setTextColor(0xff000000);
                    textTwo.setBackground(null);
                } else {
                    textTwo.setTextColor(0xffffffff);
                    textTwo.setBackgroundResource(R.drawable.bg_yellow_24);
                }
                twoSelected = !twoSelected;
                showRoutes();
            }
        });

        textComprehensive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 1;
                setAllWhite();
                textComprehensive.setTextColor(0xff1FC756);
                showRoutes();
            }
        });
        textPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 2;
                setAllWhite();
                textPrice.setTextColor(0xff1FC756);
                showRoutes();
            }
        });
        textTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 3;
                setAllWhite();
                textTime.setTextColor(0xff1FC756);
                showRoutes();
            }
        });
        textComfort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 4;
                setAllWhite();
                textComfort.setTextColor(0xff1FC756);
                showRoutes();
            }
        });
    }

    private void showRoutes() {
        showedRoutes.clear();
        switch (selected) {
            case 1:
                for (Route comprehensiveRoute : comprehensiveRoutes) {
                    if (comprehensiveRoute.getThirdWay() == null && comprehensiveRoute.getSecondWay() == null && zeroSelected) {
                        showedRoutes.add(comprehensiveRoute);
                    } else if (comprehensiveRoute.getThirdWay() == null && comprehensiveRoute.getSecondWay() != null && oneSelected) {
                        showedRoutes.add(comprehensiveRoute);
                    } else if (comprehensiveRoute.getThirdWay() != null && comprehensiveRoute.getSecondWay() != null && twoSelected) {
                        showedRoutes.add(comprehensiveRoute);
                    }
                }
                Format.sortRoute(showedRoutes, 0.33, 0.33, 0.34);
                break;
            case 2:
                for (Route priceRoute : priceRoutes) {
                    if (priceRoute.getThirdWay() == null && priceRoute.getSecondWay() == null && zeroSelected) {
                        showedRoutes.add(priceRoute);
                    } else if (priceRoute.getThirdWay() == null && priceRoute.getSecondWay() != null && oneSelected) {
                        showedRoutes.add(priceRoute);
                    } else if (priceRoute.getThirdWay() != null && priceRoute.getSecondWay() != null && twoSelected) {
                        showedRoutes.add(priceRoute);
                    }
                }
                Format.sortRoute(showedRoutes, 1, 0, 0);
                break;
            case 3:
                for (Route timeRoute : timeRoutes) {
                    if (timeRoute.getThirdWay() == null && timeRoute.getSecondWay() == null && zeroSelected) {
                        showedRoutes.add(timeRoute);
                    } else if (timeRoute.getThirdWay() == null && timeRoute.getSecondWay() != null && oneSelected) {
                        showedRoutes.add(timeRoute);
                    } else if (timeRoute.getThirdWay() != null && timeRoute.getSecondWay() != null && twoSelected) {
                        showedRoutes.add(timeRoute);
                    }
                }
                Format.sortRoute(showedRoutes, 0, 1, 0);
                break;
            case 4:
                for (Route comfortRoute : comfortRoutes) {
                    if (comfortRoute.getThirdWay() == null && comfortRoute.getSecondWay() == null && zeroSelected) {
                        showedRoutes.add(comfortRoute);
                    } else if (comfortRoute.getThirdWay() == null && comfortRoute.getSecondWay() != null && oneSelected) {
                        showedRoutes.add(comfortRoute);
                    } else if (comfortRoute.getThirdWay() != null && comfortRoute.getSecondWay() != null && twoSelected) {
                        showedRoutes.add(comfortRoute);
                    }
                }
                Format.sortRoute(showedRoutes, 0, 0, 1);
                break;
        }
        resultListAdapter.notifyDataSetChanged();
    }

    private void setAllWhite() {
        textComprehensive.setTextColor(0xFFFFFFFF);
        textPrice.setTextColor(0xffffffff);
        textTime.setTextColor(0xffffffff);
        textComfort.setTextColor(0xffffffff);
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
