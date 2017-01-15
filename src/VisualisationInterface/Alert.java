package VisualisationInterface;

import Sensor.SensorType;

public class Alert {

    private String id;
    private SensorType sensorType;
    private double min;
    private double max;

    public Alert(String id, SensorType sensorType, double min, double max) {
        this.id = id;
        this.sensorType = sensorType;
        this.min = min;
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
