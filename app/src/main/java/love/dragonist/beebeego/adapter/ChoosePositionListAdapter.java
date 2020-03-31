package love.dragonist.beebeego.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.List;

import love.dragonist.beebeego.R;
import love.dragonist.beebeego.util.Format;

public class ChoosePositionListAdapter extends BaseAdapter {
    private Context context;
    private List<ReverseGeoCodeResult> positions;

    public ChoosePositionListAdapter(Context context, List<ReverseGeoCodeResult> positions) {
        this.context = context;
        this.positions = positions;
    }

    @Override
    public int getCount() {
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getLocation() == null) return i;
        }
        return positions.size();
    }

    @Override
    public Object getItem(int position) {
        return positions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            vh = new VH();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_position, null, true);
            vh.img = convertView.findViewById(R.id.item_choose_position_img);
            vh.accurateLocation = convertView.findViewById(R.id.item_choose_position_accurate);
            vh.vagueLocation = convertView.findViewById(R.id.item_choose_position_vague);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }

        ReverseGeoCodeResult position = positions.get(i);
        vh.accurateLocation.setText(Format.getNameByReverseGeoCodeResult(position));
        vh.vagueLocation.setText(position.getAddressDetail().province + position.getAddressDetail().city + position.getAddressDetail().district);

        return convertView;
    }

    class VH {
        private ImageView img;
        private TextView accurateLocation;
        private TextView vagueLocation;
    }
}
