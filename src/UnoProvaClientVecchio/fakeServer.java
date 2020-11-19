package UnoProvaClientVecchio;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class fakeServer {


    ServerSocket ss;
    //creo un arrai che conservi tutte le connessioni al server
    public ArrayList<fakeServerConnection> connections = new ArrayList<fakeServerConnection>();
    boolean shouldRun = true;

    //Socket s;
    //DataInputStream din;
    //DataOutputStream dout;

    //costruttore
    public fakeServer() {
        try {


            ss = new ServerSocket(3333);

            while(shouldRun) {

                System.out.println("Server in ascoloto - porta : "+3333);
                Socket s = ss.accept();
                System.out.println("accettata connessione da socket "+s);
                //chreo un oggetto ServerConnection e gli passo lo stesso oggetto chiamate ovver l'oggetto della classe Server
                fakeServerConnection sc = new fakeServerConnection(s, this);
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

        new fakeServer();
    }

}
