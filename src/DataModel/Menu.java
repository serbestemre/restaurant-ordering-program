package DataModel;

public class Menu {

    private int menuID;
    private String menuName;
    private double menuPrice;
    private double menuCost;
    private int orderQuantity;
    private double subTotal;

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public double getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(double menuPrice) {
        this.menuPrice = menuPrice;
    }

    public double getMenuCost() {
        return menuCost;
    }

    public void setMenuCost(double menuCost) {
        this.menuCost = menuCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Menu)) return false;
        Menu menu = (Menu) obj;
        return this.getMenuID() == menu.getMenuID();
    }
}
