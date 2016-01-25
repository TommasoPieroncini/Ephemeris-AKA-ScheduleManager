package com.schedulemanager.tommaso.schedulemanager;

import android.app.Application;

public class MyApplication extends Application {

    private String username;
    private String password;

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }
}
