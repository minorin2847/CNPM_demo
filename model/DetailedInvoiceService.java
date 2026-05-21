package model;

import java.io.Serializable;

public class DetailedInvoiceService implements Serializable {
    private int id;
    private int price;
    private int total;
    private ServiceStaff serviceStaff;
    private Service service;

    public DetailedInvoiceService() {
        super();
    }

    public DetailedInvoiceService(int price, int total, ServiceStaff serviceStaff, Service service) {
        super();
        this.price = price;
        this.total = total;
        this.serviceStaff = serviceStaff;
        this.service = service;
    }

    /* id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* price */
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    /* total */
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /* serviceStaff */
    public ServiceStaff getServiceStaff() {
        return serviceStaff;
    }

    public void setServiceStaff(ServiceStaff serviceStaff) {
        this.serviceStaff = serviceStaff;
    }

    /* service */
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

}
