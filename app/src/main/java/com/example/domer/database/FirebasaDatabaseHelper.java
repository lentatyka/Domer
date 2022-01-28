package com.example.domer.database;



import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class FirebasaDatabaseHelper {
    private static DatabaseReference mReference;
    private static final String NODE_NAME = "book";
    private static FirebasaDatabaseHelper instance;

    private FirebasaDatabaseHelper(){
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebasaDatabaseHelper getInstance(){
        if(instance == null){
            instance = new FirebasaDatabaseHelper();
        }
        return instance;
    }

    public void readBook(Consumer<List<Product>> consumer)  {
        mReference.child(NODE_NAME).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Product> productList = new ArrayList<>();
                    for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        Product p = keyNode.getValue(Product.class);
                        assert p != null;
                        p.setId(Integer.parseInt(Objects.requireNonNull(keyNode.getKey())));
                        productList.add(p);
                    }
                    consumer.accept(productList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public void updateProduct(Product product, Consumer<Boolean> consumer){
        Date time = Calendar.getInstance().getTime();
        mReference
            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
            .child(time.toString())
            .child(String.valueOf(product.getId())).setValue(product)
            .addOnSuccessListener(e -> {
                if(consumer != null) consumer.accept(true);
            })
            .addOnFailureListener(e -> {
                if(consumer!=null) consumer.accept(false);
            });
    }
}