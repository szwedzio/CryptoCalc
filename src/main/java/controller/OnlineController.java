package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import model.pairs;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Szwedzio on 06.03.2018.
 */
public class OnlineController {
    String ab;
    String ba;
    JSONObject json;
    JSONArray jsonarray;
    @FXML
    TableView tableview;
    @FXML
    Label lastupdate;

    public static JSONArray readJsonFromUrl(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONArray myResponse = new JSONArray((response.toString()));
        return myResponse;
    }


    public   ObservableList<pairs> getdata() throws IOException {
        ObservableList<pairs> values = FXCollections.observableArrayList();
        jsonarray = readJsonFromUrl("https://api.abucoins.com/products/ticker");
        pairs addthis = new pairs();

        for (Object o : jsonarray) {
            if (o instanceof JSONObject) {
                addthis=new pairs();
                addthis.setProduct_id(((JSONObject) o).getString("product_id"));
                addthis.setPrice(((JSONObject) o).getBigDecimal("price"));
                addthis.setTrade_id(((JSONObject) o).getString("trade_id"));
                values.add(addthis);
            }
        }
        return values;
    }
    public void initialize() throws IOException {

            ObservableList<pairs> values = getdata();
            List<String> columnProperty = new ArrayList<>();

            columnProperty.add("product_id");
            columnProperty.add("price");
            columnProperty.add("trade_id");

            List<String> columnName = new ArrayList<>();

            columnName.add("Pair");
            columnName.add("Price");
            columnName.add("Trade ID");

            for (int i = 0; i < columnProperty.size(); i++) {
                TableColumn<ObservableList<pairs>, String> column = new TableColumn<>(columnName.get(i));
                if ( i==1) {
                    column.prefWidthProperty().bind(tableview.widthProperty().multiply(0.6));
                } else if (i == 2) {
                    column.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));
                }  else
                    column.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));

                column.setResizable(false);
                column.setCellValueFactory(new PropertyValueFactory<>(columnProperty.get(i)));
                tableview.getColumns().add(column);
            }
            tableview.setItems(values);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {

                        ObservableList<pairs> values = getdata();
                        tableview.setItems(values);
                        lastupdate.setText(((JSONObject) jsonarray.get(0)).getString("time"));
                    }
                    ///problem z pobraniem danych/połączeniem
                    catch (UnknownHostException e) {
                        System.out.println(e);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                });

            }
        }, 0, 2000);
    }
}
