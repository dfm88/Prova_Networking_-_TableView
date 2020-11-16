package provaNetworking1;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientConnection extends Thread {

    Socket s;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;

    //costruttore
    public ClientConnection(Socket socket, Client1 client) {

        this.s = socket;


    }



    public void sendStringToServer(String text) {

        try {
            //stampo la stringa al serever
            dout.writeUTF(text);
            dout.flush();

        } catch (IOException e) {
            e.printStackTrace();
            close();
        }


    }


    public void run(){

        try {

            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(shouldRun){
                try{
                    /*aspetta una risposta dal server*/
                    while(din.available()==0) { //se niente è disponibile in lettura, sleep
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e ) {
                            e.printStackTrace();
                        }
                    }
                    /*quando arriva la risposta stampala sulla console*/
                    String reply = din.readUTF();
                    System.out.println(reply);

                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }

            }

        } catch (IOException e){
            e.printStackTrace();
            close();
        }



    }

    public void close() {
        try{

            din.close();
            dout.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public static void main(String[] args) {


    }

}