package Sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Sensor implements Runnable {
    private String id;
    private SensorType sensorType;
    private String ip;
    private int port;

    protected Socket socket = null;
    protected BufferedReader br = null;
    protected PrintStream ps = null;

    private long frequency;
    private double data;
    private boolean isRandom = false;
    private Thread thread;
    private boolean running = false;
    private DecimalFormat decimalFormat;

    private static Random random = new Random();

    public Sensor(String id, SensorType sensorType, String ip, int port) {
        this.id = id;
        this.sensorType = sensorType;
        this.ip = ip;
        this.port = port;
        frequency = sensorType.getFrequency();
        decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(sensorType.getPrecision());
    }

    public String getId() {
        return id;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isIn() {
        return false;
    }

    public abstract boolean connection() throws IOException;

    public boolean disconnection() throws IOException {
        ps.println("DeconnexionCapteur;" + getId());
        String line = br.readLine();

        return line.equals("DeconnexionOK");
    }

    public void sendData(double data, long frequency) {
        this.data = data;
        this.frequency = frequency;
        start();
    }

    public void sendRandomData(long frequency) {
        isRandom = true;
        this.frequency = frequency;
        start();
    }

    public synchronized void stopSendingData() {
        running = false;
        thread.interrupt();
    }

    private synchronized void start() {
        running = true;
        thread = new Thread(this, "SendData");
        thread.start();
    }

    public void run() {
        while (running) {
            if (isRandom) {
                data = sensorType.getInterval()[0] + (sensorType.getInterval()[1]
                        - sensorType.getInterval()[0]) * random.nextDouble();
            }
            ps.println("ValeurCapteur;" + decimalFormat.format(data));
            try {
                TimeUnit.SECONDS.sleep(frequency);
            } catch (InterruptedException ignored) {
            }
        }
        stopSendingData();
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sensor sensor = (Sensor) o;

        return getId().equals(sensor.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
