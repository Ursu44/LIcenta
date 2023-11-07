package com.example.repository;

import com.example.firebase.FirebaseInitializer;
import com.example.model.Profesor;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

public class ProfesorRepository implements FireBaseRepository<Profesor>{

    @Inject
    private FirebaseInitializer firebaseInitializer;
    private DatabaseReference profesor = null;

    @Inject
    public ProfesorRepository() {
        profesor = FirebaseDatabase.getInstance().getReference("Profesori");
    }

    @Override
    public void create(Profesor entity) {
        DatabaseReference profesorNou = profesor.push();
        profesorNou.setValue(entity, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    System.out.println("Data saved successfully");
                } else {
                    System.err.println("Data could not be saved. " + databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void read() {
        profesor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(snapshot.getKey() + ": " + snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Citirea a esuat: " + databaseError.getCode());
            }
        });
    }
}