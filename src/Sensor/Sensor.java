package Sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public abstract class Sensor {
    private String id;
    private SensorType sensorType;

    protected Socket socket = null;
    protected BufferedReader br = null;
    protected PrintStream ps = null;

    public Sensor(String id, SensorType sensorType) {
        this.id = id;
        this.sensorType = sensorType;
    }

    public String getId() {
        return id;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public boolean isIn() {
        return false;
    }

    public boolean isOut() {
        return false;
    }

    public abstract boolean connection() throws IOException;

    public boolean disconnection() throws IOException {
        ps.println("DeconnexionCapteur;" + getId());
        String line = br.readLine();

        return line.equals("DeconnexionOK");
    }

    public void sendData(float data) {
        ps.println("ValeurCapteur;" + data);
    }

    @Override
    public String toString() {
        return id;
    }
}
