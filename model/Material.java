package model;

import java.io.Serializable;

public class Material implements Serializable {
    private int id;
    private String name;
    private int unitPrice;

    public Material() {
        super();
    }

    public Material(String name, int unitPrice) {
        super();
        this.name = name;
        this.unitPrice = unitPrice;
    }

    /* id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* name */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* unitPrice */
    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
    
}
