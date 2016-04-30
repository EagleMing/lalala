package com.example.eagle.lalala.PDM;


import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/25.
 */
public class MarkItemResponse {
    private String content;
    private String photo;
    private int authority;
    private Long userID;
    private Long markID;
    private String lbsID;
    private Timestamp createTime;
    private String userName;
    private String icon;

    private Double longitude;
    private Double latitude;
    private String positionName;

    ArrayList<CommentResponse> comments;
    ArrayList<LikeResponse> likes;

    public MarkItemResponse() {
    }

    public MarkItemResponse(String content, String photo, int authority, Long userID, Long markID, String lbsID, Timestamp createTime, Double longitude, Double latitude, String positionName, ArrayList<CommentResponse> comments, ArrayList<LikeResponse> likes, String userName, String icon) {
        this.content = content;
        this.photo = photo;
        this.authority = authority;
        this.userID = userID;
        this.markID = markID;
        this.lbsID = lbsID;
        this.createTime = createTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.positionName = positionName;
        this.comments = comments;
        this.likes = likes;
        this.userName = userName;
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getMarkID() {
        return markID;
    }

    public void setMarkID(Long markID) {
        this.markID = markID;
    }

    public String getLbsID() {
        return lbsID;
    }

    public void setLbsID(String lbsID) {
        this.lbsID = lbsID;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public ArrayList<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentResponse> comments) {
        this.comments = comments;
    }

    public ArrayList<LikeResponse> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<LikeResponse> likes) {
        this.likes = likes;
    }
}

