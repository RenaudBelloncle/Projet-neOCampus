package VisualisationInterface;

import Sensor.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.List;

public class VisualisationFrame extends JFrame implements TreeSelectionListener, ActionListener {

    private JTabbedPane tabbed_panel;
    private JScrollPane scroll_panel;
    private JScrollPane scroll_area;
    private JSplitPane main_split_panel;
    private JSplitPane split_panel;

    private JTextArea dialog_area;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenu data;
    private JMenuItem close;
    private JMenuItem closeAll;
    private JMenuItem alert;
    private JMenuItem closeAlert;
    private JMenuItem connection;
    private JMenuItem signIn;

    private JTree tree;
    private DefaultMutableTreeNode top = new DefaultMutableTreeNode("");

    private int nb_Sensor = 0;
    private int nbAlert = 0;
    private JLabel status;
    private JLabel nb_Sensor_label;
    private JLabel nb_Alert_label;

    private Set<InSensor> inSensors = new TreeSet<>();
    private Set<OutSensor> outSensors = new TreeSet<>();
    private Map<String, List<Data>> sensorData = new HashMap<>();
    private Set<String> openPanel = new TreeSet<>();
    private Set<VisualisationTabPanel> openTabPanel = new TreeSet<>();

    private VisualisationServer server = new VisualisationServer(this);
    private List<Sensor> signList = new ArrayList<>();
    private List<Alert> alertList = new ArrayList<>();

    private List<String> building_List = new ArrayList<>();
    private List<String> floor_List = new ArrayList<>();
    private List<String> room_List = new ArrayList<>();


    public VisualisationFrame(){
        super("Visualisation des capteurs");
        setSize(850, 700);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeOperation();
            }
        });
        initialize();
        place();
        setListener();
        setResizable(false);
        setVisible(true);
    }

    private void initialize() {
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        data = new JMenu("Données");
        signIn = new JMenuItem("Inscription / Désinscription aux capteurs");
        connection = new JMenuItem("Connexion / Déconnexion");
        close = new JMenuItem("Fermer onglet actif");
        closeAll = new JMenuItem("Fermer tous les onglets");
        alert = new JMenuItem("Créer une alerte");
        closeAlert = new JMenuItem("Supprimer une alerte");
        status = new JLabel("   Status : Déconnecté    ");
        nb_Sensor_label = new JLabel("Nb capteurs : "+nb_Sensor);
        nb_Alert_label = new JLabel("   Nb alertes : "+nbAlert);
        tabbed_panel = new JTabbedPane(SwingConstants.TOP);
        dialog_area = new JTextArea(10,25);

        readConfigFile();
        readDataFile();
        createJTree();

        scroll_panel = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_area = new JScrollPane(dialog_area, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        split_panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll_area, scroll_panel);
        main_split_panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split_panel, tabbed_panel);

        dialog_area.setEditable(false);
        dialog_area.setForeground(Color.BLACK);

    }

    private void place() {
        Container content = getContentPane();
        menu.add(connection);
        menu.add(signIn);
        data.add(close);
        data.add(closeAll);
        data.add(alert);
        data.add(closeAlert);
        menuBar.add(menu);
        menuBar.add(data);
        menuBar.add(status);
        menuBar.add(nb_Sensor_label);
        menuBar.add(nb_Alert_label);
        setJMenuBar(menuBar);
        content.add(main_split_panel);
    }

    private void setListener() {
        tree.addTreeSelectionListener(this);
        signIn.addActionListener(this);
        connection.addActionListener(this);
        close.addActionListener(this);
        closeAll.addActionListener(this);
        alert.addActionListener(this);
        closeAlert.addActionListener(this);
    }

    public void sendMessage(String text) {
        dialog_area.setForeground(Color.BLACK);
        dialog_area.append(text + "\n");
    }


    private void closeOperation() {
        if (server.isConnected()) {
            JOptionPane.showMessageDialog(this, "Vous devez vous déconnecter\n" +
                    " avant de fermer cette fenêtre !");
        } else {
            setVisible(false);
            System.exit(0);
        }
    }

    private void readDataFile() {
        String path = System.getProperty("user.dir");
        String name = "data.txt";

        File dataFile = new File(path + "/" + name);
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length == 3) {
                    String id = tokens[0];
                    Date date = Date.from(Instant.parse(tokens[1]));
                    Double value = Double.valueOf(tokens[2]);

                    java.util.List<Data> sensorValues = new ArrayList<>();
                    if (sensorData.containsKey(id))
                        sensorValues = sensorData.get(id);
                    sensorValues.add(new Data(value, date));

                    if (sensorData.containsKey(id))
                        sensorData.replace(id, sensorValues);
                    else
                        sensorData.put(id, sensorValues);
                } else {
                    SensorType type = SensorType.STRINGTOTYPE(tokens[1]);
                    switch (tokens[2]) {
                        case "Intérieur":
                            inSensors.add(new InSensor(tokens[0], type, tokens[3],
                                    tokens[4], tokens[5], tokens[6]));
                            break;
                        case "Extérieur":
                            outSensors.add(new OutSensor(tokens[0], type, tokens[3], tokens[4]));
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendMessage("Fichier de données lu");
    }

    private void readConfigFile() {
        String path = System.getProperty("user.dir");
        String name = "config.txt";
        File configFile = new File(path + "/" + name);
        try {
            if (configFile.createNewFile()) {
                JOptionPane.showMessageDialog(this, "Le fichier de configuration n'a pas été trouvé.\n" +
                        "Un fichier vierge a été créé.");
            }
            List<String> list = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("Étages")) {
                    building_List.addAll(list);
                    list.clear();
                } else if (line.equals("Salles")) {
                    floor_List.addAll(list);
                    list.clear();
                } else if (!line.equals("Bâtiments")) {
                    list.add(line);
                }
            }
            room_List.addAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createJTree() {
        // Second Floor
        DefaultMutableTreeNode in = new DefaultMutableTreeNode("Intérieur");
        top.add(in);
        DefaultMutableTreeNode out = new DefaultMutableTreeNode("Extérieur");
        top.add(out);

        for (String building: building_List) {
            DefaultMutableTreeNode buildingNode = new DefaultMutableTreeNode(building);
            in.add(buildingNode);

            for (String floor: floor_List) {
                DefaultMutableTreeNode floorNode = new DefaultMutableTreeNode(floor);
                buildingNode.add(floorNode);

                for (String room: room_List) {
                    if (room.substring(0,1).equals(floor)) {
                        DefaultMutableTreeNode roomNode = new DefaultMutableTreeNode(room);
                        floorNode.add(roomNode);
                    }
                }
            }
        }

        for (InSensor sensor: inSensors) {
            createNodeInSensor(sensor);
        }

        for (OutSensor sensor: outSensors) {
            createNodeOutSensor(sensor);
        }

        tree = new JTree(top);
    }

    private void createNodeInSensor(InSensor sensor) {
        DefaultMutableTreeNode in = findNextNode(top, "Intérieur");

        if (in != null) {
            DefaultMutableTreeNode building = findNextNode(in, sensor.getBuilding());

            if (building != null) {
                DefaultMutableTreeNode floor = findNextNode(building, sensor.getFloor());

                if (floor != null) {
                    DefaultMutableTreeNode room = findNextNode(floor, sensor.getRoom());

                    if (room != null) {
                        DefaultMutableTreeNode sensorNode = new DefaultMutableTreeNode(sensor.getId());
                        room.add(sensorNode);
                    }
                }
            }
        }
    }

    private DefaultMutableTreeNode findNextNode(DefaultMutableTreeNode parent, String node) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).toString().equals(node))
                return (DefaultMutableTreeNode) parent.getChildAt(i);
        }
        return null;
    }

    private void createNodeOutSensor(OutSensor sensor) {
        DefaultMutableTreeNode out = findNextNode(top, "Extérieur");

        if (out != null) {
            DefaultMutableTreeNode sensorNode = new DefaultMutableTreeNode(sensor.getId());
            out.add(sensorNode);
        }
    }


    public void addSensor(Sensor sensor) {
        if (sensor.isIn()) {
            inSensors.add((InSensor) sensor);
            createNodeInSensor((InSensor) sensor);
        } else {
            outSensors.add((OutSensor) sensor);
            createNodeOutSensor((OutSensor) sensor);
        }
    }

    public void removeSensor(String id) {
        Sensor rmSensor = null;
        for (Sensor sensor: signList) {
            if (sensor.getId().equals(id)) {
                rmSensor = sensor;
            }
        }
        if (rmSensor != null) {
            signList.remove(rmSensor);
            nb_Sensor--;
        }
    }


    public void updateData(String id, double data) {
        if (sensorData.containsKey(id)) {
            sensorData.get(id).add(new Data(data, Date.from(Instant.now())));
        } else {
            List<Data> dataList = new ArrayList<Data>();
            dataList.add(new Data(data, Date.from(Instant.now())));
            sensorData.put(id, dataList);
        }

        boolean alert = false;
        Sensor sensor = null;
        if (alertList.size() != 0) {
            for (Sensor s: signList)
                if (s.getId().equals(id))
                    sensor = s;

            if (sensor != null) {
                for (Alert a: alertList)
                    if (a.getSensorType().equals(sensor.getSensorType()))
                        if (data < a.getMin() || data > a.getMax()) {
                            alert = true;
                            sendMessage("ALERTE - " + id + " -> " + data);
                        }
            }
        }

        boolean isIn = false;
        for (VisualisationTabPanel tabPanel: openTabPanel)
            if (tabPanel.update(id, data, alert)) {
                tabbed_panel.setSelectedIndex(tabbed_panel.indexOfTab(tabPanel.getName()));
                isIn = true;
            }

        if (!isIn && alert) {
            if (sensor.isIn())
                openTab("Intérieur", "[, Intérieur]", true);
            else
                openTab("Extérieur", "[, Extérieur]", true);
        }
    }

    public void updateStatus(String newStatus) {
        status.setText(newStatus);
    }

    public void updateNbSensor(int nb) {
        nb_Sensor_label.setText("Nb capteurs : "+ nb);
    }

    public void updateNbAlert(int nb) {
        nb_Alert_label.setText("   Nb alertes : "+ nb);
    }


    public void setSignList(List<Sensor> signList) {
        this.signList = signList;
    }

    public void setAlertList(List<Alert> alertList) {
        this.alertList = alertList;
    }

    public void addToAlert(Alert alert) {
        this.alertList.add(alert);
    }

    public int getNbAlert() {
        return nbAlert;
    }

    public void openTab(String id, String path, boolean alert) {
        String[] tokens = path.substring(1, path.length() - 1).split(", ");

        if (!openPanel.contains(id) && tokens.length > 1) {
            Sensor sensor = null;
            for (InSensor inSensor : inSensors)
                if (id.equals(inSensor.getId()))
                    sensor = inSensor;
            for (OutSensor outSensor : outSensors)
                if (id.equals(outSensor.getId()))
                    sensor = outSensor;

            if (sensor != null) {
                if (sensorData.containsKey(id)) {
                    VisualisationGraphPanel graphPanel = new VisualisationGraphPanel(sensor, sensorData.get(id));
                    tabbed_panel.add(id, graphPanel);
                    openPanel.add(id);
                } else
                    sendMessage("Il n'y a aucun historique de valeur pour ce capteur.");
            } else {
                List<Sensor> sensorList = new ArrayList<>();
                List<Double> values = new ArrayList<>();

                if (tokens[1].equals("Extérieur"))
                    sensorList.addAll(outSensors);
                else {
                    List<InSensor> sensors = new ArrayList<>();
                    sensors.addAll(inSensors);
                    DefaultMutableTreeNode in = findNextNode(top, tokens[1]);

                    if (tokens.length > 2 && in != null) {
                        for (ListIterator<InSensor> it = sensors.listIterator(); it.hasNext();)
                            if (!it.next().getBuilding().equals(tokens[2]))
                                it.remove();

                        DefaultMutableTreeNode building = findNextNode(in, tokens[2]);

                        if (tokens.length > 3 && building != null) {
                            for (ListIterator<InSensor> it = sensors.listIterator(); it.hasNext();)
                                if (!it.next().getFloor().equals(tokens[3]))
                                    it.remove();

                            DefaultMutableTreeNode floor = findNextNode(building, tokens[3]);

                            if (tokens.length > 4 && floor != null) {
                                for (ListIterator<InSensor> it = sensors.listIterator(); it.hasNext();)
                                    if (!it.next().getRoom().equals(tokens[4]))
                                        it.remove();
                            }
                        }
                    }

                    sensorList.addAll(sensors);
                }

                for (Sensor s: sensorList) {
                    if (sensorData.containsKey(s.getId()))
                        values.add(sensorData.get(s.getId()).get(sensorData.get(s.getId()).size() - 1).getData());
                    else
                        values.add(5000.0);
                }

                if (!sensorList.isEmpty()) {
                    VisualisationTabPanel tabPanel = new VisualisationTabPanel(id, sensorList, values, alert);
                    JScrollPane scrollPane = new JScrollPane(tabPanel);
                    tabbed_panel.add(id, scrollPane);
                    openPanel.add(id);
                    openTabPanel.add(tabPanel);
                } else
                    sendMessage("Il n'y a aucun capteur à afficher");
            }
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        String path = e.getPath().toString();
        String last = e.getNewLeadSelectionPath().getLastPathComponent().toString();

        openTab(last, path, false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connection) {
            if (!server.isConnected()) {
                new ConnectionFrame(this, server);
                sendMessage("Tentative de connexion au serveur");
            } else {
                if (server.disconnect()) {
                    updateStatus("   Status : Déconnecté    ");
                    updateNbSensor(0);
                }
            }
        }

        if (e.getSource() == signIn) {
            if (server.getListInSensor().size() != 0 || server.getListOutSensor().size() != 0) {
                new ListFrame(this, server, signList);
            } else sendMessage("Il n'y a aucun capteur auquel s'inscrire ! ");
        }

        if (e.getSource() == close) {
            openPanel.remove(tabbed_panel.getTitleAt(tabbed_panel.getSelectedIndex()));
            VisualisationTabPanel tabPanel = null;
            for (VisualisationTabPanel panel: openTabPanel)
                if (panel.getName().equals(tabbed_panel.getTitleAt(tabbed_panel.getSelectedIndex())))
                    tabPanel = panel;
            if (tabPanel != null)
                openTabPanel.remove(tabPanel);
            tabbed_panel.remove(tabbed_panel.getSelectedComponent());
        }

        if (e.getSource() == closeAll) {
            openPanel.clear();
            openTabPanel.clear();
            tabbed_panel.removeAll();
        }

        if (e.getSource() == alert) {
            new AlertFrame(this, signList);
        }

        if (e.getSource() == closeAlert) {
            if (alertList.size() != 0) {
                new DeleteAlertFrame(this, alertList);
            } else sendMessage("Il n'y a aucune alerte à supprimer ! ");
        }
    }

}
