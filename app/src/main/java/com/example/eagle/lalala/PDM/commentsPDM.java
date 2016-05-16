package com.example.eagle.lalala.PDM;

import java.sql.Timestamp;

/**
 * Created by NeilHY on 2016/4/26.
 */
public class commentsPDM {
    private long commentId;
    private long markId;
    private long friendId;
    private String friendName;
    private String content;
    private Timestamp commentTime;

    public commentsPDM() {
    }

    public commentsPDM( long markId, long friendId, String friendName, String content, Timestamp commentTime) {
        //this.commentId = commentId;
        this.markId = markId;
        this.friendId = friendId;
        this.friendName = friendName;
        this.content = content;
        this.commentTime = commentTime;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getMarkId() {
        return markId;
    }

    public void setMarkId(long markId) {
        this.markId = markId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }
}
