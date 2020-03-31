package love.dragonist.beebeego.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import love.dragonist.beebeego.R;

public class InsideCityView extends ConstraintLayout {
    private TextView textTitle;     //标题 四川大学江安校区——双流机场
    private TextView textTime;    //用时多少时间
    private TextView textDistance; //总共多长距离
    private ImageView imgArrow;     //上下箭头
    private Button btnChange;       //切换路径的按钮
    private LinearLayout linearStep;    //一条路径里面的所有步骤

    public InsideCityView(Context context) {
        this(context, null);
    }

    public InsideCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_insidecity, this, true);
        textTitle = findViewById(R.id.insidecity_title);
        textTime = findViewById(R.id.insidecity_time);
        textDistance = findViewById(R.id.insidecity_distance);
        imgArrow = findViewById(R.id.insidecity_arrow);
        btnChange = findViewById(R.id.insidecity_btn_change);
        btnChange.setVisibility(GONE);
        linearStep = findViewById(R.id.insidecity_linear);
        linearStep.setVisibility(GONE);
        textTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearStep.getVisibility() == GONE) {
                    linearStep.setVisibility(VISIBLE);
                    btnChange.setVisibility(VISIBLE);
                    imgArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    linearStep.setVisibility(GONE);
                    btnChange.setVisibility(GONE);
                    imgArrow.setImageResource(R.drawable.ic_arrow_up);
                }
            }
        });
    }

    public void setTextTitleText(String title) {
        textTitle.setText(title);
    }

    public void setTextTimeText(String time) {
        textTime.setText(time);
    }

    public void setTextDistance(String distance) {
        textDistance.setText(distance);
    }

    public void addItemInsideView(ItemInsideView view) {
        linearStep.addView(view);
    }

    public LinearLayout getLinearStep() {
        return linearStep;
    }

    public Button getBtnChange() {
        return btnChange;
    }

    public TextView getTextTitle() {
        return textTitle;
    }

    public ImageView getImgArrow() {
        return imgArrow;
    }
}
