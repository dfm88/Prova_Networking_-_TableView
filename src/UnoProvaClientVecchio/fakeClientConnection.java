package UnoProvaClientVecchio;



import Util.JsonUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class fakeClientConnection extends Thread {


    Socket s;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;
    fakeClient cl;
    private static Map<String, Runnable> comandi = new HashMap<String, Runnable>();

    //costruttore
    public fakeClientConnection(Socket socket, fakeClient client) throws IOException {
        this.s = socket;
        this.cl = client;
        compilaHashMap();
    }

    private void compilaHashMap() throws IOException {

        if (comandi.isEmpty()) {
            //       System.out.println("dovuta compilare hashmap");
            comandi.put("aggiornati", () -> {
                cl.aggiornatiListaUser();
            });
        }
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

    String jsonDaServer;

    public void run(){

        try {

            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(shouldRun){
                try{
                    /*aspetta una risposta dal server*/
                    /*quando arriva la risposta stampala sulla console*/
                    jsonDaServer = din.readUTF();
                    System.out.println("FAKE_CLIENT_CONNECTIONJSON_DA_SERVER : "+jsonDaServer);
                   cl.setEsitoDalServer(jsonDaServer);

                    //QUI CHIAMA IL METODO CLIENT CON HASHMAP
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // code goes here.
                            String comando = JsonUtil.getComandoDaJson(jsonDaServer);
                            comandi.get(comando).run();
                        }
                    }).start();

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