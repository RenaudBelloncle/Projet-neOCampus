package SimulationInterface;

import Sensor.InSensor;
import Sensor.OutSensor;
import Sensor.Sensor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class SimulationValueFrame implements ActionListener {
    private Sensor sensor;

    private JFrame frame;
    private JPanel main_Panel;

    private JPanel sensor_id_Panel;
    private JLabel id;
    private JPanel sensor_type_Panel;
    private JLabel sensorType;
    private JPanel sensor_data_Panel;
    private JLabel building;
    private JLabel floor;
    private JLabel room;
    private JLabel latitude;
    private JPanel sensor_description_Panel;
    private JLabel description;
    private JLabel longitude;

    private JSeparator separator1;

    private JPanel sensorType_Panel;
    private JLabel interval;
    private JPanel sensorType_precision_Panel;
    private JLabel precision;
    private JPanel sensorType_margin_Panel;
    private JLabel margin;

    private JSeparator separator2;

    private JPanel data_Panel;
    private JPanel data_bPanel;
    private JLabel data;
    private ButtonGroup data_rbutton;
    private JRadioButton fixed;
    private JRadioButton random;
    private JPanel data_area_Panel;
    private JLabel value;
    private JTextArea value_area;
    private JLabel value_unit;

    private JPanel frequency_Panel;
    private JLabel frequency;
    private JTextArea frequency_area;
    private JLabel frequency_unit;

    private JPanel button_Panel;
    private JButton button;
    private boolean isSendingData = false;

    public SimulationValueFrame(Sensor sensor) {
        this.sensor = sensor;
        frame = new JFrame("Sélection de la valeur et envoie des données");
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeOperation();
            }
        });
        initialize();
        place();
        setActionListener();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initialize() {
        main_Panel = new JPanel();

        sensor_id_Panel = new JPanel();
        id = new JLabel("Id : " + sensor.toString());
        sensor_type_Panel = new JPanel();
        sensorType = new JLabel("Type de capteur : " + sensor.getSensorType().toString());
        sensor_data_Panel = new JPanel();
        sensor_description_Panel = new JPanel();
        if (sensor.isIn()) {
            building = new JLabel("Bâtiment : " + ((InSensor) sensor).getBuilding() + " ");
            floor = new JLabel("Étage : " + ((InSensor) sensor).getFloor() + " ");
            room = new JLabel("Salle : " + ((InSensor) sensor).getRoom());
            description = new JLabel("Position relative : " + ((InSensor) sensor).getDescription());
            latitude = new JLabel();
            longitude = new JLabel();
        } else {
            building = new JLabel();
            floor = new JLabel();
            room = new JLabel();
            description = new JLabel();
            latitude = new JLabel("Latitude : " + ((OutSensor) sensor).getLatitude());
            longitude = new JLabel("Longitude : " + ((OutSensor) sensor).getLongitude());
        }

        separator1 = new JSeparator();

        sensorType_Panel = new JPanel();
        interval = new JLabel("Intervalle des données : [" + sensor.getSensorType().getInterval()[0]
                + "," + sensor.getSensorType().getInterval()[1] + "] " + sensor.getSensorType().getShort_unit());
        sensorType_precision_Panel = new JPanel();
        precision = new JLabel("Précision des données : " + sensor.getSensorType().getPrecision()
                + "" + sensor.getSensorType().getShort_unit());
        sensorType_margin_Panel = new JPanel();
        margin = new JLabel("Marge d'erreur des données : " + sensor.getSensorType().getMargin()
                + "" + sensor.getSensorType().getShort_unit());

        separator2 = new JSeparator();

        data_Panel = new JPanel();
        data_bPanel = new JPanel();
        data = new JLabel("Données : ");
        data_rbutton = new ButtonGroup();
        fixed = new JRadioButton("Fixe ");
        random = new JRadioButton("Aléatoire ");
        data_area_Panel = new JPanel();
        value = new JLabel("Valeur : ");
        value_area = new JTextArea(1, 5);
        value_area.insert("" + (sensor.getSensorType().getInterval()[0]
                + sensor.getSensorType().getInterval()[1])/2, 0);
        value_unit = new JLabel(sensor.getSensorType().getShort_unit());

        frequency_Panel = new JPanel();
        frequency = new JLabel("Fréquence d'envoi : ");
        frequency_area = new JTextArea(1, 5);
        frequency_area.insert("" + sensor.getSensorType().getFrequency(), 0);
        frequency_unit = new JLabel("s");

        button_Panel = new JPanel();
        button = new JButton("Envoyer les données");
        button.setEnabled(false);
    }

    private void place() {
        Container content = frame.getContentPane();
        main_Panel.setLayout(new BoxLayout(main_Panel, BoxLayout.Y_AXIS));

        sensor_id_Panel.add(id);
        sensor_type_Panel.add(sensorType);
        sensor_data_Panel.add(building);
        sensor_data_Panel.add(floor);
        sensor_data_Panel.add(room);
        sensor_data_Panel.add(latitude);
        sensor_description_Panel.add(description);
        sensor_description_Panel.add(longitude);

        sensorType_Panel.add(interval);
        sensorType_precision_Panel.add(precision);
        sensorType_margin_Panel.add(margin);

        data_Panel.setLayout(new GridLayout(2,1));
        data_bPanel.add(data);
        data_rbutton.add(fixed);
        data_rbutton.add(random);
        data_bPanel.add(fixed);
        data_bPanel.add(random);
        data_Panel.add(data_bPanel);
        data_area_Panel.add(value);
        data_area_Panel.add(value_area);
        data_area_Panel.add(value_unit);
        data_Panel.add(data_area_Panel);

        frequency_Panel.add(frequency);
        frequency_Panel.add(frequency_area);
        frequency_Panel.add(frequency_unit);

        button_Panel.add(button);

        main_Panel.add(sensor_id_Panel);
        main_Panel.add(sensor_type_Panel);
        main_Panel.add(sensor_data_Panel);
        main_Panel.add(sensor_description_Panel);
        main_Panel.add(separator1);
        main_Panel.add(sensorType_Panel);
        main_Panel.add(sensorType_precision_Panel);
        main_Panel.add(sensorType_margin_Panel);
        main_Panel.add(separator2);
        main_Panel.add(data_Panel);
        main_Panel.add(frequency_Panel);
        main_Panel.add(button_Panel);

        content.add(main_Panel);

        setVisible();
    }

    private void setActionListener() {
        fixed.addActionListener(this);
        random.addActionListener(this);
        button.addActionListener(this);
    }

    private void setVisible() {
        value.setVisible(fixed.isSelected());
        value_area.setVisible(fixed.isSelected());
        value_unit.setVisible(fixed.isSelected());
    }

    private void closeOperation() {
        if (isSendingData) {
            sensor.stopSendingData();
            isSendingData = false;
            button.setText("Envoyer les données");
        }
        try {
            if (sensor.disconnection()) {
                JOptionPane.showMessageDialog(frame, "Déconnexion du capteur " + sensor.toString() + " réussie.");
                frame.setVisible(false);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Déconnexion du capteur " + sensor.toString() + " échouée.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Déconnexion du capteur " + sensor.toString() + " échouée.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fixed || e.getSource() == random) {
            setVisible();
            button.setEnabled(true);
        } else if (e.getSource() == button) {
            if (!isSendingData) {
                if (fixed.isSelected()) {
                    try {
                        double val = Double.parseDouble(value_area.getText());
                        long freq = Long.parseLong(frequency_area.getText());
                        if (val >= sensor.getSensorType().getInterval()[0]
                                && val <= sensor.getSensorType().getInterval()[1]) {
                            sensor.sendData(val, freq);
                            isSendingData = true;
                            button.setText("Arrêt de l'envoi");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Certains champs sont invalides !");
                        }
                    } catch (NumberFormatException ignored) {
                        JOptionPane.showMessageDialog(frame, "Certains champs sont invalides !");
                    }
                } else {
                    try {
                        long freq = Long.parseLong(frequency_area.getText());
                        sensor.sendRandomData(freq);
                        isSendingData = true;
                        button.setText("Arrêt de l'envoi");
                    } catch (NumberFormatException ignored) {
                        JOptionPane.showMessageDialog(frame, "Certains champs sont invalides !");
                    }
                }
            } else {
                sensor.stopSendingData();
                isSendingData = false;
                button.setText("Envoyer les données");
            }
        }
    }
}
