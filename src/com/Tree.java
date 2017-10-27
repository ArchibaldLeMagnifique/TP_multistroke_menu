package com;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    public List<Tree<T>> children = new ArrayList<Tree<T>>();
    public Tree<T> parent = null;
    public T data = null;

    public Tree(T data) {
        this.data = data;
    }

    public Tree(T data, Tree<T> parent) {
        this.data = data;
        this.parent = parent;
        parent.children.add(this);
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setParent(Tree<T> parent) {
        parent.addChild(this);
        this.parent = parent;
    }

    public void addChild(T data) {
        Tree<T> child = new Tree<T>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Tree<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }
}