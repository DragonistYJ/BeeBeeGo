package love.dragonist.beebeego.util;


import android.util.Log;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.List;

import love.dragonist.beebeego.bean.Route;

public class Format {
    public static String getNameByReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult.getPoiRegionsInfoList() == null || reverseGeoCodeResult.getPoiRegionsInfoList().size() == 0) {
            String ans = reverseGeoCodeResult.getSematicDescription().split(",")[0];
            if (ans.contains("(")) {
                ans = ans.substring(0, ans.indexOf("("));
            }
            return ans;
        } else {
            return reverseGeoCodeResult.getPoiList().get(0).getName();
        }
    }

    public static void sortRoute(List<Route> routes, double pPrice, double pTime, double pComfort) {
        for (int i = 0; i < routes.size() - 1; i++) {
            for (int j = routes.size() - 1; j > i; j--) {
                if (routes.get(j).isBetter(routes.get(j - 1), pPrice, pTime, pComfort)) {
                    Route route = routes.get(j);
                    routes.set(j, routes.get(j - 1));
                    routes.set(j - 1, route);
                }
            }
        }
    }
}
