package com.roomie.android;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;



public class HomeActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textName, textEmail;
    FirebaseAuth mAuth;

    private Uri mInvitationUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.imageView);
        textName = findViewById(R.id.textViewName);
        textEmail = findViewById(R.id.textViewEmail);
        FirebaseUser user = mAuth.getCurrentUser();
        Glide.with(this).load(user.getPhotoUrl()).into(imageView);
        textName.setText(user.getDisplayName());
        textEmail.setText(user.getEmail());

        final Button button = (Button)findViewById(R.id.add_task);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("Amit","on add task");
                Intent myIntent = new Intent(v.getContext(), AddTaskActivity.class);
                startActivityForResult(myIntent, 0);

            }
        });

        Button button2 = (Button)findViewById(R.id.add_roommate);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("Amit","on add task");

                String shareUri = createShareUri(FirebaseAuth.getInstance().getCurrentUser().getUid());
                sendInvitation(createLink(shareUri));
            }
        });
    }

    public String createShareUri(String invitedBy){
        return "https://roomie.com/rTCx/?invitedBy="+invitedBy;
    }

    public Uri createLink(String myUri) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(myUri))
                .setDynamicLinkDomain("roomieoye.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        return dynamicLink.getUri();
    }

    public void sendInvitation(Uri linkUri){
        //Shorten link and launch send email intent
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(linkUri)
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            String subject = String.format("%s wants you to play MyExampleGame!", referrerName);
                            String msg = "Let's play MyExampleGame together! Use my referrer link: "
                                    + shortLink.toString();
                            String msgHtml = String.format("<p>Let's play MyExampleGame together! Use my "
                                    + "<a href=\"%s\">referrer link</a>!</p>", shortLink.toString());

                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            intent.putExtra(Intent.EXTRA_TEXT, msg);
                            intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        } else {
                            // Error
                            // ...
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the user is not logged in
        //opening the login activity
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}