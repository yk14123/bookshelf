package com.chinafocus.bookshelf.bean;

public class ShelvesCategoryRawBean {

    /**
     * code : 200
     * data : {"bg":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bg.jpg","categories":[{"shelfId":2,"name":"现当代文学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/contemporaryIcon.png","categoryId":14,"status":1},{"shelfId":2,"name":"习近平著作","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookIcon.png","categoryId":16,"status":1},{"shelfId":2,"name":"经史典集","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/historyIcon.png","categoryId":13,"status":1},{"shelfId":2,"name":"思想哲学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/thoughtIcon.png","categoryId":18,"status":1},{"shelfId":2,"name":"经济学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/economyIcon.png","categoryId":17,"status":1},{"shelfId":2,"name":"历史与科学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/scienceIcon.png","categoryId":15,"status":1},{"shelfId":2,"name":"军事学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/militaryIcon.png","categoryId":10,"status":1},{"shelfId":2,"name":"政治学","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/politicsIcon.png","categoryId":12,"status":1},{"shelfId":2,"name":"外国名著","customerId":1,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/foreignIcon.png","categoryId":11,"status":1}],"clickCount":0,"logo":"https://bookcases.oss-cn-beijing.aliyuncs.com/imgs/bookcaseLogo.png","metadata":{},"name":"习近平的\u201c书柜\u201d","shelfId":2,"status":1}
     * msg : success
     */

    private int code;
    private ShelvesCategoryResultBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ShelvesCategoryResultBean getData() {
        return data;
    }

    public void setData(ShelvesCategoryResultBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
