package love.dragonist.beebeego.bean;

public class User {
    private String telephone;
    private String password;
    private String portrait;
    private double pPrice;
    private double pTime;
    private double pComfort;
    private int state;

    public User() {
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getpPrice() {
        return pPrice;
    }

    public void setpPrice(double pPrice) {
        this.pPrice = pPrice;
    }

    public double getpTime() {
        return pTime;
    }

    public void setpTime(double pTime) {
        this.pTime = pTime;
    }

    public double getpComfort() {
        return pComfort;
    }

    public void setpComfort(double pComfort) {
        this.pComfort = pComfort;
    }
}
