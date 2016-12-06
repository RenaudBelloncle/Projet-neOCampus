import Sensor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationMainFrame extends JFrame implements ActionListener {

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
    private JComboBox<Enum> type_box;

    private JPanel button_panel;
    private JButton button;
    private JLabel error;


    public SimulationMainFrame() {
        super("Création de capteur");
        setSize(500, 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initialiser();
        placer();
        setActionListener();
        setVisible(true);
    }

    public void initialiser() {
        main_panel = new JPanel();

        id_panel = new JPanel();
        id = new JLabel("Id : ");
        id_area = new JTextArea(1, 30);

        location_panel = new JPanel();
        location_bpanel = new JPanel();
        location_area_panel = new JPanel();
        location = new JLabel("Localisation : ");

        location_rbutton = new ButtonGroup();
        in = new JRadioButton("Intérieur ");
        out = new JRadioButton("Extérieur ");

        building = new JLabel("Bâtiment : ");
        floor = new JLabel("Étage : ");
        room = new JLabel("Salle : ");
        description = new JLabel("Complément : ");
        latitude = new JLabel("lat : ");
        longitude = new JLabel("long : ");
        out_longitude_area = new JTextArea(1,15);
        out_latitude_area = new JTextArea(1,15);
        in_building = new JTextArea(1,3);
        in_floor = new JTextArea(1,3);
        in_room = new JTextArea(1,3);
        in_description = new JTextArea(1,10);

        type_panel = new JPanel();
        type = new JLabel(" Type : ");
        type_box = new JComboBox<>();

        button_panel = new JPanel();
        button = new JButton("Créer capteur ");
        error = new JLabel("Certains champs sont incomplets !");
    }

    public void placer() {
        Container content = getContentPane();
        button_panel.add(button);
        button_panel.add(error);
        error.setVisible(false);

        type_panel.add(type);
        type_panel.add(type_box);

        location_bpanel.add(location);
        location_rbutton.add(in);
        location_rbutton.add(out);
        location_bpanel.add(in);
        location_bpanel.add(out);

        location_panel.setLayout(new GridLayout(2,1));
        location_panel.add(location_bpanel);
        location_panel.add(location_area_panel);

        location_area_panel.add(latitude);
        location_area_panel.add(out_latitude_area);
        location_area_panel.add(longitude);
        location_area_panel.add(out_longitude_area);
        location_area_panel.add(building);
        location_area_panel.add(in_building);
        location_area_panel.add(floor);
        location_area_panel.add(in_floor);
        location_area_panel.add(room);
        location_area_panel.add(in_room);
        location_area_panel.add(description);
        location_area_panel.add(in_description);

        setVisibleElement(false, false);

        id_panel.add(id);
        id_panel.add(id_area);

        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.add(id_panel);
        main_panel.add(location_panel);
        main_panel.add(type_panel);
        main_panel.add(button_panel);
        content.add(main_panel);
    }

    public void setActionListener() {
        in.addActionListener(this);
        out.addActionListener(this);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == in) {
            setVisibleElement(true, false);

        } else if (e.getSource() == out) {
            setVisibleElement(false, true);

        } else if (e.getSource() == button){
            if(in.isSelected()) {
                if (id_area.getText().isEmpty() || in_building.getText().isEmpty()
                        || in_floor.getText().isEmpty() || in_description.getText().isEmpty()
                        || in_room.getText().isEmpty()) error.setVisible(true);
                else {
                    error.setVisible(false);
                    Sensor sensor = new InSensor(id_area.getText(), in_building.getText(),
                            in_floor.getText(), in_room.getText(), in_description.getText(),
                            InType.values()[type_box.getSelectedIndex()]);
                    //SensorFrame sensor_frame = new SensorFrame(sensor);
                }
            } else {
                if (id_area.getText().isEmpty() || out_longitude_area.getText().isEmpty()
                        || out_latitude_area.getText().isEmpty() ) error.setVisible(true);
                else {
                    error.setVisible(false);
                    Sensor sensor = new OutSensor(id_area.getText(), out_latitude_area.getText(),
                            out_longitude_area.getText(), OutType.values()[type_box.getSelectedIndex()]);
                    //SensorFrame sensor_frame = new SensorFrame(sensor);
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
            type_box.setModel(new DefaultComboBoxModel<>(InType.values()));
        } else {
            type_box.setModel(new DefaultComboBoxModel<>(OutType.values()));
        }
        type_box.setVisible(true);
    }

    public static void main(String[] args) {
        new SimulationMainFrame();
    }
}
