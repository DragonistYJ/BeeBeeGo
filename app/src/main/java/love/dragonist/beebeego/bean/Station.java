package love.dragonist.beebeego.bean;

import com.baidu.mapapi.model.LatLng;

import java.util.Objects;

public class Station {
    private String name;
    private String province;
    private String city;
    private String district;
    private double longitude;
    private double latitude;
    private boolean type;
    private int level;

    public Station() {
    }

    public Station(String name, String province, String city, String district, double longitude, double latitude, boolean type, int level) {
        this.name = name;
        this.province = province;
        this.city = city;
        this.district = district;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.level = level;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return type == station.type &&
                level == station.level &&
                Objects.equals(name, station.name) &&
                Objects.equals(province, station.province) &&
                Objects.equals(city, station.city) &&
                Objects.equals(district, station.district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, province, city, district, type, level);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", type=" + type +
                ", level=" + level +
                '}';
    }
}
