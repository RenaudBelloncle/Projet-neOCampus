package VisualisationInterface;

import Sensor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class AlertFrame extends JFrame implements ActionListener{

    private JLabel sensorLabel;
    private JLabel minLabel;
    private JLabel maxLabel;

    private JTextField min;
    private JTextField max;
    private JComboBox<String> sensor;

    private JButton cancel;
    private JButton ok;

    private JPanel text_panel;
    private JPanel button_panel;
    private JPanel main_panel;

    private VisualisationFrame frame;
    private List<Sensor> signList = new ArrayList<>();

    public AlertFrame(VisualisationFrame frame, List<Sensor> signList){
        super("Création d'une alerte");
        this.frame = frame;
        this.signList = signList;

        setSize(500, 120);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initialize();
        place();
        setListener();
        setResizable(false);
        setVisible(true);
    }

    private void initialize() {
        main_panel = new JPanel();
        text_panel = new JPanel();
        button_panel = new JPanel();

        sensorLabel = new JLabel("Capteur : ");
        minLabel = new JLabel("Min : ");
        maxLabel = new JLabel("Max : ");

        sensor = new JComboBox<>();
        sensor.addItem(SensorType.ATMOSPHERICPRESSURE.toString());
        sensor.addItem(SensorType.BRIGHTNESS.toString());
        sensor.addItem(SensorType.COLDWATER.toString());
        sensor.addItem(SensorType.HOTWATER.toString());
        sensor.addItem(SensorType.HUMIDITY.toString());
        sensor.addItem(SensorType.LIGHTCONSUMPTION.toString());
        sensor.addItem(SensorType.SOUNDVOLUME.toString());
        sensor.addItem(SensorType.TEMPERATURE.toString());
        sensor.addItem(SensorType.WINDSPEED.toString());

        min = new JTextField(5);
        max = new JTextField(5);

        cancel = new JButton("Annuler");
        ok = new JButton("Ok");
    }

    private void place() {
        Container content = getContentPane();

        text_panel.add(sensorLabel);
        text_panel.add(sensor);
        text_panel.add(minLabel);
        text_panel.add(min);
        text_panel.add(maxLabel);
        text_panel.add(max);

        button_panel.add(cancel);
        button_panel.add(ok);

        main_panel.add(text_panel);
        main_panel.add(button_panel);

        content.add(main_panel);
    }

    private void setListener() {
        cancel.addActionListener(this);
        ok.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            this.dispose();
        } else if (e.getSource() == ok) {
            try {
                frame.addToAlert(new Alert("Alerte"+frame.getNbAlert(), SensorType.STRINGTOTYPE(sensor.getItemAt(sensor.getSelectedIndex())),
                        Double.parseDouble(min.getText().replace(",", ".")), Double.parseDouble(max.getText().replace(",", "."))));
                frame.updateNbAlert(frame.getNbAlert()+1);
                frame.sendMessage("Création d'une alerte sur " + SensorType.STRINGTOTYPE(sensor.getItemAt(sensor.getSelectedIndex())));
                this.dispose();
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(this, "La valeur entrée n'est pas valide !");
            }
        }
    }
}
