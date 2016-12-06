package Sensor;

public abstract class Sensor {
    private String id;

    public Sensor(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isIn() {
        return false;
    }

    public boolean isOut() {
        return false;
    }
}
