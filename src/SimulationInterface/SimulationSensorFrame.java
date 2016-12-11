package SimulationInterface;

import Sensor.InSensor;
import Sensor.OutSensor;
import Sensor.Sensor;
import Sensor.SensorType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
    private JTextArea in_building;
    private JTextArea in_floor;
    private JTextArea in_room;
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

    private JPanel panel_con;
    private JLabel label_ip;
    private JTextArea ip_1;
    private JTextArea ip_2;
    private JTextArea ip_3;
    private JTextArea ip_4;
    private JLabel label_port;
    private JTextArea port;

    private JPanel button_panel;
    private JButton button;

    public SimulationSensorFrame() {
        super("Création et connexion du capteur");
        setSize(400, 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initialize();
        place();
        setActionListener();
        setResizable(false);
        setVisible(true);
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
        in_building = new JTextArea(1,3);
        in_floor = new JTextArea(1,3);
        in_room = new JTextArea(1,3);
        in_description = new JTextArea(1,20);

        type_panel = new JPanel();
        type = new JLabel(" Capteur : ");
        type_box = new JComboBox<>();

        panel_con = new JPanel();
        label_ip = new JLabel(" Ip : ");
        label_port = new JLabel(" Port : ");
        ip_1 = new JTextArea(1,2);
        ip_2 = new JTextArea(1,2);
        ip_3 = new JTextArea(1,2);
        ip_4 = new JTextArea(1,2);
        port = new JTextArea(1, 10);

        button_panel = new JPanel();
        button = new JButton("Créer et Connecter");
        button.setEnabled(false);
    }

    private void place() {
        Container content = getContentPane();
        button_panel.add(button);

        panel_con.add(label_ip);
        panel_con.add(ip_1);
        panel_con.add(new JLabel("."));
        panel_con.add(ip_2);
        panel_con.add(new JLabel("."));
        panel_con.add(ip_3);
        panel_con.add(new JLabel("."));
        panel_con.add(ip_4);
        panel_con.add(label_port);
        panel_con.add(port);

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
        main_panel.add(panel_con);
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
                if (id_area.getText().isEmpty() || in_building.getText().isEmpty()
                        || in_floor.getText().isEmpty() || in_room.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Certains champs sont incomplets !");
                } else {
                    connection(new InSensor(id_area.getText(), (SensorType) type_box.getSelectedItem(),
                            in_building.getText(), in_floor.getText(), in_room.getText(), in_description.getText()));
                }
            } else {
                if (id_area.getText().isEmpty() || out_longitude_area.getText().isEmpty()
                        || out_latitude_area.getText().isEmpty() ) {
                    JOptionPane.showMessageDialog(this, "Certains champs sont incomplets !");
                } else {
                    try {
                        double latitude = Double.parseDouble(out_latitude_area.getText());
                        double longitude = Double.parseDouble(out_longitude_area.getText());
                        if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
                            connection(new OutSensor(id_area.getText(), (SensorType) type_box.getSelectedItem(),
                                    out_latitude_area.getText(), out_longitude_area.getText()));
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
