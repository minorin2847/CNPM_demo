package model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String position;

    public User() {
        super();
    }

    public User(String username, String password, String fullName, String position) {
        super();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.position = position;
    }

    /* id */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    /* username */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /* password */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /* fullName */
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /* position */
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
}