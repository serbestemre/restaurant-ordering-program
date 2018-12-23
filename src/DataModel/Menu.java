package DataModel;

public class Menu {

    private int menuID;
    private String menuName;
    private double menuPrice;
    private double menuCost;
    private double menuVat;
    private int menuIngredientsID;

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

    public double getMenuVat() {
        return menuVat;
    }

    public void setMenuVat(double menuVat) {
        this.menuVat = menuVat;
    }

    public int getMenuIngridientsID() {
        return menuIngredientsID;
    }

    public void setMenuIngridientsID(int menuIngridientsID) {
        this.menuIngredientsID = menuIngridientsID;
    }
}
