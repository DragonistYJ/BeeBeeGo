package love.dragonist.beebeego.bean;

import android.util.Log;

import java.util.Objects;

import love.dragonist.beebeego.code.Path;

public class Route {
    private Way firstWay;
    private Way secondWay;
    private Way thirdWay;
    private double price;
    private long time;
    private double comfort;

    public Route() {
    }

    public Route(Way firstWay, Way secondWay, Way thirdWay, double price, long time, double comfort) {
        this.firstWay = firstWay;
        this.secondWay = secondWay;
        this.thirdWay = thirdWay;
        this.price = price;
        this.time = time;
        this.comfort = comfort;
    }

    public Route(Way firstWay) {
        this.firstWay = firstWay;
        this.secondWay = null;
        this.thirdWay = null;
        this.price = firstWay.getPrice();
        this.time = firstWay.getArriveTime().getTime() - firstWay.getStartTime().getTime() + firstWay.getDayDifferent() * 24 * 3600000;
        if (firstWay.getType().equals("K"))
            this.comfort = Path.K_COMFORT * Path.COMFORT_P + firstWay.getPunctualityRate() * Path.PUNCTUALITY_P;
        else if (firstWay.getType().equals("GD"))
            this.comfort = Path.GD_COMFORT * Path.COMFORT_P + firstWay.getPunctualityRate() * Path.PUNCTUALITY_P;
        else
            this.comfort = Path.F_COMFORT * Path.COMFORT_P + firstWay.getPunctualityRate() * Path.PUNCTUALITY_P;
    }

    public void setPrice() {
        if (secondWay == null) {
            price = firstWay.getPrice();
        } else if (thirdWay == null) {
            price = firstWay.getPrice() + secondWay.getPrice();
        } else {
            price = firstWay.getPrice() + secondWay.getPrice() + thirdWay.getPrice();
        }
    }

    public void setTime() {
        long time = 0;
        long t = 0;

        time += firstWay.getArriveTime().getTime() - firstWay.getStartTime().getTime() + firstWay.getDayDifferent() * 24 * 3600000;
        if (secondWay == null) {
            this.time = time;
            return;
        }

        t = secondWay.getStartTime().getTime() - firstWay.getArriveTime().getTime();
        time = t < 0 ? time + t + 24 * 3600000 : time + t;
        time += secondWay.getArriveTime().getTime() - secondWay.getStartTime().getTime() + secondWay.getDayDifferent() * 24 * 3600000;
        if (thirdWay == null) {
            this.time = time;
            return;
        }

        t = thirdWay.getStartTime().getTime() - secondWay.getArriveTime().getTime();
        time = t < 0 ? time + t + 24 * 3600000 : time + t;
        time += thirdWay.getArriveTime().getTime() - thirdWay.getStartTime().getTime() + thirdWay.getDayDifferent() * 24 * 3600000;
        this.time = time;
    }

    public void setComfort() {
        if (secondWay == null) {
            comfort = getScore(firstWay);
        } else if (thirdWay == null) {
            comfort = getScore(firstWay) * Path.TWO_FIRST + getScore(secondWay) * Path.TWO_SECOND;
        } else {
            comfort = getScore(firstWay) * Path.THREE_FIRST + getScore(secondWay) * Path.THREE_SECOND + getScore(thirdWay) * Path.THREE_THIRD;
        }
    }

    private double getScore(Way way) {
        double score;
        if (way.getType().equals("K"))
            score = Path.K_COMFORT * Path.COMFORT_P + way.getPunctualityRate() * Path.PUNCTUALITY_P;
        else if (way.getType().equals("GD"))
            score = Path.GD_COMFORT * Path.COMFORT_P + way.getPunctualityRate() * Path.PUNCTUALITY_P;
        else
            score = Path.F_COMFORT * Path.COMFORT_P + way.getPunctualityRate() * Path.PUNCTUALITY_P;
        return score;
    }

    public Way getFirstWay() {
        return firstWay;
    }

    public void setFirstWay(Way firstWay) {
        this.firstWay = firstWay;
    }

    public Way getSecondWay() {
        return secondWay;
    }

    public void setSecondWay(Way secondWay) {
        this.secondWay = secondWay;
    }

    public Way getThirdWay() {
        return thirdWay;
    }

    public void setThirdWay(Way thirdWay) {
        this.thirdWay = thirdWay;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getComfort() {
        return comfort;
    }

    public void setComfort(double comfort) {
        this.comfort = comfort;
    }

    public boolean isBetter(Route route, double pPrice, double pTime, double pComfort) {
        double proportion;
        if (route.getPrice() == 0.0 || price == 0.0) {
            proportion = route.getTime() * 1.0 / time * pTime + comfort / route.getComfort() * pComfort;
        } else {
            proportion = route.getPrice() / price * pPrice + route.getTime() * 1.0 / time * pTime + comfort / route.getComfort() * pComfort;
        }
        return proportion > 1.0;
    }

    @Override
    public String toString() {
        return "Route{" +
                "firstWay=" + firstWay +
                ", secondWay=" + secondWay +
                ", thirdWay=" + thirdWay +
                ", price=" + price +
                ", time=" + time +
                ", comfort=" + comfort +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return Objects.equals(firstWay, route.firstWay) &&
                Objects.equals(secondWay, route.secondWay) &&
                Objects.equals(thirdWay, route.thirdWay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstWay, secondWay, thirdWay);
    }
}
