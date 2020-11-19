package UnoProvaServerVecchioo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server 

{
	private final static int porta = 3000;
	//private final static String indirizzo = "localhost";
	
	private ServerSocket server = null; //dichiaro un ServerSocket di nome "server"
	private Socket socketClient = null; //dichiaro il socket client chiamato socketClient
	//creo un user

	List<ServerThread> connectionList = new ArrayList<ServerThread>();
	
    private  static  int numeroUtentiConnessi;
   
	
/**
 * @throws IOException *************************************************************************/	
	public void attendiConnessione() throws IOException
	{
			
	//System.out.println(percorsoDBUser);
		System.out.println("[00 Server Main] - Inizializzo il Server");
		
		server = new ServerSocket(porta);//inizializzo il servizio
		
		System.out.println("[01 Server Main] - Server in ascolto sulla porta " + porta);
				
			while(true)
			{
				try
				{
					
				socketClient = server.accept(); //il server si mette in ascolto sulla porta data
												//la funzione server.accept() resttuisce una variabile socket
												//che in questo caso è quella del client
				
				System.out.println("[02 Server Main] - Connessione stabilita con il client");
			
				
				System.out.println("[03 Server Main] - Elenco abilitati :");

				ServerThread servertThread = new ServerThread(socketClient, this);
				connectionList.add(servertThread);

					for(int i=0; i< connectionList.size();i++)
					{
						System.out.println("[04 Server Main] - server : "+server);
						System.out.println("[05 Server Main] - client socket : "+socketClient);
						System.out.println("[06 Server Main] -  lista dei serverThread : "+i+"  : "+connectionList.get(0).getSocketClient());
						connectionList.get(connectionList.size() -1).setSocketClient(socketClient);
						System.out.println("[07 Server Main] - lista dell'ultimo serverThread dopo aer forzato SocketClient : "+i+"  : "+connectionList.get(connectionList.size() -1).getSocketClient());
					}


			/*		System.out.println("connection list completa : ");
					for(int i = 0; i<connectionList.size(); i++)
					{
						System.out.println("utenti connessi : "+connectionList.get(i).getName());
					}*/


					new Thread(() -> {
						// code goes here.
						Thread thread = new Thread(connectionList.get(connectionList.size() -1));
						System.out.println("[08 Server Main] -  lascio il compito a ServeThread con socket : "+socketClient);
						thread.start();
					}).start();

				
				//inizio gestione Thread		
			//	new Thread(new ServerThread(socketClient)).start();


	
				}
				 catch (IOException e) 
				{		
				e.printStackTrace();
				} 
			}
			
	}

	public void broadcast(String message) throws IOException {
		System.out.println("[09 Server Main] -  broadcast : "+socketClient);
		for (ServerThread clientConnection : this.connectionList) {
			System.out.println("[10 Server Main] -  broadcast : inviato a "+socketClient+"+  ServerThread : "+clientConnection.getSocketClient());
			clientConnection.sendMessage(message);
		}
	}
	
	public void diminuisciUtentiDiUno ()
	{
		numeroUtentiConnessi--;
		setNumeroUtentiConnessi(numeroUtentiConnessi);	
	}
				
	public void aumentaUtentiDiUno ()
	{
		numeroUtentiConnessi++;
		setNumeroUtentiConnessi(numeroUtentiConnessi);	
	}

	//setter and getter
	public int getNumeroUtentiConnessi()
	{
		return numeroUtentiConnessi;
	}

	public void setNumeroUtentiConnessi(int numeroUtentiConnessi)
	{
		this.numeroUtentiConnessi = numeroUtentiConnessi;
	}

	public static void main(String[] args) throws IOException, InterruptedException
    {
		Server s=new Server();
        s.attendiConnessione();
        
    }

}


