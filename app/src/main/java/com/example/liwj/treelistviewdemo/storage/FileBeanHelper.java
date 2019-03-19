package com.example.liwj.treelistviewdemo.storage;

import android.app.Activity;
import android.content.Context;

import com.example.liwj.treelistviewdemo.MainApplication;
import com.example.liwj.treelistviewdemo.bean.DaoSession;
import com.example.liwj.treelistviewdemo.bean.FileBean;
import com.example.liwj.treelistviewdemo.bean.FileBeanDao;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class FileBeanHelper {
    FileBeanDao mFileBeanDao;
    Query<FileBean> mQuery;

    public FileBeanHelper(Activity context){
        DaoSession daoSession = ((MainApplication)context.getApplication()).getDaoSession();
        mFileBeanDao = daoSession.getFileBeanDao();
    }

    public void addBean(FileBean bean){
        mFileBeanDao.insert(bean);
    }

    public List<FileBean> query(){
        List<FileBean> list = new ArrayList<>();
        mQuery = mFileBeanDao.queryBuilder().build();

        list = mQuery.list();
        return list;
    }

}
