package com.example.eagle.lalala.PDM;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.example.eagle.lalala.PDM.BasicEnum.Authorities;
import com.example.eagle.lalala.PictureWork.HandlePicture;
import com.example.eagle.lalala.bean.FavortItem;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by NeilHY on 2016/4/26.
 */
public class MarksPDM {
    private long userId;
    private long markId;
    private String userName;
    private Bitmap icon;
    private Timestamp createTime;
    private Authorities authority;
    private double longitude;
    private double latitude;
    private String positionName;
    private String address;
    private String content;
    private String photo;
    private List<commentsPDM> comments;
    private List<likesPDM> likes;

    public MarksPDM() {
    }

    public MarksPDM(long userId, long markId, String userName,String icon,Timestamp createTime, double longitude, double latitude, String positionName, String address, String content, String photo, Authorities authority, List<commentsPDM> comments, List<likesPDM> likes) {
        this.userId = userId;
        this.markId = markId;
        this.userName=userName;
        this.icon = HandlePicture.StringToBitmap(icon);
        this.createTime = createTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.positionName = positionName;
        this.address = address;
        this.content = content;
        this.photo = photo;
        this.authority = authority;
        this.comments = comments;
        this.likes = likes;
    }

    public boolean hasFavort() {
        if (likes != null && likes.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean hasComment() {
        if (comments != null && comments.size() > 0) {
            return true;
        }
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = HandlePicture.StringToBitmap(icon);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMarkId() {
        return markId;
    }

    public void setMarkId(long markId) {
        this.markId = markId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public Authorities getAuthority() {
        return authority;
    }

    public void setAuthority(Authorities authority) {
        this.authority = authority;
    }

    public List<commentsPDM> getComments() {
        return comments;
    }

    public void setComments(List<commentsPDM> comments) {
        this.comments = comments;
    }

    public List<likesPDM> getLikes() {
        return likes;
    }

    public void setLikes(List<likesPDM> likes) {
        this.likes = likes;
    }

    public long getCurUserFavortId(long curUserId) {
        long favortid = -1;
        if ( hasFavort()) {
            for (likesPDM item : likes) {
                if (curUserId == item.getUserId() ) {
                    favortid = item.getLikeId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
