package com.example.medmemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class MedDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveMedbtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode=false;
    TextView deleteMedTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_details);

        titleEditText=findViewById(R.id.meds_title_text);
        contentEditText=findViewById(R.id.meds_content_text);
        saveMedbtn=findViewById(R.id.save_med_btn);
        pageTitleTextView=findViewById(R.id.page_title);
        deleteMedTextViewBtn=findViewById(R.id.delete_med_text_view_btn);

        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if(isEditMode){
            pageTitleTextView.setText("Edit your Medicine");
            deleteMedTextViewBtn.setVisibility(View.VISIBLE);
        }


        saveMedbtn.setOnClickListener( (v) -> saveMed() );
        deleteMedTextViewBtn.setOnClickListener((v) -> deleteMedFromFirebase());

    }
    void saveMed(){
        String medTitle=titleEditText.getText().toString();
        String medContent=contentEditText.getText().toString();
        if(medTitle==null || medTitle.isEmpty()){
            titleEditText.setError("Medicine Name is required");
            return;
        }
        Med med=new Med();
        med.setTitle(medTitle);
        med.setContent(medContent);


        saveMedToFirebase(med);
    }

    void saveMedToFirebase(Med med){
        DocumentReference documentReference;
        if(isEditMode){
            documentReference = Utility.getCollectionReferenceForMeds().document(docId);
        }
        else {
            documentReference = Utility.getCollectionReferenceForMeds().document();
        }
        documentReference.set(med).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(MedDetailsActivity.this, "Medicine added successfully");
                    finish();
                } else {
                    Utility.showToast(MedDetailsActivity.this, "Failed in adding medicine");
                }
            }
        });

    }

    void deleteMedFromFirebase(){
        DocumentReference documentReference;
            documentReference = Utility.getCollectionReferenceForMeds().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(MedDetailsActivity.this, "Medicine deleted successfully");
                    finish();
                } else {
                    Utility.showToast(MedDetailsActivity.this, "Failed while deleting medicine");
                }
            }
        });
    }
}