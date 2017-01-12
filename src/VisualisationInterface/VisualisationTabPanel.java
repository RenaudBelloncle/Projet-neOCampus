package VisualisationInterface;


import Sensor.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class VisualisationTabPanel extends JPanel{


    private JTable sensors_array;
    private boolean inside;
    private String[][] table;
    private String title[];
    private Sensor temp=null;


    public VisualisationTabPanel(List<Sensor> sensors, List<Double> values){

        if(sensors.isEmpty() || values.isEmpty() || sensors.size()!=values.size()){
            System.out.print("Erreur : une des listes est vides ou elles ne sont pas de meme tailles");
            exit(1);
        }

        inside= sensors.get(0).isIn();


        if(inside){
            title= new String[]{"Id", "Type de capteur", "Batiment", "Étage", "Salle", "Description", "Ip", "Port","Valeur"};
            table=new String[sensors.size()][9];
            for(int i=0;i<sensors.size();i++) {
                temp = sensors.get(i);
                table[i][0] = temp.getId();
                table[i][1] = temp.getSensorType().getType();
                table[i][2] = ((InSensor) temp).getBuilding();
                table[i][3] = ((InSensor) temp).getFloor();
                table[i][4] = ((InSensor) temp).getRoom();
                table[i][5] = ((InSensor) temp).getDescription();
                table[i][6] = temp.getIp();
                table[i][7] = ("" + temp.getPort());
                table[i][8] = (""+values.get(i));

            }
        }else {
            title= new String[]{"Id", "Type de capteur", "Longitude","Latitude", "Ip", "Port","Valeur"};
            table=new String[sensors.size()][7];
            for(int i=0;i<sensors.size();i++) {
                temp = sensors.get(i);
                table[i][0] = temp.getId();
                table[i][1] = temp.getSensorType().getType();
                table[i][2] = ((OutSensor) temp).getLongitude();
                table[i][3] = ((OutSensor) temp).getLatitude();
                table[i][4] = temp.getIp();
                table[i][5] = ("" + temp.getPort());
                table[i][6] = (""+values.get(i));
            }
        }
        sensors_array=new JTable(table,title);
        sensors_array.setDefaultEditor(Object.class,null);

        sensors_array.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sensors_array.getColumnModel().getColumn(0).setPreferredWidth(100);
        sensors_array.getColumnModel().getColumn(1).setPreferredWidth(200);

        if(inside){
            //sensors_array.getColumnModel().getColumn(2).setPreferredWidth(70);
            //sensors_array.getColumnModel().getColumn(3).setPreferredWidth(70);
            //sensors_array.getColumnModel().getColumn(4).setPreferredWidth(70);
            sensors_array.getColumnModel().getColumn(5).setPreferredWidth(200);
            sensors_array.getColumnModel().getColumn(6).setPreferredWidth(100);
            //sensors_array.getColumnModel().getColumn(7).setPreferredWidth(70);
            //sensors_array.getColumnModel().getColumn(8).setPreferredWidth(70);
        }else{
            sensors_array.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        sensors_array.setPreferredScrollableViewportSize(sensors_array.getPreferredSize());
        sensors_array.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(sensors_array);
        add(scrollPane);

    }
}
