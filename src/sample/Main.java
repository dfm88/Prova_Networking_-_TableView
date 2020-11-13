package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LogIN.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    /**************   ShowHomePageView **********************************/
    public  void ShowHomePageView() throws IOException {

        //creo un Loader per caricare questo layout nella mia classe Main
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("./HOME.fxml"));

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml
        Pane homePageLayout = loader.load();

        //creo un nuovo stage per mostrare la nuova finestra Home
        Stage stage = new Stage();
        stage.setScene(new Scene(homePageLayout));

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
