package com.devolper.complaintfyp.objects;

public class Users {
     public Users(){

     }


    public void setStatus(String status) {
        this.status = status;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setToken(String key) {
        this.token = key;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }
    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public String getReg() {
        return regno;
    }
    public String getToken() {
        return token;
    }

    public Users(String name, String gender, String username, String email, String regno, String status) {
        this.name = name;
        this.gender = gender;
        this.username = username;
        this.email = email;
        this.regno = regno;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    String name,gender,username,email,regno,status,key,token;
}
