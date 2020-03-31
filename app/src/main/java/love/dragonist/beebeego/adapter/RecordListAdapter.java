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

import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.bean.Record;
import love.dragonist.beebeego.bean.Route;
import love.dragonist.beebeego.net.RecordNet;
import love.dragonist.beebeego.util.Format;

public class RecordListAdapter extends BaseAdapter {
    private Context context;
    private List<Record> records;
    private List<Integer> zans;
    private Gson gson;

    public RecordListAdapter(Context context, List<Record> records, List<Integer> zans) {
        this.context = context;
        this.records = records;
        this.zans = zans;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record, null, true);
            vh.textTitle = convertView.findViewById(R.id.item_record_text_position);
            vh.textMoney = convertView.findViewById(R.id.item_record_text_money);
            vh.textChangeTime = convertView.findViewById(R.id.item_record_text_changeTime);
            vh.textComfort = convertView.findViewById(R.id.item_record_text_comfort);
            vh.textZan = convertView.findViewById(R.id.item_record_text_zan);
            vh.textCancel = convertView.findViewById(R.id.item_record_text_cancel);
            vh.imgBg = convertView.findViewById(R.id.item_record_img);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }

        final Record record = records.get(position);
        ReverseGeoCodeResult depart = gson.fromJson(record.getDepartPosition(), ReverseGeoCodeResult.class);
        ReverseGeoCodeResult arrive = gson.fromJson(record.getArrivePosition(), ReverseGeoCodeResult.class);
        Integer zan = zans.get(position);
        if (position % 2 == 0) {
            vh.imgBg.setImageResource(R.mipmap.ic_bg_bus);
        } else {
            vh.imgBg.setImageResource(R.mipmap.ic_bg_road);
        }
        vh.textTitle.setText(Format.getNameByReverseGeoCodeResult(depart) + "\n" + Format.getNameByReverseGeoCodeResult(arrive));
        vh.textMoney.setText("￥" + record.getRoute().getPrice());
        vh.textZan.setText(zan + "");
        vh.textComfort.setText("舒适度为" + String.format("%.1f", record.getRoute().getComfort()) + "分");
        if (record.getRoute().getSecondWay() == null) {
            vh.textChangeTime.setText("城际间转乘0次");
        } else if (record.getRoute().getThirdWay() == null) {
            vh.textChangeTime.setText("城际间转乘1次");
        } else {
            vh.textChangeTime.setText("城际间转乘2次");
        }

        vh.textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
                TextView cancel = view.findViewById(R.id.dialog_confirm_text_cancel);
                cancel.setText("取消操作");
                TextView confirm = view.findViewById(R.id.dialog_confirm_text_confirm);
                confirm.setText("删除收藏");
                ((ImageView) view.findViewById(R.id.dialog_confirm_img)).setImageResource(R.drawable.ic_bee_unchecked);
                ((TextView) view.findViewById(R.id.dialog_confirm_text_title)).setText("删除提示");
                ((TextView) view.findViewById(R.id.dialog_confirm_text_hint)).setText("您是否删除该收藏路径？");
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
                        }).deleteCollection(record);
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
        private TextView textTitle;
        private TextView textMoney;
        private TextView textChangeTime;
        private TextView textComfort;
        private TextView textZan;
        private TextView textCancel;
        private ImageView imgBg;
    }
}
