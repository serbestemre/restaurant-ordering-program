package DataModel;

public class Desk {
    private int deskId;
    private String tag;

    public Desk(int deskId, String tag) {
        this.deskId = deskId;
        this.tag = tag;
    }

    public Desk() {
        this.deskId = deskId;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.tag;
    }
}
