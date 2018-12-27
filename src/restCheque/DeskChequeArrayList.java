package restCheque;

import java.util.ArrayList;

public class DeskChequeArrayList extends ArrayList {

    private int chequeID;

    public int getChequeID() {
        return chequeID;
    }

    public void setChequeID(int chequeID) {
        this.chequeID = chequeID;
    }


    public DeskChequeArrayList(int chequeID) {
        this.chequeID = chequeID;
    }
}
