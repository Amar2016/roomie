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

    // Adds a new user to firebase database and return the new user id
    public static String createNewUser(String roomId, FirebaseUser user){
        User.getInstance().setmRoomId(roomId);
        User.getInstance().setmEmail(user.getEmail());
        User.getInstance().setmName(user.getDisplayName());
        DatabaseReference newUserRef = FirebaseDatabase.getInstance().getReference().child("Users").push();
        final String userID = newUserRef.getKey();
        newUserRef.setValue(User.getInstance());
        return userID;
    }

    // Adds a new room to firebase database and returns the new room id
    public static String createNewRoom(String roomName){
        Room room = new Room(roomName);
        DatabaseReference mTempRoom = FirebaseDatabase.getInstance().getReference().child("Rooms").push();
        final String roomID = mTempRoom.getKey();
        mTempRoom.setValue(room);
        return roomID;
    }

    public static void addUserToRoom(final String roomId,final String userID){
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("Rooms").child(roomId);

        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Roomie2", dataSnapshot.getKey());
                Log.d("Roomie3", "Inside database snapshot");
                Room room = dataSnapshot.getValue(Room.class);
                Log.d("Roomie4", room.getmName());
                for(String user : room.getmUsers()){
                    Log.d("Roomie5", user);
                }
                room.addUserToRoom(userID,this);
                DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("Rooms").child(roomId);
                tempRef.setValue(room);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("dberror", databaseError.getMessage());
            }
        });

    }
}
