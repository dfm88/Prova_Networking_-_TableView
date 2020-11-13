package Util;

import Bean.UserBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBConnection {
   private static final String jdbcURL = "jdbc:sqlite:proveNetworkingDB.db";
  //  private static final String jdbcURL = "jdbc:sqlite:C:\\Users\\user\\Pictures\\aaa.db";

    //alternativamente il path diretto: "jdbc:sqlite:C:\\Users\\user\\Documents\\MY_Intellij_Projects\\Final_Project_Margoni_Diego\\Final_Project_Margoni_Diego.db"
    private static HikariDataSource ds;

    /**
     * Classe di connessione al db
     * @return l'oggetto Connection
     */
    public static Connection getConnection() throws ClassNotFoundException {


        if(ds == null)
        {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcURL);
            //usernaname e password non necessari
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtChacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            ds = new HikariDataSource(config);
        }

        try {
            return ds.getConnection();
        } catch (SQLException e) {
            System.err.println("Errore di connessione al db");
            throw  new RuntimeException(e);
        }


    }



    //MAIN PER PROVE
    public static void main(String[] args)  {

      //  String query = "SELECT * FROM user where isAdmin = ?";

        String query = "SELECT * FROM USERS";



        Connection con = null;
        try {
           con = DBConnection.getConnection();
           PreparedStatement st = con.prepareStatement(query);
         //  st.setBoolean(1, true); //seleziona solo gli admin
           ResultSet rs = st.executeQuery();

           UserBean user;

            while (rs.next())
            {
                System.out.println(rs.getString("ID"));
                System.out.println(rs.getString("Nome"));
                System.out.println(rs.getString("Cognome"));
                System.out.println();
            }


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        //    Class.forName("org.sqlite.JDBC");
        //Connection con = DriverManager.getConnection("jdbc:sqlite:Final_Project_Margoni_Diego.db");

       // Connection con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\user\\Documents\\MY_Intellij_Projects\\Final_Project_Margoni_Diego\\Final_Project_Margoni_Diego.db");


      //  String query2 = "INSERT into user (userName, isAdmin) VALUES('ofhhhh Shit', 1)";

     //   s.executeUpdate(query2);



    }

}
