package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.BaiTap;

public class UserModel {

    private String email;
    private String avatar;

    private int videoQuantity;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVideoQuantity() {
        return videoQuantity;
    }

    public void setVideoQuantity(int videoQuantity) {
        this.videoQuantity = videoQuantity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserModel(String email, int videoQuantity, String avatar) {
        this.email = email;
        this.videoQuantity = videoQuantity;
        this.avatar = avatar;
    }

    public UserModel() {
    }
}
