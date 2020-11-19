package UnoProvaServerVecchioo;



import Bean.UserBean;
import DAO.UserDAO;
import UnoProvaClientVecchio.Client;
import Util.DBConnection;

import Util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class ServerThread extends Thread
{

	private Socket socketClient = null; //dichiaro il socket client chiamato socketClient
	//creo un user

	public Socket getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(Socket socketClient) {
		this.socketClient = socketClient;
	}


	//creo oggetto news

	//creo oggetto managerFile

//	DataInputStream in;
	//DataOutputStream out;
	private BufferedReader in;
    private PrintWriter out;
    private static int posizioneNotiziaCommentata; //variabile per poter aggiornare con il .set nell'array, 
    										//la notizia a cui viene aggiunto un commento
    public int numeroUtentiConnessi;
    private Server serverMain;
	private String jsonDaClient;
	private static Map<String, Runnable> comandi = new HashMap<String, Runnable>();
    		
//inizio gestione Server con Thread
    
    //Costruttore
    public ServerThread(Socket s, Server server) throws IOException {

		this.socketClient = s;
		System.out.println("[00.1 Server Thread - costruttore socket dopo] -   : **"+socketClient.getPort()+"** Client "+ Client.getClientMaster());
		this.serverMain = server;
		System.out.println("[00.1 Server Thread - costruttor serverMain] -   : "+serverMain+" Client "+ Client.getClientMaster());
		compilaHashMap();
	}
    
    @Override
	public void run()
	{
		try {
			System.out.println("[00 Server Thread - Metodo run() inizio] -   : **"+socketClient.getPort()+"** Client "+ Client.getClientMaster());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try
		{
    	//devo ora specificare i 2 flussi di input e output
			in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);

		//	in=new DataInputStream(socketClient.getInputStream());
		//	out=new DataOutputStream(socketClient.getOutputStream());
			System.out.println("[01 Server Thread - Metodo run() dopo l'inizializzazine dei flussi] -   : **"+socketClient.getPort()+"** Client "+ Client.getClientMaster());
		
        
        //chiamo il metodo che mi consente di accettare comandi dal client

		//	new Thread(() -> {
				// code goes here.
		//		try {
					comandiClient();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//	}).start();

			//comandiClient();
		
		} catch (IOException e) {
		
		e.printStackTrace();
		} 
//fine gestione Server con Thread  		

	}

	public String getJsonDaClient() {
		return jsonDaClient;
	}

	public void setJsonDaClient(String jsonDaClient) {
		this.jsonDaClient = jsonDaClient;
	}

	/***************************************************************************/
		
	public void comandiClient() throws IOException {
		System.out.println("[02 Server Thread - Metodo comandiClient() inizio] -   : **"+socketClient.getPort()+"** Client "+ Client.getClientMaster());


		//while(jsonDaClient==null)
		//{
	//	System.out.println("é pronto l'inputstream ? "+in.ready());

	//	System.out.println("--> "+in.available());

				jsonDaClient = in.readLine();

		System.out.println("[03 Server Thread - Metodo comandiClient() Json dal client 1 ] "+jsonDaClient+" Client "+ Client.getClientMaster());



		if(jsonDaClient!=null) {


			System.out.println("[04 Server Thread - Metodo comandiClient() Json dal client 1 ] "+jsonDaClient+" Client "+ Client.getClientMaster());

	/*	if(jsonDaClient.equals("login"))
		{
			System.out.println("chiamaoto metodo login del server");

			out.println("ciao");
			out.flush();



		}

		run();*/
			//estraggo la stringa corrispondente al 'comando'
			// dell'oggetto Create Command del client mappato nel json
			String comando = JsonUtil.getComandoDaJson(jsonDaClient);

			System.out.println("[05 Server Thread - Metodo comandiClient() omando ricevuto dal client ]: " + comando+" Client "+ Client.getClientMaster());


			//    serverMain.compilaTextArea("Comando ricevuto dal client : "+comando);

			//invoco dall'HashMap 'comandi' il metodo corrispondente al comando
			comandi.get(comando).run();

		}


	}

	public void compilaHashMap()
	{
		if(comandi.isEmpty())
		{ System.out.println("[00.1 Server Thread - compila HshMap] ");
			comandi.put("update", ()-> {
				try {
					update();
				} catch (ClassNotFoundException | SQLException | IOException e) {
					e.printStackTrace();
				}
			});
			comandi.put("aggiungiUser", ()-> {
				try {
					addUser();
				} catch (ClassNotFoundException | SQLException | IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private void addUser() throws SQLException, ClassNotFoundException, IOException {

		//spacchetto il json per ottenere username e psw
		System.out.println("[07 Server Thread - addUser() inizio : **" + socketClient.getPort()+"** Client "+ Client.getClientMaster());

		Gson gson = new Gson();

		System.out.println("[08 Server Thread - addUser() json dal cliente : "+jsonDaClient);

		createCommand gc = gson.fromJson(jsonDaClient, createCommand.class);
		UserBean userParzialeDaClient = JsonUtil.nestedClassFromJson(gc.getObj(), UserBean.class,gson );

		Connection con = null;
		try {
			con = DBConnection.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		UserDAO userDao = new UserDAO(con);



		userDao.addUserInDB(userParzialeDaClient.getNome(), userParzialeDaClient.getCognome());

		//String arrayInJson = (new Gson().toJson(userArrayList));
		con.close();

		update();


		//run();  run parte gia da update()
	}


	private void update() throws SQLException, ClassNotFoundException, IOException {

		System.out.println("[06 Server Thread - update() inizio : **" + socketClient.getPort()+"** Client "+ Client.getClientMaster());

		Connection con = null;
		try {
			con = DBConnection.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		UserDAO userDao = new UserDAO(con);



		ArrayList<UserBean> userArrayList = (ArrayList<UserBean>) userDao.getUserFromDB();

						//String arrayInJson = (new Gson().toJson(userArrayList));

		String jsonUser = JsonUtil.setComandoJson("aggiornati", userArrayList);

		//out.writeUTF(jsonUser);

		sendStringToAllClient(jsonUser);
	//	out.flush();



		//in.close();
		//out.close();
		con.close();


		new Thread(() -> {
			// code goes here.
			run();
		}).start();

	}

	/***************************************************************************/
	public void mandaMessaggioAlCLient() throws IOException {
		System.out.println("metodo Server manda messaggio client");

					out.println("ciao");
					out.flush();

		System.out.println("messaggio al client mandato");


		//in attesa nuovi comandi dal client
		run();
	}

	public void sendMessage(String message) throws IOException {
		System.out.println("[09 Server Thread - sendMessage broadcat chiamato da server () inizio : **" + socketClient.getPort()+"** Client "+ Client.getClientMaster());
		out.println(message);
		out.flush();

	}

	public void sendStringToAllClient (String text) {
		for (int index = 0; index < serverMain.connectionList.size(); index++)
		{
			//itero per tutti i client e chiamo il metodo send String to client
			ServerThread sc = serverMain.connectionList.get(index);
			sc.sendStringToClient(text);
		}
	}

	public void sendStringToClient (String text) {
		out.println(text);
		out.flush();

	}

	public static void main(String[] args) throws IOException, InterruptedException
    {
		
    }


}
