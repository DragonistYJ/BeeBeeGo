package love.dragonist.beebeego.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.Route;

public class ResultListAdapter extends BaseAdapter {
    private Context context;
    private List<Route> routes;

    public ResultListAdapter(Context context, List<Route> routes) {
        this.context = context;
        this.routes = routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            vh = new VH();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_result, null, true);
            vh.textDepartTime = convertView.findViewById(R.id.item_result_text_departTime);
            vh.textArriveTime = convertView.findViewById(R.id.item_result_text_arriveTime);
            vh.textTimeCost = convertView.findViewById(R.id.item_result_text_timeCost);
            vh.textChangeTimes = convertView.findViewById(R.id.item_result_text_change_times);
            vh.textMoney = convertView.findViewById(R.id.item_result_text_money);
            vh.textT1 = convertView.findViewById(R.id.item_result_text_1);
            vh.textT2 = convertView.findViewById(R.id.item_result_text_2);
            vh.textT3 = convertView.findViewById(R.id.item_result_text_3);
            vh.textDayDifferent = convertView.findViewById(R.id.item_result_text_dayDifferent);
            vh.textComfort = convertView.findViewById(R.id.item_result_text_comfort);
            vh.imgI1 = convertView.findViewById(R.id.item_result_img_1);
            vh.imgI2 = convertView.findViewById(R.id.item_result_img_2);
            vh.imgI3 = convertView.findViewById(R.id.item_result_img_3);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }

        Route route = routes.get(position);
        vh.textDepartTime.setText(route.getFirstWay().getStartTime().toString().substring(0, 5));
        vh.textTimeCost.setText(getDurationFormat(route.getTime()));
        vh.textDayDifferent.setText(getDayDifferent(route.getFirstWay().getStartTime().toString(), route.getTime() / 1000));
        vh.textMoney.setText(String.format("%.1f", route.getPrice()));
        vh.textComfort.setText("舒适度" + String.format("%.1f", route.getComfort()));

        String type = getType(route.getFirstWay().getNo(), route.getFirstWay().getFromStation().getName());
        if (type.equals("飞机")) {
            vh.imgI1.setImageResource(R.drawable.ic_airplane);
        } else {
            vh.imgI1.setImageResource(R.drawable.ic_train);
        }
        vh.textT1.setText("第一站:" + type);
        vh.textChangeTimes.setText("城际间转乘0次");
        vh.textArriveTime.setText(route.getFirstWay().getArriveTime().toString().substring(0, 5));
        vh.textT2.setVisibility(View.INVISIBLE);
        vh.textT3.setVisibility(View.INVISIBLE);
        vh.imgI2.setVisibility(View.INVISIBLE);
        vh.imgI3.setVisibility(View.INVISIBLE);

        if (route.getSecondWay() != null) {
            vh.textT2.setVisibility(View.VISIBLE);
            vh.imgI2.setVisibility(View.VISIBLE);
            type = getType(route.getSecondWay().getNo(), route.getSecondWay().getFromStation().getName());
            if (type.equals("飞机")) {
                vh.imgI2.setImageResource(R.drawable.ic_airplane);
            } else {
                vh.imgI2.setImageResource(R.drawable.ic_train);
            }
            vh.textT2.setText("第二站:" + type);
            vh.textChangeTimes.setText("城际间转乘1次");
            vh.textArriveTime.setText(route.getSecondWay().getArriveTime().toString().substring(0, 5));
        }

        if (route.getThirdWay() != null) {
            vh.textT3.setVisibility(View.VISIBLE);
            vh.imgI3.setVisibility(View.VISIBLE);
            type = getType(route.getThirdWay().getNo(), route.getSecondWay().getFromStation().getName());
            if (type.equals("飞机")) {
                vh.imgI3.setImageResource(R.drawable.ic_airplane);
            } else {
                vh.imgI3.setImageResource(R.drawable.ic_train);
            }
            vh.textT3.setText("第三站:" + type);
            vh.textChangeTimes.setText("城际间转乘2次");
            vh.textArriveTime.setText(route.getThirdWay().getArriveTime().toString().substring(0, 5));
        }

        return convertView;
    }

    private String getDayDifferent(String departTime, long duration) {
        String s[] = departTime.split(":");
        long t = Long.parseLong(s[0]) * 3600;
        t = t + Long.parseLong(s[1]) * 60;
        t += duration;
        return "+" + (t / 24 / 3600);
    }

    private String getDurationFormat(long time) {
        long hour = time / 3600000;
        long min = (time % 3600000) / 1000 / 60;
        String t = "";
        t = t + hour + "时";
        t = t + min + "分";
        return t;
    }

    private String getType(String no, String station) {
        if (station.endsWith("机场")) return "飞机";
        else if (no.startsWith("K")) return "快车";
        else if (no.startsWith("G")) return "高铁";
        else return "动车";
    }

    class VH {
        private TextView textDepartTime;
        private TextView textArriveTime;
        private TextView textTimeCost;
        private TextView textChangeTimes;
        private TextView textMoney;
        private TextView textT1;
        private TextView textT2;
        private TextView textT3;
        private TextView textDayDifferent;
        private TextView textComfort;
        private ImageView imgI1;
        private ImageView imgI2;
        private ImageView imgI3;
    }
}
