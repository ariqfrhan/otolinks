package maf.mobile.finalprojectpapb.model;

public class User {
    public User(){

    }
    public User(String id, String username, String email, String phone, String imgUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgUrl;
    }

    private String id;
    private String username;
    private String email;
    private String phone;
    private String imgUrl;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
