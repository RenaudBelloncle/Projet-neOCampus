package SimulationInterface;

import Sensor.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationValueFrame extends JFrame implements ActionListener{

    private Sensor sensor;

    public SimulationValueFrame(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
