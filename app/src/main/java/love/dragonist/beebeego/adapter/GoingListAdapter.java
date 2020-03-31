package love.dragonist.beebeego.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.Record;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.net.RecordNet;
import love.dragonist.beebeego.util.Format;

public class GoingListAdapter extends BaseAdapter {
    private Context context;
    private List<Record> records;
    private Gson gson;

    public GoingListAdapter(Context context, List<Record> records) {
        this.context = context;
        this.records = records;
        this.gson = new Gson();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            vh = new VH();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_going, null, true);
            vh.textTime = convertView.findViewById(R.id.item_going_text_time);
            vh.textDayDifferent = convertView.findViewById(R.id.item_going_text_dayDifferent);
            vh.textTitle = convertView.findViewById(R.id.item_going_text_title);
            vh.textMoney = convertView.findViewById(R.id.item_going_text_money);
            vh.textChangeTime = convertView.findViewById(R.id.item_going_text_changeTime);
            vh.textComfort = convertView.findViewById(R.id.item_going_text_comfort);
            vh.textCancel = convertView.findViewById(R.id.item_going_text_cancel);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }

        final Record record = records.get(position);
        ReverseGeoCodeResult depart = gson.fromJson(record.getDepartPosition(), ReverseGeoCodeResult.class);
        ReverseGeoCodeResult arrive = gson.fromJson(record.getArrivePosition(), ReverseGeoCodeResult.class);
        vh.textMoney.setText("￥" + String.format("%.1f", record.getRoute().getPrice()));
        vh.textComfort.setText("舒适度为" + String.format("%.1f", record.getRoute().getComfort()));
        vh.textTitle.setText(Format.getNameByReverseGeoCodeResult(depart) + "-" + Format.getNameByReverseGeoCodeResult(arrive));
        vh.textDayDifferent.setText(getDayDifferent(record.getRoute().getFirstWay().getStartTime().toString(), record.getRoute().getTime() / 1000));

        String time = new SimpleDateFormat("MM月dd日").format(record.getDate()) + " "
                + record.getRoute().getFirstWay().getStartTime().toString().substring(0, 5) + "-";
        if (record.getRoute().getSecondWay() == null) {
            vh.textChangeTime.setText("城际间转乘0次");
            vh.textTime.setText(time + record.getRoute().getFirstWay().getArriveTime().toString().substring(0, 5));
        } else if (record.getRoute().getThirdWay() == null) {
            vh.textChangeTime.setText("城际间转乘1次");
            vh.textTime.setText(time + record.getRoute().getSecondWay().getArriveTime().toString().substring(0, 5));
        } else {
            vh.textChangeTime.setText("城际间转乘2次");
            vh.textTime.setText(time + record.getRoute().getThirdWay().getArriveTime().toString().substring(0, 5));
        }

        vh.textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
                TextView cancel = view.findViewById(R.id.dialog_confirm_text_cancel);
                cancel.setText("取消操作");
                TextView confirm = view.findViewById(R.id.dialog_confirm_text_confirm);
                confirm.setText("删除待出行");
                ((ImageView) view.findViewById(R.id.dialog_confirm_img)).setImageResource(R.drawable.ic_bee_unchecked);
                ((TextView) view.findViewById(R.id.dialog_confirm_text_title)).setText("删除提示");
                ((TextView) view.findViewById(R.id.dialog_confirm_text_hint)).setText("您是否删除该待出行路径？");
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new RecordNet(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                records.remove(position);
                                notifyDataSetChanged();
                            }
                        }).deleteGoing(record);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });

        return convertView;
    }

    private class VH {
        private TextView textTime;
        private TextView textDayDifferent;
        private TextView textTitle;
        private TextView textMoney;
        private TextView textChangeTime;
        private TextView textComfort;
        private TextView textCancel;
    }

    private String getDayDifferent(String departTime, long duration) {
        String s[] = departTime.split(":");
        long t = Long.parseLong(s[0]) * 3600;
        t = t + Long.parseLong(s[1]) * 60;
        t += duration;
        return "+" + (t / 24 / 3600);
    }
}
