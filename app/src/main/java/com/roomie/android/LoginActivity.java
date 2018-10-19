package com.roomie.android;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private String mTempRoomId;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = findViewById(R.id.pbloading);
        /* Customizing google sigin in button on the login page */
        SignInButton signinbtn = (SignInButton) findViewById(R.id.sign_in_button);
        signinbtn.setColorScheme(SignInButton.COLOR_LIGHT);
        signinbtn.setSize(SignInButton.SIZE_WIDE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                signIn();
            }
        });

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Log.d("ROOMIE","Successful referral");
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            Log.d("ROOMIE","PendingDynamicLink");
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //
                        // If the user isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the user anonymously, and record the
                        // referrer's UID.
                        //
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedBy", false)) {
                            Log.d("ROOMIE","Referral id is this");
                            mTempRoomId = deepLink.getQueryParameter("invitedBy");
                            Log.d("ROOMIE",mTempRoomId);
                        }
                    }
                });
    }

    private void signIn() {
        // Sign out client to enable account selection
        mGoogleSignInClient.signOut();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mFirebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "You are signed in.", Toast.LENGTH_LONG).show();

                            // Add to Firebase database Users node after checking if new user
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNew && (mTempRoomId == null)){
                                // User has a single instance.
                                /* User.getInstance().setmEmail(user.getEmail().toString());
                                User.getInstance().setmName(user.getDisplayName().toString()); */
                                // mUsersDatabaseReference.push().setValue(User.getInstance());
                                Toast.makeText(LoginActivity.this, "You are signed in again.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, AddRoomActivity.class));
                            }else if (isNew && (mTempRoomId != null)){
                                //push the user with roomid
                                User.getInstance().setmRoomId(mTempRoomId);
                                User.getInstance().setmEmail(user.getEmail());
                                User.getInstance().setmName(user.getDisplayName());
                                mUsersDatabaseReference.push().setValue(User.getInstance());
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
