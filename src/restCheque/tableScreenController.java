package restCheque;

import DataModel.Menu;
import DataModel.MenuIngredient;
import DataModel.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
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

        myTable.setAll(MainScreenController.allCheques.get(MainScreenController.clickedDeskID));
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
                                MenuIngredient ingredient= new MenuIngredient();

                                order.setOrderQuantity(amount);
                                order.setSubTotal(amount*order.getMenuPrice());

                             /** Eğer adisyonda zaten böyle bir sipariş varsa adisyon+=quantity **/

                            if(myTable.contains(order)) {
                                System.out.println("CONTAINS IF");

                                for (int i = 0; i < myTable.size(); i++) {
                                Menu existing= myTable.get(i);
                                if(existing.equals(order)){
                                    System.out.println(i);
                                  //  System.out.println("doğru ürünle eşleşme sağlandı");
                                    existing.setOrderQuantity(order.getOrderQuantity()+existing.getOrderQuantity());
                                    tableViewTableMenuList.refresh();
                                }else{
                                 //   System.out.println("yanlış ürün");
                                }

                                }
                            }else{
                              //  System.out.println("dont contains");
                                myTable.add(order);
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
}
