package UnoProvaClientVecchio;


import Bean.UserBean;
import UnoProvaServerVecchioo.createCommand;
import Util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sample.ControllerHome;
import sample.Main;

import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Client extends Thread implements Observed
{
       //Singleton inizio (assicurarsi che esista solo un'istanza di Client
	private static Client clientMaster = null;
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

    ////////////
    private IntegerProperty numero;

	private Client() throws IOException {


//////////////////


	}//costruttore
	
	public static Client getClientMaster() throws IOException {
		if(clientMaster==null) {
            clientMaster = new Client();
            System.out.println("[00 Costruttore Client vecchio ] : Creato costruttore per nuovo CLient ");
        }
		return clientMaster;
	}
        //Singeton fine

    @Override
    public void run () {
        //continuously loop it
        while (true) {
            try {
                System.out.println("[00 TaskReadUno run() -  inizio : **" + socket.getLocalPort()+"** Client "+ this+" ClientMaster "+ Client.getClientMaster());
                compilaHashMap();
                //Create data input stream
                inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //   inSocket = new DataInputStream(socket.getInputStream());
                //     outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                System.out.println("[01 TaskReadUno run() -  dopo inizializzazione flussi : **" + socket.getLocalPort()+"** Client "+ this+" ClientMaster "+ Client.getClientMaster()+" : inSocket : "+inSocket);

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

    private static Map<String, Runnable> comandi = new HashMap<String, Runnable>();


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








    public void taskClient() throws IOException {
         task = new TaskReadThreadUno(socket, this);
         thread = new Thread(task);
         if(!thread.isAlive())
         {
             System.out.println("[04 Client - taskClient()] : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);
             thread.start();
         } else {
             System.out.println("*thread è già alive*");
         }

    }

    
    @SuppressWarnings("finally")
	public void connetti()
    {

        try
        {
            System.out.println("[01 Client - connetti()] : inizio ");

            socket = new Socket(address, PORT);

            System.out.println("[02 Client - connetti()] : inizio **"+socket.getLocalPort()+"**");

       //     socket = new Socket(address, PORT);
            //canali di comunicazione
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        //     inSocket = new DataInputStream(socket.getInputStream());
         //    outSocket = new DataOutputStream(socket.getOutputStream());
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

            System.out.println("[03 Client - connetti()] : sopo inizializzazione flussi **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

            new Thread(() -> {
                // code goes here.
               // task.run();
                run();

            }).start();


        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();


            // Always close it:
            try { System.out.println("chiuso il socket 1");
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
            System.out.println("[05 Client - loginClient()] : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);
                outSocket.println("login"); //chiama il metodo loginServer di ServerThread
                outSocket.flush();


            String esitoServ = inSocket.readLine();

            System.out.println("esito dal server "+esitoServ);

            new Thread(() -> {
                // code goes here.
                // task.run();
                run();

            }).start();




        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();


        }


    }


    public void provaClient() throws IOException {
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

        try {

            System.out.println("[06 Client - aggiornaTabellaNew()] : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

            String jsonUser = JsonUtil.setComandoJson("update", null);

            System.out.println("dal client parte questo json : "+jsonUser);

            System.out.println("[07 Client - aggiornaTabellaNew()] : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

            outSocket.println(jsonUser+"\n");
            outSocket.flush();

            new Thread(() -> {
                // code goes here.
                // task.run();
                run();

            }).start();

        }  catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();


            // Always close it:
            try { System.out.println("chiuso il socket 2");
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }


      /*  System.out.println("....update NEW mandato");
        esitoDalServer = inSocket.readLine();*/

    }

    public void aggiungiUserinTabella(String cognome, String nome) throws IOException {

        try {

            System.out.println("[08 Client - aggiungiUserinTabella()] : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

            UserBean userParziale = new UserBean(-1, nome, cognome);

            String jsonUser = JsonUtil.setComandoJson("aggiungiUser", userParziale);

            System.out.println("[09 Client - aggiungiUserinTabella()] : json che manderò il client per inserire l'user : "+jsonUser+" : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);



            outSocket.println(jsonUser+"\n");
            outSocket.flush();

            new Thread(() -> {
                // code goes here.
                // task.run();
                run();

            }).start();
            System.out.println("[10 Client - aggiungiUserinTabella()] : json Mandato! il client per inserire l'user : "+jsonUser+" : socket **"+socket.getLocalPort()+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);

        }  catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
}

            // Always close it:
            try {
                System.out.println("chiuso il socket 3");
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }





    public void aggiornatiListaUser() {

        System.out.println("[11 Client - aggiornatiListaUser()] : inizio metodo Aggiornati : socket **"+socket+"** +inSocket:+ "+inSocket+" +outSocket:+ "+outSocket);


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
        new Thread(() -> {
            // code goes here.
            // task.run();
            run();

        }).start();

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

