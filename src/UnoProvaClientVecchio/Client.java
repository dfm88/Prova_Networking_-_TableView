package UnoProvaClientVecchio;


import Bean.UserBean;
import UnoProvaServerVecchioo.createCommand;
import Util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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


public class Client implements Observed
{
       //Singleton inizio (assicurarsi che esista solo un'istanza di Client
	private static Client clientMaster = null;
    private final static int PORT=3000;


    private final static String address="localhost";

    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;

    Thread thread;

    public static boolean nuovoUser;

    ControllerHome cH;

    TaskReadThreadUno task;
    String esitoDalServer;

    ////////////
    private IntegerProperty numero;

	private Client() throws IOException {
        socket = new Socket(address, PORT);
        taskClient();
//////////////////


	}//costruttore
	
	public static Client getClientMaster() throws IOException {
		if(clientMaster==null) {
            clientMaster = new Client();
            System.out.println("Creato costruttore per nuovo CLient");
        }
		return clientMaster;
	}
        //Singeton fine








    public void taskClient() throws IOException {
         task = new TaskReadThreadUno(socket, this);
         thread = new Thread(task);
        thread.start();
    }

    
    @SuppressWarnings("finally")
	public void connetti()
    {

        try
        {

            System.out.println("Il client tenta di connettersi");

            socket = new Socket(address, PORT);
            //canali di comunicazione
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
            
            System.out.println("Client connesso");

          /*  new Thread(() -> {
                // code goes here.
                task.run();
            }).start();*/


        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
           

            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }

    }

    //metodo login impostato a boolean in modo da notificare anche al loginConmtroller l'esito del login
    public void loginClient()
    {


        try
        {
            System.out.println("invita string login al server");
                outSocket.println("login"); //chiama il metodo loginServer di ServerThread
                outSocket.flush();


            String esitoServ = inSocket.readLine();

            System.out.println("esito dal server "+esitoServ);




        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();

            try {
                socket.close();
            }
            catch(IOException ex)
            {
                System.err.println("Socket not closed");
            }
        }


    }


    public void provaClient()
    {
        System.out.println("invita string prova al server");



        UserBean user = new UserBean(7, "Margoni", "7777");
        Gson gson = new Gson();
        String s = "azionn";

        createCommand cmd = new createCommand(s, user);
        String json = gson.toJson(cmd);

        System.out.println(json);

        outSocket.println(json); //chiama il metodo loginServer di ServerThread
        outSocket.flush();

     //   taskClient();


    }


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
        System.out.println("sto per mandare 'uodate'...NEW al server....");

        String jsonUser = JsonUtil.setComandoJson("update", null);

        System.out.println("dal client parte questo json : "+jsonUser);

        outSocket.println(jsonUser);
        outSocket.flush();

      /*  System.out.println("....update NEW mandato");
        esitoDalServer = inSocket.readLine();*/



    }

    public void aggiornatiListaUser() {


        System.out.println("JSON ricevuta dal serever "+esitoDalServer);

        Gson gson = new Gson();

        createCommand cc = gson.fromJson(esitoDalServer, createCommand.class);
       ArrayList<UserBean> aaa; //= JsonUtil.nestedClassFromJson(cc.getObj(), ArrayList.class,gson );

    //    Object oo = JsonUtil.nestedClassFromJson(cc.getObj(), Object.class, gson);

        String gson2 = (new Gson().toJson(cc.getObj()));

        aaa = gson.fromJson(gson2, new TypeToken<List<UserBean>>(){}.getType());

        System.out.println("arraylist ricevuta dal client "+aaa.get(0).getNome()+" "+aaa.get(1).getNome());

    //    System.out.println(JsonUtil.nestedClassFromJson(cc.getObj(), ArrayList.class,gson ));
  //      System.out.println("arraylist ricevuta dal client "+aaa.get(0)+" "+aaa.get(1));



        TypeToken<ArrayList<UserBean>> token = new TypeToken<ArrayList<UserBean>>() {};
       // ArrayList<UserBean> aaa = (new Gson().fromJson(esitoDalServer, token.getType()));

       setListaDalServer(aaa);


    }

    @Override
    public void Attach(Observer ob) {
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



    public void aggiungiUserinTabella(String cognome, String nome) throws IOException {

        outSocket.println("aggiungi");
        outSocket.flush();
        outSocket.println(cognome);
        outSocket.flush();
        outSocket.println(nome);
        outSocket.flush();



        String esitoDalServer = inSocket.readLine();

        System.out.println("il server ha aggiunto? "+esitoDalServer);


    }

    public void setController(ControllerHome cH) {
        this.cH = cH;
    }
/*

    public void aggiornati() {
        Main.cH.popolaTable();
    }
*/

    public String getEsitoDalServer() {
        return esitoDalServer;
    }

    public void setEsitoDalServer(String esitoDalServer) {
        this.esitoDalServer = esitoDalServer;
    }



}

