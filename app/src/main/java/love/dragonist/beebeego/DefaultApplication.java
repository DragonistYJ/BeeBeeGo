package love.dragonist.beebeego;

import android.app.Application;
import android.os.Environment;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBaiduNaviManager;

import java.io.File;

public class DefaultApplication extends Application {
    private static final String APP_FOLDER_NAME = "BeeBeeGo";
    private String mSDCardPath = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

//        if (initDirs()) {
//            BaiduNaviManagerFactory.getBaiduNaviManager().init(this, mSDCardPath, APP_FOLDER_NAME,
//                    new IBaiduNaviManager.INaviInitListener() {
//                        @Override
//                        public void onAuthResult(int i, String s) {
//
//                        }
//
//                        @Override
//                        public void initStart() {
//
//                        }
//
//                        @Override
//                        public void initSuccess() {
//
//                        }
//
//                        @Override
//                        public void initFailed(int i) {
//
//                        }
//                    });
//            BaiduNaviManagerFactory.getTTSManager().initTTS(this, mSDCardPath, APP_FOLDER_NAME, "16798130");
//        }
    }

//    private boolean initDirs() {
//        mSDCardPath = getSdcardDir();
//        if (mSDCardPath == null) {
//            return false;
//        }
//        File f = new File(mSDCardPath, APP_FOLDER_NAME);
//        if (!f.exists()) {
//            try {
//                f.mkdir();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private String getSdcardDir() {
//        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
//            return Environment.getExternalStorageDirectory().toString();
//        }
//        return null;
//    }
}
