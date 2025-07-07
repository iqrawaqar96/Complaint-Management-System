package com.devolper.complaintfyp.objects;

public class Tokens {
     public Tokens(){

     }

    public void setToken(String key) {
        this.token = key;
    }

    public String getToken() {
        return token;
    }

    public Tokens(String status) {
        this.token = status;
    }

    String token;
}
