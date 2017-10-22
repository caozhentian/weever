package cn.people.weever.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import cn.people.weever.R;


public class CustomProgressDialog extends Dialog {


	public CustomProgressDialog(Context context, int themeResId) {
		super(context, themeResId);
		//加载布局文件
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.progressdialog_layout, null);
		//dialog添加视图
		setContentView(view);
		setCanceledOnTouchOutside(false) ;
	}
	public void cancel(){
		dismiss();
	}
}
