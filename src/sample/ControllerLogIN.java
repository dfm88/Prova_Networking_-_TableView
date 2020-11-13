package sample;


import Bean.LoginBean;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ControllerLogIN {

    private Main main;
    public static LoginBean logBean;

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
    void offServerButtonClick(MouseEvent event) {

    }

    @FXML
    void onServerButtonClick(MouseEvent event) {

    }

}
