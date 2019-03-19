package com.example.liwj.treelistviewdemo.bean;

import java.util.ArrayList;
import java.util.List;


public class Node {
    private int id;

    /**
     * 根节点pId 为0
     */
    private int pId = 0;

    private String name;

    private String uuid;

    private String type;

    /**
     * 当前级别
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    private int icon;

    /**
     * 下一级的子Node
     */
    private List<Node> children = new ArrayList<>();

    /**
     * 父Node
     */
    private Node parent;

    public Node(){

    }

    public Node(int id, int pId,String name, String uuid, String type){
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.uuid = uuid;
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getpId() {
        return pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isExpand() {
        return isExpand;
    }


    public List<Node> getChildren(){
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * 是否为根节点
     * @return
     */
    public boolean isRoot(){
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     * @return
     */
    public boolean isParentExpand(){
        if (parent == null){
            return false;
        }
        return parent.isExpand;
    }

    public boolean isLeaf(){
        boolean isLeaf = false;
        if ("room".equals(type)){
            isLeaf = true;
        }
        return isLeaf;
    }

    /**
     * 获取level
     */
    public int getLevel(){
        return parent == null?  0 : parent.getLevel() + 1;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
        if (!expand){
            for (Node node : children){
                node.setExpand(expand);
            }
        }
    }

}
