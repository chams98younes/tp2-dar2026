package serverPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServeurCC {
    private static final int PORT = 1234;
    private static final Pattern OPERATION = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+*/-])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$"
    );
    public static void main(String[] args) {
        System.out.println("Serveur CC en attente...");
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(
                     socket.getOutputStream(), true)) {
            System.out.println("Client connecté.");
            String expression = in.readLine();
            System.out.println("Opération reçue : " + expression);
            Matcher matcher = OPERATION.matcher(expression);
            if (!matcher.matches()) {
                out.println("Erreur : opération invalide.");
                return;
            }
            double a = Double.parseDouble(matcher.group(1));
            String operateur = matcher.group(2);
            double b = Double.parseDouble(matcher.group(3));
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
                        out.println("Erreur : division par zéro.");
                        return;
                    }
                    resultat = a / b;
                    break;
                default:
                    out.println("Erreur : opérateur invalide.");
                    return;
            }
            out.println("Résultat : " + resultat);
            System.out.println("Résultat envoyé : " + resultat);
        } catch (Exception e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}

