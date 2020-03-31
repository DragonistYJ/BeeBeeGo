package love.dragonist.beebeego.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.Way;

public class InterCityView extends ConstraintLayout {
    private TextView textTitle;
    private TextView textNo;
    private TextView textFrom;
    private TextView textTo;
    private TextView textDuration;
    private TextView textPrice;
    private ImageView imgIcon;
    private Button btnChange;

    public InterCityView(Context context) {
        this(context, null);
    }

    public InterCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_intercity, this, true);
        textTitle = findViewById(R.id.intercity_title);
        textNo = findViewById(R.id.intercity_no);
        textFrom = findViewById(R.id.intercity_from);
        textTo = findViewById(R.id.intercity_to);
        textDuration = findViewById(R.id.intercity_duration);
        textPrice = findViewById(R.id.intercity_price);
        imgIcon = findViewById(R.id.intercity_icon);
        btnChange = findViewById(R.id.intercity_btn_change);
    }

    public void showView(Way way) {
        textTitle.setText(way.getFromStation().getName() + "-" + way.getToStation().getName());
        textNo.setText(way.getNo());
        textFrom.setText(way.getStartTime().toString().substring(0, 5) + " " + way.getFromStation().getName());
        textTo.setText(way.getArriveTime().toString().substring(0, 5) + " " + way.getToStation().getName());
        textPrice.setText("￥" + String.format("%.1f", way.getPrice()) + "起");
        textDuration.setText(getDurationFormat(way.getArriveTime().getTime() - way.getStartTime().getTime() + way.getDayDifferent() * 24 * 3600000));
        if (way.getType().equals("F")) imgIcon.setImageResource(R.drawable.ic_airplane);
        else imgIcon.setImageResource(R.drawable.ic_train);
    }

    public Button getBtnChange() {
        return btnChange;
    }

    private String getDurationFormat(long time) {
        long hour = time / 3600000;
        long min = (time % 3600000) / 1000 / 60;
        String t = "";
        t = t + hour + "时";
        t = t + min + "分";
        return t;
    }
}
