package cn.people.weever.update;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.people.weever.R;


/**
 *@author caozhentian
 *@date 2016-8-1
 */

public class UpdateManager
{
	public static final String URL_BANNER_HEAD = "" ;
	public static final String DIR_APK = "" ;
	public static final String PACKAGE_NAME = "cn.people.weever" ;
	
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;

	/**
	 * 下载失败
	 */
	private static final int DOWNLOAD_FAIL   = 3;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private volatile boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private UpdateAPKModel mUpdateAPKModel ; //云端更新的模型 
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FAIL:
				Toast.makeText(mContext, R.string.soft_update_fail, Toast.LENGTH_LONG).show();
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context , UpdateAPKModel updateAPKModel)
	{
		this.mContext   =  context;
		mUpdateAPKModel =  updateAPKModel ;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate()
	{
		if (isUpdate())
		{
			// 显示提示对话框
			showNoticeDialog();
		} else
		{
			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	public boolean isUpdate()
	{
		// 获取当前软件版本
		String versionCode = getVersionCode(mContext);
		
		if (null != mUpdateAPKModel)
		{
			String serviceVersionCode = mUpdateAPKModel.getVersionNo() ;
			// 版本判断
			if (serviceVersionCode.compareTo(versionCode) > 0)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private String getVersionCode(Context context)
	{
		return ServerVersionService.getVersionCode(context);
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{

	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		mDownloadDialog.setCanceledOnTouchOutside(false) ;
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			FileOutputStream fos  = null   ;
			InputStream is        = null   ;
			try
			{
				
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					mSavePath = DIR_APK;
					URL url = new URL(URL_BANNER_HEAD + mUpdateAPKModel.getDownloadUrl());
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mUpdateAPKModel.getOriginalFilename());
					fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					if(fos != null){
						fos.close();
					}
					if(is != null ){
						is.close();
					}
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
				mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
			} catch (IOException e)
			{
				e.printStackTrace();
				mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
			}
			finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(is != null ){
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mUpdateAPKModel.getOriginalFilename());
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}
}
