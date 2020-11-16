
package Util;




import provaNetworking1.Server1;
import provaNetworking1.Server1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread {

    Socket socket;
    Server1 server;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;




    //costruttore
    public ServerConnection (Socket socket, Server1 server) {
        super("Server Connection Threads"); // chiamo il costruttore della classe Thread e gli assegno un nome
        this.socket = socket;
        this.server = server;

    }

    public void sendStringToAllClient (String text) {


        for (int index = 0; index < server.connections.size(); index++)
        {
            //itero per tutti i client e chiamo il metodo send String to client
            ServerConnection sc = server.connections.get(index);
            sc.sendStringToClient(text);
        }
    }

    public void sendStringToClient (String text) {
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public void run() {

        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            

            while (shouldRun) {
                while (din.available()==0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String textIn = din.readUTF();
                sendStringToAllClient(textIn);

            }

            din.close();
            dout.close();
            socket.close();

        } catch ( IOException e) {
            e.printStackTrace();
        }

    }





    public static void main(String[] args) {


    }

}
