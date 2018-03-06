package content;


import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

/**
 * Created by Szwedzio on 06.03.2018.
 */
public class HomeContent extends AnchorPane{


    public HomeContent(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}