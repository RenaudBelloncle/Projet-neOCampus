package VisualisationInterface;


import Sensor.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class VisualisationTabPanel extends JPanel{


    private JTable sensors_array;
    private JScrollPane scrollPane;
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

        scrollPane=new JScrollPane(sensors_array);
        add(scrollPane);

    }


    public static void main(String[] args){
        JFrame fen=new JFrame();
        List<Sensor> liste=new ArrayList<Sensor>();
        List<Double> val=new ArrayList<>();
        fen.setSize(1200,900);
        /*
        Sensor sen=new OutSensor("toast", SensorType.ATMOSPHERICPRESSURE,"32.000","24.5000","127.46.33.2",5321);
        Sensor blblbl=new OutSensor("tast", SensorType.ATMOSPHERICPRESSURE,"2.000","24.5000","127.46.33.1",51);
        Sensor fp=new OutSensor("dbvcdbsfb", SensorType.ATMOSPHERICPRESSURE,"2.000","24.5000","127.46.33.1",51);
        liste.add(sen);
        liste.add(blblbl);
        liste.add(fp);
        */

        Sensor sen1=new InSensor("capteur1",SensorType.LIGHTCONSUMPTION,"U1","2","204","mid","127.0.0.1",513);
        Sensor sen2=new InSensor("capteur2",SensorType.LIGHTCONSUMPTION,"U1","2","205","mid","127.0.0.1",513);
        Sensor sen3=new InSensor("capteur3",SensorType.ATMOSPHERICPRESSURE,"U1","2","208","cote paralelees d fdvsdqfv","127.0.0.1",513);
        liste.add(sen1);
        liste.add(sen2);
        liste.add(sen3);
        val.add(12.);
        val.add(2.23);
        val.add(0.003);
        fen.setLayout(new BorderLayout());
        fen.add(new VisualisationTabPanel(liste,val));
        fen.setVisible(true);
        fen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
