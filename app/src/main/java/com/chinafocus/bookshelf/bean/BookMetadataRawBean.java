package com.chinafocus.bookshelf.bean;

import java.util.List;

public class BookMetadataRawBean {

    private int code;
    private BookMetadataResultBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BookMetadataResultBean getData() {
        return data;
    }

    public void setData(BookMetadataResultBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class BookMetadataResultBean {

        private String cover;
        private String author;
        private String name;
        private String description;
        private String publisher;
        private String comment;
        private String category;
        private List<TocBean> toc;

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

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<TocBean> getToc() {
            return toc;
        }

        public void setToc(List<TocBean> toc) {
            this.toc = toc;
        }

        public static class TocBean extends BaseNode<String> {

            private int visible;
            private String id;
            //自定义字段parentId,当前Node以来上级Node的唯一标识
            private String parentId;
            private String href;
            private String title;
            private String full;
            private List<TocBean> children;

            @Override
            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            @Override
            public String getLabel() {
                return title;
            }

            //当前节点是否是父节点
            @Override
            public boolean parent(BaseNode dest) {
                return id.equals(dest.getParentId());
            }

            //当前节点是否是孩子节点
            @Override
            public boolean child(BaseNode dest) {
                return parentId.equals(dest.getId());
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFull() {
                return full;
            }

            public void setFull(String full) {
                this.full = full;
            }

            public List<TocBean> getChildren() {
                return children;
            }

            public void setChildren(List<TocBean> children) {
                this.children = children;
            }

        }
    }
}
