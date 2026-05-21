package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DetailedInvoice implements Serializable {
    private int id;
    private Date date;
    private String status;
    private CustomerSlot customerSlot;
    private ArrayList<DetailedInvoiceService> services;
    private ArrayList<DetailedInvoiceMaterial> materials;

    public DetailedInvoice() {
        super();
        services = new ArrayList<DetailedInvoiceService>();
        materials = new ArrayList<DetailedInvoiceMaterial>();
    }

    public DetailedInvoice(Date date, String status, CustomerSlot customerSlot) {
        super();
        this.date = date;
        this.status = status;
        this.customerSlot = customerSlot;
        services = new ArrayList<DetailedInvoiceService>();
        materials = new ArrayList<DetailedInvoiceMaterial>();
    }

    /* id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* date */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /* status */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /* customerSlot */
    public CustomerSlot getCustomerSlot() {
        return customerSlot;
    }

    public void setCustomerSlot(CustomerSlot customerSlot) {
        this.customerSlot = customerSlot;
    }

    /* services */
    public ArrayList<DetailedInvoiceService> getServices() {
        return services;
    }

    public void setServices(ArrayList<DetailedInvoiceService> services) {
        this.services = services;
    }

    /* materials */
    public ArrayList<DetailedInvoiceMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<DetailedInvoiceMaterial> materials) {
        this.materials = materials;
    }

}
