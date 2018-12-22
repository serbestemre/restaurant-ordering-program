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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Locale;

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
    private Button btnEdit;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDelete;

    @FXML
    private ProgressBar progressBar;


    ObservableList<Product> listofProducts =FXCollections.observableArrayList();


    public Product selectedProduct= new Product();
@FXML
    public void initialize(){
    getAllProducts();

    tableViewProduct.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            // Your action here
            if(tableViewProduct.getSelectionModel().getSelectedItem()!=null) {
                selectedProduct = (Product) newValue;
                int i =tableViewProduct.getItems().indexOf(selectedProduct);
                System.out.println("table index" +i);
                System.out.println("liste index " +listofProducts.indexOf(selectedProduct));
            }
        }
    });
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


                    tfName.clear();
                    tfCost.clear();
                    tfAmount.clear();
                }
            });

            taskAddNewCustomer.setOnRunning(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    progressBar.setProgress(taskAddNewCustomer.getProgress());
                    progressBar.setVisible(true);

                }
            });






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
    public void editProduct(){

    if (tableViewProduct.getSelectionModel().getSelectedItem()!=null){
        btnCancel.setVisible(true);
        btnUpdate.setVisible(true);

        btnCreate.setDisable(true);
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        Product editingProduct=selectedProduct;

        tfName.setText(editingProduct.getProductName());
        tfCost.setText(String.valueOf(editingProduct.getProductCost()));
        tfAmount.setText(String.valueOf(editingProduct.getProductAmount()));

    }else{
        System.out.println("ürün seçilmedi");
    }


}

@FXML
    public void btnCancel(){

    btnCancel.setVisible(false);
    btnUpdate.setVisible(false);

    btnCreate.setDisable(false);
    btnEdit.setDisable(false);
    btnDelete.setDisable(false);
}

@FXML
    public void deleteProduct(){

    if(tableViewProduct.getSelectionModel().getSelectedItem()!=null){
        Product deletingProduct =tableViewProduct.getSelectionModel().getSelectedItem();
        DataSource.getInstance().deleteProduct(deletingProduct.getProductID());
        listofProducts.remove(deletingProduct);
        tableViewProduct.refresh();
    }
}

@FXML
    public void btnUpdate(){
    try {
        Product editingProduct=selectedProduct;
        int amount = Integer.parseInt(tfAmount.getText().trim());
        double cost = Double.parseDouble(tfCost.getText().trim());
        //if we catch NumberFormatException that means user didn't enter valid input for those double variables !
        //warn the user with an Error Dialog
        if (!tfName.getText().trim().isEmpty() && tfName.getText() != null && tfAmount.getText().trim() != null && !tfAmount.getText().trim().isEmpty() && !tfCost.getText().trim().isEmpty() && tfCost.getText().trim() != null) {
            Product myNewProduct = new Product();
            editingProduct.setProductName(tfName.getText().trim().toUpperCase(Locale.ENGLISH));
            editingProduct.setProductCost(cost);
            editingProduct.setProductAmount(amount);
                Task<Boolean> taskUpdateCustomer = new Task() {
                    @Override
                    protected Object call() throws Exception {


                        return DataSource.getInstance().updateProduct(editingProduct);

                    }
                };

            new Thread(taskUpdateCustomer).start();
            taskUpdateCustomer.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    progressBar.setVisible(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Process Failed!!!");
                    alert.setContentText("Ooops, There was something wrong!\nThe product could not updated!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                }
            });


            taskUpdateCustomer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    int index = listofProducts.indexOf(selectedProduct);
                    listofProducts.remove(index);
                    listofProducts.add(index,editingProduct);
                    tableViewProduct.refresh();
                    progressBar.setVisible(false);

                    tfName.clear();
                    tfCost.clear();
                    tfAmount.clear();
                }
            });

            taskUpdateCustomer.setOnRunning(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    progressBar.setProgress(taskUpdateCustomer.getProgress());
                    progressBar.setVisible(true);

                }
            });


            tableViewProduct.refresh();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong or Invalid Attempt!");
            alert.setContentText("Ooops, ALL FIELDS MUST BE FILLED!\n");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();
        }

    }catch (NumberFormatException n){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Wrong or Invalid Attempt!");
        alert.setContentText("Ooops, It looks like you did NOT enter valid number!\n" +
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

