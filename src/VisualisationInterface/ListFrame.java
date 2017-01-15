package VisualisationInterface;

import Sensor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ListFrame extends JFrame implements ActionListener{

    private VisualisationServer server;
    private VisualisationFrame frame;

    private List<InSensor> inList = new ArrayList<>();
    private List<OutSensor> outList = new ArrayList<>();
    private List<Sensor> signList = new ArrayList<>();
    private List<Sensor> newSignList = new ArrayList<>();
    private List<String> signInSensor = new ArrayList<>();
    private List<String> signOutSensor = new ArrayList<>();

    private JScrollPane scrollPane = new JScrollPane();

    private JCheckBox[] boxTab;

    private JPanel listPanel;
    private JPanel buttonPanel;
    private JPanel mainPanel;

    private JButton ok;
    private JButton cancel;

    public ListFrame( VisualisationFrame frame, VisualisationServer server, List<Sensor> signList){
        super("Connexion au serveur");
        this.frame = frame;
        this.server= server;
        this.inList = server.getListInSensor();
        this.outList = server.getListOutSensor();
        this.signList = signList;

        setSize(300, 250);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initialize();
        setListener();
        setResizable(false);
        setVisible(true);
    }

    private void initialize() {
        Container content = getContentPane();
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        boxTab = new JCheckBox[inList.size()+outList.size()];

        for (int i = 0; i < inList.size(); i++) {
            boxTab[i] = new JCheckBox(inList.get(i).getId()+", intérieur, ");
            if (signList.contains(inList.get(i))) boxTab[i].setSelected(true);
            listPanel.add(new JPanel().add(boxTab[i]));
        }
        for (int i = inList.size(); i < outList.size()+inList.size(); i++) {
            boxTab[i] = new JCheckBox(outList.get(i-inList.size()).getId()+", extérieur");
            if (signList.contains(outList.get(i-inList.size()))) boxTab[i].setSelected(true);
            listPanel.add(new JPanel().add(boxTab[i]));
        }

        scrollPane = new JScrollPane(listPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        ok = new JButton("Ok");
        cancel = new JButton("Annuler");
        buttonPanel = new JPanel();
        buttonPanel.add(cancel);
        buttonPanel.add(ok);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        content.add(mainPanel);
    }

    private void setListener() {
        cancel.addActionListener(this);
        ok.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == cancel) dispose();
        if (e.getSource() == ok) {
            for (int i = 0; i < boxTab.length; i++) {
                if (boxTab[i].isSelected()) {
                    if (i < inList.size()) newSignList.add(inList.get(i));
                    else newSignList.add(outList.get(i-inList.size()));
                }
            }
            for (int i = 0; i < newSignList.size(); i++) {
                if(!signList.contains(newSignList.get(i)))signInSensor.add(newSignList.get(i).getId());
            }
            for (int i = 0; i < signList.size(); i++) {
                if(!newSignList.contains(signList.get(i)))signOutSensor.add(signList.get(i).getId());
            }
            frame.setSignList(newSignList);
            server.signInSensors(signInSensor);
            server.signOutSensors(signOutSensor);
            frame.updateNbSensor(newSignList.size());
            dispose();
        }
    }
}
