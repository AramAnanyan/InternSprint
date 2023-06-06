package Models;

import java.util.ArrayList;

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
    private String profilePicture;
    private ArrayList<String> invitations;



    public UserModel() {
    }
    public UserModel(String name, String email, String password,String surName,String id) {

        invitations=new ArrayList<>();
        this.name = name;
        this.email = email;
        this.surName = surName;
        this.password = password;
        this.id=id;
    }

    public UserModel(String name, String email, String password,String surName,ArrayList<String> invitations) {

        this.invitations=invitations;
        this.name = name;
        this.email = email;
        this.surName = surName;
        this.password = password;

    }

    public ArrayList<String> getInvitations(){
        return invitations;
    }
    public void setInvitations(ArrayList<String> invitations){
        this.invitations=invitations;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
