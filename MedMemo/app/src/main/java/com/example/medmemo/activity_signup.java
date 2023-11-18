package com.example.medmemo;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_signup extends AppCompatActivity {
    public static final String TAG="TAG";
    EditText emailText,passwordText,nameText,ageText,genderText;
    Button buttonReg;
    FirebaseAuth mAuth;
    TextView textView;
    FirebaseFirestore fstore;
    String userId;
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if((currentUser != null) && (currentUser.isEmailVerified())){
            Toast.makeText(activity_signup.this,"Please Login",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        emailText = findViewById(R.id.usersign);
        passwordText = findViewById(R.id.passsign);
        nameText=findViewById(R.id.usrname);
        ageText=findViewById(R.id.age);
        genderText=findViewById(R.id.gender);
        textView = findViewById(R.id.logsign_btn);
        buttonReg = findViewById(R.id.sign_btn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password,username,age,gender;
                email = String.valueOf(emailText.getText());
                password = String.valueOf(passwordText.getText());
                username=String.valueOf(nameText.getText());
                age=String.valueOf(ageText.getText());
                gender=String.valueOf(genderText.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(activity_signup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(activity_signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //send verification
                                    FirebaseUser fuser=mAuth.getCurrentUser();


                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(activity_signup.this,"Verification Email has been sent!",Toast.LENGTH_SHORT).show();
                                            userId=mAuth.getCurrentUser().getUid();
                                            DocumentReference documentReference=fstore.collection("users").document(userId);
                                            Map<String,Object> user= new HashMap<>();
                                            user.put("uName",username);
                                            user.put("age",age);
                                            user.put("gender",gender);
                                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG,"onSuccess: user profile is created for"+ userId);
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG,"Email not sent "+e.getMessage());
                                        }
                                    });


                                } else {
                                    FirebaseUser fuser=mAuth.getCurrentUser();

                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(activity_signup.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}