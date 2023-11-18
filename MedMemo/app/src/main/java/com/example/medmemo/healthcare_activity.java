package com.example.medmemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class healthcare_activity extends AppCompatActivity {
    TextView name,age,gender;
    public static final String TAG="TAG";
    private EditText searchEditText;
    private ImageButton searchBtn;
    private TextView resultTextView;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String userId;
    ImageButton speech;
    TextToSpeech speechBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcare);
        name=findViewById(R.id.nameuser);

        mAuth= FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        userId=mAuth.getCurrentUser().getUid();

        DocumentReference documentReference= fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("uName")+"!");

            }
        });
        searchEditText = findViewById(R.id.searchEditText);
        speech=findViewById(R.id.speaker);
        resultTextView = findViewById(R.id.resultTextView);

        searchBtn=findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicineName = searchEditText.getText().toString();
                speech.setVisibility(View.VISIBLE);
                searchMedicineUsage(medicineName);
            }
        });


    }

    private void searchMedicineUsage(String medicineName) {
        CollectionReference medicinesRef = fstore.collection("medicines");
        Query query = medicinesRef.whereEqualTo("name", medicineName);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String medicineUsage = document.getString("usage");
                        resultTextView.setText(medicineUsage);
                        speechBtn=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    speechBtn.setLanguage(Locale.UK);
                                }
                            }
                        });
                        speech.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getBaseContext(),medicineUsage, Toast.LENGTH_LONG).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    speechBtn.speak(medicineUsage, TextToSpeech.QUEUE_FLUSH, null, null);
                                } else {
                                    speechBtn.speak(medicineUsage, TextToSpeech.QUEUE_FLUSH, null);
                                }

                            }
                        });

                        }
                        return;

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                // Handle the case when no document with the given medicine name is found
                resultTextView.setText("No usage information found for this medicine.");
            }
        });
    }


}






