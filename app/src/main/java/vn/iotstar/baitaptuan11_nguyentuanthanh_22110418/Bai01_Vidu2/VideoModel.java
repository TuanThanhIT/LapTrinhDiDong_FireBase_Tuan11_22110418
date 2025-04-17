package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.Bai01_Vidu2;

import java.io.Serializable;

public class VideoModel implements Serializable {
    private int id;
    private String title;
    private String description;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VideoModel(int id, String url, String description, String title) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.title = title;
    }

    public VideoModel() {
    }
}
