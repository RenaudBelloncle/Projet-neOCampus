package VisualisationInterface;

import java.util.Date;

public class Data {
    private double data;
    private Date date;

    public Data(double data, Date date) {
        this.data = data;
        this.date = date;
    }

    public double getData() {
        return data;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Data{" +
                "data=" + data +
                ", date=" + date +
                '}';
    }
}
