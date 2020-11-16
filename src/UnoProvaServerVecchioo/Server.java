package UnoProvaServerVecchioo;

import provaNetwoking2.TaskClientConnection;

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
		System.out.println("[] - Inizializzo il Server");
		
		server = new ServerSocket(porta);//inizializzo il servizio
		
		System.out.println("[] - Server in ascolto sulla porta " + porta);
				
			while(true)
			{
				try
				{
					
				socketClient = server.accept(); //il server si mette in ascolto sulla porta data
												//la funzione server.accept() resttuisce una variabile socket
												//che in questo caso è quella del client
				
				System.out.println("[] - Connessione stabilita con il client");
			
				
				System.out.println("[] - Elenco abilitati :");

				ServerThread servertThread = new ServerThread(socketClient, this);
				connectionList.add(servertThread);

					System.out.println("connection list completa : ");
					for(int i = 0; i<connectionList.size(); i++)
					{
						System.out.println(connectionList.get(i).getName());
					}
				Thread thread = new Thread(servertThread);
				thread.start();

				
				//inizio gestione Thread		
			//	new Thread(new ServerThread(socketClient)).start();


	
				}
				 catch (IOException e) 
				{		
				e.printStackTrace();
				} 
			}
			
	}

	public void broadcast(String message) {
		System.out.println("chiamato metodo broadcast di server");
		for (ServerThread clientConnection : this.connectionList) {
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


