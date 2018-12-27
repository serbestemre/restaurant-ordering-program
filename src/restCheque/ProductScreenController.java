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
import org.sqlite.SQLiteException;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;

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

    @FXML
    private RadioButton radioSell;

    @FXML
    private ToggleGroup priceChoice;

    @FXML
    private RadioButton radioDontSell;

    @FXML
    private TextField tfPrice;

    Product editingProduct;


    ObservableList<Product> listofProducts =FXCollections.observableArrayList();

    public Product selectedProduct= new Product();
@FXML
    public void initialize(){
    getAllProducts();

    priceChoice.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            if(radioSell.isSelected()){
                System.out.println("may sell selected");
                tfPrice.setDisable(false);
            }else{
                System.out.println("will not sell!");
                tfPrice.setDisable(true);
            }
        }
    });

    tableViewProduct.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            // Your action here
            if(tableViewProduct.getSelectionModel().getSelectedItem()!=null) {
                selectedProduct = (Product) newValue;

            }
        }
    });
}


@FXML
    public void createNewProduct(){
    Product myNewProduct = new Product();

    double price=0;
    boolean validationOk=false;

    /*********REFACTORRRRRRR PRICE AND GET VALID DOUBLE INPUT!!!******///////////////////
    if(radioSell.isSelected()){
        try {

            price=Double.parseDouble(tfPrice.getText().trim());
            myNewProduct.setProductPrice(price);
            validationOk=true;


        }catch (NumberFormatException e) {
            validationOk=false;
            //if price will not enter then its exception will get here
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong or Invalid Attempt!");
            alert.setContentText("Ooops, It Looks Like You Did not Enter Valid  For Price Field!\n" + "Careful with decimal format for COST\n" + "You must use '.'\nExample:Price=21.35\nExample:Price=2500 ");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }
    }else{
        myNewProduct.setProductPrice(0);
        validationOk=true;
    }
            if (validationOk==true){

                try {
                int amount = Integer.parseInt(tfAmount.getText().trim());
                double cost = Double.parseDouble(tfCost.getText().trim());


                //if we catch NumberFormatException that means user didn't enter valid input for those double variables !
                //warn the user with an Error Dialog
                if (!tfName.getText().trim().isEmpty() && tfName.getText() != null && tfAmount.getText().trim() != null && !tfAmount.getText().trim().isEmpty() && !tfCost.getText().trim().isEmpty() && tfCost.getText().trim() != null) {

                    myNewProduct.setProductName(tfName.getText().trim().toUpperCase());
                    myNewProduct.setProductCost(cost);
                    myNewProduct.setProductAmount(amount);
                    // normally user will not be able to enter 0 for price!

                    Task<Boolean> taskCreateNewProduct = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            return DataSource.getInstance().createNewProduct(myNewProduct);
                        }
                    };

                    new Thread(taskCreateNewProduct).start();

                    taskCreateNewProduct.setOnFailed(new EventHandler<WorkerStateEvent>() {
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

                    taskCreateNewProduct.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            System.out.println("başarılı şekilde eklendi");
                            listofProducts.add(myNewProduct);
                            tableViewProduct.refresh();
                            progressBar.setVisible(false);

                            tfName.clear();
                            tfCost.clear();
                            tfAmount.clear();
                            tfPrice.clear();

                        }
                    });

                    taskCreateNewProduct.setOnRunning(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            progressBar.setProgress(taskCreateNewProduct.getProgress());
                            progressBar.setVisible(true);

                        }
                    });


                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Wrong or Invalid Attempt!");
                    alert.setContentText("Ooops, ALL FIELDS MUST BE FILLED!\n");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();

                }
                            /////
            } catch (NumberFormatException a) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong or Invalid Attempt!");
                alert.setContentText("Ooops, It Looks Like You Did not Enter Valid Number!\n" + "Careful with decimal format for COST\n" + "You must use '.'\nExample:COST=21.35\nExample:STOCK=2500 ");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                alert.showAndWait();

            }

        }else{

                System.out.println("ürün satılabilir fakat fiyatı girilmedi ? kaydedilemedi hatalı giriş!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong or Invalid Attempt!");
                alert.setContentText("Ooops, It Looks Like You Did not Enter Valid  For Price Field!\n" + "Careful with decimal format for COST\n" + "You must use '.'\nExample:Price=21.35\nExample:Price=2500 ");
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

        if(tableViewProduct.getSelectionModel().getSelectedItem().getProductPrice()!=0){
            radioSell.setSelected(true);
            tfPrice.setDisable(false);
            tfPrice.setText(String.valueOf(selectedProduct.getProductPrice()));
        }


        editingProduct=selectedProduct;
        tfName.setText(editingProduct.getProductName());
        tfCost.setText(String.valueOf(editingProduct.getProductCost()));
        tfAmount.setText(String.valueOf(editingProduct.getProductAmount()));
    }else{
        System.out.println("ürün seçilmedi");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Choose an item!!!");
        alert.setContentText("Ooops, There was something wrong!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
        alert.showAndWait();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, You are doing something risky!");
        alert.setContentText("Are you sure to delete " + tableViewProduct.getSelectionModel().getSelectedItem().getProductName()+ " ?" );

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            Product deletingProduct =tableViewProduct.getSelectionModel().getSelectedItem();

            DataSource.getInstance().deleteProduct(deletingProduct.getProductID());
            listofProducts.remove(deletingProduct);
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Information Dialog");
            alert2.setHeaderText(null);
            alert2.setContentText("The product deleted successfully!");
            Stage stage = (Stage) alert2.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/checked.png").toString()));
            alert2.showAndWait();

            tableViewProduct.refresh();
        }
    }else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Select an item to deleteeeeeee!!!");
        alert.setContentText("Ooops, There was something wrong!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
        alert.showAndWait();
    }

}

@FXML
    public void btnUpdate(){
    Product myNewProduct = new Product();

    double price=0;
    boolean validationOk=false;

    /*********REFACTORRRRRRR PRICE AND GET VALID DOUBLE INPUT!!!******///////////////////
    if(radioSell.isSelected()){
        try {

            price=Double.parseDouble(tfPrice.getText().trim());
            myNewProduct.setProductPrice(price);
            validationOk=true;


        }catch (NumberFormatException e) {
            validationOk=false;
            //if price will not enter then its exception will get here
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong or Invalid Attempt!");
            alert.setContentText("Ooops, It Looks Like You Did not Enter Valid  For Price Field!\n" + "Careful with decimal format for COST\n" + "You must use '.'\nExample:Price=21.35\nExample:Price=2500 ");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }
    }else{
        myNewProduct.setProductPrice(0);
        validationOk=true;
    }
    if (validationOk==true){

        try {
            int amount = Integer.parseInt(tfAmount.getText().trim());
            double cost = Double.parseDouble(tfCost.getText().trim());


            //if we catch NumberFormatException that means user didn't enter valid input for those double variables !
            //warn the user with an Error Dialog
            if (!tfName.getText().trim().isEmpty() && tfName.getText() != null && tfAmount.getText().trim() != null && !tfAmount.getText().trim().isEmpty() && !tfCost.getText().trim().isEmpty() && tfCost.getText().trim() != null) {

                myNewProduct.setProductName(tfName.getText().trim().toUpperCase());
                myNewProduct.setProductCost(cost);
                myNewProduct.setProductAmount(amount);
                // normally user will not be able to enter 0 for price!

                Task<Boolean> taskUpdateMyProduct = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return DataSource.getInstance().createNewProduct(myNewProduct);
                    }
                };

                new Thread(taskUpdateMyProduct).start();

                taskUpdateMyProduct.setOnFailed(new EventHandler<WorkerStateEvent>() {
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

                taskUpdateMyProduct.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        int index = listofProducts.indexOf(editingProduct);
                        System.out.println("update edilen product index="+ index);
                        listofProducts.remove(index);
                        listofProducts.add(index,myNewProduct);

                        tableViewProduct.refresh();
                        progressBar.setVisible(false);

                        btnCancel.setVisible(false);
                        btnUpdate.setVisible(false);

                        btnCreate.setDisable(false);
                        btnEdit.setDisable(false);
                        btnDelete.setDisable(false);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("The product updated successfully!");
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image(this.getClass().getResource("/icons/checked.png").toString()));
                        alert.showAndWait();

                        tfName.clear();
                        tfCost.clear();
                        tfAmount.clear();
                        tfPrice.clear();

                    }
                });

                taskUpdateMyProduct.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        progressBar.setProgress(taskUpdateMyProduct.getProgress());
                        progressBar.setVisible(true);

                    }
                });


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong or Invalid Attempt!");
                alert.setContentText("Ooops, ALL FIELDS MUST BE FILLED!\n");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                alert.showAndWait();

            }
            /////
        } catch (NumberFormatException a) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong or Invalid Attempt!");
            alert.setContentText("Ooops, It Looks Like You Did not Enter Valid Number!\n" + "Careful with decimal format for COST\n" + "You must use '.'\nExample:COST=21.35\nExample:STOCK=2500 ");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }

    }else{

        System.out.println("ürün satılabilir fakat fiyatı girilmedi ? kaydedilemedi hatalı giriş!");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Wrong or Invalid Attempt!");
        alert.setContentText("Ooops, It Looks Like You Did not Enter Valid  For Price Field!\n" + "Careful with decimal format for COST\n" + "You must use '.'\nExample:Price=21.35\nExample:Price=2500 ");
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