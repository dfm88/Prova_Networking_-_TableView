package sample;

import Bean.UserBean;
import DAO.UserDAO;
import Util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerSearch {

    @FXML
    private TextField nomeRicerca;

    @FXML
    private TextField cognomeRicerca;

    @FXML
    private TableView<UserBean> tabellaRicerca;

    @FXML
    private TableColumn<UserBean, String> nomeColumnRic;

    @FXML
    private TableColumn<UserBean, String> cognomeColumnRic;

    Connection con;

    UserDAO userDao;

    @FXML
    void avviaRicerca(MouseEvent event) {

        nomeColumnRic.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cognomeColumnRic.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        ObservableList<UserBean> userListObservable = FXCollections.observableArrayList();

        try {
            con = DBConnection.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        userDao = new UserDAO(con);

        try {


            ArrayList<UserBean> userArrayList = (ArrayList<UserBean>) userDao.getUserFromDBByNameorSurname(nomeRicerca.getText(),cognomeRicerca.getText());




            for(int i=0; i<userArrayList.size();i++)
            {
                userListObservable.add(userArrayList.get(i));
            }

            tabellaRicerca.setItems(userListObservable);

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }



    }

}
