package love.dragonist.beebeego.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.walknavi.WalkNavigateHelper;

import love.dragonist.beebeego.R;

public class GuideActivity extends Activity {
    WalkNavigateHelper walkNavigateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取WalkNavigateHelper实例
        walkNavigateHelper = WalkNavigateHelper.getInstance();
        //获取诱导页面地图展示View
        try {
            View view = walkNavigateHelper.onCreate(GuideActivity.this);
            if (view != null) {
                setContentView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        walkNavigateHelper.startWalkNavi(GuideActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        walkNavigateHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        walkNavigateHelper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        walkNavigateHelper.quit();
    }
}
