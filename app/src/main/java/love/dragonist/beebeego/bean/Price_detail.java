package love.dragonist.beebeego.bean;

public class Price_detail {
    private int ticket_type;
    private int ticket_price;

    public Price_detail() {
    }

    public Price_detail(int ticket_type, int ticket_price) {
        this.ticket_type = ticket_type;
        this.ticket_price = ticket_price;
    }

    public int getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(int ticket_type) {
        this.ticket_type = ticket_type;
    }

    public int getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(int ticket_price) {
        this.ticket_price = ticket_price;
    }
}
