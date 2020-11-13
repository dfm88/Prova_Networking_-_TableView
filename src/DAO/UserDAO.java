package DAO;


import Bean.UserBean;
import Util.DBConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection con;

    /**
     * Costruttore DAO di user
     * @param connection
     */
    public UserDAO(Connection connection)
    {
        this.con = connection;
    }

    public List<UserBean> getUserFromDB() throws SQLException, ClassNotFoundException {

        if(con.isClosed())
        {
            con = DBConnection.getConnection();
        }

        String query = "SELECT * FROM USERS";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {

            List<UserBean> listaUsers = new ArrayList<UserBean>();

            UserBean user;

            try (ResultSet result = pstatement.executeQuery())
            {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {

                    while(result.next()) {

                        int id = result.getInt("ID");
                        String nome = result.getString("Nome");
                        String cognome = result.getString("Cognome");


                        user = new UserBean(id, nome, cognome);

                        listaUsers.add(user);


                    }

                    result.close();
                    con.close();
                    return listaUsers;
                }
            }
        } catch (SQLException e)
        {
            System.err.println("Errore di connessione al  nella classe userDAO");
            throw  new RuntimeException(e);
        }

    }

    public List<UserBean> getUserFromDBByNameorSurname(String name, String surname) throws SQLException, ClassNotFoundException {

        if (con.isClosed()) {
            con = DBConnection.getConnection();
        }

        String query = "SELECT * FROM USERS WHERE cognome like ? and nome like ?;";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {

            if(name.isBlank())
                name = "";
            if(surname.isBlank())
                surname= "";

            pstatement.setString(1, "%"+surname+"%");
            pstatement.setString(2, "%"+name+"%");


            List<UserBean> listaUsers = new ArrayList<UserBean>();

            UserBean user;

            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {

                    while (result.next()) {


                        int id = result.getInt("ID");
                        String nome = result.getString("Nome");
                        String cognome = result.getString("Cognome");


                        user = new UserBean(id, nome, cognome);

                        listaUsers.add(user);


                    }

                    result.close();
                    con.close();
                    return listaUsers;
                }


            } catch (SQLException e) {
                System.err.println("Errore di connessione al  nella classe userDAO");
                throw new RuntimeException(e);
            }

        }

    }

    public void addUserInDB(String nome, String cognome) throws SQLException, ClassNotFoundException {

        if(con.isClosed())
        {
            con = DBConnection.getConnection();
        }

        String query = "INSERT INTO USERS (Cognome, Nome) VALUES (?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {

            pstatement.setString(1, cognome);
            pstatement.setString(2, nome);

            pstatement.executeUpdate();

            System.out.println("Nell'userDAO ho eseguito la query inserendo "+nome+" - "+cognome);

        } catch (SQLException e)
        {
            System.err.println("Errore di connessione al  nella classe userDAO");
            throw  new RuntimeException(e);
        }

    }



}
