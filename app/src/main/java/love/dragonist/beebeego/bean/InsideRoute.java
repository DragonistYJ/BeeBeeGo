package love.dragonist.beebeego.bean;

import java.util.List;

public class InsideRoute {
    private int distance;
    private int duration;
    private int price;
    private List<Price_detail> price_details;
    private List<Step> steps;

    public InsideRoute() {
    }

    public InsideRoute(int distance, int duration, int price, List<Price_detail> price_details, List<Step> steps) {
        this.distance = distance;
        this.duration = duration;
        this.price = price;
        this.price_details = price_details;
        this.steps = steps;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Price_detail> getPrice_details() {
        return price_details;
    }

    public void setPrice_details(List<Price_detail> price_details) {
        this.price_details = price_details;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "InsideRoute{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", price=" + price +
                ", price_details=" + price_details +
                ", steps=" + steps +
                '}';
    }
}
