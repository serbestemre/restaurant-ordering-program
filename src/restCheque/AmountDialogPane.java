package restCheque;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class AmountDialogPane {

    @FXML
    private DialogPane amountDialogPane;

    @FXML
    private TextField tfAmount;

    private static String amountIngredients;

    public static String getAmountIngredients() {
        return amountIngredients;
    }

    public static void setAmountIngredients(String amountIngredients) {
        AmountDialogPane.amountIngredients = amountIngredients;
    }

    @FXML
    public void initialize(){

        tfAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setAmountIngredients(tfAmount.getText().trim().toString());

            }
        });

    }
}


