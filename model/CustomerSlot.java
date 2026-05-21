package model;

import java.io.Serializable;

public class CustomerSlot implements Serializable {
    private int id;
    private Slot slot;
    private Customer customer;
    private String status;

    public CustomerSlot() {
        super();
    }

    public CustomerSlot(Slot slot, Customer customer, String status) {
        super();
        this.slot = slot;
        this.customer = customer;
        this.status = status;
    }

    /* id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* slot */
    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /* customer */
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /* status */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
