package VisualisationInterface;

import Sensor.InSensor;
import Sensor.OutSensor;
import Sensor.SensorType;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class TestServer implements Runnable {
    private static Scanner scanner = new Scanner(System.in);

    private BufferedReader br;
    private PrintStream ps;
    private FileWriter fw;

    private Thread thread;
    private boolean isRunning = false;

    private Map<String, InSensor> inSensors = new HashMap<>();
    private Map<String, OutSensor> outSensors = new HashMap<>();

    private void connect() {
        try {
            Socket socket = new Socket("127.0.0.1", 7888);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());

            ps.println("ConnexionVisu;Test");
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveLine() {
        try {
            String line = br.readLine();
            if (line == null)
                return;
            String[] tokens = line.split(";");
            switch (tokens[0]) {
                case "CapteurPresent":
                    addSensor(tokens);
                    break;
                case "CapteurDeco":
                    removeSensor(tokens);
                    break;
                case "ValeurCapteur":
                    printLine(tokens[1] + ";" + LocalDateTime.now().toString()
                            + "Z;" + tokens[2] + "\n");
                    break;
                default:
                    System.out.println(line);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        ps.println("DeconnexionVisu");
    }

    public synchronized void start() {
        isRunning = true;
        thread = new Thread(this, "ReceiveData");
        thread.start();
    }

    @Override
    public void run() {
        connect();
        openFileWriter();
        while (isRunning) {
            receiveLine();
        }
        stop();
    }

    public synchronized void stop() {
        disconnect();
        closeFileWriter();
        isRunning = false;
        thread.interrupt();
    }

    private void addSensor(String[] tokens) {
        SensorType type = SensorType.STRINGTOTYPE(tokens[2]);
        if (tokens.length == 8)
            inSensors.put(tokens[1], new InSensor(tokens[1], type, tokens[3],
                    tokens[4], tokens[5], tokens[6]));
        else
            outSensors.put(tokens[1], new OutSensor(tokens[1], type, tokens[3], tokens[4]));
    }

    private void removeSensor(String[] tokens) {
        if (inSensors.containsKey(tokens[1]))
            inSensors.remove(tokens[1]);
        else if (outSensors.containsKey(tokens[1]))
            outSensors.remove(tokens[1]);
    }

    private void openFileWriter() {
        String path = System.getProperty("user.dir");
        String name = "data.txt";
        File dataFile = new File(path + "/" + name);
        try {
            fw = new FileWriter(dataFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeFileWriter() {
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printLine(String line) {
        try {
            fw.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signInSensors(String[] ids) {
        String line = "InscriptionCapteur";
        for (String id: ids) {
            line += ";" + id;
        }
        ps.println(line);
    }

    public void signOutSensors(String[] ids) {
        String line = "DesinscriptionCapteur";
        for (String id: ids) {
            line += ";" + id;
        }
        ps.println(line);
    }

    public static void main(String[] args) {
        TestServer testServer = new TestServer();
        testServer.start();
        while (!Objects.equals(scanner.next(), "Deco")) {}
        testServer.stop();
    }
}
