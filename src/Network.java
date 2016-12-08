import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Network {
    public static void main(String[] args) {
        BufferedReader br = null;
        PrintStream ps = null;
        String line;
        Socket socket = null;
        int port = -1;

        try {
            port = 7888;
            socket = new Socket("127.0.0.1", port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());

            // Exemple de connexion au server
            ps.println("ConnexionCapteur;Radiateur;Température;U3;2;204;A l'entrée de la pièce");
            line = br.readLine();
            System.out.println("le serveur me repond : "+line);

            // Exemple de transmission de donnée au server
            ps.println("ValeurCapteur;20");

            // Exemple de déconnexion au server
            ps.println("DeconnexionCapteur;Radiateur");
            line = br.readLine();
            System.out.println("le serveur me repond : "+line);

            br.close();
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
