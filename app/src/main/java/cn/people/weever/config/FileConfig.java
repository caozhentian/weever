package cn.people.weever.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/6/5.
 */

public class FileConfig {
    public static final String APP_FOLDER_NAME = "Weever";
    public static String       S_SDCardPath = null;

    public static boolean initDirs() {
        S_SDCardPath = getSdcardDir();
        if (S_SDCardPath == null) {
            return false;
        }
        File f = new File(S_SDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
}
