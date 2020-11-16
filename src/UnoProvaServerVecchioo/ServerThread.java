package UnoProvaServerVecchioo;



import Bean.UserBean;
import DAO.UserDAO;
import Util.DBConnection;

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
    		
//inizio gestione Server con Thread
    
    //Costruttore
    public ServerThread(Socket s, Server server)
	{
		this.socketClient = s;
		this.serverMain = server;
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

			new Thread(() -> {
				// code goes here.
				comandiClient();
			}).start();

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


			//prova GSON
			String inputClientString = in.readLine();

			System.out.println("[1] stringa dal client "+inputClientString);
/*
			Gson gson = new Gson();
			//UserBean user = gson.fromJson(inputClientString, UserBean.class);
			createCommand cmd = gson.fromJson(inputClientString, createCommand.class);
			createCommand cmd2 = cmd;
			UserBean u = gson.fromJson(new Gson().toJson(((LinkedTreeMap<String, Object>) cmd.getObj())), UserBean.class);
			System.out.println("Con JSON : "+u.getNome());

*/

			//UPDATE COSTANTE
			if(inputClientString.equals("update"))
			{
				Connection con = null;
				try {
					con = DBConnection.getConnection();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				UserDAO userDao = new UserDAO(con);

				System.out.println("[2] il Client ha chiamato il metodo update tabella'");

				ArrayList<UserBean> userArrayList = (ArrayList<UserBean>) userDao.getUserFromDB();

				String arrayInJson = (new Gson().toJson(userArrayList));

				out.println(arrayInJson);
				out.flush();

				con.close();



				run();
			}


			//INSERISCI IN TABELLA
			if(inputClientString.equals("aggiungi"))
			{
				String nome = in.readLine();
				String cognome = in.readLine();

				Connection con = null;
				try {
					con = DBConnection.getConnection();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				UserDAO userDao = new UserDAO(con);

				System.out.println("il Client ha chiamato il metodo aggiungi tabella' su cognome "+cognome+" / e nome : "+nome);

				userDao.addUserInDB(cognome, nome);


				out.println("ok li ho inseriti");
				out.flush();
				con.close();


				run();
			}



	//		System.out.println("Ricevuto  dal client : "+cmd.getComando());
			//System.out.println("aaaaaaaaaaaaa  "+inputClientString);
			
			// "  LOGIN  " 
			if(inputClientString.equals("login"))
			{
				serverMain.broadcast("buongiorno a tutti");
			//	out.println("buongiorno a tutti col vecchio mertodo");
			//	out.flush();

				System.out.println("il Client mi ha mandato la stringa 'login'");
				run();
			}

			// "  PROVA  "
			else if(inputClientString.equals("prova"))
			{

				System.out.println("il Client mi ha mandato la stringa 'prova'");
				run();

			}

			// "  PROVA  "
			else if(inputClientString.equals("trigger"))
			{
				System.out.println("il Client mi ha mandato la stringa 'trigger' "+LocalDateTime.now());
				mandaMessaggioAlCLient();



			}
			

		} catch (IOException e)
		{
			
			e.printStackTrace();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
