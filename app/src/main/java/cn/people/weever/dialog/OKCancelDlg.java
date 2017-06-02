package cn.people.weever.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.people.weever.R;


/**
 * OK Cancel 按钮的对话框
 * @author caozhentian
 *
 */
public class OKCancelDlg {

	public static void createCancelOKDlg(Context context , String hint , final ICancelOK cancelOk){
		if(cancelOk == null){
			return ;
		}
		String leftText  =  context.getString(R.string.ok)     ;
		String rightText =  context.getString(R.string.cancel) ;
		createCancelOKDlg(context , hint ,leftText ,rightText ,cancelOk) ;
	
	}
	
	public static void createCancelOKDlg(Context context , String hint , String leftText , String rightText ,final ICancelOK cancelOk){
		if(cancelOk == null){
			return ;
		}
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setContentView(R.layout.dlg_ok_cancel);//需要自定义
		View contentView = window.getDecorView();
		final TextView txt_hint=(TextView) contentView.findViewById(R.id.txt_hint);
		txt_hint.setText(hint);
	
		final TextView txt_ok = (TextView) contentView.findViewById(R.id.txt_ok);
		txt_ok.setText(leftText) ;
		final TextView txt_cancel=(TextView) contentView.findViewById(R.id.txt_cancel);
		txt_cancel.setText(rightText) ;
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);  
		txt_ok.setOnClickListener(new OnClickListener() {  
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				cancelOk.ok() ;
			}
		});
		txt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				cancelOk.cancel() ;
			}
		});
	
	}
}
