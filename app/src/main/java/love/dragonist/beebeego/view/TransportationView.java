package love.dragonist.beebeego.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.Step;

public class TransportationView extends LinearLayout {
    private TextView textNo;
    private TextView textInfo;

    public TransportationView(Context context) {
        this(context, null);
    }

    public TransportationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_transportation, this, true);

        textNo = findViewById(R.id.item_transportation_text_no);
        textInfo = findViewById(R.id.item_transportation_text_info);
    }

    public void setView(Step step) {
        textNo.setText(step.getVehicle_info().getDetail().getName());
        if (step.getVehicle_info().getDetail().getType() == 1) {
            textNo.setBackgroundResource(R.drawable.bg_green_20);
        } else if (step.getVehicle_info().getDetail().getType() == 0) {
            textNo.setBackgroundResource(R.drawable.bg_orange_20);
        } else {
            textNo.setBackgroundResource(R.drawable.bg_papper_20);
        }
        textInfo.setText(step.getVehicle_info().getDetail().getStop_num() + "站");
    }
}
