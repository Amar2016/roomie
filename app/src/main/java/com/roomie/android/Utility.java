package com.roomie.android;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Utility {
    final public static int SUCCESSFUL = 0;
    final public static int UNSUCCESSFUL = -1;

    Utility(){}

    /* Adds a new user to firebase database and return the new user key */
    public static String createNewUser(String mTempRoomId, FirebaseUser user){
        User.getInstance().setmRoomId(mTempRoomId);
        User.getInstance().setmEmail(user.getEmail());
        User.getInstance().setmName(user.getDisplayName());
        DatabaseReference mTempUser = FirebaseDatabase.getInstance().getReference().child("Users").push();
        final String userID = mTempUser.getKey();
        mTempUser.setValue(User.getInstance());
        return userID;
    }

    /* Adds a new room to firebase database and return the room key*/
    public static String createNewRoom(String name){
        Room room = new Room(name);
        DatabaseReference mTempRoom = FirebaseDatabase.getInstance().getReference().child("Rooms").push();
        final String roomID = mTempRoom.getKey();
        mTempRoom.setValue(room);
        return roomID;
    }

    public static void addUserToRoom(final String mTempRoomId,final String userID){
        DatabaseReference mTemp = FirebaseDatabase.getInstance().getReference().child("Rooms").child(mTempRoomId);
        final Room[] rooms= new Room[1] ;

        mTemp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Roomie2",dataSnapshot.getKey());
                Log.d("Roomie3","Inside database snapshot");
                Room room = dataSnapshot.getValue(Room.class);
                Log.d("Roomie4",room.getmName());
                for(String user : room.getmUsers()){
                    Log.d("Roomie5",user);
                }
                room.addUserToRoom(userID,this);
                DatabaseReference mTemp1 = FirebaseDatabase.getInstance().getReference("Rooms").child(mTempRoomId);
                mTemp1.setValue(room);
                // your message is here do what you wan
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("dberror",databaseError.getMessage());
            }
        });

    }
}
