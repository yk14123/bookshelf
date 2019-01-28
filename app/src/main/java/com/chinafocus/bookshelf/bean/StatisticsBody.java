package com.chinafocus.bookshelf.bean;

/**
 * @author
 * @date 2019/1/9
 * description：
 */
public class StatisticsBody {

    private String end;
    private String id;
    private String type;

    public StatisticsBody(String end, String id, String type) {
        this.end = end;
        this.id = id;
        this.type = type;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
