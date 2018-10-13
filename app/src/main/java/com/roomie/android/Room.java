package com.roomie.android;

import java.util.ArrayList;

public class Room {

    private String mRoomId;
    private String mName;
    private ArrayList mUsers;

    public Room(String roomId, String name){
        this.mRoomId = roomId;
        this.mName = name;
    }

}
