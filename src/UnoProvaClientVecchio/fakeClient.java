package UnoProvaClientVecchio;


import Bean.UserBean;
import UnoProvaServerVecchioo.createCommand;
import Util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sample.ControllerHome;
import sample.Main;

import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class fakeClient implements Observed
{
    //Singleton inizio (assicurarsi che esista solo un'istanza di Client
    private static fakeClient fakeClientMaster = null;
    private final static int PORT=3000;


    private final static String address="localhost";

    private Socket socket;
    private BufferedReader inSocket;

    // DataInputStream inSocket;
    //  DataOutputStream outSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;

    Thread thread;

    public static boolean nuovoUser;

    ControllerHome cH;

    TaskReadThreadUno task;
    String esitoDalServer;


    fakeClientConnection cc;
    public fakeClient() throws IOException {

        try {
            Socket s = new Socket("localhost", 3333);
            cc = new fakeClientConnection(s, this);
            cc.start();
            System.out.println("client connesso sullka porta "+s);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }//costruttore

        //    String input = inSocket.readUTF();

       //     cc.sendStringToServer(clientName+" ha scritto : "+input);


   /* public static fakeClient getClientMaster() throws IOException {
        if(fakeClientMaster==null) {
            fakeClientMaster = new fakeClient();
            System.out.println("[00 Costruttore fakeClient ] : Creato costruttore per nuovo fakeCLient ");
        }
        return fakeClientMaster;
    }*/
    //Singeton fine



    private ArrayList<UserBean> listaDalServer = new ArrayList<UserBean>();

    private ArrayList <Observer> observers = new ArrayList<Observer>();

    public void setListaDalServer(ArrayList<UserBean> listaDalServer) {
        this.listaDalServer = listaDalServer;
        for(int i =0; i<observers.size();i++)
        {
            observers.get(i).update(this);
        }
    }

    public ArrayList<UserBean> getListaDalServer() {
        return listaDalServer;
    }

    public void aggiornaTabellaNew() throws IOException {

        try {

     //       System.out.println("[06 Client - aggiornaTabellaNew()] : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

            String jsonUser = JsonUtil.setComandoJson("update", null);

            System.out.println("dal client parte questo json : "+jsonUser);

     //       System.out.println("[07 Client - aggiornaTabellaNew()] : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

            cc.sendStringToServer(jsonUser);


        }  catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();

        }

    }

    public void aggiungiUserinTabella(String cognome, String nome) throws IOException {

        try {

            System.out.println("[08 Client - aggiungiUserinTabella()] : socket **"+this+"** "+cognome+" - "+nome);

            UserBean userParziale = new UserBean(-1, nome, cognome);

            String jsonUser = JsonUtil.setComandoJson("aggiungiUser", userParziale);

            System.out.println("[09 Client - aggiungiUserinTabella()] : json che manderò il client per inserire l'user : "+jsonUser+" - da client - "+this);



            cc.sendStringToServer(jsonUser);

         //   System.out.println("[10 Client - aggiungiUserinTabella()] : json Mandato! il client per inserire l'user : "+jsonUser+" : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

        }  catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }


    }





    public void aggiornatiListaUser() {

    //    System.out.println("[11 Client - aggiornatiListaUser()] : inizio metodo Aggiornati : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);


        System.out.println("[12 Client - aggiornatiListaUser()] : esito dal server "+esitoDalServer);

        Gson gson = new Gson();

        createCommand cc = gson.fromJson(esitoDalServer, createCommand.class);
        ArrayList<UserBean> aaa; //= JsonUtil.nestedClassFromJson(cc.getObj(), ArrayList.class,gson );

        //    Object oo = JsonUtil.nestedClassFromJson(cc.getObj(), Object.class, gson);

        String gson2 = (new Gson().toJson(cc.getObj()));

        aaa = gson.fromJson(gson2, new TypeToken<List<UserBean>>(){}.getType());

        System.out.println("[13 Client - aggiornatiListaUser()] arraylist ricevuta dal client "+aaa.get(0).getNome()+" "+aaa.get(1).getNome());

        //TypeToken<ArrayList<UserBean>> token = new TypeToken<ArrayList<UserBean>>() {};
        // ArrayList<UserBean> aaa = (new Gson().fromJson(esitoDalServer, token.getType()));

        setListaDalServer(aaa);

//  socket.close();


    }

    @Override
    public void Attach(Observer ob) {
        System.out.println("dal ControllerHome attaccato l'osservatore");
        observers.add(ob);
    }

    @Override
    public void Detach(Observer ob) {

    }

    @Override
    public void Notify() {

    }


    public ArrayList<UserBean> aggiornaTabella() throws IOException, InterruptedException {


        System.out.println("sto per mandare 'update' al server....");

        String jsonUser = JsonUtil.setComandoJson("update", null);



        outSocket.println(jsonUser);
        outSocket.flush();



        System.out.println("....update mandato");

        esitoDalServer = inSocket.readLine();

        System.out.println("esito dal server "+ esitoDalServer);

        TypeToken<ArrayList<UserBean>> token = new TypeToken<ArrayList<UserBean>>() {};
        ArrayList<UserBean> aaa = (new Gson().fromJson(esitoDalServer, token.getType()));

        System.out.println("arraylist ricevuta dal client "+aaa.get(0)+" "+aaa.get(1));



        return aaa;


    }





    public void setController(ControllerHome cH) {
        this.cH = cH;
    }



    public String getEsitoDalServer() {
        return esitoDalServer;
    }

    public void setEsitoDalServer(String esitoDalServer) {
        this.esitoDalServer = esitoDalServer;
    }



}

