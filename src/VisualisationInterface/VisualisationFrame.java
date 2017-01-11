package VisualisationInterface;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualisationFrame extends JFrame implements TreeSelectionListener, ActionListener {

    private JPanel sensor_panel;
    private JTabbedPane tabbed_panel;
    private JScrollPane scroll_panel;
    private JSplitPane split_panel;

    private JButton close;
    private JLabel title;

    private JTree tree = new JTree();

    private int nbTab=0;

    public VisualisationFrame(){
        super("Visualisation des capteurs");
        setSize(400, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initialize();
        place();
        setListener();
        setResizable(false);
        setVisible(true);
    }

    private void initialize() {
        close = new JButton("X");
        title = new JLabel("Onglet ");
        tabbed_panel = new JTabbedPane(SwingConstants.TOP);
        scroll_panel = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        split_panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll_panel, tabbed_panel);
        sensor_panel = new JPanel();
    }

    private void place() {
        Container content = getContentPane();
        sensor_panel.add(close);
        content.add(split_panel);
    }

    private void setListener() {
        tree.addTreeSelectionListener(this);
        close.addActionListener(this);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        String node = e.getNewLeadSelectionPath().getLastPathComponent().toString();
        if (node == "") {
        } else {
            nbTab++;
            tabbed_panel.add(node, new JPanel());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
