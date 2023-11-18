package com.example.medmemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class mymeds extends AppCompatActivity {
    FloatingActionButton addmedbtn;
    RecyclerView recyclerView;

    MedAdapter medAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymeds);
        addmedbtn = findViewById(R.id.add_med_btn);
        recyclerView = findViewById(R.id.recycler_view);

        addmedbtn.setOnClickListener((v) -> startActivity(new Intent(mymeds.this, MedDetailsActivity.class)));

        setupRecyclerView();
    }
    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForMeds();
        FirestoreRecyclerOptions<Med> options = new FirestoreRecyclerOptions.Builder<Med>().setQuery(query, Med.class).build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medAdapter = new MedAdapter(options, this);
        recyclerView.setAdapter(medAdapter);

    }

    @Override
    protected void onStart() {

        try {
            super.onStart();
            medAdapter.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        try {
            super.onResume();

            medAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
            medAdapter.stopListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





