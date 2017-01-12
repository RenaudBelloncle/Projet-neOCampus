package VisualisationInterface;


import Sensor.InSensor;
import Sensor.OutSensor;
import Sensor.Sensor;

import javax.swing.*;
import java.util.List;

public class VisualisationTabPanel extends JPanel {

    public VisualisationTabPanel(VisualisationFrame frame, List<Sensor> sensors, List<Double> values){

        if(sensors.isEmpty() || values.isEmpty() || sensors.size() != values.size()) {
            frame.sendErrorMessage("Erreur dans la liste des capteurs.");
            return;
        }

        boolean inside = sensors.get(0).isIn();

        String[][] table;
        String[] title;
        Sensor temp;

        if(inside) {
            title = new String[]{"Id", "Type de capteur", "Batiment", "Étage", "Salle", "Description","Valeur"};
            table =new String[sensors.size()][7];
            for(int i=0;i<sensors.size();i++) {
                temp = sensors.get(i);
                table[i][0] = temp.getId();
                table[i][1] = temp.getSensorType().getType();
                table[i][2] = ((InSensor) temp).getBuilding();
                table[i][3] = ((InSensor) temp).getFloor();
                table[i][4] = ((InSensor) temp).getRoom();
                table[i][5] = ((InSensor) temp).getDescription();
                table[i][6] = (""+values.get(i));

            }
        } else {
            title = new String[]{"Id", "Type de capteur", "Longitude","Latitude","Valeur"};
            table =new String[sensors.size()][5];
            for(int i=0;i<sensors.size();i++) {
                temp = sensors.get(i);
                table[i][0] = temp.getId();
                table[i][1] = temp.getSensorType().getType();
                table[i][2] = ((OutSensor) temp).getLongitude();
                table[i][3] = ((OutSensor) temp).getLatitude();
                table[i][4] = (""+values.get(i));
            }
        }

        JTable sensors_array = new JTable(table, title);
        sensors_array.setDefaultEditor(Object.class,null);
        sensors_array.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sensors_array.getColumnModel().getColumn(0).setPreferredWidth(100);
        sensors_array.getColumnModel().getColumn(1).setPreferredWidth(200);

        if(inside)
            sensors_array.getColumnModel().getColumn(5).setPreferredWidth(200);

        sensors_array.setPreferredScrollableViewportSize(sensors_array.getPreferredSize());
        sensors_array.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(sensors_array);
        add(scrollPane);
    }
}
