package model;

import java.io.Serializable;

public class ServiceStaff implements Serializable{
    private int id;
    private String name;
    private String phone;
    private String status;

    public ServiceStaff() {
        super();
    }

    public ServiceStaff(String name, String phone, String status) {
        super();
        this.name = name;
        this.phone = phone;
        this.status = status;
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

    /* phone */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /* status */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
