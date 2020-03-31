package love.dragonist.beebeego.bean;

public class Detail {
    private String first_time;
    private String last_time;
    private String on_station;
    private String off_station;
    private int stop_num;
    private String name;
    private int type;

    public Detail() {
    }

    public Detail(String first_time, String last_time, String on_station, String off_station, int stop_num, String name, int type) {
        this.first_time = first_time;
        this.last_time = last_time;
        this.on_station = on_station;
        this.off_station = off_station;
        this.stop_num = stop_num;
        this.name = name;
        this.type = type;
    }

    public String getFirst_time() {
        return first_time;
    }

    public void setFirst_time(String first_time) {
        this.first_time = first_time;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getOn_station() {
        return on_station;
    }

    public void setOn_station(String on_station) {
        this.on_station = on_station;
    }

    public String getOff_station() {
        return off_station;
    }

    public void setOff_station(String off_station) {
        this.off_station = off_station;
    }

    public int getStop_num() {
        return stop_num;
    }

    public void setStop_num(int stop_num) {
        this.stop_num = stop_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
