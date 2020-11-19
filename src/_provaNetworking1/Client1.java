package _provaNetworking1;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {

    String clientName;
    ClientConnection cc;

    //costruttore
    public Client1() throws InterruptedException {
        Scanner console = new Scanner(System.in);
        System.out.println("inserisci il tuo nome : ");
        String input = console.nextLine();
        this.clientName = input;

        try {
            Socket s = new Socket("localhost", 3333);
            System.out.println();
            cc = new ClientConnection(s, this);
            cc.start();


            listenForInput();




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void provaStampa() {
        System.out.println("prova stampa");
    }

    //metodo che ascolta l'iput del cliente
    public void listenForInput() throws InterruptedException {
        //leggo l'input da tastiera
        Scanner console = new Scanner(System.in);


        /*aspetto l'input del cliente*/
        while(true) {
            while(!console.hasNextLine()){ //blocca il while esterno che ascolata l'input dell'utente, se l'utente non sta già digitando
                Thread.sleep(1);
            }

            //salvo l'input dell'utente in una stringa
            String input = console.nextLine();

            if(input.toLowerCase().equals("fanculo")) {
                cc.sendStringToServer(clientName+" MI HA MANDATO A FANCULO : "+input);
            }

            //do la possibilità di finire la comunicazione
            if(input.toLowerCase().equals("quit")) {
                break;
            }


            cc.sendStringToServer(clientName+" ha scritto : "+input);

        }

        cc.close();




    }



    public static void main(String[] args) throws InterruptedException {
        new Client1();


    }

}
