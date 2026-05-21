package model;

import java.io.Serializable;

public class Customer implements Serializable {
    private int id;
    private String name;
    private String phone;

    public Customer() {
        super();
    }

    public Customer(String name, String phone) {
        super();
        this.name = name;
        this.phone = phone;
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
    
}
