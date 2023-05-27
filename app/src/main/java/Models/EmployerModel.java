package Models;

import java.util.ArrayList;
import java.util.List;

public class EmployerModel {
    private String name;
    private String surName;
    private String email;
    private String password;
    private String workPlace;
    private String role;
    private String about;
    private String phone;

    private String id;
    private ArrayList<String> registeredUsers;
    private ArrayList<String> confirmedUsers;


    public EmployerModel() {
    }

    public EmployerModel(String name, String surName, String workPlace, String email, String password,String id) {
        this.registeredUsers=new ArrayList<String>();
        this.confirmedUsers=new ArrayList<String>();

        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.workPlace = workPlace;
        this.surName = surName;
    }
    public EmployerModel(String name, String surName, String workPlace,String role, String email, String password,String id) {
        this.registeredUsers=new ArrayList<String>();
        this.confirmedUsers=new ArrayList<String>();
        this.role=role;
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.workPlace = workPlace;
        this.surName = surName;
    }
    public EmployerModel(String name, String surName, String workPlace, String email, String password,String id,ArrayList<String> registeredUsers) {
        this.registeredUsers=registeredUsers;
        this.confirmedUsers=new ArrayList<String>();
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.workPlace = workPlace;
        this.surName = surName;
    }
    public EmployerModel(String name, String surName, String workPlace,String role, String email, String password,String id,ArrayList<String> registeredUsers) {
        this.registeredUsers=registeredUsers;
        this.confirmedUsers=new ArrayList<String>();
        this.role=role;
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.workPlace = workPlace;
        this.surName = surName;
    }



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addRegUserId(String id){
        registeredUsers.add(id);
    }
    public void setRegUsers(ArrayList<String> users){
        registeredUsers=users;
    }
    public ArrayList<String> getRegUsers(){
        return registeredUsers;
    }
    public String getRegUserId(int id){
        return registeredUsers.get(id);
    }
    public void deleteRegUserId(int id){
        registeredUsers.remove(id);
    }
    public void deleteRegUserId(String id){
        registeredUsers.remove(id);
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

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
}
