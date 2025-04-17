package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.Bai01_Vidu1;

import java.io.Serializable;

public class Video1Model implements Serializable {

    private String title;
    private String desc;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Video1Model(String title, String url, String desc) {
        this.title = title;
        this.url = url;
        this.desc = desc;
    }

    public Video1Model() {
    }
}
