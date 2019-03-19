package com.example.liwj.treelistviewdemo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liwj.treelistviewdemo.bean.FileBean;
import com.example.liwj.treelistviewdemo.bean.Node;

import java.util.List;

public class SimpleTreeAdapter extends TreeListViewAdapter {
    private static final String TAG = "SimpleTreeAdapter";

    public SimpleTreeAdapter(ListView mTree, Context context, List<FileBean> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }


    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            viewHolder.lable = (TextView)convertView.findViewById(R.id.lable);
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mContext != null){
//                        MainActivity activity = (MainActivity)mContext;
//                        Log.d(TAG, "node children: " + node.getChildren().size());
//                        if (node.getChildren().size() > 0){
//                            expandOrCollapse(position);
//                        }else {
//                            activity.getEntitys(node.getUuid(),node.getId());
//                            expandOrCollapse(position);
//                            activity.setPosition(position);
//                        }
//                    }
                    expandOrCollapse(position);
                }
            });
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1){
            viewHolder.icon.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        viewHolder.lable.setText(node.getName());

        return convertView;
    }

    private final class ViewHolder{
        ImageView icon;
        TextView lable;
    }

}
