package provaNetworking1;




import Util.ServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server1 {


    ServerSocket ss;
    //creo un arrai che conservi tutte le connessioni al server
    public ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    boolean shouldRun = true;

    //Socket s;
    //DataInputStream din;
    //DataOutputStream dout;

    //costruttore
    public Server1() {
        try {


            ss = new ServerSocket(3333);

            while(shouldRun) {


                Socket s = ss.accept();
                //chreo un oggetto ServerConnection e gli passo lo stesso oggetto chiamate ovver l'oggetto della classe Server
                ServerConnection sc = new ServerConnection(s, this);
                sc.start();
                connections.add(sc); // aggiungo all'arrayList

                //s = ss.accept();
                //din = new DataInputStream(s.getInputStream());
                //dout = new DataOutputStream(s.getOutputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    public static void main(String[] args) {

        new Server1();
    }

}
