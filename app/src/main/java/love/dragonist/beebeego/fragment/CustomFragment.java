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
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CustomFragment extends Fragment {
    private ListView listResult;
    private ImageView imgBee;
    private ImageView imgLoading;
    private SeekBar seekBarPrice;
    private SeekBar seekBarTime;
    private SeekBar seekBarComfort;
    private TextView textProgressPrice;
    private TextView textProgressTime;
    private TextView textProgressComfort;
    private TextView textZero;
    private TextView textOne;
    private TextView textTwo;

    private int progressPrice = 33;
    private int progressTime = 33;
    private int progressComfort = 34;
    private boolean zeroSelected = false;
    private boolean oneSelected = true;
    private boolean twoSelected = true;
    private List<Route> routes = new ArrayList<>();
    private List<Route> ansRoutes = new ArrayList<>();
    private List<Route> nonstopRoutes = new ArrayList<>();
    private Gson gson;
    private ReverseGeoCodeResult departPosition;
    private ReverseGeoCodeResult arrivePosition;
    private RoutePlan routePlan;
    private ResultListAdapter resultListAdapter;
    private RotateAnimation rotateAnimation;

    private OnFragmentInteractionListener mListener;

    public CustomFragment() {
        gson = new Gson();
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
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        initData();

        imgBee = view.findViewById(R.id.custom_img_load_bee);
        imgLoading = view.findViewById(R.id.custom_img_loading);
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        imgLoading.setAnimation(rotateAnimation);
        textZero = view.findViewById(R.id.custom_text_zero);
        textOne = view.findViewById(R.id.custom_text_one);
        textTwo = view.findViewById(R.id.custom_text_two);
        textProgressPrice = view.findViewById(R.id.custom_progress1);
        textProgressTime = view.findViewById(R.id.custom_progress2);
        textProgressComfort = view.findViewById(R.id.custom_progress3);
        listResult = view.findViewById(R.id.custom_list);
        listResult.setAdapter(resultListAdapter);
        seekBarPrice = view.findViewById(R.id.custom_seekBar1);
        seekBarPrice.setMax(100);
        seekBarPrice.setProgress(33);
        seekBarTime = view.findViewById(R.id.custom_seekBar2);
        seekBarTime.setMax(100);
        seekBarTime.setProgress(33);
        seekBarComfort = view.findViewById(R.id.custom_seekBar3);
        seekBarComfort.setMax(100);
        seekBarComfort.setProgress(34);

        initListener();
        return view;
    }

    private void initData() {
        resultListAdapter = new ResultListAdapter(getContext(), routes);

        routePlan = new RoutePlan(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ansRoutes.clear();
                ansRoutes.addAll((Collection<? extends Route>) msg.obj);
                showRoutes();
                imgBee.setVisibility(View.GONE);
                imgLoading.setVisibility(View.GONE);
                imgLoading.clearAnimation();
            }
        });
        routePlan.post(departPosition.getAddressDetail().city, arrivePosition.getAddressDetail().city, 0.33, 0.33, 0.34, -1);

        new Nonstop(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                nonstopRoutes.addAll((Collection<? extends Route>) msg.obj);
            }
        }).post(departPosition.getAddressDetail().city, arrivePosition.getAddressDetail().city);
    }

    private void initListener() {
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

        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((progress + progressComfort + progressTime) > 100) {
                    seekBar.setProgress(100 - progressComfort - progressTime);
                    progressPrice = 100 - progressComfort - progressTime;
                } else {
                    progressPrice = progress;
                }

                textProgressPrice.setText(progressPrice + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progressPrice == 0 && progressTime == 0 && progressComfort == 0) return;
                normalization(progressPrice, progressTime, progressComfort);
                imgBee.setVisibility(View.VISIBLE);
                imgLoading.setVisibility(View.VISIBLE);
                imgLoading.setAnimation(rotateAnimation);
            }
        });

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((progressPrice + progressComfort + progress) > 100) {
                    seekBar.setProgress(100 - progressComfort - progressPrice);
                    progressTime = 100 - progressComfort - progressPrice;
                } else {
                    progressTime = progress;
                }

                textProgressTime.setText(progressTime + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progressPrice == 0 && progressTime == 0 && progressComfort == 0) return;
                normalization(progressPrice, progressTime, progressComfort);
                imgBee.setVisibility(View.VISIBLE);
                imgLoading.setVisibility(View.VISIBLE);
                imgLoading.setAnimation(rotateAnimation);
            }
        });

        seekBarComfort.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((progressPrice + progress + progressTime) > 100) {
                    seekBar.setProgress(100 - progressPrice - progressTime);
                    progressComfort = 100 - progressPrice - progressTime;
                } else {
                    progressComfort = progress;
                }

                textProgressComfort.setText(progressComfort + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progressPrice == 0 && progressTime == 0 && progressComfort == 0) return;
                normalization(progressPrice, progressTime, progressComfort);
                imgBee.setVisibility(View.VISIBLE);
                imgLoading.setVisibility(View.VISIBLE);
                imgLoading.setAnimation(rotateAnimation);
            }
        });

        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("route", gson.toJson(routes.get(position)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(jsonObject);
            }
        });
    }

    private void normalization(int pPrice, int pTime, int pComfort) {
        int sum = pPrice + pTime + pComfort;
        double p = pPrice * 1.0 / sum;
        double t = pTime * 1.0 / sum;
        double c = pComfort * 1.0 / sum;
        routePlan.post(departPosition.getAddressDetail().city, arrivePosition.getAddressDetail().city, p, t, c, -1);
    }

    private void showRoutes() {
        routes.clear();
        for (Route ansRoute : ansRoutes) {
            if (ansRoute.getThirdWay() == null && ansRoute.getSecondWay() != null && oneSelected) {
                routes.add(ansRoute);
            } else if (ansRoute.getThirdWay() != null && ansRoute.getSecondWay() != null && twoSelected) {
                routes.add(ansRoute);
            }
        }
        if (zeroSelected) {
            routes.addAll(nonstopRoutes);
        }
        int sum = progressPrice + progressTime + progressComfort;
        double p = progressPrice * 1.0 / sum;
        double t = progressTime * 1.0 / sum;
        double c = progressComfort * 1.0 / sum;
        Format.sortRoute(routes, p, t, c);
        resultListAdapter.notifyDataSetChanged();
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
