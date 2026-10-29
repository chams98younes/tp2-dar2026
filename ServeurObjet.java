package serverPackage;

import clientPackage.Operation;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurObjet {
    private static final int PORT = 1234;
    public static void main(String[] args) {
        System.out.println("Serveur Objet en attente...");
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept()) {
            System.out.println("Client connecté.");
            // Créer le flux de sortie en premier
            ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in =
                    new ObjectInputStream(socket.getInputStream());
            // Recevoir l'objet
            Operation operation = (Operation) in.readObject();
            double a = operation.getOperande1();
            String operateur = operation.getOperateur();
            double b = operation.getOperande2();
            System.out.println("Opération reçue : "
                    + a + " " + operateur + " " + b);
            double resultat;
            switch (operateur) {
                case "+":
                    resultat = a + b;
                    break;
                case "-":
                    resultat = a - b;
                    break;
                case "*":
                    resultat = a * b;
                    break;
                case "/":
                    if (b == 0) {
                        out.writeObject("Erreur : division par zéro.");
                        out.flush();
                        return;
                    }
                    resultat = a / b;
                    break;
                default:
                    out.writeObject("Erreur : opérateur invalide.");
                    out.flush();
                    return;
            }
            // Envoyer le résultat au client
            out.writeObject(Double.valueOf(resultat));
            out.flush();
            System.out.println("Résultat envoyé : " + resultat);
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}

