package com.schedulemanager.tommaso.schedulemanager;

import android.app.Application;

public class MyApplication extends Application {

    private String username;
    private String password;
    private Integer numCourses;
    private Integer numSchedules;
    private Integer schedVar;

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

    public void setNumCourses (Integer numCourses){
        this.numCourses = numCourses;
    }

    public Integer getNumCourses (){
        return this.numCourses;
    }

    public void setNumSchedules (Integer numSchedules){
        this.numSchedules = numSchedules;
    }

    public Integer getNumSchedules (){
        return this.numSchedules;
    }

    public void setSchedVar(Integer schedVar){
        this.schedVar = schedVar;
    }

    public Integer getSchedVar(){
        if (this.schedVar != null) {
            return this.schedVar;
        } else {
            return 0;
        }
    }

}
