package com.devolper.complaintfyp.objects;

public class Complaint {
    public String getCat() {
        return cat;
    }

    public String getAddress() {
        return address;
    }

    public String getDetail() {
        return detail;
    }

    public String getIdentity() {
        return identity;
    }

    public String getPic() {
        return pic;
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

    public String getTracker() {
        return tracker;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    public Complaint(){

    }
    public Complaint(String cat, String address, String detail, String identity, String pic, String video, String date, String username, String status, String tracker, String key, String r_date) {
        this.cat = cat;
        this.address = address;
        this.detail = detail;
        this.identity = identity;
        this.pic = pic;
        this.video = video;
        this.date = date;
        this.username = username;
        this.status = status;
        this.tracker = tracker;
        this.key=key;
        this.resdate=r_date;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getResdate() {
        return resdate;
    }

    public void setResdate(String resdate) {
        this.resdate = resdate;
    }

    String resdate,key,cat,address,detail,identity,pic,video,date,username,status,tracker;
}
