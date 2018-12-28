package restCheque;

import DataModel.Menu;
import DataModel.MenuIngredient;
import DataModel.Order;
import DataModel.Product;
import DataSource.DataSource;
import com.sun.xml.internal.bind.v2.util.TypeCast;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public class tableScreenController {

    @FXML
    private DialogPane dialogPaneTable;

    @FXML
    private TableView<Menu> tableViewTableMenuList;

    @FXML
    private Button btnAddOrder;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button btnDeleteOrder;

    @FXML
    private Button btnPayment;

    @FXML
    private Label btnTotalCost;

    @FXML
    private Label labelTableName;

    public static int myDeskID;

    Menu selectedOrder =new Menu();


    ObservableList<Menu> myTable = FXCollections.observableArrayList();

    @FXML
    public void initialize(){



        System.out.println("DETAILS !");
        for (int i =0;i<myTable.size();i++){
            System.out.println(myTable.get(i).getOrderQuantity());
            System.out.println("order ID>>>> "+ myTable.get(i));

        }

        Task<Boolean> taskGetOrders = new Task() {
            @Override
            protected Object call() throws Exception {
                return myTable.setAll(DataSource.getInstance().getAllOrders(MainScreenController.selectedDesk.getDeskId()));

            }
        };

        new Thread(taskGetOrders).start();

        taskGetOrders.setOnFailed(new EventHandler<WorkerStateEvent>() {
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

        taskGetOrders.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("başarılı şekilde eklendi");

                tableViewTableMenuList.refresh();
                progressBar.setVisible(false);


            }
        });

        taskGetOrders.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.setProgress(taskGetOrders.getProgress());
                progressBar.setVisible(true);

            }
        });


        tableViewTableMenuList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Menu>() {
            @Override
            public void changed(ObservableValue<? extends Menu> observable, Menu oldValue, Menu newValue) {

        selectedOrder=newValue;
              /**
               *
               * it throws null point exception bc when a selected item is not orginal product its not casting to Order! therefore our buttons doesnt works!
               * we didnt realize a product will be big problem for us, we just realized it while implementing and no time for refactoring.
                try{


                    selectedOrder= (Order) newValue;
                    System.out.println("seçilen ORDER =>" + newValue.getMenuID()+ " ORDER ID ?>>" +selectedOrder.getOrderID());
                }catch (Exception e){
                    System.out.println("belirlenemeyen bir hata oluştu");

                }



**/

            }
        });
       // myTable.setAll(MainScreenController.allCheques.get(MainScreenController.clickedDeskID));
        labelTableName.setText(MainScreenController.selectedDesk.getTag());
        tableViewTableMenuList.setItems(myTable);

    }

    @FXML
    public void addOrder(){
        try {
            Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
            dialog2.initOwner(dialogPaneTable.getScene().getWindow());
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("orderNewMenuScreen.fxml"));
            Parent dialogContent = null;
            dialogContent = fxmlLoader.load();
            dialog2.getDialogPane().setContent(dialogContent);
            dialog2.setTitle("Order Menu");
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
                        dialog3.initOwner(dialogPaneTable.getScene().getWindow());
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
                                int amount =0;
                                amount=Integer.parseInt(AmountDialogPane.getAmountIngredients());
                                dialogLoop=false;
                                // stock * cost / new amount
                                Menu order=orderNewMenuScreenController.selectedOrder;
                             //   MenuIngredient ingredient= new MenuIngredient();

                                order.setOrderQuantity(amount);
                                order.setSubTotal(amount*order.getMenuPrice());
                             /** Eğer adisyonda zaten böyle bir sipariş varsa adisyon+=quantity **/

                                Product product = new Product();
                          if(order.getIsItOriginalMenu()==0) { //SEARCH ON PRODUCT TABLE

                              try {
                                  product = DataSource.getInstance().getProductInfo(order.getMenuID());

                                  System.out.println("QUANTITY UPDATE PID???**** "+ product.getProductID());

                              } catch (SQLException e) {
                                  System.out.println("GETPRODUCTINFO() SQL HATASI");
                              }
                           //   System.out.println("___Order Quantity =" +order.getOrderQuantity()+" ** Stock Amount "+ product.getProductAmount());
                              if (order.getOrderQuantity() <= product.getProductAmount()){
                                  System.out.println("içerde!>");
                                  //orderQ must be less than amount(stock) of the product!
                                  if (myTable.contains(order)) {
                                      for (int i = 0; i < myTable.size(); i++) {
                                          Menu existing = myTable.get(i);
                                          if (existing.equals(order)) {
                                              System.out.println(i);
                                              //  System.out.println("doğru ürünle eşleşme sağlandı");
                                              existing.setOrderQuantity(order.getOrderQuantity() + existing.getOrderQuantity());

                                                  int newQ=existing.getOrderQuantity();
  /** EKLENEN ÜRÜN PRODUCT STOKTAN DÜŞÜLÜYOR **/  DataSource.getInstance().updateFromOrderTable(MainScreenController.selectedDesk.getDeskId(),newQ,selectedOrder.getMenuID());
                                              int minusAmount=product.getProductAmount()-order.getOrderQuantity();
                                              System.out.println("UPDATE AMOUNT RUN------>>>>" +product.getProductID()+" "+"new amount "+ minusAmount);

                                              DataSource.getInstance().updateAmountOfProduct(product.getProductID(),minusAmount);
                                                  System.out.println("order eklendi!");

                                              tableViewTableMenuList.refresh();
                                          }
                                      }
                                  } else {
                                      //  System.out.println("dont contains");
                                      myTable.add(order);

                                      try {

                                          int minusAmount=product.getProductAmount()-order.getOrderQuantity();
                                          DataSource.getInstance().updateAmountOfProduct(product.getProductID(),minusAmount);
                                          System.out.println("UPDATE AMOUNT RUN------>>>>" +product.getProductID()+" "+"new amount "+ minusAmount);
                                          DataSource.getInstance().insertIntoOrderTable(order,myDeskID);
                                          System.out.println("order eklendi!");
                                      } catch (SQLException e) {
                                          System.out.println("SIPARIS KAYDEDİLEMEDİ!");
                                      }
                                  }

                              }else{
                                  /// Product stock yetersiz

                                  Alert alert = new Alert(Alert.AlertType.ERROR);
                                  alert.setTitle("Error");
                                  alert.setHeaderText("Insufficient Stock Capacity!");
                                  alert.setContentText("Ooops, We are so sorry "+ order.getMenuName()+" is not in our stock now !\n" +
                                          "Stock Info:" + product.getProductAmount());
                                  Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                                  stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                                  alert.showAndWait();
                              }

                          }else{
                              //check ing here
                              ArrayList<MenuIngredient> returnedIngredients = new ArrayList<>();
                              try {
                                 returnedIngredients= DataSource.getInstance().getIngredientsOfSelectedMenu(order);
                              } catch (SQLException e) {
                                  System.out.println("SQL EXP getIngOfSelectedMenu =>");
                              }

                              ArrayList<MenuIngredient> insufficientCapacity=new ArrayList<>();

                              Iterator<MenuIngredient> iterator = returnedIngredients.iterator();
                                boolean insufficient=false; // if any ingredients is not in stock then dont let to add the order into table cheque
                              while (iterator.hasNext()){
                                  System.out.println("while?");
                                  MenuIngredient orderIngredient=iterator.next();
                                  //getting details of ingredient from product table!!
                                  Product detailOfingProduct=new Product();

                                  try {
                                   //   System.out.println("orderİng ProductID'>>>>" + orderIngredient.getIngProductID());
                                      detailOfingProduct=DataSource.getInstance().getProductInfo(orderIngredient.getIngProductID());
                                  } catch (SQLException e) {
                                      System.out.println("order Ing - info SQL Hatası");
                                  }
                                  System.out.println("CRITICAL SECTION ! :) ");
                                  System.out.println("order Q = " +order.getOrderQuantity()+ " ingAmount"+orderIngredient.getIngAmount()+" productDetail Stock Cap " +detailOfingProduct.getProductAmount()  );
                                 if ((order.getOrderQuantity()*orderIngredient.getIngAmount())>detailOfingProduct.getProductAmount()){
                                     /// Sipariş edilen ürün sayısı*ürün içeriği < o ürünün toplam kapasitesiden küçük olmalı
                                    // eğer > ise bu sipariş ürün stoğundan büyüktür bu yüzden eklenemez!
                                       insufficient = true;
                                insufficientCapacity.add(orderIngredient);
                                 }

                              }//while ended all ingredients checked
                              System.out.println("__ing checked__");
                              /** checking sout
                              for (int i =0;i<insufficientCapacity.size();i++){
                                  System.out.println("insf ing??>>"+insufficientCapacity.get(i).getIngName());
                              }**/

                              if(insufficient==false){ // it means all ingredients in our stock // then add order into cheque

                                  for (int i =0;i<returnedIngredients.size();i++){
                                      System.out.println("ING STOCKTAN DÜŞÜYOR>>"+returnedIngredients.get(i).getIngName());

                                      try {
                                          Product proIng=DataSource.getInstance().getProductInfo(returnedIngredients.get(i).getIngProductID());
                                          int newAmount=proIng.getProductAmount()-(returnedIngredients.get(i).getIngAmount()*order.getOrderQuantity());
                                          System.out.println("NEW AMOUNT>>>> "+newAmount);
                                          proIng.setProductAmount(newAmount);

                                          DataSource.getInstance().updateProduct(proIng);
                                      } catch (SQLException e) {
                                          e.printStackTrace();
                                      }


                                  }


                                  if (myTable.contains(order)) {
                                      for (int i = 0; i < myTable.size(); i++) {
                                          Menu existing = myTable.get(i);
                                          if (existing.equals(order)) {
                                              System.out.println(i);
                                              //  System.out.println("doğru ürünle eşleşme sağlandı");
                                              existing.setOrderQuantity(order.getOrderQuantity() + existing.getOrderQuantity());
                                              tableViewTableMenuList.refresh();
/** update existing order **/
                                              int newQ=existing.getOrderQuantity();
                                              DataSource.getInstance().updateFromOrderTable(MainScreenController.selectedDesk.getDeskId(),newQ,existing.getMenuID());
/** INGREDIENTS PRODUCT STOKTAN DÜŞÜLÜYOR  **/
                                          }
                                      }
                                  } else {
                                      //  System.out.println("dont contains");
                                      myTable.add(order);


                                      try {
                                          int minusAmount=product.getProductAmount()-order.getOrderQuantity();
                                          DataSource.getInstance().updateAmountOfProduct(product.getProductID(),minusAmount);
                                          DataSource.getInstance().insertIntoOrderTable(order,myDeskID);
                                          System.out.println("db order inserted");
                                      } catch (SQLException e) {
                                          System.out.println("SIPARIS KAYDEDİLEMEDİ!");
                                          e.printStackTrace();
                                      }
                                  }

                              }else{

                                  Alert alert = new Alert(Alert.AlertType.ERROR);
                                  alert.setTitle("Error");
                                  alert.setHeaderText("Insufficient Stock Capacity!");
                                  alert.setContentText("Ooops, We are so sorry "+ order.getMenuName()+" is not in our stock now !");
                                  Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                                  stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                                  alert.showAndWait();

                              }



                          }// ing checked



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
    public void deleteOrder(){
        if(tableViewTableMenuList.getSelectionModel().getSelectedItem()!=null){
            Task<Boolean> taskDeleteOrder = new Task() {

                @Override
                protected Object call() throws Exception {
                    return DataSource.getInstance().deleteSelectedOrder(MainScreenController.selectedDesk.getDeskId(),selectedOrder.getMenuID());
                }
            };
            new Thread(taskDeleteOrder).start();
            taskDeleteOrder.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    progressBar.setVisible(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Process Failed!!!");
                    alert.setContentText("Ooops, There was something wrong!\nThe order could not deleted product database!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
                    alert.showAndWait();
                }
            });
            taskDeleteOrder.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println(" order başarılı silindi!!");
                    tableViewTableMenuList.refresh();
                    tableViewTableMenuList.getItems().remove(tableViewTableMenuList.getSelectionModel().getSelectedItem());

                    System.out.println("ÜRÜNLER STOĞA GERİ YÜKLENİYOR!");

                   if(tableViewTableMenuList.getSelectionModel().getSelectedItem().getIsItOriginalMenu()==0){
                       DataSource.getInstance().updateAmountOfProduct(selectedOrder.getMenuID(),selectedOrder.getOrderQuantity());

                   }else {

                   }


                    progressBar.setVisible(false);
}
});

taskDeleteOrder.setOnRunning(new EventHandler<WorkerStateEvent>() {
@Override
public void handle(WorkerStateEvent event) {
progressBar.setProgress(taskDeleteOrder.getProgress());
progressBar.setVisible(true);

}
});




        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong or Invalid Attempt!");
            alert.setContentText("Ooops, Please select an order to delete!");
            Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
            stage2.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();

        }

    }

    @FXML
    public void btnPayment(){


        int deskID=MainScreenController.selectedDesk.getDeskId();
        System.out.println("TABLE SIZE>>>>" +myTable.size());
        for (int i=0;i<myTable.size();i++){

            Menu menu = new Menu();
            menu.setMenuID(myTable.get(i).getMenuID());
            menu.setMenuPrice(myTable.get(i).getSubTotal());
            menu.setOrderQuantity(myTable.get(i).getOrderQuantity());

            DataSource.getInstance().insertIntoPayment(menu,deskID);
            DataSource.getInstance().deleteSelectedOrder(deskID,menu.getMenuID());
            tableViewTableMenuList.getItems().clear();
            tableViewTableMenuList.refresh();
        }





    }
}
