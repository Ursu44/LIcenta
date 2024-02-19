package com.example;

import com.example.firebase.FirebaseInitializer;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

public class TeacherSearch {

    @Inject
    private FirebaseInitializer firebaseInitializer;

    private DatabaseReference profesori = null;

    public TeacherSearch() {
        profesori = FirebaseDatabase.getInstance().getReference().child("Catalog");
    }

    public String search(String materie){
        final String[] profesor = {""};
        profesori.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String emailValue = snapshot.child("email").getValue(String.class);
                    if (snapshot.child("materie").getValue(String.class).equals(materie) ) {
                        profesor[0] = snapshot.child("mail").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Read operation cancelled: " + databaseError.getMessage());
            }

        });
        return profesor[0];
    }
}
