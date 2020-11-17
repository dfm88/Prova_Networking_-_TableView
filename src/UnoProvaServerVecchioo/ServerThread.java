package UnoProvaServerVecchioo;



import Bean.UserBean;
import DAO.UserDAO;
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

	
	//creo oggetto news

	//creo oggetto managerFile

	
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
    public ServerThread(Socket s, Server server)
	{
		this.socketClient = s;
		this.serverMain = server;
		compilaHashMap();
	}
    
    @Override
	public void run()
	{

		System.out.println("metodo run Server partito");
		try
		{
    	//devo ora specificare i 2 flussi di input e output
			in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);
		
        
        //chiamo il metodo che mi consente di accettare comandi dal client

		//	new Thread(() -> {
				// code goes here.
				comandiClient();
		//	}).start();

			//comandiClient();
		
		} catch (IOException e) {
		
		e.printStackTrace();
		} 
//fine gestione Server con Thread  		

	}
		
		/***************************************************************************/
		
	public void comandiClient()

	{
		System.out.println("[0] ServerThread pronto a ricevere comandi dal client "	+ LocalDateTime.now());

		try
		{
			//leggo il json ricevuto dal client
			jsonDaClient = in.readLine();

			if(jsonDaClient!=null)
			{
				System.out.println("Json dal client : "+jsonDaClient);

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



				//    serverMain.compilaTextArea("Comando ricevuto dal client : "+comando);

				//invoco dall'HashMap 'comandi' il metodo corrispondente al comando
				comandi.get(comando).run();

				run();
			}





		} catch (IOException e)
		{
			
			e.printStackTrace();
		}
	}

	public void compilaHashMap()
	{
		if(comandi.isEmpty())
		{ System.out.println("dovuta compilare hashmap");
			comandi.put("update", ()-> {
				try {
					update();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private void update() throws SQLException, ClassNotFoundException {

		Connection con = null;
		try {
			con = DBConnection.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		UserDAO userDao = new UserDAO(con);

		System.out.println("[2] il Client ha chiamato il metodo update tabella'");

		ArrayList<UserBean> userArrayList = (ArrayList<UserBean>) userDao.getUserFromDB();

						//String arrayInJson = (new Gson().toJson(userArrayList));

		String jsonUser = JsonUtil.setComandoJson("aggiornati", userArrayList);


		out.println(jsonUser);
		out.flush();

		con.close();



		run();
	}

	/***************************************************************************/
	public void mandaMessaggioAlCLient()
	{
		System.out.println("metodo Server manda messaggio client");

					out.println("ciao");
					out.flush();

		System.out.println("messaggio al client mandato");


		//in attesa nuovi comandi dal client
		run();
	}

	public void sendMessage(String message) {
		out.println(message);
		out.flush();

	}

	public static void main(String[] args) throws IOException, InterruptedException
    {
		
    }


}
