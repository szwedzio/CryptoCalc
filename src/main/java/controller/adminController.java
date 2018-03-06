package controller;

/**
 * Created by Szwedzio on 06.03.2018.
 */
import content.OnlineContent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import content.HomeContent;


public class adminController {



    @FXML
    BorderPane mainPane;

    public void onBtnHomeClick(){
        HomeContent home = new HomeContent();
        mainPane.setCenter(home);
    }
    public void onBtnOnlineviewClick(){
        OnlineContent online = new OnlineContent();
        mainPane.setCenter(online);
    }

    @FXML
    public void initialize() {
        HomeContent start = new HomeContent();
        mainPane.setCenter(start);

    }
    }

