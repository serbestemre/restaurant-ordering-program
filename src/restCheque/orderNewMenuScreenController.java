package restCheque;

import DataModel.Menu;
import DataModel.MenuIngredient;
import DataSource.DataSource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

    public static Menu selectedOrder = new Menu();



    @FXML
    private ProgressBar progressBar;
    public static ObservableList<Menu> listofMenus= FXCollections.observableArrayList();
    public static ObservableList<MenuIngredient> listOfIngredients= FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        getAllMenusAndProductsToSell();

        tableViewMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedOrder=newSelection;
                getSelectedMenuIngredients(newSelection);
            }
        });


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

    @FXML
    public void getSelectedMenuIngredients(Menu selectedMenu){
        Task<ObservableList<MenuIngredient>> taskGetSelectedMenuIngredients = new GetSelectedMenuIngredients();
        try {

            listOfIngredients.setAll(DataSource.getInstance().getIngredientsOfSelectedMenu(selectedMenu));
            tableViewIngredients.setItems(listOfIngredients);
        } catch (SQLException e) {
            System.out.println("tabloya ingredientslar get edilemedi");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Database");
            alert.setHeaderText("Ingredients could not load!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();
        }
        new Thread(taskGetSelectedMenuIngredients).start();

        taskGetSelectedMenuIngredients.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(true);
                progressBar.setProgress(taskGetSelectedMenuIngredients.getProgress());
            }
        });

        taskGetSelectedMenuIngredients.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(false);
                tableViewMenu.refresh();
            }
        });

        taskGetSelectedMenuIngredients.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(false);
            }
        });


    }

    class GetSelectedMenuIngredients extends Task{
        @Override
        protected Object call() throws Exception {
            // System.out.println("---- Guiden giden selected menuID>>>" + selectedMenu.getMenuID());
            return FXCollections.observableArrayList(DataSource.getInstance().getIngredientsOfSelectedMenu(selectedOrder));
        }
    }



}
