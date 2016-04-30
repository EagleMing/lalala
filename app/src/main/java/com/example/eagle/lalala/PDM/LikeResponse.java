package com.example.eagle.lalala.PDM;

/**
 * Created by lenovo on 2016/4/25.
 */
public class LikeResponse {
    private Long likeID;
    private Long friendID;
    private Long markID;
    private String friendName;


    public LikeResponse() {
    }

    public LikeResponse(Long likeID, Long friendID, Long markID, String friendName) {
        this.likeID = likeID;
        this.friendID = friendID;
        this.markID = markID;
        this.friendName = friendName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public Long getLikeID() {
        return likeID;
    }

    public void setLikeID(Long likeID) {
        this.likeID = likeID;
    }

    public Long getFriendID() {
        return friendID;
    }

    public void setFriendID(Long friendID) {
        this.friendID = friendID;
    }

    public Long getMarkID() {
        return markID;
    }

    public void setMarkID(Long markID) {
        this.markID = markID;
    }
}
