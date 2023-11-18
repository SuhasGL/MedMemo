bpackage com.example.medmemo;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility{
    static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForMeds(){
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("meds").document(currentUser.getUid()).collection("my_meds");

    }


}
