package love.dragonist.beebeego.bean;

public class Vehicle_info {
    private int type;
    private Detail detail;

    public Vehicle_info() {
    }

    public Vehicle_info(int type, Detail detail) {
        this.type = type;
        this.detail = detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }
}
