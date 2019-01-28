package com.chinafocus.bookshelf.bean;

import java.util.List;

public class ShelvesRawBean  {


    /**
     * code : 200
     * data : [{"bg":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bg.jpg","categories":[14,16,13,18,17,15,10,12,11],"clickCount":0,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookcaseLogo.png","metadata":{},"name":"习近平的\u201c书柜\u201d","shelfId":2,"status":1}]
     * msg : success
     */

    private int code;
    private String msg;
    private java.util.List<ShelvesResultBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ShelvesResultBean> getData() {
        return data;
    }

    public void setData(List<ShelvesResultBean> data) {
        this.data = data;
    }


    /**
     * 书柜内容展示
     */
    public static class ShelvesResultBean {

        /**
         * parent : 2
         * metadata : {"description":"123123"}
         * shelfId : 2
         * bg : https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bg.jpg
         * customerId : 1
         * name : 习近平的“书柜”
         * id : 2
         * status : 1
         */

        private int parent;
        private ShelvesMetadataBean metadata;
        private int shelfId;
        private String bg;
        private int customerId;
        private String name;
        private int id;
        private int status;

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public ShelvesMetadataBean getMetadata() {
            return metadata;
        }

        public void setMetadata(ShelvesMetadataBean metadata) {
            this.metadata = metadata;
        }

        public int getShelfId() {
            return shelfId;
        }

        public void setShelfId(int shelfId) {
            this.shelfId = shelfId;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class ShelvesMetadataBean {
            /**
             * description : 123123
             */

            private String description;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }
}
