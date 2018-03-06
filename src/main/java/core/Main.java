package core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.peer.SystemTrayPeer;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedHashSet;

import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception {
        /// wyłączanie wszystkich wątków spowodowane użyciem BorderPane
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        Parent root = FXMLLoader.load(getClass().getResource("/views/admin.fxml"));
        primaryStage.setTitle("Konwerter walut");
        primaryStage.setScene(new Scene(root,  695.0,466.0));
        primaryStage.getScene().getStylesheets().add
                (getClass().getResource("/css/main.css").toExternalForm());
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
