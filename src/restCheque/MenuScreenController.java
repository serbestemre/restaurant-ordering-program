package restCheque;

import DataModel.Menu;
import DataModel.Product;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class MenuScreenController {

    @FXML
    private DialogPane menuDialogPane;

    @FXML
    private TextField tfMenuName;

    @FXML
    private TextField tfPrice;

    @FXML
    private TextField tfVat;

    @FXML
    private TextField tfCost;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button btnDefineIngridients;

    @FXML
    private Button btnDeleteIngridients;

    @FXML
    private Button btnCreateMenu;

    @FXML
    private Label labelTotalCost;

    @FXML
    private TableView<Product> tableViewIngridients;

    @FXML
    private TableView<Menu> tableViewMenu;



    ObservableList<Product> listInredients= FXCollections.observableArrayList();
    ObservableList<Menu> listMenu= FXCollections.observableArrayList();
    private double totalCost=0;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @FXML
    public void initialize(){
        getAllMenus();
    tableViewIngridients.setItems(listInredients);

    tableViewMenu.setItems(listMenu);

       listInredients.addListener(new ListChangeListener<Product>() {
           @Override
           public void onChanged(Change<? extends Product> c) {
               totalCost=0;
               for(int i =0;i<listInredients.size();i++){
                totalCost+=listInredients.get(i).getProductCost();
               }
               labelTotalCost.setText(String.valueOf(totalCost));
               tfCost.setText(String.valueOf(totalCost));

           }
       });

    }

    @FXML
    public void showIngredientsDialogPane(){
        try {
            Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
            dialog2.initOwner(menuDialogPane.getScene().getWindow());
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ingredientsScreen.fxml"));
            Parent dialogContent = null;
            dialogContent = fxmlLoader.load();
            dialog2.getDialogPane().setContent(dialogContent);
            dialog2.setTitle("Add Ingredients");
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            //sonuc nesnesini gönderir
            Optional<ButtonType> result =dialog2.showAndWait();
            if (result.get() == ButtonType.OK){
                System.out.println("seçilen ing = " + IngredientsController.getSelectedProduct().getProductName());


/////////////////////////amount start
                boolean dialogLoop=true;
                while (dialogLoop){
                try {
                    Dialog<ButtonType> dialog3 = new Dialog<ButtonType>();
                    dialog3.initOwner(menuDialogPane.getScene().getWindow());
                    FXMLLoader fxmlLoader2 = new FXMLLoader();
                    fxmlLoader2.setLocation(getClass().getResource("amountScreen.fxml"));
                    Parent dialogContent2 = null;
                    dialogContent2 = fxmlLoader2.load();
                    dialog3.getDialogPane().setContent(dialogContent2);
                    dialog3.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    dialog3.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                    dialog3.setTitle("Amount of Ingredients");
                    Stage stage = (Stage) dialog3.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/amount.png").toString()));
                    //sonuc nesnesini gönderir
                    Optional<ButtonType> result2 =dialog3.showAndWait();
                    if(result2.get()==ButtonType.OK){

                        try {
                           int amount =Integer.parseInt(AmountDialogPane.getAmountIngredients());
                            dialogLoop=false;
                            System.out.println( "returned amount = " + AmountDialogPane.getAmountIngredients()
                            );
                           // stock * cost / new amount

                            Product ingredient=IngredientsController.getSelectedProduct();
                            int stock = ingredient.getProductAmount();
                            double cost = ingredient.getProductCost();
/**
 * UNIT COST CALCULATION
 */
                            double unitCost =(amount*cost)/stock;
                            ingredient.setProductAmount(amount);
                            ingredient.setProductCost(unitCost);

                            if(!listInredients.contains(ingredient)){
                            listInredients.add(ingredient);
                            }else{
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Information");
                                alert.setHeaderText("Already Added!");
                                alert.setContentText("You are trying to add existing item!");
                                Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                                stage2.getIcons().add(new Image(this.getClass().getResource("/icons/confirmation.png").toString()));
                                alert.showAndWait();
                            }

                        }catch (NumberFormatException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid Number Format!");
                            alert.setContentText("Ooops, Please Enter Valid Integer Number!");
                            Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                            alert.showAndWait();
                        }
                    }

                    if (result2.get()==ButtonType.CANCEL){
                        dialogLoop=false;
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (IllegalStateException target){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("PAGE NOT FOUND!!!");
                    alert.setContentText("Ooops, There was something wrong!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                } }
/////////////////////amount end

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException target){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("PAGE NOT FOUND!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();
        }






    }


   @FXML
    public void createNewMenu(){
        try {
            Menu menu = new Menu();
            double price = Double.parseDouble(tfPrice.getText());
            double cost = Double.parseDouble(tfCost.getText());
            if(!tfVat.getText().trim().isEmpty()) {
                double vat = Double.parseDouble(tfVat.getText());
            }else{
                menu.setMenuVat(0);
            }
            if(!tfMenuName.getText().trim().isEmpty()){
                menu.setMenuName(tfMenuName.getText().trim().toUpperCase(Locale.ENGLISH));
                menu.setMenuCost(cost);
                menu.setMenuPrice(price);


                System.out.println("menu name: " + menu.getMenuName());
                System.out.println("menu cost: " + menu.getMenuCost());
                System.out.println("menu price: " +menu.getMenuPrice());
                System.out.println("menu vat: " +menu.getMenuVat());


                Task<Boolean> taskCreateNewMenu = new Task() {
                    @Override
                    protected Object call() throws Exception {


                        return DataSource.getInstance().createNewMenuInsertIngredients(menu,listInredients);

                    }
                };
                new Thread(taskCreateNewMenu).start();
                taskCreateNewMenu.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        progressBar.setVisible(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Process Failed!!!");
                        alert.setContentText("Ooops, There was something wrong!\nThe new menu couldn't created !");
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                        alert.showAndWait();
                    }
                });


                taskCreateNewMenu.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("başarılı şekilde eklendi");
                        progressBar.setVisible(false);

                        listMenu.add(menu);
                        tfMenuName.clear();
                        tfCost.clear();
                        tfPrice.clear();
                        tfVat.clear();
                        listInredients.clear();
                    }
                });

                taskCreateNewMenu.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        progressBar.setProgress(taskCreateNewMenu.getProgress());
                        progressBar.setVisible(true);

                    }
                });

                System.out.println("____Ingredients____");
                for (int i = 0; i<listInredients.size();i++){
                    System.out.println("ingredients: " + listInredients.get(i).getProductName());
                }



            }else{
                System.out.println("menü ismi boş bırakılamaz!");
            }



        }catch (NumberFormatException e){


            System.out.println("sayı format hatası");


        }

   }

    private double calculateMenuCost(int stock, double cost) {

            double unitCost= stock/cost;

        return unitCost;
    }

    @FXML
    public void deleteIngredients(){
        if(tableViewIngridients.getSelectionModel().getSelectedItem()!=null){
            listInredients.remove(tableViewIngridients.getSelectionModel().getSelectedItem());
        }

   }

    @FXML
    public void getAllMenus(){
        Task<ObservableList<Product>> taskGetAllMenu = new GetAllMenu();
        try {

            listMenu=FXCollections.observableArrayList(DataSource.getInstance().getAllMenus());
            tableViewMenu.setItems(listMenu);
        } catch (SQLException e) {
            System.out.println("tabloya productlar get edilemedi");
        }
        new Thread(taskGetAllMenu).start();

        taskGetAllMenu.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(true);
                progressBar.setProgress(taskGetAllMenu.getProgress());
            }
        });

        taskGetAllMenu.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(false);
                tableViewMenu.refresh();
            }
        });

        taskGetAllMenu.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setVisible(false);
            }
        });


    }

    class GetAllMenu extends Task{

        @Override
        protected Object call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().getAllMenus());
        }
    }


}





