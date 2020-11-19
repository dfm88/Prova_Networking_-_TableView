package UnoCommandPattern;

import Bean.UserBean;
import DAO.UserDAO;
import UnoProvaClientVecchio.Client;
import UnoProvaClientVecchio.fakeServer;
import UnoProvaClientVecchio.fakeServerConnection;
import Util.DBConnection;
import Util.JsonUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class updateTable implements Comando{

    fakeServerConnection serverConn;

    public updateTable(fakeServerConnection serv)
    {
        this.serverConn = serv;
    }

    @Override
    public void execute(String json) throws SQLException, ClassNotFoundException {




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

       // out.writeUTF(jsonUser);
        serverConn.sendStringToAllClient(jsonUser);

        //	out.flush();



        //in.close();
        //out.close();
        con.close();


    }
}
