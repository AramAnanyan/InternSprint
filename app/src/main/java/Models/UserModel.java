package Models;

public class UserModel {
    private String name;
    private String email;
    private String password;
    private String surName;

    private String id;



    public UserModel() {
    }
    public UserModel(String name, String email, String password,String surName,String id) {

        this.name = name;
        this.email = email;
        this.surName = surName;
        this.password = password;
        this.id=id;

    }
    public UserModel(String name, String email, String password,String surName) {

        this.name = name;
        this.email = email;
        this.surName = surName;
        this.password = password;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }




}
