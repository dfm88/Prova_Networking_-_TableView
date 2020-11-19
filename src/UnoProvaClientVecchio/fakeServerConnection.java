package UnoProvaClientVecchio;


import Bean.UserBean;
import DAO.UserDAO;
import UnoCommandPattern.GestoreComandi;
import UnoCommandPattern.addUser;
import UnoCommandPattern.updateTable;
import UnoProvaServerVecchioo.createCommand;
import Util.DBConnection;
import Util.JsonUtil;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fakeServerConnection extends Thread {

    Socket socket;
    fakeServer server;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;
    private static Map<String, Runnable> comandi = new HashMap<String, Runnable>();
    String jsonDaClient;

    //costruttore
    public fakeServerConnection(Socket socket, fakeServer server) {
        super("Server Connection Threads"); // chiamo il costruttore della classe Thread e gli assegno un nome
        this.socket = socket;
        this.server = server;
        compilaHashMap();

    }

    public void sendStringToAllClient (String text) {


        for (int index = 0; index < server.connections.size(); index++)
        {
            //itero per tutti i client e chiamo il metodo send String to client
            fakeServerConnection sc = server.connections.get(index);
            System.out.println(server.connections.get(index).socket);
            sc.sendStringToClient(text);
        }
    }

    public void sendStringToClient (String text) {
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void run() {

        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());


            while (shouldRun) {

                //legge la stringa inviata dal client
                jsonDaClient = din.readUTF();

                String comando = JsonUtil.getComandoDaJson(jsonDaClient);

                gestoreCom.eseguiComando(comando, jsonDaClient);


               /* if(comando.equals("update"))
                {
                    update();
                } else if (comando.equals("aggiungiUser"))
                {
                    addUser();
                }*/

/*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // code goes here.
                        String comando = JsonUtil.getComandoDaJson(jsonDaClient);
                        comandi.get(comando).run();
                    }
                }).start();*/

            }

            din.close();
            dout.close();
            socket.close();

        } catch ( IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

   /* public void compilaHashMap()
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
    }*/

    GestoreComandi gestoreCom;
    public void compilaHashMap()
    {
        gestoreCom = new GestoreComandi(server);
        gestoreCom.addComando("aggiungiUser", new addUser(this));
        gestoreCom.addComando("update", new updateTable(this));


    }


    private void addUser() throws SQLException, ClassNotFoundException, IOException {

        //spacchetto il json per ottenere username e psw
        System.out.println("[07 Server Thread - addUser() inizio : **" + socket.getPort()+"** Client "+ Client.getClientMaster());

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

        System.out.println("[06 Server Thread - update() inizio : **" + socket.getPort()+"** Client "+ Client.getClientMaster());

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

/*
        new Thread(() -> {
            // code goes here.
            run();
        }).start();*/

    }





    public static void main(String[] args) {


    }

}
