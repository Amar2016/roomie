package com.roomie.android;

import java.util.Date;

public class Task {
    private String mTitle;
    private String mAssignee;
    private String mAssigner;
    //private Date mDueDate;

    Task(){}
    Task(String title,String assignee,String assigner) {
        this.mTitle = title;
        this.mAssignee = assignee;
        this.mAssigner = assigner;
      //  this.mDueDate = date;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAssignee() {
        return mAssignee;
    }

    public void setmAssignee(String mAssignee) {
        this.mAssignee = mAssignee;
    }

    public String getmAssigner() {
        return mAssigner;
    }

    public void setmAssigner(String mAssigner) {
        this.mAssigner = mAssigner;
    }
/*
    public Date getmDueDate() {
        return mDueDate;
    }

    public void setmDueDate(Date mDueDate) {
        this.mDueDate = mDueDate;
    } */
}
