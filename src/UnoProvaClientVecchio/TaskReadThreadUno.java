package UnoProvaClientVecchio;


import Util.JsonUtil;
import javafx.application.Platform;
import sample.Main;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
  //  private PrintWriter outSocket;
  //   DataInputStream inSocket;

    private static Map<String, Runnable> comandi = new HashMap<String, Runnable>();

    //constructor
        public TaskReadThreadUno(Socket socket, Client client) throws IOException {
            this.socket = socket;
            this.client = client;
            compilaHashMap();
        }

        private void compilaHashMap() throws IOException {

            if (comandi.isEmpty()) {
         //       System.out.println("dovuta compilare hashmap");
                comandi.put("aggiornati", () -> {
                    try {
                        Client.getClientMaster().aggiornatiListaUser();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        @Override
        public void run () {
            //continuously loop it
            while (true) {
                try {
                    System.out.println("[00 TaskReadUno run() -  inizio : **" + socket.getLocalPort()+"** Client "+ client+" ClientMaster "+ Client.getClientMaster());

                    //Create data input stream
                    inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 //   inSocket = new DataInputStream(socket.getInputStream());
               //     outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    System.out.println("[01 TaskReadUno run() -  dopo inizializzazione flussi : **" + socket.getLocalPort()+"** Client "+ client+" ClientMaster "+ Client.getClientMaster()+" : inSocket : "+inSocket);

                    //get input from the client
                    String message = inSocket.readLine();

                    String comando = JsonUtil.getComandoDaJson(message);

                    System.out.println("[02 TaskReadUno run() -  comando ricevuto dal server : " + comando);

                    Client.getClientMaster().setEsitoDalServer(message);

                    comandi.get(comando).run();


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


