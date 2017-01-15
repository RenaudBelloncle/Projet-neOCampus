package VisualisationInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ConnectionFrame extends JFrame implements ActionListener{

    private JLabel id;
    private JLabel ip;
    private JLabel port;

    private JTextField id_text;
    private JTextField ip_text;
    private JTextField port_text;

    private JButton cancel;
    private JButton connection;

    private JPanel text_panel;
    private JPanel button_panel;
    private JPanel main_panel;

    private VisualisationFrame frame;
    private VisualisationServer server;

    public ConnectionFrame(VisualisationFrame frame, VisualisationServer server){
        super("Connexion au serveur");
        setSize(450, 120);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.frame = frame;
        this.server = server;
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

        id = new JLabel("Id : ");
        ip = new JLabel("Ip : ");
        port = new JLabel("Port : ");

        ip_text = new JTextField(10);
        id_text = new JTextField(10);
        port_text = new JTextField(5);

        cancel = new JButton("Annuler");
        connection = new JButton("Connexion");
    }

    private void place() {
        Container content = getContentPane();

        text_panel.add(id);
        text_panel.add(id_text);
        text_panel.add(ip);
        text_panel.add(ip_text);
        text_panel.add(port);
        text_panel.add(port_text);

        button_panel.add(cancel);
        button_panel.add(connection);

        main_panel.add(text_panel);
        main_panel.add(button_panel);

        content.add(main_panel);
    }

    private void setListener() {
        cancel.addActionListener(this);
        connection.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            this.dispose();
        } else if (e.getSource() == connection) {
            if (server.connect(id_text.getText(), ip_text.getText(), Integer.parseInt(port_text.getText()))) {
                frame.updateStatus("   Status : Connecté      ");
                this.dispose();
            }
        }
    }
}
