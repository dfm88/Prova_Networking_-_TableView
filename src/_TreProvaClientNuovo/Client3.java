package _TreProvaClientNuovo;



/*Limitations:
Although the above implementation of server manages to handle most of the scenarios,
there are some shortcomings in the approach defined above.

One clear observation from above programs is that if the number of clients grew large,
 the searching time would increase in the handler class. To avoid this increase, two
 hash maps can be used. One with name as the key, and index in active list as the value.
 Another with index as key, and associated handler object as value. This way, we can
 quickly look up the two hashmaps for matching recipient. It is left to the readers to
 implement this hack to increase efficiency of the implementation.
Another thing to notice is that this implementation doesn’t work well when users
disconnect from the server. A lot of errors would be thrown because disconnection is
not handled in this implementation. It can easily be implemented as in previous basic
TCP examples. It is also left for the reader to implement this feature in the program.*/


/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java implementation for multithreaded chat client
// Save file as Client.java
//https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
//https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/?ref=lbp
//https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/?ref=lbp
import java.io.*;
import java.net.*;
import java.util.Scanner;

// formato messaggi da scrivere

//      testo#client x      //// dove x è un intero che parte da 0 ed è attribuito ai client in ordine di entrata. (non manda in broadcast ma al singolo client




public class Client3
{
    public final static int ServerPort = 1234;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Client3() throws IOException {

        System.out.println("partito Client3");
        Scanner scn = new Scanner(System.in);


        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {
                    if(getMsg()!=null) {
                        // read the message to deliver.
                        //   String msg = scn.nextLine();
                        setMsg("testoAAA#client 0\"");
                        try {
                            // write on the output stream
                            dos.writeUTF(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }




    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner scn = new Scanner(System.in);

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();

                    try {
                        // write on the output stream
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }


}
