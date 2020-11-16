package TreProvaServerNuovo;


// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server3.java

import TreProvaClientNuovo.Client3;

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server3
{

    // Vector to store active clients
    static Vector<ClientHandler3> ar = new Vector<>();

    // counter for clients
    static int i = 0;

    public static void main(String[] args) throws IOException
    {
        System.out.println("server in ascolto sulla porta "+ Client3.ServerPort);
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(Client3.ServerPort);

        Socket s;

        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            s = ss.accept();

            System.out.println("New client request received : " + s);

            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            // Create a new handler object for handling this request.
            ClientHandler3 mtch = new ClientHandler3(s,"client " + i, dis, dos);

            // Create a new Thread with this object.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // add this client to active clients list
            ar.add(mtch);

            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }
    }
}
