package restCheque;

import DataModel.Desk;
import DataSource.DataSource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;


public class DeleteTableController {
    @FXML
    private DialogPane dialogPaneDeleteTable;

    @FXML
    private ComboBox<Desk> cbTableList;

    ObservableList<Desk> existingTableList = FXCollections.observableArrayList();

    public static Desk selectedItem = new Desk(); // this item sends to the method which runs the delete sql query

    @FXML
    public void initialize(){
    existingTableList.setAll(DataSource.getInstance().getAllDeskInfo());
    cbTableList.setItems(existingTableList);
    if(existingTableList.size()!=0){
    cbTableList.getSelectionModel().selectFirst();
    selectedItem= cbTableList.getSelectionModel().getSelectedItem();
    }
    cbTableList.valueProperty().addListener(new ChangeListener<Desk>() {
        @Override
        public void changed(ObservableValue<? extends Desk> observable, Desk oldValue, Desk newValue) {
            selectedItem=newValue;
        }
    });

    }


}
