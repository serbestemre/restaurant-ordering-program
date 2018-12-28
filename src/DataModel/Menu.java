package DataModel;

public class Menu {

    private int menuID;
    private String menuName;
    private double menuPrice;
    private double menuCost;
    private int orderQuantity;
    private double subTotal;
    private int isItOriginalMenu;  // if its 1 then its original if its 0 then its comes from product table.
    //when add an order into the table, we have to check can we affor with the order. If its Original menu then we have to check its ingredients stock
    //if its comes from product table like a Dessert or any Drink - that we dont store its ingredients or that we store only by unit-
    // then we dont need to go to ingredients table to search for its ingredients because its not efficient and more over there might be a original menu with same ID
    // for exp :(orginal Menu) menuID:5 lazy chicken --> ingredients a,b,c
    //         :(a menu comes from product) => productID:5 - Coca Cola -->no ingredients

    // so that if we go to check Ingredients DB we may get wrong information therefore before go to check db we must know is it original menu or not!
    //to understand that excatly please check Datasource.getInstance().getAllMenusAndProductsThatCanSell();

    public int getIsItOriginalMenu() {
        return isItOriginalMenu;
    }

    public void setIsItOriginalMenu(int isItOriginalMenu) {
        this.isItOriginalMenu = isItOriginalMenu;
    }

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
