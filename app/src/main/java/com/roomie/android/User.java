package com.roomie.android;

import java.util.ArrayList;

public class User {
    private String mEmail;
    private String mName;
    private String mRoomId;
    private ArrayList mTasksToDo;
    private ArrayList mTasksAssignedToOthers;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String name){
        this.mEmail = email;
        this.mName = name;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmRoomId() {
        return mRoomId;
    }

    public void setmRoomId(String mRoomId) {
        this.mRoomId = mRoomId;
    }

    public ArrayList getmTasksToDo() {
        return mTasksToDo;
    }

    public void setmTasksToDo(ArrayList mTasksToDo) {
        this.mTasksToDo = mTasksToDo;
    }

    public ArrayList getmTasksAssignedToOthers() {
        return mTasksAssignedToOthers;
    }

    public void setmTasksAssignedToOthers(ArrayList mTasksAssignedToOthers) {
        this.mTasksAssignedToOthers = mTasksAssignedToOthers;
    }
}
