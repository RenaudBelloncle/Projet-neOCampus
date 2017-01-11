package Sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class InSensor extends Sensor implements Comparable<InSensor> {
    private String building;
    private String floor;
    private String room;
    private String description;

    public InSensor(String id, SensorType sensorType, String building, String floor,
                    String room, String description) {
        this(id, sensorType, building, floor, room, description, "", 0);
    }

    public InSensor(String id, SensorType sensorType, String building, String floor,
                    String room, String description, String ip, int port) {
        super(id, sensorType, ip, port);
        this.building = building;
        this.floor = floor;
        this.room = room;
        if (description.equals("")) {
            this.description = " ";
        } else {
            this.description = description;
        }
    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getRoom() {
        return room;
    }

    public String getDescription() {
        return description;
    }

    public boolean isIn() {
        return true;
    }

    public boolean connection() throws IOException {
        socket = new Socket(getIp(), getPort());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ps = new PrintStream(socket.getOutputStream());

        ps.println("ConnexionCapteur;" + getId() + ";" + getSensorType().toString() + ";" + getBuilding() + ";"
                + getFloor() + ";" + getRoom() + ";" + getDescription());
        String line = br.readLine();

        return line.equals("ConnexionOK");
    }

    @Override
    public int compareTo(InSensor sensor) {
        return getId().compareTo(sensor.getId());
    }
}
