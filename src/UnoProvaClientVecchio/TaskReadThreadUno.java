package UnoProvaClientVecchio;


import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 *
 * @author topman garbuja,
 *
 * It is used to get input from server simultaneously
 */
public class TaskReadThreadUno implements Runnable {
    //private variables
    Socket socket;
    Client client;
    private BufferedReader inSocket;
    private PrintWriter outSocket;

    //constructor
    public TaskReadThreadUno(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        //continuously loop it
        while (true) {
            try {
                System.out.println("Partito metodo run di TaskReadThreadUno");
                //Create data input stream
                inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //get input from the client
                String message = inSocket.readLine();

                System.out.println("Messaggio Ricevuto dal Serverrrrrrr : "+message + " --- "+ LocalDateTime.now());

                //append message of the Text Area of UI (GUI Thread)
                Platform.runLater(() -> {
                    //display the message in the textarea
//                     client.txtAreaDisplay.appendText(message + "\n");
                });
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
