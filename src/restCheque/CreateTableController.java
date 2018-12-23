package restCheque;

import DataSource.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Insert tag error!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }

    }
}
