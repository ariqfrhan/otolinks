package maf.mobile.finalprojectpapb.model;


import java.util.List;

public class Post {

    private String userId;
    private String content;
    private String contentPhoto;
    private String timestamp;
    private String postId;

    public Post(){

    }

    public Post(String userId, String postId, String content, String contentPhoto, String timestamp) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.contentPhoto = contentPhoto;
        this.timestamp = timestamp;
    }
    public String getPostId(){return postId;}
    public void setPostId(String postId){
        this.postId = postId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentPhoto() {
        return contentPhoto;
    }

    public void setContentPhoto(String contentPhoto) {
        this.contentPhoto = contentPhoto;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
