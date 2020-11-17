package sample;

import UnoProvaClientVecchio.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static ControllerHome cH;
    public static ControllerLogIN cL;
    public static ProvaController cP;


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./LogIN.fxml"));
        Pane root = loader.load();
        Scene scene = new Scene (root);

        cL = (ControllerLogIN)loader.getController();
        cL.setModel(Client.getClientMaster());



        primaryStage.setTitle("Hello World");

/*
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("./HOME.fxml"));
        Pane root2 = loader2.load();

      //  loader.setLocation(Main.class.getResource("./HOME.fxml"));
        cH = (ControllerHome)loader2.getController();
        Client.getClientMaster().setController(cH);*/

        //definisco il controller e ne setto il modello

       /* cS.setModel(server);
        server.setController(cS);*/

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**************   ShowPROVAView **********************************/
    public  void ShowProvaView() throws IOException {

        //creo un Loader per caricare questo layout nella mia classe Main
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./prova.fxml"));
        //    FXMLLoader loader = new FXMLLoader();
        //   loader.setLocation(Main.class.getResource("./HOME.fxml"));
        Pane homePageLayout = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(homePageLayout));
        cP = (ProvaController) loader.getController();

        //Client c = Client.getClientMaster();
        //    cH.setModel(server);
    //    Client.getClientMaster().setController(cH);

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml


        //creo un nuovo stage per mostrare la nuova finestra Home


        //blocco l'accesso alla la primaryStage del Login
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("HomePage");




        //mostro lo stage Homepage
        stage.show();

    }


    /**************   ShowHomePageView **********************************/
    public  void ShowHomePageView() throws IOException {

        //creo un Loader per caricare questo layout nella mia classe Main
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./HOME.fxml"));
    //    FXMLLoader loader = new FXMLLoader();
     //   loader.setLocation(Main.class.getResource("./HOME.fxml"));
        Pane homePageLayout = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(homePageLayout));
        cH = (ControllerHome)loader.getController();

 //Client c = Client.getClientMaster();
    //    cH.setModel(server);
        Client.getClientMaster().setController(cH);

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml


        //creo un nuovo stage per mostrare la nuova finestra Home


        //blocco l'accesso alla la primaryStage del Login
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("HomePage");




        //mostro lo stage Homepage
        stage.show();

    }

    /**************   ShowSearch **********************************/
    public  void ShowSearchView() throws IOException {

        //creo un Loader per caricare questo layout nella mia classe Main
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("./search.fxml"));

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml
        Pane homePageLayout = loader.load();

        //creo un nuovo stage per mostrare la nuova finestra Home
        Stage stage = new Stage();
        stage.setScene(new Scene(homePageLayout));

        //blocco l'accesso alla la primaryStage del Login
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("Ricerca");




        //mostro lo stage Homepage
        stage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
