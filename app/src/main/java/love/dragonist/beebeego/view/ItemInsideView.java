package love.dragonist.beebeego.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.Step;

public class ItemInsideView extends ConstraintLayout {
    private ImageView imgIcon;
    private ImageView imgStart;
    private ImageView imgEnd;
    private TextView textNo;
    private TextView textStart;
    private TextView textEnd;
    private TextView textDuration;
    private TextView textStops;
    private TextView textGuide;
    private ConstraintLayout layoutUp;
    private ConstraintLayout layoutDown;

    public ItemInsideView(Context context) {
        this(context, null);
    }

    public ItemInsideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_insidecity_list, this, true);

        imgIcon = findViewById(R.id.insidecity_list_icon);
        imgStart = findViewById(R.id.insidecity_list_start_ic);
        imgEnd = findViewById(R.id.insidecity_list_end_ic);
        textNo = findViewById(R.id.insidecity_list_title);
        textStart = findViewById(R.id.insidecity_list_start);
        textEnd = findViewById(R.id.insidecity_list_end);
        textDuration = findViewById(R.id.insidecity_list_time);
        textStops = findViewById(R.id.insidecity_list_stops);
        textGuide = findViewById(R.id.insidecity_list_guide);
        layoutUp = findViewById(R.id.insidecity_list_cons1);
        layoutDown = findViewById(R.id.insidecity_list_cons2);
    }

    public void setView(Step step) {
        if (step.getVehicle_info().getType() == 5) {
            imgStart.setVisibility(View.GONE);
            imgEnd.setVisibility(View.GONE);
            textStart.setVisibility(View.GONE);
            textEnd.setVisibility(View.GONE);
            textDuration.setVisibility(View.GONE);
            layoutDown.setVisibility(View.GONE);
            layoutUp.setBackgroundResource(R.drawable.bg_green_20_t);
            imgIcon.setImageResource(R.drawable.ic_walking);
            textNo.setText("步行  约" + (step.getDuration() / 60 + 1) + "分");
        } else {
            textGuide.setVisibility(GONE);
            imgStart.setVisibility(View.VISIBLE);
            imgEnd.setVisibility(View.VISIBLE);
            textStart.setText(step.getVehicle_info().getDetail().getOn_station());
            textEnd.setText(step.getVehicle_info().getDetail().getOff_station());
            textNo.setText(step.getVehicle_info().getDetail().getName());
            textDuration.setText("约" + (step.getDuration() / 60 + 1) + "分钟");
            textStops.setText("共" + step.getVehicle_info().getDetail().getStop_num() + "站");
            switch (step.getVehicle_info().getDetail().getType()) {
                case 1:
                    imgIcon.setImageResource(R.drawable.ic_subway);
                    break;
                case 0:
                    imgIcon.setImageResource(R.drawable.ic_bus);
                    break;
            }
        }
    }

    public TextView getTextGuide() {
        return textGuide;
    }
}
