package love.dragonist.beebeego.bean;

import java.sql.Time;
import java.util.HashMap;
import java.util.Objects;

public class Way {
    private String no;
    private String fromStationName;
    private Station fromStation;
    private String toStationName;
    private Station toStation;
    private Time startTime;
    private Time arriveTime;
    private int dayDifferent;
    private double price;
    private double swzPrice;
    private double ydPrice;
    private double edPrice;
    private double yzPrice;
    private double ywPrice;
    private double rwPrice;
    private double punctualityRate;
    private String type;

    public Way() {
    }

    public Way(String no, String fromStationName, Station fromStation, String toStationName, Station toStation, Time startTime, Time arriveTime, int dayDifferent, double price, double swzPrice, double ydPrice, double edPrice, double yzPrice, double ywPrice, double rwPrice, double punctualityRate) {
        this.no = no;
        this.fromStationName = fromStationName;
        this.fromStation = fromStation;
        this.toStationName = toStationName;
        this.toStation = toStation;
        this.startTime = startTime;
        this.arriveTime = arriveTime;
        this.dayDifferent = dayDifferent;
        this.price = price;
        this.swzPrice = swzPrice;
        this.ydPrice = ydPrice;
        this.edPrice = edPrice;
        this.yzPrice = yzPrice;
        this.ywPrice = ywPrice;
        this.rwPrice = rwPrice;
        this.punctualityRate = punctualityRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Way)) return false;
        Way way = (Way) o;
        return dayDifferent == way.dayDifferent &&
                Objects.equals(no, way.no) &&
                Objects.equals(fromStation, way.fromStation) &&
                Objects.equals(toStation, way.toStation) &&
                Objects.equals(startTime, way.startTime) &&
                Objects.equals(arriveTime, way.arriveTime) &&
                Objects.equals(type, way.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, fromStation, toStation, startTime, arriveTime, dayDifferent, type);
    }

    public void setType() {
        if (fromStation.isType()) {
            type = no.startsWith("K") ? "K" : "GD";
        } else {
            type = "F";
        }
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Time arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getDayDifferent() {
        return dayDifferent;
    }

    public void setDayDifferent(int dayDifferent) {
        this.dayDifferent = dayDifferent;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSwzPrice() {
        return swzPrice;
    }

    public void setSwzPrice(double swzPrice) {
        this.swzPrice = swzPrice;
    }

    public double getYdPrice() {
        return ydPrice;
    }

    public void setYdPrice(double ydPrice) {
        this.ydPrice = ydPrice;
    }

    public double getEdPrice() {
        return edPrice;
    }

    public void setEdPrice(double edPrice) {
        this.edPrice = edPrice;
    }

    public double getYzPrice() {
        return yzPrice;
    }

    public void setYzPrice(double yzPrice) {
        this.yzPrice = yzPrice;
    }

    public double getYwPrice() {
        return ywPrice;
    }

    public void setYwPrice(double ywPrice) {
        this.ywPrice = ywPrice;
    }

    public double getRwPrice() {
        return rwPrice;
    }

    public void setRwPrice(double rwPrice) {
        this.rwPrice = rwPrice;
    }

    public double getPunctualityRate() {
        return punctualityRate;
    }

    public void setPunctualityRate(double punctualityRate) {
        this.punctualityRate = punctualityRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Way{" +
                "no='" + no + '\'' +
                ", fromStationName='" + fromStationName + '\'' +
                ", fromStation=" + fromStation +
                ", toStationName='" + toStationName + '\'' +
                ", toStation=" + toStation +
                ", startTime=" + startTime +
                ", arriveTime=" + arriveTime +
                ", dayDifferent=" + dayDifferent +
                ", price=" + price +
                ", swzPrice=" + swzPrice +
                ", ydPrice=" + ydPrice +
                ", edPrice=" + edPrice +
                ", yzPrice=" + yzPrice +
                ", ywPrice=" + ywPrice +
                ", rwPrice=" + rwPrice +
                ", punctualityRate=" + punctualityRate +
                ", type='" + type + '\'' +
                '}';
    }
}
