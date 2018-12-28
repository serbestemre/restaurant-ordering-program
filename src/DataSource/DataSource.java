package DataSource;

import DataModel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;

public class DataSource {

    public static final String DB_NAME = "resCheque.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
    private Connection connection;

    private static final String TABLE_DESK = "Desk";
    private static final String COLUMN_DESKID = "deskId";
    private static final String COLUMN_TAG = "tag";

    private static final String TABLE_PRODUCT = "Product";
    private static final String COLUMN_PRODUCTID= "productID";
    private static final String COLUMN_PRODUCT_NAME= "productName";
    private static final String COLUMN_PRODUCT_COST="productCost";
    private static final String COLUMN_PRODUCT_AMOUNT="productAmount";
    private static final String COLUMN_PRODUCT_PRICE="productPrice";

    private static final String TABLE_MENU="Menu";
    private static final String COLUMN_MENUID="menuID";
    private static final String COLUMN_MENU_NAME="menuName";
    private static final String COLUMN_MENU_PRICE="menuPrice";
    private static final String COLUMN_MENU_COST="menuCost";

    private static final String TABLE_MENU_INGREDIENTS="MenuIngredient";
    private static final String COLUMN_ROWID="rowID";
    private static final String COLUMN_ING_MENUID="MenuID";
    private static final String COLUMN_ING_COST="ingCost";
    private static final String COLUMN_ING_PRODUCTID="ingProductID";
    private static final String COLUMN_ING_PRODUCT_NAME="ingName";
    private static final String COLUMN_ING_AMOUNT="ingAmount";

    private static final String TABLE_ORDER="Orders";
    private static final String COLUMN_ORDERID="orderID";
    private static final String COLUMN_ORDER_MENUID="orderMenuID";
    private static final String COLUMN_ORDER_MENU_PRICE="orderMenuPrice";
    private static final String COLUMN_ORDER_QUANTITY="orderQuantity";
    private static final String COLUMN_ORDER_IS_ORIGINAL="isOriginal";
    private static final String COLUMN_ORDER_DESK_ID="orderDeskID";


    private static final String TABLE_SALE="Sale";
    private static final String COLUMN_SALEID="saleID";
    private static final String COLUMN_SALE_MENUID="saleMenuID";
    private static final String COLUMN_SALE_MENU_PRICE="saleMenuPrice";
    private static final String COLUMN_SALE_MENUQUANTITY="saleMenuQuantity";
    private static final String COLUMN_SALE_DESKID="saleDeskID";

    private static DataSource instance = new DataSource();

    public static DataSource getInstance() {
        return instance;

    }

    public boolean openDatabase() {

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Could not connected to the database " + e.toString());
            return false;
        }
    }

    public void closeDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("The database could not closed");

        }
    }

    public boolean createNewTable(String tag) throws Exception {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(TABLE_DESK);
        sb.append(" ('");
        sb.append(COLUMN_TAG);
        sb.append("') ");
        sb.append("VALUES ");
        sb.append("('");
        sb.append(tag);
        sb.append("');");
        try {

            Statement statement = connection.createStatement();
            statement.executeQuery(sb.toString());
            return true;


        } catch (Exception e) {/*
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("This Customer might be created before \n Phone & TaxID has to be unique ");
            alert.showAndWait();*/
            return false;
        }


    }

    public ArrayList<Desk> getAllDeskInfo() {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_DESK);

        ArrayList<Desk> allCustomers = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());


            while (rs.next()) {
                Desk desk = new Desk();
                desk.setDeskId(rs.getInt(COLUMN_DESKID));
                desk.setTag(rs.getString(COLUMN_TAG));
                allCustomers.add(desk);
            }
            return allCustomers;

        } catch (SQLException e) {
            System.out.println("MASA SORGUSU BAŞARISIZ " + e.getMessage());
            return null;
        }

    }

    public boolean deleteSelectedTable(String tableTag) {

        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + TABLE_DESK + " WHERE " + COLUMN_TAG + "='" + tableTag +"' ;");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean createNewProduct(Product myNewProduct) throws SQLException {

        try {StringBuilder sb = new StringBuilder("INSERT INTO ");
            sb.append(TABLE_PRODUCT);
            sb.append(" ( ");
            sb.append(COLUMN_PRODUCT_NAME);
            sb.append(", ");
            sb.append(COLUMN_PRODUCT_COST);
            sb.append(", ");
            sb.append(COLUMN_PRODUCT_AMOUNT);
            sb.append(", ");
            sb.append(COLUMN_PRODUCT_PRICE);
            sb.append(" ) ");
            sb.append("VALUES ");
            sb.append(" ( '");
            sb.append(myNewProduct.getProductName());
            sb.append("' , ");
            sb.append(myNewProduct.getProductCost());
            sb.append(", ");
            sb.append(myNewProduct.getProductAmount());
            sb.append(", ");
            sb.append(myNewProduct.getProductPrice());
            sb.append(" );");
            Statement statement = connection.createStatement();
            statement.execute(sb.toString());
            return true;

        }catch (Exception e){
            System.out.println("BAŞARISIZ EKLEME!!!");
            return false;
        }
    }

    public ArrayList<Product> getAllProducts() throws SQLException {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_PRODUCT);

        ArrayList<Product> allProducts = new ArrayList<>();


            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt(COLUMN_PRODUCTID));
                product.setProductName(rs.getString(COLUMN_PRODUCT_NAME));
                product.setProductCost(rs.getDouble(COLUMN_PRODUCT_COST));
                product.setProductAmount(rs.getInt(COLUMN_PRODUCT_AMOUNT));
                product.setProductPrice(rs.getDouble(COLUMN_PRODUCT_PRICE));
                allProducts.add(product);
            }
            return allProducts;

    }

    public Boolean deleteProduct(int productID){
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCTID + "=" + productID +" ;");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Boolean updateProduct(Product editingProduct) throws SQLException {


        System.out.println("update product " +editingProduct.getProductID()+" " + editingProduct.getProductName());
            Statement statement = connection.createStatement();


            int updatedRows=statement.executeUpdate("UPDATE " + TABLE_PRODUCT + " SET "+  COLUMN_PRODUCT_NAME+"='"+editingProduct.getProductName()+"', "+  COLUMN_PRODUCT_COST+"="+editingProduct.getProductCost()+", "+COLUMN_PRODUCT_AMOUNT+"="+editingProduct.getProductAmount()+", "+COLUMN_PRODUCT_PRICE+"="+editingProduct.getProductPrice()+" WHERE "+ COLUMN_PRODUCTID+"="+editingProduct.getProductID());




            return true;

    }

    public Boolean insertMenuIngredient(MenuIngredient ingredient,int menuID) throws SQLException {

            Statement statement=connection.createStatement();
            statement.execute("INSERT INTO " + TABLE_MENU_INGREDIENTS + "("+COLUMN_MENUID+","+COLUMN_ING_COST+","+COLUMN_ING_PRODUCTID+","+COLUMN_ING_PRODUCT_NAME+","+COLUMN_ING_AMOUNT+") VALUES ("+ menuID + ", " + ingredient.getIngCost() + ", " +  ingredient.getIngProductID() + ", '"+ingredient.getIngName()+"', "+ ingredient.getIngAmount()+ ")");
            return true;
            }

    public Boolean insertMenuIngredientList(ObservableList<MenuIngredient> ingredientsList,int menuID) throws SQLException {
        for(int i =0; i<ingredientsList.size();i++){
            MenuIngredient ingredient= ingredientsList.get(i);
        Statement statement=connection.createStatement();
        statement.execute("INSERT INTO " + TABLE_MENU_INGREDIENTS + "("+COLUMN_MENUID+","+COLUMN_ING_COST+","+COLUMN_ING_PRODUCTID+","+COLUMN_ING_PRODUCT_NAME+","+COLUMN_ING_AMOUNT+") VALUES ("+ menuID + ", " + ingredient.getIngCost() + ", " +  ingredient.getIngProductID() + ", '"+ingredient.getIngName()+"', "+ ingredient.getIngAmount()+ ")");
        }

        return true;
    }

    public Boolean createNewMenuInsertIngredients(Menu menu, ObservableList<MenuIngredient> ingredientsList) throws SQLException {

        try {


        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(TABLE_MENU);
        sb.append(" (");
        sb.append(COLUMN_MENU_NAME);
        sb.append(",");
        sb.append(COLUMN_MENU_COST);
        sb.append(",");
        sb.append(COLUMN_MENU_PRICE);
        sb.append(") ");
        sb.append("VALUES ");
        sb.append("('");
        sb.append(menu.getMenuName());
        sb.append("', ");
        sb.append(menu.getMenuCost());
        sb.append(", ");
        sb.append(menu.getMenuPrice());
        sb.append(");");

        System.out.println("SQL değiştirildi :" +sb.toString());
        Statement statement = connection.createStatement();

            System.out.println("ing sql " + sb.toString());
         statement.execute(sb.toString());
         int id =statement.getGeneratedKeys().getInt(1);
        System.out.println("dönen id = " + id);

        for (int i=0;i<ingredientsList.size();i++){
            insertMenuIngredient(ingredientsList.get(i),id);
        }



        return true;
        }catch (SQLException e){

            e.printStackTrace();
            return false;
        }

    }

    public ArrayList<Menu> getAllMenus() throws SQLException {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_MENU);

        ArrayList<Menu> allMenu = new ArrayList<>();


        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sb.toString());
        while (rs.next()) {
            Menu menu = new Menu();
            menu.setMenuID(rs.getInt(COLUMN_MENUID));
            menu.setMenuName(rs.getString(COLUMN_MENU_NAME));
            menu.setMenuCost(rs.getDouble(COLUMN_MENU_COST));
            menu.setMenuPrice(rs.getDouble(COLUMN_MENU_PRICE));
            allMenu.add(menu);
        }
        return allMenu;

    }

    public ArrayList<MenuIngredient> getIngredientsOfSelectedMenu(Menu selectedMenu) throws SQLException {

        System.out.println("******-*-*-*-*-* parametre gelen menuID => " + selectedMenu.getMenuID());

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_MENU_INGREDIENTS);
        sb.append(" WHERE ");
        sb.append(COLUMN_ING_MENUID);
        sb.append(" = ");
        sb.append(selectedMenu.getMenuID());
        sb.append(";");

        ArrayList<MenuIngredient> allIngredientsOfSelectedProduct = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sb.toString());
    //    System.out.println("INGREDIENTS SQL>>>>>>>> " + sb.toString());
        while (rs.next()) {

            MenuIngredient ingredient = new MenuIngredient();
            ingredient.setIngName(rs.getString(COLUMN_ING_PRODUCT_NAME));
            ingredient.setIngCost(rs.getDouble(COLUMN_ING_COST));
            ingredient.setIngProductID(rs.getInt((COLUMN_ING_PRODUCTID)));
            ingredient.setIngAmount(rs.getInt(COLUMN_ING_AMOUNT));
            System.out.println("ingName "+ingredient.getIngName()+" "+ ingredient.getIngAmount());
            ingredient.setMenuID(rs.getInt(COLUMN_ING_MENUID));

            allIngredientsOfSelectedProduct.add(ingredient);
        }
        return allIngredientsOfSelectedProduct;

    }

    public Boolean deleteSelectedMenu(Menu selectedMenu) throws SQLException {

            StringBuilder sb = new StringBuilder("DELETE FROM ");
            sb.append(TABLE_MENU);
            sb.append(" WHERE ");
            sb.append(COLUMN_ING_MENUID);
            sb.append(" = ");
            sb.append(selectedMenu.getMenuID());
            sb.append(" ;");
            Statement statement = connection.createStatement();
            statement.execute(sb.toString());
            return true;

    }

    public Boolean updateSelectedMenu(Menu editingMenu) throws SQLException {

        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(TABLE_MENU);
        sb.append(" SET ");
        sb.append(COLUMN_MENU_NAME);
        sb.append(" = ");
        sb.append("'");
        sb.append(editingMenu.getMenuName());
        sb.append("', ");
        sb.append(COLUMN_MENU_PRICE);
        sb.append(" = ");
        sb.append(editingMenu.getMenuPrice());
        sb.append(", ");
        sb.append(COLUMN_MENU_COST);
        sb.append("= ");
        sb.append(editingMenu.getMenuCost());
        sb.append(" WHERE ");
        sb.append(COLUMN_MENUID);
        sb.append(" = ");
        sb.append(editingMenu.getMenuID());
        sb.append(" ;");
        Statement statement = connection.createStatement();
        statement.execute(sb.toString());
        return true;


    }

    public ArrayList<Menu> getAllMenusAndProductsThatCanSell() throws SQLException {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_MENU);

        ArrayList<Menu> allMenusAndProductsThatCanBeSell = new ArrayList<>();


        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sb.toString());
        while (rs.next()) {
            Menu menu = new Menu();
            menu.setMenuID(rs.getInt(COLUMN_MENUID));
            menu.setMenuName(rs.getString(COLUMN_MENU_NAME));
            menu.setMenuCost(rs.getDouble(COLUMN_MENU_COST));
            menu.setMenuPrice(rs.getDouble(COLUMN_MENU_PRICE));
            menu.setIsItOriginalMenu(1);// ** HERE WE ASSING IT ORIGINAL BC IT HAS INGREDIENTS
            allMenusAndProductsThatCanBeSell.add(menu);
        }

        StringBuilder sb2 = new StringBuilder("Select * FROM ");
        sb2.append(TABLE_PRODUCT);
        sb2.append(" WHERE ");
        sb2.append(COLUMN_PRODUCT_PRICE);
        sb2.append("<>0;");

        Statement statement2 = connection.createStatement();
        ResultSet rs2 = statement.executeQuery(sb2.toString());


        //casting product to menu
        //we may get wrong info because we set productID into menu.setMenuID();

        while (rs.next()) {
            Menu productParseToMenu = new Menu();
            productParseToMenu.setMenuID(rs.getInt(COLUMN_PRODUCTID));
            productParseToMenu.setMenuName(rs.getString(COLUMN_PRODUCT_NAME));
            double stock =(rs.getInt(COLUMN_PRODUCT_AMOUNT));
            double cost=(rs.getDouble(COLUMN_PRODUCT_COST));
            productParseToMenu.setMenuCost(cost/stock);
          //  System.out.println("birim maliyeti >>> + " +productParseToMenu.getMenuCost());
            productParseToMenu.setMenuPrice(rs.getDouble(COLUMN_PRODUCT_PRICE));
            productParseToMenu.setIsItOriginalMenu(0);// ** HERE WE ASSING IT NOT ORIGINAL Because IT doesnt have any INGREDIENTS

            allMenusAndProductsThatCanBeSell.add(productParseToMenu);
        }



        return allMenusAndProductsThatCanBeSell;


    }

    public Product getProductInfo(int productID) throws SQLException {

        StringBuilder sb = new StringBuilder("Select * FROM ");
        sb.append(TABLE_PRODUCT);
        sb.append(" WHERE ");
        sb.append(COLUMN_PRODUCTID);
        sb.append(" = ");
        sb.append(productID);
        sb.append(";");

        Product product = new Product();
       // System.out.println("getProductInfo() SQL => "+ sb);
        Statement statement = connection.createStatement();
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (rs.next()) {

            product.setProductID(rs.getInt(COLUMN_PRODUCTID));
            product.setProductName(rs.getString(COLUMN_PRODUCT_NAME));
            product.setProductCost(rs.getDouble(COLUMN_PRODUCT_COST));
            product.setProductAmount(rs.getInt(COLUMN_PRODUCT_AMOUNT));
            product.setProductPrice(rs.getDouble(COLUMN_PRODUCT_PRICE));

        }

        return product;

    } // single product

    public Menu getMenuInfo(int menuID) {

        try {
            System.out.println("_____________________GET MENU INFO ______________________");

            StringBuilder sb = new StringBuilder("Select * FROM ");
            sb.append(TABLE_MENU);
            sb.append(" WHERE ");
            sb.append(COLUMN_MENUID);
            sb.append(" = ");
            sb.append(menuID);
            sb.append(";");

            Menu menu = new Menu();
            // System.out.println("getProductInfo() SQL => "+ sb);
            Statement statement = connection.createStatement();
            ResultSet rs = null;
            try {
                rs = statement.executeQuery(sb.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            while (rs.next()) {

                menu.setMenuID(rs.getInt(COLUMN_MENUID));
                menu.setMenuName(rs.getString(COLUMN_MENU_NAME));
                menu.setMenuCost(rs.getDouble(COLUMN_MENU_COST));
                menu.setMenuPrice(rs.getInt(COLUMN_MENU_PRICE));

            }
            System.out.println("_____________________________getmenüinfo______________________");
            return menu;
        }catch (Exception e){
        return null;
        }
    } //single menu


    public void insertIntoOrderTable(Menu order,int deskID) throws SQLException {


        StringBuilder sb = new StringBuilder("INSERT INTO ");
            sb.append(TABLE_ORDER);
            sb.append(" (");
            sb.append(COLUMN_ORDER_MENUID);
            sb.append(", ");
            sb.append(COLUMN_ORDER_MENU_PRICE);
            sb.append(", ");
            sb.append(COLUMN_ORDER_QUANTITY);
            sb.append(", ");
            sb.append(COLUMN_ORDER_IS_ORIGINAL);
            sb.append(",");
            sb.append(COLUMN_ORDER_DESK_ID);
            sb.append(") ");
            sb.append("VALUES ");
            sb.append(" (");
            sb.append(order.getMenuID());
            sb.append(", ");
            sb.append(order.getMenuPrice());
            sb.append(", ");
            sb.append(order.getOrderQuantity());
            sb.append(", ");
            sb.append(order.getIsItOriginalMenu());
            sb.append(", ");
            sb.append(deskID);
            sb.append(");");
            Statement statement = connection.createStatement();
            System.out.println("ORDER INSERT SQL   "+ sb);
            statement.execute(sb.toString());




    }

    public ObservableList<Order> getAllOrders(int deskID)  {

        try {
            System.out.println("PARAMETRE OLAN desk ID = " + deskID);

            ObservableList<Order> returnList = FXCollections.observableArrayList();

            StringBuilder sb = new StringBuilder("Select * FROM ");
            sb.append(TABLE_ORDER);
            sb.append(" WHERE ");
            sb.append(COLUMN_ORDER_DESK_ID);
            sb.append(" = ");
            sb.append(deskID);
            ArrayList<Order> allOrders = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt(COLUMN_ORDERID));
                order.setMenuID(rs.getInt(COLUMN_ORDER_MENUID));
                order.setDeskID(rs.getInt(COLUMN_ORDER_DESK_ID));
                order.setOrderQuantity(rs.getInt(COLUMN_ORDER_QUANTITY));
                order.setMenuPrice(rs.getDouble(COLUMN_ORDER_MENU_PRICE));
                order.setIsItOriginalMenu(rs.getInt(COLUMN_ORDER_IS_ORIGINAL));
                allOrders.add(order);
            }

            System.out.println("ALL ORDERS SIZE " + allOrders.size());


            for (int i = 0; i < allOrders.size(); i++) {

                Order finalOrder = new Order();
                finalOrder = allOrders.get(i);

                if (allOrders.get(i).getIsItOriginalMenu() == 0) {
                    //product sql
                    Product product = getProductInfo(finalOrder.getMenuID());
                    finalOrder.setMenuName(product.getProductName());
                    finalOrder.setMenuPrice(allOrders.get(i).getMenuPrice());


                    finalOrder.setSubTotal(finalOrder.getMenuPrice() * finalOrder.getOrderQuantity());
                    returnList.add(finalOrder);

                } else {            //menu sql;
                    Menu menu = getMenuInfo(finalOrder.getMenuID());
                    finalOrder.setMenuName(menu.getMenuName());
                    finalOrder.setMenuPrice(allOrders.get(i).getMenuPrice());
                    finalOrder.setSubTotal(finalOrder.getMenuPrice() * finalOrder.getOrderQuantity());
                    returnList.add(finalOrder);
                }
            }

            System.out.println("******** Returned List ***** Will be list for cheque table");
            for (int i = 0; i < returnList.size(); i++) {
                System.out.println(returnList.get(i).getMenuID() + "  " + returnList.get(i).getMenuName());
            }
            return returnList;
        }catch (Exception e){

            return null;
        }
    }


    public Boolean deleteSelectedOrder(int deskID, int menuID) {
        System.out.println("PARAMETRE DELETING DESK ID DELETE ORDER >> " +deskID);

        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + TABLE_ORDER + " WHERE " + COLUMN_ORDER_DESK_ID + "=" + deskID + " and "+ COLUMN_ORDER_MENUID+"="+menuID+";");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Boolean updateFromOrderTable(int deskID, int newQ, int menuID) {


        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(TABLE_ORDER);
        sb.append(" SET ");
        sb.append(COLUMN_ORDER_QUANTITY);
        sb.append(" = ");
        sb.append("");
        sb.append(newQ);
        sb.append(" WHERE ");
        sb.append(COLUMN_DESKID);
        sb.append(" = ");
        sb.append(deskID);
        sb.append(" and ");
        sb.append(COLUMN_MENUID);
        sb.append(" = ");
        sb.append(menuID);
        sb.append(";");
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;



    }

    public Boolean updateAmountOfProduct(int productID, int amount) {

        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(TABLE_PRODUCT);
        sb.append(" SET ");
        sb.append(COLUMN_PRODUCT_AMOUNT);
        sb.append(" = ");
        sb.append("");
        sb.append(amount);
        sb.append(" WHERE ");
        sb.append(COLUMN_PRODUCTID);
        sb.append(" = ");
        sb.append(productID);
        sb.append(";");
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;



    }

    public void insertIntoPayment(Menu menu,int deskID) {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(TABLE_SALE);
        sb.append(" (");
        sb.append(COLUMN_SALE_MENUID);
        sb.append(", ");
        sb.append(COLUMN_SALE_MENU_PRICE);
        sb.append(", ");
        sb.append(COLUMN_SALE_MENUQUANTITY);
        sb.append(", ");
        sb.append(COLUMN_SALE_DESKID);
        sb.append(") ");
        sb.append("VALUES ");
        sb.append(" (");
        sb.append(menu.getMenuID());
        sb.append(", ");
        sb.append(menu.getMenuPrice());
        sb.append(", ");
        sb.append(menu.getOrderQuantity());
        sb.append(", ");
        sb.append(deskID);
        sb.append(");");
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("INSERT SALE SQL   "+ sb);




    }
}
