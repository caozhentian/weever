package cn.people.weever.activity.poi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

import cn.people.weever.R;
import cn.people.weever.adapter.BaseListAdapter;

/**
 *   gps 定位数据适配器
 * Created by Administrator on 2016/12/26 0026.
 */

public class AddressGpsAdapter extends BaseListAdapter<PoiInfo> {
    private  Context mcontext;

    public AddressGpsAdapter(Context context, List<PoiInfo> list) {
        super(context, list, 0);
        this.mcontext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView ;
        View v = convertView;
        if(v == null){
            holderView = new HolderView();
            v = mInflater.inflate(R.layout.lv_address_location_item,null);
            holderView.iv_ischeck = (ImageView) v.findViewById(R.id.iv_ischeck);
            holderView.tv_address = (TextView)v.findViewById(R.id.tv_address);
            holderView.tv_name = (TextView)v.findViewById(R.id.tv_name);
            v.setTag(holderView);
        }else{
            holderView = (HolderView) v.getTag();
        }
        if(position == 0){
            holderView.iv_ischeck.setVisibility(View.VISIBLE);
        }else{
            holderView.iv_ischeck.setVisibility(View.INVISIBLE);
        }
        holderView.tv_address.setText(mList.get(position).address);
        holderView.tv_name.setText(mList.get(position).name);
        return v;
    }

    private class HolderView{
        TextView tv_name,tv_address;
        ImageView iv_ischeck;
    }
}
