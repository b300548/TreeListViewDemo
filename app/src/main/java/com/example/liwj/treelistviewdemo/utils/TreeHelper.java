package com.example.liwj.treelistviewdemo.utils;

import com.example.liwj.treelistviewdemo.bean.FileBean;
import com.example.liwj.treelistviewdemo.bean.Node;

import java.util.ArrayList;
import java.util.List;

public class TreeHelper {


    /**
     * 传入我们普通的bean，转化为我们排序后的Node
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<Node> getSortedNodes(List<FileBean> datas, int defaultExpandLevel) throws IllegalArgumentException,IllegalAccessException{
        List<Node> result = new ArrayList<>();
        // 将用户数据转化为List<Node> 以及设置Node间关系
        List<Node> nodes = convertData2Node(datas);
        // 拿到根节点
        List<Node> rootNodes = getRootNodes(nodes);
        // 排序
        for (Node node: rootNodes){
            addNode(result, node,defaultExpandLevel,1);
        }
        return result;

    }

    /**
     * 过滤出所有显示的Node
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNode(List<Node> nodes){
        List<Node> result = new ArrayList<>();

        for (Node node : nodes){
            // 如果为根节点, 或者上层目录为展开状态
            if (node.isRoot() || node.isParentExpand()){
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

    /**
     *
     * 将我们的数据转化为数节点
     * @param datas
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static  List<Node> convertData2Node(List<FileBean> datas) throws IllegalArgumentException,IllegalAccessException{
        List<Node> nodes = new ArrayList<>();
        Node node;

        for (FileBean bean : datas){
            int id = bean.getId();
            int pid = bean.getParentId();
            String name = bean.getName();
            String uuid =bean.getUuid();
            String type = bean.getType();

            node = new Node(id,pid,name,uuid,type);
            nodes.add(node);
        }

        /**
         * 设置Node间，父子关系；让没两个接线都比较一次，即可设置其中的关系
         */
        for (int i=0;i< nodes.size();i++){
            Node n = nodes.get(i);
            for (int j = i+1;j<nodes.size();j++){
                Node m = nodes.get(j);
                if (m.getpId() == n.getId()){
                    n.getChildren().add(m);
                    m.setParent(n);
                }else if (m.getId() == n.getId()){
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        // 设置图片
        for (Node n : nodes){
            setNodeIcon(n);
        }

        return nodes;
    }

    private static List<Node> getRootNodes(List<Node> nodes){
        List<Node> root = new ArrayList<>();
        for (Node node : nodes){
            if (node.isRoot()){
                root.add(node);
            }
        }
        return root;
    }

    /**
     * 吧一个节点上的所有内容都挂上去
     * @param nodes
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<Node> nodes,Node node,int defaultExpandLevel, int currentLevel){
        nodes.add(node);
        if (defaultExpandLevel >= currentLevel){
            node.setExpand(true);
        }
        if (node.isLeaf()){
            return;
        }
        for(int i=0;i< node.getChildren().size();i++){
            addNode(nodes, node.getChildren().get(i),defaultExpandLevel,currentLevel+1);
        }
    }

    private static void setNodeIcon(Node node){
        if (!node.isLeaf() && node.isExpand()){
            node.setIcon(android.R.drawable.arrow_down_float);
        }else if (!node.isLeaf()  && !node.isExpand()){
            node.setIcon(android.R.drawable.ic_media_play);
        }else {
            node.setIcon(-1);
        }
    }


}
