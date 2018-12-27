package restCheque;

import javafx.scene.control.Button;

public class DeskButtons extends Button {
    private int deskID;

    public int getDeskID() {
        return deskID;
    }

    public void setDeskID(int deskID) {
        this.deskID = deskID;
    }


    public DeskButtons(int deskID) {
        this.deskID = deskID;
      //  this.setText(super.getText());
    }
}
