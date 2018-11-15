package com.chinafocus.bookshelf.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsEntity implements Serializable {

    private long updateTime;
    private boolean error;
    private List<NewsResultEntity> results = new ArrayList<>();

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<NewsResultEntity> getResults() {
        return results;
    }

    public void setResults(List<NewsResultEntity> results) {
        this.results = results;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
