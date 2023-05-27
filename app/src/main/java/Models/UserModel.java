package Models;

public class UserModel {
    private String name;
    private String email;
    private String password;
    private String surName;
    private String age;
    private String phone;
    private String workplace;
    private String education;
    private String about;
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
    /*public UserModel(String name, String email, String password,String surName,String id,String age,String phone,String education,String about,String workplace) {

        this.name = name;
        this.age = age;
        this.workplace = workplace;
        this.education = education;
        this.about = about;
        this.email = email;
        this.surName = surName;
        this.password = password;
        this.id=id;

    }*/
    public UserModel(String name, String email, String password,String surName) {

        this.name = name;
        this.email = email;
        this.surName = surName;
        this.password = password;

    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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
