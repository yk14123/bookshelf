package com.chinafocus.bookshelf.model.bean;

import java.util.List;

public class ShelvesResultBean {

    /**
     * bg : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bg.jpg
     * categories : [14,16,13,18,17,15,10,12,11]
     * clickCount : 0
     * logo : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookcaseLogo.png
     * metadata : {}
     * name : 习近平的“书柜”
     * shelfId : 2
     * status : 1
     */

    private String bg;
    private int clickCount;
    private String logo;
    private MetadataBean metadata;
    private String name;
    private int shelfId;
    private int status;
    private List<Integer> categories;

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShelfId() {
        return shelfId;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public static class MetadataBean {
    }
}
