package restCheque;

import DataModel.Product;
import DataSource.DataSource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public class IngredientsController {

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private ProgressBar progressBar;

    ObservableList<Product> listofProducts =FXCollections.observableArrayList();
    private static Product selectedProduct = new Product();


    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    public static void setSelectedProduct(Product selectedProduct) {
        IngredientsController.selectedProduct = selectedProduct;
    }

    @FXML
    public void initialize(){
        getAllProducts();

        tableViewProduct.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // Your action here
                setSelectedProduct(tableViewProduct.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    public void getAllProducts(){
        Task<ObservableList<Product>> taskGetAllProduct = new GetAllProducts();
        try {

            listofProducts= FXCollections.observableArrayList(DataSource.getInstance().getAllProducts());
            tableViewProduct.setItems(listofProducts);
        } catch (SQLException e) {
            System.out.println("tabloya ingridientslar get edilemedi");
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
                tableViewProduct.refresh();
            }
        });

        taskGetAllProduct.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(false);
            }
        });


    }

    class GetAllProducts extends Task{

        @Override
        protected Object call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().getAllProducts());
        }
    }


}