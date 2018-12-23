package restCheque;


import DataModel.Desk;
import DataSource.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;
public class MainScreenController {
    public ArrayList<Desk> deskDBreturnlist = new ArrayList<Desk>();

    @FXML
    private BorderPane mainScreen;

    @FXML
    private Button btnEkle;

    @FXML
    private Button btnSil;

    @FXML
    private VBox vBox;

    @FXML
    private ScrollPane scrollPane;



    @FXML
    public void initialize(){
        vBox.setPadding(new Insets(5));
        getTables();

        btnEkle.setTooltip(new Tooltip("Masa Ekle"));

    }

    @FXML
    public void masaSil(){
        try {
        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(mainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("deleteTable.fxml"));
        Parent dialogContent = null;
        dialogContent = fxmlLoader.load();
        dialog2.getDialogPane().setContent(dialogContent);
        dialog2.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog2.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        //sonuc nesnesini gönderir
        Optional<ButtonType> result =dialog2.showAndWait();
        if(result.get()==ButtonType.OK) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("It looks like risky attemption!");
            alert.setContentText("Do you really want to delete '" + DeleteTableController.selectedItem.getTag().toString() + "' table ?");

            Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
            stage2.getIcons().add(new Image(this.getClass().getResource("/icons/confirmation.png").toString()));

            Optional<ButtonType> result2 = alert.showAndWait();
            if (result2.get() == ButtonType.OK){

                DataSource.getInstance().deleteSelectedTable(DeleteTableController.selectedItem.getTag().toString());
                getTables();
            } else {
            }
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

    public void getTables(){

        vBox.getChildren().clear();

        deskDBreturnlist=DataSource.getInstance().getAllDeskInfo();

        int columnNumber=11;
        int hBoxCounter = (deskDBreturnlist.size()/6)+2;  //+2 çünkü j[0] boş ve 6'nın bölümünden kalanları yeni satıra eklemesi için

        HBox hBoxes[]=new HBox[hBoxCounter];
        ImageView imageViews[]=new ImageView[deskDBreturnlist.size()+1];
        int j=0;
        // hBoxes[j]=new HBox();
        //  hBoxes[j].setSpacing(5);
      //  System.out.println("**hbox[j="+j+"]**  ilk HBOX");
        Button buttons[]=new Button[deskDBreturnlist.size()+1];
      //  System.out.println("buttons.length/4= " + buttons.length/4);

        for (int i =0; i<buttons.length-1; i++){

         //   System.out.println("j döngüde "+ j + " i döngüde " + i);
            if((j<((buttons.length/columnNumber)+1)) && (i % columnNumber==0)){
                j++;
                hBoxes[j]=new HBox();
                hBoxes[j].setMaxWidth(Region.USE_COMPUTED_SIZE);
                hBoxes[j].setPrefHeight(Region.USE_COMPUTED_SIZE);

                hBoxes[j].setSpacing(5);

            //    System.out.println("**hbox[j="+j+"]** yaratıldı-------------------------------------");
            //    System.out.println("j ++ " + j);

            }
            buttons[i]=new Button(deskDBreturnlist.get(i).getTag().toUpperCase().toString());
            imageViews[i]=new ImageView("icons/emptyTable.png");
            buttons[i].setGraphic(imageViews[i]);
            buttons[i].contentDisplayProperty().setValue(ContentDisplay.BOTTOM);

            imageViews[i].setFitHeight(70);
            imageViews[i].setFitWidth(70);
            buttons[i].setMinSize(150,150);
            hBoxes[j].getChildren().add(buttons[i]);
        }
        for (int i=1;i<=(buttons.length/columnNumber)+1;i++){
            vBox.getChildren().add(hBoxes[i]);
         //   System.out.println("yazdir hbox=" +i);
            // vBox.scrol
        }
        for (int i =0; i<buttons.length-1; i++) {

            final String tag= deskDBreturnlist.get(i).getTag().toUpperCase().toString();

            final int id=i;

            buttons[i].setOnAction(new javafx.event.EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println( deskDBreturnlist.get(id).getTag().toUpperCase().toString() );

                    ImageView newPic =new ImageView("icons/table.png");
                    buttons[id].setGraphic(newPic);
                    newPic.setFitHeight(70);
                    newPic.setFitWidth(70);
                    buttons[id].contentDisplayProperty().setValue(ContentDisplay.BOTTOM);


                    //   System.out.println(tag);
                }
            });
        }
    }

    @FXML
    public void addNewTable() {
        try {
        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(mainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("createTable.fxml"));
        Parent dialogContent = null;
        dialogContent = fxmlLoader.load();
        dialog2.getDialogPane().setContent(dialogContent);
        dialog2.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        //sonuc nesnesini gönderir
        Optional<ButtonType> result =dialog2.showAndWait();
    if(result.get()==ButtonType.CLOSE){
        getTables();
    }

    }
    catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("CREATE TABLE PAGE ERROR!!!");
        alert.setContentText("Ooops, Something went wrong!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
        alert.showAndWait();

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
    public void showProductDialogPane(){

        try {
            Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
            dialog2.initOwner(mainScreen.getScene().getWindow());
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("productScreen.fxml"));
            Parent dialogContent = null;
            dialogContent = fxmlLoader.load();
            dialog2.getDialogPane().setContent(dialogContent);
            dialog2.setTitle("Products");
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            //sonuc nesnesini gönderir
            Optional<ButtonType> result =dialog2.showAndWait();

                if (result.get() == ButtonType.OK){


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
    public void showMenuDialogPane(){

        try {
            Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
            dialog2.initOwner(mainScreen.getScene().getWindow());
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("menuScreen.fxml"));
            Parent dialogContent = null;
            dialogContent = fxmlLoader.load();
            dialog2.getDialogPane().setContent(dialogContent);
            dialog2.setTitle("Create New Menu");
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            //sonuc nesnesini gönderir
            Optional<ButtonType> result =dialog2.showAndWait();
            if (result.get() == ButtonType.OK){
            }


        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("MENU PAGE COULD NOT LOADED!!!");
            alert.setContentText("Ooops, There was something wrong!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/icons/error.png").toString()));
            alert.showAndWait();
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
