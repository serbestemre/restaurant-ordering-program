package DataModel;

public class MenuIngredient {
    private int rowID;
    private int menuID;
    private double ingCost;
    private int ingProduct;
    private String ingName;
    private int ingAmount;

    public String getIngName() {
        return ingName;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public double getIngCost() {
        return ingCost;
    }

    public void setIngCost(double ingCost) {
        this.ingCost = ingCost;
    }

    public int getIngProduct() {
        return ingProduct;
    }

    public void setIngProduct(int ingProduct) {
        this.ingProduct = ingProduct;
    }

    public int getIngAmount() {
        return ingAmount;
    }

    public void setIngAmount(int ingAmount) {
        this.ingAmount = ingAmount;
    }
}

