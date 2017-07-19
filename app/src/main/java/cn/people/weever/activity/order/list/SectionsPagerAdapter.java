package cn.people.weever.activity.order.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Collections;
import java.util.List;

import cn.people.weever.model.BaseOrder;

/**
 * Created by Administrator on 2017/5/9.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<String> mTitles = Collections.emptyList();

    public SectionsPagerAdapter(FragmentManager fm , List<String> titles) {
        super(fm);
        mTitles = titles ;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position  ,getOrderStaus(position));
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles != null){
            return mTitles.get(position) ;
        }
        return null;
    }

    private int getOrderStaus(int position){
        if(position <2 ){
            return BaseOrder.ORDER_STAUS_APPOINTMENT  + position;
        }
        return BaseOrder.ORDER_STAUS_FINISH ;
    }
}
