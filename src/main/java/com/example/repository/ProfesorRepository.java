package com.example.repository;

import com.example.firebase.FirebaseInitializer;
import com.example.model.Profesor;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

public class ProfesorRepository extends AbstractFirebasRepository<Profesor>{
    @Inject
    private FirebaseInitializer firebaseInitializer;
    private DatabaseReference profesor = null;

    @Inject
    public ProfesorRepository() {
        super("Profesori");
        profesor = FirebaseDatabase.getInstance().getReference("Profesori");
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

    @Override
    public void update(Profesor entity, String identifier) {
        profesor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    protected String getEmailFromEntity(Profesor entity) {
        return entity.getMail();
    }

    @Override
    protected String getRoleFromEntity(Profesor entity) {
        return entity.getRol();
    }

    @Override
    public void updateConfirmation(String token) {

    }


}