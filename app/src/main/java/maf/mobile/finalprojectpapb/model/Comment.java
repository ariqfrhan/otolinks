package maf.mobile.finalprojectpapb.model;

public class Comment {
    private String commentId;
    private String userId;
    private String comment;
    private String timestamp;

    public Comment(String commentId, String userId, String comment, String timestamp) {
        this.commentId = commentId;
        this.userId = userId;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public Comment(){

    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
