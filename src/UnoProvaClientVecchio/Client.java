package UnoProvaClientVecchio;


import Bean.UserBean;
import UnoProvaServerVecchioo.createCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Client
{
///////Singleton inizio (assicurarsi che esista solo un'istanza di Client
	private static Client clientMaster = null;
	private Client() throws IOException {
        socket = new Socket(address, PORT);
	    taskClient();

	}//costruttore
	
	public static Client getClientMaster() throws IOException {
		if(clientMaster==null) {
            clientMaster = new Client();
            System.out.println("Creato costruttore per nuovo CLient");
        }
		return clientMaster;
	}
///////Singeton fine
	
	
	private final static int PORT=3000;
    
    
    private final static String address="localhost";
    
    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;
    
    public static boolean nuovoUser;

    TaskReadThreadUno task;
    
 /*   public Client()
    {
        System.out.println("Client avviato");
        
        try
        {
            esegui();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
        finally
        {
            // Always close it:
            try {
                socket.close();
            } catch(IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }*/


    public void taskClient()
    {
         task = new TaskReadThreadUno(socket, this);
        Thread thread = new Thread(task);
        thread.start();
    }

    
    @SuppressWarnings("finally")
	public void connetti()
    {

        try
        {
            System.out.println("Il client tenta di connettersi");

        //    socket = new Socket(address, PORT);
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

          /*  new Thread(() -> {
                // code goes here.
                task.run();
            }).start();
*/
            //   inSocket.readLine();

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

    public void triggeraServerperOttenereRisposta() throws IOException, InterruptedException {

        System.out.println("preparo invita string trigger al server "+ LocalDateTime.now());

        outSocket.println("trigger"); //chiama il metodo loginServer di ServerThread
        outSocket.flush();
       // Thread.sleep(5000);

        System.out.println("invita string trigger al server"+ LocalDateTime.now()+"\n");
        String s = String.valueOf(inSocket.readLine());

        System.out.println("sono il client e il server mi ha triggerato con il messaggio "+s);

//  taskClient();









    }




    public ArrayList<UserBean> aggiornaTabella() throws IOException {

        System.out.println("sto per mandare 'update' al server....");

        outSocket.println("update");
        outSocket.flush();

        System.out.println("....update mandato");

        String esitoDalServer = inSocket.readLine();

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
}

