package SimulationInterface;

import Sensor.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationSensorFrame extends JFrame implements ActionListener{

    private Sensor sensor;

    public SimulationSensorFrame(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
