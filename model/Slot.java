package model;

import java.io.Serializable;

public class Slot implements Serializable {
    private int id;
    private User creator;

    public Slot() {
        super();
    }

    public Slot(User creator) {
        super();
        this.creator = creator;
    }

    /* id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* creator */
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
