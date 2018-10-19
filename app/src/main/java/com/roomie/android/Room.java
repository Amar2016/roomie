package com.roomie.android;

import java.util.ArrayList;

public class Room {

    //private String mRoomId;
    private String mName;
    private String mRoomId;
    private ArrayList<User> mUsers;
    private ArrayList<Task> mTasks;
    public int SUCCESSFUL = 0;
    public int UNSUCCESSFUL = -1;

    public Room(String name){
        //this.mRoomId = roomId;
        this.mName = name;
    }

    public int addUserToRoom(User user){
        if(!mUsers.contains(user)) {
            mUsers.add(user);
            return SUCCESSFUL;
        }
        return UNSUCCESSFUL;
    }

    public int removeUserFromRoom(User user){
        if(mUsers.contains(user)){
            mUsers.remove(user);
            return SUCCESSFUL;
        }
        return UNSUCCESSFUL;
    }

    public int addTask(Task task){
        if(mTasks.add(task)){
            return SUCCESSFUL;
        }
        return UNSUCCESSFUL;
    }

    public int removeTask(Task task){
        if(mTasks.remove(task)){
            return SUCCESSFUL;
        }
        return UNSUCCESSFUL;
    }

    public String getmRoomId() {
        return mRoomId;
    }

    public void setmRoomId(String mRoomId) {
        this.mRoomId = mRoomId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
