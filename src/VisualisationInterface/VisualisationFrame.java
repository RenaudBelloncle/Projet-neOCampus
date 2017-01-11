package VisualisationInterface;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualisationFrame extends JFrame implements TreeSelectionListener, ActionListener {

    private JTabbedPane tabbed_panel;
    private JScrollPane scroll_panel;
    private JSplitPane main_split_panel;
    private JSplitPane split_panel;

    private JTextArea dialog_area;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenu menu_data;
    private JMenuItem connection;
    private JMenuItem refresh;
    private JMenuItem signIn;
    private JMenuItem signOut;

    private JTree tree = new JTree();

    private int nb_Sensor = 0;
    private JLabel status;
    private JLabel nb_Sensor_label;

    public VisualisationFrame(){
        super("Visualisation des capteurs");
        setSize(850, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initialize();
        place();
        setListener();
        setResizable(false);
        setVisible(true);
    }

    private void initialize() {
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menu_data = new JMenu("Données");
        signIn = new JMenuItem("Inscription aux capteurs");
        refresh = new JMenuItem("Actualiser");
        signOut = new JMenuItem("Désinscription aux capteurs");
        connection = new JMenuItem("Connexion au serveur");
        status = new JLabel("   Status : Déconnecté    ");
        nb_Sensor_label = new JLabel("Nb capteurs : "+nb_Sensor);
        tabbed_panel = new JTabbedPane(SwingConstants.TOP);
        scroll_panel = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        split_panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll_panel, tabbed_panel);
        dialog_area = new JTextArea(100, 20);
        main_split_panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dialog_area, split_panel);
    }

    private void place() {
        Container content = getContentPane();
        menu.add(connection);
        menu.add(signIn);
        menu.add(signOut);
        menuBar.add(menu);
        menu_data.add(refresh);
        menuBar.add(menu_data);
        menuBar.add(status);
        menuBar.add(nb_Sensor_label);
        setJMenuBar(menuBar);
        content.add(main_split_panel);
    }

    public void switchText() {
        if (connection.getText() == "Connexion au serveur") {
            connection.setText("Déconnexion du serveur");
            status.setText("   Status : Connecté      ");
        }
        else {
            connection.setText("Connexion au serveur");
            status.setText("   Status : Déconnecté    ");
        }
    }

    private void setListener() {
        tree.addTreeSelectionListener(this);
        signOut.addActionListener(this);
        signIn.addActionListener(this);
        connection.addActionListener(this);
        refresh.addActionListener(this);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        String node = e.getNewLeadSelectionPath().getLastPathComponent().toString();
        if (node == "") {
        } else {
            tabbed_panel.add(node, new JPanel());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connection) {
            if (connection.getText() == "Connexion au serveur") new ConnectionFrame();
            else {

            }
            switchText(); // à placer ailleurs
        }
        if (e.getSource() == signIn) { // gestion erreur si déjà inscrit à tous les capteurs ?
            nb_Sensor++;
            nb_Sensor_label.setText("Nb capteurs : "+nb_Sensor);
        }
        if (e.getSource() == signOut) {
            if (nb_Sensor > 0) {
                nb_Sensor--;
                nb_Sensor_label.setText("Nb capteurs : "+nb_Sensor);
            } // sinon erreur
        }
    }
}
