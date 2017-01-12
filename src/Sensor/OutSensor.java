package Sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class OutSensor extends Sensor implements Comparable<OutSensor> {
    private String latitude;
    private String longitude;

    public OutSensor(String id, SensorType sensorType, String latitude, String longitude) {
        this(id, sensorType, latitude, longitude, "", 0);
    }

    public OutSensor(String id, SensorType sensorType, String latitude,
                     String longitude, String ip, int port) {
        super(id, sensorType, ip, port);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean connection() throws IOException {
        socket = new Socket(getIp(), getPort());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ps = new PrintStream(socket.getOutputStream());

        ps.println("ConnexionCapteur;" + getId() + ";" + getSensorType().toString()
                + ";" + getLatitude() + ";" + getLongitude());
        String line = br.readLine();

        return line.equals("ConnexionOK");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OutSensor outSensor = (OutSensor) o;

        return getLatitude().equals(outSensor.getLatitude()) && getLongitude().equals(outSensor.getLongitude());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getLatitude().hashCode();
        result = 31 * result + getLongitude().hashCode();
        return result;
    }

    @Override
    public int compareTo(OutSensor sensor) {
        if (getLatitude().equals(sensor.getLatitude()))
            return getLongitude().compareTo(sensor.getLongitude());
        return getLatitude().compareTo(sensor.getLatitude());
    }
}
