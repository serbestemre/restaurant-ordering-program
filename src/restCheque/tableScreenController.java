package restCheque;

import DataModel.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class tableScreenController {

    @FXML
    private DialogPane dialogPaneTable;

    @FXML
    private TableView<Menu> tableViewTableMenuList;

    @FXML
    private Button btnAddOrder;

    @FXML
    private Button btnDeleteOrder;

    @FXML
    private Button btnPayment;

    @FXML
    private Label btnTotalCost;

    @FXML
    private Label labelTableName;

    ObservableList<Menu> myTable = FXCollections.observableArrayList();
    @FXML
    public void initialize(){

        labelTableName.setText(MainScreenController.selectedDesk.getTag());
        myTable=MainScreenController.tablesMenus.get(MainScreenController.tableIndex);
        tableViewTableMenuList.setItems(myTable);

    }
}
