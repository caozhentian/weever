package cn.people.weever.db;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import cn.people.weever.model.DaoMaster;
import cn.people.weever.model.DaoSession;

public class DaoManager {
	private static final String TAG = DaoManager.class.getSimpleName();
	private static final String DB_NAME = "danone.db";// 数据库名称
	private volatile static DaoManager mDaoManager;// 多线程访问
	private static DaoMaster.DevOpenHelper mHelper;
	private static DaoMaster mDaoMaster;
	private static DaoSession mDaoSession;
	private Context context;
	private Database sqLiteDatabase;

	/**
	 * 使用单例模式获得操作数据库的对象
	 *
	 * @return
	 */
	public static DaoManager getInstance() {
		DaoManager instance = null;
		if (mDaoManager == null) {
			synchronized (DaoManager.class) {
				if (instance == null) {
					instance = new DaoManager();
					mDaoManager = instance;
				}
			}
		}
		return mDaoManager;
	}

	/**
	 * 初始化Context对象
	 *
	 * @param context
	 */
	public DaoManager init(Context context) {
		this.context = context;
		return mDaoManager;
	}

	/**
	 * 判断数据库是否存在，如果不存在则创建
	 *
	 * @return
	 */
	public DaoMaster getDaoMaster() {
		if (null == mDaoMaster) {
            mHelper = new DaoMaster.DevOpenHelper(context , DB_NAME) ;
			sqLiteDatabase = mHelper.getWritableDb() ;
			mDaoMaster = new DaoMaster(sqLiteDatabase);
		}
		return mDaoMaster;
	}

	/**
	 * 完成对数据库的增删查找
	 *
	 * @return
	 */
	public DaoSession getDaoSession() {
		if (null == mDaoSession) {
			if (null == mDaoMaster) {
				mDaoMaster = getDaoMaster();
			}
			mDaoSession = mDaoMaster.newSession();

		}
		return mDaoSession;
	}

	/**
	 * 设置debug模式开启或关闭，默认关闭
	 *
	 * @param flag
	 */
	public void setDebug(boolean flag) {
		QueryBuilder.LOG_SQL = flag;
		QueryBuilder.LOG_VALUES = flag;
	}

	/**
	 * 关闭数据库
	 */
	public void closeDataBase() {
		mDaoMaster = null;
        closeDaoSession();
		closeHelper();
	}

	public void closeDaoSession() {
		if (null != mDaoSession) {
			mDaoSession.clear();
			mDaoSession = null;
		}
	}

	public void closeHelper() {
		if (mHelper != null) {
			mHelper.close();
			mHelper = null;
		}
	}
}
