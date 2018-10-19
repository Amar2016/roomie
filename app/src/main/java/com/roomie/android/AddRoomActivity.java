package com.roomie.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddRoomActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRoomsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRoomsDatabaseReference = mFirebaseDatabase.getReference().child("Rooms");
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        Button mSubmit = (Button) findViewById(R.id.create_room);


        mSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                EditText mText = (EditText) findViewById(R.id.room_name);
                String name = mText.getText().toString();
                FirebaseUser mCurrUser = mFirebaseAuth.getCurrentUser();
                DatabaseReference mTemp = mRoomsDatabaseReference.push();
                String roomId = mTemp.getKey();
                mTemp.setValue(new Room(name));

                //To-do make a template for this.
                mTemp.child(roomId).getRef().addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Room room = dataSnapshot.getValue(Room.class);
                            // your message is here do what you want
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("dberror",databaseError.getMessage());
                    }
                });


                User.getInstance().setmRoomId(roomId);
                User.getInstance().setmName(mCurrUser.getDisplayName());
                User.getInstance().setmEmail(mCurrUser.getEmail());
                mUsersDatabaseReference.push().setValue(User.getInstance());

                Intent myIntent = new Intent(view.getContext(), HomeActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
    }
}