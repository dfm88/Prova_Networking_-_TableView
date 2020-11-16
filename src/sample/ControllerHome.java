package sample;

import Bean.UserBean;
import DAO.UserDAO;
import UnoProvaClientVecchio.Client;
import Util.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerHome implements Initializable {

    private UserDAO userDao;
    private Connection con;
    ObservableList<UserBean> userListObservable = FXCollections.observableArrayList();

    @FXML
    private TableView<UserBean> tabella;

    @FXML
    private TableColumn<UserBean, String> nomeColumn;

    @FXML
    private TableColumn<UserBean, String> cognomeColumn;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField cognomeTextField;

    @FXML
    private Text intestazioneText;

    @FXML
    void apriRicerca(MouseEvent event) throws IOException {

        Main main = new Main();
        main.ShowSearchView();

    }

    @FXML
    void addUser(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {

        Client.getClientMaster().aggiungiUserinTabella(cognomeTextField.getText(), nomeTextField.getText());
     //   userDao.addUserInDB(cognomeTextField.getText(), nomeTextField.getText());
      //  tabella.getItems().clear();
  //      popolaTable();
        partiTimer();

    }

    @FXML
    void refreshTableClick(MouseEvent event) {

        intestazioneText.setText("Client : "+ControllerLogIN.logBean.getUsername());

        tabella.getItems().clear();
    //    popolaTable();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        popolaTable();

        partiTimer();


    }

    public void partiTimer()
    {
       Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        tabella.getItems().clear();
                        popolaTable();
                    }
                });



            }
        }, 0, 1000);
/*
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        // do stuff


                        new Thread(() -> {
                            // code goes here.
                            tabella.getItems().clear();
                            popolaTable();
                        }).start();

                    }
                }
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();*/
    }

    public void popolaTable()
    {
        intestazioneText.setText("Client : "+ControllerLogIN.logBean.getUsername());


        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        try {
            con = DBConnection.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        userDao = new UserDAO(con);

        try {

//            ArrayList<UserBean> userArrayList = (ArrayList<UserBean>) userDao.getUserFromDB();

            ArrayList<UserBean> userArrayList = Client.getClientMaster().aggiornaTabella();

            con.close();




            for(int i=0; i<userArrayList.size();i++)
            {
                userListObservable.add(userArrayList.get(i));
            }

            tabella.setItems(userListObservable);

        } catch (IOException | SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
