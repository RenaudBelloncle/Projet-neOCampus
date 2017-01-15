package VisualisationInterface;

import Sensor.InSensor;
import Sensor.OutSensor;
import Sensor.SensorType;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class VisualisationServer implements Runnable {
    private VisualisationFrame frame;

    private BufferedReader br;
    private PrintStream ps;

    private Thread thread;
    private boolean isRunning = false;

    private Map<String, InSensor> inSensorMap = new HashMap<>();
    private Map<String, OutSensor> outSensorMap = new HashMap<>();

    private boolean isConnected = false;

    public VisualisationServer(VisualisationFrame frame) {
        this.frame = frame;
    }

    public synchronized boolean connect(String id, String ip, int port) {
        connectServer(id, ip, port);
        startThread();
        return isConnected;
    }

    private synchronized void startThread() {
        isRunning = true;
        thread = new Thread(this, "ReceiveData");
        thread.start();
    }

    private void connectServer(String id, String ip, int port) {
        Socket socket;

        try {
            socket = new Socket(ip, port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());
            ps.println("ConnexionVisu;" + id);
            String line = br.readLine();
            isConnected = line.equals("ConnexionOK");
            if (isConnected)
                frame.sendMessage("Connexion au serveur réussi");
        } catch (IOException e) {
            frame.sendMessage("Echec de la connexion au serveur");
        }
    }

    public synchronized boolean disconnect() {
        stopThread();
        disconnectServer();
        return !isConnected;
    }

    private void disconnectServer() {
        ps.println("DeconnexionVisu");
        isConnected = false;
    }

    private synchronized void stopThread() {
        isRunning = false;
        thread.interrupt();
    }

    private void receiveLine() {
        try {
            if (br == null) return;
            String line = br.readLine();
            if (line == null) return;

            String[] tokens = line.split(";");
            switch (tokens[0]) {
                case "CapteurPresent":
                    addSensor(tokens);
                    frame.sendMessage("Capteur présent sur le serveur: " + tokens[1]);
                    break;
                case "CapteurDeco":
                    removeSensor(tokens);
                    frame.sendMessage(tokens[1] + " n'est plus sur le serveur");
                    break;
                case "ValeurCapteur":
                    frame.updateData(tokens[1], Double.parseDouble(tokens[2].replace(",",".")));
                    frame.sendMessage("Valeur reçu: " + tokens[1] + ", " + tokens[2]);
                    break;
                case "InscriptionCapteurKO":
                    frame.sendMessage("Echec de l'inscription à certains capteurs");
                    break;
                case "DesinscriptionCapteurKO":
                    frame.sendMessage("Echec de la désinscription à certains capteurs");
                    break;
                case "DeconnexionOK":
                    frame.sendMessage("Déconnexion du serveur");
                    isConnected = false;
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
        if (tokens.length == 7) {
            InSensor sensor = new InSensor(tokens[1], type, tokens[3],
                    tokens[4], tokens[5], tokens[6]);
            inSensorMap.put(tokens[1], sensor);
            frame.addSensor(sensor);
        } else {
            OutSensor sensor = new OutSensor(tokens[1], type, tokens[3], tokens[4]);
            outSensorMap.put(tokens[1], sensor);
            frame.addSensor(sensor);
        }
    }

    private void removeSensor(String[] tokens) {
        if (inSensorMap.containsKey(tokens[1]))
            inSensorMap.remove(tokens[1]);
        else if (outSensorMap.containsKey(tokens[1]))
            outSensorMap.remove(tokens[1]);
        frame.removeSensor(tokens[1]);
    }

    public void signInSensors(List<String> ids) {
        String line = "InscriptionCapteur";
        for (String id: ids)
            line += ";" + id;
        ps.println(line);
    }

    public void signOutSensors(List<String> ids) {
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

    public boolean isConnected() {
        return isConnected;
    }
}
