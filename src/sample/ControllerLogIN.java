package sample;


import Bean.LoginBean;
import TreProvaClientNuovo.Client3;
import UnoProvaClientVecchio.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ControllerLogIN implements Initializable {

    private Main main;
    public static LoginBean logBean;
    Client3 c3;

    @FXML
    private TextField loginText;

    @FXML
    void loginButtonClick(MouseEvent event) throws IOException {

        String username;

        if(loginText.getText().isBlank())
         username = "unknown";
        else
            username = loginText.getText();


        logBean = new LoginBean(username);

        main = new Main();
        main.ShowHomePageView();

    }

    @FXML
    void connettiServerButtono(MouseEvent event) throws IOException {
        Client c = Client.getClientMaster();
        c.connetti();

    }




    @FXML
    void loginServerButton(MouseEvent event) throws IOException {
        Client c = Client.getClientMaster();
        c.loginClient();

    }


    @FXML
    void provaServerButton(MouseEvent event) throws IOException {

       Client c = Client.getClientMaster();
        c.provaClient();
      //  c3.setMsg("testo#client 0");



    }

    @FXML
    void inviaCiaoAlClient(MouseEvent event) throws IOException, InterruptedException {
        Client c = Client.getClientMaster();
        c.triggeraServerperOttenereRisposta();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
