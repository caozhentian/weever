/**
 * 
 */
package cn.people.weever.update;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author caozhentian
 *
 */
public class ServerVersionService {

	/** 获取服务器存放App 版本的相关信息
	 * @return
	 */
	public static void getServerVersion(Context baseInteractActivity){
		
	}
	
	
	
	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static  String getVersionCode(Context context)
	{
		String versionName = "1.0";
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionName = context.getPackageManager().getPackageInfo(UpdateManager.PACKAGE_NAME, 0).versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionName;
	}
}
