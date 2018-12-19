package restCheque;

import DataSource.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class CreateTableController {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField tfTableTag;



    @FXML
    public void initialize(){



    }


    @FXML
    public void callCreateNewTable(){


        try {
            DataSource.getInstance().createNewTable(tfTableTag.getText().toString().toUpperCase().trim());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insert tag başarısız");
        }

    }
}
