package com.chinafocus.bookshelf.bean;

/**
 * @author
 * @date 2018/12/25
 * description：
 */
public class TocResultBean {

    public int level;

    public String title;

    public String full;

    @Override
    public String toString() {
        return "TocResultBean{" +
                "level=" + level +
                ", title='" + title + '\'' +
                ", full='" + full + '\'' +
                '}';
    }
}
