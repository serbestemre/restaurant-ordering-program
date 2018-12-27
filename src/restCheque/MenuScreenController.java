package restCheque;

import DataModel.Menu;
import DataModel.MenuIngredient;
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
    private Button btnDeleteMenu;

    @FXML
    private Button btnEditMenu;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAddExistingIngredienets;

    @FXML
    private Button btndeleteExistingIngredients;

    @FXML
    private Label labelTotalCost;

    @FXML Label labelLeftTitle;

    @FXML
    private TableView<MenuIngredient> tableViewIngridients;

    @FXML
    private TableView<MenuIngredient> tableViewExistingIngredients;


    @FXML
    private TableView<Menu> tableViewMenu;

    public Menu selectedMenu=new Menu();

    Menu editingMenu=new Menu();
    boolean editingMode=false;



    ObservableList<MenuIngredient> listIngredients= FXCollections.observableArrayList();
    ObservableList<Menu> listMenu= FXCollections.observableArrayList();
    ObservableList<MenuIngredient> existingIngredientsList= FXCollections.observableArrayList();
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
    tableViewIngridients.setItems(listIngredients);
/**
    if(listMenu.size()!=0){
        tableViewMenu.getSelectionModel().selectFirst();
        Menu tempMenu = tableViewMenu.getSelectionModel().getSelectedItem();
        getSelectedMenuIngredients(tempMenu);
    }
**/
    tableViewMenu.setItems(listMenu);

        listIngredients.addListener(new ListChangeListener<MenuIngredient>() {
           @Override
           public void onChanged(Change<? extends MenuIngredient> c) {
               totalCost=0;
               for(int i =0;i<listIngredients.size();i++){
                totalCost+=listIngredients.get(i).getIngCost();
               }
               labelTotalCost.setText(String.valueOf(totalCost));
               tfCost.setText(String.valueOf(totalCost));

           }
       });

        tableViewMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMenu=newSelection;
                getSelectedMenuIngredients(newSelection);
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
                           // stock * cost / new amount

                            Product product=IngredientsController.getSelectedProduct();
                            MenuIngredient ingredient= new MenuIngredient();

                            int stock = product.getProductAmount();
                            double cost = product.getProductCost();
/**
 * UNIT COST CALCULATION
 */
                            double unitCost =(amount*cost)/stock;

                            ingredient.setIngAmount(amount);
                            ingredient.setIngCost(unitCost);
                            product.setProductAmount(amount);
                            product.setProductCost(unitCost);
                            ingredient.setIngName(product.getProductName());
                            ingredient.setIngProduct(product.getProductID());
                            if(!listIngredients.contains(ingredient)){
                                listIngredients.add(ingredient);
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

        if(listIngredients.size()!=0) {
            try {
                Menu menu = new Menu();
                double price = Double.parseDouble(tfPrice.getText());
                double cost = Double.parseDouble(tfCost.getText());
                double vat=0;

                if (!tfMenuName.getText().trim().isEmpty()) {
                    menu.setMenuName(tfMenuName.getText().trim().toUpperCase(Locale.ENGLISH));
                    menu.setMenuCost(cost);
                    menu.setMenuPrice(price);


                    System.out.println("menu name: " + menu.getMenuName());
                    System.out.println("menu cost: " + menu.getMenuCost());
                    System.out.println("menu price: " + menu.getMenuPrice());



                    Task<Boolean> taskCreateNewMenu = new Task() {
                        @Override
                        protected Object call() throws Exception {


                            return DataSource.getInstance().createNewMenuInsertIngredients(menu, listIngredients);

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
                            listIngredients.clear();
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
                    for (int i = 0; i < listIngredients.size(); i++) {
                        System.out.println("ingredients: " + listIngredients.get(i).getIngName());
                    }


                } else {
                    System.out.println("menü ismi boş bırakılamaz!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Menu name can not be empty!!!");
                    alert.setContentText("Ooops, There was something wrong!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                }


            } catch (NumberFormatException e) {
                System.out.println("sayı format hatası"); Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Number format error!!!");
                alert.setContentText("Ooops, There was something wrong!");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                alert.showAndWait();



            }
        }else{
            System.out.println("MENU TANIMLAMA BAŞARISIZ EN AZ BİR INGREDIENTS GIRIN!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Could not create menu. Select at least one ingredients!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();
        }
   }

    @FXML
    public void deleteIngredients(){
        if(tableViewIngridients.getSelectionModel().getSelectedItem()!=null){
            listIngredients.remove(tableViewIngridients.getSelectionModel().getSelectedItem());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Select an item to delete!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Products could not loaded!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();
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

    @FXML
    public void getSelectedMenuIngredients(Menu selectedMenu){
        Task<ObservableList<MenuIngredient>> taskGetSelectedMenuIngredients = new GetSelectedMenuIngredients();
        try {

            existingIngredientsList.setAll(DataSource.getInstance().getIngredientsOfSelectedMenu(selectedMenu));
            tableViewExistingIngredients.setItems(existingIngredientsList);
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
            System.out.println("---- Guiden giden selected menuID>>>" + selectedMenu.getMenuID());
            return FXCollections.observableArrayList(DataSource.getInstance().getIngredientsOfSelectedMenu(selectedMenu));
        }
    }

    @FXML
    public void editSelectedMenu(){
        /**
         * HOW TO WORK editing/updating menu and its ingredients !
         * when the user click on edit button.
         * checking validation end then buttons controlling (disable/visible)
         * after doing need edition
         * the user click on Update Button
         * there are two TASKS one of them taskUpdateMenu and the other one is taskRE_InsertIngredients
         * taskUpdateMenu > we call sql method to update selected Menu according with menuID key in database table
         * THERE IS TWO TRIGGER ON DATABASE
         * 1-AFTER UPDATE ON MENU TABLE -> delete updated menu's ingredients from MenuIngredient Table
         * CREATE TRIGGER deleteIngredientsOfUpdatedMenuITEM AFTER UPDATE ON Menu begin DELETE FROM MenuIngredient where MenuIngredient.menuID=old.menuID; end
         *2-AFTER DELETE ON MENU TABLE -> THE SAME TRIGGER
         * ---->then if there is no error ! another task is starting
         * ----->taskRE_InsertIngredients> we call insert sql method to insert ingredients of updated/edited Item
         *----------->if there is an error while inserting Ingredients of menu! -> then we delete new updated menu on MENU Table and then
         *-----------------> show an alert dialog to warn the user "while updating menu something went wrong and the menu deleted, please create that menu again!"
         *----->if no error then update process completed successfully **/

        if(tableViewMenu.getSelectionModel().getSelectedItem()!=null){
            /** Editing Mode setting True!**/
            editingMode=true;
            editingMenu=tableViewMenu.getSelectionModel().getSelectedItem();

            labelLeftTitle.setText("EDIT MENU");
            tfMenuName.setText(tableViewMenu.getSelectionModel().getSelectedItem().getMenuName());
            tfCost.setText(String.valueOf(tableViewMenu.getSelectionModel().getSelectedItem().getMenuCost()));
            tfPrice.setText(String.valueOf(tableViewMenu.getSelectionModel().getSelectedItem().getMenuPrice()));

            listIngredients.setAll(existingIngredientsList);

            tableViewExistingIngredients.setDisable(true);
            tableViewMenu.setDisable(true);
            btnEditMenu.setDisable(true);
            btnDeleteMenu.setDisable(true);
            btnCreateMenu.setDisable(true);

            btnUpdate.setVisible(true);
            btnCancel.setVisible(true);
         //   btndeleteExistingIngredients.setVisible(true);
          //  btnAddExistingIngredienets.setVisible(true);






        }else{
            System.out.println("menu seçilmedi");
        }

    }

    @FXML
    public void updateMenu() {
        /**
         * HOW TO WORK editing/updating menu and its ingredients !
         * when the user click on edit button.
         * checking validation end then buttons controlling (disable/visible)
         * after doing need edition
         * the user click on Update Button
         * there are two TASKS one of them taskUpdateMenu and the other one is taskRE_InsertIngredients
         * taskUpdateMenu > we call sql method to update selected Menu according with menuID key in database table
         * THERE IS TWO TRIGGER ON DATABASE
         * 1-AFTER UPDATE ON MENU TABLE -> delete updated menu's ingredients from MenuIngredient Table
         * CREATE TRIGGER deleteIngredientsOfUpdatedMenuITEM AFTER UPDATE ON Menu begin DELETE FROM MenuIngredient where MenuIngredient.menuID=old.menuID; end
         *2-AFTER DELETE ON MENU TABLE -> THE SAME TRIGGER
         * ---->then if there is no error ! another task is starting
         * ----->taskRE_InsertIngredients> we call insert sql method to insert ingredients of updated/edited Item
         *----------->if there is an error while inserting Ingredients of menu! -> then we delete new updated menu on MENU Table and then
         *-----------------> show an alert dialog to warn the user "while updating menu something went wrong and the menu deleted, please create that menu again!"
         *----->if no error then update process completed successfully **/
        if (listIngredients.size() != 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Would you like to update " +editingMenu.getMenuName());
            alert.setContentText("Are you sure?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/confirmation.png").toString()));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                try {
                    editingMenu.setMenuCost(Double.parseDouble(tfCost.getText()));
                    editingMenu.setMenuPrice(Double.parseDouble(tfPrice.getText()));

                    if (!tfMenuName.getText().trim().isEmpty()) {
                        editingMenu.setMenuName(tfMenuName.getText().trim().toUpperCase(Locale.ENGLISH));

                        Task<Boolean> taskUpdateMenu = new Task() {
                            @Override
                            protected Object call() throws Exception {


                                return DataSource.getInstance().updateSelectedMenu(editingMenu);

                            }
                        };
                        new Thread(taskUpdateMenu).start();
                        taskUpdateMenu.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                progressBar.setVisible(false);
                                editingMode = false;
                                labelLeftTitle.setText("CREATE NEW MENU");
                                tableViewExistingIngredients.setDisable(false);
                                tableViewMenu.setDisable(false);
                                btnEditMenu.setDisable(false);
                                btnDeleteMenu.setDisable(false);
                                btnCreateMenu.setDisable(false);
                                btnUpdate.setVisible(false);
                                btnCancel.setVisible(false);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error Dialog");
                                alert.setHeaderText("Process Failed!!!");
                                alert.setContentText("Ooops, There was something wrong!\nThe new menu couldn't updated !!");
                                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                                alert.showAndWait();
                            }
                        });
                        taskUpdateMenu.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                //    System.out.println("menütable başarılı bir şekilde güncellendi");
                                /**child process starts **/
                                Task<Boolean> taskRE_InsertIngredients = new Task() {
                                    @Override
                                    protected Object call() throws Exception {


                                        return DataSource.getInstance().insertMenuIngredientList(listIngredients, editingMenu.getMenuID());

                                    }
                                };
                                new Thread(taskRE_InsertIngredients).start();
                                taskRE_InsertIngredients.setOnFailed(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent event) {
                                        progressBar.setVisible(false);
                                        editingMode = false;
                                        labelLeftTitle.setText("CREATE NEW MENU");
                                        tableViewExistingIngredients.setDisable(false);
                                        tableViewMenu.setDisable(false);
                                        btnEditMenu.setDisable(false);
                                        btnDeleteMenu.setDisable(false);
                                        btnCreateMenu.setDisable(false);

                                        btnUpdate.setVisible(false);
                                        btnCancel.setVisible(false);

                                        /** after menu created successfully if there went something wrong while inserting ingredients of new created menu
                                         * then delete new created menu! **/

                                        /********* ALERT MESSAGE !! TO INFORM THE USER UPDATING MENU DELETED BC SOMETHING WENT WRONG! ************/

                                        deleteSelectedMenu(editingMenu);// bir hata oluştuğu için yeni yaratılmaya çalışılan menüyü sil!

                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Error Dialog");
                                        alert.setHeaderText("Failed. Try again!");
                                        alert.setContentText("Ooops, There was something wrong!\nThe new product did not add into product database!");
                                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                                        alert.showAndWait();
                                    }
                                });


                                taskRE_InsertIngredients.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent event) {
                                        //       System.out.println("başarılı şekilde eklendi");

                                        Alert alertMenuUpdate = new Alert(Alert.AlertType.INFORMATION);
                                        alertMenuUpdate.setTitle("Information Dialog");
                                        alertMenuUpdate.setHeaderText("Menu updated successfully!");
                                        alertMenuUpdate.setContentText("Successful!");
                                        Stage stageMenuUpdate = (Stage) alertMenuUpdate.getDialogPane().getScene().getWindow();
                                        stageMenuUpdate.getIcons().add(new Image(this.getClass().getResource("/icons/checked.png").toString()));
                                        alertMenuUpdate.showAndWait();

                                        //gui update
                                        int index = listMenu.indexOf(selectedMenu);
                                        listMenu.remove(index);
                                        listMenu.add(index, editingMenu);
                                        //gui update

                                        /** button yönetimlerini yap**/
                                        progressBar.setVisible(false);
                                        editingMode = false;
                                        labelLeftTitle.setText("CREATE NEW MENU");
                                        tfMenuName.clear();
                                        tfCost.clear();
                                        tfPrice.clear();
                                        listIngredients.clear();

                                        tableViewExistingIngredients.setDisable(false);
                                        tableViewMenu.setDisable(false);
                                        btnEditMenu.setDisable(false);
                                        btnDeleteMenu.setDisable(false);
                                        btnCreateMenu.setDisable(false);

                                        btnUpdate.setVisible(false);
                                        btnCancel.setVisible(false);

/** bütün update işlemi başarılı **/
                                    }
                                });

                                taskRE_InsertIngredients.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent event) {
                                        progressBar.setProgress(taskRE_InsertIngredients.getProgress() + taskUpdateMenu.getProgress());
                                        progressBar.setVisible(true);
                                    }
                                });
                                /**child process end **/
                            }
                        });


                    } else {
                        //System.out.println("menü ismi girilmedi!!!");
                        Alert menuName = new Alert(Alert.AlertType.ERROR);
                        menuName.setTitle("Error Dialog");
                        menuName.setHeaderText("Fill the menu name!");
                        menuName.setContentText("Ooops, There was something wrong!");
                        Stage stageMenu = (Stage) menuName.getDialogPane().getScene().getWindow();
                        stageMenu.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                        menuName.showAndWait();

                    }


                } catch (NumberFormatException n) {
                   // System.out.println("menü cost, price veya vat doğru formatta girilmedi!!");
                    Alert numberFormat = new Alert(Alert.AlertType.ERROR);
                    numberFormat.setTitle("Error Dialog");
                    numberFormat.setHeaderText("Menu cost, price or vat is not valid. Check Again!!!");
                    numberFormat.setContentText("Ooops, There was something wrong!");
                    Stage stageForNumber = (Stage) numberFormat.getDialogPane().getScene().getWindow();
                    stageForNumber.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    numberFormat.showAndWait();

                }
            } else {
                //System.out.println("boş menü yaratamazsınız.!!! en az bir ingredients ekleyin!");
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Error Dialog");
                alert2.setHeaderText("Can't create empty menu. Add at least one ingredients!!!");
                alert2.setContentText("Ooops, There was something wrong!");
                Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
                stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                alert2.showAndWait();
            }
        }
        else {
            //Cancel pressed for updating item
    }
    }
    @FXML
    public void cancelButton(){
        editingMode=false;
        tableViewMenu.setDisable(false);
        btnEditMenu.setDisable(false);
        btnDeleteMenu.setDisable(false);
        btnCreateMenu.setDisable(false);

        tfMenuName.clear();
        tfPrice.clear();
        tfCost.clear();
        labelLeftTitle.setText("CREATE NEW MENU");
        tableViewExistingIngredients.setDisable(false);

        btnUpdate.setVisible(false);
        btnCancel.setVisible(false);
        listIngredients.clear();

    }

    @FXML
    public void btnDeleteMenu(){



        if(tableViewMenu.getSelectionModel().getSelectedItem()!=null){
            //ask question are u sure?
            deleteSelectedMenu(tableViewMenu.getSelectionModel().getSelectedItem());
            //ask question are u sure?


        }else{
            //seçim yapılmadı

        }

    }


    public void deleteSelectedMenu(Menu Selected){
            Task<Boolean> taskDeleteSelectedMenu = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return DataSource.getInstance().deleteSelectedMenu(tableViewMenu.getSelectionModel().getSelectedItem());
                    }
                };
                new Thread(taskDeleteSelectedMenu).start();

                taskDeleteSelectedMenu.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        progressBar.setVisible(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Process Failed!!!");
                        alert.setContentText("Ooops, There was something wrong!\nThe menu could not deleted !");
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                        alert.showAndWait();
                    }
                });
                taskDeleteSelectedMenu.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                       // System.out.println("menu başarılı şekilde silindi");
                        progressBar.setVisible(false);

                        if(editingMode){ // if editing mode is on/true
                            System.out.println("delete setOnSucceed editing mode on konumda");
                            int index = listMenu.indexOf(selectedMenu);
                            listMenu.remove(index);
                            listMenu.add(index,editingMenu);
                            tableViewExistingIngredients.setDisable(false);
                            tableViewMenu.setDisable(false);
                            btnEditMenu.setDisable(false);
                            btnDeleteMenu.setDisable(false);
                            btnCreateMenu.setDisable(false);
                            btnUpdate.setVisible(false);
                            btnCancel.setVisible(false);
                            listIngredients.clear();
                        }else{
                            listMenu.remove(selectedMenu); //deleted in gui from tableview
                            System.out.println("delete setOnSucceed editing mode off konumda");
                        }
                    }
                });
                taskDeleteSelectedMenu.setOnRunning(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        progressBar.setProgress(taskDeleteSelectedMenu.getProgress());
                        progressBar.setVisible(true);

                    }
                });

    }



}





