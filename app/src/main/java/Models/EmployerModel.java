package Models;

public class EmployerModel {
    private String name;
    private String surName;
    private String email;
    private String password;
    private String workPlace;

    private String id;


    public EmployerModel() {


    }

    public EmployerModel(String name, String surName, String workPlace, String email, String password,String id) {


        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.workPlace = workPlace;
        this.surName = surName;
    }


    public String getid() {
        return id;
    }

    public void setid(String id) {
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

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
}
