package com.devolper.complaintfyp.objects;

/**
 * Created by waqas ahmed on 7/22/2019.
 */

public class ComplaintData {
    public String getCat() {
        return catagory;
    }

    public String getAddress() {
        return address;
    }

    public String getDetail() {
        return details;
    }

    public String getIdentity() {
        return identity;
    }

    public String getPic() {
        return picture;
    }

    public String getVideo() {
        return video;
    }

    public String getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    /*public String getTracker() {
        return tracker;
    }*/

    public void setCat(String cat) {
        this.catagory = cat;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDetail(String detail) {
        this.details = detail;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setPic(String pic) {
        this.picture = pic;
    }

    public void setVideo(String video) {
        this.video = video;
    }
    public void setUserID(String video) {
        this.userID = video;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSubcatagory(String tracker) {
        this.subcatagory = tracker;
    }
    public String getSubcatagory() {
        return  this.subcatagory;
    }

    public ComplaintData(){

    }
    public ComplaintData(String cat, String address,  String identity, String pic, String video, String date,String userID,  String status, String subcatagory,String key,String username,String details) {
        this.catagory = cat;
        this.address = address;
        this.details = details;
        this.identity = identity;
        this.picture = pic;
        this.video = video;
        this.date = date;
        this.username = username;
        this.status = status;
        this.subcatagory = subcatagory;
        this.key=key;
        this.userID=userID;
        //this.resdate=r_date;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
    public String getUserID() {
        return userID;
    }

    /*public String getResdate() {
        return date;
    }

    public void setResdate(String resdate) {
        this.date = resdate;
    }*/

    String address,catagory,date,identity,picture,status,subcatagory,video,key,details,username,userID;


}