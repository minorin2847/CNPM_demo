package model;

import java.io.Serializable;

public class DetailedInvoiceMaterial implements Serializable {
    private int id;
    private int quantity;
    private int price;
    private Material material;

    public DetailedInvoiceMaterial() {
        super();
    }

    public DetailedInvoiceMaterial(int quantity, int price, Material material) {
        super();
        this.quantity = quantity;
        this.price = price;
        this.material = material;
    }

    /* id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* quantity */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* price */
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    /* material */
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    
}
