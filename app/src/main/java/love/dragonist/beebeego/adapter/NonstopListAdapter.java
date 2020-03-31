package love.dragonist.beebeego.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.Route;

public class NonstopListAdapter extends BaseAdapter {
    private Context context;
    private List<Route> routes;

    public NonstopListAdapter(Context context, List<Route> routes) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nonstop_route, null, true);
            vh.textDepartTime = convertView.findViewById(R.id.item_nonstop_depart_time);
            vh.textDepartLocation = convertView.findViewById(R.id.item_nonstop_depart_location);
            vh.textArriveTime = convertView.findViewById(R.id.item_nonstop_arrive_time);
            vh.textArriveLocation = convertView.findViewById(R.id.item_nonstop_arrive_location);
            vh.textSecondSeat = convertView.findViewById(R.id.item_nonstop_second_class);
            vh.textFirstSeat = convertView.findViewById(R.id.item_nonstop_first_class);
            vh.textBusinessSeat = convertView.findViewById(R.id.item_nonstop_route_business_class);
            vh.textMoney = convertView.findViewById(R.id.item_nonstop_money);
            vh.textTrainNumber = convertView.findViewById(R.id.item_nonstop_train_number);
            vh.textTimeCost = convertView.findViewById(R.id.item_nonstop_time_cost);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }

        Route route = routes.get(position);
        vh.textDepartTime.setText(route.getFirstWay().getStartTime().toString().substring(0, 5));
        vh.textArriveTime.setText(route.getFirstWay().getArriveTime().toString().substring(0, 5));
        vh.textDepartLocation.setText(getNameFormat(route.getFirstWay().getFromStation().getName()));
        vh.textArriveLocation.setText(getNameFormat(route.getFirstWay().getToStation().getName()));
        vh.textTrainNumber.setText(route.getFirstWay().getNo());
        vh.textTimeCost.setText(getDurationFormat(route.getTime()));

        vh.textFirstSeat.setVisibility(View.VISIBLE);
        vh.textSecondSeat.setVisibility(View.VISIBLE);
        vh.textBusinessSeat.setVisibility(View.VISIBLE);
        if (route.getFirstWay().getType().equals("F")) {
            vh.textSecondSeat.setText("经济舱 " + String.format("%.1f", route.getPrice()));
            vh.textFirstSeat.setVisibility(View.GONE);
            vh.textBusinessSeat.setVisibility(View.GONE);
        } else if (route.getFirstWay().getType().equals("K")) {
            vh.textSecondSeat.setText("硬座 " + String.format("%.1f", route.getFirstWay().getYzPrice()));
            vh.textFirstSeat.setText("硬卧 " + String.format("%.1f", route.getFirstWay().getYwPrice()));
            vh.textBusinessSeat.setText("软卧 " + String.format("%.1f", route.getFirstWay().getRwPrice()));
            vh.textMoney.setText(String.format("%.1f", route.getFirstWay().getYzPrice()));
        } else {
            vh.textSecondSeat.setText("二等座 " + String.format("%.1f", route.getFirstWay().getEdPrice()));
            vh.textFirstSeat.setText("一等座 " + String.format("%.1f", route.getFirstWay().getYdPrice()));
            vh.textMoney.setText(String.format("%.1f", route.getFirstWay().getEdPrice()));
            vh.textBusinessSeat.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class VH {
        private TextView textDepartTime;
        private TextView textDepartLocation;
        private TextView textArriveTime;
        private TextView textArriveLocation;
        private TextView textTrainNumber;
        private TextView textTimeCost;
        private TextView textSecondSeat;
        private TextView textFirstSeat;
        private TextView textBusinessSeat;
        private TextView textMoney;
    }

    private String getDurationFormat(long time) {
        long hour = time / 3600000;
        long min = (time % 3600000) / 1000 / 60;
        String t = "";
        t = t + hour + "时";
        t = t + min + "分";
        return t;
    }

    private String getNameFormat(String name) {
        if (name.endsWith("国际机场")) return name.substring(0, name.length() - 4);
        else if (name.endsWith("机场")) return name.substring(0, name.length() - 2);
        else return name;
    }
}
