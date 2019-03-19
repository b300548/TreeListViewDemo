package com.example.liwj.treelistviewdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.liwj.treelistviewdemo.bean.FileBean;
import com.example.liwj.treelistviewdemo.bean.Node;
import com.example.liwj.treelistviewdemo.storage.FileBeanHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int MSG_ERR = -1;
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_SUCCESS_ENTITY = 1;

    private List<FileBean> mDatas = new ArrayList<FileBean>();
    private ListView mTree;
    private TreeListViewAdapter mAdapter;

    private FileBeanHelper mFileBeanHelper;


    private volatile int count = 1;



    Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_ERR:
                    Toast.makeText(MainActivity.this,"获取实体失败...",Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SUCCESS:
                    try{
                        mAdapter = new SimpleTreeAdapter(mTree, MainActivity.this, mDatas, 0);
//                        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//                            @Override
//                            public void onClick(Node node, int position) {
//                                Log.d(TAG, "id:" + node.getId() + "pId:" + node.getpId() + "name: " + node.getName()+ "   uuid"  + node.getUuid());
//                                if (!(node.getChildren().size() > 0)){
//                                    getEntitys(node.getUuid(),node.getId());
//                                }else {
//                                    mAdapter.expandOrCollapse(position);
//                                }
//                                Toast.makeText(MainActivity.this, "onclick", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        mTree.setAdapter(mAdapter);
                    } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case MSG_SUCCESS_ENTITY:
                    try
                    {
                        mAdapter = new SimpleTreeAdapter(mTree, MainActivity.this, mDatas, 0);
//                        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//                            @Override
//                            public void onClick(Node node, int position) {
//                                Log.d(TAG, "id:" + node.getId() + "pId:" + node.getpId() + "name: " + node.getName()+ "   uuid"  + node.getUuid());
//                                if (!(node.getChildren().size() > 0)){
//                                    getEntitys(node.getUuid(),node.getId());
//                                }else {
//                                    mAdapter.expandOrCollapse(position);
//                                }
//                                Toast.makeText(MainActivity.this, "onclick", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                            mTree.setAdapter(mAdapter);
//                            mAdapter.expandOrCollapse(position);
                            Log.d("curThread", "curThread(Handler); notify");
                        } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
//                    mAdapter.notifyDataSetChanged();
                    break;
                    default:
                        break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initDatas();
        mTree = (ListView) findViewById(R.id.listView);
        try
        {
            mAdapter = new SimpleTreeAdapter(mTree, this, mDatas, 0);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        mFileBeanHelper = new FileBeanHelper(this);
        mDatas = mFileBeanHelper.query();
        if ( mDatas.size() >0 ){
            try{
                mAdapter = new SimpleTreeAdapter(mTree, MainActivity.this, mDatas, 0);
//                mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//                    @Override
//                    public void onClick(Node node, int position) {
//                        Log.d(TAG, "id:" + node.getId() + "pId:" + node.getpId() + "name: " + node.getName()+ "   uuid"  + node.getUuid());
//                        getEntitys(node.getUuid(),node.getId());
//                        Toast.makeText(MainActivity.this, "onclick", Toast.LENGTH_SHORT).show();
//                    }
//                });
                mTree.setAdapter(mAdapter);
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }else {
                getHttpDatas();
        }
    }


    private void getHttpDatas(){

        final String url = "http://39.108.210.48:8090/v2/entity/slaves";

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    Log.d(TAG, "----------------- 开始发数据 -----------------------");
                    Response response = client.newCall(request).execute();
                    String responseString = response.body().string();
                    Log.d(TAG, responseString);
                    parseData(responseString);
                }catch (IOException e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = MSG_ERR;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();

    }

    public void getEntitys(final String uuid, final int id){

        final String url = "http://39.108.210.48:8090/v2/entity/slaves?master_uuid=" + uuid;

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    Log.d(TAG, "----------------- 开始发数据 -----------------------");
                    Response response = client.newCall(request).execute();
                    String responseString = response.body().string();
                    Log.d(TAG, responseString);
                    parseData(responseString, id);
                }catch (IOException e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = MSG_ERR;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();

    }

    public void parseData(String response){
        try {
            List<FileBean> beans = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("statusCode");
            if (! "200".equals(status)){
                Message msg = new Message();
                msg.what = MSG_ERR;
                mHandler.sendMessage(msg);
            }else {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0;i<dataArray.length();i++){
                    JSONObject entity = dataArray.getJSONObject(i);
                    FileBean bean = new FileBean(count++,0,entity.getString("name"),entity.getString("uuid"),entity.getString("type"));
                    mDatas.add(bean);
                    beans.add(bean);
                    mFileBeanHelper.addBean(bean);
                }

                for (FileBean bean : beans){
                    getEntitys(bean.getUuid(),bean.getId());
                }

                Thread.sleep(15000);
                Message msg = new Message();
                msg.what = MSG_SUCCESS;
                mHandler.sendMessage(msg);
            }

        }catch (JSONException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void parseData(String response,int id){
        try {
            List<FileBean> beans = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("statusCode");
            if ("404005".equals(status)){
                return;
            }else if (! "200".equals(status)){
                Message msg = new Message();
                msg.what = MSG_ERR;
                mHandler.sendMessage(msg);
            }else {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                int size =mDatas.size();
                for (int i = 0;i<dataArray.length();i++){
                    JSONObject entity = dataArray.getJSONObject(i);
                    FileBean bean = new FileBean(count++,id,entity.getString("name"),entity.getString("uuid"),entity.getString("type"));
                    mDatas.add(bean);
                    beans.add(bean);
                    mFileBeanHelper.addBean(bean);
                }

                for (FileBean bean: beans){
                    getEntitys(bean.getUuid(),bean.getId());
                }
//                Message msg = new Message();
//                msg.what = MSG_SUCCESS_ENTITY;
//                mHandler.sendMessage(msg);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public TreeListViewAdapter getAdapter() {
        return mAdapter;
    }


}
