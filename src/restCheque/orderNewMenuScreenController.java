package restCheque;

import DataModel.Menu;
import DataModel.MenuIngredient;
import DataSource.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.SQLException;

public class orderNewMenuScreenController {

    @FXML
    private DialogPane menuDialogPane;

    @FXML
    private TableView<Menu> tableViewMenu;

    @FXML
    private TableView<MenuIngredient> tableViewIngredients;

    @FXML
    private ProgressBar progressBar;
    public static ObservableList<Menu> listofMenus= FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        getAllMenusAndProductsToSell();

    }

    @FXML
    public void getAllMenusAndProductsToSell(){
        Task<ObservableList<Menu>> taskGetAllProduct = new GetAllMenusAndProducts();
        try {

            listofMenus=FXCollections.observableArrayList(DataSource.getInstance().getAllMenusAndProductsThatCanSell());
            tableViewMenu.setItems(listofMenus);
        } catch (SQLException e) {
            System.out.println("tabloya productlar get edilemedi");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Could not get product to the table!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }
        new Thread(taskGetAllProduct).start();

        taskGetAllProduct.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(true);
                progressBar.setProgress(taskGetAllProduct.getProgress());
            }
        });

        taskGetAllProduct.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(false);
                tableViewMenu.refresh();
            }
        });

        taskGetAllProduct.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(false);
            }
        });


    }

    class GetAllMenusAndProducts extends Task {

        @Override
        protected Object call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().getAllMenusAndProductsThatCanSell());
        }
    }










}
