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
import cn.people.weever.dialog.IOK;


/**
 * OK Cancel 按钮的对话框
 * @author caozhentian
 *
 */
public class OKlDlg {

	public static void createOKDlg(Context context , String hint , final IOK iOK){
		if(iOK == null){
			return ;
		}
		String leftText  =  context.getString(R.string.ok)     ;
		createOKDlg(context , hint ,leftText ,iOK)        ;
	}
	
	public static void createOKDlg(Context context , String hint , String leftText ,final IOK iOK){
		if(iOK == null){
			return ;
		}
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setContentView(R.layout.dlg_ok);//需要自定义
		View contentView = window.getDecorView();
		final TextView txt_hint=(TextView) contentView.findViewById(R.id.txt_hint);
		txt_hint.setText(hint);
	
		final TextView txt_ok = (TextView) contentView.findViewById(R.id.txt_ok);
		txt_ok.setText(leftText) ;
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);  
		txt_ok.setOnClickListener(new OnClickListener() {  
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				iOK.ok() ;
			}
		});
	
	}
}
