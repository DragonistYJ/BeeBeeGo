package love.dragonist.beebeego.bean;

import java.util.ArrayList;
import java.util.List;

public class InsideRoutesHolder {
    private static final List<InsideRoute> insideRoutes = new ArrayList<>();

    public static List<InsideRoute> getInstance() {
        return insideRoutes;
    }
}
