package com.example.liwj.treelistviewdemo.bean;

import android.print.PrinterId;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FileBean {

    @NotNull
    private int id;

    @NotNull
    private int parentId;

    @NotNull
    private String name;

    @Id
    private String uuid;

    @NotNull
    private String type;

    @Generated(hash = 739426542)
    public FileBean(int id, int parentId, @NotNull String name, String uuid,
            @NotNull String type) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.uuid = uuid;
        this.type = type;
    }

    @Generated(hash = 1910776192)
    public FileBean() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
}
