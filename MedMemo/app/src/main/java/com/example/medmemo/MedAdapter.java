package com.example.medmemo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MedAdapter extends FirestoreRecyclerAdapter<Med,MedAdapter.MedViewHolder> {
    Context context;



    public MedAdapter(@NonNull FirestoreRecyclerOptions<Med> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MedViewHolder holder, int position, @NonNull Med med) {
        holder.titleTextView.setText(med.title);
        holder.contentTextView.setText(med.content);
        holder.itemView.setOnClickListener((v)-> {
            Intent intent=new Intent(context,MedDetailsActivity.class);
            intent.putExtra("title",med.title);
            intent.putExtra("content",med.content);
            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);

        });

    }

    @NonNull
    @Override
    public MedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_med_item,parent,false);
        return new MedViewHolder(view);
    }

    class MedViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView;
        public MedViewHolder(View itemView)
        {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.med_title_text_view);
            contentTextView=itemView.findViewById(R.id.med_content_text_view);


        }
    }
}
