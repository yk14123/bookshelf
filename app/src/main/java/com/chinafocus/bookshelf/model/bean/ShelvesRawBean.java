package com.chinafocus.bookshelf.model.bean;

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
}
