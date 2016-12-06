package Sensor;

public class OutSensor extends Sensor{

    private String latitude;
    private String longitude;
    private OutType type;

    public OutSensor(String id, String latitude, String longitude, OutType type) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public OutType getType() {
        return type;
    }

    public boolean isOut() {
        return true;
    }
}
