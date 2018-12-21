package restCheque;

import DataModel.Product;
import DataSource.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class ProductScreenController {

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfCost;

    @FXML
    private TextField tfAmount;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btrnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private ProgressBar progressBar;

    @FXML
    ObservableList<Product> listofProducts =FXCollections.observableArrayList();

@FXML
    public void initialize(){
getAllProducts();


}

@FXML
    public void createNewProduct(){

    try{

        int amount = Integer.parseInt(tfAmount.getText().trim());
        double cost = Double.parseDouble(tfCost.getText().trim());
        //if we catch NumberFormatException that means user didn't enter valid input for those double variables !
        //warn the user with an Error Dialog
        if(!tfName.getText().trim().isEmpty() && tfName.getText()!=null && tfAmount.getText().trim()!=null && !tfAmount.getText().trim().isEmpty() && !tfCost.getText().trim().isEmpty() && tfCost.getText().trim()!=null){

            Product myNewProduct = new Product();

            myNewProduct.setProductName(tfName.getText().trim().toUpperCase());
            myNewProduct.setProductCost(cost);
            myNewProduct.setProductAmount(amount);

            Task<Boolean> taskAddNewCustomer = new Task() {
                @Override
                protected Object call() throws Exception {


                    return DataSource.getInstance().createNewProduct(myNewProduct);

                }
            };
            new Thread(taskAddNewCustomer).start();
            taskAddNewCustomer.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    progressBar.setVisible(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Process Failed!!!");
                    alert.setContentText("Ooops, There was something wrong!\nThe new product did not add into product database!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                }
            });


            taskAddNewCustomer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println("başarılı şekilde eklendi");
                    listofProducts.add(myNewProduct);
                    tableViewProduct.refresh();
                    progressBar.setVisible(false);
                }
            });

            taskAddNewCustomer.setOnRunning(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    progressBar.setProgress(taskAddNewCustomer.getProgress());
                    progressBar.setVisible(true);

                }
            });





            tfName.clear();
            tfCost.clear();
            tfAmount.clear();

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong or Invalid Attempt!");
            alert.setContentText("Ooops, ALL FIELDS MUST BE FILLED!\n");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }

    }catch (NumberFormatException e){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Wrong or Invalid Attempt!");
        alert.setContentText("Ooops, It Looks Like You Did not Enter Valid Number!\n" +
                "Careful with decimal format\n" +
                "You must use '.' Example: 21.35 ");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
        alert.showAndWait();

    }



}

@FXML
    public void getAllProducts(){
    Task<ObservableList<Product>> taskGetAllProduct = new GetAllProducts();
    try {

        listofProducts=FXCollections.observableArrayList(DataSource.getInstance().getAllProducts());
        tableViewProduct.setItems(listofProducts);
    } catch (SQLException e) {
        System.out.println("tabloya productlar get edilemedi");
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

