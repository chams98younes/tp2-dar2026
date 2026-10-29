package clientPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;

public class ClientCC {

    private static final int PORT = 1234;
    private static final Pattern OPERATION = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+*/-])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$"
    );
    public static void main(String[] args) {
        try (BufferedReader clavier = new BufferedReader(
                new InputStreamReader(System.in))) {
            System.out.print("Entrez une opération (ex. 55 * 25) : ");
            String expression = clavier.readLine();
            if (!OPERATION.matcher(expression).matches()) {
                System.out.println("Opération invalide !");
                System.out.println("Exemple valide : 55 * 25");
                return;
            }
            try (Socket socket = new Socket("localhost", PORT);
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(
                         socket.getOutputStream(), true)) {
                out.println(expression);
                System.out.println("Opération envoyée : " + expression);
                String reponse = in.readLine();
                System.out.println("Réponse du serveur : " + reponse);
            }
        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}

