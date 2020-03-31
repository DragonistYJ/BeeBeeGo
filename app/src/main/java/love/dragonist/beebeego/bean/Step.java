package love.dragonist.beebeego.bean;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Step {
    private int distance;
    private int duration;
    private String instruction;
    private List<LatLng> paths;
    private Vehicle_info vehicle_info;

    public Step() {
    }

    public Step(int distance, int duration, String instruction, List<LatLng> paths, Vehicle_info vehicle_info) {
        this.distance = distance;
        this.duration = duration;
        this.instruction = instruction;
        this.paths = paths;
        this.vehicle_info = vehicle_info;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<LatLng> getPaths() {
        return paths;
    }

    public void setPaths(String path) {
        String[] paths = path.split(";");
        this.paths = new ArrayList<>();
        for (String s : paths) {
            String[] x = s.split(",");
            this.paths.add(new LatLng(Double.parseDouble(x[1]), Double.parseDouble(x[0])));
        }
    }

    public Vehicle_info getVehicle_info() {
        return vehicle_info;
    }

    public void setVehicle_info(Vehicle_info vehicle_info) {
        this.vehicle_info = vehicle_info;
    }

    @Override
    public String toString() {
        return "Step{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", instruction='" + instruction + '\'' +
                ", paths=" + paths +
                ", vehicle_info=" + vehicle_info +
                '}';
    }
}
