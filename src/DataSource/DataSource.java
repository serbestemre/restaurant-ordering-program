package DataSource;

import DataModel.Desk;
import DataModel.Product;

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

    private static final String TABLE_MENU="Menu";
    private static final String COLUMN_MENUID="menuID";
    private static final String COLUMN_MENU_PRICE="menuPrice";
    private static final String COLUMN_MENU_COST="menuCost";
    private static final String COLUMN_MENU_VAT="menuVat";




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
            StringBuilder sb = new StringBuilder("INSERT INTO ");
            sb.append(TABLE_PRODUCT);
            sb.append(" ( ");
            sb.append(COLUMN_PRODUCT_NAME);
            sb.append(", ");
            sb.append(COLUMN_PRODUCT_COST);
            sb.append(", ");
            sb.append(COLUMN_PRODUCT_AMOUNT);
            sb.append(" ) ");
            sb.append("VALUES ");
            sb.append(" ( '");
            sb.append(myNewProduct.getProductName());
            sb.append("' , ");
            sb.append(myNewProduct.getProductCost());
            sb.append(", ");
            sb.append(myNewProduct.getProductAmount());
            sb.append(" );");
            Statement statement = connection.createStatement();
            statement.execute(sb.toString());
            return true;
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

            Statement statement = connection.createStatement();
            int updatedRows=statement.executeUpdate("UPDATE " + TABLE_PRODUCT + " SET "+  COLUMN_PRODUCT_NAME+"='"+editingProduct.getProductName()+"', "+  COLUMN_PRODUCT_COST+"="+editingProduct.getProductCost()+", "+COLUMN_PRODUCT_AMOUNT+"="+editingProduct.getProductAmount()+" WHERE "+ COLUMN_PRODUCTID+"="+editingProduct.getProductID());
            return true;

    }

}
