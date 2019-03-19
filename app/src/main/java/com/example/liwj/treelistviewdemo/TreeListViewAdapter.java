package com.example.liwj.treelistviewdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.liwj.treelistviewdemo.bean.FileBean;
import com.example.liwj.treelistviewdemo.bean.Node;
import com.example.liwj.treelistviewdemo.utils.TreeHelper;

import java.util.List;

public abstract class TreeListViewAdapter extends BaseAdapter {
    private static final String TAG = "TreeListViewAdapter";
    protected Context mContext;

    protected ListView mTree;

    /**
     * 存储所有可见的Node
     */
    protected List<Node> mNodes;
    protected LayoutInflater mInflater;

    /**
     * 存储所有的Node
     */
    protected List<Node> mAllNodes;


    /**
     * 点击回调的接口
     */
    private OnTreeNodeClickListener onTreeNodeClickListener;



    public interface OnTreeNodeClickListener{
        void onClick(Node node, int position);
    }

    public void setOnTreeNodeClickListener(final OnTreeNodeClickListener onTreeNodeClickListener){
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    public TreeListViewAdapter(ListView tree, Context context, List<FileBean> datas, int defaultExpandLevel) throws  IllegalArgumentException, IllegalAccessException{
        mContext = context;

        mTree = tree;

//        for (FileBean fileBean : datas){
//            Log.d(TAG, "----datas     "+"id: " + fileBean.getId() + "   pid: "+ fileBean.getParentId()+"   name: " + fileBean.getName());
//        }

        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        mInflater = LayoutInflater.from(context);

        Log.d(TAG, "TreeListViewAdapter: mNodes.size" + mNodes.size());

        /**
         * 设置节点点时，可以展开以及关闭，并且将ItemClick时间继续往外公布
         */
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"点击了第" + position + "项");
            }
        });
    }

    public void expandOrCollapse(int position){

            Node n = mNodes.get(position);
            n.setExpand(!n.isExpand());
            mNodes = TreeHelper.filterVisibleNode(mAllNodes);
            for (Node node : mNodes){
                Log.d(TAG, "-----mNodes:  name: " + node.getName());
            }
            //刷新视图
        Log.d(TAG, "notify: expand");
            notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return mNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);

        convertView.setPadding(node.getLevel()*30,3,3,3);
        return convertView;
    }

    public List<Node> getNodes() {
        return mNodes;
    }

    public OnTreeNodeClickListener getOnTreeNodeClickListener() {
        return onTreeNodeClickListener;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);
}
