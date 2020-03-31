package love.dragonist.beebeego.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.InsideRoute;
import love.dragonist.beebeego.bean.Step;

public class ChooseInsideView extends LinearLayout {
    private TextView textTime;
    private TextView textDistance;
    private TextView textInfo;
    private LinearLayout linearTransportation;

    public ChooseInsideView(Context context) {
        this(context, null);
    }

    public ChooseInsideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_choose_inside, this, true);
        textTime = findViewById(R.id.item_choose_inside_time);
        textDistance = findViewById(R.id.item_choose_inside_distance);
        textInfo = findViewById(R.id.item_choose_inside_info);
        linearTransportation = findViewById(R.id.item_choose_inside_linear);
    }

    public void setView(InsideRoute insideRoute, Context context) {
        textTime.setText((insideRoute.getDuration() / 3600) + "小时" + ((insideRoute.getDuration() % 3600) / 60) + "分");
        textDistance.setText(String.format("%.1f", insideRoute.getDistance() * 1.0 / 1000) + "千米");
        int stations = 0;
        String info = null;
        for (Step step : insideRoute.getSteps()) {
            if (step.getVehicle_info().getType() != 3) continue;
            TransportationView transportationView = new TransportationView(context);
            transportationView.setView(step);
            linearTransportation.addView(transportationView);
            stations += step.getVehicle_info().getDetail().getStop_num();

            if (info == null) {
                info = " · " + step.getVehicle_info().getDetail().getOff_station();
                if (info.contains("(")) {
                    info = info.substring(0, info.indexOf("(")) + "站";
                }
            }
        }

        if (insideRoute.getPrice() == -1) {
            info = stations + "站" + info + "上车";
        } else {
            info = stations + "站 · " + insideRoute.getPrice() + "元" + info + "上车";
        }

        textInfo.setText(info);
    }
}
