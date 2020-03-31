package love.dragonist.beebeego.bean;

import java.sql.Date;
import java.util.Objects;

public class Record {
    private String telephone;
    private int departHash;
    private int arriveHash;
    private int routeId;
    private Route route;
    private Date date;
    private String departPosition;
    private String arrivePosition;

    public Record() {
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDepartPosition() {
        return departPosition;
    }

    public void setDepartPosition(String departPosition) {
        this.departPosition = departPosition;
    }

    public String getArrivePosition() {
        return arrivePosition;
    }

    public void setArrivePosition(String arrivePosition) {
        this.arrivePosition = arrivePosition;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public void setRouteId() {
        this.routeId = this.route.hashCode();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDepartHash() {
        return departHash;
    }

    public void setDepartHash(int departHash) {
        this.departHash = departHash;
    }

    public int getArriveHash() {
        return arriveHash;
    }

    public void setArriveHash(int arriveHash) {
        this.arriveHash = arriveHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;
        Record record = (Record) o;
        return departHash == record.departHash &&
                arriveHash == record.arriveHash &&
                routeId == record.routeId &&
                Objects.equals(telephone, record.telephone) &&
                Objects.equals(route, record.route) &&
                Objects.equals(date, record.date) &&
                Objects.equals(departPosition, record.departPosition) &&
                Objects.equals(arrivePosition, record.arrivePosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telephone, departHash, arriveHash, routeId, route, date, departPosition, arrivePosition);
    }

    @Override
    public String toString() {
        return "Record{" +
                "telephone='" + telephone + '\'' +
                ", departHash=" + departHash +
                ", arriveHash=" + arriveHash +
                ", routeId=" + routeId +
                ", route=" + route +
                ", date=" + date +
                ", departPosition='" + departPosition + '\'' +
                ", arrivePosition='" + arrivePosition + '\'' +
                '}';
    }
}
