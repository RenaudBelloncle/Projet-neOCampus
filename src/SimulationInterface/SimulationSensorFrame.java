package SimulationInterface;

import Sensor.InSensor;
import Sensor.OutSensor;
import Sensor.Sensor;
import Sensor.SensorType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Sensor.SensorType.*;

public class SimulationSensorFrame extends JFrame implements ActionListener {

    private JPanel main_panel;

    private JPanel id_panel;
    private JLabel id;
    private JTextArea id_area;

    private JPanel location_panel;
    private JPanel location_bpanel;
    private JLabel location;
    private ButtonGroup location_rbutton;
    private JRadioButton in;
    private JRadioButton out;

    private JPanel location_area_panel;
    private JPanel location_description_panel;
    private JTextArea out_latitude_area;
    private JTextArea out_longitude_area;
    private JComboBox<String> in_building;
    private JComboBox<String> in_floor;
    private JComboBox<String> in_room;
    private JTextArea in_description;
    private JLabel building;
    private JLabel floor;
    private JLabel room;
    private JLabel description;
    private JLabel latitude;
    private JLabel longitude;

    private JPanel type_panel;
    private JLabel type;
    private JComboBox<SensorType> type_box;

    private JPanel connection_Panel;
    private JLabel ip_Label;
    private JTextArea ip1_area;
    private JTextArea ip2_area;
    private JTextArea ip3_area;
    private JTextArea ip4_area;
    private JLabel port_Label;
    private JTextArea port_area;

    private JPanel button_panel;
    private JButton button;

    private List<String> building_List = new ArrayList<>();
    private List<String> floor_List = new ArrayList<>();
    private List<String> room_List = new ArrayList<>();

    public SimulationSensorFrame() {
        super("Création et connexion du capteur");
        setSize(400, 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        readConfigFile();
        initialize();
        place();
        setActionListener();
        setResizable(false);
        setVisible(true);
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

    private void initialize() {
        main_panel = new JPanel();

        id_panel = new JPanel();
        id = new JLabel("Id : ");
        id_area = new JTextArea(1, 20);

        location_panel = new JPanel();
        location_bpanel = new JPanel();
        location_area_panel = new JPanel();
        location_description_panel = new JPanel();
        location = new JLabel("Localisation : ");

        location_rbutton = new ButtonGroup();
        in = new JRadioButton("Intérieur ");
        out = new JRadioButton("Extérieur ");

        building = new JLabel("Bâtiment : ");
        floor = new JLabel("Étage : ");
        room = new JLabel("Salle : ");
        description = new JLabel("Position relative : ");
        latitude = new JLabel("Latitude : ");
        longitude = new JLabel("Longitude : ");
        out_longitude_area = new JTextArea(1,15);
        out_latitude_area = new JTextArea(1,15);
        in_building = new JComboBox<>();
        in_building.addItem("-");
        for (String building : building_List) {
            in_building.addItem(building);
        }
        in_floor = new JComboBox<>();
        in_floor.addItem("-");
        for (String floor : floor_List) {
            in_floor.addItem(floor);
        }
        in_room = new JComboBox<>();
        in_room.addItem("-");
        for (String room : room_List) {
            in_room.addItem(room);
        }
        in_description = new JTextArea(1,20);

        type_panel = new JPanel();
        type = new JLabel(" Capteur : ");
        type_box = new JComboBox<>();

        connection_Panel = new JPanel();
        ip_Label = new JLabel(" Ip : ");
        port_Label = new JLabel(" Port : ");
        ip1_area = new JTextArea(1,3);
        ip2_area = new JTextArea(1,3);
        ip3_area = new JTextArea(1,3);
        ip4_area = new JTextArea(1,3);
        port_area = new JTextArea(1, 10);

        button_panel = new JPanel();
        button = new JButton("Créer et Connecter");
        button.setEnabled(false);
    }

    private void place() {
        Container content = getContentPane();
        button_panel.add(button);

        connection_Panel.add(ip_Label);
        connection_Panel.add(ip1_area);
        connection_Panel.add(new JLabel("."));
        connection_Panel.add(ip2_area);
        connection_Panel.add(new JLabel("."));
        connection_Panel.add(ip3_area);
        connection_Panel.add(new JLabel("."));
        connection_Panel.add(ip4_area);
        connection_Panel.add(port_Label);
        connection_Panel.add(port_area);

        type_panel.add(type);
        type_panel.add(type_box);

        location_bpanel.add(location);
        location_rbutton.add(in);
        location_rbutton.add(out);
        location_bpanel.add(in);
        location_bpanel.add(out);

        location_panel.setLayout(new GridLayout(3,1));
        location_panel.add(location_bpanel);
        location_panel.add(location_area_panel);
        location_panel.add(location_description_panel);

        location_area_panel.add(latitude);
        location_area_panel.add(out_latitude_area);
        location_description_panel.add(longitude);
        location_description_panel.add(out_longitude_area);

        location_area_panel.add(building);
        location_area_panel.add(in_building);
        location_area_panel.add(floor);
        location_area_panel.add(in_floor);
        location_area_panel.add(room);
        location_area_panel.add(in_room);
        location_description_panel.add(description);
        location_description_panel.add(in_description);

        setVisibleElement(false, false);

        id_panel.add(id);
        id_panel.add(id_area);

        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.add(id_panel);
        main_panel.add(location_panel);
        main_panel.add(type_panel);
        main_panel.add(connection_Panel);
        main_panel.add(button_panel);
        content.add(main_panel);
    }

    private void setActionListener() {
        in.addActionListener(this);
        out.addActionListener(this);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == in) {
            setVisibleElement(true, false);
            button.setEnabled(true);
        } else if (e.getSource() == out) {
            setVisibleElement(false, true);
            button.setEnabled(true);
        } else if (e.getSource() == button) {
            if(in.isSelected()) {
                if (id_area.getText().isEmpty() || in_building.getSelectedIndex() == 0
                        || in_floor.getSelectedIndex() == 0 || in_room.getSelectedIndex() == 0
                        || ip1_area.getText().isEmpty() || ip2_area.getText().isEmpty()
                        || ip3_area.getText().isEmpty() || ip4_area.getText().isEmpty()
                        || port_area.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Certains champs sont incomplets !");
                } else {
                    try {
                        int ip1 = Integer.parseInt(ip1_area.getText());
                        int ip2 = Integer.parseInt(ip2_area.getText());
                        int ip3 = Integer.parseInt(ip3_area.getText());
                        int ip4 = Integer.parseInt(ip4_area.getText());
                        int port = Integer.parseInt(port_area.getText());
                        if (ip1 >= 0 && ip1 <= 255 && ip2 >= 0 && ip2 <= 255 && ip3 >= 0 && ip3 <= 255
                                && ip4 >= 0 && ip4 <= 255 && port > 0) {
                            String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
                            connection(new InSensor(id_area.getText(), (SensorType) type_box.getSelectedItem(),
                                    building_List.get(in_building.getSelectedIndex()-1),
                                    floor_List.get(in_floor.getSelectedIndex()-1),
                                    room_List.get(in_floor.getSelectedIndex()-1),
                                    in_description.getText(), ip, port));
                        }
                    } catch (NumberFormatException ignored) {
                        JOptionPane.showMessageDialog(this, "Certains champs sont invalides !");
                    }
                }
            } else {
                if (id_area.getText().isEmpty() || out_longitude_area.getText().isEmpty()
                        || out_latitude_area.getText().isEmpty() || ip1_area.getText().isEmpty()
                        || ip2_area.getText().isEmpty() || ip3_area.getText().isEmpty()
                        || ip4_area.getText().isEmpty() || port_area.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Certains champs sont incomplets !");
                } else {
                    try {
                        double latitude = Double.parseDouble(out_latitude_area.getText());
                        double longitude = Double.parseDouble(out_longitude_area.getText());
                        int ip1 = Integer.parseInt(ip1_area.getText());
                        int ip2 = Integer.parseInt(ip2_area.getText());
                        int ip3 = Integer.parseInt(ip3_area.getText());
                        int ip4 = Integer.parseInt(ip4_area.getText());
                        int port = Integer.parseInt(port_area.getText());
                        if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180
                                && ip1 >= 0 && ip1 <= 255 && ip2 >= 0 && ip2 <= 255 && ip3 >= 0 && ip3 <= 255
                                && ip4 >= 0 && ip4 <= 255 && port > 0) {
                            String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
                            connection(new OutSensor(id_area.getText(), (SensorType) type_box.getSelectedItem(),
                                    out_latitude_area.getText(), out_longitude_area.getText(), ip, port));
                        } else {
                            JOptionPane.showMessageDialog(this, "Certains champs sont invalides !");
                        }
                    } catch (NumberFormatException ignored) {
                        JOptionPane.showMessageDialog(this, "Certains champs sont invalides !");
                    }
                }
            }
        }
    }

    private void setVisibleElement(boolean in, boolean out) {
        setComboboxItems(in);
        type.setVisible(in);
        type_box.setVisible(in);
        if (!in) {
            type.setVisible(out);
            type_box.setVisible(out);
        }
        description.setVisible(in);
        out_longitude_area.setVisible(out);
        out_latitude_area.setVisible(out);
        in_building.setVisible(in);
        in_floor.setVisible(in);
        in_room.setVisible(in);
        in_description.setVisible(in);
        longitude.setVisible(out);
        latitude.setVisible(out);
        building.setVisible(in);
        floor.setVisible(in);
        room.setVisible(in);
    }

    private void setComboboxItems(boolean in) {
        type_box.removeAllItems();
        if (in) {
            type_box.addItem(TEMPERATURE);
            type_box.addItem(HUMIDITY);
            type_box.addItem(BRIGHTNESS);
            type_box.addItem(SOUNDVOLUME);
            type_box.addItem(LIGHTCONSUMPTION);
            type_box.addItem(COLDWATER);
            type_box.addItem(HOTWATER);
        } else {
            type_box.addItem(TEMPERATURE);
            type_box.addItem(HUMIDITY);
            type_box.addItem(BRIGHTNESS);
            type_box.addItem(WINDSPEED);
            type_box.addItem(ATMOSPHERICPRESSURE);
        }
        type_box.setVisible(true);
    }

    private void connection(Sensor sensor) {
        try {
            if (sensor.connection()) {
                JOptionPane.showMessageDialog(this, "Connexion du capteur " + sensor.toString() + " réussie.");
                new SimulationValueFrame(sensor);
            } else {
                JOptionPane.showMessageDialog(this, "Connexion du capteur " + sensor.toString() + " échouée.");
            }
        } catch (IOException ignored) {
            JOptionPane.showMessageDialog(this, "Connexion du capteur " + sensor.toString()
                    + " échouée.\nAucune réponse du serveur !");
        }
    }
}
