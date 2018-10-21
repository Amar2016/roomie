package com.roomie.android;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Room {

    //private String mRoomId;
    private String mName;
    private ArrayList<String> mUsers;
    private ArrayList<Task> mTasks;

    public Room(String name){
        //this.mRoomId = roomId;
        this.mName = name;
        mTasks = new ArrayList<>();
        mUsers = new ArrayList<>();
    }

    public Room(){
        mTasks = new ArrayList<>();
        mUsers = new ArrayList<>();
    }

    public void addUserToRoom(String uid, Object obj) {
            if(!mUsers.contains(uid)){
                mUsers.add(uid);
            }else{
                //Add already exist toast
            }
        }


    public void removeUserFromRoom(String uid){
        mUsers.remove(uid);
    }

    public int addTask(Task task){
        if(mTasks.add(task)){
            return Utility.SUCCESSFUL;
        }
        return Utility.UNSUCCESSFUL;
    }

    public int removeTask(Task task){
        if(mTasks.remove(task)){
            return Utility.SUCCESSFUL;
        }
        return Utility.UNSUCCESSFUL;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ArrayList<String> getmUsers(){
        return this.mUsers;
    }

    public void setmUsers(ArrayList<String> mUsers){
        this.mUsers = mUsers;
    }

    public ArrayList<Task> getmTasks(){
        return mTasks;
    }

    public void setmTasks(ArrayList<Task> mTasks){
        this.mTasks = mTasks;
    }

}
