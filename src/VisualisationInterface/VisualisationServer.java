package VisualisationInterface;

import Sensor.InSensor;
import Sensor.OutSensor;
import Sensor.SensorType;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualisationServer implements Runnable {
    private VisualisationFrame frame;

    private BufferedReader br;
    private PrintStream ps;
    private FileWriter fw;

    private Thread thread;
    private boolean isRunning = false;

    private Map<String, InSensor> inSensorMap = new HashMap<>();
    private Map<String, OutSensor> outSensorMap = new HashMap<>();

    public VisualisationServer(VisualisationFrame frame) {
        this.frame = frame;
    }

    public synchronized boolean connect(String id, String ip, int port) {
        startThread();
        return openFileWriter() && connectServer(id, ip, port);
    }

    private synchronized void startThread() {
        isRunning = true;
        thread = new Thread(this, "ReceiveData");
        thread.start();
    }

    private boolean openFileWriter() {
        String path = System.getProperty("user.dir");
        String name = "data.txt";
        File dataFile = new File(path + "/" + name);

        try {
            fw = new FileWriter(dataFile, true);
        } catch (IOException e) {
            frame.sendErrorMessage("Erreur lors de l'ouverture du fichier de données");
            return false;
        }
        return true;
    }

    private boolean connectServer(String id, String ip, int port) {
        Socket socket = null;
        boolean isConnected = false;

        try {
            socket = new Socket(ip, port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());
            ps.println("ConnexionVisu;" + id);
            String line = br.readLine();
            isConnected = line.equals("ConnexionOK");
        } catch (IOException e) {
            frame.sendErrorMessage("Echec de la connexion au serveur");
            return false;
        }
        return isConnected;
    }

    public synchronized boolean disconnect() {
        disconnectServer();
        boolean isClosed = closeFileWriter();
        stopThread();
        return isClosed;
    }

    private void disconnectServer() {
        ps.println("DeconnexionVisu");
    }

    private boolean closeFileWriter() {
        try {
            fw.close();
        } catch (IOException e) {
            frame.sendErrorMessage("Erreur lors de la fermeture du fichier de données");
            return false;
        }
        return true;
    }

    private synchronized void stopThread() {
        isRunning = false;
        thread.interrupt();
    }

    private void receiveLine() {
        try {
            String line = br.readLine();
            if (line == null) return;

            String[] tokens = line.split(";");
            switch (tokens[0]) {
                case "CapteurPresent":
                    addSensor(tokens);
                    break;
                case "CapteurDeco":
                    removeSensor(tokens);
                    break;
                case "ValeurCapteur":
                    printLine(tokens[1] + ";"
                            + LocalDateTime.now().toString() + "Z;"
                            + tokens[2] + "\n");
                    break;
                case "InscriptionCapteurKO":
                    frame.sendErrorMessage("Echec de l'inscription à certains capteurs");
                    break;
                case "DesinscriptionCapteurKO":
                    frame.sendErrorMessage("Echec de la désinscription à certains capteurs");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            receiveLine();
        }
        stopThread();
    }

    private void addSensor(String[] tokens) {
        SensorType type = SensorType.STRINGTOTYPE(tokens[2]);
        if (tokens.length == 8) {
            inSensorMap.put(tokens[1], new InSensor(tokens[1], type, tokens[3],
                    tokens[4], tokens[5], tokens[6]));
        }
        else {
            outSensorMap.put(tokens[1], new OutSensor(tokens[1], type, tokens[3], tokens[4]));
        }
    }

    private void removeSensor(String[] tokens) {
        if (inSensorMap.containsKey(tokens[1]))
            inSensorMap.remove(tokens[1]);
        else if (outSensorMap.containsKey(tokens[1]))
            outSensorMap.remove(tokens[1]);
    }

    private void printLine(String line) {
        try {
            fw.write(line);
        } catch (IOException e) {
            frame.sendErrorMessage("Erreur lors de l'écriture dans le fichier de données");
        }
    }

    public void signInSensors(String[] ids) {
        String line = "InscriptionCapteur";
        for (String id: ids)
            line += ";" + id;
        ps.println(line);
    }

    public void signOutSensors(String[] ids) {
        String line = "DesinscriptionCapteur";
        for (String id: ids)
            line += ";" + id;
        ps.println(line);
    }

    public List<InSensor> getListInSensor() {
        List<InSensor> inSensors = new ArrayList<>();
        for (String id: inSensorMap.keySet())
            inSensors.add(inSensorMap.get(id));
        return inSensors;
    }

    public List<OutSensor> getListOutSensor() {
        List<OutSensor> outSensors = new ArrayList<>();
        for (String id: outSensorMap.keySet())
            outSensors.add(outSensorMap.get(id));
        return outSensors;
    }
}
