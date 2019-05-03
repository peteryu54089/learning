package model;

public class User {

    final private String id;
    final private String password;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public String getPassword() {
        return password;
    }
}
