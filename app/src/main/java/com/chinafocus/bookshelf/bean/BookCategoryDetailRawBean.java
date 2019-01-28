package com.chinafocus.bookshelf.bean;

import java.util.List;

public class BookCategoryDetailRawBean {


    private int code;
    private BookCategoryDetailResultBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BookCategoryDetailResultBean getData() {
        return data;
    }

    public void setData(BookCategoryDetailResultBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class BookCategoryDetailResultBean {


        private String categoryName;
        private List<BooksCategoryDetailFinalBean> epubs;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<BooksCategoryDetailFinalBean> getEpubs() {
            return epubs;
        }

        public void setEpubs(List<BooksCategoryDetailFinalBean> epubs) {
            this.epubs = epubs;
        }

        public static class BooksCategoryDetailFinalBean {


            private String cover;
            private String author;
            private String name;
            private int epubMappingId;
            private String description;

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getEpubMappingId() {
                return epubMappingId;
            }

            public void setEpubMappingId(int epubMappingId) {
                this.epubMappingId = epubMappingId;
            }

        }
    }
}
