package com.chinafocus.bookshelf.model.bean;

import java.util.List;

public class ShelvesCategoryResultBean {

    /**
     * bg : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bg.jpg
     * categories : [{"shelfId":2,"name":"现当代文学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/contemporaryIcon.png","categoryId":14,"status":1},{"shelfId":2,"name":"习近平著作","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookIcon.png","categoryId":16,"status":1},{"shelfId":2,"name":"经史典集","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/historyIcon.png","categoryId":13,"status":1},{"shelfId":2,"name":"思想哲学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/thoughtIcon.png","categoryId":18,"status":1},{"shelfId":2,"name":"经济学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/economyIcon.png","categoryId":17,"status":1},{"shelfId":2,"name":"历史与科学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/scienceIcon.png","categoryId":15,"status":1},{"shelfId":2,"name":"军事学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/militaryIcon.png","categoryId":10,"status":1},{"shelfId":2,"name":"政治学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/politicsIcon.png","categoryId":12,"status":1},{"shelfId":2,"name":"外国名著","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/foreignIcon.png","categoryId":11,"status":1}]
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
    private List<ShelvesCategoriesDetailBean> categories;

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

    public List<ShelvesCategoriesDetailBean> getCategories() {
        return categories;
    }

    public void setCategories(List<ShelvesCategoriesDetailBean> categories) {
        this.categories = categories;
    }

    public static class MetadataBean {
    }

    public static class ShelvesCategoriesDetailBean {
        /**
         * shelfId : 2
         * name : 现当代文学
         * customerId : 1
         * logo : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/contemporaryIcon.png
         * categoryId : 14
         * status : 1
         */

        private int shelfId;
        private String name;
        private int customerId;
        private String logo;
        private int categoryId;
        private int status;

        public int getShelfId() {
            return shelfId;
        }

        public void setShelfId(int shelfId) {
            this.shelfId = shelfId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
