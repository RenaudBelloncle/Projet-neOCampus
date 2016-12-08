package Sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class OutSensor extends Sensor{
    private String latitude;
    private String longitude;

    public OutSensor(String id, SensorType sensorType, String latitude, String longitude) {
        super(id, sensorType);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean isOut() {
        return true;
    }

    public boolean connection() throws IOException {
        socket = new Socket("127.0.0.1", 7888);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ps = new PrintStream(socket.getOutputStream());

        ps.println("ConnexionCapteur;" + getId() + ";" + getSensorType().toString()
                + ";" + getLatitude() + ";" + getLongitude());
        String line = br.readLine();

        return line.equals("ConnexionOK");
    }
}
