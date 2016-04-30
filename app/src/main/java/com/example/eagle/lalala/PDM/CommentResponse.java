package com.example.eagle.lalala.PDM;

import java.sql.Timestamp;

/**
 * Created by lenovo on 2016/4/25.
 */
public class CommentResponse {
    private String content;
    private Timestamp commentTime;
    private Long commentId;
    private Long markID;
    private Long friendID;
    private String friendName;

    public CommentResponse(String content, Timestamp commentTime, Long commentId, Long markID, Long friendID, String friendName) {
        this.content = content;
        this.commentTime = commentTime;
        this.commentId = commentId;
        this.markID = markID;
        this.friendID = friendID;
        this.friendName = friendName;
    }

    public CommentResponse() {
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

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getMarkID() {
        return markID;
    }

    public void setMarkID(Long markID) {
        this.markID = markID;
    }

    public Long getFriendID() {
        return friendID;
    }

    public void setFriendID(Long friendID) {
        this.friendID = friendID;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
