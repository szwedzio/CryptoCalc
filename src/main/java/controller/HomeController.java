package controller;

import core.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Szwedzio on 06.03.2018.
 */
public class HomeController {
    String ab;
    String ba;
    JSONObject json;
    JSONArray jsonarray;
    @FXML
    ChoiceBox<Object> currencyA;
    @FXML
    ChoiceBox<Object> currencyB;
    @FXML
    Label lastupdate;
    @FXML
    TextField amount;
    @FXML
    TextField result;
    public void Convert(ActionEvent event) {
        boolean flag = false;
        int value = 0;
        try {
            String a = currencyA.getSelectionModel().getSelectedItem().toString();
            String b = currencyB.getSelectionModel().getSelectedItem().toString();
            ab = a + "-" + b;
            ba = b + "-" + a;
            BigDecimal amountBD = new BigDecimal((amount.getText()));
            BigDecimal btc = BigDecimal.ZERO;
            if (amountBD.compareTo(BigDecimal.ZERO) <= 0)
                throw new NegativeArraySizeException();

            for (int i = 0; i < jsonarray.length(); i++) {
                json = (JSONObject) jsonarray.get(i);
                //CZY JEST BEZPOSREDNIE
                if (json.getString("product_id").equals(ab) || json.getString("product_id").equals(ba)) {
                    flag = true;
                    value = i;
                    break;
                }
            }

            //BEZPOSREDNIE LICZENIE
            if (flag) {
                json = (JSONObject) jsonarray.get(value);
                if (json.getString("product_id").equals(ab))
                   result.setText(amountBD+" "+a+" => "+json.getBigDecimal("price").multiply(amountBD) + " " + b);
                else
                    result.setText(amountBD+" "+a+" => "+BigDecimal.ONE.divide(json.getBigDecimal("price"), 50, RoundingMode.HALF_DOWN).multiply(amountBD) + " " + b);
                flag = false;
            } else {
                //POSREDNIE LICZENIE (biorąc pod uwagę, że każdą parę walut można obliczyć poprzez konwercje A -> BTC, BTC-> B
                //Jeżeli brak zostanie wypisany w komórce result tekst "Brak konwercji z BTC"
                for (int i = 0; i < jsonarray.length(); i++) {
                    json = (JSONObject) jsonarray.get(i);
                    ab = a + "-BTC";
                    ba = "BTC-" + a;
                    if (json.getString("product_id").equals(ab)) {
                        btc = json.getBigDecimal("price").multiply(amountBD);
                    } else if (json.getString("product_id").equals(ba)) {
                        btc = BigDecimal.ONE.divide(json.getBigDecimal("price"), 50, RoundingMode.HALF_DOWN).multiply(amountBD);
                    }
                }
                if (btc.compareTo(BigDecimal.ZERO) > 0) {
                    ab = "BTC-" + b;
                    ba = b + "-BTC";
                    for (int i = 0; i < jsonarray.length(); i++) {
                        json = (JSONObject) jsonarray.get(i);
                        if (json.getString("product_id").equals(ab)) {
                            result.setText(amountBD+" "+a+" => "+json.getBigDecimal("price").multiply(btc) + " " + b);
                            break;
                        } else if (json.getString("product_id").equals(ba)) {
                            result.setText(amountBD+" "+a+" => "+BigDecimal.ONE.divide(json.getBigDecimal("price"), 50, RoundingMode.HALF_DOWN).multiply(btc) + " " + b);
                            break;
                        }
                    }
                    ///if flaga która da nam brak przewalutowania z BTC-> B
                } else {
                    result.setText("Brak konwersji z "+ a+" na BTC");
                }
            }

            ////Niepoprawne dane w choiseboxach lub amount
        } catch (NegativeArraySizeException e)
        {
            System.out.println(e + "Value <=0 ");
            result.setText("Amount must be greater than 0");
        } catch (RuntimeException e) {
            System.out.print(e);
            result.setText("Invalid data in currency A,currency B or amount is empty");
        }

    }




    public void initialize() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {

                        jsonarray = Main.readJsonFromUrl("https://api.abucoins.com/products/ticker");
                        ///Aktualizacaja nazw walut
                        LinkedHashSet<String> names = new LinkedHashSet<String>();

                        for (Object o : jsonarray) {
                            if (o instanceof JSONObject) {
                                names.add(((JSONObject) o).getString("product_id").split("-")[0]);
                                names.add(((JSONObject) o).getString("product_id").split("-")[1]);
                            }
                        }

                        ObservableList<Object> nameslist = FXCollections.observableList(Arrays.asList(names.toArray()));
                        if (!currencyA.getItems().equals(nameslist)) {
                            currencyA.setItems(nameslist);
                            currencyB.setItems(nameslist);
                        }
                        ///KONIEC Aktualizacaja nazw walut

                        ///Informacja o ostatniej aktualizacji danych
                        lastupdate.setText(((JSONObject) jsonarray.get(0)).getString("time"));

                        ///KONIEC Informacja o ostatniej aktualizacji danych
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
