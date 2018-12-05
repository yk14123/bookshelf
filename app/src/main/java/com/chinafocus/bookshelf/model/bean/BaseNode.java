package com.chinafocus.bookshelf.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 针对多级list嵌套的数据，设计BaseNode抽象节点类
 */
public abstract class BaseNode<T> {

    //当前节点的层级，初始值-1
    private int _level = -1;
    //所有的孩子节点
    private List<BaseNode> _childrenList = new ArrayList<>();
    //父亲节点
    private BaseNode _parent;
    //图标资源ID(是否存在下拉资源图标icon)
    private int _icon;
    //当前状态是否展开
    private boolean isExpand = false;

    public abstract T getId();//得到当前节点ID

    public abstract T getParentId();//得到当前节点的父ID

    public abstract String getLabel();//要显示的内容

    public abstract boolean parent(BaseNode dest);//判断当前节点是否是dest的父亲节点

    public abstract boolean child(BaseNode dest);//判断当前节点是否是dest的孩子节点

    public int getLevel() {
        if (_level == -1) {//如果是 -1 的话就递归获取
            //因为是树形结构，所以此处想要得到当前节点的层级
            //，必须递归调用，但是递归效率低下，如果每次都递归获取会严重影响性能，所以我们把第一次
            //得到的结果保存起来避免每次递归获取
            _level = _parent == null ? 1 : _parent.getLevel() + 1;
            return _level;
        }
        return _level;
    }

    public void setLevel(int _level) {
        this._level = _level;
    }

    public List<BaseNode> getChildrenList() {
        return _childrenList;
    }

    public void setChildrenList(List<BaseNode> _childrenList) {
        this._childrenList = _childrenList;
    }

    public BaseNode getParent() {
        return _parent;
    }

    public void setParent(BaseNode _parent) {
        this._parent = _parent;
    }

    public int getIcon() {
        return _icon;
    }

    public void setIcon(int _icon) {
        this._icon = _icon;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (isExpand) {
//            _icon = R.mipmap.collapse;
        } else {
//            _icon = R.mipmap.expand;
        }
    }

    public boolean isRoot() {
        return _parent == null;
    }

    public boolean isLeaf() {
        return _childrenList.size() <= 0;
    }

}
