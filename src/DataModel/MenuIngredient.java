package DataModel;

public class MenuIngredient {
    private int rowID;
    private int menuID;
    private double ingCost;
    private int ingProductID;
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

    public int getIngProductID() {
        return ingProductID;
    }

    public void setIngProductID(int ingProductID) {
        this.ingProductID = ingProductID;
    }

    public int getIngAmount() {
        return ingAmount;
    }

    public void setIngAmount(int ingAmount) {
        this.ingAmount = ingAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MenuIngredient)) return false;
        MenuIngredient ingredient = (MenuIngredient) obj;
        return this.getIngProductID() == ingredient.getIngProductID();
    }




}

