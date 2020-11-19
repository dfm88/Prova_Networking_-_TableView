package UnoCommandPattern;

import Bean.UserBean;
import DAO.UserDAO;
import UnoProvaClientVecchio.Client;
import UnoProvaClientVecchio.fakeServerConnection;
import UnoProvaServerVecchioo.createCommand;
import Util.DBConnection;
import Util.JsonUtil;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;

public class addUser implements Comando {
    fakeServerConnection serverConn;

    public addUser(fakeServerConnection serv)
    {
        this.serverConn = serv;
    }


    @Override
    public void execute(String json) throws SQLException, ClassNotFoundException {
        //spacchetto il json per ottenere username e psw

        Gson gson = new Gson();

        System.out.println("[08 Server Thread - addUser() json dal cliente : "+json);

        createCommand gc = gson.fromJson(json, createCommand.class);
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

        new updateTable(serverConn).execute(null);


    }
}
