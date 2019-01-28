package com.chinafocus.bookshelf.bean;

import java.util.ArrayList;

public class ShelvesCategoryRawBean {

    /**
     * code : 200
     * data : {"bg":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bg.jpg","categories":[{"shelfId":2,"name":"现当代文学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/contemporaryIcon.png","categoryId":14,"status":1},{"shelfId":2,"name":"习近平著作","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookIcon.png","categoryId":16,"status":1},{"shelfId":2,"name":"经史典集","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/historyIcon.png","categoryId":13,"status":1},{"shelfId":2,"name":"思想哲学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/thoughtIcon.png","categoryId":18,"status":1},{"shelfId":2,"name":"经济学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/economyIcon.png","categoryId":17,"status":1},{"shelfId":2,"name":"历史与科学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/scienceIcon.png","categoryId":15,"status":1},{"shelfId":2,"name":"军事学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/militaryIcon.png","categoryId":10,"status":1},{"shelfId":2,"name":"政治学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/politicsIcon.png","categoryId":12,"status":1},{"shelfId":2,"name":"外国名著","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/foreignIcon.png","categoryId":11,"status":1}],"clickCount":0,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookcaseLogo.png","metadata":{},"name":"习近平的\u201c书柜\u201d","shelfId":2,"status":1}
     * msg : success
     */

    private int code;
    private ArrayList<ShelvesCategoryResultBean> data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<ShelvesCategoryResultBean> getData() {
        return data;
    }

    public void setData(ArrayList<ShelvesCategoryResultBean> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 九宫格内容展示
     */
    public static class ShelvesCategoryResultBean {

        /**
         * parent : 2
         * bg : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bg.jpg
         * customerId : 1
         * icon : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/contemporaryIcon.png
         * name : 现当代文学
         * description : 描述5
         * logo : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookcaseLogo.png
         * id : 15
         * categoryId : 15
         * queue : 1
         */

        private int parent;
        private String bg;
        private int customerId;
        private String icon;
        private String name;
        private String description;
        private String logo;
        private int id;
        private int categoryId;
        private int queue;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public String getBg() {
            return bg;
        }

        public void setBg(String bg) {
            this.bg = bg;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getQueue() {
            return queue;
        }

        public void setQueue(int queue) {
            this.queue = queue;
        }
    }
}
