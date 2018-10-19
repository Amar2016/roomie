package com.roomie.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AddTaskActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Rooms");

        Button mSubmit = (Button) findViewById(R.id.create_task);
        final EditText mName = (EditText) findViewById(R.id.task_name);
        final EditText mAssignee = (EditText)findViewById(R.id.task_asignee);

        mSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String name = mName.getText().toString();
                String assignee = mAssignee.getText().toString();

                mUsersDatabaseReference.child(User.getInstance().getmRoomId()).child("Tasks").push().setValue(new Task(name,User.getInstance().getmName(), assignee));

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }
}
