package com.example.medmemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class home extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    Button button,alarm_btn,med_btn,health_btn,track;
    TextView textView,verifyMsg;
    FirebaseUser user;
    String userId;
    Button resendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logoutbtn);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        alarm_btn= findViewById(R.id.alarmbtn);
        fstore= FirebaseFirestore.getInstance();



        userId=auth.getCurrentUser().getUid();
        FirebaseUser user=auth.getCurrentUser();



        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(getApplicationContext(),Alarm.class);
                startActivity(intent3);
            }
        });
        med_btn=findViewById(R.id.medsbtn);
        med_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(getApplicationContext(),mymeds.class);
                startActivity(intent2);
            }
        });
        health_btn=findViewById(R.id.healthbtn);
        health_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),healthcare_activity.class);
                startActivity(intent1);

            }
        });







        if(user==null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            auth= FirebaseAuth.getInstance();
            fstore=FirebaseFirestore.getInstance();

            userId=auth.getCurrentUser().getUid();

            DocumentReference documentReference= fstore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    textView.setText(documentSnapshot.getString("uName")+"!");
                }
            });
            //textView.setText(user.getEmail());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


    }
}