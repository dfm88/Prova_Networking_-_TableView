package sample;


import Bean.LoginBean;
import UnoProvaClientVecchio.fakeClient;

import UnoProvaClientVecchio.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogIN implements Initializable {

    private Main main;
    public static LoginBean logBean;


    @FXML
    private TextField loginText;

    static ControllerHome cH;
    @FXML
    void loginButtonClick(MouseEvent event) throws IOException {

        String username;

        if(loginText.getText().isBlank())
         username = "unknown";
        else
            username = loginText.getText();


        logBean = new LoginBean(username);


        Stage stage = new Stage();
        //mostro la pagina
        FXMLLoader loader = new FXMLLoader();



        Pane homePageLayout = loader.load( (getClass().getResource("./HOME.fxml").openStream()));

        stage.setScene(new Scene(homePageLayout));

        ControllerHome ccHH = (ControllerHome) loader.getController();

          ccHH.setModel(getC());

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("HomePage");




        //mostro lo stage Homepage
        stage.showAndWait();





    }

    @FXML
    void connettiServerButtono(MouseEvent event) throws IOException {
     //  c.connetti();


    }




    @FXML
    void checkButton(MouseEvent event) throws IOException {
      //  Client c = Client.getClientMaster();
      //  c.loginClient();

    }

    static ProvaController cP;

    @FXML
    void provaServerButton(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("./prova.fxml"));
        //    FXMLLoader loader = new FXMLLoader();
        //   loader.setLocation(Main.class.getResource("./HOME.fxml"));
        Pane homePageLayout = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(homePageLayout));
        cP = (ProvaController) loader.getController();
 //       cH = (ControllerHome)loader.getController();

        //Client c = Client.getClientMaster();
        //    cH.setModel(server);
 //       Client.getClientMaster().setController(cH);

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml


        //creo un nuovo stage per mostrare la nuova finestra Home


        //blocco l'accesso alla la primaryStage del Login
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("HomePage");




        //mostro lo stage Homepage
        stage.showAndWait();


    }

   /* @FXML
    void inviaCiaoAlClient(MouseEvent event) throws IOException, InterruptedException {

        c.triggeraServerperOttenereRisposta();

    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {






    }
    public static fakeClient c;

    public fakeClient getC() {
        return c;
    }

    public void setModel(fakeClient clientMaster) {
        this.c = clientMaster;
  //      loginText.textProperty().bind(c.numeroProperty().asString());
       

    }
}
