package baidumapsdk.demo.search.poi;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.List;

/**
 * ListView的基础适配器，继承于BaseAdapter
 * 
 * 
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected List<T> mList;// 列表List
	protected LayoutInflater mInflater;// 布局管理
	protected OnCustomListener listener;
	
	protected static final int NO_DEFAULT = -1;// 有图片但是没有默认图
	protected static final int NO_IMAGE = 0;// 没有图片

	protected  Context mContext ;
	/**
	 * 构造器
	 * 
	 * @param context
	 * @param list
	 *            起始数据
	 * @param defaultId
	 *            NO_IMAGE为没有图片要显示，NO_DEFAULT为需要显示但没有默认图片，R.drawable.XXX为默认图id
	 */
	protected BaseListAdapter(Context context, List<T> list, int defaultId) {
		this(context, list, defaultId, 0);

	}

	/**
	 * 构造器
	 * 
	 * @param context
	 * @param list
	 *            起始数据
	 * @param defaultId
	 *            NO_IMAGE为没有图片要显示，NO_DEFAULT为需要显示但没有默认图片，R.drawable.XXX为默认图id
	 * @param radius
	 *            图片圆角半径值
	 */
	protected BaseListAdapter(Context context, List<T> list,
			int defaultId, int radius) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
		mContext = context ;
	}

	@Override
	public int getCount() {
		return (mList == null) ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return (mList == null) ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取该适配器的列表数据
	 * 
	 * @return
	 */
	public List<T> getData() {
		return mList;
	}

	public void setData( List<T> list) {
		mList = list;
	}
	/**
	 * 设置适配器上，某个控件的监听事件
	 * 
	 * @param listener
	 */
	public void setOnCustomListener(OnCustomListener listener) {
		this.listener = listener;
	}

	public void ShowTost(String s){
		Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
	}

}
